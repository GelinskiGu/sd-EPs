package com.gelinski.service;

import com.gelinski.dto.BaseRequestDTO;
import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.request.LogoutRequest;
import com.gelinski.dto.response.CreateAccountResponse;
import com.gelinski.dto.response.LoginResponse;
import com.gelinski.dto.response.LogoutResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.Scanner;

public class ServerSocketCommunicationService {
    private static final Gson gson = new Gson();


    public static void startServer() throws IOException {
        ServerSocket serverSocket = null;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the server port: ");
        int port = scanner.nextInt();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 10007.");
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = null;

            try {
                System.out.println("Waiting for Client");
                clientSocket = serverSocket.accept();


                System.out.println("Connection successful");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.equals("0")) break;

                        System.out.println("REQUEST: " + inputLine);
                        String res = processRequest(gson.fromJson(inputLine, BaseRequestDTO.class), inputLine);
                        System.out.println("RESPONSE: " + res);
                        out.println(res);
                    }
                } catch (SocketException e) {
                    System.out.println("Connection closed by client");
                }

                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Accept failed.");
                System.exit(1);
            }
        }

    }

    private static String processRequest(BaseRequestDTO request, String message) {
        if (Objects.isNull(request.getOp())) {
            throw new IllegalArgumentException("Invalid operation");
        }

        return switch (request.getOp()) {

            case 1 -> {
                CreateAccountService createAccountService = new CreateAccountService();
                CreateAccountResponse response = createAccountService.createAccount(gson.fromJson(message, CreateAccountRequest.class));
                yield gson.toJson(response);
            }
            case 5 -> {
                LoginService loginService = new LoginService();
                LoginResponse response = loginService.login(gson.fromJson(message, LoginRequest.class));
                yield gson.toJson(response);
            }
            case 6 -> {
                LogoutService logoutService = new LogoutService();
                LogoutResponse response = logoutService.logout(gson.fromJson(message, LogoutRequest.class));
                yield gson.toJson(response);
            }
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}
