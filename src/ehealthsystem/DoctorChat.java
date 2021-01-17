package ehealthsystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Date;

public class DoctorChat extends Application {

    TextArea serverDetails = new TextArea();
    TextField serverTextFld = new TextField();
    String name;
    ServerSocket serversocket;
    Socket socket = null;

    // Constructor to set name of doctor
    DoctorChat(String name){
        this.name = name;
    }


    @Override
    public void start(Stage stage) throws Exception {
        serverDetails.setFont(Font.font(13));
        serverDetails.setPrefSize(500,470);
        serverDetails.setEditable(false);

        Label infoLabel = new Label("Type your text here....");
        infoLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
        // Set Text Area style
        serverDetails.setWrapText(true);
        serverDetails.setStyle("-fx-control-inner-background:#F0F8FF;");
        serverTextFld.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
        serverTextFld.setPrefHeight(70);

        VBox root = new VBox(5, serverDetails, infoLabel,serverTextFld);
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Doctor : " + name);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        new Server();
        // Call method on click of window close operation
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    // Method to close socket and serverSocket on click of close window
    private void closeWindowEvent(WindowEvent event) {
        try {
            socket.close();
            serversocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Server Class supporting multi-threading
    public class Server implements Runnable {

        DataInputStream  inFromClient;
        DataOutputStream outToClient;

        Thread t1, t2;
        String dataIn = "", dataOut = "";

        // Constructor - start server and wait for client connection
        public Server() {
            try {
                t1 = new Thread(this);
                t2 = new Thread(this);
                serversocket = new ServerSocket(5000);
                serverDetails.appendText("\t\t\t\tWaiting for new patient to connect..\n");
                Platform.runLater(() -> {
                    try {
                        socket = serversocket.accept();
                        serverDetails.appendText("\t\t\tNew Patient has got connected and is ready to chat\n\n" );
                        t1.start();
                        t2.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
            }
        }

        // Implement Run method to exchange message between server and client , Server - Theard1, Client - Thread2
        public void run() {
            try {
                if (Thread.currentThread() == t1) {
                    serverTextFld.setOnAction(e-> {
                        String userInput1 = serverTextFld.getText();
                        serverTextFld.clear();
                        try {
                            outToClient = new DataOutputStream(socket.getOutputStream());
                            dataIn=userInput1;
                            Platform.runLater(() -> {
                                serverDetails.appendText("Me : "+ userInput1+ "\n\n");
                            });
                            //Send message to client
                            outToClient.writeUTF((name.substring(0, name.lastIndexOf(" "))) + " : "+ userInput1);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    });

                } else {
                    do {
                        // Receive message from client and display on chat box
                        inFromClient = new DataInputStream((socket.getInputStream()));
                        dataOut = inFromClient.readUTF();
                        serverDetails.appendText(dataOut + "\n\n");
                    } while (!dataOut.equals("END"));
                }
            } catch (Exception e) {
            }
        }

    }
}