package com.mycompany.tttgame;
// A Screen that displays all available games to join
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OpenGamesWindow extends JFrame {
    private int userId;
    private DefaultTableModel tableModel;
    
    public OpenGamesWindow(int userId) {
        this.userId = userId;
        setTitle("Open Games");
        setSize(600, 400);
        setLayout(new BorderLayout());
        
        // Table setup
        String[] columns = {"Game ID", "Creator", "Started"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton joinButton = new JButton("Join Selected Game");
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");
        
        buttonPanel.add(joinButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load initial data
        refreshTable();
        
        // Button actions
        refreshButton.addActionListener(e -> refreshTable());
        
        joinButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int gameId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                SOAPClient client = new SOAPClient();
                String result = client.joinGame(userId, gameId);
                if (result.equals("1")) {
                    JOptionPane.showMessageDialog(this, "Joined game " + gameId + " successfully!");
                    
                    // Get creator username from table
                    String creatorName = tableModel.getValueAt(selectedRow, 1).toString();
                    
                    dispose();
                    // Open game board as joiner (not creator)
                    GameBoardWindow board = new GameBoardWindow(userId, creatorName, gameId, false);
                    board.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to join game: " + result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a game first");
            }
        });
        
        closeButton.addActionListener(e -> dispose());
        
        setLocationRelativeTo(null);
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear table
        
        SOAPClient client = new SOAPClient();
        String data = client.showOpenGames();
        if (data.equals("ERROR-NOGAMES")) {
            JOptionPane.showMessageDialog(this, "No open games available");
            return;
        }
        
        String[] rows = data.split("\n");
        for (String row : rows) {
            String[] cols = row.split(",");
            if (cols.length >= 3) {
                tableModel.addRow(new Object[]{cols[0], cols[1], cols[2]});
            }
        }
    }

}
