package org.example.physics2d;

import org.example.physics2d.common.Vector2;
import org.example.physics2d.forces.ForceRegistry;
import org.example.physics2d.forces.Gravity2D;
import org.example.physics2d.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem2D {
    private ForceRegistry forceRegistry;
    private List<Rigidbody2D> rigidbodies;
    private Gravity2D gravity;
    private float fixedUpdate;


    public PhysicsSystem2D(float fixedUpdateDt, Vector2 gravity) {
        forceRegistry = new ForceRegistry();
        this.fixedUpdate = fixedUpdateDt;
        this.gravity = new Gravity2D(gravity);
        this.rigidbodies = new ArrayList<>();
    }

    public void update(float dt) {
        fixedUpdate();
    }

    public void fixedUpdate() {
        forceRegistry.updateForces(fixedUpdate);
        for (Rigidbody2D rb : rigidbodies) {
            rb.physicsUpdate(fixedUpdate);
        }

    }

    public void addRigidbody(Rigidbody2D body){
        this.rigidbodies.add(body);
//        this.forceRegistry.add(body, gravity);
    }
}
