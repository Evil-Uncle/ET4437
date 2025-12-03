
package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends JFrame {
    public RegisterWindow() {
        setTitle("Register");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 8, 8));

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
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SOAPClient client = new SOAPClient();
            String result = client.register(username, password, name, surname);
            if (!result.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, "Registered! Your user id: " + result);
                MainMenuWindow main = new MainMenuWindow(Integer.parseInt(result), username);
                main.setVisible(true);
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