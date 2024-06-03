package UDP.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
        this.user.setRoom("Bienvenue");

        String _messagecomplet = new String(packet.getData());
        System.out.println("Message reçu : " + _messagecomplet);
        String[] _messageSplit = _messagecomplet.split(" ");
        if(_messageSplit.length != 2 || !_messageSplit[0].equals("/co")){
            repondre("/err Mauvaise commande de connexion");
        }else{
            this.user.setPseudo(_messageSplit[1].trim());
            users.add(user);
            
            // on cherche un port disponible pour mettre en place la connexion
            try {
                this.socket = new DatagramSocket();
                int port = this.socket.getLocalPort(); 
                repondre("/co " + port);
                System.out.println("New listening port for user --> " + port);
                System.out.println("User " + user.getPseudo() + " connected");
                user.connect();
    
            } catch(SocketException e) {
                e.printStackTrace();
            }
        }
    }
    
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

            String _messagecomplet = new String(packet.getData()).trim();
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
        user.sendMessage("/err You are already connected");
    }

    public void deconnexion() {
        user.sendMessage("/msg You are now disconnected, bye bye !");
        user.disconnect();
        System.out.println("User " + user.getPseudo() + " disconnected");
        socket.close();
    }

    public void newMessage(String message) {
        logMessage(message);
        System.out.println("User " + user.getPseudo() + " sent a message : " + message + " in room " + user.getRoom());
        message = user.getPseudo() + " : " + message;
        for (User u : users) {
            if (u.getRoom().equals(user.getRoom()) && u.isConnected() && u != user){
                u.sendMessage(message);
            }
        }
    }

    public void joinRoom(String room) {
        user.setRoom(room);
        user.sendMessage("/info You joined the room " + room);
        System.out.println("User " + user.getPseudo() + " joined the room " + room);
    }

    private void logMessage(String message) {
        System.out.println("Logging message");

        //on crée le dossier s'il n'existe pas
        String directoryPath = "UDP" + File.separator + "server" + File.separator + "rooms";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = directoryPath + File.separator + user.getRoom() + ".log";

        //on essai d'ouvrir.créer le fichier
        try (FileOutputStream fos = new FileOutputStream(filePath, true)) {
            //on écrit dans le fichier
            String date = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss] ").format(Calendar.getInstance().getTime());
            fos.write((date + " " + user.getPseudo() + " " + message.strip()+"\n").getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
