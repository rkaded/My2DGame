package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Closes window on 'x'
        window.setResizable(true); //Prevents windows from being resized
        window.setTitle("2D Game");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); //Sets window size
        window.setLocationRelativeTo(null); //Centers window on screen
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}
