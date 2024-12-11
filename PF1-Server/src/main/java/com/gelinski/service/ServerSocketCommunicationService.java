package com.gelinski.service;

import com.gelinski.dto.BaseRequestDTO;
import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.enums.OperationEnum;
import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.request.LogoutRequest;
import com.google.gson.Gson;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class ServerSocketCommunicationService {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("sd");
    private static final DatabaseService databaseService = new DatabaseService(emf);
    private static final Gson gson = new Gson();

    public static void startServer() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(10007);
        }
        catch (IOException e)
        {
            System.out.println("Could not listen on port: 10007.");
            System.exit(1);
        }

        Socket clientSocket = null;

        try {
            System.out.println("Waiting for Client");
            clientSocket = serverSocket.accept();
        }
        catch (IOException e)
        {
            System.out.println("Accept failed.");
            System.exit(1);
        }

        ObjectOutputStream out = new ObjectOutputStream(
                clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                clientSocket.getInputStream());

        String optionalRequestString = null;

        try {
            optionalRequestString = (String) in.readObject();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            return;
        }

        System.out.println("Server received point: " + optionalRequestString + " from Client");

        BaseRequestDTO request = gson.fromJson(optionalRequestString, BaseRequestDTO.class);
        BaseResponseDTO response = processRequest(request, optionalRequestString);


        String serializedResponse = gson.toJson(response);
        System.out.println("Server sending point to Client: " + serializedResponse);
        out.writeObject(serializedResponse);
        out.flush();


        out.close();
        in.close();

        if (Objects.equals(request.getOp(), OperationEnum.LOGOUT.getOp())) {
            clientSocket.close();
            serverSocket.close();
            emf.close();
        }
    }

    private static BaseResponseDTO processRequest(BaseRequestDTO request, String message) {
        if (Objects.isNull(request.getOp())) {
            throw new IllegalArgumentException("Invalid operation");
        }
        return switch (request.getOp()) {
            case 1 -> {
                CreateAccountService createAccountService = new CreateAccountService(databaseService);
                yield createAccountService.createAccount(gson.fromJson(message, CreateAccountRequest.class));
            }
            case 5 -> {
                LoginService loginService = new LoginService(databaseService);
                yield loginService.login(gson.fromJson(message, LoginRequest.class));
            }
            case 6 -> {
                LogoutService logoutService = new LogoutService();
                yield logoutService.logout(gson.fromJson(message, LogoutRequest.class));
            }
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}
