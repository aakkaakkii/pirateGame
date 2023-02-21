package org.example.physics2d.common;

import org.example.utils.JMath;

public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this.x = 0f;
        this.y = 0f;
    }

    public Vector2(Vector2 v) {
        x = v.x;
        y = v.y;
    }

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public Vector2 sub(Vector2 v) {
        this.x = x - v.x;
        this.y = y - v.y;
        return this;
    }

    public Vector2 add(Vector2 v) {
        this.x = x + v.x;
        this.y = y + v.y;
        return this;
    }

    public Vector2 mul(float scale) {
        this.x = x * scale;
        this.y = y * scale;
        return this;
    }

    public Vector2 mul(Vector2 v) {
        this.x = x * v.x;
        this.y = y * v.y;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public float dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    public Vector2 normalize() {
        float invLength = JMath.invsqrt(x * x + y * y);
        this.x = x * invLength;
        this.y = y * invLength;
        return this;
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2 zero() {
        this.x = 0;
        this.y = 0;
        return this;
    }
}
