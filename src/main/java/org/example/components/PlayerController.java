package org.example.components;

import org.example.components.draw.LineDrawer;
import org.example.engine.Window;
import org.example.physics.RigidBody;
import org.example.physics.World;
import org.example.physics.common.Vector2;
import org.example.physics.primitives.Box2D;
import org.example.physics.primitives.Collider2D;
import org.example.physics.primitives.RaycastResult;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayerController extends Component {

    private StateMachine stateMachine;
    private float playerWidth = 2f;
    private Vector2 velocity = new Vector2();

    public float walkSpeed = 2.5f;
    public float slowDownForce = 5f;
    private Vector2 acceleration = new Vector2();
    public Vector2 terminalVelocity = new Vector2(250.1f, 1300.1f);
    public float jumpImpulse = 200f;
    public boolean onGround = false;
    private int jumpTime = 0;
    public float groundDebounce = 0.0f;
    public float groundDebounceTime = 0.1f;

    private RigidBody rb;


    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody.class);
    }

    @Override
    public void update(float dt) {
        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            this.stateMachine.trigger("attack");
        }

        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_A)) {
            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x >= 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_D)) {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = walkSpeed;

            if (this.velocity.x <= 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
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
        }

        checkOnGround();
        //TODO: more smoothly jump
        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W) && (jumpTime > 0 || onGround || groundDebounce > 0)) {
            if((onGround || groundDebounce > 0) && jumpTime == 0) {
                jumpTime = 28;
                this.acceleration.y = -30;
            } else if(jumpTime > 0) {

                jumpTime--;
                this.acceleration.y *= 0.98;

            }
        } else if(!onGround) {
            if(jumpTime > 0) {
                this.acceleration.y *= 0.35f;
            } else if (jumpTime ==0 || jumpTime == -0) {
                this.acceleration.y = 0;
            }
        } else {
            this.acceleration.y = 0;
            this.jumpTime = 0;
        }




/*
        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W) && onGround) {
            onGround = false;
            this.acceleration.y = -140;
            jumpTime = 90;
        }

        if (!onGround) {
            this.acceleration.y *= 0.80f;

    */
/*        this.velocity.y *= 0.35f;
            if(this.velocity.y < 0.01) {
                this.velocity.y = 0;
            }*//*

            if (jumpTime-- < 0) {
//                System.out.println(onGround + " " + jumpTime);

                onGround = true;
                this.acceleration.y = 0;

            }
        }
*/


/*        float forceMagnitude = 1;
        Vector2 force = new Vector2(direction).mul(4);
        this.gameObject.getComponent(RigidBody.class).addForce(force);*/

        this.velocity.x += this.acceleration.x;
        this.velocity.y += this.acceleration.y;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);

//        System.out.println(this.velocity);

        this.rb.setLinearVelocity(this.velocity);


//        this.velocity.x += this.acceleration.x * dt;
//        this.velocity.y += this.acceleration.y * dt;
//        this.rb.addForce(new Vector2(acceleration).mul(10000));

//        gameObject.transform.position.x += this.velocity.x;
//        gameObject.transform.position.y += this.velocity.y;


        if (!onGround) {
            stateMachine.trigger("jump");
        } else {
            stateMachine.trigger("stopJumping");
        }

        if (!Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_A)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_S)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_D)) {
            if (this.velocity.x == 0 || this.velocity.y == 0) {
                this.stateMachine.trigger("stopRunning");
            }
        }
    }


    public void checkOnGround() {
        float innerPlayerWidth = 38;
        float yVal = 25f;
        Vector2 raycastBegin = new Vector2(gameObject.transform.position);
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        Vector2 raycastEnd = new Vector2(raycastBegin).add(0.0f, yVal);

        RaycastInfo info = new RaycastInfo(this.rb.getCollider());
        Window.getCurrentScene().world.raycast(info, raycastBegin, raycastEnd);

        Vector2 raycast2Begin = new Vector2(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2 raycast2End = new Vector2(raycastEnd).add(innerPlayerWidth, 0.0f);
        RaycastInfo info2 = new RaycastInfo(this.rb.getCollider());
        Window.getCurrentScene().world.raycast(info2, raycast2Begin, raycast2End);


        onGround = (info.hit && info.hitObject != null && info.hitObject.getRigidBody().gameObject.getComponent(Ground.class) != null) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.getRigidBody().gameObject.getComponent(Ground.class) != null);

    }
}
