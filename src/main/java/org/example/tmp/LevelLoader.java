package org.example.tmp;

import org.example.components.Spritesheet;
import org.example.components.draw.RectangleRenderer;
import org.example.engine.AssetPool;
import org.example.engine.GameObject;
import org.example.engine.Scene;
import org.example.engine.Window;
import org.example.physics.BodyType;
import org.example.physics.RigidBody;
import org.example.physics.World;
import org.example.physics.common.Vector2;
import org.example.physics.primitives.Box2D;
import org.example.utils.Prefabs;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelLoader {

    public static int[][] gameLevelData() {
        int[][] lvlData = new int[Window.TILES_IN_WIDTH][Window.TILES_IN_HEIGHT];
        BufferedImage img = AssetPool.getSprite("assets/level_one_data.png").image;

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                lvlData[i][j] = value;
            }
        }
        return lvlData;
    }

    public static void loadLevel(Scene scene, World world) {
        BufferedImage img = AssetPool.getSprite("assets/level_one_data.png").image;
        Spritesheet spr = AssetPool.getSpritesheet("assets/outside_sprites.png");
        int size = spr.sprites.get(0).height;

        for (int j = 0; j < Window.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < Window.TILES_IN_WIDTH; i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();


                GameObject go = Prefabs.generateSpriteObject(spr.sprites.get(value), Window.SCALE, Window.SCALE, "Block");
                go.transform.position.x = i * size * Window.SCALE + size * Window.SCALE/2;
                go.transform.position.y = j * size * Window.SCALE + size * Window.SCALE/2;
                scene.addGameObject(go);

                if(color.getRed() == 1 || color.getRed() == 0 ||
                        color.getRed() == 2 || color.getRed() == 14) {
                    RigidBody rb = new RigidBody();
                    rb.setBodyType(BodyType.Static);
                    rb.setMass(1);
                    rb.setPosition(new Vector2(go.transform.position.x, go.transform.position.y));
                    Box2D collider = new Box2D(new Vector2(size * Window.SCALE, size * Window.SCALE));
                    collider.setRigidBody(rb);

                    RectangleRenderer r = new RectangleRenderer(size * Window.SCALE, size * Window.SCALE, Color.YELLOW);
                    go.addComponent(r);
                    go.setDebugEnabled(true);

                    world.addRigidBody(rb);
                }

/*                g2.drawImage(spritesheet.sprites.get(levelData[i][j]).image,
                        i * Window.TILES_SIZE, j * Window.TILES_SIZE,
                        Window.TILES_SIZE, Window.TILES_SIZE, null);*/



            }
        }
    }
}
