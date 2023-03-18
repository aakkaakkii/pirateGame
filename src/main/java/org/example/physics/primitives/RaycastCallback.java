package org.example.physics.primitives;

public interface RaycastCallback {

    boolean reportHit(RayCastInput input, RaycastResult result);
}
