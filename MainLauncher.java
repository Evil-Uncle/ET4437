package com.mycompany.tttgame;

import javax.swing.*;

public class MainLauncher {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            StartWindow start = new StartWindow();
            start.setVisible(true);
        });
    }
}