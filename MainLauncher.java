package com.mycompany.tttgame;
// Where the user launches the game after web services are launched
import javax.swing.*;

public class MainLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartWindow start = new StartWindow();
            start.setVisible(true);
        });
    }

}
