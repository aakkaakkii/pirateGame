package org.example.engine.ui;

import org.example.components.Sprite;
import org.example.components.draw.Drawer;
import org.example.components.draw.SpriteRenderer;
import org.example.engine.GameObject;
import org.example.engine.Scene;
import org.example.engine.Transform;
import org.example.physics.common.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Panel extends GameObject implements UI {
    private float width, height;
    private List<GameObject> objects = new ArrayList<>();
    private Direction direction;
    private float lastHeight = 0;
//    private float paddingTop = 0;
//    private float paddingRight = 0;
//    private float paddingBottom = 0;
//    private float paddingLeft = 0;


    public Panel(Vector2 position, float width, float height, int zIndex, Direction direction) {
        this(new Transform(new Vector2(position)), zIndex);
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public Panel(Vector2 position, float width, float height, Direction direction) {
        this(new Transform(new Vector2(position)), 1);
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public Panel(Transform transform, int zIndex) {
        super("Panel", transform, zIndex);
    }

    public void addSprite(Sprite sprite) {
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        this.addComponent(renderer);
    }

    public void addElement(GameObject go) {
        objects.add(go);
/*        if(direction.equals(Direction.ROW)) {
            Object o = go.getComponent(Drawer.class);
//            go.transform.position.add(this.transform.position)
//                    .add(0, lastHeight);
            go.zIndex += 1;
            if(o instanceof SpriteRenderer) {
                lastHeight+= ((SpriteRenderer) o).getSprite().height;

            }
        }*/
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        for (GameObject g : objects) {
            g.update(dt);
        }

    }

    public void addToScene(Scene scene) {
        scene.addGameObject(this);
        for (GameObject go : objects) {
            ((UI) go).addToScene(scene);
//            scene.addGameObject(go);
        }

    }

    public enum Direction {
        ROW,
        COLUMN
    }
}
