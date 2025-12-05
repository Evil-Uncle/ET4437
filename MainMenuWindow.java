package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class MainMenuWindow extends JFrame {
    private final int userId;
    private final String username;

    public MainMenuWindow(int userId, String username) {
        this.userId = userId;
        this.username = username;

        setTitle("Tic Tac Toe - " + username);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 10, 10));
        setLocationRelativeTo(null);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (ID: " + userId + ")", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(0, 70, 140));

        // Create buttons with styling
        JButton createGameButton = createStyledButton("🎮 Create New Game", "Start a new Tic Tac Toe game");
        JButton openGamesButton = createStyledButton("🔍 Show Open Games", "Browse and join games waiting for players");
        JButton myGamesButton = createStyledButton("📋 My Games", "View all games you're playing");
        JButton scoreButton = createStyledButton("📊 My Score Board", "View your win/loss/draw statistics");
        JButton leaderboardButton = createStyledButton("🏆 Leader Board", "See all players ranked by wins");
        JButton logoutButton = createStyledButton("🚪 Logout", "Return to login screen");

        // Add components
        add(welcomeLabel);
        add(createGameButton);
        add(openGamesButton);
        add(myGamesButton);
        add(scoreButton);
        add(leaderboardButton);
        add(logoutButton);

        // Button actions
        createGameButton.addActionListener(e -> createNewGame());
        openGamesButton.addActionListener(e -> showOpenGames());
        myGamesButton.addActionListener(e -> showMyGames());
        scoreButton.addActionListener(e -> showScoreBoard());
        leaderboardButton.addActionListener(e -> showLeaderBoard());
        logoutButton.addActionListener(e -> logout());
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 245));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 210), 1),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 230, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 240, 245));
            }
        });
        return button;
    }

    private void createNewGame() {
        SOAPClient client = new SOAPClient();
        
        // Show loading indicator
        JOptionPane.showMessageDialog(this, "Creating new game...", "Please Wait", JOptionPane.INFORMATION_MESSAGE);
        
        String gameIdStr = client.newGame(userId);
        if (gameIdStr != null && !gameIdStr.startsWith("ERROR")) {
            try {
                int gid = Integer.parseInt(gameIdStr.trim());
                
                // Success message with game ID
                int option = JOptionPane.showConfirmDialog(this,
                    "Game created successfully!\n\n" +
                    "Game ID: " + gid + "\n" +
                    "Share this ID with friends to join.\n\n" +
                    "Game will auto-delete if no one joins in 30 minutes.\n\n" +
                    "Open game board now?",
                    "Game Created",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (option == JOptionPane.YES_OPTION) {
                    // Open board as creator (X goes first)
                    SwingUtilities.invokeLater(() -> {
                        GameBoardWindow board = new GameBoardWindow(userId, username, gid, true);
                        board.setVisible(true);
                    });
                }
                
                // Start timeout thread for auto-delete after 30 minutes (for demo)
                GameTimeoutThread timeoutThread = new GameTimeoutThread(gid, userId, client);
                timeoutThread.start();
                
                System.out.println("Game " + gid + " created. Timeout thread started (30 min).");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid game ID returned: " + gameIdStr,
                    "Creation Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create game: " + gameIdStr + "\n\n" +
                "Possible issues:\n" +
                "1. Database connection error\n" +
                "2. Web service not running\n" +
                "3. Server overloaded",
                "Creation Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showOpenGames() {
        SwingUtilities.invokeLater(() -> {
            OpenGamesWindow ogw = new OpenGamesWindow(userId);
            ogw.setVisible(true);
        });
    }

    private void showMyGames() {
        SOAPClient client = new SOAPClient();
        String data = client.showAllMyGames(userId);
        
        if (data.startsWith("ERROR")) {
            JOptionPane.showMessageDialog(this,
                "No games found for your account.\n" +
                "Create a new game or join an existing one!",
                "No Games",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create a simple dialog showing user's games
        String[] rows = data.split("\n");
        StringBuilder sb = new StringBuilder();
        sb.append("Your Games:\n\n");
        
        int activeGames = 0;
        int completedGames = 0;
        int waitingGames = 0;
        
        for (String row : rows) {
            String[] cols = row.split(",");
            if (cols.length >= 4) {
                String gameId = cols[0].trim();
                String player1 = cols[1].trim();
                String player2 = cols[2].trim();
                String state = cols[3].trim();
                
                sb.append("Game #").append(gameId).append(": ");
                sb.append(player1).append(" vs ").append(player2).append(" - ");
                
                switch (state) {
                    case "-1":
                        sb.append("⏳ Waiting for player 2");
                        waitingGames++;
                        break;
                    case "0":
                        sb.append("🎮 In progress");
                        activeGames++;
                        break;
                    case "1":
                        sb.append("✅ Player 1 won");
                        completedGames++;
                        break;
                    case "2":
                        sb.append("✅ Player 2 won");
                        completedGames++;
                        break;
                    case "3":
                        sb.append("🤝 Draw");
                        completedGames++;
                        break;
                    default:
                        sb.append("❓ Unknown state");
                }
                sb.append("\n");
            }
        }
        
        sb.append("\n--- Summary ---\n");
        sb.append("Active games: ").append(activeGames).append("\n");
        sb.append("Waiting games: ").append(waitingGames).append("\n");
        sb.append("Completed games: ").append(completedGames).append("\n");
        sb.append("Total: ").append(rows.length);
        
        JOptionPane.showMessageDialog(this,
            sb.toString(),
            "My Games",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showScoreBoard() {
        SwingUtilities.invokeLater(() -> {
            ScoreWindow sw = new ScoreWindow(userId, username);
            sw.setVisible(true);
        });
    }

    private void showLeaderBoard() {
        SwingUtilities.invokeLater(() -> {
            LeaderBoardWindow lb = new LeaderBoardWindow();
            lb.setVisible(true);
        });
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?\n\n" +
            "Username: " + username + "\n" +
            "User ID: " + userId,
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Close connection if needed
            SOAPClient client = new SOAPClient();
            client.closeConnection();
            
            // Return to start window
            dispose();
            SwingUtilities.invokeLater(() -> {
                StartWindow start = new StartWindow();
                start.setVisible(true);
            });
        }
    }

    @Override
    public void dispose() {
        // Any cleanup before closing
        System.out.println("Main menu closing for user: " + username);
        super.dispose();
    }
}