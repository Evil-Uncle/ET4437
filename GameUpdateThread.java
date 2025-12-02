package com.mycompany.tttgame;

import javax.swing.SwingUtilities;

/*
 * ET4437: Group Project Mark Hynes, Ronan Tarrant, David Bradshaw
 * THREADING COMPONENT — GameUpdateThread
 *
 * Background thread that repeatedly calls the TicTacToeWS Web Service
 * to check for remote player moves, and updates the GUI using
 * SwingUtilities.invokeLater().
 */

public class GameUpdateThread implements Runnable {

    private volatile boolean running = true;
    private final int gameId;
    private final GameState state;
    private final GameLogic logic;
    private final GameUpdateListener listener;
    private final int refreshRateMs = 1500;

    // Web service proxy
    private final com.tttgameWS.TicTacToeWS proxy;

    public interface GameUpdateListener {
        void onBoardUpdated();
        void onGameFinished(char winner);
    }

    public GameUpdateThread(int gameId, GameState state, GameLogic logic,
                            GameUpdateListener listener,
                            com.tttgamews.TicTacToeWS proxy) {
        this.gameId = gameId;
        this.state = state;
        this.logic = logic;
        this.listener = listener;
        this.proxy = proxy;
    }

    @Override
    public void run() {
        while (running) {
            try {
                pollServer();
                Thread.sleep(refreshRateMs);
            } catch (Exception e) {
                System.err.println("GameUpdateThread error: " + e.getMessage());
            }
        }
    }

    private void pollServer() {
        // Pull board from WS
        String wsBoard = proxy.getGameBoard(gameId);

        // Convert string to 3x3 state
        updateLocalBoard(wsBoard);

        SwingUtilities.invokeLater(() -> {
            listener.onBoardUpdated();
            if (state.isGameOver()) {
                listener.onGameFinished(state.getWinner());
            }
        });
    }

    private void updateLocalBoard(String boardStr) {
        // Example WS board format: "XOX-O--O-"
        char[] arr = boardStr.toCharArray();
        int k = 0;

        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++, k++) {
                state.setCell(r, c, arr[k] == '-' ? ' ' : arr[k]);
            }
    }

    public void stop() {
        running = false;
    }
}