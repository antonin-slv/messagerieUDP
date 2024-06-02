package UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.crypto.Data;

public class Client implements Runnable {


    int port = 13401;

    protected DatagramSocket socket;

    InetAddress addr;

    String pseudo;

    ArrayList<String> messages = new ArrayList<String>();

    Boolean connected = false;

    public Client() {
        System.out.printf("Entre ton pseudo : ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try{
            pseudo = reader.readLine().replace('/', '_').replace(' ', '_');
        }catch(IOException e){
            e.printStackTrace();
            return;
        }

        try {
            addr = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
    public Client(DatagramSocket socket) {
        super();
        this.socket.close();
        this.socket = socket;
    }

    @Override
    public void run() {
        String message;

        if (!connect()) return;

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new ClientListener(this));

        

        Boolean running = true;
        while (running) {
            System.out.printf(pseudo + " : ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try{
                message = reader.readLine();
            }catch(IOException e){
                e.printStackTrace();
                continue;
            }

            if (message == null || message.length() == 0) {
                continue;
            }
            if (message.charAt(0) == '/') {
                if (message.equals("/quit")) {
                    running = false;
                    executor.shutdown();
                }
                if (message.contains("/co ")) {
                    if (connected) {
                        System.out.println("You are already connected");
                        continue;
                    }
                }
            }
            else {
                message = "/msg " + message;
            }

            byte[] data = message.getBytes();
            try {

                DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        socket.close();
    }
    private boolean connect() {
        //connexion :
        connected = false;
        int counter = 0;
        while (!connected && counter < 5) {
            counter++;
            String message = "/co " + pseudo;
            

            //envoie message pour demander la conexion
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            
            //attend réponse du serveur
            byte[] buffer = new byte[1024];
            DatagramPacket entree = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(entree);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            //analyse la réponse du serveur
            String [] response = new String(entree.getData()).trim().split(" ");
            int newPort = -1;
            if (response[0].equals("/co")) {
                try { 
                    newPort = Integer.parseInt(response[1]);
                } catch (NumberFormatException e) {
                    System.out.println(newPort + " : " + response[1] + " is not a valid port");
                    continue;
                }
                port = newPort;
                connected = true;
                System.out.println("Connected");
            } else {
                System.out.println("Connection failed");
                continue;
            }
        }
        return connected;
    }
    
}
