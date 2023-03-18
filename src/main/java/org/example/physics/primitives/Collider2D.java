package org.example.physics.primitives;

import org.example.physics.RigidBody;

public interface Collider2D  {
    RigidBody getRigidBody();
    void setRigidBody(RigidBody rb);
    AABB getAABB();
    void rbTransformCallback();
    float calculateRotationalInertia();
    boolean raycast(RayCastInput input, RaycastResult result);

}
