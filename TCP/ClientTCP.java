import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {
    @SuppressWarnings("resource")
	public static void main(String[] args) {
        
        Socket socket = null;
        Scanner in = null;
        PrintWriter out = null;

        try {
            System.out.println("Client is starting");
            socket = new Socket("localhost", 12345);
            System.out.println("Client is connected");
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            
        } catch (Exception e) {
            System.out.println("Client Error");
            e.printStackTrace();
            return;
        }
        
        //il peut y avoir des erreurs de résolution DNS ici
        
        //écrire au clavier dans out
        try {
            String message = System.console().readLine();
            out.println(message);
        } catch (Exception e) {
            System.out.println("Client Error");
            e.printStackTrace();
            return;
        }
        out.flush();
        //lire dans in
        System.out.println(in.nextLine());
        System.out.println("Client is done");
        
       
        
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("socket closing Error");
            e.printStackTrace();
            return;
        }

    }


}
