
package com.mycompany.tttgame;

/*
 * ET4437 GROUP PROJECT
 * GAME TIMEOUT COMPONENT — GameTimeoutThread
 *
 * Monitors a newly created game and automatically deletes it
 * after 15 minutes IF AND ONLY IF no second player has joined.
 *
 * This uses the Web Service proxy provided by TicTacToeWS.
 */

public class GameTimeoutThread implements Runnable {

    private final int gameId;
    private final com.tttws.TicTacToeWS proxy;
    private volatile boolean running = true;

    // 15 minutes in milliseconds
    private static final long TIMEOUT_MS = 15 * 60 * 1000;

    public GameTimeoutThread(int gameId, com.tttws.TicTacToeWS proxy) {
        this.gameId = gameId;
        this.proxy = proxy;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (running) {
            try {
                Thread.sleep(5000); // Check every 5 seconds

                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed >= TIMEOUT_MS) {
                    checkAndDelete();
                    return;
                }

                // If a second player joins, stop thread
                if (proxy.hasGameStarted(gameId)) {
                    System.out.println("Game " + gameId + " has started. Timer cancelled.");
                    return;
                }

            } catch (Exception ex) {
                System.err.println("GameTimeoutThread Error: " + ex.getMessage());
            }
        }
    }

    private void checkAndDelete() {
        try {
            boolean started = proxy.hasGameStarted(gameId);

            if (!started) {
                proxy.deleteGame(gameId);
                System.out.println("Game " + gameId + " deleted due to timeout (15 minutes).");
            } else {
                System.out.println("Game " + gameId + " started before timeout. Not deleted.");
            }

        } catch (Exception e) {
            System.err.println("Failed to delete game: " + e.getMessage());
        }
    }
}
