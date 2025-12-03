package com.ul.et4437.tictactoe;

import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends JFrame {
    public RegisterWindow() {
        setTitle("Register");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 10, 10));
        
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel surnameLabel = new JLabel("Surname:");
        JTextField surnameField = new JTextField();
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        
        add(nameLabel); add(nameField);
        add(surnameLabel); add(surnameField);
        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(registerButton); add(backButton);
        
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            String result = WSClient.register(name, surname, username, password);
            if (!result.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, "Registration successful! User ID: " + result);
                new MainGameWindow(Integer.parseInt(result), username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed: " + result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backButton.addActionListener(e -> {
            new StartWindow().setVisible(true);
            dispose();
        });
        
        setLocationRelativeTo(null);
    }
}