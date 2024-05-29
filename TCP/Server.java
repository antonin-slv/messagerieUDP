//sockets : 
import java.net.ServerSocket; // pour le socket d'écoute principal
import java.net.Socket;//pour les sockets de communication "individuels"
//Threading
import java.util.concurrent.ExecutorService;
import java.io.IOException;
//I-O
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class Server {
    @SuppressWarnings("resource")
    public static void main(String[] args) {

        ServerSocket listener = null;
        try {
            listener = new ServerSocket(12345);
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        
        int[] range = {50, 100};
        String ipServer = "localhost";
        boolean[] openclosed = testPort(range, ipServer);
        
        System.out.println("Ports libres : ");
        for(int i = 0; i < openclosed.length; i++) {
            if(openclosed[i]) {
                System.out.println(i + range[0]);
            }
        }
        

        ExecutorService pool = Executors.newFixedThreadPool(20);
        int i = 0;
        while(true) {
            System.out.println("Waiting for a connection "+ i);
            Socket new_task;
            try {
                new_task = listener.accept();
                pool.execute(new Process(new_task,i));
                System.out.println("Connection "+ i + " done");
                
            } catch(IOException e) {
                e.printStackTrace();
                System.out.println("Connection "+ i +" failed");
                continue;
            }
            i += 1;
        }

    }
    public static boolean[] testPort(int[] range, String ip){
        boolean[] openclosed = new boolean[range[1] - range[0] + 1];
        for(int i = range[0]; i <= range[1]; i++) {
            try {
                Socket s = new Socket(ip, i);
                s.close();
                openclosed[i - range[0]] = true;
            } catch(IOException e) {
                openclosed[i - range[0]] = false;
            }
        }
        return openclosed;

    }

}