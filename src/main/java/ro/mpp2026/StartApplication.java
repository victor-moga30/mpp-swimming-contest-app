package ro.mpp2026;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2026.repository.db.ChildDbRepository;
import ro.mpp2026.repository.db.EventDbRepository;
import ro.mpp2026.repository.db.JdbcUtils;
import ro.mpp2026.repository.db.RegistrationDbRepository;
import ro.mpp2026.repository.db.UserDbRepository;
import ro.mpp2026.service.ContestService;
import ro.mpp2026.ui.LoginController;

public class StartApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        JdbcUtils jdbcUtils = new JdbcUtils();

        ContestService service = new ContestService(
                new UserDbRepository(jdbcUtils),
                new ChildDbRepository(jdbcUtils),
                new EventDbRepository(jdbcUtils),
                new RegistrationDbRepository(jdbcUtils)
        );

        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource("/login-view.fxml"));
        Scene scene = new Scene(loader.load());

        LoginController controller = loader.getController();
        controller.setService(service);

        primaryStage.setTitle("MPP Contest - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}