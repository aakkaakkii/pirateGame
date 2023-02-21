package org.example.physics2d.primitives;

import org.example.physics2d.rigidbody.Rigidbody2D;
import org.example.physics2d.common.Vector2;

public class Circle {
    private float radius = 1.0f;
    private Rigidbody2D rigidBody = null;

    public Circle() {
    }

    public Circle(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public Vector2 getCenter() {
        return rigidBody.getPosition();
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setRigidBody(Rigidbody2D rigidBody) {
        this.rigidBody = rigidBody;
    }
}
