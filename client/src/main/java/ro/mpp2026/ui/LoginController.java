package ro.mpp2026.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ro.mpp2026.StartApplication;
import ro.mpp2026.model.User;
import ro.mpp2026.service.IContestServices;
import ro.mpp2026.service.ServiceException;

public class LoginController {
    private IContestServices service;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void setService(IContestServices service) {
        this.service = service;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("/main-view.fxml"));
            Scene scene = new Scene(loader.load(), 900, 700);
            MainController mainController = loader.getController();
            User user = service.login(username, password, mainController);
            mainController.setService(service, user);

            Stage stage = new Stage();
            stage.setTitle("MPP Contest - Main");
            stage.setScene(scene);
            stage.setMinWidth(850);
            stage.setMinHeight(650);
            stage.centerOnScreen();
            stage.show();

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();

        } catch (ServiceException e) {
            showError("Login error", e.getMessage());
        } catch (Exception e) {
            showError("Application error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}