package org.example.physics2d.forces;


import org.example.physics.RigidBody;

public interface ForceGenerator {
    void updateForce(RigidBody body, float dt);

}
