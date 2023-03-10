package org.example.scenes;

import org.example.components.draw.CircleRenderer;
import org.example.components.draw.RectangleRenderer;
import org.example.engine.*;
import org.example.engine.Window;
import org.example.physics.BodyType;
import org.example.physics.CollisionManifold;
import org.example.physics.RigidBody;
import org.example.physics.World;
import org.example.physics.primitives.Box2D;
import org.example.physics.primitives.Circle;
import org.example.physics.primitives.Collider2D;
import org.example.physics2d.common.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.ThreadLocalRandom;

public class PhysicsScene extends Scene {
    private GameObject player;
    World world;


    public PhysicsScene() {
        super("Physics");
    }

    @Override
    public void init() {
        player = new GameObject("player", new Transform(new Vector2(1000, 100)), 1);
        player.addComponent(new RectangleRenderer(100, 100));
//        player.addComponent(new CircleRenderer(50));
        RigidBody gmRb = new RigidBody();
        gmRb.setPosition(new Vector2(1000, 100));
        player.addComponent(gmRb);

        Box2D collider = new Box2D(new Vector2(100, 100));
//        Circle collider = new Circle( 50);
        collider.setRigidBody(gmRb);
        player.addComponent(collider);
        gmRb.setMass(1);


        GameObject gm = new GameObject("rect", new Transform(new Vector2(300, 100)), 1);
        gm.addComponent(new RectangleRenderer(100, 100));
        RigidBody gmRb2 = new RigidBody();
        gmRb2.setPosition(new Vector2(300, 100));
        gm.addComponent(gmRb2);
        Box2D box2D2 = new Box2D(new Vector2(100, 100));
        box2D2.setRigidBody(gmRb2);
        gm.addComponent(box2D2);
        gmRb2.setBodyType(BodyType.Static);
        gmRb2.setMass(1);

        GameObject gm3 = new GameObject("rect", new Transform(new Vector2(1200, 100)), 1);
        gm3.addComponent(new RectangleRenderer(100, 100));
        RigidBody gmRb3 = new RigidBody();
        gmRb3.setPosition(new Vector2(1200, 100));
        gm3.addComponent(gmRb3);
        Box2D box2D3 = new Box2D(new Vector2(100, 100));
        box2D3.setRigidBody(gmRb3);
        gm3.addComponent(box2D3);
        gmRb3.setBodyType(BodyType.Static);
        gmRb3.setMass(1);


/*        GameObject g2 = new GameObject("circle", new Transform(new Vector2(600, 400)), 1);
        g2.addComponent(new CircleRenderer(50));
//        g2.addComponent(new RectangleRenderer(100, 100));
        RigidBody r2 = new RigidBody();
        r2.setPosition(new Vector2(600, 100));
        g2.addComponent(r2);
        Circle c2 = new Circle( 50);
//        Box2D c2 = new Box2D(new Vector2(100, 100));
        c2.setRigidBody(r2);
        gm.addComponent(c2);
        r2.setMass(1);*/


        GameObject ground = new GameObject("ground", new Transform(new Vector2(600, 550)), 1);
        ground.addComponent(new RectangleRenderer(800,50, new Color(0, 100, 0) ));
        RigidBody gRb = new RigidBody();
        gRb.setPosition(new Vector2(600, 550));
        ground.addComponent(gRb);
        Box2D groundBox2D = new Box2D(new Vector2(800, 50));
        groundBox2D.setRigidBody(gRb);
        ground.addComponent(groundBox2D);
        gRb.setBodyType(BodyType.Static);
        gRb.setMass(1);


        addGameObject(player);
        addGameObject(gm);
//        addGameObject(g2);
        addGameObject(ground);
        addGameObject(gm3);

        world = new World();
        world.addRigidBody(gmRb);
        world.addRigidBody(gmRb2);
//        world.addRigidBody(r2);
        world.addRigidBody(gRb);
        world.addRigidBody(gmRb3);
    }


    public void createObject() {
        int type;
        if(Window.getWindow().mouseListener.mouseClicked(ML.MouseKeys.LEFT_KEY)) {
            type = 1;
        } else if(Window.getWindow().mouseListener.mouseClicked(ML.MouseKeys.RIGHT_KEY)) {
            type = 2;
        } else {
            return;
        }

        float xPos = Window.getWindow().mouseListener.x;
        float yPos = Window.getWindow().mouseListener.y;
        GameObject gm = new GameObject("ground", new Transform(new Vector2(xPos, yPos)), 1);

        RigidBody rb = new RigidBody();
        rb.setPosition(new Vector2(xPos, yPos));
        gm.addComponent(rb);
        Collider2D collider;
        if(type == 1) {
            int width = ThreadLocalRandom.current().nextInt(20, 100);
            int height = ThreadLocalRandom.current().nextInt(20, 100);
            gm.addComponent(new RectangleRenderer(width,height ));
            collider = new Box2D(new Vector2(width,height ));
        } else {
            int radius = ThreadLocalRandom.current().nextInt(20, 100);
            gm.addComponent(new CircleRenderer(radius ));
            collider = new Circle(radius);
        }

        collider.setRigidBody(rb);
        gm.addComponent(type == 1 ? (Box2D)collider : (Circle) collider);
        rb.setMass(1);
        rb.setRestitution(ThreadLocalRandom.current().nextFloat(0.6f, 1f));

        world.addRigidBody(rb);
        addGameObject(gm);
    }

    @Override
    public void update(float dt) {
        Vector2 velocity = new Vector2();
        Vector2 direction = new Vector2();

        if (org.example.engine.Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_A)) {
            direction.add(new Vector2(-1, 0));
        }
        if (org.example.engine.Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_S)) {
            direction.add(new Vector2(0, 1));
        }
        if (org.example.engine.Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_D)) {
            direction.add(new Vector2(1, 0));
        }
        if (org.example.engine.Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W)) {
            direction.add(new Vector2(0, -1));
        }
        if (org.example.engine.Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_E)) {
            RigidBody rb = player.getComponent(RigidBody.class);
            player.transform.rotation = rb.getRotation() + dt * 50;
            rb.setRotation(rb.getRotation() + 50 * dt);
        }
        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_Q)) {
            RigidBody rb = player.getComponent(RigidBody.class);
            player.transform.rotation = rb.getRotation() - dt * 50;
            rb.setRotation(rb.getRotation() - 50 * dt);
        }

        createObject();


        float forceMagnitude = 1;
        Vector2 force = new Vector2(direction).mul(forceMagnitude);

        this.player.getComponent(RigidBody.class).addForce(force);

        for (GameObject g : gameObjects) {
            g.update(dt);
        }

        world.update(dt);
    }

    @Override
    public void draw(Graphics2D g2) {
        renderer.render(g2);
        for (Vector2 v : World.contactPoint) {
            g2.setColor(Color.RED);
            Graphics2D oldGraphics = (Graphics2D) g2.create();

            oldGraphics.translate((v.x - 5 / 2), (v.y - 5 / 2));
            oldGraphics.fillRect(0, 0, (int) 5, (int) 5);

        }


    }
}
