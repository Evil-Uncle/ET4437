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
        System.out.println("Game " + gameId + " created. Auto-delete DISABLED for demo.");
        // DISABLED for demo - don't delete games
        // return;
        
        // Or keep but make it 30 MINUTES for demo:
        try {
            Thread.sleep(30 * 60 * 1000); // 30 minutes
            String state = client.getGameState(gameId);
            if (state != null && state.trim().equals("-1")) {
                client.deleteGame(gameId, creatorId);
                System.out.println("Game " + gameId + " deleted after 30 minutes (demo)");
            }
        } catch (Exception e) {
            // Ignore
        }
    }
}