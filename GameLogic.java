
package com.mycompany.tttgame;

/*
 * ET4437: Group Project Mark Hynes, Ronan Tarrant, David Bradshaw
 * GAME LOGIC COMPONENT — GameLogic Class
 *
 * Handles all logic: validating moves, win detection, draws,
 * switching players, and (x,y) coordinate translation required
 * by the TicTacToeWS SOAP service.
 */

public class GameLogic {

    private final GameState state;

    public GameLogic(GameState state) {
        this.state = state;
    }

    // Player attempts a move
    public boolean makeMove(int x, int y) {
        if (state.isGameOver()) return false;

        if (!isValidMove(x, y)) return false;

        state.setCell(x, y, state.getCurrentPlayer());

        if (checkWin()) {
            state.setWinner(state.getCurrentPlayer());
            state.setGameOver(true);
        } 
        else if (checkDraw()) {
            state.setWinner(' ');  // no winner
            state.setGameOver(true);
        } 
        else {
            state.switchPlayer();
        }

        return true;
    }

    // VALIDATION 
    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3 &&
               state.getCell(x, y) == ' ';
    }

    // WIN CHECK
    private boolean checkWin() {
        char p = state.getCurrentPlayer();
        char[][] b = state.getBoard();

        // Rows + Columns
        for (int i = 0; i < 3; i++) {
            if (b[i][0] == p && b[i][1] == p && b[i][2] == p) return true;
            if (b[0][i] == p && b[1][i] == p && b[2][i] == p) return true;
        }

        // Diagonals
        if (b[0][0] == p && b[1][1] == p && b[2][2] == p) return true;
        if (b[0][2] == p && b[1][1] == p && b[2][0] == p) return true;

        return false;
    }

    // DRAW CHECK
    private boolean checkDraw() {
        char[][] b = state.getBoard();
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (b[r][c] == ' ') return false;
        return true;
    }

    // Converts WS coordinates (1–3) to array index (0–2)
    public static int wsToIndex(int n) { return n - 1; }

    // Converts internal array index (0–2) to WS coordinate (1–3)
    public static int indexToWs(int i) { return i + 1; }
}
