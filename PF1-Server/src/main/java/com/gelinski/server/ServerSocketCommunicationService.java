/*
package com.gelinski.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerSocketCommunicationService {
    private final List<String> loggedUsers = new ArrayList<>();

    public void startServer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the server port: ");
        int port = scanner.nextInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);

            while (true) {
                System.out.println("Waiting for Client");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection successful");

                ClientHandler clientHandler = new ClientHandler(clientSocket, loggedUsers, );
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(1);
        }
    }
}
*/
