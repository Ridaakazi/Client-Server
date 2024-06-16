# Client-Server README.md
This project is a simple file transfer system that consists of a multi-threaded server and a client application. The server allows clients to request a list of text files and upload new files. This project was created for the Networks module (COMP2221) programming assignment.

## Overview

The server application runs continuously and uses an Executor to manage a fixed thread pool with 20 connections. It can list files in a specific directory and accept new file uploads from clients. The client application can send commands to the server to list files or upload a new file.

## Project Structure

![image](https://github.com/Ridaakazi/Client-Server/assets/123863826/634a2045-3e65-43f0-98bc-2e1d546edd1e)

markdown
Copy code

- `Client.java`: The client application file.
- `Server.java`: The server application file.
- `serverFiles/`: Directory containing server files.
- `lipsum1.txt`: Example text file on the server.
- `lipsum2.txt`: Example text file in the client directory for upload.

### Server Application

- **Continuous Operation**: The server must run continuously.
- **Thread Management**: Use an Executor to manage a fixed thread pool with 20 connections.
- **File Listing**: Upon a client's request, query the local folder `serverFiles` and return a list of the files found there to the same client.
- **File Upload**: Receive a request from a client to upload a new file to `serverFiles`. If a file with the same name already exists, return an error to the client; otherwise transfer the file from the client and save it to `serverFiles`.
- **Logging**: Create the file `log.txt` in the server directory and log every valid client request, with one line per request, in the following format:
date|time|client IP address|request

where `request` is one of `list` or `put`. Do not add other rows (e.g., headers) to the log file. Note that you must create the log file, not overwrite or append an existing file. Any `log.txt` file in your submission will be deleted at the start of the assessment.

### Client Application

- **Commands**: Accept the following commands as command-line arguments, and perform the stated task:
- `list`: Lists all of the files in the server’s folder `serverFiles`.
- `put <filename>`: Uploads the file `<filename>` to the server to be added to `serverFiles`, or returns an error message to say that this file already exists.
- **Command Execution**: Exit after completing each command.

## How to Run

1. **Compile the Server**: 
cd cwk/server
javac Server.java

2. **Run the Server**:
java Server

3. **Compile the Client**:
cd ../client
javac Client.java

4. **Run the Client**:
java Client list
java Client put lipsum2.txt

