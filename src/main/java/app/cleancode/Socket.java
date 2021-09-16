package app.cleancode;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Socket implements AutoCloseable {
    private DatagramSocket socket;

    public Socket() {
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void post(byte[] message, String ip, int port) throws Exception {
        DatagramPacket packet =
                new DatagramPacket(message, message.length, InetAddress.getByName(ip), port);
        socket.send(packet);
    }

    public DatagramPacket get(String ip, int port) throws Exception {
        DatagramPacket packet =
                new DatagramPacket(new byte[512], 500, InetAddress.getByName(ip), port);
        socket.receive(packet);
        return packet;
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
