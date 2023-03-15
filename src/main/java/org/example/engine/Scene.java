package org.example.engine;

import org.example.components.Component;
import org.example.physics.common.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    public String name;
    public Camera camera;
    public List<GameObject> gameObjects;
    public Renderer renderer;

    public Scene(String name) {
        this.name = name;
        this.camera = new Camera(new Vector2());
        this.gameObjects = new ArrayList<>();
        this.renderer = new Renderer(this.camera);
    }

    public void init() {
    }

    public void addGameObject(GameObject g) {
        gameObjects.add(g);
        renderer.submit(g);

        for (Component c : g.getAllComponents()) {
            c.start();
        }
    }

    public abstract void update(float dt);

    public abstract void draw(Graphics2D g2);
}
