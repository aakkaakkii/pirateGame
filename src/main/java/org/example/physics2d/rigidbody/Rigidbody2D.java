package org.example.physics2d.rigidbody;

import org.example.components.Component;
import org.example.engine.GameObject;
import org.example.engine.Transform;
import org.example.physics2d.common.Vector2;
import org.example.utils.JMath;

public class Rigidbody2D extends Component {
    private Vector2 position = new Vector2();
    private float rotation = 0.0f;
    private float mass =0.0f;
    private float inverseMass =0.0f;

    private Vector2 forceAccum = new Vector2();
    private Vector2 linearVelocity = new Vector2();
    private Vector2 velocity = new Vector2();
    private float angularVelocity = 0.0f;
    private float linearDamping = 0.0f;
    private float angularDamping = 0.0f;

    private boolean fixedRotation = false;


    public Rigidbody2D() {
        if(gameObject != null) {
            this.position.set(gameObject.transform.position);
        }
    }

    public void init() {
    }

    public Vector2 getPosition() {
        return position;
    }

    public void physicsUpdate(float dt) {
        if(this.mass == 0.0f) {
            return;
        }

        //Linear velocity
        Vector2 acceleration = new Vector2(forceAccum).mul(this.inverseMass);
        linearVelocity.add(acceleration.mul(dt));

        // Update the linear position
        this.position.add(new Vector2(linearVelocity).mul(dt));

        synchCollisionTransforms();
        clearAccumulators();
    }

    public void synchCollisionTransforms() {
        if(gameObject != null) {
            gameObject.transform.position.set(this.position);
        }
    }

    public void clearAccumulators(){
        this.forceAccum.zero();
    }

    public void setTransform(Vector2 position) {
        this.position = position;
    }

    public void setTransform(Vector2 position, float rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        if(this.mass != 0.0f) {
            this.inverseMass = 1.0f / this.mass;
        }
    }

    public void addForce(Vector2 force) {
        if(!JMath.compere(force.x, 0)) {
            System.out.println(force.x);
        }
        this.forceAccum.add(force);
    }

    public void setVelocity(Vector2 velocity) {
        this.linearVelocity.set(velocity);
    }
}
