package ehealthsystem;

import ehealthsystem.model.PayLoad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class EditPatientController {
    @FXML
    private Label patientName;
    @FXML
    private Label pName;
    @FXML
    private VBox flowPane;
    @FXML
    private HBox bottomPane;
    @FXML
    private PayLoad payLoad;


    // Method to Edit Patient Details and Edit layout
    public void editPatientDetails(PayLoad result) {
        payLoad = result;

        // Set patient name on the menu bar
        patientName.setText(new Name().setName(payLoad.patient));
        pName.setText(new Name().setName(payLoad.patient));
        patientName.setUnderline(true);

        // Outline of the Patient Edit form
        Rectangle basicOutline = new Rectangle();
        basicOutline.setWidth(600);
        basicOutline.setHeight(200);
        basicOutline.setStroke(Paint.valueOf("#00BFFF"));
        basicOutline.setFill(Color.WHITE);

        StackPane stackBasic = new StackPane();
        // GridPane to contain patient details for edit
        GridPane basicPane = new GridPane();
        basicPane.setAlignment(Pos.CENTER);
        basicPane.setHgap(100);
        basicPane.setVgap(10);
        Label fNameLb = new Label("First Name");
        fNameLb.setStyle("-fx-font-size: 14.0 pt;-fx-font-weight: bold;");
        TextField fNameText = new TextField();
        fNameText.setText(result.patient.firstName);
        fNameText.setFont(Font.font(14));
        fNameText.setDisable(true);
        GridPane.setConstraints(fNameLb,0,0);
        GridPane.setConstraints(fNameText,1,0);

        Label lNameLb = new Label("Last Name");
        lNameLb.setStyle("-fx-font-size: 14.0 pt;-fx-font-weight: bold;");
        TextField lNameText = new TextField();
        lNameText.setText(result.patient.lastName);
        lNameText.setFont(Font.font(14));
        lNameText.setDisable(true);
        GridPane.setConstraints(lNameLb,0,3);
        GridPane.setConstraints(lNameText,1,3);

        Label emailLb = new Label("Email Address");
        emailLb.setStyle("-fx-font-size: 14.0 pt;-fx-font-weight: bold;");
        TextField emailText = new TextField();
        emailText.setText(result.patient.eMail);
        emailText.setFont(Font.font(14));
        emailText.setDisable(true);
        GridPane.setConstraints(emailLb,0,6);
        GridPane.setConstraints(emailText,1,6);

        basicPane.getChildren().addAll(fNameLb,fNameText,lNameLb,lNameText,emailLb,emailText);
        stackBasic.getChildren().addAll(basicOutline,basicPane);

        Label basicLb = new Label("Basic Information",stackBasic);
        basicLb.setFont(Font.font(14));
        basicLb.setContentDisplay(ContentDisplay.BOTTOM);

        Rectangle addtlOutline = new Rectangle();
        addtlOutline.setWidth(600);
        addtlOutline.setHeight(100);
        addtlOutline.setStroke(Paint.valueOf("#00BFFF"));
        addtlOutline.setFill(Color.WHITE);

        StackPane stackAddtl = new StackPane();
        GridPane addtlPane = new GridPane();
        addtlPane.setAlignment(Pos.CENTER);
        addtlPane.setHgap(100);
        addtlPane.setVgap(5);
        Label genderLb = new Label("Gender            ");
        genderLb.setStyle("-fx-font-size: 14.0 pt;-fx-font-weight: bold;");
        TextField genderText = new TextField();
        genderText.setText(result.patient.gender);
        genderText.setFont(Font.font(14));
        genderText.setDisable(true);
        GridPane.setConstraints(genderLb,0,0);
        GridPane.setConstraints(genderText,1,0);

        Label dobLb = new Label("Birthday");
        dobLb.setStyle("-fx-font-size: 14.0 pt;-fx-font-weight: bold;");
        TextField dobText = new TextField();
        dobText.setText(result.patient.dob);
        dobText.setFont(Font.font(14));
        dobText.setDisable(true);
        GridPane.setConstraints(dobLb,0,3);
        GridPane.setConstraints(dobText,1,3);

        addtlPane.getChildren().addAll(genderLb,genderText,dobLb,dobText);
        stackAddtl.getChildren().addAll(addtlOutline,addtlPane);

        Label addtlLb = new Label("Additional Information",stackAddtl);
        addtlLb.setFont(Font.font(14));
        addtlLb.setContentDisplay(ContentDisplay.BOTTOM);

        Rectangle contactOutline = new Rectangle();
        contactOutline.setWidth(600);
        contactOutline.setHeight(100);
        contactOutline.setStroke(Paint.valueOf("#00BFFF"));
        contactOutline.setFill(Color.WHITE);

        StackPane stackContact = new StackPane();
        GridPane contactPane = new GridPane();
        contactPane.setAlignment(Pos.CENTER);
        contactPane.setHgap(100);
        contactPane.setVgap(5);
        Label mobileLb = new Label("Phone Number");
        mobileLb.setStyle("-fx-font-size: 14.0 pt;-fx-font-weight: bold;");
        TextField mobileText = new TextField();
        mobileText.setText(result.patient.mobile);
        //fNameText.setMaxWidth(200);
        mobileText.setFont(Font.font(14));
        mobileText.setDisable(true);
        GridPane.setConstraints(mobileLb,0,0);
        GridPane.setConstraints(mobileText,1,0);

        Label mailLb = new Label("Mailing Address");
        mailLb.setStyle("-fx-font-size: 14.0 pt;-fx-font-weight: bold;");
        TextField mailText = new TextField();
        mailText.setText(result.patient.address);
        mailText.setFont(Font.font(14));
        mailText.setDisable(true);
        GridPane.setConstraints(mailLb,0,3);
        GridPane.setConstraints(mailText,1,3);


        contactPane.getChildren().addAll(mobileLb,mobileText,mailLb,mailText);
        stackContact.getChildren().addAll(contactOutline,contactPane);

        Label contactLb = new Label("Contact Information",stackContact);
        contactLb.setFont(Font.font(14));
        contactLb.setContentDisplay(ContentDisplay.BOTTOM);


        flowPane.getChildren().addAll(basicLb,addtlLb,contactLb);

        bottomPane.setAlignment(Pos.BOTTOM_RIGHT);
        bottomPane.setPadding(new Insets(0,200,200,0));
        bottomPane.setSpacing(20);
        Image imageEdit = new Image("file:///C:/Piku/Java/eHMS/images/Edit.jpg");
        ImageView iVEdit = new ImageView(imageEdit);
        iVEdit.setFitHeight(30);
        iVEdit.setFitWidth(30);

        // Enable the edit in text box for all the fields on click of edit option
        iVEdit.setOnMouseClicked(e-> {
            fNameText.setDisable(false);
            lNameText.setDisable(false);
            dobText.setDisable(false);
            genderText.setDisable(false);
            mobileText.setDisable(false);
            mailText.setDisable(false);
        });

        Image imageSave = new Image("file:///C:/Piku/Java/eHMS/images/Save.jpg");
        ImageView iVSave = new ImageView(imageSave);
        iVSave.setFitHeight(30);
        iVSave.setFitWidth(30);

        // Update table patient with new edited details of patient
        iVSave.setOnMouseClicked(e-> {
            Connection connection=null;
            Statement statement = null;

            try{
                // Start DB connection
                connection = DriverManager.getConnection("jdbc:sqlite:C:\\Piku\\Java\\eHMS\\Database\\eHMS.db");
                statement = connection.createStatement();
                String UPD_USER = ("UPDATE Patient SET FirstName = '"+fNameText.getText()+"',LastName = '"+lNameText.getText()+"',Gender = '"+genderText.getText()+"', Mobile = '"+mobileText.getText()+"', DateofBirth = '"+dobText.getText()+"', Address = '"+mailText.getText()+"' where Email = '"+emailText.getText()+"'");
                int rs = statement.executeUpdate(UPD_USER);
                statement.close();
                connection.close();
            }
            catch (SQLException e1) {
                System.out.println("DB connection error" + e1.getMessage());
            }

            fNameText.setDisable(true);
            lNameText.setDisable(true);
            dobText.setDisable(true);
            genderText.setDisable(true);
            mobileText.setDisable(true);
            mailText.setDisable(true);

        });

        bottomPane.getChildren().addAll(iVEdit,iVSave);


    }

    // navigate to home page on click of home menu
    public void homePage(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PatientHome.fxml"));
        Parent homePage = loader.load();
        PatientHomeController patientHomeController = loader.getController();
        patientHomeController.showPatientHomePageContents(payLoad);
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    // navigate to Online Appointment page on click of Make Online Appointment click
    public void makeOnlineAppointment(MouseEvent e) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("onlineAppointment.fxml"));
        Parent homePage = loader.load();
        OnlineAppointmentController onlineAppointmentController = loader.getController();
        onlineAppointmentController.setOnlineAppointment(payLoad);
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    // navigate on home page and logout patient on click of logout menu
    public void onLogut(MouseEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = loader.load();
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

}
