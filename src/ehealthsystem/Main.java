package ehealthsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Loads Home.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        primaryStage.setTitle("e-Health Management System");
        primaryStage.setScene(new Scene(root, 1550, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
