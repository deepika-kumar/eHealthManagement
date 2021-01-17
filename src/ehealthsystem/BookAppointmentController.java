package ehealthsystem;

import ehealthsystem.model.PayLoad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class BookAppointmentController {
    @FXML
    private PayLoad payLoad;
    @FXML
    private Label patientName;
    @FXML
    private VBox bookPane;

    // Method to book an appointment for specific doctor
    public void bookAppointments(PayLoad result) {
        payLoad = result;

        // Set the patient name on the menu bar
        patientName.setText(new Name().setName(payLoad.patient));
        patientName.setUnderline(true);

        // Navigate to EditPatient page on click of patient name
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

        // Rectangle layout for outer box
        Rectangle outerLine = new Rectangle();
        outerLine.setHeight(550);
        outerLine.setWidth(800);
        outerLine.setStroke(Color.BLUE);
        outerLine.setFill(Color.WHITE);

        // Stack pane to stack outer layer rectangle and inner content
        StackPane stackPane = new StackPane();
        VBox innerContentVBPane = new VBox();
        innerContentVBPane.setPadding(new Insets(0,0,50,0));
        innerContentVBPane.setSpacing(20);
        innerContentVBPane.setAlignment(Pos.CENTER);

        // Rectangle for blue header box
        Rectangle header = new Rectangle();
        header.setHeight(120);
        header.setWidth(800);
        header.setStroke(Color.ROYALBLUE);
        header.setFill(Color.ROYALBLUE);

        StackPane headerStackPane = new StackPane();
        VBox headerVBox = new VBox();
        headerVBox.setAlignment(Pos.CENTER);
        Label formLabel = new Label("Appointment Form");
        formLabel.setFont(Font.font(20));
        formLabel.setTextFill(Color.WHITE);
        formLabel.setStyle("-fx-font-weight:bold;");

        Label message = new Label("To schedule an appointment, please fill out the information below");
        message.setTextFill(Color.LIGHTGOLDENRODYELLOW);
        headerVBox.getChildren().addAll(formLabel,message);
        headerStackPane.getChildren().addAll(header,headerVBox);

        // Gridpane for entering patient details
        GridPane basicDetailGPane = new GridPane();
        basicDetailGPane.setHgap(50);
        basicDetailGPane.setVgap(10);
        basicDetailGPane.setAlignment(Pos.CENTER);

        Label infoLb = new Label("Patient Information");
        infoLb.setFont(Font.font(14));
        GridPane.setConstraints(infoLb,0,0);

        TextField fName = new TextField();
        fName.setFocusTraversable(false);
        fName.setMaxWidth(300);
        fName.setPrefWidth(300);
        fName.setPromptText("First Name");
        GridPane.setConstraints(fName,0,2);

        TextField lName = new TextField();
        lName.setFocusTraversable(false);
        lName.setMaxWidth(300);
        lName.setPrefWidth(300);
        lName.setPromptText("Last Name");
        GridPane.setConstraints(lName,1,2);

        TextField mobile = new TextField();
        mobile.setFocusTraversable(false);
        mobile.setPromptText("Phone Number");
        GridPane.setConstraints(mobile,0,4);

        TextField email = new TextField(payLoad.patient.eMail);
        email.setFont(Font.font("Verdana", FontWeight.BOLD,12));
        email.setDisable(true);
        GridPane.setConstraints(email,1,4);

        basicDetailGPane.getChildren().addAll(infoLb,fName,lName,mobile,email);

        TextField reason = new TextField();
        reason.setFocusTraversable(false);
        reason.setMaxWidth(650);
        reason.setPromptText("Reason of Appointment");

        Line line = new Line(5, 650, 650, 650);
        line.setStroke(Color.BLACK);

        GridPane appointmentGPane = new GridPane();
        appointmentGPane.setHgap(50);
        appointmentGPane.setVgap(10);
        appointmentGPane.setAlignment(Pos.CENTER);

        Label appointmentLb = new Label("Appointment Details");
        appointmentLb.setFont(Font.font(14));
        GridPane.setConstraints(appointmentLb,0,0);

        TextField dummy = new TextField("");
        dummy.setPrefWidth(300);
        dummy.setVisible(false);
        GridPane.setConstraints(dummy,1,2);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Please select an appointment date ");
        datePicker.setEditable(false);
        datePicker.setPrefWidth(300);
        datePicker.setFocusTraversable(false);
        GridPane.setConstraints(datePicker,0,2);

        appointmentGPane.getChildren().addAll(appointmentLb,dummy,datePicker);

        // Label for displaying error message
        Label errorMessage = new Label();
        errorMessage.setFont(Font.font("Verdana", FontPosture.ITALIC,14));
        errorMessage.setTextFill(Color.RED);

        // Label for displaying success message
        Label successMessage = new Label();
        successMessage.setFont(Font.font("Verdana", FontPosture.ITALIC,14));
        successMessage.setTextFill(Color.GREEN);

        // Flowpane for button controls
        FlowPane timeSelectFPane = new FlowPane();
        timeSelectFPane.setPadding(new Insets(20,0,0,0));
        timeSelectFPane.setAlignment(Pos.CENTER);
        timeSelectFPane.setHgap(20);
        timeSelectFPane.setVgap(20);
        ArrayList<String> timeOption = new ArrayList<String>();

        datePicker.setOnAction(e->{
            successMessage.setText("");
            errorMessage.setText("");
            if ((java.time.LocalDate.now().compareTo(datePicker.getValue())) > 0 ) {
                timeSelectFPane.getChildren().clear();
                errorMessage.setText("Please select a future date for book an appointment");
            }
            else {
                ArrayList<String> bookedSlot = new ArrayList<String>();

                // Retrieve the details of booked slots
                try {
                    timeSelectFPane.getChildren().clear();
                    timeOption.clear();
                    String QUERY_APPOINTMENT_SLOTS = ("SELECT Time FROM Appointment where DEmail = '" + payLoad.doctor.eMail + "' and AppointmentDate = '" + datePicker.getValue().toString() + "' and Status = 'Pending' ");
                    Query selectQuery = new Query();
                    ResultSet rs = selectQuery.retrieveSqlQuery(QUERY_APPOINTMENT_SLOTS);

                    bookedSlot.clear();
                    while (rs.next()) {
                        bookedSlot.add(rs.getString("Time"));
                    }
                    selectQuery.close();
                } catch (SQLException e1) {
                    System.out.println("DB connection error" + e1.getMessage());
                }

                // List of time options available
                timeOption.add("8:00 - 9:00");
                timeOption.add("9:00 - 10:00");
                timeOption.add("10:00 - 11:00");
                timeOption.add("11:00 - 12:00");
                timeOption.add("15:00 - 16:00");
                timeOption.add("16:00 - 17:00");
                Button[] timeButtons = new Button[timeOption.size()];
                for (int i = 0; i < timeOption.size(); i++) {
                    timeButtons[i] = new Button(timeOption.get(i));
                    timeButtons[i].setStyle("-fx-background-color: royalblue;-fx-font-size: 12.0 pt;-fx-font-weight: bold;-fx-text-fill:White");
                    int finalI = i;
                    timeButtons[i].setOnAction(event -> {

                        Connection connection1 = null;
                        Statement statement1 = null;
                        boolean flag = true;

                        // Insert new appointment details in the appointment table
                        try {
                            connection1 = DriverManager.getConnection("jdbc:sqlite:C:\\Piku\\Java\\eHMS\\Database\\eHMS.db");
                            statement1 = connection1.createStatement();
                            statement1.executeUpdate("INSERT INTO Appointment('DEmail','FirstName','LastName','Mobile','PEmail','Reason','AppointmentDate','Time','Status')" + "VALUES('" + payLoad.doctor.eMail + "','" + fName.getText() + "','" + lName.getText() + "','" + mobile.getText() + "','" + email.getText() + "','" + reason.getText() + "','" + datePicker.getValue().toString() + "','" + timeOption.get(finalI) + "','Pending')");
                            statement1.close();
                            connection1.close();
                        } catch (SQLException error) {
                            flag = false;
                            System.out.println(error.getErrorCode());
                        }

                        if (flag == true) {
                            successMessage.setText("Your appointment has been confirmed on " + datePicker.getValue() + " between " + timeOption.get(finalI) + " hours");
                        }
                        fName.clear();
                        lName.clear();
                        mobile.clear();
                        reason.clear();
                        timeSelectFPane.setVisible(false);

                    });
                }

                // Disable the buttons at which the appointment is already booked
                for (int i = 0; i < bookedSlot.size(); i++) {
                    System.out.println(bookedSlot.get(i));
                    for (int j = 0; j < timeButtons.length; j++) {
                        if (bookedSlot.get(i).equals(timeButtons[j].getText())) {
                            timeButtons[j].setDisable(true);
                        }
                    }
                }
                timeSelectFPane.getChildren().addAll(timeButtons);
                timeSelectFPane.setVisible(true);
            }
        });

        innerContentVBPane.getChildren().addAll(headerStackPane,basicDetailGPane,reason,line,appointmentGPane,timeSelectFPane,errorMessage,successMessage);
        stackPane.getChildren().addAll(outerLine,innerContentVBPane);

        bookPane.getChildren().addAll(stackPane);
    }

    // Call Patient Home page on click of Home Menu
    public void homePage(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PatientHome.fxml"));
        Parent homePage = loader.load();
        PatientHomeController patientHomeController = loader.getController();
        patientHomeController.showPatientHomePageContents(payLoad);
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    // Call Make an Online Appointment page on click of online appointment menu
    public void makeOnlineAppointment(MouseEvent e) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("onlineAppointment.fxml"));
        Parent homePage = loader.load();
        OnlineAppointmentController onlineAppointmentController = loader.getController();
        onlineAppointmentController.setOnlineAppointment(payLoad);
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    // call home page on click of logout
    public void onLogut(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }
}
