package com.mycompany.tttgame;

import com.tttws.TicTacToeWS;
import com.tttws.TicTacToeWebService;

// Try Jakarta first; if it fails, switch to javax.xml.ws.BindingProvider
import jakarta.xml.ws.BindingProvider;
// import javax.xml.ws.BindingProvider;

/**
 * Real SOAP client for TicTacToe Web Service.
 */
public class SOAPClient {

    private static TicTacToeWebService service;
    private static TicTacToeWS proxy;

    public SOAPClient() {
        init();
    }

    private static synchronized void init() {
        if (service == null || proxy == null) {
            try {
                service = new TicTacToeWebService();
                proxy = service.getTicTacToeWSPort();

                // -------------------------
                //   UPDATE SERVER ADDRESS
                // -------------------------
                String serverIP = "10.63.248.134";  // <--- Windows teammate's IP
                String endpoint = "http://" + serverIP + ":8080/TicTacToeWS/TicTacToeWebService";

                // Override endpoint
                BindingProvider bp = (BindingProvider) proxy;
                bp.getRequestContext().put(
                        BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                        endpoint
                );

                // Log what endpoint we are using
                System.out.println("SOAP endpoint set to: " + endpoint);

                System.out.println("✓ Web Service client initialized successfully");
            } catch (Exception e) {
                System.err.println("✗ Failed to initialize web service client: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ============ AUTHENTICATION METHODS ============

    public int login(String username, String password) {
        try {
            int result = proxy.login(username, password);
            System.out.println("✓ Login result for " + username + ": " + result);
            return result;
        } catch (Exception e) {
            System.err.println("✗ Login error: " + e.getMessage());
            return -1;
        }
    }

    public String register(String username, String password, String name, String surname) {
        try {
            String result = proxy.register(username, password, name, surname);
            System.out.println("✓ Register result: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("✗ Register error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    // ============ GAME MANAGEMENT METHODS ============

    public String newGame(int uid) {
        try {
            String result = proxy.newGame(uid);
            System.out.println("✓ New game created for user " + uid + ": " + result);
            return result;
        } catch (Exception e) {
            System.err.println("✗ newGame error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String joinGame(int uid, int gid) {
        try {
            String result = proxy.joinGame(uid, gid);
            System.out.println("✓ Join game result: user " + uid + " joined game " + gid + ": " + result);
            return result;
        } catch (Exception e) {
            System.err.println("✗ joinGame error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String getBoard(int gid) {
        try {
            return proxy.getBoard(gid);
        } catch (Exception e) {
            System.err.println("✗ getBoard error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String checkSquare(int x, int y, int gid) {
        try {
            return proxy.checkSquare(x, y, gid);
        } catch (Exception e) {
            System.err.println("✗ checkSquare error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String takeSquare(int x, int y, int gid, int pid) {
        try {
            String result = proxy.takeSquare(x, y, gid, pid);
            System.out.println("✓ takeSquare result: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("✗ takeSquare error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String checkWin(int gid) {
        try {
            return proxy.checkWin(gid);
        } catch (Exception e) {
            System.err.println("✗ checkWin error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public int checkWinInt(int gid) {
        try {
            String s = checkWin(gid);
            if (s == null || s.startsWith("ERROR")) return -1;
            return Integer.parseInt(s.trim());
        } catch (Exception ex) {
            System.err.println("✗ Error in checkWinInt: " + ex.getMessage());
            return -1;
        }
    }

    public String getGameState(int gid) {
        try {
            return proxy.getGameState(gid);
        } catch (Exception e) {
            System.err.println("✗ getGameState error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public boolean hasGameStarted(int gid) {
        try {
            String s = getGameState(gid);
            if (s == null) return false;
            if (s.startsWith("ERROR")) return false;
            return !s.trim().equals("-1");
        } catch (Exception ex) {
            System.err.println("✗ Error in hasGameStarted: " + ex.getMessage());
            return false;
        }
    }

    public String deleteGame(int gid, int uid) {
        try {
            String result = proxy.deleteGame(gid, uid);
            System.out.println("✓ deleteGame result: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("✗ deleteGame error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    // ============ LIST/QUERY METHODS ============

    public String showOpenGames() {
        try {
            return proxy.showOpenGames();
        } catch (Exception e) {
            System.err.println("✗ showOpenGames error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String showAllMyGames(int uid) {
        try {
            return proxy.showAllMyGames(uid);
        } catch (Exception e) {
            System.err.println("✗ showAllMyGames error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String leagueTable() {
        try {
            return proxy.leagueTable();
        } catch (Exception e) {
            System.err.println("✗ leagueTable error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    // ============ UTILITY METHODS ============

    public void closeConnection() {
        try {
            proxy.closeConnection();
            System.out.println("✓ Connection closed");
        } catch (Exception e) {
            System.err.println("✗ closeConnection error: " + e.getMessage());
        }
    }
}