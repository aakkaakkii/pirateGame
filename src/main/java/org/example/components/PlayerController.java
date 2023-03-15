package org.example.components;

import org.example.engine.Window;
import org.example.physics.RigidBody;
import org.example.physics.common.Vector2;

import java.awt.event.KeyEvent;

public class PlayerController extends Component {

    private StateMachine stateMachine;
    private float playerWidth = 2f;
    private Vector2 velocity = new Vector2();

    public float walkSpeed = 2.5f;
    public float slowDownForce = 5f;
    private transient Vector2 acceleration = new Vector2();
    public float jumpImpulse = 200f;
    public Vector2 terminalVelocity = new Vector2(250.1f, 1300.1f);
    public transient boolean onGround = true;
    private transient int jumpTime = 0;

    private transient RigidBody rb;


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

        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W) && onGround) {
            System.out.println(onGround);
            onGround = false;
            this.acceleration.y = -125;
            jumpTime = 90;
        }

        if(!onGround) {
            this.acceleration.y *= 0.80f;

    /*        this.velocity.y *= 0.35f;
            if(this.velocity.y < 0.01) {
                this.velocity.y = 0;
            }*/
            if(jumpTime-- < 0 ) {
                System.out.println(onGround+ " " + jumpTime);

                onGround = true;
                this.acceleration.y = 0;

            }
        }

//        System.out.println(this.rb.getLinearVelocity());




/*        float forceMagnitude = 1;
        Vector2 force = new Vector2(direction).mul(4);
        this.gameObject.getComponent(RigidBody.class).addForce(force);*/

        this.velocity.x += this.acceleration.x ;
        this.velocity.y += this.acceleration.y ;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);

//        System.out.println(this.velocity);

        this.rb.setLinearVelocity(this.velocity);



        if (!Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_W)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_A)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_S)
                && !Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_D)) {
            if (this.velocity.x == 0 || this.velocity.y == 0) {
                this.stateMachine.trigger("stopRunning");
            }
        }


//        this.velocity.x += this.acceleration.x * dt;
//        this.velocity.y += this.acceleration.y * dt;
//        this.rb.addForce(new Vector2(acceleration).mul(10000));

//        gameObject.transform.position.x += this.velocity.x;
//        gameObject.transform.position.y += this.velocity.y;

    }
}
