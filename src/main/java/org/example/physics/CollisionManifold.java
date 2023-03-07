package org.example.physics;

import org.example.physics2d.common.Vector2;

public class CollisionManifold {
    private boolean isColliding;
    private Vector2 normal;
    private float depth;

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

    @Override
    public String toString() {
        return "CollisionManifold{" +
                "isColliding=" + isColliding +
                ", normal=" + normal +
                ", depth=" + depth +
                '}';
    }
}
