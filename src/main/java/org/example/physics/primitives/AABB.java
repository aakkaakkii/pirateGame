package org.example.physics.primitives;

import org.example.physics2d.common.Vector2;

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
}
