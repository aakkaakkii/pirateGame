package org.example.physics.primitives;

import org.example.physics.common.Vector2;

public class RaycastResult {
    private Vector2 point;
    private Vector2 normal;
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

    public static void reset(RaycastResult result) {
        if (result != null) {
            result.point.zero();
            result.normal.set(0, 0);
            result.hit = false;
        }
    }
}
