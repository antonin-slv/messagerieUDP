package UDP;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.HashMap;


public class Process implements Runnable {
    
    private static HashMap<String, String[]> users = new HashMap<String, String[]>(); // <username, [ip, port, room]>
    
    private String message;
    private String action;
    private InetAddress addr;
    private int port;

    
    private String pseudo;
    private String reponse;
    

    public Process(DatagramPacket packet) {
        
        this.message = new String(packet.getData());
        this.action = this.message.split(" ")[0];
        this.addr = packet.getAddress();
        this.port = packet.getPort();
    }
    
    public void run() {

        System.out.println("Received: " + message);
        
        // TODO trouve pseudo avec ip/port    
        
        switch (action) {
            case "/co":
                
                break;
            case "/deco":
                
                break;
            case "/msg":

                break;
            case "/join":

                break;
            default:
                System.out.println("Unknown command");
                break;
        }
        
        






        // DatagramPacket response = new DatagramPacket(rep, rep.length, addr, port);
        // try {
        //     DatagramSocket socket = new DatagramSocket();
        //     socket.send(response);
        //     socket.close();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    public String traiter() {
        return "You said: " + new String(message);
    }
}

