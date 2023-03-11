package org.example.physics;

import org.example.physics2d.common.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollisionManifold {
    private boolean isColliding;
    private Vector2 normal;
    private float depth;
    private List<Vector2> contactPoint = new ArrayList<>();
    private RigidBody b1;
    private RigidBody b2;

    public CollisionManifold() {
        normal = new Vector2();
        depth = 0.0f;
        isColliding = false;
    }

    public CollisionManifold(Vector2 normal, float depth) {
        this.normal = normal;
        this.depth = depth;
        isColliding = true;
    }


    public boolean isColliding() {
        return isColliding;
    }

    public void setColliding(boolean colliding) {
        isColliding = colliding;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public void addContactPoint(Vector2 contactPoint) {
        this.contactPoint.add(contactPoint);
    }

    public List<Vector2> getContactPoint() {
        return contactPoint;
    }

    public RigidBody getB1() {
        return b1;
    }

    public void setB1(RigidBody b1) {
        this.b1 = b1;
    }

    public RigidBody getB2() {
        return b2;
    }

    public void setB2(RigidBody b2) {
        this.b2 = b2;
    }

    @Override
    public String toString() {
        return "CollisionManifold{" +
                "isColliding=" + isColliding +
                ", normal=" + normal +
                ", depth=" + depth +
                ", contactPoint=" + contactPoint +
                '}';
    }
}
