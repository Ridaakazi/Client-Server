import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {

        //For displaying new log file
        File logFile = new File("log.txt");
        if (logFile.exists()) {
            logFile.delete();
        }

        // Creating thread pool of 20 threads
        ExecutorService executor = Executors.newFixedThreadPool(20); 

        try (ServerSocket serverSocket = new ServerSocket(9100)) {
            // System.out.println("Server started.");

            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                // Execute ClientHandler in a separate thread
                executor.execute(new ClientHandler(clientSocket)); 
        }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //closes the executor when exiting the server 
            executor.shutdown(); 
        }
    }
}

