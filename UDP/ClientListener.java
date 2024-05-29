package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientListener implements Runnable {

    Client father;

    public ClientListener(Client father) {
        this.father = father;
    }

    @Override
    public void run() {
        DatagramPacket entree = null;
        while (true) {

            try {
            //le client attend la réponse
            byte[] buffer = new byte[1024];
            entree = new DatagramPacket(buffer, buffer.length);
            father.socket.receive(entree);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            father.messages.add(new String(entree.getData()));
            System.out.println("\n\n\n\n");
            for (String message : father.messages) {
                System.out.println(message);
            }
            System.out.printf(father.pseudo + " : ");
        }


        
    }

}
