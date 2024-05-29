package UDP;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        if (args.length != 1) {
            System.out.println("Wrong argument provided.");
        } else {
            if(args[0].equals("serv")){
                System.out.println("Server mode");
                Server server = new Server();
                server.run();
            }
            else if(args[0].equals("cli")){
                System.out.println("Client mode");
                Client client = new Client();
                client.run();
            }
            else{
                System.out.println("Wrong argument provided.");
            }
        }
    }
}
