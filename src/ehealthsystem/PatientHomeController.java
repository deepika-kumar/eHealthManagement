package ehealthsystem;

import ehealthsystem.model.PayLoad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class PatientHomeController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Label patientName;
    @FXML
    private PayLoad payLoad;
    @FXML
    private StackPane centreStackPane;

    public void initialize(){
        Controller displayScreen = new Controller();
        displayScreen.homeScreen(centreStackPane);
    }

    // Display patient home screen
    public void showPatientHomePageContents(PayLoad result) {
        payLoad=result;
        patientName.setText(new Name().setName(payLoad.patient));
        patientName.setUnderline(true);
        patientName.setOnMouseClicked(e-> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPatient.fxml"));
                Parent homePage = loader.load();
                EditPatientController editPatientController = loader.getController();
                editPatientController.editPatientDetails(result);
                Scene homeScene = new Scene(homePage);
                Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                appStage.setScene(homeScene);


            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public void makeOnlineAppointment(MouseEvent e) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("onlineAppointment.fxml"));
        Parent homePage = loader.load();
        OnlineAppointmentController onlineAppointmentController = loader.getController();
        onlineAppointmentController.setOnlineAppointment(payLoad);
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    public void onLogut(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }
}
