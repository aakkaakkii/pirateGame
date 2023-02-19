package org.example.tmp;

import org.example.components.Spritesheet;
import org.example.engine.AssetPool;
import org.example.engine.GameObject;
import org.example.engine.Scene;
import org.example.engine.Window;
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

    public static void loadLevel(Scene scene) {
        BufferedImage img = AssetPool.getSprite("assets/level_one_data.png").image;
        Spritesheet spr = AssetPool.getSpritesheet("assets/outside_sprites.png");
        int size = spr.sprites.get(0).height;

        for (int j = 0; j < Window.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < Window.TILES_IN_WIDTH; i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();


                GameObject go = Prefabs.generateSpriteObject(spr.sprites.get(value), Window.SCALE, Window.SCALE, "Block");
                go.transform.position.x = i * size * Window.SCALE;
                go.transform.position.y = j * size * Window.SCALE;
                scene.addGameObject(go);

/*                g2.drawImage(spritesheet.sprites.get(levelData[i][j]).image,
                        i * Window.TILES_SIZE, j * Window.TILES_SIZE,
                        Window.TILES_SIZE, Window.TILES_SIZE, null);*/



            }
        }
    }
}
