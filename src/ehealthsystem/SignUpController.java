package ehealthsystem;

import ehealthsystem.model.Patient;
import ehealthsystem.model.PayLoad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private ComboBox gender;
    @FXML
    private PasswordField pwd1;
    @FXML
    private PasswordField pwd2;
    @FXML
    private TextField mob;
    @FXML
    private DatePicker dob;
    @FXML
    private Button bSignUp;
    @FXML
    private Label errorFN;
    @FXML
    private Label errorLN;
    @FXML
    private Label errorEM;
    @FXML
    private Label errorP1;
    @FXML
    private Label errorP2;
    @FXML
    private Label errorMOB;
    @FXML
    private Label errorDOB;
    @FXML
    private Label errorBtn;
    @FXML
    private PayLoad payLoad;
    Connection connection = null;
    Statement statement = null;

    // Validate name
    public boolean isValidName(String s) {
        String regex = "[A-Za-z\\s]+";
        return s.matches(regex);
    }

    // Validate email id
    public boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    // Validate password
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }

    // Validate mobile number
    private static boolean isValidMobile(String s) {
        String regex = "\\d{3}-\\d{3}-\\d{4}";
        return s.matches(regex);
    }

    // Validate first name
    public void validateFirstName(KeyEvent e) {
        errorFN.setText("");
        if (!(isValidName(firstName.getText()))) {
            errorFN.setText("Invalid First Name");
        }
        enableSignup();
    }

    // Enable the sigup button only after all validations are success
    private void enableSignup() {
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || email.getText().isEmpty() || pwd1.getText().isEmpty()
                || pwd2.getText().isEmpty() || mob.getText().isEmpty() ) {
            bSignUp.setDisable(true);
        } else if (errorFN.getText().isEmpty() && errorLN.getText().isEmpty() && errorDOB.getText().isEmpty() && errorMOB.getText().isEmpty()
                && errorEM.getText().isEmpty() && errorP1.getText().isEmpty() && errorP2.getText().isEmpty()) {
            bSignUp.setDisable(false);
        }
    }

    public void validatelastName(KeyEvent e) {
        errorLN.setText("");
        if (!(isValidName(lastName.getText()))) {
            errorLN.setText("Invalid Last Name");
        }
        enableSignup();
    }

    public void validateEMail(KeyEvent e) {
        errorEM.setText("");
        if (!(isEmailValid(email.getText()))) {
            errorEM.setText("Invalid Email");
        }
        enableSignup();
    }


    public void validatePassword(KeyEvent e) {
        errorP1.setText("");
        if (!(isEmailValid(pwd1.getText()))) {
            errorP1.setText("Must be 8-20chars,number,Upper/Lower case,special char");
        }
        enableSignup();
    }

    public void validateRePassword(KeyEvent e) {
        errorP2.setText("");
        if (!(pwd1.getText().equals(pwd2.getText()))) {
            errorP2.setText("Password does not match");
        }
        enableSignup();
    }

    public void validateMobile(KeyEvent e) {
        errorMOB.setText("");
        if (!(isValidMobile(mob.getText()))) {
            errorMOB.setText("Invalid Mobile Number");
        }
        enableSignup();
    }

    public void validateDOB(ActionEvent e) {
        errorDOB.setText("");
        if ((java.time.LocalDate.now().compareTo(dob.getValue())) < 0 || (java.time.LocalDate.now().compareTo(dob.getValue())) > 200) {
            errorDOB.setText("Invalid Date of Birth");
        }
        enableSignup();
    }


    public void onSignUpButton(ActionEvent e) {
        Patient patientDetails = new Patient();
        patientDetails.firstName = firstName.getText();
        patientDetails.lastName = lastName.getText();
        patientDetails.eMail = email.getText();
        patientDetails.gender = gender.getValue().toString();
        patientDetails.mobile = mob.getText();
        patientDetails.dob = dob.getValue().toString();
        String password = pwd1.getText();


        boolean flag = true;

        // Insert Patient basic details to table patient and userdetail
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Piku\\Java\\eHMS\\Database\\eHMS.db");
            statement = connection.createStatement();
            try{

                statement.executeUpdate("INSERT INTO UserDetail('Email','Password','User')" + "VALUES('"+patientDetails.eMail+"','"+password+"','patient')");
                statement.executeUpdate("INSERT INTO Patient ('FirstName','LastName','eMail', 'Gender', 'Mobile', 'DateofBirth') " +
                        "VALUES('"+patientDetails.firstName+"','"+patientDetails.lastName+"','"+patientDetails.eMail+"','"+patientDetails.gender+"','"+patientDetails.mobile+"','"+patientDetails.dob+"')");
                statement.close();
                connection.close();
            }
            catch (SQLException error){
                flag = false;
            }

        } catch (SQLException e1) {
            System.out.println("DB connection error" + e1.getMessage());
        }

        // Clear the field after signup was successful
        firstName.clear();
        lastName.clear();
        email.clear();
        gender.getSelectionModel().clearSelection();
        gender.setPromptText("Select Gender");
        pwd1.clear();
        pwd2.clear();
        mob.clear();
        dob.getEditor().clear();
        if(flag){
            Alert a = new Alert(AlertType.NONE);
            a.setAlertType(AlertType.CONFIRMATION);
            a.setTitle("Alert");
            a.setContentText("Signup was successful. Click on Login as Patient to continue");
            a.showAndWait();
        }
        else{
            errorBtn.setText("Account already exists");
        }


    }

    public void onSignUpClick(MouseEvent event) throws IOException {
        Parent homePage = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    public void onLogin(MouseEvent event) throws IOException {
        Parent homePage = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    public void homePage(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
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
}

