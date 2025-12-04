package com.mycompany.tttgame;
// The Games Start Up Screen
import javax.swing.*;
import java.awt.*;

public class StartWindow extends JFrame {
    public StartWindow() {
        setTitle("Tic Tac Toe - Start");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));
        
        JLabel titleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        add(titleLabel);
        add(loginButton);
        add(registerButton);
        
        loginButton.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });
        
        registerButton.addActionListener(e -> {
            new RegisterWindow().setVisible(true);
            dispose();
        });
        
        setLocationRelativeTo(null);
    }

}
