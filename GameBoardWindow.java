package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;

/**
 * Game board GUI. Uses GameUpdateThread to poll server and update board.
 */
public class GameBoardWindow extends JFrame {
    private final int userId;
    private final String username;
    private final int gameId;
    private final boolean isCreator;
    private final JButton[][] buttons = new JButton[3][3];
    private final SOAPClient client;
    private volatile boolean myTurn = false;
    private GameUpdateThread updater;
    private Thread updaterThread;

    public GameBoardWindow(int userId, String username, int gameId, boolean isCreator) {
        this.userId = userId;
        this.username = username;
        this.gameId = gameId;
        this.isCreator = isCreator;
        this.client = new SOAPClient();

        setTitle("Game " + gameId + " - Player: " + username + (isCreator ? " (creator)" : ""));
        setSize(420, 460);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(3, 3));
        Font f = new Font("Arial", Font.BOLD, 36);

        for (int y = 1; y <= 3; y++) {
            for (int x = 1; x <= 3; x++) {
                final int xx = x;
                final int yy = y;
                JButton b = new JButton("");
                b.setFont(f);
                b.setEnabled(false); // enabled later by setMyTurn
                b.addActionListener(e -> makeMove(xx, yy, b));
                buttons[y - 1][x - 1] = b;
                grid.add(b);
            }
        }

        add(grid, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout());
        JButton refresh = new JButton("Refresh");
        JButton close = new JButton("Close");
        south.add(refresh);
        south.add(close);
        add(south, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            if (updater != null) updater.requestImmediateRefresh();
        });
        close.addActionListener(e -> {
            if (updater != null) updater.stopRunning();
            if (updaterThread != null) updaterThread.interrupt();
            dispose();
        });

        // Start updater
        updater = new GameUpdateThread(this, client, userId, gameId);
        updaterThread = new Thread(updater);
        updaterThread.start();

        // If creator, the first move should be available (if game hasn't started yet).
        // The updater will set correct enablement after first poll.
        setLocationRelativeTo(null);
    }

    private void makeMove(int x, int y, JButton btn) {
        try {
            // call SOAP takeSquare
            String res = client.takeSquare(x, y, gameId, userId);
            if ("1".equals(res)) {
                btn.setText(userMark());
                btn.setEnabled(false);
                // request immediate refresh so other side sees change
                if (updater != null) updater.requestImmediateRefresh();
            } else if ("ERROR-TAKEN".equals(res)) {
                JOptionPane.showMessageDialog(this, "Square already taken.", "Move Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to make move: " + res, "Move Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Service error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // mark used for this player on UI (X for first player/creator, O otherwise)
    private String userMark() {
        return isCreator ? "X" : "O";
    }

    // Called by updater to set button text based on board string
    public void applyBoard(String boardStr) {
        // reset
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++) {
                buttons[r][c].setText("");
                buttons[r][c].setEnabled(false);
            }

        if (boardStr == null) return;
        if (boardStr.startsWith("ERROR-NOMOVES")) {
            // no moves yet; if creator -> enable on-turn squares will be set by setMyTurn
            return;
        }
        if (boardStr.startsWith("ERROR")) {
            return;
        }

        String[] rows = boardStr.split("\n");
        for (String r : rows) {
            String[] cols = r.split(",");
            if (cols.length >= 3) {
                try {
                    int pid = Integer.parseInt(cols[0].trim());
                    int x = Integer.parseInt(cols[1].trim());
                    int y = Integer.parseInt(cols[2].trim());
                    int row = y - 1;
                    int col = x - 1;
                    String mark;
                    // if pid == userId use this player's mark, else other player's mark
                    if (pid == userId) {
                        mark = userMark();
                    } else {
                        mark = isCreator ? "O" : "X";
                    }
                    buttons[row][col].setText(mark);
                    buttons[row][col].setEnabled(false);
                } catch (Exception ex) {
                    // ignore malformed entries
                }
            }
        }
    }

    // Called by updater to enable/disable empty squares when it's the player's turn
    public void setMyTurn(boolean value) {
        this.myTurn = value;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton b = buttons[r][c];
                if (b.getText() == null || b.getText().isEmpty()) {
                    b.setEnabled(value);
                } else {
                    b.setEnabled(false);
                }
            }
        }
    }

    // Expose creator flag to GameUpdateThread
    public boolean isCreator() {
        return isCreator;
    }

    // Force updater to poll immediately
    public void requestImmediateRefresh() {
        if (updater != null) updater.requestImmediateRefresh();
    }
}