/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tttgame;

public class MainWindow {
    public static void main(String[] args) {
        Player p1 = new Player("Mark", "X");
        Player p2 = new Player("Ronan", "O");

        GameController game = new GameController(p1, p2);
        game.startGame();
    }
}
