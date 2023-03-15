package org.example.physics.primitives;

import org.example.components.Component;
import org.example.physics.RigidBody;
import org.example.physics.common.Vector2;

public class Circle extends Component implements Collider2D {
    private RigidBody rigidBody;
    private float radius;

    public Circle(float radius) {
        this.radius = radius;
    }

    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
        this.rigidBody.setCollider(this);
    }

    @Override
    public float calculateRotationalInertia() {
        if (this.getRigidBody() != null) {
            return (1f / 2) * this.getRigidBody().getMass() * (this.radius * this.radius);
        }
        return 0;
    }

    @Override
    public boolean raycast(RayCastInput input, RaycastResult result) {
        RaycastResult.reset(result);
        Vector2 startToCircle = new Vector2(this.rigidBody.getPosition()).sub(input.p1);
        float radiusSquared = this.radius * this.radius;
        float startToCircleLengthSquared = startToCircle.lengthSquared();
        Vector2 direction = new Vector2(input.p2).sub(input.p1).normalize();

        // Project vector from the start point onto the direction of the ray
        float a = startToCircle.dot(direction);
        float bSq = startToCircleLengthSquared - (a * a);
        if (radiusSquared - bSq < -.0f) {
            return false;
        }

        float f = (float) Math.sqrt(radiusSquared - bSq);
        float t = 0;

        if (startToCircleLengthSquared < radiusSquared) {
            t = a + f;
        } else {
            t = a - f;
        }

        if(result != null) {
            Vector2 point = new Vector2(input.p1).add(direction.mul(t));
            Vector2 normal = new Vector2(point).sub(this.rigidBody.getPosition());
            normal.normalize();

            result.init(point, normal, true);
        }

        return true;
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
