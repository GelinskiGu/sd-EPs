package com.gelinski.service;

import com.gelinski.dto.BaseRequestDTO;
import com.gelinski.dto.BaseResponseDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketCommunicationService {
    public static void startServer() throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10007);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10007.");
            System.exit(1);
        }

        Socket clientSocket = null;

        try {
            System.out.println ("Waiting for Client");
            clientSocket = serverSocket.accept();
        }
        catch (IOException e)
        {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        ObjectOutputStream out = new ObjectOutputStream(
                clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                clientSocket.getInputStream());

        BaseRequestDTO request = null;
        BaseResponseDTO response = null;

        try {
            request = (BaseRequestDTO) in.readObject();
        }
        catch (Exception ex)
        {
            System.out.println (ex.getMessage());
        }


        System.out.println ("Server recieved point: " + request + " from Client");

        response = new BaseResponseDTO();
        System.out.println ("Server sending point: " + response + " to Client");
        out.writeObject(response);
        out.flush();


        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
