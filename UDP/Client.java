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
    
}
