package org.example.utils;

import org.example.physics.common.Vector2;

public class JMath {

    public static void rotate(Vector2 vec, float angleDeg, Vector2 origin) {
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        float cos = (float) Math.cos(Math.toRadians(angleDeg));
        float sin = (float) Math.sin(Math.toRadians(angleDeg));

        vec.x = x * cos - y * sin + origin.x;
        vec.y= x * sin + y * cos + origin.y;

//        xPrime += origin.x;
//        yPrime += origin.y;

//        vec.x = xPrime;
//        vec.y = yPrime;
    }

    public static boolean compere(float x, float y, float epsilon) {
        return Math.abs(x - y) <= epsilon * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    public static boolean compere(float x, float y) {
        return Math.abs(x - y) <= Float.MIN_VALUE * Math.max(1.0f, Math.max(Math.abs(x), Math.abs(y)));
    }

    public static boolean compere(Vector2 vec1, Vector2 vec2) {
        return compere(vec1.x, vec2.x) && compere(vec1.y, vec2.y);
    }

    public static boolean compere(Vector2 vec1, Vector2 vec2, float epsilon) {
        return compere(vec1.x, vec2.x) && compere(vec1.y, vec2.y);
    }

    public static float invsqrt(float v) {
        if(v == 0) {
            return 0;
        }
        return 1.0f / (float) Math.sqrt(v);
    }

    public static float clamp(float min, float max, float value) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;

    }
}
