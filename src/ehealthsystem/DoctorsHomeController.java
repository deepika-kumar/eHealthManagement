package ehealthsystem;

import ehealthsystem.model.PayLoad;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DoctorsHomeController {
    @FXML
    private Label appointCount;
    @FXML
    private Label doctorName;
    @FXML
    private Label startChat;
    @FXML
    private Label vAppointLb;
    PayLoad payLoad = new PayLoad();

    public void initialize(){

    }

    // Display Doctor's home page contents
    public void showHomePageContents(PayLoad result) {
        payLoad = result;
        // Set doctor name and display at menu bar
        doctorName.setText("Dr. "+new Name().setName(result.doctor));

        // Find current day
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        // Query count of appointments for the current day
        String CHCK_USER = ("SELECT count(1) FROM Appointment where DEmail = '"+result.doctor.eMail+"' and Status = 'Pending' and AppointmentDate = '"+formatted.toString()+"' ");
        Query selectCount = new Query();
        ResultSet rs = selectCount.retrieveSqlQuery(CHCK_USER);
        try {
            if(rs.next()){
                appointCount.setText("You have "+rs.getInt(1)+" appointments for today. Please navigate to View Appointments for more details.");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        selectCount.close();

        // Start server for chat consultation.
        startChat.setOnMouseClicked(e-> {
            String QUERY_UPDATE_STATUS = null;
            QUERY_UPDATE_STATUS = ("UPDATE Doctor SET Status = 'Available' WHERE Email = '"+result.doctor.eMail+"'");

            Stage stage = new Stage();
            // Call DoctorChat class to transfer the control
            DoctorChat doctorChat = new DoctorChat(doctorName.getText());
            try {
                doctorChat.start(stage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            Query updateQuery = new Query();
            updateQuery.updateSqlQuery(QUERY_UPDATE_STATUS);
            updateQuery.close();
        });

        // Navigate to View Appointment Page to view appointment details on click of View Appointment menu
        vAppointLb.setOnMouseClicked(e-> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAppointment.fxml"));
                Parent homePage = loader.load();
                ViewAppointmentController viewAppointmentController = loader.getController();
                viewAppointmentController.viewAppointmentDetails(result);
                Scene homeScene = new Scene(homePage);
                Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                appStage.setScene(homeScene);


            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    // Navigate to Home Page on click of Logout menu and logout doctor
    public void onLogut(MouseEvent e) throws IOException {
        String QUERY_UPDATE_OFFLINE_STATUS = ("UPDATE Doctor SET Status = 'Offline' WHERE Email = '"+payLoad.doctor.eMail+"'");
        Query updateQuery_2 = new Query();
        updateQuery_2.updateSqlQuery(QUERY_UPDATE_OFFLINE_STATUS);
        updateQuery_2.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

}

