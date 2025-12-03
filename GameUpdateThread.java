/*
 * ET4437: Project (23364505)
 *
 * CHALLENGE: Tic Tac Toe client GUI windows (student-provided).
 *
 * This file: GameUpdateThread - minimal runnable that calls back periodically.
 *
 * Use: instantiate with a Runnable updater that polls the web service and updates UI.
 */

package com.mycompany.tttgame;

import javax.swing.SwingUtilities;

/*
 * ET4437: Group Project
 * THREADING COMPONENT — GameUpdateThread
 */

public class GameUpdateThread implements Runnable {

    private volatile boolean running = true;
    private final int gameId;
    private final GameState state;
    private final GameLogic logic;
    private final GameUpdateListener listener;
    private final int refreshRateMs = 1500;

    // Correct Web service proxy type
    private final com.tttws.TicTacToeWS proxy;

    public interface GameUpdateListener {
        void onBoardUpdated();
        void onGameFinished(char winner);
    }

    public GameUpdateThread(int gameId,
                            GameState state,
                            GameLogic logic,
                            GameUpdateListener listener,
                            com.tttws.TicTacToeWS proxy) {

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
        String wsBoard = proxy.getBoard(gameId);

        updateLocalBoard(wsBoard);

        SwingUtilities.invokeLater(() -> {
            listener.onBoardUpdated();
            if (state.isGameOver()) {
                listener.onGameFinished(state.getWinner());
            }
        });
    }

    private void updateLocalBoard(String boardStr) {
        char[] arr = boardStr.toCharArray();
        int k = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++, k++) {
                state.setCell(r, c, arr[k] == '-' ? ' ' : arr[k]);
            }
        }
    }

    public void stop() {
        running = false;
    }
}

