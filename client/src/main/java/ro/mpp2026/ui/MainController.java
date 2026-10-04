package ro.mpp2026.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import ro.mpp2026.StartApplication;
import ro.mpp2026.model.User;
import ro.mpp2026.service.IContestObserver;
import ro.mpp2026.service.IContestServices;
import ro.mpp2026.service.ServiceException;
import ro.mpp2026.service.dto.ChildRegistrationDTO;
import ro.mpp2026.service.dto.EventParticipantsDTO;

import java.util.ArrayList;
import java.util.List;

public class MainController implements IContestObserver {
    private IContestServices service;
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
    private ComboBox<String> searchDistanceCombo;

    @FXML
    private TextField nameField;
    @FXML
    private TextField cnpField;
    @FXML
    private TextField ageField;

    @FXML
    private TextField modifyCnpField;

    @FXML
    private TableView<SelectableEventRow> registerEventsTable;
    @FXML
    private TableColumn<SelectableEventRow, Boolean> registerSelectColumn;
    @FXML
    private TableColumn<SelectableEventRow, String> registerEventNameColumn;
    @FXML
    private TableColumn<SelectableEventRow, String> registerAgeColumn;

    @FXML
    private TableView<SelectableEventRow> modifyEventsTable;
    @FXML
    private TableColumn<SelectableEventRow, Boolean> modifySelectColumn;
    @FXML
    private TableColumn<SelectableEventRow, String> modifyEventNameColumn;
    @FXML
    private TableColumn<SelectableEventRow, String> modifyAgeColumn;

    public void setService(IContestServices service, User user) {
        this.service = service;
        this.currentUser = user;

        welcomeLabel.setText("Logged in as: " + user.getUsername() + " | Office: " + user.getOffice());

        initTables();
        initControls();
        loadEvents();
        loadSelectableTables();
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

        registerEventsTable.setEditable(true);
        registerSelectColumn.setEditable(true);
        registerSelectColumn.setSortable(false);

        registerSelectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        registerSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(registerSelectColumn));

        registerEventNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEventName()));
        registerAgeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAgeGroup()));

        modifyEventsTable.setEditable(true);
        modifySelectColumn.setEditable(true);
        modifySelectColumn.setSortable(false);

        modifySelectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        modifySelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(modifySelectColumn));

        modifyEventNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEventName()));
        modifyAgeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAgeGroup()));

        eventsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadChildrenForEvent(newValue.getEventId());
            }
        });
    }

    private void initControls() {
        searchAgeGroupCombo.setItems(FXCollections.observableArrayList("6-8", "9-11", "12-15"));
        searchDistanceCombo.setItems(FXCollections.observableArrayList("50m", "100m", "1000m", "1500m"));
    }

    private void loadSelectableTables() {
        List<EventParticipantsDTO> events = service.getAllEventsWithParticipantsCount();

        List<SelectableEventRow> registerRows = new ArrayList<SelectableEventRow>();
        List<SelectableEventRow> modifyRows = new ArrayList<SelectableEventRow>();

        int i;
        for (i = 0; i < events.size(); i++) {
            EventParticipantsDTO dto = events.get(i);
            SelectableEventRow row1 = new SelectableEventRow(dto.getEventId(), dto.getEventName(), dto.getMinAge() + "-" + dto.getMaxAge());
            SelectableEventRow row2 = new SelectableEventRow(dto.getEventId(), dto.getEventName(), dto.getMinAge() + "-" + dto.getMaxAge());
            registerRows.add(row1);
            modifyRows.add(row2);
        }

        registerEventsTable.setItems(FXCollections.observableArrayList(registerRows));
        modifyEventsTable.setItems(FXCollections.observableArrayList(modifyRows));
    }

    @FXML
    private void handleRefresh() {
        try {
            Long selectedEventId = getSelectedEventId();
            loadEvents();
            loadSelectableTables();

            if (selectedEventId != null) {
                reselectEventById(selectedEventId.longValue());
                loadChildrenForEvent(selectedEventId.longValue());
            } else {
                childrenTable.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception e) {
            showError("Refresh error", e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        try {
            String ageGroup = searchAgeGroupCombo.getValue();
            String distanceValue = searchDistanceCombo.getValue();

            if (ageGroup == null || ageGroup.trim().isEmpty()) {
                throw new ServiceException("Select age group.");
            }
            if (distanceValue == null || distanceValue.trim().isEmpty()) {
                throw new ServiceException("Select distance.");
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

            EventParticipantsDTO event = findEventByCriteria(distanceValue, minAge, maxAge);
            if (event == null) {
                throw new ServiceException("No event found for selected filters.");
            }

            reselectEventById(event.getEventId());

            List<ChildRegistrationDTO> result = service.searchChildren(event.getEventId(), minAge, maxAge);
            childrenTable.setItems(FXCollections.observableArrayList(result));

        } catch (ServiceException e) {
            showError("Search error", e.getMessage());
        } catch (Exception e) {
            showError("Application error", e.getMessage());
        }
    }

    @FXML
    private void handleShowSelectedEventChildren() {
        try {
            EventParticipantsDTO selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
            if (selectedEvent == null) {
                throw new ServiceException("Select an event from the table.");
            }
            loadChildrenForEvent(selectedEvent.getEventId());
        } catch (ServiceException e) {
            showError("Load error", e.getMessage());
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
            List<Long> eventIds = getCheckedEventIds(registerEventsTable);

            service.registerChild(name, cnp, age, eventIds);

            showInfo("Success", "Child registered successfully.");
            clearRegisterFields();
            clearSelections(registerEventsTable);

            Long selectedEventId = getSelectedEventId();
            loadEvents();
            loadSelectableTables();

            if (selectedEventId != null) {
                reselectEventById(selectedEventId.longValue());
                loadChildrenForEvent(selectedEventId.longValue());
            }

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

            if (cnp == null || cnp.trim().isEmpty()) {
                throw new ServiceException("Child CNP cannot be empty.");
            }

            ro.mpp2026.service.dto.ChildDTO child = service.getChildByCnp(cnp);
            List<Long> selectedEventIds = service.getEventIdsForChild(cnp);
            List<EventParticipantsDTO> allEvents = service.getAllEventsWithParticipantsCount();

            List<SelectableEventRow> rows = new ArrayList<SelectableEventRow>();

            int i;
            for (i = 0; i < allEvents.size(); i++) {
                EventParticipantsDTO dto = allEvents.get(i);

                if (child.getAge() >= dto.getMinAge() && child.getAge() <= dto.getMaxAge()) {
                    SelectableEventRow row = new SelectableEventRow(
                            dto.getEventId(),
                            dto.getEventName(),
                            dto.getMinAge() + "-" + dto.getMaxAge()
                    );

                    if (selectedEventIds.contains(dto.getEventId())) {
                        row.setSelected(true);
                    }

                    rows.add(row);
                }
            }

            modifyEventsTable.setItems(FXCollections.observableArrayList(rows));
            modifyEventsTable.refresh();

            showInfo("Info", "Child events loaded successfully for age " + child.getAge() + ".");

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
            List<Long> eventIds = getCheckedEventIds(modifyEventsTable);

            service.updateChildRegistrations(cnp, eventIds);

            showInfo("Success", "Registrations updated successfully.");

            Long selectedEventId = getSelectedEventId();
            loadEvents();
            loadSelectableTables();

            if (selectedEventId != null) {
                reselectEventById(selectedEventId.longValue());
                loadChildrenForEvent(selectedEventId.longValue());
            }

        } catch (ServiceException e) {
            showError("Modify error", e.getMessage());
        } catch (Exception e) {
            showError("Application error", e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            service.logout(currentUser, this);

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

    private void loadChildrenForEvent(long eventId) {
        List<ChildRegistrationDTO> children = service.getChildrenForEvent(eventId);
        childrenTable.setItems(FXCollections.observableArrayList(children));
    }

    private EventParticipantsDTO findEventByCriteria(String distanceValue, int minAge, int maxAge) {
        List<EventParticipantsDTO> events = eventsTable.getItems();

        int distance = Integer.parseInt(distanceValue.replace("m", ""));

        int i;
        for (i = 0; i < events.size(); i++) {
            EventParticipantsDTO dto = events.get(i);
            if (dto.getDistance() == distance && dto.getMinAge() == minAge && dto.getMaxAge() == maxAge) {
                return dto;
            }
        }
        return null;
    }

    private List<Long> getCheckedEventIds(TableView<SelectableEventRow> table) {
        List<Long> result = new ArrayList<Long>();

        int i;
        for (i = 0; i < table.getItems().size(); i++) {
            SelectableEventRow row = table.getItems().get(i);
            if (row.isSelected()) {
                result.add(row.getEventId());
            }
        }

        return result;
    }

    private void clearSelections(TableView<SelectableEventRow> table) {
        int i;
        for (i = 0; i < table.getItems().size(); i++) {
            table.getItems().get(i).setSelected(false);
        }
        table.refresh();
    }

    private Long getSelectedEventId() {
        EventParticipantsDTO selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            return null;
        }
        return selectedEvent.getEventId();
    }

    private void reselectEventById(long eventId) {
        int i;
        for (i = 0; i < eventsTable.getItems().size(); i++) {
            EventParticipantsDTO dto = eventsTable.getItems().get(i);
            if (dto.getEventId() == eventId) {
                eventsTable.getSelectionModel().clearSelection();
                eventsTable.getSelectionModel().select(i);
                eventsTable.scrollTo(i);
                return;
            }
        }
    }

    private String buildEventText(EventParticipantsDTO dto) {
        return dto.getEventName() + " | " + dto.getDistance() + "m | ages " + dto.getMinAge() + "-" + dto.getMaxAge();
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

    @Override
    public void contestDataUpdated() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Long selectedEventId = getSelectedEventId();
                    loadEvents();
                    loadSelectableTables();

                    if (selectedEventId != null) {
                        reselectEventById(selectedEventId.longValue());
                        loadChildrenForEvent(selectedEventId.longValue());
                    } else {
                        childrenTable.setItems(FXCollections.observableArrayList());
                    }
                } catch (Exception e) {
                    showError("Update error", e.getMessage());
                }
            }
        });
    }
}