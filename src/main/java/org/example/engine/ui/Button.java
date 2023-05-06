package org.example.engine.ui;

import org.example.components.Sprite;
import org.example.components.draw.SpriteRenderer;
import org.example.engine.*;
import org.example.physics.common.Vector2;

public class Button extends GameObject implements UI{
    public float width, height;
    private SpriteRenderer renderer;
    private Sprite[] buttonSprites = new Sprite[3];
    private ButtonState currentState = ButtonState.DEFAULT;
    private ButtonClickCallback buttonClickCallback;

    public Button(Vector2 position, float width, float height, int zIndex) {
        this(new Transform(new Vector2(position)), zIndex);
        this.width = width;
        this.height = height;
        renderer = new SpriteRenderer();
        this.addComponent(renderer);

    }


    @Override
    public void update(float dt) {
        super.update(dt);

        if ((Window.getWindow().mouseListener.x > this.transform.position.x - width / 2)
                && (Window.getWindow().mouseListener.x < this.transform.position.x + width / 2)
                && (Window.getWindow().mouseListener.y > this.transform.position.y )
                && (Window.getWindow().mouseListener.y < this.transform.position.y + height )) {

            if(Window.getWindow().mouseListener.isMousePressed(ML.MouseKeys.LEFT_KEY)) {
                currentState = ButtonState.Clicked;
                if(buttonClickCallback != null) {
                    buttonClickCallback.onClick();
                }
            } else {
                currentState = ButtonState.HOVER;
            }

        } else {
            currentState = ButtonState.DEFAULT;
        }

        renderer.setSprite(buttonSprites[currentState.ordinal()]);

    }

    public Button(Transform transform, int zIndex) {
        super("Button", transform, zIndex);
    }

    public void addDefaultState(Sprite sprite) {
        buttonSprites[ButtonState.DEFAULT.ordinal()] = sprite;
    }

    public void addHoverState(Sprite sprite) {
        buttonSprites[ButtonState.HOVER.ordinal()] = sprite;
    }

    public void addClickedState(Sprite sprite) {
        buttonSprites[ButtonState.Clicked.ordinal()] = sprite;
    }

    public void setCallback(ButtonClickCallback callback) {
        this.buttonClickCallback = callback;
    }

    @Override
    public void addToScene(Scene scene) {
        scene.addGameObject(this);
    }

    private enum ButtonState {
        DEFAULT,
        HOVER,
        Clicked
    }
}
