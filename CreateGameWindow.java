package com.mycompany.tttgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreateGameWindow extends JFrame {

    private final JButton btnCreate = new JButton("Create Game");
    private final JLabel lblInfo = new JLabel("Click to create a new game");

    public CreateGameWindow() {
        setTitle("Create Game");
        setSize(300, 130);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel p = new JPanel(new BorderLayout(8,8));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(lblInfo, BorderLayout.CENTER);
        p.add(btnCreate, BorderLayout.SOUTH);
        add(p);
    }

    /**
     * Register an action to run when the user clicks Create.
     * The action should perform the web-service call to create a game.
     */
    public void setCreateAction(ActionListener action) {
        btnCreate.addActionListener(action);
    }

    /** Set informational text (e.g. "Game created: id=12") */
    public void setInfo(String text) {
        lblInfo.setText(text);
    }
}