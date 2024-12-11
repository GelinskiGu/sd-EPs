package com.gelinski;

import com.gelinski.service.ServerSocketCommunicationService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ServerApplication {
    public static void main(String[] args) throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("sd");
        factory.createEntityManager();
        ServerSocketCommunicationService.startServer();
    }
}