package ehealthsystem;

import ehealthsystem.model.PayLoad;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class Controller {
    @FXML
    private StackPane centreStackPane;
    @FXML
    private PayLoad payLoad;

    // Method to call homeScreen methon on page load
    public void initialize(){
        homeScreen(centreStackPane);
    }

    // Method to display home screen content
    public void homeScreen(StackPane centreStackPane){

        ImageView imageView = new ImageView(new Image("file:///C:/Piku/Java/eHMS/images/5.jpg"));
        imageView.setFitHeight(700);
        imageView.setFitWidth(1550);

        BorderPane centreBorderPane = new BorderPane();
        Label lb = new Label("Take advantage of online consultation with top doctors \nfrom anywhere.");
        lb.setPadding(new Insets(80,0,0,50));
        lb.setFont(Font.font(34));
        centreBorderPane.setTop(lb);

        Label leftLabel = new Label("CHOOSE YOUR PHYSICIAN\n\n\n\nSELECT THE AVAILABLE PHYSICIANS FOR CHAT CONSULTATION\n\n\n\nBOOK APPOINTMENT AND WAIT TO BE CALLED BACK");
        leftLabel.setPadding(new Insets(80,0,0,50));
        leftLabel.setFont(Font.font("Verdana", FontPosture.ITALIC,16));
        centreBorderPane.setLeft(leftLabel);

        // Add media on home screen related to Covid message
        Media media = new Media("file:///C:/Piku/Java/eHMS/video/STOP-COVID.mp4");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitHeight(300);
        mediaView.setFitWidth(500);
        mediaPlayer.stop();

        // Play or pause the media content
        Button playBtn = new Button("▷");
        playBtn.setOnAction(e-> {
            if (playBtn.getText().equals("▷")){
                mediaPlayer.play();
                playBtn.setText("||");
            }else{
                mediaPlayer.pause();
                playBtn.setText("▷");
            }
        });

        // Add slider to control the volume of the media
        Slider sVolume = new Slider();
        sVolume.setPrefWidth(150);
        sVolume.setMaxWidth(Region.USE_PREF_SIZE);
        sVolume.setMinWidth(30);
        sVolume.setValue(50);
        mediaPlayer.volumeProperty().bind(sVolume.valueProperty().divide(100));

        HBox mediaHBox = new HBox();
        mediaHBox.setAlignment(Pos.CENTER);
        mediaHBox.getChildren().addAll(playBtn,new Label("  \tVolume"),sVolume);

        BorderPane mediaBorderPane = new BorderPane();
        mediaBorderPane.setPadding(new Insets(0,400,150,0));
        mediaBorderPane.setCenter(mediaView);
        mediaBorderPane.setBottom(mediaHBox);

        centreBorderPane.setCenter(mediaBorderPane);

        centreStackPane.getChildren().addAll(imageView,centreBorderPane);

    }

    // Call Login page on click of login menu
    public void onLogin(MouseEvent event) throws IOException {
        Parent homePage = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    // Call Signup page on click of sign up menu option
    public void onSignUpClick(MouseEvent event) throws IOException {
        Parent homePage = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Scene homeScene = new Scene(homePage);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(homeScene);
    }

    // Call OnlineAppointment page on click of Online Appointment menu option
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
