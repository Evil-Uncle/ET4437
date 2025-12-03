package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class MainMenuWindow extends JFrame {
    private final int userId;
    private final String username;
    private final SOAPClient client;

    public MainMenuWindow(int userId, String username) {
        this.userId = userId;
        this.username = username;
        this.client = new SOAPClient();  // create SOAP client once

        setTitle("Tic Tac Toe - Welcome " + username);
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 15, 15));

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + username + " (ID: " + userId + ")",
                SwingConstants.CENTER
        );
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

        // Use SOAPClient instead of WSClient.newGame(...)
        createGameButton.addActionListener(e -> {
            String gameId = client.newGame(userId);  // newGame(uid) -> String
            JOptionPane.showMessageDialog(
                    this,
                    "Game created!\nGame ID: " + gameId + "\nShare this ID with opponent."
            );
        });

        openGamesButton.addActionListener(e -> {
            new OpenGamesWindow(userId).setVisible(true);
        });

        scoreButton.addActionListener(e -> {
            new ScoreWindow(userId).setVisible(true);
        });

        leaderboardButton.addActionListener(e -> {
            new LeaderBoardWindow().setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new StartWindow().setVisible(true);
        });

        setLocationRelativeTo(null);
    }
}
