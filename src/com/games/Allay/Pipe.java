package com.games.Allay;

import java.awt.*;

public class Pipe {
    public int x, y, width, height;
    public Image img;
    public boolean passed;

    public Pipe(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
        this.passed = false;
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }
}