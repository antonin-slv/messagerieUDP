import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Process implements Runnable {
    
    private Socket socket;
    private int number;

    public Process(Socket s, int number) {
        this.socket = s;
        this.number = number;
    }
    
    public void run() {

        java.util.Scanner in;
        java.io.PrintWriter out;

        try{
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());

            //lire dans in 
            System.out.println("Server received: " + in.nextLine());
            //traitement
            out.println("Hello, Client n°"+ number +". From Server.");
            out.flush();
            //écrire dans out

        } catch (IOException e) {
            System.out.println("IO Error");
            e.printStackTrace();
            return;
        }
        

        //traitement
        //écrire dans out

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
