package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.util.ArrayList;



public class Process implements Runnable {
    
    private static ArrayList<User> users = new ArrayList<User>();

    private String message;
    private String action;
    private InetAddress addr;
    private int port;

    private String pseudo;
    

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
    
    public void run() {

        System.out.println("Action: " + action);
        System.out.println("Received: " + message);
        
        if(!action.equals("/co")) {
            if (pseudo.equals("")) { // si on ne trouve pas de pseudo on renvoie un message d'erreur
                System.out.println("Unknown user");
                repondre("Unknown user");
                return;
            }
        }
        
        switch (action) {
            case "/co":
                this.pseudo = message.split(" ")[0];
                User user = new User(pseudo, addr.toString(), port);
                user.connect();
                users.add(user);
                System.out.println("User " + pseudo + " connected");
                repondre("User " + pseudo + " connected");
                break;
            case "/deco":
                
                break;
            case "/msg":
                System.out.println("Message from " + this.pseudo + " : " + message);
                repondre("Message bien reçu  ");
                break;
            case "/join":

                break;
            default:
                System.out.println("Unknown command");
                repondre("Unknown command");
                break;
        }
        return;
    }

    public void repondre(String rep) {
        DatagramPacket response = new DatagramPacket(rep.getBytes(), rep.length(), addr, port);
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.send(response);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}

