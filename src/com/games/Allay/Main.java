package com.games.Allay;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("The Adventure of Allay");

        StartScreen startScreen = new StartScreen();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(startScreen); // Menambahkan StartScreen ke frame
        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}
