package UDP;

import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class Process implements Runnable {
    
    private static ArrayList<User> users = new ArrayList<User>();


    private User user;
    private DatagramSocket socket;
    
    public Process(DatagramPacket packet) {
        
        this.user = new User(packet.getAddress(), packet.getPort());

        String _messagecomplet = new String(packet.getData());
        String[] _messageSplit = _messagecomplet.split(" ");
        if(_messageSplit.length != 2 || _messageSplit[0] != "/co"){
            repondre("/err Mauvaise commande de connexion");
        }else{
            this.user.setPseudo(_messageSplit[1]);
            users.add(user);
            
            // on cherche un port disponible pour mettre en place la connexion
            try {
                int port = getPort();
                if(port == -1) {
                    repondre("/err No free port, try again later");
                    System.out.println("No free port available for a new connection");
                    return;
                }
                this.socket = new DatagramSocket(port);
                repondre("/co " + port);
                System.out.println("Server started on port --> " + this.socket.getPort());
                System.out.println("User " + user.getPseudo() + " connected");
                user.connect();
    
            } catch(SocketException e) {
                e.printStackTrace();
            }
        }
    }
    /**
    public Process(DatagramPacket packet) {
        
        String _messagecomplet = new String(packet.getData());
        String[] _messageSplit = _messagecomplet.split(" ");

        this.action = _messageSplit[0];
        this.message = null;
        if(_messageSplit.length > 1) { //si il existe, on récupère le message (sans l'action)
            this.message = _messagecomplet.substring(action.length() + 1);
        }
        this.addr = packet.getAddress();
        this.port = packet.getPort();
        this.pseudo = "";

        if(users != null && !users.isEmpty()) {
            for (User user : users) { // on cherche le pseudo correspondant à l'ip/port
                if (user.getIp().equals(addr.toString()) && user.getPort() == port) {
                    pseudo = user.getPseudo();
                }
            }
        }
    }
    **/
    
    public void run() {

        while (user.isConnected()) {
            
            // on attend un paquet
            
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(packet);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // on a reçu qqch :

            String _messagecomplet = new String(packet.getData());
            String[] _messageSplit = _messagecomplet.split(" ");
            
            String action = _messageSplit[0];
            String message = null;
            if(_messageSplit.length > 1) { //si il existe, on récupère le message (sans l'action)
                message = _messagecomplet.substring(action.length() + 1);
            }
            
            System.out.println("==================> New message received");
            System.out.println("Pseudo: " + user.getPseudo());
            System.out.println("Action: " + action);
            System.out.println("Received: " + message);

            switch (action) {
                case "/co" -> connexion();
                case "/quit" -> deconnexion();
                case "/msg" -> newMessage(message);                    
                case "/room" -> joinRoom(message);
                default -> {
                    System.out.println("Unknown command");
                    repondre("Unknown command");
                }
            }
        }
        socket.close();
    }
    
    public void repondre(String rep) {
        DatagramPacket reponse = new DatagramPacket(rep.getBytes(), rep.length(), this.user.getIp(), this.user.getPort());
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.send(reponse);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getPort() {
        int[] range = {13401, 13720};
        for(int i = range[0]; i <= range[1]; i++) {
            try {
                DatagramSocket s = new DatagramSocket(i);
                s.close();
                System.out.println("Port " + i + " is free");
                return i;
            }catch (SocketException e) {}
        }
        System.out.println("No free port available between 13400 and 13720");
        return -1;
    }

    public void connexion() {
        System.out.println("User " + user.getPseudo() + " is already connected and tried to connect again (so dumb)");
        repondre("You are already connected");
    }

    public void deconnexion() {
        repondre("/msg You are now disconnected, bye bye !");
        user.disconnect();
        System.out.println("User " + user.getPseudo() + " disconnected");
    }

    public void newMessage(String message) {
        // TODO     
    }

    public void joinRoom(String room) {
        user.setRoom(room);
        repondre("/info You joined the room " + room);
        System.out.println("User " + user.getPseudo() + " joined the room " + room);
    }

    private void logMessage(String message) {
        try {
            FileOutputStream fos = new FileOutputStream(user.getRoom() + ".log", true);
            String date = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss] ").format(Calendar.getInstance().getTime());
            fos.write((date + message.strip()+"\n").getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

