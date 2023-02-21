package org.example.physics2d.rigidbody;

import org.example.physics2d.common.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollisionManifold {
    private boolean isColliding;
    private Vector2 normal;
    private List<Vector2> contactPoint;
    private float depth;

    public CollisionManifold() {
        normal = new Vector2();
        depth = 0.0f;
        isColliding = false;
    }

    public CollisionManifold(Vector2 normal, float depth) {
        this.normal = normal;
        this.contactPoint = new ArrayList<>();
        this.depth = depth;
        isColliding = true;
    }

    public void addContactPoint(Vector2 contactPoint) {
        this.contactPoint.add(contactPoint);
    }

    public Vector2 getNormal() {
        return normal;
    }

    public List<Vector2> getContactPoint() {
        return contactPoint;
    }

    public float getDepth() {
        return depth;
    }


}
