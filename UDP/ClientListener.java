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

        while (!father.socket.isClosed()) {

            try {
                //le client attend la réponse
                byte[] buffer = new byte[1024];
                entree = new DatagramPacket(buffer, buffer.length);
                father.socket.receive(entree);
            } catch (Exception e) {
                if (father.socket.isClosed()) {
                    return;
                }
                System.out.println("Erreur lors de la réception du message : " + new String(entree.getData(), 0, entree.getData().length));
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
