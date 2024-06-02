package UDP.server;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//#define PORT 12345 s'ecrit en java comme suit :  


public class Server implements Runnable {

    DatagramSocket socket;
    Boolean running = false;
    ExecutorService pool = Executors.newFixedThreadPool(20);

    public Server() {
        socket = null;
        running = false;
        
        try {
            int port = getPort();
            if(port == -1) {return;}
            
            System.out.println("Server started on port --> " + port);
            socket = new DatagramSocket(port);

        } catch(SocketException e) {
            e.printStackTrace();
        }
    }

    //renvoi un tableau de booléens, avec true si le port range[0] + i est ouvert, false sinon
    public static boolean[] testPort(int[] range){
        boolean[] openclosed = new boolean[range[1] - range[0] + 1];
        for(int i = range[0]; i <= range[1]; i++) {
            try {
                DatagramSocket s = new DatagramSocket(i);
                s.close();
                openclosed[i - range[0]] = true;
            }catch (SocketException e) {
                openclosed[i - range[0]] = false;
            }
        }
        return openclosed;
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

    @Override
    public void run() {
        if (socket == null) {return;}
        running = true;

        while(running) {
            System.out.println("Waiting for a connection");
            //on attend un paquet
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            //on traite le paquet
            try {
                socket.receive(packet);
                   
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            //on a reçu qqch :

            packet.getData();

            pool.execute(new Process(packet));
            // byte[] data = packet.getData();
            // System.out.println("Received: " + new String(data));
            // DatagramPacket response = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
            // try {
            //     socket.send(response);
            // } catch (Exception e) {
            //     e.printStackTrace();
            //     continue;
            // }
        }
    }
}
