package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class User {
    
    private String pseudo;
   
    private InetAddress ip;
    
    private int port;

    private String room;
    
    private boolean connected;
    

    public User(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public User(String pseudo, InetAddress ip, int port) {
        this.pseudo = pseudo;
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        connected = true;
    }

    public void disconnect() {
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void sendMessage(String message) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte [] data = message.getBytes();
            socket.send(new DatagramPacket(data, data.length, ip, port));
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}