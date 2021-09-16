package app.cleancode;

import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Client extends Application {
    private Queue<byte[]> inboundMessages = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        launch(args);
    }

    private final GridPane root = new GridPane();
    private Socket socket = new Socket();
    private Socket receiver = new Socket(3802);

    private boolean running = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chat server");
        primaryStage.setWidth(1024);
        primaryStage.setHeight(768);
        primaryStage.setScene(new Scene(root));
        createScene();
        primaryStage.show();
        new Thread(this::recieverThread, "Client Listener Thread").start();
        primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt -> {
            running = false;
        });
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
                message.setText("");
                message.requestFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        AnimationTimer messageReader = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (!inboundMessages.isEmpty()) {
                    messages.setText(String.format("%s\n%s", messages.getText(),
                            new String(inboundMessages.poll())));
                }
            }
        };
        messageReader.start();
    }

    private void recieverThread() {
        while (running) {
            try {
                inboundMessages.add(receiver.get().getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
