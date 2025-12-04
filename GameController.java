package com.mycompany.tttgame;

public class GameController {
// File that starts the Game
    private GameBoard board;
    private Player p1;
    private Player p2;

    public GameController(Player p1, Player p2) {
        this.board = new GameBoard();
        this.p1 = p1;
        this.p2 = p2;
    }

    public void startGame() {
        System.out.println("Starting Tic-Tac-Toe Game...");
        board.printBoard();
    }

}
