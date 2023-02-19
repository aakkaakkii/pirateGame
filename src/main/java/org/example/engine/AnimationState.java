package org.example.engine;

import org.example.components.Sprite;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    //TODO: Change Default Sprite
    public static Sprite defaultSprite = new Sprite("assets/player_sprites.png");
    private float time = 0.0f;
    private transient int currentSprite = 0;
    private boolean doesLoop = false;
    private boolean isComplete = false;

    //TODO: refreshTextures
/*    public void refreshTextures() {
        for (Frame frame : animationFrames) {
            frame.sprite.set
        }
    }*/

    public void addFrame(Sprite sprite, float frameTime) {
        animationFrames.add(new Frame(sprite, frameTime));
    }

    public void addFrames(List<Sprite> sprites, float frameTime) {
        for (Sprite sprite : sprites) {
            this.animationFrames.add(new Frame(sprite, frameTime));
        }
    }

    public void setLoop(boolean doesLoop) {
        this.doesLoop = doesLoop;
    }

/*  old method
    public void update(float dt) {
        if (currentSprite < animationFrames.size()) {
            time -= dt;
            if (time <= 0) {
                if (!(currentSprite == animationFrames.size() - 1 && !doesLoop)) {
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                }
                time = animationFrames.get(currentSprite).frameTime;
            }
        }
    }*/

    public void update(float dt) {
        isComplete = false;
        if(currentSprite < animationFrames.size()) {
            time -=dt;
            if (time <= 0) {
                if(doesLoop) {
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                } else {
                    if(currentSprite != animationFrames.size() -1) {
                        currentSprite = currentSprite+1;
                    } else {
                        isComplete = true;
                        currentSprite = 0;
                    }
                }
                time = animationFrames.get(currentSprite).frameTime;
            }
        }
    }

    public Sprite getCurrentSprite() {
        if (currentSprite < animationFrames.size()) {
            return animationFrames.get(currentSprite).sprite;
        }

        return defaultSprite;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
