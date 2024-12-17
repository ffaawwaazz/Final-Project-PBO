package com.games.Allay;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StartScreen extends JPanel {
    private boolean gameStarted = false;
    private GamePanel gamePanel;
    private Image backgroundImg; // Untuk latar belakang
    private JButton startButton; // Tombol Start

    // Ukuran layar yang sama dengan GamePanel (360x640)
    public StartScreen() {
        setPreferredSize(new Dimension(360, 640)); // Ukuran layar yang sama dengan GamePanel
        setFocusable(true);
        loadAssets(); // Memuat gambar latar belakang

        // Membuat tombol Start
        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 30)); // Mengatur font tombol
        startButton.setBounds(100, getHeight() / 2 + 50, 160, 50); // Mengatur posisi dan ukuran tombol
        startButton.setFocusable(true); // Agar tombol tidak dapat diakses dengan keyboard
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStarted = true; // Mengubah status game dimulai
                repaint(); // Menggambar ulang untuk mulai permainan
            }
        });

        setLayout(null); // Menggunakan layout null agar kita bisa mengatur posisi tombol secara manual
        add(startButton); // Menambahkan tombol ke layar
    }

    private void loadAssets() {
        // Memuat gambar latar belakang dari file
        backgroundImg = new ImageIcon(getClass().getResource("/Startscreenbg.png")).getImage(); // Gantilah nama file sesuai dengan file latar belakang Anda
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Menggambar latar belakang
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);
        

        // Menampilkan pesan jika game dimulai
        if (gameStarted) {
            gamePanel = new GamePanel(); // Membuat panel permainan
            this.setVisible(false); // Menyembunyikan layar pembuka
            this.getParent().add(gamePanel); // Menambahkan gamePanel ke JFrame
            gamePanel.requestFocusInWindow();
        }
    }
}
