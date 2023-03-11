package org.example.physics.primitives;

import org.example.components.Component;
import org.example.physics.RigidBody;
import org.example.physics2d.common.Vector2;

public class Circle extends Component implements Collider2D {
    private RigidBody rigidBody;
    private float radius;

    public Circle() {
    }

    public Circle( float radius) {
        this.radius = radius;
    }

    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
        this.rigidBody.setCollider(this);
    }

    @Override
    public AABB getAABB() {
        Vector2 min = new Vector2(this.rigidBody.getPosition().x - this.radius,
                this.rigidBody.getPosition().y - this.radius);
        Vector2 max = new Vector2(this.rigidBody.getPosition().x + this.radius,
                this.rigidBody.getPosition().y + this.radius);

        return new AABB(min, max);
    }

    @Override
    public void rbTransformCallback() {

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
