package app.cleancode;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        new Server().begin();
    }

    private Socket socket = new Socket(3801);
    private Set<String> clients = new HashSet<>();

    public void begin() {
        while (true) {
            try {
                System.out.println("About to check for packets");
                var packet = socket.get();
                if (!clients.contains(packet.getAddress().getHostAddress())) {
                    clients.add(packet.getAddress().getHostAddress());
                }
                byte[] message = packet.getData();
                System.out.printf("Message from %s: %s\n", packet.getAddress().getHostAddress(),
                        new String(message, StandardCharsets.UTF_8));
                clients.forEach(host -> {
                    try {
                        System.out.println("Sending message to " + host);
                        socket.post(message, host, 3802);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
