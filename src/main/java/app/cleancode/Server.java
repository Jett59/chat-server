package app.cleancode;

import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        new Server().begin();
    }

    private Socket socket = new Socket();
    private Map<String, Integer> clients = new HashMap<>();

    public void begin() {
        while (true) {
            try {
                var packet = socket.get("0.0.0.0", 3801);
                if (!clients.containsKey(packet.getAddress().getHostAddress())) {
                    clients.put(packet.getAddress().getHostAddress(), packet.getPort());
                }
                byte[] message = packet.getData();
                clients.forEach((host, port) -> {
                    try {
                        socket.post(message, host, port);
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
