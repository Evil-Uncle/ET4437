package com.mycompany.tttgame;

public class GameTimeoutThread extends Thread {

    private final int gameId;
    private final int creatorId;
    private final SOAPClient client;

    public GameTimeoutThread(int gameId, int creatorId, SOAPClient client) {
        this.gameId = gameId;
        this.creatorId = creatorId;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // 15 minutes = 900,000 ms
            // For testing, use 30 seconds (30000 ms) instead
            Thread.sleep(30000);

            // Check if the game is still waiting (-1)
            String state = client.getGameState(gameId);

            if (state.equals("-1")) {
                // No one joined → delete it
                String result = client.deleteGame(gameId, creatorId);
                System.out.println("Game " + gameId + " deleted due to timeout: " + result);
            } else {
                System.out.println("Game " + gameId + " already started. Not deleting.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}