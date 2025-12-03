package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    public LoginWindow() {
        setTitle("Login");
        setSize(360, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 8, 8));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(new JLabel());
        add(new JLabel());
        add(loginButton);
        add(backButton);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            SOAPClient client = new SOAPClient();
            int userId;
            try {
                userId = client.login(username, password);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Login failed (service error).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userId > 0) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                MainMenuWindow main = new MainMenuWindow(userId, username);
                main.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed (invalid credentials).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            new StartWindow().setVisible(true);
            dispose();
        });

        setLocationRelativeTo(null);
    }
}