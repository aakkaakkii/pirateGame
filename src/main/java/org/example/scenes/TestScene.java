package org.example.scenes;

import org.example.components.Spritesheet;
import org.example.components.draw.LineDrawer;
import org.example.components.draw.RectangleRenderer;
import org.example.engine.AssetPool;
import org.example.engine.GameObject;
import org.example.engine.Scene;
import org.example.engine.Window;
import org.example.physics.RigidBody;
import org.example.physics.World;
import org.example.physics.common.Vector2;
import org.example.physics.primitives.Box2D;
import org.example.physics.primitives.RaycastResult;
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
        gameObject.setDebugEnabled(true);

        RigidBody rb = new RigidBody();
        rb.setMass(1);
        rb.setPosition(new Vector2(gameObject.transform.getPosition()));
        Box2D collider = new Box2D(new Vector2(40, 50));
        collider.setRigidBody(rb);
        gameObject.addComponent(rb);


//        RectangleRenderer r = new RectangleRenderer(40, 50, Color.RED);
//        gameObject.addComponent(r);



        addGameObject(gameObject);
        world = new World();

        LevelLoader.loadLevel(this, world);

        world.addRigidBody(rb, true);
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

//        System.out.println(gameObject.transform.position);

        world.update(dt);

        for (GameObject g : gameObjects) {
            g.update(dt);
        }

    }

    @Override
    public void draw(Graphics2D g2) {
        renderer.render(g2);

        float innerPlayerWidth = 38;

        float yVal = 26;

        Vector2 raycastBegin = new Vector2(gameObject.transform.position);
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        Vector2 raycastEnd = new Vector2(raycastBegin).add(0.0f, yVal);

        Vector2 p1 = new Vector2(100, 300);
        Vector2 p2 = new Vector2(110, 500);

//        RaycastResult info = org.example.engine.Window.getCurrentScene().world.raycast(raycastBegin, raycastEnd);
//        RaycastResult info = org.example.engine.Window.getCurrentScene().world.raycast(p1, p2);

//        System.out.println(info.isHit() );

        g2.setColor(Color.MAGENTA);
        g2.drawLine((int)raycastBegin.x, (int)raycastBegin.y, (int)raycastEnd.x, (int)raycastEnd.y);
        g2.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);

        Vector2 raycast2Begin = new Vector2(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2 raycast2End = new Vector2(raycastEnd).add(innerPlayerWidth, 0.0f);
//
//
        g2.setColor(Color.MAGENTA);
        g2.drawLine((int)raycast2Begin.x, (int)raycast2Begin.y, (int)raycast2End.x, (int)raycast2End.y);




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
