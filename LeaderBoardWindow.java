package com.mycompany.tttgame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LeaderBoardWindow extends JFrame {
    public LeaderBoardWindow() {
        setTitle("Leaderboard");
        setSize(800, 420);
        setLayout(new BorderLayout());

        String[] columns = {"Game ID", "Player 1", "Player 2", "Game State", "Started"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        SOAPClient client = new SOAPClient();
        String data = client.leagueTable();
        if (data != null && !data.startsWith("ERROR")) {
            String[] rows = data.split("\n");
            for (String row : rows) {
                String[] cols = row.split(",");
                if (cols.length >= 5) {
                    model.addRow(new Object[]{
                            cols[0].trim(),
                            cols[1].trim(),
                            cols[2].trim(),
                            cols[3].trim(),
                            cols[4].trim()
                    });
                }
            }
        } else {
            if (data != null && data.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, "No games or error: " + data);
            }
        }

        add(scroll, BorderLayout.CENTER);
        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        add(close, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }
}