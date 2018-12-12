package com.client.chatwindow;

import com.client.login.MainLauncher;
import com.client.lyrics.Sentence;
import com.client.util.Json;
import com.client.util.MessageSender;
import com.client.util.Messenger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;


public class GameController implements Initializable {

    @FXML private TextArea messageBox;
    @FXML private ListView<String> messageList;
    @FXML private Label usernameLabel;
    @FXML private Label onlineCountLabel;
    @FXML private ListView statusPane;

    @FXML ListView<HBox> chatPane;
    @FXML BorderPane borderPane;
    @FXML BorderPane borderTopPane;
    private String gameStatus = "";
    private ArrayList<Sentence> song;
    private int current = 0;
    private int points = 0;

    private double xOffset;
    private double yOffset;
    private MessageSender sender;
    Logger logger = LoggerFactory.getLogger(GameController.class);

    public void addToStatus(String msg){
        Platform.runLater(()->{
            Label input = new Label();
            input.setText(msg);
            this.statusPane.getItems().add(input);
            System.out.println("addToStatus");
        });
    }

    public void setSocket(Socket socket){
        try {
            this.sender = new MessageSender(new PrintStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendButtonAction() throws IOException {
        String msg = messageBox.getText();
        if(current > song.size()) return;
        messageList.setEditable(true);
        if (!messageBox.getText().isEmpty()) {
            if(song.get(current).testAnswer(msg)){
                this.points++;
                messageList.getItems().set(current, song.get(current).getContent());
            }
            this.current++;
            playSong();
            messageBox.clear();
        }else {

        }
    }

    public void setGameStatus(String gameStatus){
        this.gameStatus = gameStatus;
    }

    public void disableMessageBox(){
        this.messageBox.setDisable(true);
    }

    public void enableMessageBox(){
        this.messageBox.setDisable(false);
    }

    public void setSong(List<Sentence> song){
        this.song = new ArrayList<>(song);
    }

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
    }

    public void setPaneColor(String color){
        setColor(color, borderTopPane);
    }

    public static void setColor(String color, BorderPane borderTopPane) {
        switch (color){
            case "Black":
                borderTopPane.setStyle("-fx-background-color: #000000;");
                break;
            case "Green":
                borderTopPane.setStyle("-fx-background-color: #15ff00;");
                break;
            case "Pink":
                borderTopPane.setStyle("-fx-background-color: #f702b1;");
                break;

            default:
                borderTopPane.setStyle("-fx-background-color: #29efff;");
                break;
        }
    }

    public void sendMethod(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
        }
    }

    @FXML
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        borderPane.setOnMousePressed(event -> {
            xOffset = MainLauncher.getPrimaryStage().getX() - event.getScreenX();
            yOffset = MainLauncher.getPrimaryStage().getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            MainLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            MainLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });


        /* Added to prevent the enter from adding a new line to inputMessageBox */
        messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    sendButtonAction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ke.consume();
            }
        });

    }



    public void endGame(){
        String totalPoints = String.format("Total Points: %s", points);
        messageList.getItems().add(totalPoints);
        sender.sendMessage(Json.toJson(Messenger.getMessenger(totalPoints, "TOTAL_POINTS", "USER: " + this.usernameLabel.getText())));
    }

    public void logoutScene() {
        Platform.runLater(() -> {
            FXMLLoader fmxlLoader = new FXMLLoader(getClass()
                    .getResource("/views/LoginView.fxml"));
            Parent window = null;
            try {
                window = (Pane) fmxlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = MainLauncher.getPrimaryStage();
            Scene scene = new Scene(window);
            stage.setMaxWidth(350);
            stage.setMaxHeight(420);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }

    public void playSong() {
        Platform.runLater(()->{
                if(current > song.size()-1) { endGame();}
                Sentence sentence = song.get(this.current);
                sentence.setMissingWord();
                boolean isCensored = !StringUtils.isBlank(sentence.getCensoredString());
                String line = isCensored ?  sentence.getCensoredString() : sentence.getContent() ;

                messageList.getItems().add(line);

                if(!isCensored){
                    current++;
                    playSong();
                }


       });
    }
}