package UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable {


    int port = 12345;

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

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new ClientListener(this));


        //connexion :
        message = "/co " + pseudo;

        byte[] data;
        data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        connected = true;
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

            data = message.getBytes();
            try {

                packet = new DatagramPacket(data, data.length, addr, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        socket.close();
    }
    
}
