package UDP;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.xml.crypto.Data;

public class Server implements Runnable {

    DatagramSocket socket;
    Boolean running = false;

    public Server() {
        socket = null;
        running = false;
        try {
            socket = new DatagramSocket(12345);
        } catch(SocketException e) {
            e.printStackTrace();
        }
    }

    //renvoi un tableau de booléens, avec true si le port range[0] + i est ouvert, false sinon
    public static boolean[] testPort(int[] range, String ip){
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

    @Override
    public void run() {
        if (socket == null) {
            return;
        }
        running = true;

        while(true) {
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
            //on envoie une réponse
            byte[] data = packet.getData();
            DatagramPacket response = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
            try {
                socket.send(response);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
