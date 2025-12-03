package com.mycompany.tttgame;

import javax.swing.*;

public class TestRun {
    public static void main(String[] args) {
        System.out.println("Testing compilation...");
        SwingUtilities.invokeLater(() -> {
            JFrame test = new JFrame("Test");
            test.setSize(300, 200);
            test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            test.add(new JLabel("If you see this, Swing works!"));
            test.setVisible(true);
        });
    }
}