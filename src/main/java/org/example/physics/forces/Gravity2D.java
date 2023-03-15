package org.example.physics.forces;

import org.example.physics.RigidBody;
import org.example.physics.common.Vector2;

public class Gravity2D implements ForceGenerator{
    private Vector2 gravity;

    public Gravity2D(Vector2 force) {
        this.gravity = new Vector2(force);
    }


    @Override
    public void updateForce(RigidBody body, float dt) {
        body.addForce(new Vector2(gravity).mul(body.getMass()));
    }
}
