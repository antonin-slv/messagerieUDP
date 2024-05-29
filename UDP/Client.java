package UDP;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {

    public Client() {
        
    }



    @Override
    public void run() {


        int port = 12345;
        DatagramSocket socket;
        InetAddress addr;
        try {
            addr = InetAddress.getByName("localhost");
            socket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String message;
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try{
                message = reader.readLine();
            }catch(IOException e){
                e.printStackTrace();
                continue;
            }
            if (message.charAt(0) == '/') {
                if (message.equals("/quit")) {
                    break;
                }
            }

            byte[] data = message.getBytes();
            try {

                DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
                socket.send(packet);
                //le client attend la réponse
                byte[] buffer = new byte[1024];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);
                System.out.println("Client received : " + new String(response.getData()));
            
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
    
}
