package ro.mpp2026;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2026.network.objectprotocol.ContestServicesObjectProxy;
import ro.mpp2026.service.IContestServices;
import ro.mpp2026.ui.LoginController;

import java.io.InputStream;
import java.util.Properties;

public class StartApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties properties = new Properties();
        try (InputStream input = StartApplication.class.getClassLoader().getResourceAsStream("server.properties")) {
            if (input != null) {
                properties.load(input);
            }
        }

        String host = properties.getProperty("server.host", "127.0.0.1");
        int port = Integer.parseInt(properties.getProperty("server.port", "55556"));

        IContestServices service = new ContestServicesObjectProxy(host, port);

        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("/login-view.fxml"));
        Scene scene = new Scene(loader.load(), 420, 260);

        LoginController controller = loader.getController();
        controller.setService(service);

        primaryStage.setTitle("MPP Contest - Login");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(420);
        primaryStage.setMinHeight(260);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}