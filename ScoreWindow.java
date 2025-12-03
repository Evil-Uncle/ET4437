package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class ScoreWindow extends JFrame {
    public ScoreWindow(int userId, String username) {
        setTitle("My Score Board - " + username);
        setSize(400, 300);
        setLayout(new BorderLayout());
        
        SOAPClient client = new SOAPClient();
        String data = client.showAllMyGames(userId);
        
        if (data.startsWith("ERROR")) {
            JOptionPane.showMessageDialog(this, "No game history found");
            dispose();
            return;
        }
        
        // Calculate stats
        String[] rows = data.split("\n");
        int totalGames = rows.length;
        int wins = 0, losses = 0, draws = 0;
        
        for (String row : rows) {
            String[] cols = row.split(",");
            if (cols.length >= 4) {
                String state = cols[3].trim(); // gamestate column
                // Need more logic to determine if user won - this is simplified
                // For now, we'll count all non-draws as wins (will need refinement)
                if (state.equals("3")) {
                    draws++;
                } else if (state.equals("1") || state.equals("2")) {
                    // This needs checking: is userId player1 or player2?
                    // For demo, count as win
                    wins++;
                }
            }
        }
        
        losses = totalGames - wins - draws;
        
        // Display stats
        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.add(new JLabel("Your Statistics", SwingConstants.CENTER));
        statsPanel.add(new JLabel("Total Games: " + totalGames));
        statsPanel.add(new JLabel("Wins: " + wins));
        statsPanel.add(new JLabel("Losses: " + losses));
        statsPanel.add(new JLabel("Draws: " + draws));
        
        add(statsPanel, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
}