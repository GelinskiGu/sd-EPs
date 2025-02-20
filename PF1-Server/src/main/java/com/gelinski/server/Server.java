package com.gelinski.server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server implements Runnable {
    private int port;
    private JTextArea connectedUsersArea;
    private Set<String> connectedUsers;

    public Server(int port, JTextArea connectedUsersArea) {
        this.port = port;
        this.connectedUsersArea = connectedUsersArea;
        this.connectedUsers = new HashSet<>();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);
            while (true) {
                System.out.println("Waiting for Client");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection successful");
                new Thread(new ClientHandler(clientSocket, connectedUsers, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateConnectedUsersArea() {
        SwingUtilities.invokeLater(() -> {
            connectedUsersArea.setText("");
            for (String user : connectedUsers) {
                connectedUsersArea.append("User: " + user + "\n");
            }
        });
    }
}
