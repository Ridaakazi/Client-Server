import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    

    // Constructor for new client
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        //opens input nd output streams 
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Reads the client request
            String request = in.readLine();
            if ("list".equals(request)) {
                listFiles(out);
            } else if ("put".equals(request)) {
                // Reads the filename from the client
                String filename = in.readLine(); 
                if (filename != null) {
                    out.println("filenameReceived"); 
                    receiveFile(filename, clientSocket.getInputStream(), out);;
                } else {
                    System.out.println("Error: No filename received from the client.");
                }
            }
            // Getting  required information for Log file
            logRequest(request, clientSocket.getInetAddress().getHostAddress()); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for the log file
    private void logRequest(String request, String ipAddress) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("%s|%s|%s", timestamp, ipAddress, request);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to list files names on the server
    private void listFiles(PrintWriter out) {
        File serverDirectory = new File("serverFiles");
        File[] files = serverDirectory.listFiles();
        if (files != null) {
            out.println("Listing files:");
            for (File file : files) {
                out.println(file.getName());
            }
        }
    }

    // method for if file exists or to upload file 
    private void receiveFile(String filename, InputStream inputStream, PrintWriter out) {
        try {
            File file = new File("serverFiles", filename);
            if (file.exists()) {
                // Send message to client
                out.println("exists"); 
                out.flush(); 
                return; 
            }

            // Send confirmation to the client
            out.println("filenameReceived"); 
            out.flush(); 

            // to write to the file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            return; 
        } catch (IOException e) {
            e.printStackTrace();
            // Send error message to client
            out.println("Error: " + e.getMessage()); 
            out.flush(); 
            return; 
        }
    }

}
