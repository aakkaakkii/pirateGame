package org.example.scenes;

import org.example.components.Sprite;
import org.example.components.Spritesheet;
import org.example.engine.AssetPool;
import org.example.engine.Scene;
import org.example.engine.Window;
import org.example.engine.ui.Button;
import org.example.engine.ui.Panel;
import org.example.physics.common.Vector2;

import java.awt.*;

public class MenuScene extends Scene {
    private Panel panel;

    public MenuScene() {
        super("Menu");
    }

    @Override
    public void init() {
        AssetPool.addSpritesheet("assets/button_atlas.png", 140, 56, 0, 3, 3 * 3);
//        AssetPool.addSpritesheet("assets/player_sprites.png", 64, 40, 0, 6, 6 * 9);
        Sprite panelSprite = AssetPool.getSprite("assets/menu_background.png");

        Spritesheet buttons = AssetPool.getSpritesheet("assets/button_atlas.png");

        panel = new Panel(new Vector2(Window.GAME_WIDTH/2, Window.GAME_HEIGHT/2 - 80), panelSprite.width, panelSprite.height, 1, Panel.Direction.ROW);
        panel.addSprite(panelSprite);

        Button playButton = new Button(new Vector2(Window.GAME_WIDTH/2, Window.GAME_HEIGHT/2 - 110), 140, 59, 2);
        playButton.addDefaultState(buttons.sprites.get(0));
        playButton.addHoverState(buttons.sprites.get(1));
        playButton.addClickedState(buttons.sprites.get(2));
        playButton.setCallback(()-> Window.getWindow().changeScene(0));
        panel.addElement(playButton);

/*        Button optionButton = new Button(new Vector2(Window.GAME_WIDTH/2, Window.GAME_HEIGHT/2 - 50), 140, 59, 2);
        optionButton.addSprite(buttons.sprites.get(3));
        panel.addElement(optionButton);

        Button quitButton = new Button(new Vector2(Window.GAME_WIDTH/2, Window.GAME_HEIGHT/2 + 10 ), 140, 59, 2);
        quitButton.addSprite(buttons.sprites.get(6));
        panel.addElement(quitButton);

        addGameObject(quitButton);
        addGameObject(optionButton);*/
        panel.addToScene(this);
    }

    @Override
    public void update(float dt) {
        panel.update(dt);
    }

    @Override
    public void draw(Graphics2D g2) {
        renderer.render(g2);
    }
}
