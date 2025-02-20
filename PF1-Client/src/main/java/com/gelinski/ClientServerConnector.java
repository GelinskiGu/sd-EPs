package com.gelinski;

import com.gelinski.dto.request.*;
import com.gelinski.dto.request.announcement.*;
import com.gelinski.dto.request.category.Category;
import com.gelinski.dto.request.category.CreateCategoryRequest;
import com.gelinski.dto.request.category.DeleteCategoryRequest;
import com.gelinski.dto.request.category.ReadCategoryRequest;
import com.gelinski.dto.response.LoginResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ClientServerConnector {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    private static final Gson gson = new Gson();
    public String token;

    public ClientServerConnector() {
        this.socket = null;
        this.out = null;
        this.in = null;
        this.stdIn = null;
    }

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("INFO: Connection successfully host: " + host + " port: " + port);
        } catch (UnknownHostException e) {
            System.out.println("ERROR: Host not found" + e.getMessage());
            closeAllConnections();
        } catch (IOException e) {
            System.out.println("ERROR: I/O" + e.getMessage());
            closeAllConnections();
        }
    }

    public String sendToServer(String input) {
        out.println(input);
        System.out.println("REQUEST: " + input);

        try {
            return in.readLine();
        } catch (IOException e) {
            System.out.println("ERROR: Server Response " + e.getMessage());
            closeAllConnections();
        }

        return "{}";
    }

    public void closeAllConnections() {
        try {
            if (stdIn != null) stdIn.close();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.exit(0);
        } catch (Exception e) {
            System.out.println("ERROR" + e.getMessage());
            System.exit(0);
        }
    }

    /*public void process() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.println("Escolha uma operação: ");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Login");
            System.out.println("3 - Logout");
            System.out.println("4 - Ler conta");
            System.out.println("5 - Atualizar conta");
            System.out.println("6 - Deletar conta");
            System.out.println("7 - Criar categoria");
            System.out.println("8 - Ler categoria");
            System.out.println("9 - Atualizar categoria");
            System.out.println("10 - Deletar categoria");
            System.out.print("Operação: ");
            String operation = scanner.nextLine();

            switch (operation) {
                case "1":
                    String createAccountSerializedRequest = createAccount(scanner);
                    System.out.println("INFO: Sending create account operation to server.");
                    String responseCreate = sendToServer(createAccountSerializedRequest);
                    processResponse(responseCreate);
                    break;
                case "2":
                    String loginSerializedRequest = login(scanner);
                    System.out.println("INFO: Sending login operation to server.");
                    String responseLogin = sendToServer(loginSerializedRequest);
                    LoginResponse loginResponse = gson.fromJson(responseLogin, LoginResponse.class);
                    token = loginResponse.getToken();
                    processResponse(responseLogin);
                    break;
                case "3":
                    String logoutSerializedRequest = logout(scanner);
                    System.out.println("INFO: Sending logout operation to server.");
                    String responseLogout = sendToServer(logoutSerializedRequest);
                    processResponse(responseLogout);
                    running = false;
                    break;
                case "4":
                    String readAccountSerializedRequest = readAccount(scanner);
                    System.out.println("INFO: Sending read account operation to server.");
                    String responseRead = sendToServer(readAccountSerializedRequest);
                    processResponse(responseRead);
                    break;
                case "5":
                    String updateAccountSerializedRequest = updateAccount(scanner);
                    System.out.println("INFO: Sending update account operation to server.");
                    String responseUpdate = sendToServer(updateAccountSerializedRequest);
                    processResponse(responseUpdate);
                    break;
                case "6":
                    String deleteAccountSerializedRequest = deleteAccount(scanner);
                    System.out.println("INFO: Sending delete account operation to server.");
                    String responseDelete = sendToServer(deleteAccountSerializedRequest);
                    processResponse(responseDelete);
                    break;
                case "7":
                    String createCategorySerializedRequest = createCategory(scanner);
                    System.out.println("INFO: Sending create category operation to server.");
                    String responseCreateCategory = sendToServer(createCategorySerializedRequest);
                    processResponse(responseCreateCategory);
                    break;
                case "8":
                    String readCategorySerializedRequest = readCategory(scanner);
                    System.out.println("INFO: Sending read category operation to server.");
                    String responseReadCategory = sendToServer(readCategorySerializedRequest);
                    processResponse(responseReadCategory);
                    break;
                case "9":
                    String updateCategorySerializedRequest = updateCategory(scanner);
                    System.out.println("INFO: Sending update category operation to server.");
                    String responseUpdateCategory = sendToServer(updateCategorySerializedRequest);
                    processResponse(responseUpdateCategory);
                    break;
                case "10":
                    String deleteCategorySerializedRequest = deleteCategory(scanner);
                    System.out.println("INFO: Sending delete category operation to server.");
                    String responseDeleteCategory = sendToServer(deleteCategorySerializedRequest);
                    processResponse(responseDeleteCategory);
                    break;
                default:
                    System.out.println("Operação inválida.");
            }
        }
    }*/

    public String createAccount(String name, String user, String password) {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOp("1");
        createAccountRequest.setName(name);
        createAccountRequest.setUser(user);
        createAccountRequest.setPassword(password);
        return gson.toJson(createAccountRequest);
    }

    public String login(String user, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setOp("5");
        loginRequest.setUser(user);
        loginRequest.setPassword(password);
        return gson.toJson(loginRequest);
    }

    public String logout() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setOp("6");
        logoutRequest.setToken(token);
        return gson.toJson(logoutRequest);
    }

    public String readAccount(String user) {
        ReadAccountRequest readAccountRequest = new ReadAccountRequest();
        readAccountRequest.setOp("2");
        readAccountRequest.setUser(user);
        readAccountRequest.setToken(token);
        return gson.toJson(readAccountRequest);
    }

    public String updateAccount(String user, String password, String name) {
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setOp("3");
        updateAccountRequest.setUser(user);
        updateAccountRequest.setPassword(password);
        updateAccountRequest.setName(name);
        updateAccountRequest.setToken(token);
        return gson.toJson(updateAccountRequest);
    }

    public String deleteAccount(String user) {
        DeleteAccountRequest deleteAccountRequest = new DeleteAccountRequest();
        deleteAccountRequest.setOp("4");
        deleteAccountRequest.setUser(user);
        deleteAccountRequest.setToken(token);
        return gson.toJson(deleteAccountRequest);
    }

    public String createCategory(String name, String description) {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setOp("7");
        createCategoryRequest.setToken(token);
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        createCategoryRequest.setCategories(List.of(category));
        return gson.toJson(createCategoryRequest);
    }

    public String readCategory() {
        ReadCategoryRequest readCategoryRequest = new ReadCategoryRequest();
        readCategoryRequest.setOp("8");
        readCategoryRequest.setToken(token);
        return gson.toJson(readCategoryRequest);
    }

    public String updateCategory(String id, String name, String description) {
        CreateCategoryRequest updateCategoryRequest = new CreateCategoryRequest();
        updateCategoryRequest.setOp("9");
        updateCategoryRequest.setToken(token);
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        updateCategoryRequest.setCategories(List.of(category));
        return gson.toJson(updateCategoryRequest);
    }

    public String deleteCategory(String id) {
        DeleteCategoryRequest deleteCategoryRequest = new DeleteCategoryRequest();
        deleteCategoryRequest.setOp("10");
        deleteCategoryRequest.setToken(token);
        deleteCategoryRequest.setCategoryIds(List.of(id));
        return gson.toJson(deleteCategoryRequest);
    }

    public String createAnnouncement(String title, String text, String categoryId) {
        CreateAnnouncementRequest createAnnouncementRequest = new CreateAnnouncementRequest();
        createAnnouncementRequest.setOp("11");
        createAnnouncementRequest.setToken(token);
        createAnnouncementRequest.setTitle(title);
        createAnnouncementRequest.setText(text);
        createAnnouncementRequest.setCategoryId(categoryId);
        return gson.toJson(createAnnouncementRequest);
    }

    public String readAnnouncements() {
        ReadAnnouncementsRequest readAnnouncementsRequest = new ReadAnnouncementsRequest();
        readAnnouncementsRequest.setOp("12");
        readAnnouncementsRequest.setToken(token);
        return gson.toJson(readAnnouncementsRequest);
    }

    public String updateAnnouncements(String id, String title, String text, String categoryId) {
        UpdateAnnouncementRequest updateAnnouncementRequest = new UpdateAnnouncementRequest();
        updateAnnouncementRequest.setOp("13");
        updateAnnouncementRequest.setToken(token);
        updateAnnouncementRequest.setId(id);
        updateAnnouncementRequest.setTitle(title);
        updateAnnouncementRequest.setText(text);
        updateAnnouncementRequest.setCategoryId(categoryId);
        return gson.toJson(updateAnnouncementRequest);
    }

    public String deleteAnnouncements(String id) {
        DeleteAnnouncementRequest deleteAnnouncementRequest = new DeleteAnnouncementRequest();
        deleteAnnouncementRequest.setOp("14");
        deleteAnnouncementRequest.setToken(token);
        deleteAnnouncementRequest.setId(id);
        return gson.toJson(deleteAnnouncementRequest);
    }

    public String subscribeToCategory(String categoryId) {
        SubscribeToCategoryRequest subscribeToCategoryRequest = new SubscribeToCategoryRequest();
        subscribeToCategoryRequest.setOp("15");
        subscribeToCategoryRequest.setToken(token);
        subscribeToCategoryRequest.setCategoryId(categoryId);
        return gson.toJson(subscribeToCategoryRequest);
    }

    public String unsubscribeToCategory(String categoryId) {
        UnsubscribeToCategoryRequest unsubscribeToCategoryRequest = new UnsubscribeToCategoryRequest();
        unsubscribeToCategoryRequest.setOp("16");
        unsubscribeToCategoryRequest.setToken(token);
        unsubscribeToCategoryRequest.setCategoryId(categoryId);
        return gson.toJson(unsubscribeToCategoryRequest);
    }
}
