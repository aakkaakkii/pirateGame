package org.example.physics2d.primitives;

import org.example.physics2d.rigidbody.Rigidbody2D;
import org.example.physics2d.common.Vector2;

public class AABB {
    private Vector2 size = new Vector2();
    private Vector2 halfSize = new Vector2();
    private Rigidbody2D rigidBody = null;

    public AABB() {
        this.halfSize = new Vector2(size.mul(0.5f));
    }

    public AABB(Vector2 min, Vector2 max) {
        this.size = new Vector2(max).sub(min);
        this.halfSize = new Vector2(size.mul(0.5f));
    }

    public Vector2 getMin() {
        return new Vector2(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2 getMax() {
        return new Vector2(this.rigidBody.getPosition()).add(this.halfSize);
    }

    public void setRigidBody(Rigidbody2D rigidBody) {
        this.rigidBody = rigidBody;
    }

    public void setSize(Vector2 size) {
        this.size.set(size);
        this.halfSize.set(size.x / 2.0f, size.y / 2.0f);
    }
}
