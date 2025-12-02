package com.ul.et4437.tictactoe;

public class WSClient {
    public static int login(String user, String pass) {
        System.out.println("Login: " + user);
        return 1; // dummy user ID
    }
    
    public static String register(String name, String surname, String user, String pass) {
        System.out.println("Register: " + user);
        return "1"; // dummy success
    }
    
    public static String newGame(int userId) {
        return "100"; // dummy game ID
    }
    
    public static String showOpenGames() {
        return "100,player1,2025-01-01\n200,player2,2025-01-02";
    }
    
    // Add other stub methods as needed
}