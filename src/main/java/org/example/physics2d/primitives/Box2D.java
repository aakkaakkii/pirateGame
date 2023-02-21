package org.example.physics2d.primitives;

import org.example.physics2d.rigidbody.Rigidbody2D;
import org.example.physics2d.common.Vector2;
import org.example.utils.JMath;

public class Box2D {
    private Vector2 size = new Vector2();
    private Vector2 halfSize = new Vector2();
    private Rigidbody2D rigidBody = null;


    public Box2D() {
        this.halfSize = new Vector2(size.mul(0.5f));
    }

    public Box2D(Vector2 min, Vector2 max) {
        this.size = new Vector2(max).sub(min);
        this.halfSize = new Vector2(size.mul(0.5f));
    }

    public Vector2 getLocalMin() {
        return new Vector2(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2 getLocalMax() {
        return new Vector2(this.rigidBody.getPosition()).add(this.halfSize);
    }

    public Vector2[] getVertices() {
        Vector2 min = getLocalMin();
        Vector2 max = getLocalMax();

        Vector2[] vertices = {
                new Vector2(min.x, min.y),
                new Vector2(min.x, max.y),
                new Vector2(max.x, min.y),
                new Vector2(max.x, max.y),
        };

        if (rigidBody.getRotation() != 0.0f) {
            for (Vector2 vertex : vertices) {
                JMath.rotate(vertex, this.rigidBody.getRotation(), this.rigidBody.getPosition());
            }

        }

        return vertices;
    }

    public Rigidbody2D getRigidBody() {
        return rigidBody;
    }

    public Vector2 getHalfSize() {
        return this.halfSize;
    }

    public void setRigidBody(Rigidbody2D rigidBody) {
        this.rigidBody = rigidBody;
    }

    public void setSize(Vector2 size) {
        this.size.set(size);
        this.halfSize.set(size.x / 2.0f, size.y / 2.0f);
    }
}
