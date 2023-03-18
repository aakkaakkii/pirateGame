package org.example.physics.primitives;

import org.example.physics.common.Vector2;

public class AABB {
    private Vector2 min = new Vector2();
    private Vector2 max = new Vector2();

    public AABB() {
    }

    public AABB(Vector2 min, Vector2 max) {
        this.min = min;
        this.max = max;
    }


    public Vector2 getMin() {
        return min;
    }

    public void setMin(Vector2 min) {
        this.min = min;
    }

    public Vector2 getMax() {
        return max;
    }

    public void setMax(Vector2 max) {
        this.max = max;
    }

    public boolean overlap(AABB b) {
        return AABB.overlap(this, b);
    }
    public static boolean overlap(AABB b1, AABB b2) {
        if (b1.getMax().x <= b2.getMin().x || b2.getMax().x <= b1.getMin().x ||
                b1.getMax().y <= b2.getMin().y || b2.getMax().y <= b1.getMin().y) {
            return false;
        }

        return true;
    }
}
