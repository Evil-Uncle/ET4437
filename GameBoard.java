/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tttgame;

public class GameBoard {
    private final int SIZE = 3;
    private String[][] board;

    public GameBoard() {
        board = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = "_";
            }
        }
    }

    public boolean placeMarker(int x, int y, String marker) {
        if (x < 1 || x > 3 || y < 1 || y > 3) return false;
        if (!board[y-1][x-1].equals("_")) return false;

        board[y-1][x-1] = marker;
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
