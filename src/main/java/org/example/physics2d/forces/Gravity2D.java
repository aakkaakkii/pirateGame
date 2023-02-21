package org.example.physics2d.forces;

import org.example.physics2d.common.Vector2;
import org.example.physics2d.rigidbody.Rigidbody2D;

public class Gravity2D implements ForceGenerator {
    private Vector2 gravity;

    public Gravity2D(Vector2 force) {
        this.gravity = new Vector2(force);
    }

    @Override
    public void updateForce(Rigidbody2D body, float dt) {
        body.addForce(new Vector2(gravity).mul(body.getMass()));
    }
}
