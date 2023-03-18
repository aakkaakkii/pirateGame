package org.example.physics.common;

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

    public Vector2 sub(float x, float y) {
        this.x = this.x - x;
        this.y = this.y - y;
        return this;
    }

    public Vector2 add(Vector2 v) {
        this.x = x + v.x;
        this.y = y + v.y;
        return this;
    }

    public Vector2 add(float x, float y) {
        this.x = this.x + x;
        this.y = this.y + y;
        return this;
    }

    public Vector2 mul(float v) {
        this.x = x * v;
        this.y = y * v;
        return this;
    }

    public Vector2 mul(float x, float y) {
        this.x = this.x * x;
        this.y = this.y * y;
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

    public Vector2 mulPosition(Matrix3 mat) {
        float rx = this.x * mat.m00 + this.y * mat.m01 + mat.m02;
        float ry = this.x * mat.m10 + this.y * mat.m11 + mat.m12;
        this.x = rx;
        this.y = ry;
        return this;
    }

    public float cross(Vector2 v) {
        return this.x * v.y - this.y * v.x;
    }

    public float get(int component) throws IllegalArgumentException {
        switch (component) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Vector2 setComponent(int component, float value) throws IllegalArgumentException {
        switch (component) {
            case 0:
                x = value;
                break;
            case 1:
                y = value;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return this;
    }


    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
