import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        //to validated cmd arguments
        if (args.length < 1) {
            System.out.println("Usage: java Client <command>");
            return;
        }
        
        String command = args[0];
        switch (command) {
            case "list":
            // if command is list listing the files names method is called
                listFiles();
                break;
            case "put":
                if (args.length < 2) {
                    System.out.println("Usage: java Client put <filename>");
                    return;
                }
                String filename = args[1];
                // if command is put upload the file method is called
                uploadFile(filename);
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }
    
    private static void listFiles() {
        // makes the connection using socket
        try (Socket socket = new Socket("localhost", 9100);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("list");
            // Receive response from the server
            String response = in.readLine();
            if (response.startsWith("Listing")) {
                // Print list 
                System.out.println(response);
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            } else {
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void uploadFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Error: File '" + filename + "' does not exist.");
            return;
        }
    
        try (Socket socket = new Socket("localhost", 9100);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             FileInputStream fileIn = new FileInputStream(file)) {
    
            // Send put command    
            out.println("put"); 
            out.flush();  
    
            // Send  out filename
            out.println(filename); 
            out.flush();    
    
            // Wait for servr response after sending filename
            String serverResponse = in.readLine(); 
            if ("filenameReceived".equals(serverResponse)) {
                // continue to send the file contents
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    socket.getOutputStream().write(buffer, 0, bytesRead);
                }
                socket.getOutputStream().flush(); 
    
                // Wait again for server response after sending file content
                serverResponse = in.readLine();
                if ("uploaded".equals(serverResponse)) {
                    System.out.println("Uploaded file " + filename);
                } else if ("exists".equals(serverResponse)) {
                    //if file already added to the server print this error
                    System.out.println("Error: File '" + filename + "' already exists on the server.");
                } else {
                    System.out.println("Uploaded file " + filename);
                }
            } else {
                System.out.println("Error: Unexpected server response2: " + serverResponse);
            }
    
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open local file '" + filename + "' for reading");
        } catch (ConnectException e) {
            System.out.println("Error: Unable to connect to the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
