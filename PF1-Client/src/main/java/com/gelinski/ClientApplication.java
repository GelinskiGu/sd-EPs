package com.gelinski;

import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.dto.request.LoginRequest;
import com.gelinski.dto.request.LogoutRequest;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientApplication {
    private static final Gson gson = new Gson();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

    public static void main(String[] args) throws IOException {
        ClientServerConnector connector = new ClientServerConnector();

        connector.connect();
    }
}