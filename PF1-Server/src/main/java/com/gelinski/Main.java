package com.gelinski;

import com.gelinski.service.ServerSocketCommunicationService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocketCommunicationService.startServer();
    }
}