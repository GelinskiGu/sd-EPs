package com.gelinski;

import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.request.LogoutRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientApplication {
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Qual host deseja se conectar?");
        String host = scanner.nextLine();

        System.out.print("Qual porta deseja se conectar?");
        int port = Integer.parseInt(scanner.nextLine());

        Socket echoSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            echoSocket = new Socket(host, port);

            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + host);
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            System.out.println("Escolha uma operação: ");
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Login");
            System.out.println("3 - Logout");
            System.out.print("Operação: ");
            int operation = Integer.parseInt(scanner.nextLine());

            switch (operation) {
                case 1:
                    CreateAccountRequest createAccountRequest = new CreateAccountRequest();
                    // Preencher os dados do createAccountRequest
                    createAccountRequest.setOp(1);
                    String createAccountSerializedRequest = gson.toJson(createAccountRequest);
                    out.writeObject(createAccountSerializedRequest);
                    out.flush();
                    break;
                case 2:
                    LoginRequest loginRequest = new LoginRequest();
                    // Preencher os dados do loginRequest
                    loginRequest.setOp(5);
                    String loginSerializedRequest = gson.toJson(loginRequest);
                    out.writeObject(loginSerializedRequest);
                    out.flush();
                    break;
                case 3:
                    LogoutRequest logoutRequest = new LogoutRequest();
                    // Preencher os dados do logoutRequest
                    logoutRequest.setOp(6);
                    String logoutSerializedRequest = gson.toJson(logoutRequest);
                    out.writeObject(logoutSerializedRequest);
                    out.flush();
                    running = false;
                    break;
                default:
                    System.out.println("Operação inválida.");
                    continue;
            }


            LoginRequest request = new LoginRequest();
            request.setUser("user");
            request.setPassword("password");
            request.setOp(5);

            String serializedRequest = gson.toJson(request);
            System.out.println("Sending point: " + serializedRequest + " to Server");
            out.writeObject(serializedRequest);
            out.flush();
            System.out.println("Send point, waiting for return value");

            receiveResponse(in, out);
        }

        echoSocket.close();
    }

    private static void receiveResponse(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        String response = null;

        try {
            response = (String) in.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        System.out.println("Got point: " + response + " from Server");

        out.close();
        in.close();
    }
}