package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {

    public Client() {


        
    }



    @Override
    public void run() {
        String message = System.console().readLine();

        byte[] data = message.getBytes();
        try {
            InetAddress addr = InetAddress.getByName("localhost");
            int port = 12345;
            DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);

            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);



            //le client attend la réponse
            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);
            System.out.println("Client received : " + new String(response.getData()));

            socket.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
}
