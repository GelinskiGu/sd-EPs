package com.gelinski;

import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.request.LogoutRequest;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Scanner;

public class ClientServerConnector {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;
    private static final Gson gson = new Gson();

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
                    processResponse(responseLogin);
                    break;
                case "3":
                    String logoutSerializedRequest = logout(scanner);
                    System.out.println("INFO: Sending logout operation to server.");
                    String responseLogout = sendToServer(logoutSerializedRequest);
                    processResponse(responseLogout);
                    running = false;
                    break;
                default:
                    System.out.println("Operação inválida.");
            }
        }
    }

    private static String logout(Scanner scanner) {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setOp("6");
        System.out.println("Token: ");
        logoutRequest.setToken(scanner.nextLine());
        return gson.toJson(logoutRequest);
    }

    private static String login(Scanner scanner) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setOp("5");
        System.out.println("User: ");
        loginRequest.setUser(scanner.nextLine());
        System.out.println("Password: ");
        loginRequest.setPassword(scanner.nextLine());
        return gson.toJson(loginRequest);
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

    private void processResponse(String response) {
        if(Objects.isNull(response)) {
            System.out.println("ERROR: Null Response");
            return;
        }

        System.out.println("RESPONSE: " + response);
    }
}
