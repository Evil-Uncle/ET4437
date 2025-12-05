package com.mycompany.tttgame;

import javax.swing.*;

public class GameUpdateThread implements Runnable {
    
    private final GameBoardWindow view;
    private final SOAPClient client;
    private final int userId;
    private final int gameId;
    private boolean running = true;

    public GameUpdateThread(GameBoardWindow view, SOAPClient client, int userId, int gameId) {
        this.view = view;
        this.client = client;
        this.userId = userId;
        this.gameId = gameId;
    }

    public void stopRunning() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // 1. Check win FIRST
                int win = client.checkWinInt(gameId);
                System.out.println("Win check: " + win);
                
                if (win == 1 || win == 2 || win == 3) {
                    SwingUtilities.invokeLater(() -> view.showGameResult(win));
                    running = false;
                    break;
                }
                
                // 2. Get board
                String board = client.getBoard(gameId);
                
                // 3. Update board
                SwingUtilities.invokeLater(() -> {
                    if (board != null && !board.startsWith("ERROR")) {
                        view.applyBoard(board);
                    }
                });
                
                // 4. Calculate turn
                if (board != null && !board.startsWith("ERROR")) {
                    boolean myTurn = calculateTurn(board);
                    SwingUtilities.invokeLater(() -> view.setMyTurn(myTurn));
                }
                
                Thread.sleep(2000);
                
            } catch (Exception e) {
                System.err.println("Poll error: " + e.getMessage());
                try { Thread.sleep(2000); } catch (InterruptedException ie) { break; }
            }
        }
    }

    private boolean calculateTurn(String board) {
        try {
            if (board.startsWith("ERROR-NOMOVES")) {
                // No moves yet - check if game has started
                String state = client.getGameState(gameId);
                System.out.println("Game state: " + state);
                
                if (state != null && state.trim().equals("-1")) {
                    // Game waiting for player 2
                    System.out.println("Waiting for player 2 to join");
                    return false; // Can't play until someone joins
                }
                
                // Game has 2 players, creator starts
                System.out.println("Game ready, creator starts: " + view.isCreator());
                return view.isCreator();
            }
            
            // Count moves
            String[] moves = board.split("\n");
            int totalMoves = moves.length;
            System.out.println("Moves: " + totalMoves);
            
            // X (creator) on even turns: 0, 2, 4, 6, 8
            // O (joiner) on odd turns: 1, 3, 5, 7
            boolean isXTurn = (totalMoves % 2 == 0);
            
            if (view.isCreator()) {
                System.out.println("You are X, your turn: " + isXTurn);
                return isXTurn;
            } else {
                System.out.println("You are O, your turn: " + !isXTurn);
                return !isXTurn;
            }
            
        } catch (Exception e) {
            System.err.println("Turn calc error: " + e.getMessage());
            return false;
        }
    }
}