package com.gelinski;

import javax.swing.*;
import java.awt.*;

public class ClientGUITest {
    private JTextField HostField;
    private JPanel panel1;
    private JTextField PortField;
    private JLabel HostLabel;
    private JLabel PortLabel;
    private JButton conectarButton;
    private JPanel operationsPanel;
    private ClientServerConnector connector;

    public ClientGUITest() {
        connector = new ClientServerConnector();

        operationsPanel = new JPanel();
        operationsPanel.setLayout(new GridLayout(10, 1));
        operationsPanel.setVisible(false);

        conectarButton.addActionListener(e -> {
            String host = HostField.getText();
            int port = Integer.parseInt(PortField.getText());
            connector.connect(host, port);
            operationsPanel.setVisible(true);
            panel1.revalidate();
            panel1.repaint();
        });

        JButton registerButton = new JButton("Cadastrar usuário");
        registerButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nome:");
            String user = JOptionPane.showInputDialog("Usuário:");
            String password = JOptionPane.showInputDialog("Senha:");
            String createAccountRequest = connector.createAccount(name, user, password);
            String response = connector.sendToServer(createAccountRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(registerButton);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String password = JOptionPane.showInputDialog("Senha:");
            String loginRequest = connector.login(user, password);
            String response = connector.sendToServer(loginRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(loginButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            String logoutRequest = connector.logout();
            String response = connector.sendToServer(logoutRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(logoutButton);

        JButton readAccountButton = new JButton("Ler conta");
        readAccountButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String readAccountRequest = connector.readAccount(user);
            String response = connector.sendToServer(readAccountRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(readAccountButton);

        JButton updateAccountButton = new JButton("Atualizar conta");
        updateAccountButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String password = JOptionPane.showInputDialog("Senha:");
            String name = JOptionPane.showInputDialog("Nome:");
            String updateAccountRequest = connector.updateAccount(user, password, name);
            String response = connector.sendToServer(updateAccountRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(updateAccountButton);

        JButton deleteAccountButton = new JButton("Deletar conta");
        deleteAccountButton.addActionListener(e -> {
            String user = JOptionPane.showInputDialog("Usuário:");
            String deleteAccountRequest = connector.deleteAccount(user);
            String response = connector.sendToServer(deleteAccountRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(deleteAccountButton);

        JButton createCategoryButton = new JButton("Criar categoria");
        createCategoryButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Nome da categoria:");
            String description = JOptionPane.showInputDialog("Descrição da categoria:");
            String createCategoryRequest = connector.createCategory(name, description);
            String response = connector.sendToServer(createCategoryRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(createCategoryButton);

        JButton readCategoryButton = new JButton("Ler categoria");
        readCategoryButton.addActionListener(e -> {
            String readCategoryRequest = connector.readCategory();
            String response = connector.sendToServer(readCategoryRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(readCategoryButton);

        JButton updateCategoryButton = new JButton("Atualizar categoria");
        updateCategoryButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID da categoria:");
            String name = JOptionPane.showInputDialog("Nome da categoria:");
            String description = JOptionPane.showInputDialog("Descrição da categoria:");
            String updateCategoryRequest = connector.updateCategory(id, name, description);
            String response = connector.sendToServer(updateCategoryRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(updateCategoryButton);

        JButton deleteCategoryButton = new JButton("Deletar categoria");
        deleteCategoryButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID da categoria:");
            String deleteCategoryRequest = connector.deleteCategory(id);
            String response = connector.sendToServer(deleteCategoryRequest);
            JOptionPane.showMessageDialog(null, "Resposta do servidor: " + response);
        });
        operationsPanel.add(deleteCategoryButton);

        panel1.setLayout(new BorderLayout());
        panel1.add(operationsPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ClientGUITest");
        ClientGUITest clientGUITest = new ClientGUITest();
        frame.setContentPane(clientGUITest.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
