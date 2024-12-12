package com.gelinski;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientApplication {
    private static final Gson gson = new Gson();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

    public static void main(String[] args) {
        ClientServerConnector connector = new ClientServerConnector();

        connector.connect();
    }
}