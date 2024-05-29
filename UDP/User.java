package UDP;

public class User {
    private String pseudo;
    private String ip;
    private int port;
    private String room;
    private boolean connected;

    public User(String pseudo, String ip, int port) {
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
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

}
