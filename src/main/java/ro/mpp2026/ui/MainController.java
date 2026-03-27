package ro.mpp2026.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.mpp2026.StartApplication;
import ro.mpp2026.model.User;
import ro.mpp2026.service.ContestService;
import ro.mpp2026.service.ServiceException;
import ro.mpp2026.service.dto.ChildRegistrationDTO;
import ro.mpp2026.service.dto.EventParticipantsDTO;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    private ContestService service;
    private User currentUser;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<EventParticipantsDTO> eventsTable;

    @FXML
    private TableColumn<EventParticipantsDTO, Number> eventIdColumn;

    @FXML
    private TableColumn<EventParticipantsDTO, String> eventNameColumn;

    @FXML
    private TableColumn<EventParticipantsDTO, Number> eventMinAgeColumn;

    @FXML
    private TableColumn<EventParticipantsDTO, Number> eventMaxAgeColumn;

    @FXML
    private TableColumn<EventParticipantsDTO, Number> eventParticipantsColumn;

    @FXML
    private TableView<ChildRegistrationDTO> childrenTable;

    @FXML
    private TableColumn<ChildRegistrationDTO, Number> childIdColumn;

    @FXML
    private TableColumn<ChildRegistrationDTO, String> childNameColumn;

    @FXML
    private TableColumn<ChildRegistrationDTO, String> childCnpColumn;

    @FXML
    private TableColumn<ChildRegistrationDTO, Number> childAgeColumn;

    @FXML
    private TableColumn<ChildRegistrationDTO, String> childEventsColumn;

    @FXML
    private ComboBox<String> searchAgeGroupCombo;

    @FXML
    private TextField nameField;

    @FXML
    private TextField cnpField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField modifyCnpField;

    public void setService(ContestService service, User user) {
        this.service = service;
        this.currentUser = user;

        welcomeLabel.setText("Logged in as: " + user.getUsername() + " | Office: " + user.getOffice());

        initTables();
        initControls();
        loadEvents();
    }

    private void initTables() {
        eventIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getEventId()));
        eventNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(buildEventText(cellData.getValue())));
        eventMinAgeColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getMinAge()));
        eventMaxAgeColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getMaxAge()));
        eventParticipantsColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getParticipantsCount()));

        childIdColumn.setCellValueFactory(cellData ->
                new SimpleLongProperty(cellData.getValue().getChildId()));
        childNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getChildName()));
        childCnpColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCnp()));
        childAgeColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAge()));
        childEventsColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEvents()));

        eventsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        eventsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadChildrenForSelectedEvent();
            }
        });
    }

    private void initControls() {
        searchAgeGroupCombo.setItems(FXCollections.observableArrayList(
                "6-8",
                "9-11",
                "12-15"
        ));
    }

    @FXML
    private void handleRefresh() {
        loadEvents();
        loadChildrenForSelectedEvent();
    }

    @FXML
    private void handleSearch() {
        try {
            EventParticipantsDTO selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
            if (selectedEvent == null) {
                throw new ServiceException("Selecteaza o proba.");
            }

            String ageGroup = searchAgeGroupCombo.getValue();
            if (ageGroup == null || ageGroup.trim().isEmpty()) {
                throw new ServiceException("Selecteaza grupa de varsta.");
            }

            int minAge;
            int maxAge;

            if (ageGroup.equals("6-8")) {
                minAge = 6;
                maxAge = 8;
            } else if (ageGroup.equals("9-11")) {
                minAge = 9;
                maxAge = 11;
            } else {
                minAge = 12;
                maxAge = 15;
            }

            List<ChildRegistrationDTO> result = service.searchChildren(selectedEvent.getEventId(), minAge, maxAge);
            childrenTable.setItems(FXCollections.observableArrayList(result));

        } catch (ServiceException e) {
            showError("Search error", e.getMessage());
        } catch (Exception e) {
            showError("Application error", e.getMessage());
        }
    }

    @FXML
    private void handleRegisterChild() {
        try {
            String name = nameField.getText();
            String cnp = cnpField.getText();
            String ageText = ageField.getText();

            if (ageText == null || ageText.trim().isEmpty()) {
                throw new ServiceException("Age cannot be empty.");
            }

            int age = Integer.parseInt(ageText);

            List<EventParticipantsDTO> selectedEvents = eventsTable.getSelectionModel().getSelectedItems();
            List<Long> eventIds = new ArrayList<Long>();

            int i;
            for (i = 0; i < selectedEvents.size(); i++) {
                eventIds.add(selectedEvents.get(i).getEventId());
            }

            service.registerChild(name, cnp, age, eventIds);

            showInfo("Success", "Child registered successfully.");
            clearRegisterFields();
            loadEvents();
            loadChildrenForSelectedEvent();

        } catch (NumberFormatException e) {
            showError("Validation error", "Age must be a valid number.");
        } catch (ServiceException e) {
            showError("Registration error", e.getMessage());
        } catch (Exception e) {
            showError("Application error", e.getMessage());
        }
    }

    @FXML
    private void handleLoadChildEvents() {
        try {
            String cnp = modifyCnpField.getText();
            List<Long> eventIds = service.getEventIdsForChild(cnp);

            eventsTable.getSelectionModel().clearSelection();

            int i;
            for (i = 0; i < eventsTable.getItems().size(); i++) {
                EventParticipantsDTO dto = eventsTable.getItems().get(i);
                if (eventIds.contains(dto.getEventId())) {
                    eventsTable.getSelectionModel().select(i);
                }
            }

            showInfo("Info", "Probele copilului au fost incarcate. Poti modifica selectia.");

        } catch (ServiceException e) {
            showError("Load error", e.getMessage());
        } catch (Exception e) {
            showError("Application error", e.getMessage());
        }
    }

    @FXML
    private void handleModifyChild() {
        try {
            String cnp = modifyCnpField.getText();

            List<EventParticipantsDTO> selectedEvents = eventsTable.getSelectionModel().getSelectedItems();
            List<Long> eventIds = new ArrayList<Long>();

            int i;
            for (i = 0; i < selectedEvents.size(); i++) {
                eventIds.add(selectedEvents.get(i).getEventId());
            }

            service.updateChildRegistrations(cnp, eventIds);

            showInfo("Success", "Inscrierile copilului au fost modificate.");
            loadEvents();
            loadChildrenForSelectedEvent();

        } catch (ServiceException e) {
            showError("Modify error", e.getMessage());
        } catch (Exception e) {
            showError("Application error", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("/login-view.fxml"));
            Scene scene = new Scene(loader.load());

            LoginController controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            stage.setTitle("MPP Contest - Login");
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            showError("Logout error", e.getMessage());
        }
    }

    private void loadEvents() {
        List<EventParticipantsDTO> events = service.getAllEventsWithParticipantsCount();
        eventsTable.setItems(FXCollections.observableArrayList(events));
    }

    private void loadChildrenForSelectedEvent() {
        EventParticipantsDTO selectedEvent = eventsTable.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            childrenTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<ChildRegistrationDTO> children = service.getChildrenForEvent(selectedEvent.getEventId());
        childrenTable.setItems(FXCollections.observableArrayList(children));
    }

    private String buildEventText(EventParticipantsDTO dto) {
        return dto.getEventName() + " | ages " + dto.getMinAge() + "-" + dto.getMaxAge();
    }

    private void clearRegisterFields() {
        nameField.clear();
        cnpField.clear();
        ageField.clear();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}