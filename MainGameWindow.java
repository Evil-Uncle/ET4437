package com.ul.et4437.tictactoe;

import javax.swing.*;
import java.awt.*;

public class MainGameWindow extends JFrame {
    private int userId;
    private String username;
    
    public MainGameWindow(int userId, String username) {
        this.userId = userId;
        this.username = username;
        
        setTitle("Tic Tac Toe - Welcome " + username);
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 15, 15));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (ID: " + userId + ")", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton createGameButton = new JButton("Create New Game");
        JButton openGamesButton = new JButton("Show Open Games");
        JButton scoreButton = new JButton("My Score Board");
        JButton leaderboardButton = new JButton("Leader Board");
        JButton logoutButton = new JButton("Logout");
        
        add(welcomeLabel);
        add(createGameButton);
        add(openGamesButton);
        add(scoreButton);
        add(leaderboardButton);
        add(logoutButton);
        
        createGameButton.addActionListener(e -> {
            String gameId = WSClient.newGame(userId);
            JOptionPane.showMessageDialog(this, "Game created!\nGame ID: " + gameId + "\nShare this ID with opponent.");
        });
        
        openGamesButton.addActionListener(e -> {
            new OpenGamesWindow(userId).setVisible(true);
        });
        
        scoreButton.addActionListener(e -> {
            new ScoreWindow(userId).setVisible(true);
        });
        
        leaderboardButton.addActionListener(e -> {
            new LeaderboardWindow().setVisible(true);
        });
        
        logoutButton.addActionListener(e -> {
            dispose();
            new StartWindow().setVisible(true);
        });
        
        setLocationRelativeTo(null);
    }
}