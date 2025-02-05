package com.gelinski.service;


import com.gelinski.dto.BaseRequestDTO;
import com.gelinski.dto.request.*;
import com.gelinski.dto.request.category.CreateCategoryRequest;
import com.gelinski.dto.request.category.DeleteCategoryRequest;
import com.gelinski.dto.request.category.ReadCategoryRequest;
import com.gelinski.dto.response.account.*;
import com.gelinski.dto.response.category.CreateCategoryResponse;
import com.gelinski.dto.response.category.DeleteCategoryResponse;
import com.gelinski.dto.response.category.ReadCategoryResponse;
import com.gelinski.dto.response.category.UpdateCategoryResponse;
import com.gelinski.service.account.*;
import com.gelinski.service.category.CreateCategoryService;
import com.gelinski.service.category.DeleteCategoryService;
import com.gelinski.service.category.ReadCategoryService;
import com.gelinski.service.category.UpdateCategoryService;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ServerSocketCommunicationService {
    private static final Gson gson = new Gson();
    private final List<String> loggedUsers = new ArrayList<>();

    public void startServer() throws IOException {
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

                loggedUsers.clear();
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Accept failed.");
                System.exit(1);
            }
        }

    }

    private String processRequest(BaseRequestDTO request, String message) {
        if (Objects.isNull(request.getOp())) {
            throw new IllegalArgumentException("Invalid operation");
        }

        return switch (request.getOp()) {
            case "1" -> {
                CreateAccountService createAccountService = new CreateAccountService();
                CreateAccountResponse response = createAccountService.createAccount(gson.fromJson(message, CreateAccountRequest.class));
                yield gson.toJson(response);
            }
            case "2" -> {
                ReadAccountService readAccountService = new ReadAccountService();
                ReadAccountResponse response = readAccountService.readAccount(gson.fromJson(message, ReadAccountRequest.class), loggedUsers.get(0));
                yield gson.toJson(response);
            }
            case "3" -> {
                UpdateAccountService updateAccountService = new UpdateAccountService();
                UpdateAccountResponse response = updateAccountService.updateAccount(gson.fromJson(message, UpdateAccountRequest.class), loggedUsers.get(0));
                yield gson.toJson(response);
            }
            case "4" -> {
                DeleteAccountService deleteAccountService = new DeleteAccountService();
                DeleteAccountResponse response = deleteAccountService.deleteAccount(gson.fromJson(message, DeleteAccountRequest.class), loggedUsers.get(0));
                yield gson.toJson(response);
            }
            case "5" -> {
                LoginService loginService = new LoginService();
                LoginResponse response = loginService.login(gson.fromJson(message, LoginRequest.class));
                loggedUsers.add(response.getToken());
                yield gson.toJson(response);
            }
            case "6" -> {
                LogoutService logoutService = new LogoutService();
                LogoutResponse response = logoutService.logout(gson.fromJson(message, LogoutRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "7" -> {
                CreateCategoryService createCategoryService = new CreateCategoryService();
                CreateCategoryResponse response = createCategoryService.createCategory(gson.fromJson(message, CreateCategoryRequest.class), loggedUsers.get(0));
                yield gson.toJson(response);
            }
            case "8" -> {
                ReadCategoryService readCategoryService = new ReadCategoryService();
                ReadCategoryResponse response = readCategoryService.readCategories(gson.fromJson(message, ReadCategoryRequest.class), loggedUsers.get(0));
                yield gson.toJson(response);
            }
            case "9" -> {
                UpdateCategoryService updateCategoryService = new UpdateCategoryService();
                UpdateCategoryResponse response = updateCategoryService.updateCategory(gson.fromJson(message, CreateCategoryRequest.class), loggedUsers.get(0));
                yield gson.toJson(response);
            }
            case "10" -> {
                DeleteCategoryService deleteCategoryService = new DeleteCategoryService();
                DeleteCategoryResponse response = deleteCategoryService.deleteCategory(gson.fromJson(message, DeleteCategoryRequest.class), loggedUsers.get(0));
                yield gson.toJson(response);
            }
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}
