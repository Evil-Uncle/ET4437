package com.mycompany.tttgame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OpenGamesWindow extends JFrame {
    private final int userId;
    private final DefaultTableModel tableModel;

    public OpenGamesWindow(int userId) {
        this.userId = userId;
        setTitle("Open Games");
        setSize(640, 400);
        setLayout(new BorderLayout());

        String[] columns = {"Game ID", "Creator", "Started"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton joinButton = new JButton("Join Selected Game");
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(joinButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> refreshTable());
        joinButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a game first.");
                return;
            }
            int gid;
            try {
                gid = Integer.parseInt(tableModel.getValueAt(row, 0).toString().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Bad game id selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SOAPClient client = new SOAPClient();
            String res = client.joinGame(userId, gid);
            if ("1".equals(res)) {
                JOptionPane.showMessageDialog(this, "Joined game " + gid);
                // open game board as joiner
                GameBoardWindow gb = new GameBoardWindow(userId, "Player" + userId, gid, false);
                gb.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to join: " + res, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> dispose());

        refreshTable();
        setLocationRelativeTo(null);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        SOAPClient client = new SOAPClient();
        String data = client.showOpenGames();
        if (data == null || data.startsWith("ERROR")) {
            // no games or error
            if ("ERROR-NOGAMES".equals(data)) {
                JOptionPane.showMessageDialog(this, "No open games available.");
            } else {
                JOptionPane.showMessageDialog(this, "Error fetching open games: " + data);
            }
            return;
        }
        String[] rows = data.split("\n");
        for (String row : rows) {
            String[] cols = row.split(",");
            if (cols.length >= 3) {
                tableModel.addRow(new Object[]{cols[0].trim(), cols[1].trim(), cols[2].trim()});
            }
        }
    }
}