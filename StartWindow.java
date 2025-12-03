package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class StartWindow extends JFrame {
    public StartWindow() {
        setTitle("Tic Tac Toe - Start");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel lbl = new JLabel("Welcome to Tic Tac Toe", SwingConstants.CENTER);
        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register");

        loginBtn.addActionListener(e -> {
            new LoginWindow().setVisible(true);
            dispose();
        });

        regBtn.addActionListener(e -> {
            new RegisterWindow().setVisible(true);
            dispose();
        });

        add(lbl);
        add(loginBtn);
        add(regBtn);

        setLocationRelativeTo(null);
    }
}