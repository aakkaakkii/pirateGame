package org.example.physics2d;

import org.example.physics2d.common.Vector2;

public class Line2D {
    private Vector2 from;
    private Vector2 to;

    public Line2D(Vector2 from, Vector2 to) {
        this.from = from;
        this.to = to;
    }

    public Vector2 getFrom() {
        return from;
    }

    public Vector2 getTo() {
        return to;
    }

    public Vector2 getStart() {
        return this.getFrom();
    }

    public Vector2 getEnd() {
        return this.getTo();
    }

    public float lengthSquared() {
        return new Vector2(to).sub(from).lengthSquared();
    }
}
