package ehealthsystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.time.format.TextStyle;
import java.util.Date;

public class PatientChat extends Application {

    TextArea clientDetails = new TextArea();
    TextField clientTextFld = new TextField();
    String name;
    Socket socket = null;

    // Constructor - Set name
    PatientChat(String name){
        this.name = name;
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Style setting for patient chat window
        clientDetails.setFont(Font.font(13));
        clientDetails.setPrefSize(500,470);
        clientDetails.setEditable(false);
        Label infoLabel = new Label("Type your text here....");
        infoLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
        Label endLabel = new Label(" ");
        clientDetails.setWrapText(true);
        clientDetails.setStyle("-fx-control-inner-background:#F0F8FF;");
        clientTextFld.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
        clientTextFld.setPrefHeight(70);
        //clientDetails.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        //ScrollPane scrollPane = new ScrollPane();
        //scrollPane.setContent(clientDetails);

        VBox root = new VBox(5, clientDetails,infoLabel, clientTextFld);
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Patient : " + name);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        new Client();
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

    }

    // Method to close socket on click of windows close
    private void closeWindowEvent(WindowEvent event) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Client Class to start socket connection
    public class Client implements Runnable {
        DataInputStream  inFromServer;
        DataOutputStream outToServer;
        Thread t1, t2;
        String dataIn = "", dataOut = "";

        public Client() {
            try {
                t1 = new Thread(this);
                t2 = new Thread(this);
                // Create Socket
                socket = new Socket("localhost", 5000);
                clientDetails.appendText("\t\tHello "+(name.substring(0, name.indexOf(" ")))+ ", Welcome to e-Healthcare management System. \n\t\t\t\tYou are connected to our chat services.\n\t\t\tPlease specify your medical symptoms to continue.\n\n");
                t1.start();
                t2.start();

            } catch (Exception e) {
            }
        }

        public void run() {

            // Thread for Client
            try {
                if (Thread.currentThread() == t2) {
                    clientTextFld.setOnAction(e->{
                        String userInput1 = clientTextFld.getText();
                        clientTextFld.clear();
                        try {
                            outToServer = new DataOutputStream(socket.getOutputStream());

                            dataIn=userInput1;
                            Platform.runLater(() -> {
                                clientDetails.appendText( "Me : "+ userInput1+ "\n\n");

                                try {
                                    // Send messages to client
                                    outToServer.writeUTF(name + " : "+ userInput1 );
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            });

                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }


                    });
                } else {
                    do {
                        // Receive message from Server
                        inFromServer = new DataInputStream((socket.getInputStream()));
                        dataOut = inFromServer.readUTF();
                        clientDetails.appendText(dataOut + "\n\n");
                    } while (!dataOut.equals("END"));
                }
            } catch (Exception e) {
            }

        }
    }
}
