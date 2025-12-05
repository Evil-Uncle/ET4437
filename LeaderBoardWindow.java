package com.mycompany.tttgame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LeaderBoardWindow extends JFrame {
    
    // Inner class to store player statistics
    private static class PlayerStats {
        String username;
        int wins = 0;
        int losses = 0;
        int draws = 0;
        
        PlayerStats(String username) {
            this.username = username;
        }
        
        int getTotalGames() {
            return wins + losses + draws;
        }
        
        double getWinRate() {
            int total = getTotalGames();
            return total > 0 ? (wins * 100.0) / total : 0.0;
        }
    }
    
    public LeaderBoardWindow() {
        setTitle("Leaderboard - Player Statistics");
        setSize(900, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        // Table columns
        String[] columns = {"Rank", "Username", "Wins", "Losses", "Draws", "Total Games", "Win Rate"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(table);
        
        // Get all games data
        SOAPClient client = new SOAPClient();
        String gamesData = client.leagueTable();
        
        if (gamesData != null && !gamesData.startsWith("ERROR")) {
            // Parse all games and calculate stats per user
            Map<String, PlayerStats> statsMap = new HashMap<>();
            String[] games = gamesData.split("\n");
            
            for (String game : games) {
                String[] cols = game.split(",");
                if (cols.length >= 5) {
                    String gameId = cols[0].trim();
                    String p1Username = cols[1].trim();
                    String p2Username = cols[2].trim();
                    String gameState = cols[3].trim(); // -1, 0, 1, 2, 3
                    
                    // Skip waiting and in-progress games
                    if (gameState.equals("-1") || gameState.equals("0")) {
                        continue;
                    }
                    
                    // Initialize stats for players if not exists
                    statsMap.putIfAbsent(p1Username, new PlayerStats(p1Username));
                    statsMap.putIfAbsent(p2Username, new PlayerStats(p2Username));
                    
                    PlayerStats p1Stats = statsMap.get(p1Username);
                    PlayerStats p2Stats = statsMap.get(p2Username);
                    
                    // Update statistics based on game result
                    if (gameState.equals("3")) { // Draw
                        p1Stats.draws++;
                        p2Stats.draws++;
                    } 
                    else if (gameState.equals("1")) { // Player 1 won
                        p1Stats.wins++;
                        p2Stats.losses++;
                    }
                    else if (gameState.equals("2")) { // Player 2 won
                        p1Stats.losses++;
                        p2Stats.wins++;
                    }
                }
            }
            
            // Convert to array and sort by wins (then total games, then username)
            PlayerStats[] statsArray = statsMap.values().toArray(new PlayerStats[0]);
            java.util.Arrays.sort(statsArray, (a, b) -> {
                // First by wins (descending)
                int winCompare = Integer.compare(b.wins, a.wins);
                if (winCompare != 0) return winCompare;
                
                // Then by total games (descending)
                int totalCompare = Integer.compare(b.getTotalGames(), a.getTotalGames());
                if (totalCompare != 0) return totalCompare;
                
                // Finally by username (alphabetical)
                return a.username.compareTo(b.username);
            });
            
            // Add sorted data to table with ranking
            int rank = 1;
            for (PlayerStats stats : statsArray) {
                int totalGames = stats.getTotalGames();
                if (totalGames > 0) { // Only show players who have played games
                    model.addRow(new Object[]{
                        rank++,
                        stats.username,
                        stats.wins,
                        stats.losses,
                        stats.draws,
                        totalGames,
                        String.format("%.1f%%", stats.getWinRate())
                    });
                }
            }
            
            // If no games found
            if (model.getRowCount() == 0) {
                model.addRow(new Object[]{"1", "No games played yet", "0", "0", "0", "0", "0%"});
            }
            
        } else {
            // Error handling
            if (gamesData != null && gamesData.startsWith("ERROR")) {
                model.addRow(new Object[]{"-", "Error loading data: " + gamesData, "-", "-", "-", "-", "-"});
            } else {
                model.addRow(new Object[]{"-", "No game data available", "-", "-", "-", "-", "-"});
            }
        }
        
        // Make win rate column right-aligned
        table.getColumnModel().getColumn(6).setCellRenderer(new RightAlignRenderer());
        
        add(scroll, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");
        
        refreshButton.addActionListener(e -> {
            dispose();
            new LeaderBoardWindow().setVisible(true);
        });
        
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Custom renderer for right-aligned cells
    private static class RightAlignRenderer extends javax.swing.table.DefaultTableCellRenderer {
        public RightAlignRenderer() {
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }
}