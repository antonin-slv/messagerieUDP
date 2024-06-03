package UDP.client;

import java.net.DatagramPacket;

public class ClientListener implements Runnable {

    Client father;

    public ClientListener(Client father) {
        this.father = father;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket entree = new DatagramPacket(buffer, buffer.length);

        while (!father.socket.isClosed()) {
            String message = "";
            try {
                //le client attend la réponse
                father.socket.receive(entree);
                message = new String(entree.getData(), 0, entree.getData().length);
            } catch (Exception e) {
                if (father.socket.isClosed()) {
                    return;
                }
                System.out.println("Erreur lors de la réception du message : " + message);
                continue;
            }

            traitementCommande(message);
        }        
    }
    private void traitementCommande(String message) {
        String [] messageArray = message.split(" ");
        if (messageArray[0].equals("/hist")) {
            System.out.print("\t" + messageArray[3] + " : ");
            for (int i = 4; i < messageArray.length; i++) {
                System.out.print(messageArray[i] + " ");
            }
            System.out.println();
        }
        else traitementMessage(message);
    }

    private void traitementMessage(String message) {
        System.out.println("\t" + message);
    }

}
