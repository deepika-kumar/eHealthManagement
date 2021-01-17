package ehealthsystem;

import ehealthsystem.model.Doctor;
import ehealthsystem.model.Patient;
import ehealthsystem.model.PayLoad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLogin;
    @FXML
    private VBox loginPane;
    private StackPane stackInnerPane;
    private Boolean toggleFlag;
    private PayLoad payLoad = new PayLoad();

    // Calls this method on page load displays the login page
    public void initialize(){
        // The outer layer of login form
        Rectangle loginOutline = new Rectangle();
        loginOutline.setWidth(850);
        loginOutline.setHeight(550);
        loginOutline.setStroke(Paint.valueOf("#00BFFF"));
        loginOutline.setFill(Color.WHITE);

        StackPane stackPane = new StackPane();
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        Label message = new Label("Welcome to e-Health Management System");
        message.setStyle("-fx-text-fill: royalblue; -fx-font-size: 24.0 pt;-fx-font-weight: bold;");
        message.setUnderline(true);

        // Radio Button for Patient and Doctor Login toggle
        ToggleGroup tg = new ToggleGroup();
        RadioButton patientRB = new RadioButton("Login as Patient");
        RadioButton doctorRB = new RadioButton("Login as Doctor");
        patientRB.setToggleGroup(tg);
        doctorRB.setToggleGroup(tg);
        patientRB.setSelected(true);
        stackInnerPane=patientLoginScreen();

        // Load Patient Login Screen on click of patient radio button
        patientRB.setOnAction(e-> {
            vBox.getChildren().clear();
            loginPane.getChildren().clear();
            stackInnerPane=patientLoginScreen();
            vBox.getChildren().addAll(message,patientRB,doctorRB,stackInnerPane);
            loginPane.getChildren().add(stackPane);
        });

        // Load Doctor login screen on click of doctor radio button
        doctorRB.setOnAction(e-> {
            vBox.getChildren().clear();
            loginPane.getChildren().clear();
            stackInnerPane=doctorLoginScreen();
            vBox.getChildren().addAll(message,patientRB,doctorRB,stackInnerPane);
            loginPane.getChildren().add(stackPane);
        });

        vBox.getChildren().addAll(message,patientRB,doctorRB,stackInnerPane);
        stackPane.getChildren().addAll(loginOutline, vBox);
        loginPane.getChildren().add(stackPane);
    }

    // Method to create layout for patient login screen
    public StackPane patientLoginScreen(){
        StackPane sInnerPane = new StackPane();
        Rectangle innerPane = new Rectangle();
        innerPane.setWidth(600);
        innerPane.setHeight(350);
        innerPane.setStroke(Paint.valueOf("#00BFFF"));
        innerPane.setFill(Color.WHITE);

        GridPane gridInnerPane = new GridPane();
        gridInnerPane.setAlignment(Pos.CENTER);
        gridInnerPane.setHgap(5);
        gridInnerPane.setVgap(5);

        Label patientLB = new Label("Patient Login");
        patientLB.setStyle("-fx-text-fill: royalblue; -fx-font-size: 24.0 pt;-fx-font-weight: bold;");
        GridPane.setConstraints(patientLB,0,0);
        GridPane.setHalignment(patientLB, HPos.CENTER);

        TextField userName = new TextField();
        userName.setPromptText("Enter your email id");
        userName.setFocusTraversable(false);
        userName.setPrefHeight(30);
        userName.setPrefWidth(500);
        GridPane.setConstraints(userName,0,10);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setFocusTraversable(false);
        password.setPrefHeight(30);
        password.setPrefWidth(500);
        GridPane.setConstraints(password,0,15);

        Button pButton = new Button("Login as Patient");
        pButton.setStyle("-fx-background-color: royalblue;-fx-font-size: 14.0 pt;-fx-font-weight: bold;-fx-text-fill:White");
        GridPane.setConstraints(pButton,0,20);
        GridPane.setHalignment(pButton, HPos.CENTER);

        Hyperlink forgotPwd = new Hyperlink("Forgot Password");
        GridPane.setConstraints(forgotPwd,0,22);
        GridPane.setHalignment(forgotPwd, HPos.CENTER);

        Label errorPLogin = new Label();
        GridPane.setConstraints(errorPLogin,0,24);
        GridPane.setHalignment(errorPLogin, HPos.CENTER);
        errorPLogin.setStyle("-fx-font-size: 14.0 pt;-fx-text-fill:Green");

        // Call Login for patient method on click of login button
        pButton.setOnAction(e->{
            PayLoad result = onLoginasPatient(userName.getText(),password.getText());
            // Set error message if login failed
            if(result.loginFlag == false){
                errorPLogin.setText("Invalid Username or Password");
            }else
            {
                // Navigate to home page if login is success
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("PatientHome.fxml"));
                    Parent homePage = loader.load();
                    PatientHomeController patientHomeController = loader.getController();
                    patientHomeController.showPatientHomePageContents(result);
                    Scene homeScene = new Scene(homePage);
                    Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    appStage.setScene(homeScene);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        gridInnerPane.getChildren().addAll(patientLB,userName,password,pButton,forgotPwd,errorPLogin);

        sInnerPane.getChildren().addAll(innerPane,gridInnerPane);
        return sInnerPane;
    }

    // Method to create doctor login screen
    public StackPane doctorLoginScreen(){
        StackPane sInnerPane = new StackPane();
        Rectangle innerPane = new Rectangle();
        innerPane.setWidth(600);
        innerPane.setHeight(350);
        innerPane.setStroke(Paint.valueOf("#00BFFF"));
        innerPane.setFill(Color.WHITE);

        GridPane gridInnerPane = new GridPane();
        gridInnerPane.setAlignment(Pos.CENTER);
        gridInnerPane.setHgap(5);
        gridInnerPane.setVgap(5);

        Label doctorLB = new Label("Doctor Login");
        doctorLB.setStyle("-fx-text-fill: royalblue; -fx-font-size: 24.0 pt;-fx-font-weight: bold;");
        GridPane.setConstraints(doctorLB,0,0);
        GridPane.setHalignment(doctorLB, HPos.CENTER);

        TextField userName = new TextField();
        userName.setPromptText("Enter your email id");
        userName.setFocusTraversable(false);
        userName.setPrefHeight(30);
        userName.setPrefWidth(500);
        GridPane.setConstraints(userName,0,10);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setFocusTraversable(false);
        password.setPrefHeight(30);
        password.setPrefWidth(500);
        GridPane.setConstraints(password,0,15);

        Button dButton = new Button("Login as Doctor");
        dButton.setStyle("-fx-background-color: royalblue;-fx-font-size: 14.0 pt;-fx-font-weight: bold;-fx-text-fill:White");
        GridPane.setConstraints(dButton,0,20);
        GridPane.setHalignment(dButton, HPos.CENTER);

        Hyperlink forgotPwd = new Hyperlink("Forgot Password");
        GridPane.setConstraints(forgotPwd,0,22);
        GridPane.setHalignment(forgotPwd, HPos.CENTER);

        Label errorDLogin = new Label();
        GridPane.setConstraints(errorDLogin,0,24);
        GridPane.setHalignment(errorDLogin, HPos.CENTER);
        errorDLogin.setStyle("-fx-font-size: 14.0 pt;-fx-text-fill:Green");

        // call method for doctor login on click of login button
        dButton.setOnAction(e->{
            PayLoad result = onLoginasDoctor(userName.getText(),password.getText());
            // display error message if login fails
            if(payLoad.loginFlag==false){
                errorDLogin.setText("Invalid Username or Password");
            }else
            {
                // calls doctor home page if login is success
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("DoctorsHome.fxml"));
                    Parent homePage = loader.load();
                    DoctorsHomeController doctorsHomeController = loader.getController();
                    doctorsHomeController.showHomePageContents(payLoad);
                    Scene homeScene = new Scene(homePage);
                    Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    appStage.setScene(homeScene);


                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        gridInnerPane.getChildren().addAll(doctorLB,userName,password,dButton,forgotPwd,errorDLogin);

        sInnerPane.getChildren().addAll(innerPane,gridInnerPane);
        return sInnerPane;
    }

    public void onSignUpClick(MouseEvent event) throws IOException {
        Parent homePage = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    public PayLoad onLoginasPatient(String uName, String pwd) {

        Patient loggedInPatient = new Patient();
        payLoad.loginFlag=false;

        try{
            // Query userdetail table with username and password
            String QUERY_PATIENT_LOGIN_CHECK = ("SELECT * FROM UserDetail where Email = '"+uName+"' and Password = '"+pwd+"' and User = 'patient'");
            Query selectQuery_1 = new Query();
            ResultSet rs = selectQuery_1.retrieveSqlQuery(QUERY_PATIENT_LOGIN_CHECK);
            if (rs.next()){
                payLoad.loginFlag=true;
                // Retrieve patient details if login is success
                String QUERY_PATIENT_DETAILS = ("SELECT * FROM Patient where eMail = '"+uName+"'");
                Query selectQuery_2 = new Query();
                ResultSet rsName = selectQuery_2.retrieveSqlQuery(QUERY_PATIENT_DETAILS);
                // Save the patient details to Patient class
                loggedInPatient.firstName=rsName.getString("FirstName");
                loggedInPatient.lastName=rsName.getString("LastName");
                loggedInPatient.dob=rsName.getString("DateofBirth");
                loggedInPatient.eMail=uName;
                loggedInPatient.mobile=rsName.getString("Mobile");
                loggedInPatient.gender=rsName.getString("Gender");
                loggedInPatient.address=rsName.getString("Address");
                payLoad.patient=loggedInPatient;
                selectQuery_2.close();
            }
            selectQuery_1.close();
            }
        catch (SQLException e1) {
            System.out.println("DB connection error" + e1.getMessage());
        }

        if(payLoad.loginFlag == false){
            return payLoad;
        }
        return payLoad;
    }

    public PayLoad onLoginasDoctor(String uName, String pwd) {
        boolean flag = false;
        Doctor loggedinDoctor = new Doctor();

        try{
            // Query UserDetail table with username and password
            String QUERY_DOCTOR_LOGIN_CHECK = ("SELECT * FROM UserDetail where Email = '"+uName+"' and Password = '"+pwd+"' and User = 'doctor'");
            Query sqlQuery_3 = new Query();
            ResultSet rs = sqlQuery_3.retrieveSqlQuery(QUERY_DOCTOR_LOGIN_CHECK);
            if (rs.next()){
                payLoad.loginFlag = true;
                // Retrieve doctor details if login is success
                String QUERY_DOCTOR_DETAILS = ("SELECT * FROM Doctor where Email = '"+uName+"'");
                Query sqlQuery_4 = new Query();
                ResultSet rsName = sqlQuery_4.retrieveSqlQuery(QUERY_DOCTOR_DETAILS);
                // Save doctor details to doctor class model
                loggedinDoctor.firstName = rsName.getString("FirstName");
                loggedinDoctor.lastName = rsName.getString("LastName");
                loggedinDoctor.eMail = rsName.getString("Email");
                loggedinDoctor.status=rsName.getString("Status");
                payLoad.doctor = loggedinDoctor;
                sqlQuery_4.close();
            }
            sqlQuery_3.close();
        }
        catch (SQLException e1) {
            System.out.println("DB connection error" + e1.getMessage());
        }

        if(payLoad.loginFlag == false){
            return payLoad;
        }
        return payLoad;
    }

    // Navigate to home page on click home menu
    public void homePage(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    // Navigate to Online Appointment page on click from Online Appointment menu
    public void makeOnlineAppointment(MouseEvent e) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("onlineAppointment.fxml"));
        Parent homePage = loader.load();
        OnlineAppointmentController onlineAppointmentController = loader.getController();
        onlineAppointmentController.setOnlineAppointment(payLoad);
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }
}
