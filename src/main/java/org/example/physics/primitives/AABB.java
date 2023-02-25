package org.example.physics.primitives;

import org.example.physics.RigidBody;
import org.example.physics2d.common.Vector2;

public class AABB {
    private RigidBody rigidBody;
    private Vector2 size = new Vector2();

    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
    }
}
