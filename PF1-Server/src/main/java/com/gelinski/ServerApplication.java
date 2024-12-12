package com.gelinski;

import com.gelinski.service.ServerSocketCommunicationService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.sql.SQLException;

@RequiredArgsConstructor
public class ServerApplication {
    public static void main(String[] args) throws IOException, SQLException {
        ServerSocketCommunicationService.startServer();

//        new Thread(() -> new Server(22001)).start();
    }
}