package com.mycompany.tttgame;

public class GameState {

    private char[][] board;   // 3x3 board
    private char currentPlayer;
    private boolean gameOver;
    private char winner;      // 'X', 'O', or ' ' if none

    public GameState() {
        board = new char[3][3];
        reset();
    }

    public void reset() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                board[r][c] = ' ';

        currentPlayer = 'X';
        gameOver = false;
        winner = ' ';
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'X' ? 'O' : 'X');
    }

    public char getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setWinner(char winner) {
        this.winner = winner;
    }

    public char[][] getBoard() {
        return board;
    }

    public void setCell(int x, int y, char value) {
        board[x][y] = value;
    }

    public char getCell(int x, int y) {
        return board[x][y];
    }
}