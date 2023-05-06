package org.example.scenes;

import org.example.components.Spritesheet;
import org.example.engine.AssetPool;
import org.example.engine.GameObject;
import org.example.engine.Scene;
import org.example.engine.Window;
import org.example.engine.ui.Panel;
import org.example.physics.RigidBody;
import org.example.physics.World;
import org.example.physics.common.Vector2;
import org.example.physics.primitives.Box2D;
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
    private boolean isPaused = false;
    private Panel pauseMenu;

    private int[][] levelData;

    public TestScene() {
        super("Test");
    }

    @Override
    public void init() {

        AssetPool.addSpritesheet("assets/urm_buttons.png", 56, 56, 0, 3, 3 * 3);
        AssetPool.addSpritesheet("assets/sound_button.png", 42, 42, 0, 3, 2 * 3);


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


        pauseMenu = Prefabs.generatePauseMenu(
                () -> isPaused = false,
                () -> Window.getWindow().changeScene(0)
        );



        addGameObject(gameObject);
        pauseMenu.addToScene(this);
        world = new World();

        LevelLoader.loadLevel(this, world);

        world.addRigidBody(rb, true);
    }

    @Override
    public void update(float dt) {
/*        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {

        }*/

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


        if(isPaused) {
            return;
        }

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



//        RaycastResult info = org.example.engine.Window.getCurrentScene().world.raycast(raycastBegin, raycastEnd);
//        RaycastResult info = org.example.engine.Window.getCurrentScene().world.raycast(p1, p2);

//        System.out.println(info.isHit() );






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
