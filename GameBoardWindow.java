package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

public class GameBoardWindow extends JFrame {
    private final int userId;
    private final String username;
    private final int gameId;
    private final boolean isCreator;
    private final JButton[][] buttons = new JButton[3][3];
    private final SOAPClient client;
    private boolean myTurn = false;
    private GameUpdateThread updater;
    private JLabel statusLabel;

    public GameBoardWindow(int userId, String username, int gameId, boolean isCreator) {
        this.userId = userId;
        this.username = username;
        this.gameId = gameId;
        this.isCreator = isCreator;
        this.client = new SOAPClient();

        System.out.println("Game " + gameId + " - " + username + " (" + (isCreator ? "X" : "O") + ")");
        
        setupUI();
        startUpdater();
    }
    
    private void setupUI() {
        setTitle("Game " + gameId + " - " + username);
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        // Status label
        statusLabel = new JLabel("Starting game...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(statusLabel, BorderLayout.NORTH);
        
        // Game grid
        JPanel grid = new JPanel(new GridLayout(3, 3, 2, 2));
        Font bigFont = new Font("Arial", Font.BOLD, 48);
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int x = col + 1;
                final int y = row + 1;
                
                JButton btn = new JButton("");
                btn.setFont(bigFont);
                btn.setFocusPainted(false);
                btn.setEnabled(false);
                btn.addActionListener(e -> {
                    if (myTurn) {
                        makeMove(x, y, btn);
                    }
                });
                buttons[row][col] = btn;
                grid.add(btn);
            }
        }
        
        add(grid, BorderLayout.CENTER);
        
        // Control panel
        JPanel controls = new JPanel();
        JButton closeBtn = new JButton("Close Game");
        closeBtn.addActionListener(e -> {
            if (updater != null) updater.stopRunning();
            dispose();
        });
        controls.add(closeBtn);
        add(controls, BorderLayout.SOUTH);
    }
    
    private void startUpdater() {
        updater = new GameUpdateThread(this, client, userId, gameId);
        new Thread(updater).start();
        updateStatus("Game started");
    }
    
    private void makeMove(int x, int y, JButton btn) {
        System.out.println(username + " making move at (" + x + "," + y + ")");
        
        // Show move immediately
        String mark = isCreator ? "X" : "O";
        btn.setText(mark);
        btn.setEnabled(false);
        myTurn = false;
        updateStatus("Move placed...");
        
        // Send to server
        new Thread(() -> {
            try {
                String result = client.takeSquare(x, y, gameId, userId);
                SwingUtilities.invokeLater(() -> {
                    if (!"1".equals(result)) {
                        // Failed
                        btn.setText("");
                        myTurn = true;
                        updateStatus("Move failed: " + result);
                        JOptionPane.showMessageDialog(this, "Move failed: " + result);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    btn.setText("");
                    myTurn = true;
                    updateStatus("Error: " + e.getMessage());
                });
            }
        }).start();
    }
    
    public void applyBoard(String boardStr) {
        SwingUtilities.invokeLater(() -> {
            // Clear board
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    buttons[r][c].setText("");
                    buttons[r][c].setEnabled(false);
                }
            }
            
            if (boardStr == null || boardStr.startsWith("ERROR")) {
                if (boardStr != null && boardStr.startsWith("ERROR-NOMOVES")) {
                    updateStatus("No moves yet");
                }
                return;
            }
            
            // Apply server moves
            String[] moves = boardStr.split("\n");
            for (String move : moves) {
                String[] parts = move.split(",");
                if (parts.length >= 3) {
                    try {
                        int pid = Integer.parseInt(parts[0]);
                        int x = Integer.parseInt(parts[1]) - 1;
                        int y = Integer.parseInt(parts[2]) - 1;
                        
                        if (x >= 0 && x < 3 && y >= 0 && y < 3) {
                            String mark = (pid == userId) ? 
                                (isCreator ? "X" : "O") : 
                                (isCreator ? "O" : "X");
                            buttons[y][x].setText(mark);
                        }
                    } catch (Exception e) {
                        // Skip invalid
                    }
                }
            }
            
            updateStatus("Moves: " + moves.length);
        });
    }
    
    public void setMyTurn(boolean value) {
        SwingUtilities.invokeLater(() -> {
            myTurn = value;
            
            if (value) {
                updateStatus("✓ YOUR TURN - Click empty square!");
                enableEmptyButtons();
            } else {
                updateStatus("Waiting for opponent...");
                disableAllButtons();
            }
        });
    }
    
    private void enableEmptyButtons() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton btn = buttons[r][c];
                btn.setEnabled(btn.getText().isEmpty());
            }
        }
    }
    
    private void disableAllButtons() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                buttons[r][c].setEnabled(false);
            }
        }
    }
    
    public void showGameResult(int winResult) {
        SwingUtilities.invokeLater(() -> {
            String message;
            if (winResult == 3) {
                message = "Game ended in a draw!";
            } else if (winResult == 1) {
                message = isCreator ? "🎉 You won! (X)" : "Player 1 (X) won!";
            } else {
                message = !isCreator ? "🎉 You won! (O)" : "Player 2 (O) won!";
            }
            
            JOptionPane.showMessageDialog(this, 
                message + "\n\nGame #" + gameId,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
            
            updateStatus("Game finished!");
            disableAllButtons();
        });
    }
    
    private void updateStatus(String text) {
        statusLabel.setText(text);
    }
    
    public boolean isCreator() {
        return isCreator;
    }
}