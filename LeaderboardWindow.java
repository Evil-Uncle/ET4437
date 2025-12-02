package com.ul.et4437.tictactoe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LeaderboardWindow extends JFrame {
    public LeaderboardWindow() {
        setTitle("Leaderboard");
        setSize(700, 400);
        setLayout(new BorderLayout());
        
        // Table setup
        String[] columns = {"Game ID", "Player 1", "Player 2", "Result", "Started"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Load data
        String data = WSClient.leagueTable();
        if (!data.startsWith("ERROR")) {
            String[] rows = data.split("\n");
            for (String row : rows) {
                String[] cols = row.split(",");
                if (cols.length >= 5) {
                    model.addRow(cols);
                }
            }
        }
        
        add(scrollPane, BorderLayout.CENTER);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
}