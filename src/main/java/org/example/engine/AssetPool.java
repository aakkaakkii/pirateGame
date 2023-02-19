package org.example.engine;

import org.example.components.Sprite;
import org.example.components.Spritesheet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    public static Map<String, Sprite> sprites = new HashMap<>();
    public static Map<String, Spritesheet> spritesheets = new HashMap<>();


    public static Sprite getSprite(String pictureFile) {
        File file = new File(pictureFile);
        if(AssetPool.sprites.containsKey(file.getAbsolutePath())) {
            return AssetPool.sprites.get(file.getAbsolutePath());
        } else {
            Sprite sprite = new Sprite(pictureFile);
            AssetPool.sprites.put(file.getAbsolutePath(), sprite);
            return sprite;
        }
    }

    public static void addSpritesheet(String pictureFile, int tileWidth, int tileHeight,
                                      int spacing, int columns, int size) {
        File file = new File(pictureFile);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            Spritesheet spritesheet = new Spritesheet(pictureFile, tileWidth, tileHeight,
                    spacing, columns, size);
            AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            assert false : "Error: Tried to access spritesheet '" + resourceName + "' and it has not been added to asset pool.";
        }
        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
