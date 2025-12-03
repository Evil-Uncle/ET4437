package com.mycompany.tttgame;

public class Player {
    private String name;
    private String marker;

    public Player(String name, String marker) {
        this.name = name;
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public String getMarker() {
        return marker;
    }
}