package com.mycompany.tttgame;
// The Games Main Menu Screen
import javax.swing.*;
import java.awt.*;

public class MainMenuWindow extends JFrame {
    private final int userId;
    private final String username;

    public MainMenuWindow(int userId, String username) {
        this.userId = userId;
        this.username = username;

        setTitle("Tic Tac Toe - " + username);
        setSize(420, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 10, 10));

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
            SOAPClient client = new SOAPClient();
            String gameIdStr = client.newGame(userId);
            if (gameIdStr != null && !gameIdStr.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, "Game created. ID: " + gameIdStr);
                int gid;
                try {
                    gid = Integer.parseInt(gameIdStr.trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid game id returned: " + gameIdStr, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Open board as creator (creator moves first)
                GameBoardWindow board = new GameBoardWindow(userId, username, gid, true);
                board.setVisible(true);
                
                // Start timeout thread for auto-delete after 15 minutes
                GameTimeoutThread timeoutThread = new GameTimeoutThread(gid, userId, client);
                timeoutThread.start();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create game: " + gameIdStr, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        openGamesButton.addActionListener(e -> {
            OpenGamesWindow ogw = new OpenGamesWindow(userId);
            ogw.setVisible(true);
        });

        scoreButton.addActionListener(e -> {
            ScoreWindow sw = new ScoreWindow(userId, username);
            sw.setVisible(true);
        });

        leaderboardButton.addActionListener(e -> {
            LeaderBoardWindow lb = new LeaderBoardWindow();
            lb.setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new StartWindow().setVisible(true);
        });

        setLocationRelativeTo(null);
    }

}
