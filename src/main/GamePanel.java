package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    //Screen settings
    final int originalTileSize = 16; //16x16 pixels tile size, most resourceful size
    final int scale = 3; //Scales
    // the game to 3x the original size

    public int tileSize = originalTileSize * scale; //Actual tile size displayed
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 16;
    public final int screenWidth = maxScreenCol * tileSize; //16*3*16 = 768 pixels wide
    public final int screenHeight = maxScreenRow * tileSize; //16*3*12 = 576 pixels tall

    TileManager tileManager = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread; //keeps game running in a separate thread
    Player player = new Player(this, keyH);

    //FPS
    double fps = 60;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //improves performance by buffering behind the scenes
        this.addKeyListener(keyH); //like addEventListener in JS
        this.setFocusable(true); //makes the panel focusable so it can receive key events
    }

    public void startGameThread() {
        gameThread = new Thread(this); //constructs a new thread
        gameThread.start();

    }

//    //Sleep Method
//    @Override
//    public void run() { //Runs the game in a separate thread
//        //0. method 1: Sleep method
//        double drawInterval = 1_000 / fps; //1 full sec/ fps(60) = 0.01666s
//        double nextDrawTime = System.currentTimeMillis() + drawInterval; //every 0.01666s, draw again
//
//        //Game loop
//        while (gameThread != null) { //As long as gameThread exists, keeps running this like animation in three.js
//            long currentTime = System.currentTimeMillis(); //10^3 millis = 1s, also works
//            //long currentTime = System.nanoTime(); //10^9 nanos = 1s, more precise than millis
//            //TODO list in game loop
//            //0. Restrict loop frequency to FPS
//            //Number of times 1. and 2. happens per second == FPS
//            //1. Update game state
//            update();
//            //2. Draw game state
//            repaint(); //Calls paintComponent()
//
//
//            try {
//                double remainingTime = nextDrawTime - System.currentTimeMillis(); //time remaining until next draw
//                if (remainingTime < 0) {
//                    remainingTime = 0;
//                }
//                Thread.sleep((long)remainingTime);
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

//    //Delta time method (millis; less precise, results in 62/63 fps instead of 60)
//    @Override
//    public void run() {
//        double drawInterval = 1_000 / fps;
//        double delta = 0;
//        long prevTime = System.currentTimeMillis();
//        long currentTime;
//        long timer = 0;
//        int drawCount = 0;
//
//
//        while (gameThread != null) {
//            currentTime = System.currentTimeMillis();
//            delta += (currentTime - prevTime) / drawInterval;
//            timer += currentTime - prevTime;
//            prevTime = currentTime;
//
//            if (delta >= 1) { //Once current - prev = drawInterval, update and draw
//                update();
//                repaint();
//                delta --;
//                drawCount ++;
//            }
//            //Display FPS
//            if (timer == 1000) {
//                System.out.println("FPS = " + drawCount);
//                drawCount = 0;
//                timer = 0;
//            }
//        }
//    }

    //Delta time method (nanos; more precise)
    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / fps;
        double delta = 0;
        long prevTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;


        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - prevTime) / drawInterval;
            timer += currentTime - prevTime;
            prevTime = currentTime;

            if (delta >= 1) { //Once current - prev = drawInterval, update and draw
                update();
                repaint();
                delta --;
                drawCount ++;
            }
//            //Display FPS
//            if (timer == 1_000_000_000) {
//                System.out.println("FPS = " + drawCount);
//                drawCount = 0;
//                timer = 0;
//            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g; //Casts Graphics to Graphics2D for better drawing capabilities
        //Draw the tile then player otherwise tiles cover player
        tileManager.draw(g2);
        player.draw(g2);

        g2.dispose(); //Disposes of the Graphics2D object to free up memory
    }
}
