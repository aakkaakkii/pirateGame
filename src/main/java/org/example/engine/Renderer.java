package org.example.engine;

import org.example.components.SpriteRenderer;
import org.example.physics2d.common.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {
    Map<Integer, List<GameObject>> gameObjects;
    Camera camera;

    public Renderer(Camera camera) {
        this.camera = camera;
        this.gameObjects = new HashMap<>();
    }

    public void submit(GameObject gameObject) {
        gameObjects.computeIfAbsent(gameObject.zIndex, k -> new ArrayList<>());
        gameObjects.get(gameObject.zIndex).add(gameObject);
    }

    public void render(Graphics2D g2) {
        int lowestZIndex = Integer.MAX_VALUE;
        int highestZIndex = Integer.MIN_VALUE;
        for (Integer i : gameObjects.keySet()) {
            if (i < lowestZIndex) lowestZIndex = i;
            if (i > highestZIndex) highestZIndex = i;
        }

        int currentZIndex = lowestZIndex;
        while (currentZIndex <= highestZIndex) {
            if (gameObjects.get(currentZIndex) == null) {
                currentZIndex++;
                continue;
            }
            for (GameObject g : gameObjects.get(currentZIndex)) {
                Transform oldTransform = new Transform(new Vector2(g.transform.position.x, g.transform.position.y));
                oldTransform.rotation = g.transform.rotation;
                oldTransform.scale = new Vector2(g.transform.scale);

                g.transform.position = new Vector2(g.transform.position.x - camera.position.x,
                        g.transform.position.y - camera.position.y);


                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                spr.draw(g2);


  /*              g.transform.position.x = oldTransform.position.x;
                g.transform.position.y = oldTransform.position.y;
                g.transform.scale.x = oldTransform.scale.x;
                g.transform.scale.y = oldTransform.scale.y;
                g.transform.rotation = oldTransform.rotation;*/


             g.transform = oldTransform;

  /*           g.transform.position.set(oldTransform.position);
             g.transform.rotation = oldTransform.rotation;
             g.transform.scale.set(oldTransform.scale);*/
            }
            currentZIndex++;
        }
    }
}
