package com.games.Allay;

import java.awt.*;

public class Allay {
    public int x, y, width, height;
    public Image img;

    public Allay(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

    public void draw(Graphics g) {
        g.drawImage(img, x, y, width, height, null);
    }
}