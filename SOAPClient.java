package com.mycompany.tttgame;

import com.tttws.TicTacToeWS;
import com.tttws.TicTacToeWebService;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import java.net.URL;

/**
 * Real SOAP client for TicTacToe Web Service with configurable URL.
 */
public class SOAPClient {

    private static TicTacToeWebService service;
    private static TicTacToeWS proxy;
    private static String currentWsdlUrl = null;

    public SOAPClient() {
        init();
    }

    /**
     * Set a custom WSDL URL (for demo/testing).
     */
    public static void setWsdlUrl(String url) {
        if (url != null && !url.trim().isEmpty() && !url.equals(currentWsdlUrl)) {
            System.out.println("Changing WSDL URL to: " + url);
            currentWsdlUrl = url.trim();
            // Reset service to force reconnection with new URL
            service = null;
            proxy = null;
        }
    }

    private static synchronized void init() {
        if (service == null || proxy == null) {
            try {
                // Get WSDL URL from config or ask user
                String wsdlUrl = getWsdlUrl();
                
                System.out.println("Initializing web service client with URL: " + wsdlUrl);
                
                // Create service with custom URL
                URL url = new URL(wsdlUrl);
                QName qname = new QName("http://tttws.com/", "TicTacToeWebService");
                service = new TicTacToeWebService(url, qname);
                proxy = service.getTicTacToeWSPort();
                
                System.out.println("✓ Web Service client initialized successfully");
                currentWsdlUrl = wsdlUrl;
                
            } catch (Exception e) {
                System.err.println("✗ Failed to initialize web service client: " + e.getMessage());
                
                // Show error dialog to user
                JOptionPane.showMessageDialog(null,
                    "Cannot connect to web service at:\n" + 
                    (currentWsdlUrl != null ? currentWsdlUrl : "unknown URL") + 
                    "\n\nError: " + e.getMessage() + 
                    "\n\nPlease check:\n" +
                    "1. Web service is running\n" +
                    "2. URL is correct\n" +
                    "3. Network connection is working",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
                
                // Reset to force re-asking URL next time
                currentWsdlUrl = null;
                service = null;
                proxy = null;
                throw new RuntimeException("Web service initialization failed", e);
            }
        }
    }

    /**
     * Get WSDL URL - asks user on first run if not set.
     */
    private static String getWsdlUrl() {
        if (currentWsdlUrl == null || currentWsdlUrl.isEmpty()) {
            // Default URL for development
            String defaultUrl = "http://localhost:8080/TicTacToeWS/TicTacToeWebService?wsdl";
            
            // Try to read from system property first
            String systemUrl = System.getProperty("wsdl.url");
            if (systemUrl != null && !systemUrl.trim().isEmpty()) {
                currentWsdlUrl = systemUrl.trim();
                return currentWsdlUrl;
            }
            
            // Ask user for URL
            String input = (String) JOptionPane.showInputDialog(
                null,
                "Enter the WSDL URL for the TicTacToe Web Service:\n" +
                "(During demo, use the server URL provided by instructor)",
                "Web Service Configuration",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                defaultUrl
            );
            
            if (input != null && !input.trim().isEmpty()) {
                currentWsdlUrl = input.trim();
            } else {
                // User cancelled or entered nothing, use default
                currentWsdlUrl = defaultUrl;
            }
        }
        return currentWsdlUrl;
    }

    /**
     * Get current WSDL URL (for debugging).
     */
    public static String getCurrentWsdlUrl() {
        return currentWsdlUrl != null ? currentWsdlUrl : "Not set";
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
            String result = proxy.getBoard(gid);
            return result;
        } catch (Exception e) {
            System.err.println("✗ getBoard error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String checkSquare(int x, int y, int gid) {
        try {
            String result = proxy.checkSquare(x, y, gid);
            return result;
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
            String result = proxy.checkWin(gid);
            return result;
        } catch (Exception e) {
            System.err.println("✗ checkWin error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public int checkWinInt(int gid) {
        try {
            String s = checkWin(gid);
            
            if (s == null) {
                System.out.println("checkWin returned null for game " + gid);
                return -1;
            }
            
            if (s.startsWith("ERROR")) {
                System.out.println("checkWin returned error: " + s + " for game " + gid);
                return -1;
            }
            
            s = s.trim();
            if (s.isEmpty()) {
                return 0;
            }
            
            int result = Integer.parseInt(s);
            System.out.println("checkWinInt parsed result: " + result + " for game " + gid);
            return result;
            
        } catch (NumberFormatException e) {
            System.err.println("✗ checkWinInt parse error for game " + gid + ": '" + e.getMessage() + "'");
            return -1;
        } catch (Exception ex) {
            System.err.println("✗ Error in checkWinInt: " + ex.getMessage());
            return -1;
        }
    }

    public String getGameState(int gid) {
        try {
            String result = proxy.getGameState(gid);
            return result;
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
            String result = proxy.showOpenGames();
            return result;
        } catch (Exception e) {
            System.err.println("✗ showOpenGames error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String showAllMyGames(int uid) {
        try {
            String result = proxy.showAllMyGames(uid);
            return result;
        } catch (Exception e) {
            System.err.println("✗ showAllMyGames error: " + e.getMessage());
            return "ERROR-DB";
        }
    }

    public String leagueTable() {
        try {
            String result = proxy.leagueTable();
            return result;
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