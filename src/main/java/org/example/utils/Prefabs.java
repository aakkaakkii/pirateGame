package org.example.utils;

import org.example.components.*;
import org.example.components.draw.SpriteRenderer;
import org.example.engine.AnimationState;
import org.example.engine.AssetPool;
import org.example.engine.GameObject;
import org.example.engine.Transform;
import org.example.physics.common.Vector2;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        return generateSpriteObject(sprite, sizeX, sizeY, "Sprite_Object_Gen", 0);
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, String name) {
        return generateSpriteObject(sprite, sizeX, sizeY, name, 0);
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, String name, int zIndex) {
        Transform transform = new Transform(new Vector2(), new Vector2(sizeX, sizeY));
        GameObject go = new GameObject(name, transform, zIndex);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        go.addComponent(renderer);

        return go;
    }

    public static GameObject generatePlayer() {
        Spritesheet spritesheet = AssetPool.getSpritesheet("assets/player_sprites.png");
        GameObject player = generateSpriteObject(spritesheet.sprites.get(0), 2, 2, "player", 1);
        player.transform.position.x = 300f;
        player.transform.position.y = 300f;

        AnimationState idle = new AnimationState();
        idle.title = "idle";
        float defaultFrameTime = 0.1f;
        idle.addFrame(spritesheet.sprites.get(0), defaultFrameTime);
        idle.addFrame(spritesheet.sprites.get(1), defaultFrameTime);
        idle.addFrame(spritesheet.sprites.get(2), defaultFrameTime);
        idle.addFrame(spritesheet.sprites.get(3), defaultFrameTime);
        idle.addFrame(spritesheet.sprites.get(4), defaultFrameTime);
        idle.setLoop(true);

        AnimationState run = new AnimationState();
        run.title = "run";
        run.addFrame(spritesheet.sprites.get(6), defaultFrameTime);
        run.addFrame(spritesheet.sprites.get(7), defaultFrameTime);
        run.addFrame(spritesheet.sprites.get(8), defaultFrameTime);
        run.addFrame(spritesheet.sprites.get(9), defaultFrameTime);
        run.addFrame(spritesheet.sprites.get(10), defaultFrameTime);
        run.addFrame(spritesheet.sprites.get(11), defaultFrameTime);
        run.setLoop(true);

        AnimationState attack = new AnimationState();
        attack.title = "attack";
        attack.addFrame(spritesheet.sprites.get(48), defaultFrameTime);
        attack.addFrame(spritesheet.sprites.get(49), 0.2f);
        attack.addFrame(spritesheet.sprites.get(50), defaultFrameTime);
        attack.setLoop(false);

        AnimationState switchDirection = new AnimationState();
        switchDirection.title = "switchDirection";
        switchDirection.addFrame(spritesheet.sprites.get(0), defaultFrameTime);
        attack.setLoop(false);


        AnimationState jump = new AnimationState();
        jump.title = "jump";
        jump.addFrame(spritesheet.sprites.get(12), defaultFrameTime);
        jump.addFrame(spritesheet.sprites.get(13), defaultFrameTime);
        jump.addFrame(spritesheet.sprites.get(14), defaultFrameTime);
        jump.setLoop(false);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(idle);
        stateMachine.setDefaultState(idle.title);
        stateMachine.addState(run);
        stateMachine.addState(attack);
        stateMachine.addState(switchDirection);
        stateMachine.addState(jump);

        stateMachine.addState(idle.title, run.title, "startRunning");
        stateMachine.addState(run.title, idle.title, "stopRunning");

        stateMachine.addState(idle.title, attack.title, "attack");
        stateMachine.addState(run.title, attack.title, "attack");
        stateMachine.addState(attack.title, idle.title, "stopAttack");
        stateMachine.addState(run.title, switchDirection.title, "switchDirection");

        stateMachine.addState(idle.title, jump.title, "jump");
        stateMachine.addState(run.title, jump.title, "jump");
        stateMachine.addState(switchDirection.title, jump.title, "jump");
        stateMachine.addState(jump.title, idle.title, "stopJump");


        player.addComponent(new SpriteRenderer());
        player.addComponent(stateMachine);
        player.addComponent(new PlayerController());

        return player;
    }
}
