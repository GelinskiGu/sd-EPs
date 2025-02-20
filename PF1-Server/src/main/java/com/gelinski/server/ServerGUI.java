package com.gelinski.server;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI extends JFrame {
    private JTextField ipField;
    private JTextField portField;
    private JTextArea connectedUsersArea;
    private JButton startServerButton;
    private Server server;

    public ServerGUI() {
        setTitle("Servidor");
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        panel.setLayout(new GridLayout(5, 1));

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            ipField = new JTextField(ip);
            ipField.setEditable(false);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        portField = new JTextField();
        connectedUsersArea = new JTextArea();
        connectedUsersArea.setEditable(false);
        startServerButton = new JButton("Iniciar Servidor");

        panel.add(new JLabel("IP:"));
        panel.add(ipField);
        panel.add(new JLabel("Porta:"));
        panel.add(portField);
        panel.add(startServerButton);

        add(panel, BorderLayout.NORTH);
        /*JScrollPane comp = new JScrollPane(connectedUsersArea);
        comp.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(comp, BorderLayout.CENTER);*/


        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BorderLayout());
        usersPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel usersLabel = new JLabel("Usuários conectados");
        usersPanel.add(usersLabel, BorderLayout.NORTH);

        JScrollPane comp = new JScrollPane(connectedUsersArea);
        usersPanel.add(comp, BorderLayout.CENTER);

        add(usersPanel, BorderLayout.CENTER);


        startServerButton.addActionListener(e -> {
            int port = Integer.parseInt(portField.getText());
            portField.setEditable(false);
            startServerButton.setVisible(false);
            server = new Server(port, connectedUsersArea);
            JOptionPane.showConfirmDialog(this, "Servidor iniciado com sucesso", "Sucesso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            new Thread(server).start();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerGUI().setVisible(true));
    }
}
