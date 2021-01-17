package ehealthsystem;

import ehealthsystem.model.Doctor;
import ehealthsystem.model.PayLoad;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class OnlineAppointmentController {
    @FXML
    private TextField dName;
    @FXML
    private ComboBox speciality;
    @FXML
    private TextField state;
    @FXML
    private ComboBox gender;
    @FXML
    private FlowPane dPane;
    @FXML
    private FlowPane doctorDetailPane;
    @FXML
    private Rectangle mainRect;
    @FXML
    private PayLoad payLoad;
    @FXML
    private Label patientName;
    @FXML
    private Label logInOut;

    // Search for doctor and initiate chat and calls book appointment
    public void setOnlineAppointment(PayLoad result) throws SQLException {
        payLoad=result;

        // Set the patient name on the menu bar
        try{
            patientName.setText(new Name().setName(payLoad.patient));
            patientName.setUnderline(true);
            // Call Edit PAtient page on click of name
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
                }
            });

            logInOut.setText("Logout");
            logInOut.setOnMouseClicked(e->{
                try {
                    onLogut(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            });
        }
        catch (NullPointerException e1){
            patientName.setText("");
            logInOut.setText("Login");
            logInOut.setOnMouseClicked(e-> {
                try {
                    onLogin(e);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
        retrieveDoctorRecords(null,"General Physician",null,null);
    }

    // Retrieve list of doctors based on name, speciality, location and gender
    public void retrieveDoctorRecords(String name,String speciality,String state,String gender) throws SQLException {

        Connection connection = null;
        Statement statement = null;

        // Build the query based on selections
        List<String> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Doctor where Speciality=?");
        params.add(speciality);

       if(name!= null){
           String[] nameArray = name.split("\\s");
           if(nameArray.length>1){
               sb.append("and FirstName = ?");
               params.add(nameArray[0]);
               sb.append("and LastName = ?");
               params.add(nameArray[1]);
           }
           else{
               sb.append("and (FirstName = ?");
               params.add(name);
               sb.append("or LastName = ?)");
               params.add(name);
           }
        }
       if(state!=null){
           sb.append("and Location = ?");
           params.add(state);
       }
       if(gender!=null){
           sb.append("and Gender = ?");
           params.add(gender);
       }
       // Start DB connection
        connection = DriverManager.getConnection("jdbc:sqlite:C:\\Piku\\Java\\eHMS\\Database\\eHMS.db");
        PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
        // Build the query
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setString(i+1, params.get(i));
        }
        // execute the query
        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<Doctor> doctorList = new ArrayList<Doctor>();
        doctorList.clear();
        while (rs.next()) {
            doctorList.add(new Doctor(rs.getString("FirstName"), rs.getString("LastName"),
                    rs.getString("Email"), rs.getString("Phone"), rs.getString("Gender"),
                    rs.getString("Speciality"), rs.getString("Languages"), rs.getString("Location"),rs.getString("Status")));
        }

        preparedStatement.close();
        connection.close();

    displayDoctorsProfile(doctorList);
    }

    // Display the doctors list and display their name, image, status
    public void displayDoctorsProfile(ArrayList<Doctor> doctorArrayList) {
        dPane.getChildren().clear();
        if(doctorArrayList.isEmpty()){
            Label noDoctorMessage = new Label("Doctors not found!");
            dPane.getChildren().add(noDoctorMessage);
        }
        else{

            // Create outline to display each doctor's details
            Rectangle[] rectArray = new Rectangle[doctorArrayList.size()];

            // Loop for each list of doctors retrieved
            for (int i = 0; i < doctorArrayList.size(); i++) {
                StackPane stackPane = new StackPane();
                rectArray[i] = new Rectangle();
                rectArray[i].setStroke(Paint.valueOf("#00BFFF"));
                rectArray[i].setHeight(150);
                rectArray[i].setWidth(150);
                rectArray[i].setFill(Color.WHITE);

                VBox innerPane = new VBox();
                innerPane.setAlignment(Pos.CENTER);
                Image image = new Image("file:///C:/Piku/Java/eHMS/images/" + doctorArrayList.get(i).gender + ".jpg");
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(70);
                imageView.setFitWidth(70);
                Hyperlink dName = new Hyperlink("Dr. "+doctorArrayList.get(i).firstName + " " + doctorArrayList.get(i).lastName);
                Label dSpeciality = new Label(doctorArrayList.get(i).speciality);
                Label dStatus = new Label();
                dStatus.setStyle("-fx-font-weight: bold;");
                // Set the status as Available, Busy and Offline
                if(doctorArrayList.get(i).status.contains("Available")){
                    dStatus.setText("Online : Available");
                    dStatus.setTextFill(Color.GREEN);
                }else if(doctorArrayList.get(i).status.contains("Busy")) {
                    dStatus.setText("Online : Busy");
                    dStatus.setTextFill(Color.RED);
                }
                else{
                    dStatus.setText("Offline");
                }
                innerPane.getChildren().addAll(imageView, dName, dSpeciality,dStatus);

                stackPane.getChildren().addAll(rectArray[i], innerPane);
                dPane.getChildren().add(stackPane);
                dName.setOnAction(e-> {
                    String Name = dName.getText();
                    sendDoctorDetails(doctorArrayList,Name.replace("Dr. ",""));
                });
            }

        }
    }

    private void sendDoctorDetails(ArrayList<Doctor> doctorArrayList, String Name) {
        String fName = Name.contains(" ") ? Name.split(" ")[0] : Name;
        String lName = Name.contains(" ") ? Name.split(" ")[1] : Name;
        for (Doctor doctor:doctorArrayList) {
            if(doctor.firstName.equals(fName) && doctor.lastName.equals(lName)){
                displayDoctorsDetail(doctor);
            }

        }
    }

    // Display the detailed description of each doctor on click of doctor link
    private void displayDoctorsDetail(Doctor doctor) {
        doctorDetailPane.setVisible(true);
        doctorDetailPane.getChildren().clear();

        try {
            if(payLoad.loginFlag==true){
                StackPane firstpane = new StackPane();
                VBox sPane = new VBox();

                Rectangle headerRect = new Rectangle();
                headerRect.setStroke(Paint.valueOf("#00BFFF"));
                headerRect.setHeight(40);
                headerRect.setWidth(950);
                headerRect.setFill(Paint.valueOf("#00BFFF"));

                GridPane gPane = new GridPane();
                gPane.setVgap(10);
                gPane.setHgap(20);
                gPane.setPadding(new Insets(20,20,20,20));
                Label speciality = new Label("Speciality :  "+doctor.speciality);
                speciality.setFont(Font.font(14));
                GridPane.setConstraints(speciality,0,0);

                Label location = new Label("Location :  "+doctor.location);
                location.setFont(Font.font(14));
                GridPane.setConstraints(location,0,1);

                Label lang = new Label("Languages Spoken :  "+doctor.language);
                GridPane.setConstraints(lang,0,2);
                lang.setFont(Font.font(14));

                Label phone = new Label("Mobile :  "+doctor.phone);
                phone.setFont(Font.font(14));
                GridPane.setConstraints(phone,0,3);

                Label email = new Label("Email ID :  "+doctor.eMail);
                email.setFont(Font.font(14));
                GridPane.setConstraints(email,0,4);

                gPane.getChildren().addAll(speciality,location,lang,phone,email);


                StackPane secondPane = new StackPane();
                HBox headerPane = new HBox();
                headerPane.setSpacing(30);

                Label name = new Label("Dr. " +doctor.firstName + " " + doctor.lastName);
                name.setPadding(new Insets(8, 8, 8, 20));
                name.setFont(Font.font(16));
                name.setStyle("-fx-text-fill: white");

                Label status = new Label();
                status.setStyle("-fx-font-weight: bold;");
                if(doctor.status.contains("Available")){
                    status.setText("Online : Available");
                    status.setTextFill(Color.GREEN);
                }
                else if(doctor.status.contains("Busy")){
                    status.setText("Online : Busy");
                    status.setTextFill(Color.RED);
                }
                else{
                    status.setText("Offline");
                    status.setTextFill(Color.WHITE);
                }
                status.setPadding(new Insets(8, 200, 8, 90));
                status.setFont(Font.font(16));

                // Link for chat consultation
                Hyperlink chat = new Hyperlink("Chat Consultation");
                chat.setPadding(new Insets(6, 6, 6, 6));
                chat.setFont(Font.font(16));
                chat.setStyle("-fx-text-fill: white");
                chat.setVisible(false);
                // Call patient chat window and connect to server socket on click of link . Also update doctor's status to busy
                chat.setOnAction(e-> {
                    String UPD_STATUS = null;
                    UPD_STATUS = ("UPDATE Doctor SET Status = 'Busy' WHERE Email = '"+doctor.eMail+"'");

                    Connection connection=null;
                    Statement statement = null;

                    try{
                        connection = DriverManager.getConnection("jdbc:sqlite:C:\\Piku\\Java\\eHMS\\Database\\eHMS.db");
                        statement = connection.createStatement();
                        int rs = statement.executeUpdate(UPD_STATUS);
                        statement.close();
                        connection.close();
                    }
                    catch (SQLException e1) {
                        System.out.println("DB connection error" + e1.getMessage());
                    }

                    Stage stage = new Stage();
                    PatientChat patientChat = new PatientChat(patientName.getText());
                    try {
                        patientChat.start(stage);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                });

                Hyperlink appointment = new Hyperlink("Book an appointment");
                appointment.setPadding(new Insets(8, 8, 8, 8));
                appointment.setFont(Font.font(16));
                appointment.setStyle("-fx-text-fill: white");

                // Enable chat consultation link only when doctor is available
                if(status.getText().contains("Available")){
                    chat.setVisible(true);
                }

                appointment.setOnAction(e-> {
                    payLoad.doctor = doctor;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("bookAppointment.fxml"));
                        Parent homePage = loader.load();
                        BookAppointmentController bookAppointmentController = loader.getController();
                        bookAppointmentController.bookAppointments(payLoad);
                        Scene homeScene = new Scene(homePage);
                        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        appStage.setScene(homeScene);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                });

                headerPane.getChildren().addAll(name,status,chat,appointment);
                secondPane.getChildren().addAll(headerRect,headerPane);

                sPane.getChildren().addAll(secondPane,gPane);

                firstpane.getChildren().addAll(mainRect,sPane);
                doctorDetailPane.getChildren().add(firstpane);
            }
        }catch (NullPointerException e){
            Label loginLB = new Label("Login/ Signup for online consultation and appointments. ");
            loginLB.setTextFill(Color.RED);
            loginLB.setFont(Font.font(20));
            doctorDetailPane.getChildren().add(loginLB);
        }

    }

    public void searchDoctor(ActionEvent e) throws SQLException {
        doctorDetailPane.setVisible(false);
        System.out.println(dName.getText().isEmpty()?null:dName.getText());
        retrieveDoctorRecords(dName.getText().isEmpty()?null:dName.getText(),speciality.getValue()==null?"General Physician":speciality.getValue().toString(),state.getText().isEmpty()?null:state.getText(),null);

    }

    public void onLogin(MouseEvent event) throws IOException {
        Parent homePage = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    public void onSignUpClick(MouseEvent event) throws IOException {
        Parent homePage = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    public void homePage(MouseEvent e) throws IOException {
        System.out.println(payLoad.loginFlag);
        if(payLoad.loginFlag == true){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PatientHome.fxml"));
            Parent homePage = loader.load();
            PatientHomeController patientHomeController = loader.getController();
            patientHomeController.showPatientHomePageContents(payLoad);
            Scene homeScene = new Scene(homePage);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            appStage.setScene(homeScene);
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent homePage = loader.load();
            Scene homeScene = new Scene(homePage);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            appStage.setScene(homeScene);
        }

    }

    public void onLogut(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }
}
