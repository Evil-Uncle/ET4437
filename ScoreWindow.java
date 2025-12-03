package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class ScoreWindow extends JFrame {
    public ScoreWindow(int userId, String username) {
        setTitle("My Score Board");
        setSize(360, 260);
        setLayout(new BorderLayout());

        SOAPClient client = new SOAPClient();
        String data = client.leagueTable(); // contains gamestate per game

        int wins = 0, losses = 0, draws = 0, totalCompleted = 0;

        if (data != null && !data.startsWith("ERROR")) {
            String[] rows = data.split("\n");
            for (String row : rows) {
                String[] cols = row.split(",");
                // cols: autokey, p1username, p2username, gamestate, started
                if (cols.length >= 5) {
                    String p1 = cols[1].trim();
                    String p2 = cols[2].trim();
                    String state = cols[3].trim();
                    // Only consider games that finished (state 1,2,3) or in-progress?
                    if ("1".equals(state) || "2".equals(state) || "3".equals(state)) {
                        // winner: 1 -> player1, 2 -> player2, 3 -> draw
                        if ("3".equals(state)) {
                            // draw
                            if (username.equals(p1) || username.equals(p2)) {
                                draws++;
                                totalCompleted++;
                            }
                        } else if ("1".equals(state)) {
                            // player1 won
                            if (username.equals(p1)) wins++;
                            else if (username.equals(p2)) losses++;
                            if (username.equals(p1) || username.equals(p2)) totalCompleted++;
                        } else if ("2".equals(state)) {
                            // player2 won
                            if (username.equals(p2)) wins++;
                            else if (username.equals(p1)) losses++;
                            if (username.equals(p1) || username.equals(p2)) totalCompleted++;
                        }
                    }
                }
            }
        } else {
            if (data != null && data.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, "No data: " + data);
            }
        }

        JPanel p = new JPanel(new GridLayout(4, 1, 8, 8));
        p.add(new JLabel("Player: " + username, SwingConstants.CENTER));
        p.add(new JLabel("Wins: " + wins, SwingConstants.CENTER));
        p.add(new JLabel("Losses: " + losses, SwingConstants.CENTER));
        p.add(new JLabel("Draws: " + draws, SwingConstants.CENTER));

        add(p, BorderLayout.CENTER);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        add(close, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }
}