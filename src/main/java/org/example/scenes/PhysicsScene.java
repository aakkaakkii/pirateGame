package org.example.scenes;

import org.example.components.draw.CircleRenderer;
import org.example.components.draw.RectangleRenderer;
import org.example.engine.GameObject;
import org.example.engine.Scene;
import org.example.engine.Transform;
import org.example.engine.Window;
import org.example.physics.BodyType;
import org.example.physics.RigidBody;
import org.example.physics.World;
import org.example.physics.primitives.Box2D;
import org.example.physics.primitives.Circle;
import org.example.physics2d.common.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

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
        RigidBody gmRb = new RigidBody();
        gmRb.setPosition(new Vector2(1000, 100));
        player.addComponent(gmRb);
        Box2D box2D = new Box2D(new Vector2(100, 100));
        box2D.setRigidBody(gmRb);
        player.addComponent(box2D);
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


        GameObject g2 = new GameObject("circle", new Transform(new Vector2(600, 400)), 1);
        g2.addComponent(new CircleRenderer(50));
        RigidBody r2 = new RigidBody();
        r2.setPosition(new Vector2(600, 100));
        g2.addComponent(r2);
        Circle c2 = new Circle(r2, 50);
        c2.setRigidBody(r2);
        gm.addComponent(c2);
        r2.setMass(1);


        addGameObject(player);
        addGameObject(gm);
        addGameObject(g2);

        world = new World();
        world.addRigidBody(gmRb);
        world.addRigidBody(gmRb2);
        world.addRigidBody(r2);
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

//        this.gameObject.transform.position.add(new Vector2(direction).normalize().mul(100).mul(dt));
//        this.gameObject.getComponent(RigidBody.class).getPosition().add(new Vector2(direction).normalize().mul(100).mul(dt));
//        this.gameObject.getComponent(RigidBody.class).setPosition(this.gameObject.transform.position);

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
    }
}
