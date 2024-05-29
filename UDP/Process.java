package UDP;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class Process implements Runnable {
    

    private DatagramPacket packet;

    public Process(DatagramPacket _packet) {
        this.packet = _packet;
    }
    
    public void run() {

        byte[] data = this.packet.getData();

        System.out.println("Received: " + new String(data));
        
        byte[] rep = "GOOD".getBytes();

        DatagramPacket response = new DatagramPacket(rep, rep.length, packet.getAddress(), packet.getPort());
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.send(response);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

