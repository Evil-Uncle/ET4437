package com.mycompany.tttgame;

import com.tttws.TicTacToeWS;
import com.tttws.TicTacToeWebService;

/**
 * SOAP client wrapper (Option A).
 * Wraps the generated proxy methods and provides some helpers.
 */
public class SOAPClient {

    private static TicTacToeWebService service;
    private static TicTacToeWS proxy;

    public SOAPClient() {
        init();
    }

    private static synchronized void init() {
        if (service == null || proxy == null) {
            service = new TicTacToeWebService();
            proxy = service.getTicTacToeWSPort();
        }
    }

    public int login(String username, String password) {
        return proxy.login(username, password);
    }

    // register returns a String (id or ERROR-*)
    public String register(String username, String password, String name, String surname) {
        return proxy.register(username, password, name, surname);
    }

    // newGame returns a String (game id or ERROR-*)
    public String newGame(int uid) {
        return proxy.newGame(uid);
    }

    // joinGame returns "1", "0" or "ERROR-DB"
    public String joinGame(int uid, int gid) {
        return proxy.joinGame(uid, gid);
    }

    // getBoard returns string of rows "pId,x,y\n..."
    public String getBoard(int gid) {
        return proxy.getBoard(gid);
    }

    // checkSquare returns "0" or "1" or "ERROR-DB"
    public String checkSquare(int x, int y, int gid) {
        return proxy.checkSquare(x, y, gid);
    }

    // takeSquare returns "1" on success, "ERROR-TAKEN", etc.
    public String takeSquare(int x, int y, int gid, int pid) {
        return proxy.takeSquare(x, y, gid, pid);
    }

    // checkWin returns "0","1","2","3" or error strings
    public String checkWin(int gid) {
        return proxy.checkWin(gid);
    }

    // helper that returns int: 0..3, or -1 on error
    public int checkWinInt(int gid) {
        try {
            String s = checkWin(gid);
            if (s == null || s.startsWith("ERROR")) return -1;
            return Integer.parseInt(s.trim());
        } catch (Exception ex) {
            return -1;
        }
    }

    // getGameState returns string like "-1", "0", "1", "2", "3" or error text
    public String getGameState(int gid) {
        return proxy.getGameState(gid);
    }

    // helper to check if game has started (p2 joined and gamestate != -1)
    public boolean hasGameStarted(int gid) {
        try {
            String s = getGameState(gid);
            if (s == null) return false;
            if (s.startsWith("ERROR")) return false;
            // The WS uses "-1" for waiting for p2; anything else is started/in-progress or finished
            return !s.trim().equals("-1");
        } catch (Exception ex) {
            return false;
        }
    }

    // deleteGame requires gid and uid (returns "1" or ERROR-*)
    public String deleteGame(int gid, int uid) {
        return proxy.deleteGame(gid, uid);
    }

    // showOpenGames, showAllMyGames, leagueTable etc.
    public String showOpenGames() { return proxy.showOpenGames(); }
    public String showAllMyGames(int uid) { return proxy.showAllMyGames(uid); }
    public String leagueTable() { return proxy.leagueTable(); }

    public void closeConnection() { proxy.closeConnection(); }
}