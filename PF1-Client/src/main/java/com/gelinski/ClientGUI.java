package com.gelinski;

import com.gelinski.dto.response.LoginResponse;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {
    private ClientServerConnector connector;
    private JTextField hostField;
    private JTextField portField;
    private JPanel operationsPanel;
    private JButton registerButton;
    private JButton loginButton;
    private JButton logoutButton;
    private JButton readAccountButton;
    private JButton updateAccountButton;
    private JButton deleteAccountButton;
    private JButton createCategoryButton;
    private JButton readCategoryButton;
    private JButton updateCategoryButton;
    private JButton deleteCategoryButton;
    private Gson gson = new Gson();

    public ClientGUI() {
        connector = new ClientServerConnector();

        setTitle("Client Operations");
        setSize(800, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel connectionPanel = new JPanel();
        connectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        connectionPanel.setLayout(new GridLayout(3, 2, 10, 10));

        connectionPanel.add(new JLabel("Host:"));
        hostField = new JTextField();
        connectionPanel.add(hostField);

        connectionPanel.add(new JLabel("Porta:"));
        portField = new JTextField();
        connectionPanel.add(portField);

        JButton disconectButton = new JButton("Desconectar");
        connectionPanel.add(disconectButton);

        JButton connectButton = new JButton("Conectar");
        connectButton.addActionListener(e -> {
            String host = hostField.getText();
            int port = Integer.parseInt(portField.getText());
            connector.connect(host, port);
            registerButton.setVisible(true);
            loginButton.setVisible(true);
            logoutButton.setVisible(false);
            readAccountButton.setVisible(false);
            updateAccountButton.setVisible(false);
            deleteAccountButton.setVisible(false);
            createCategoryButton.setVisible(false);
            readCategoryButton.setVisible(false);
            updateCategoryButton.setVisible(false);
            deleteCategoryButton.setVisible(false);
            operationsPanel.setVisible(true);
        });
        connectionPanel.add(connectButton);

        mainPanel.add(connectionPanel, BorderLayout.NORTH);

        operationsPanel = new JPanel();
        operationsPanel.setLayout(new GridLayout(10, 1, 10, 15));
        operationsPanel.setVisible(false);

        registerButton = new JButton("Cadastrar usuário");
        registerButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nome:");
            String user = JOptionPane.showInputDialog("Usuário:");
            String password = JOptionPane.showInputDialog("Senha:");
            String createAccountRequest = connector.createAccount(name, user, password);
            String response = connector.sendToServer(createAccountRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        registerButton.setVisible(false);
        operationsPanel.add(registerButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String password = JOptionPane.showInputDialog("Senha:");
            String loginRequest = connector.login(user, password);
            String response = connector.sendToServer(loginRequest);
            LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
            connector.token = loginResponse.getToken();
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
            logoutButton.setVisible(true);
            readAccountButton.setVisible(true);
            updateAccountButton.setVisible(true);
            deleteAccountButton.setVisible(true);
            createCategoryButton.setVisible(true);
            readCategoryButton.setVisible(true);
            updateCategoryButton.setVisible(true);
            deleteCategoryButton.setVisible(true);
        });
        loginButton.setVisible(false);
        operationsPanel.add(loginButton);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            String logoutRequest = connector.logout();
            String response = connector.sendToServer(logoutRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        logoutButton.setVisible(false);
        operationsPanel.add(logoutButton);

        readAccountButton = new JButton("Ler conta");
        readAccountButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String readAccountRequest = connector.readAccount(user);
            String response = connector.sendToServer(readAccountRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        readAccountButton.setVisible(false);
        operationsPanel.add(readAccountButton);

        updateAccountButton = new JButton("Atualizar conta");
        updateAccountButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String password = JOptionPane.showInputDialog("Senha:");
            String name = JOptionPane.showInputDialog("Nome:");
            String updateAccountRequest = connector.updateAccount(user, password, name);
            String response = connector.sendToServer(updateAccountRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        updateAccountButton.setVisible(false);
        operationsPanel.add(updateAccountButton);

        deleteAccountButton = new JButton("Deletar conta");
        deleteAccountButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String deleteAccountRequest = connector.deleteAccount(user);
            String response = connector.sendToServer(deleteAccountRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        deleteAccountButton.setVisible(false);
        operationsPanel.add(deleteAccountButton);

        createCategoryButton = new JButton("Criar categoria");
        createCategoryButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nome da categoria:");
            String description = JOptionPane.showInputDialog("Descrição da categoria:");
            String createCategoryRequest = connector.createCategory(name, description);
            String response = connector.sendToServer(createCategoryRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        createCategoryButton.setVisible(false);
        operationsPanel.add(createCategoryButton);

        readCategoryButton = new JButton("Ler categoria");
        readCategoryButton.addActionListener(e -> {
            String readCategoryRequest = connector.readCategory();
            String response = connector.sendToServer(readCategoryRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        readCategoryButton.setVisible(false);
        operationsPanel.add(readCategoryButton);

        updateCategoryButton = new JButton("Atualizar categoria");
        updateCategoryButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID da categoria:");
            String name = JOptionPane.showInputDialog("Nome da categoria:");
            String description = JOptionPane.showInputDialog("Descrição da categoria:");
            String updateCategoryRequest = connector.updateCategory(id, name, description);
            String response = connector.sendToServer(updateCategoryRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        updateCategoryButton.setVisible(false);
        operationsPanel.add(updateCategoryButton);

        deleteCategoryButton = new JButton("Deletar categoria");
        deleteCategoryButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID da categoria:");
            String deleteCategoryRequest = connector.deleteCategory(id);
            String response = connector.sendToServer(deleteCategoryRequest);
            System.out.println("RESPONSE: " + response);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        deleteCategoryButton.setVisible(false);
        operationsPanel.add(deleteCategoryButton);

        mainPanel.add(operationsPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI().setVisible(true));
    }
}
