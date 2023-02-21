package org.example.scenes;

import org.example.components.*;
import org.example.engine.*;
import org.example.engine.Window;
import org.example.physics2d.PhysicsSystem2D;
import org.example.physics2d.common.Vector2;
import org.example.physics2d.rigidbody.Rigidbody2D;
import org.example.tmp.LevelLoader;
import org.example.utils.Prefabs;

import java.awt.*;

public class TestScene extends Scene {
    private float xDelta = 0;
    private GameObject gameObject;
    private Spritesheet spritesheet;
    private int aniTick = 0, aniIndex = 0;
    private float scaleH = 4f;
    private float scaleW = 4f;

    private int[][] levelData;
    PhysicsSystem2D physicsSystem2D;

    public TestScene() {
        super("Test");
    }

    @Override
    public void init() {

//        gameObject.addComponent(new ColorComp());
//
//        addGameObject(gameObject);
        AssetPool.addSpritesheet("assets/outside_sprites.png", 32, 32, 0, 12, 4 * 12);
        AssetPool.addSpritesheet("assets/player_sprites.png", 64, 40, 0, 6, 6 * 9);
        AssetPool.addSpritesheet("assets/player_sprites.png", 38, 40, 0, 6, 6 * 9);
        spritesheet = AssetPool.getSpritesheet("assets/outside_sprites.png");

        gameObject = Prefabs.generatePlayer();

        physicsSystem2D = new PhysicsSystem2D(1.0f / 60.f, new Vector2(0, 1));
        Rigidbody2D rb = new Rigidbody2D();
        rb.setMass(200.0f);
        gameObject.addComponent(rb);

        physicsSystem2D.addRigidbody(rb);


        addGameObject(gameObject);
        LevelLoader.loadLevel(this);

    }

    @Override
    public void update(float dt) {
/*        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            gameObject.getComponent(StateMachine.class).trigger("attack");
        }else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_D)) {
            gameObject.transform.scale.x = scaleW;
            gameObject.getComponent(StateMachine.class).trigger("startRunning");
            gameObject.transform.position.x += dt * 100;
//            xDelta += dt * 115;
        }else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_A)) {
            gameObject.transform.scale.x = -1 * scaleW;
            gameObject.getComponent(StateMachine.class).trigger("startRunning");
            gameObject.transform.position.x -= dt * 100;
        } else {
            gameObject.getComponent(StateMachine.class).trigger("stopRunning");
        }*/


        for (GameObject g : gameObjects) {
            g.update(dt);
        }
        physicsSystem2D.update(dt);

    }

    @Override
    public void draw(Graphics2D g2) {
        renderer.render(g2);
//        g2.setColor(Color.BLACK);
//        g2.fillRect(600,(int)(xDelta + 100), 200, 50);

//        g2.drawImage(spritesheet.sprites.get(0).image, 0, 0, 64, 64, null);

/*
        for (int j = 0; j < Window.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < Window.TILES_IN_WIDTH; i++) {
                g2.drawImage(spritesheet.sprites.get(levelData[i][j]).image,
                        i * Window.TILES_SIZE, j * Window.TILES_SIZE,
                        Window.TILES_SIZE, Window.TILES_SIZE, null);

            }
        }*/
        // g2.drawImage(spritesheet.sprites.get(aniIndex).image, 0, 0, 256, 160, null);


//        renderer.render(g2);

    }
}
