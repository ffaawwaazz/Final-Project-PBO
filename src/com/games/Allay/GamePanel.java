package com.games.Allay;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int boardWidth = 360;
    private int boardHeight = 640;

    private Image backgroundImg;
    private Image allayImg;
    private Image topPipeImg;
    private Image bottomPipeImg;

    private Allay allay;
    private ArrayList<Pipe> pipes;
    private Timer gameLoop;
    private Timer placePipeTimer;

    private int velocityX = -4;
    private int velocityY = 0;
    private int gravity = 1;
    private Random random;

    private boolean gameOver;
    private double score;

    private Clip clingSound;
    private Clip backgroundMusic; // Backsound clip

    public GamePanel() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        loadAssets();
        loadSound();

        allay = new Allay(boardWidth / 8, boardWidth / 2, 54, 44, allayImg);
        pipes = new ArrayList<>();
        random = new Random();

        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        playBackgroundMusic(); // Start the background music
    }

    private void loadAssets() {
        backgroundImg = new ImageIcon(getClass().getResource("/minecraftbg.png")).getImage();
        allayImg = new ImageIcon(getClass().getResource("/Allay.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();
    }

    private void loadSound() {
        try {
            // Load cling sound
            URL soundURL = getClass().getResource("/com/games/Allay/cling.wav");
            if (soundURL == null) {
                System.out.println("Sound file not found!");
            } else {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);
                clingSound = AudioSystem.getClip();
                clingSound.open(audioIn);
            }

            // Load background music
            URL bgMusicURL = getClass().getResource("/com/games/Allay/Backsound.wav");
            if (bgMusicURL == null) {
                System.out.println("Background music file not found!");
            } else {
                AudioInputStream bgAudioIn = AudioSystem.getAudioInputStream(bgMusicURL);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(bgAudioIn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
            backgroundMusic.start();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    private void playClingSound() {
        if (clingSound != null) {
            clingSound.setFramePosition(0);
            clingSound.start();
        }
    }

    private final int pipeHeight = 512;

    private void placePipes() {
        int randomPipeY = (int) (-pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        // Top pipe
        pipes.add(new Pipe(boardWidth, randomPipeY, 64, pipeHeight, topPipeImg));

        // Bottom pipe
        pipes.add(new Pipe(boardWidth, randomPipeY + pipeHeight + openingSpace, 64, pipeHeight, bottomPipeImg));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        allay.draw(g);

        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + (int) score, 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    private void move() {
        velocityY += gravity;
        allay.y += velocityY;
        allay.y = Math.max(allay.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && allay.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
                playClingSound();
            }

            if (collision(allay, pipe)) {
                gameOver = true;
            }
        }

        if (allay.y > boardHeight) {
            gameOver = true;
        }
    }

    private boolean collision(Allay a, Pipe b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver) {
            stopBackgroundMusic();
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;

            if (gameOver) {
                allay.y = boardWidth / 2;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
                playBackgroundMusic(); // Restart the background music
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
