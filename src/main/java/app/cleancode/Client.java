package app.cleancode;

import java.nio.charset.StandardCharsets;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private final GridPane root = new GridPane();
    private Socket socket = new Socket();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chat server");
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        primaryStage.setScene(new Scene(root));
        createScene();
        primaryStage.show();
    }

    public void createScene() {
        root.setPadding(new Insets(25));
        TextField ip = new TextField();
        root.getChildren().add(ip);
        ip.setPromptText("Ip address");
        GridPane.setColumnIndex(ip, 0);
        TextField message = new TextField();
        message.setPromptText("Message");
        root.getChildren().add(message);
        GridPane.setColumnIndex(message, 0);
        GridPane.setRowIndex(message, 1);
        Button send = new Button("Send");
        root.getChildren().add(send);
        GridPane.setColumnIndex(send, 1);
        GridPane.setRowIndex(send, 1);
        send.requestFocus();
        message.setOnKeyPressed(evt -> {
            if (evt.getCode().equals(KeyCode.ENTER)) {
                send.fire();
            }
        });
        TextArea messages = new TextArea();
        messages.setEditable(false);
        root.getChildren().add(messages);
        GridPane.setColumnIndex(messages, 0);
        GridPane.setRowIndex(messages, 2);
        send.setOnAction(evt -> {
            try {
                socket.post(message.getText().getBytes(StandardCharsets.UTF_8), ip.getText(), 3801);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
