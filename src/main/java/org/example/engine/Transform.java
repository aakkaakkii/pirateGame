package org.example.engine;

import org.example.components.Component;
import org.example.physics.common.Vector2;


public class Transform extends Component {
    public Vector2 position;
    public Vector2 scale;
    public float rotation = 0.0f;
    private float sin, cos;

    public Transform() {
        init(new Vector2(), new Vector2(1, 1), 0);
    }

    public Transform(Vector2 position) {
        init(position, new Vector2(1, 1), 0);
    }

    public Transform(Vector2 position, float rotation) {
        init(position, new Vector2(), rotation);
    }

    public Transform(Vector2 position, Vector2 scale) {
        init(position, scale, rotation);
    }

    public void init(Vector2 position, Vector2 scale, float rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.sin = (float) Math.sin(rotation);
        this.cos = (float) Math.cos(rotation);
    }

    public Transform copy() {
        Transform transform = new Transform(this.position.copy());
        transform.scale = this.scale.copy();
        transform.rotation = rotation;
        return transform;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getScale() {
        return scale;
    }

    public float getRotation() {
        return rotation;
    }

    public float getSin() {
        return sin;
    }

    public float getCos() {
        return cos;
    }

    public static Vector2 transform(Vector2 v, Transform t) {
        return new Vector2(t.getCos() * v.x - t.getSin() * v.x + t.getPosition().x,
                t.getSin() * v.y - t.getCos() * v.y + t.getPosition().y);
    }

    @Override
    public String toString() {
        return "Position (" + position.x + ", " + position.y + ")";
    }

}
