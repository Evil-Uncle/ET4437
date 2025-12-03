package com.mycompany.tttgame;

import javax.swing.*;

/**
 * Polls the server for updates to a particular game and updates the GameBoardWindow.
 * - polls getBoard
 * - computes whose turn it is (based on last move)
 * - applies board to view via SwingUtilities.invokeLater
 */
public class GameUpdateThread implements Runnable {

    private final GameBoardWindow view;
    private final SOAPClient client;
    private final int userId;
    private final int gameId;
    private volatile boolean running = true;
    private volatile boolean immediateRefresh = false;
    private final long pollIntervalMs = 2000; // 2s

    public GameUpdateThread(GameBoardWindow view, SOAPClient client, int userId, int gameId) {
        this.view = view;
        this.client = client;
        this.userId = userId;
        this.gameId = gameId;
    }

    public void stopRunning() {
        running = false;
    }

    /**
     * Request an immediate poll from GUI (e.g. when user hits Refresh)
     */
    public void requestImmediateRefresh() {
        immediateRefresh = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                pollOnce();

                // sleep loop with early wake for immediate refresh
                long slept = 0;
                while (running && slept < pollIntervalMs && !immediateRefresh) {
                    Thread.sleep(200);
                    slept += 200;
                }
                immediateRefresh = false;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                // Keep polling in case of transient errors
                System.err.println("GameUpdateThread error: " + ex.getMessage());
                try { Thread.sleep(pollIntervalMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        }
    }

    private void pollOnce() {
        // 1) get board
        String board = client.getBoard(gameId);

        // 2) apply board to UI (on EDT)
        SwingUtilities.invokeLater(() -> view.applyBoard(board));

        // 3) decide whose turn it is
        boolean myTurn = computeMyTurn(board);
        SwingUtilities.invokeLater(() -> view.setMyTurn(myTurn));

        // 4) check for win/draw
        int win = client.checkWinInt(gameId);
        if (win == 1 || win == 2 || win == 3) {
            // show winner/draw and stop polling
            SwingUtilities.invokeLater(() -> {
                String msg;
                if (win == 3) {
                    msg = "Game ended in a draw.";
                } else {
                    // winner 1 or 2 -> map to player id (we need to know who is player1/p2)
                    // The WS checkWin returns "1" if p1 won, "2" if p2 won.
                    // We don't know p1/p2 ids here so we'll just show generic
                    msg = (win == 1) ? "Player 1 has won!" : "Player 2 has won!";
                }
                JOptionPane.showMessageDialog(view, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            });
            stopRunning();
        }
    }

    /**
     * Determine if it's this client's turn.
     * Logic:
     * - If board string is ERROR-NOMOVES or empty -> p1 (creator) to play first.
     * - Else parse last row (last move) and see its pid: if last move was by this user, it's NOT this user's turn; otherwise it is.
     */
    private boolean computeMyTurn(String board) {
        try {
            if (board == null) return false;
            if (board.startsWith("ERROR-NOMOVES") || board.trim().isEmpty()) {
                // No moves yet -> p1 (creator) to move; we don't know if current user is p1.
                // To be conservative: if user created the game, they start; otherwise they wait.
                // The GameBoardWindow sets isCreator flag and will request setMyTurn accordingly when created.
                return view.isCreator();
            }
            if (board.startsWith("ERROR")) {
                return false;
            }

            // board format: multiple lines each "pId,x,y"
            String[] rows = board.split("\n");
            String last = rows[rows.length - 1];
            String[] parts = last.split(",");
            if (parts.length < 1) return false;
            int lastPid = Integer.parseInt(parts[0].trim());
            return lastPid != this.userId;
        } catch (Exception ex) {
            return false;
        }
    }
}