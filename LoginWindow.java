package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    public LoginWindow() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        
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
        add(new JLabel()); // Spacer
        add(loginButton);
        add(backButton);
        
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            SOAPClient client = new SOAPClient();
            int userId = client.login(username, password);
            if (userId > 0) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new MainMenuWindow(userId, username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backButton.addActionListener(e -> {
            new StartWindow().setVisible(true);
            dispose();
        });
        
        setLocationRelativeTo(null);
    }
}