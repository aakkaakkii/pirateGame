package org.example.components;

import org.example.engine.Window;
import org.example.utils.Vector2;

import java.awt.event.KeyEvent;

public class PlayerController extends Component {
    public float walkSpeed = 1.9f;

    private StateMachine stateMachine;
    private float playerWidth = 4f;
    private Vector2 velocity = new Vector2();
    public float slowDownForce = 0.05f;
    private transient Vector2 acceleration = new Vector2();

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
    }

    @Override
    public void update(float dt) {
        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            this.stateMachine.trigger("attack");
        }

        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_D)) {
            if (this.gameObject.transform.scale.x < 0) {
                gameObject.transform.position.x = gameObject.transform.position.x
                        - (stateMachine.getCurrentState().getCurrentSprite().width * playerWidth);
            }
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = walkSpeed;

            if (this.velocity.x <= 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }

        } else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_A)) {
            if (this.gameObject.transform.scale.x > 0) {
                gameObject.transform.position.x = gameObject.transform.position.x
                        + (stateMachine.getCurrentState().getCurrentSprite().width * playerWidth);
            }
            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x >= 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else {
            this.acceleration.x = 0;

            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

      /*      if (this.velocity.y == 0) {
                this.stateMachine.trigger("stopRunning");
            }*/
        }

        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W)) {
            this.acceleration.y = -walkSpeed;


        } else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_S)) {
            this.acceleration.y = walkSpeed;

        } else {
            this.acceleration.y = 0;

            if (this.velocity.y > 0) {
                this.velocity.y = Math.max(0, this.velocity.y - slowDownForce);
            } else if (this.velocity.y < 0) {
                this.velocity.y = Math.min(0, this.velocity.y + slowDownForce);
            }
        }

        if (!Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_A)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_S)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_D)) {
            if (this.velocity.x == 0 || this.velocity.y == 0) {
                this.stateMachine.trigger("stopRunning");
            }
        }


        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        gameObject.transform.position.x += this.velocity.x;
        gameObject.transform.position.y += this.velocity.y;

    }
}
