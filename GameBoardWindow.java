/*
 * ET4437: Project (23364505)
 *
 * CHALLENGE: Tic Tac Toe client GUI windows (student-provided).
 *
 * This file: GameBoardWindow - 3x3 board with clickable buttons.
 */

package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Minimal 3x3 game board window.
 * Use addSquareListener to register click handling. Coordinates are 1..3 for x and y.
 */
public class GameBoardWindow extends JFrame {

    private final JButton[][] buttons = new JButton[3][3];
    private final JLabel lblStatus = new JLabel("Status: waiting...");
    private int gameId = -1;

    public GameBoardWindow() {
        setTitle("Tic Tac Toe - Game");
        setSize(360, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel board = new JPanel(new GridLayout(3, 3, 5, 5));
        board.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 36);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                JButton b = new JButton("");
                b.setFont(f);
                buttons[x][y] = b;
                board.add(b);
            }
        }
        add(board, BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createEmptyBorder(5,10,10,10));
        south.add(lblStatus, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    /**
     * Set text on a square. x and y are 1..3 (Appendix A co-ordinates). symbol is "X", "O" or "".
     */
    public void setSquare(int x, int y, String symbol) {
        if (x < 1 || x > 3 || y < 1 || y > 3) return;
        buttons[x-1][y-1].setText(symbol == null ? "" : symbol);
    }

    /** Clear board */
    public void clearBoard() {
        for (int y=0;y<3;y++) for (int x=0;x<3;x++) buttons[x][y].setText("");
    }

    /**
     * Register a listener for clicks on squares. Listener will be called with action command "x,y"
     * where x and y are 1..3.
     */
    public void addSquareListener(ActionListener listener) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                final int cx = x+1;
                final int cy = y+1;
                JButton b = buttons[x][y];
                b.setActionCommand(cx + "," + cy);
                // remove previous listeners to avoid duplicates
                for (java.awt.event.ActionListener a : b.getActionListeners()) b.removeActionListener(a);
                b.addActionListener(listener);
            }
        }
    }

    public void setStatus(String s) {
        lblStatus.setText("Status: " + s);
    }

    public void setGameId(int gid) {
        this.gameId = gid;
        setTitle("Tic Tac Toe - Game (id=" + gid + ")");
    }

    public int getGameId() {
        return this.gameId;
    }
}