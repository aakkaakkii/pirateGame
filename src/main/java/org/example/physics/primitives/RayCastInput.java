package org.example.physics.primitives;

import org.example.physics.common.Vector2;

public class RayCastInput {
    public final Vector2 p1 = new Vector2();
    public final Vector2 p2 = new Vector2();

    public RayCastInput() {
    }

    public RayCastInput(Vector2 p1, Vector2 p2) {
        this.p1.set(p1);
        this.p2.set(p2);
    }

    public void set(RayCastInput rci) {
        this.p1.set(rci.p1);
        this.p2.set(rci.p2);
    }
}
