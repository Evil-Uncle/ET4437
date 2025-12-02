package com.ul.et4437.tictactoe;

import javax.swing.*;
import java.awt.*;

public class ScoreWindow extends JFrame {
    public ScoreWindow(int userId) {
        setTitle("My Score Board");
        setSize(400, 300);
        setLayout(new BorderLayout());
        
        String data = WSClient.showAllMyGames(userId);
        
        if (data.startsWith("ERROR")) {
            JOptionPane.showMessageDialog(this, "No game history found");
            dispose();
            return;
        }
        
        // Calculate stats
        String[] rows = data.split("\n");
        int wins = 0, losses = 0, draws = 0;
        
        for (String row : rows) {
            String[] cols = row.split(",");
            if (cols.length >= 4) {
                String state = cols[3].trim();
                if (state.equals("1") || state.equals("2")) {
                    wins++; // Simplified - need to check if user is winner
                } else if (state.equals("3")) {
                    draws++;
                }
            }
        }
        
        losses = rows.length - wins - draws;
        
        // Display stats
        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        statsPanel.add(new JLabel("Your Statistics", SwingConstants.CENTER));
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