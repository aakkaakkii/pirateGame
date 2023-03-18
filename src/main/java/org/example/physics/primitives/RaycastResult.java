package org.example.physics.primitives;

import org.example.physics.common.Vector2;

public class RaycastResult {
    private Vector2 point;
    private Vector2 normal;
    private Collider2D collider2D;
    private boolean hit;

    public RaycastResult() {
        this.point = new Vector2();
        this.normal = new Vector2();
        this.hit = false;
    }

    public void init(Vector2 point, Vector2 normal, boolean hit) {
        this.point.set(point);
        this.normal.set(normal);
        this.hit = hit;
    }

    public void init(Vector2 point, Vector2 normal, boolean hit, Collider2D collider2D) {
        this.point.set(point);
        this.normal.set(normal);
        this.hit = hit;
        this.collider2D = collider2D;
    }

    public static void reset(RaycastResult result) {
        if (result != null) {
            result.point.zero();
            result.normal.set(0, 0);
            result.hit = false;
        }
    }

    public Vector2 getPoint() {
        return point;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public boolean isHit() {
        return hit;
    }

    public Collider2D getCollider2D() {
        return collider2D;
    }

    public void setCollider2D(Collider2D collider2D) {
        this.collider2D = collider2D;
    }
}
