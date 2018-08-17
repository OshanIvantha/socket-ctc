package com.ivantha;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CTC Server that will handle multiple connections with RTU clients.
 * CTCServer is a Singleton.
 */
public class CTCServer {

    private static CTCServer instance = new CTCServer();

    private CTCServer() {
    }

    public static CTCServer getInstance() {
        return instance;
    }

    public static void registerNewClient(Socket socket, int clientNumber) {
        new RequestProcessor(socket, clientNumber).start();
    }

    private static class RequestProcessor extends Thread{

        private Socket socket;
        private int clientNumber;

        private RequestProcessor(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);
        }

        public void run() {
            try {
                // Decorate the streams so we can send characters and not just bytes.
                // Ensure output is flushed after every newline.
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                out.println("Hello, client #" + clientNumber + "!");

                // Get messages from the client, line by line; return the response
                while (true) {
                    String input = in.readLine();
                    if (input == null  || input.equals(".")) {
                        break;
                    }

                    out.println(processRequest(input));
                }
            } catch (IOException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }

        private int processRequest(String input) {

            // TODO : Validate the input

            int trackStatus = Integer.parseInt(input.substring(0, 1));
            int switchStatus = Integer.parseInt(input.substring(1, 2));
            System.out.println(trackStatus);
            System.out.println(switchStatus);
            if (trackStatus == 0) {
                if (switchStatus == 0) {
                    return 1;
                } else if (switchStatus == 1) {
                    return 2;
                }
            } else if (trackStatus == 1) {
                if (switchStatus == 0) {
                    // TODO
                } else if (switchStatus == 1) {
                    // TODO
                }
            }

            // Erroneous response
            return -1;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("CTC Server is running");

        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true){
                CTCServer.registerNewClient(listener.accept(), clientNumber++);
            }
        } finally {
            listener.close();
        }
    }

}
