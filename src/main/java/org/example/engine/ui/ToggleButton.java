package org.example.engine.ui;

import org.example.components.Sprite;
import org.example.components.draw.SpriteRenderer;
import org.example.engine.*;
import org.example.observers.EventSystem;
import org.example.observers.Observer;
import org.example.observers.events.Event;
import org.example.physics.common.Vector2;

import java.awt.event.MouseEvent;

public class ToggleButton extends GameObject implements UI, Observer {
    public float width, height;
    private SpriteRenderer renderer;
    private boolean isOn = true;
    private Sprite[] buttonSprites = new Sprite[6];
    private ToggleButton.ButtonState currentState = ToggleButton.ButtonState.DEFAULT;
    private ToggleCallback toggleCallback;

    public ToggleButton(Vector2 position, float width, float height, int zIndex) {
        this(new Transform(new Vector2(position)), zIndex);
        this.width = width;
        this.height = height;
        renderer = new SpriteRenderer();
        this.addComponent(renderer);
        EventSystem.addObserver(this);

    }

    public ToggleButton(Transform transform, int zIndex) {
        super("Toggle", transform, zIndex);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if ((Window.getWindow().mouseListener.x > this.transform.position.x - width / 2)
                && (Window.getWindow().mouseListener.x < this.transform.position.x + width / 2)
                && (Window.getWindow().mouseListener.y > this.transform.position.y)
                && (Window.getWindow().mouseListener.y < this.transform.position.y + height)) {

            currentState = ToggleButton.ButtonState.HOVER;

        } else {
            currentState = ToggleButton.ButtonState.DEFAULT;
        }


        renderer.setSprite(buttonSprites[currentState.ordinal() + (isOn ? 0 : 2)]);
    }

    public void setCallback(ToggleCallback callback) {
        this.toggleCallback = callback;
    }

    public void addOnDefaultState(Sprite sprite) {
        buttonSprites[ToggleButton.ButtonState.DEFAULT.ordinal()] = sprite;
    }

    public void addOnHoverState(Sprite sprite) {
        buttonSprites[ToggleButton.ButtonState.HOVER.ordinal()] = sprite;
    }

    public void addOnClickedState(Sprite sprite) {
        buttonSprites[ToggleButton.ButtonState.Clicked.ordinal()] = sprite;
    }

    public void addOffDefaultState(Sprite sprite) {
        buttonSprites[ToggleButton.ButtonState.DEFAULT.ordinal() + 2] = sprite;
    }

    public void addOffHoverState(Sprite sprite) {
        buttonSprites[ToggleButton.ButtonState.HOVER.ordinal() + 2] = sprite;
    }

    public void addOffClickedState(Sprite sprite) {
        buttonSprites[ToggleButton.ButtonState.Clicked.ordinal() + 2] = sprite;
    }


    @Override
    public void addToScene(Scene scene) {
        scene.addGameObject(this);
//        onButton.addToScene(scene);
//        ofButton.addToScene(scene);
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case MOUSE_CLICKED:
                MouseEvent e = (MouseEvent) event.payload;
                if (e.getButton() == ML.MouseKeys.LEFT_KEY
                        && (Window.getWindow().mouseListener.x > this.transform.position.x - width / 2)
                        && (Window.getWindow().mouseListener.x < this.transform.position.x + width / 2)
                        && (Window.getWindow().mouseListener.y > this.transform.position.y)
                        && (Window.getWindow().mouseListener.y < this.transform.position.y + height)) {
                    currentState = ToggleButton.ButtonState.Clicked;
                    isOn = !isOn;
                    if (toggleCallback != null) {
                        toggleCallback.onToggle(isOn);
                    }
                }
                break;
        }
    }

    private enum ButtonState {
        DEFAULT,
        HOVER,
        Clicked
    }

    public interface ToggleCallback {
        void onToggle(boolean isOn);
    }
}
