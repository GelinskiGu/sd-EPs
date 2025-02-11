package com.gelinski;

import com.gelinski.dto.request.*;
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
    private String token;

    public ClientServerConnector() {
        this.socket = null;
        this.out = null;
        this.in = null;
        this.stdIn = null;
    }

    public void connect() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Qual host deseja se conectar?");
        String host = scanner.nextLine();

        System.out.print("Qual porta deseja se conectar?");
        int port = Integer.parseInt(scanner.nextLine());

        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("INFO: Connection successfully host: " + host + " port: " + port);
            process();
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
            System.out.println("ERROR: Server Response" + e.getMessage());
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

    public void process() {
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
    }

    private String deleteCategory(Scanner scanner) {

        DeleteCategoryRequest deleteCategoryRequest = new DeleteCategoryRequest();
        deleteCategoryRequest.setOp("10");
        System.out.println("Token: " + token);
        deleteCategoryRequest.setToken(token);

        boolean addCategory = true;
        List<String> categoryIds = new ArrayList<>();
        while (addCategory) {
            System.out.println("Category ID: ");
            categoryIds.add(scanner.nextLine());
            System.out.println("Deseja deletar mais uma categoria? (S/N)");
            String addMore = scanner.nextLine();
            if (!addMore.equalsIgnoreCase("S")) {
                addCategory = false;
            }
        }
        deleteCategoryRequest.setCategoryIds(categoryIds);
        return gson.toJson(deleteCategoryRequest);
    }

    private String updateCategory(Scanner scanner) {

        CreateCategoryRequest updateCategoryRequest = new CreateCategoryRequest();
        updateCategoryRequest.setOp("9");
        System.out.println("Token: " + token);
        updateCategoryRequest.setToken(token);

        boolean addCategory = true;
        List<Category> categories = new ArrayList<>();
        while (addCategory) {
            Category category = new Category();
            System.out.println("Category ID: ");
            category.setId(scanner.nextLine());
            System.out.println("Nome da categoria: ");
            String categoryName = scanner.nextLine();
            category.setName(categoryName);
            System.out.println("Descrição da categoria: ");
            String categoryDescription = scanner.nextLine();
            category.setDescription(categoryDescription);
            categories.add(category);
            System.out.println("Deseja atualizar mais uma categoria? (S/N)");
            String addMore = scanner.nextLine();
            if (!addMore.equalsIgnoreCase("S")) {
                addCategory = false;
            }
        }
        updateCategoryRequest.setCategories(categories);

        return gson.toJson(updateCategoryRequest);
    }

    private String readCategory(Scanner scanner) {
        ReadCategoryRequest readCategoryRequest = new ReadCategoryRequest();
        readCategoryRequest.setOp("8");
        System.out.println("Token: " + token);
        readCategoryRequest.setToken(token);
        return gson.toJson(readCategoryRequest);
    }

    private String createCategory(Scanner scanner) {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();
        createCategoryRequest.setOp("7");
        System.out.println("Token: " + token);
        createCategoryRequest.setToken(token);

        boolean addCategory = true;
        List<Category> categories = new ArrayList<>();
        while (addCategory) {
            Category category = new Category();
            System.out.println("Nome da categoria: ");
            String categoryName = scanner.nextLine();
            category.setName(categoryName);
            System.out.println("Descrição da categoria: ");
            String categoryDescription = scanner.nextLine();
            category.setDescription(categoryDescription);
            categories.add(category);
            System.out.println("Deseja adicionar mais uma categoria? (S/N)");
            String addMore = scanner.nextLine();
            if (!addMore.equalsIgnoreCase("S")) {
                addCategory = false;
            }
        }
        createCategoryRequest.setCategories(categories);
        return gson.toJson(createCategoryRequest);
    }

    private String deleteAccount(Scanner scanner) {
        DeleteAccountRequest deleteAccountRequest = new DeleteAccountRequest();
        deleteAccountRequest.setOp("4");
        System.out.println("User: ");
        deleteAccountRequest.setUser(scanner.nextLine());
        System.out.println("Token: " + token);
        deleteAccountRequest.setToken(token);
        return gson.toJson(deleteAccountRequest);
    }

    private static String createAccount(Scanner scanner) {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setOp("1");
        System.out.println("Name: ");
        createAccountRequest.setName(scanner.nextLine());
        System.out.println("User: ");
        createAccountRequest.setUser(scanner.nextLine());
        System.out.println("Password: ");
        createAccountRequest.setPassword(scanner.nextLine());
        return gson.toJson(createAccountRequest);
    }

    private String readAccount(Scanner scanner) {
        ReadAccountRequest readAccountRequest = new ReadAccountRequest();
        readAccountRequest.setOp("2");
        System.out.println("User: ");
        readAccountRequest.setUser(scanner.nextLine());
        System.out.println("Token: " + token);
        readAccountRequest.setToken(token);
        return gson.toJson(readAccountRequest);
    }

    private String updateAccount(Scanner scanner) {
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setOp("3");
        System.out.println("User: ");
        updateAccountRequest.setUser(scanner.nextLine());
        System.out.println("Password: ");
        updateAccountRequest.setPassword(scanner.nextLine());
        System.out.println("Name: ");
        updateAccountRequest.setName(scanner.nextLine());
        System.out.println("Token: " + token);
        updateAccountRequest.setToken(token);
        return gson.toJson(updateAccountRequest);
    }

    private String logout(Scanner scanner) {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setOp("6");
        System.out.println("Token: " + token);
        logoutRequest.setToken(token);
        return gson.toJson(logoutRequest);
    }

    private String login(Scanner scanner) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setOp("5");
        System.out.println("User: ");
        loginRequest.setUser(scanner.nextLine());
        System.out.println("Password: ");
        loginRequest.setPassword(scanner.nextLine());
        return gson.toJson(loginRequest);
    }

    private void processResponse(String response) {
        if(Objects.isNull(response)) {
            System.out.println("ERROR: Null Response");
            return;
        }

        System.out.println("RESPONSE: " + response);
    }
}
