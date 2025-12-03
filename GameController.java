/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tttgame;

public class GameController {

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
