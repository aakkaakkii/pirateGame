package org.example.physics.primitives;

import org.example.components.Component;
import org.example.physics.RigidBody;
import org.example.physics2d.common.Vector2;

public class Circle extends Component implements Collider2D {
    private RigidBody rigidBody;
    private float radius;

    public Circle() {
    }

    public Circle(RigidBody rigidBody, float radius) {
        this.rigidBody = rigidBody;
        this.radius = radius;
    }

    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
        this.rigidBody.setCollider(this);
    }

    public RigidBody getRigidBody() {
        return rigidBody;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector2 getCenter() {
        return rigidBody.getPosition();
    }
}
