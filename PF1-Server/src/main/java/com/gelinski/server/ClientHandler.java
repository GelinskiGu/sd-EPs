package com.gelinski.server;

import com.gelinski.dto.BaseRequestDTO;
import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.enums.account.DeleteAccountEnum;
import com.gelinski.dto.enums.account.ReadAccountEnum;
import com.gelinski.dto.enums.account.UpdateAccountEnum;
import com.gelinski.dto.enums.category.CreateCategoryEnum;
import com.gelinski.dto.enums.category.DeleteCategoryEnum;
import com.gelinski.dto.enums.category.ReadCategoryEnum;
import com.gelinski.dto.enums.category.UpdateCategoryEnum;
import com.gelinski.dto.request.account.*;
import com.gelinski.dto.request.announcement.CreateAnnouncementRequest;
import com.gelinski.dto.request.announcement.DeleteAnnouncementRequest;
import com.gelinski.dto.request.announcement.ReadAnnouncementRequest;
import com.gelinski.dto.request.announcement.UpdateAnnouncementRequest;
import com.gelinski.dto.request.category.CreateCategoryRequest;
import com.gelinski.dto.request.category.DeleteCategoryRequest;
import com.gelinski.dto.request.category.ReadCategoryRequest;
import com.gelinski.dto.request.category.SubscribeToCategoryRequest;
import com.gelinski.dto.response.account.*;
import com.gelinski.dto.response.announcement.ReadAnnouncementResponse;
import com.gelinski.dto.response.category.CreateCategoryResponse;
import com.gelinski.dto.response.category.DeleteCategoryResponse;
import com.gelinski.dto.response.category.ReadCategoryResponse;
import com.gelinski.dto.response.category.UpdateCategoryResponse;
import com.gelinski.service.account.*;
import com.gelinski.service.announcement.CreateAnnouncementService;
import com.gelinski.service.announcement.DeleteAnnouncementService;
import com.gelinski.service.announcement.ReadAnnouncementService;
import com.gelinski.service.announcement.UpdateAnnouncementService;
import com.gelinski.service.category.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private static final Gson gson = new Gson();
    private final Socket clientSocket;
    private final List<String> loggedUsers;

    public ClientHandler(Socket clientSocket, List<String> loggedUsers) {
        this.clientSocket = clientSocket;
        this.loggedUsers = loggedUsers;
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("0")) break;

                System.out.println("REQUEST: " + inputLine);
                String res = processRequest(gson.fromJson(inputLine, BaseRequestDTO.class), inputLine);
                System.out.println("RESPONSE: " + res);
                out.println(res);
                BaseResponseDTO response = gson.fromJson(res, BaseResponseDTO.class);
                if (Objects.equals(response.getResponse(), "130")) {
                    DeleteAccountRequest deleteAccountRequest = gson.fromJson(inputLine, DeleteAccountRequest.class);
                    if (deleteAccountRequest.getUser().equals(loggedUsers.get(0)) || deleteAccountRequest.getUser().isEmpty()) {
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("Connection closed by client");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
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
                ReadAccountResponse response = readAccountService.readAccount(gson.fromJson(message, ReadAccountRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "3" -> {
                UpdateAccountService updateAccountService = new UpdateAccountService();
                UpdateAccountResponse response = updateAccountService.updateAccount(gson.fromJson(message, UpdateAccountRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "4" -> {
                DeleteAccountService deleteAccountService = new DeleteAccountService();
                DeleteAccountResponse response = deleteAccountService.deleteAccount(gson.fromJson(message, DeleteAccountRequest.class), loggedUsers);
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
                CreateCategoryResponse response = createCategoryService.createCategory(gson.fromJson(message, CreateCategoryRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "8" -> {
                ReadCategoryService readCategoryService = new ReadCategoryService();
                ReadCategoryResponse response = readCategoryService.readCategories(gson.fromJson(message, ReadCategoryRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "9" -> {
                UpdateCategoryService updateCategoryService = new UpdateCategoryService();
                UpdateCategoryResponse response = updateCategoryService.updateCategory(gson.fromJson(message, CreateCategoryRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "10" -> {
                DeleteCategoryService deleteCategoryService = new DeleteCategoryService();
                DeleteCategoryResponse response = deleteCategoryService.deleteCategory(gson.fromJson(message, DeleteCategoryRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "11" -> {
                CreateAnnouncementService createAnnouncementService = new CreateAnnouncementService();
                BaseResponseDTO response = createAnnouncementService.createAnnouncement(gson.fromJson(message, CreateAnnouncementRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "12" -> {
                ReadAnnouncementService readAnnouncementService = new ReadAnnouncementService();
                ReadAnnouncementResponse readAnnouncementResponse = readAnnouncementService.readAnnouncement(gson.fromJson(message, ReadAnnouncementRequest.class), loggedUsers);
                yield gson.toJson(readAnnouncementResponse);
            }
            case "13" -> {
                UpdateAnnouncementService updateAnnouncementService = new UpdateAnnouncementService();
                BaseResponseDTO response = updateAnnouncementService.updateAnnouncement(gson.fromJson(message, UpdateAnnouncementRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "14" -> {
                DeleteAnnouncementService deleteAnnouncementService = new DeleteAnnouncementService();
                BaseResponseDTO response = deleteAnnouncementService.deleteAnnouncement(gson.fromJson(message, DeleteAnnouncementRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "15" -> {
                SubscribeToCategoryService subscribeToCategoryService = new SubscribeToCategoryService();
                BaseResponseDTO response = subscribeToCategoryService.subscribe(gson.fromJson(message, SubscribeToCategoryRequest.class), loggedUsers);
                yield gson.toJson(response);
            }
            case "16" -> {
                UnsubscribeToCategoryService unsubscribeToCategoryService = new UnsubscribeToCategoryService();
                BaseResponseDTO response = unsubscribeToCategoryService.unsubscribe(gson.fromJson(message, SubscribeToCategoryRequest.class), loggedUsers);
                yield gson.toJson(response);
            }

            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}
