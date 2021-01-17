package ehealthsystem;

import com.sun.prism.impl.Disposer;
import ehealthsystem.model.PayLoad;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ViewAppointmentController {
    @FXML
    private Label doctorName;
    @FXML
    private VBox vBox;
    @FXML
    private Label startChat;
    @FXML
    private Label vAppointLb;
    PayLoad payLoad = new PayLoad();

    public static ObservableList<Appointment> appointmentData= null;

    public void viewAppointmentDetails(PayLoad result) {
        payLoad=result;
        doctorName.setText("Dr. "+new Name().setName(result.doctor));

        TableView<Appointment> table = new TableView<Appointment>();
        table.setEditable(false);

        ArrayList<Appointment> data = new ArrayList<Appointment>();

        try{
            String QUERY_APPOINTMENT_DETAILS = ("SELECT * FROM Appointment where DEmail = '"+result.doctor.eMail+"' and Status = 'Pending' ");
            Query selectQuery = new Query();
            ResultSet rs = selectQuery.retrieveSqlQuery(QUERY_APPOINTMENT_DETAILS);
            while (rs.next()){
                data.add(new Appointment(rs.getString("FirstName"),rs.getString("LastName"),rs.getString("PEmail"),
                        rs.getString("Mobile"),rs.getString("Reason"),rs.getString("AppointmentDate"),rs.getString("Time"),"Pending, click after consultation"));
            }
            selectQuery.close();
        }
        catch (SQLException e) {
            System.out.println("DB connection error" + e.getMessage());
        }


        appointmentData = FXCollections.observableArrayList(data);
        table.setItems(appointmentData);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setMinWidth(150);
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("firstName"));
        table.getColumns().add(firstNameCol);

        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setMinWidth(150);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("lastName"));
        table.getColumns().add(lastNameCol);
        TableColumn emailCol = new TableColumn("Email ID");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("patientEmail"));
        table.getColumns().add(emailCol);


        TableColumn mobileCol = new TableColumn("Phone Number");
        mobileCol.setMinWidth(150);
        mobileCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("mobile"));
        table.getColumns().add(mobileCol);

        TableColumn reasonCol = new TableColumn("Appointment Reason");
        reasonCol.setMinWidth(200);
        reasonCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("reason"));
        table.getColumns().add(reasonCol);


        TableColumn appointDayCol = new TableColumn("Appointment Date");
        appointDayCol.setMinWidth(200);
        appointDayCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("appointmentDay"));
        table.getColumns().add(appointDayCol);

        TableColumn appointTimeCol = new TableColumn("Appointment Time");
        appointTimeCol.setMinWidth(200);
        appointTimeCol.setCellValueFactory(
                new PropertyValueFactory<Appointment, String>("appointmentTime"));
        table.getColumns().add(appointTimeCol);

        TableColumn statusCol = new TableColumn("Action");
        statusCol.setMinWidth(200);
        table.getColumns().add(statusCol);

        statusCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });
        //Adding the Button to the cell
        statusCol.setCellFactory(
                new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {

                    @Override
                    public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> p) {
                        return new ButtonCell();
                    }

                });

        vBox.getChildren().add(table);
        vBox.setPadding(new Insets(50,50,50,50));



        startChat.setOnMouseClicked(e-> {
            String QUERY_UPDATE_STATUS = null;
            QUERY_UPDATE_STATUS = ("UPDATE Doctor SET Status = 'Available' WHERE Email = '"+result.doctor.eMail+"'");

            Stage stage = new Stage();
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
    protected static class ButtonCell extends TableCell<Disposer.Record, Boolean> {
        final Button cellButton = new Button("Mark Attended");

        ButtonCell(){

            //Action when the button is pressed
            cellButton.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    // get Selected Item
                    Appointment currentAppointment = (Appointment) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
                    //remove selected item from the table list

                    String UPD_STATUS = ("UPDATE Appointment SET Status = 'Completed' WHERE PEmail = '"+currentAppointment.getPatientEmail().toString()+"' and '"+currentAppointment.getAppointmentDay().toString()+"' and '"+currentAppointment.getAppointmentTime().toString()+"'");
                    Query updateQuery = new Query();
                    updateQuery.updateSqlQuery(UPD_STATUS);
                    updateQuery.close();

                    appointmentData.remove(currentAppointment);

                }
            });
        }

        //Display button if the row is not empty
        //@override
        public void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }
            else{
                setGraphic(null);
            }
        }
    }

    public void onLogut(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    public void onHomePage(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DoctorsHome.fxml"));
        Parent homePage = loader.load();
        DoctorsHomeController doctorsHomeController = loader.getController();
        doctorsHomeController.showHomePageContents(payLoad);
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }
}
