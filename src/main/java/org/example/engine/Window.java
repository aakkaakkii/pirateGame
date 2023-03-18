package org.example.engine;

import org.example.scenes.MenuScene;
import org.example.scenes.PhysicsScene;
import org.example.scenes.TestScene;
import org.example.utils.Constants;
import org.example.utils.Time;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame implements Runnable {

    public ML mouseListener;
    public KL keyListener;

    private static Window window = new Window();
    private boolean isRunning = true;
    private static Scene currentScene = null;
    private Image doubleBufferImage = null;
    private Graphics doubleBufferGraphics = null;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Window() {
        this.mouseListener = new ML();
        this.keyListener = new KL();

        this.setSize(GAME_WIDTH, GAME_HEIGHT);
//        this.setTitle(Constants.Window.TITLE);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.addKeyListener(keyListener);
        this.setLocationRelativeTo(null);
    }

    public void init() {
        changeScene(2);
        currentScene.init();
    }

    public void changeScene(int scene) {
        switch (scene) {
            case 0:
                this.currentScene = new TestScene();
                break;
            case 1:
                this.currentScene = new PhysicsScene();
                break;
            case 2:
                this.currentScene = new MenuScene();
                break;
        }
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public void update(float dt) {
        currentScene.update(dt);
        draw(getGraphics());
    }

    public void draw(Graphics g) {

        if (doubleBufferImage == null) {
            doubleBufferImage = createImage(getWidth(), getHeight());
            doubleBufferGraphics = doubleBufferImage.getGraphics();
        }

        renderOffScreen(doubleBufferGraphics);
        g.drawImage(doubleBufferImage, 8, 30, null);
    }

    public void renderOffScreen(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        currentScene.draw(g2);
    }

    @Override
    public void run() {
        float lastFrameTime = 0.0f;
        try {
            while (isRunning) {
                float time = (float) Time.getTime();
                float deltaTime = time - lastFrameTime;
                lastFrameTime = time;

                update(deltaTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Window getWindow() {
        return window;
    }
}
