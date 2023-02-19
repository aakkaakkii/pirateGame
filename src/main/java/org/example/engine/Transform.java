package org.example.engine;

import org.example.components.Component;
import org.example.utils.Vector2;


public class Transform extends Component {
    public Vector2 position;
    public Vector2 scale;
    public float rotation = 0.0f;

    public Transform() {
        init(new Vector2(), new Vector2(1,1));
    }

    public Transform(Vector2 position) {
        init(position, new Vector2(1,1));
    }

    public Transform(Vector2 position, Vector2 scale) {
        this.position = position;
        this.scale = scale;
        this.rotation = 0;
    }

    public void init(Vector2 position, Vector2 scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        Transform transform = new Transform(this.position.copy());
        transform.scale = this.scale.copy();
        transform.rotation = rotation;
        return transform;
    }


    @Override
    public String toString() {
        return "Position (" + position.x + ", " + position.y + ")";
    }

}
