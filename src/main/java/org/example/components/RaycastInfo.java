package org.example.components;

import org.example.physics.common.Vector2;
import org.example.physics.primitives.Collider2D;
import org.example.physics.primitives.RayCastInput;
import org.example.physics.primitives.RaycastCallback;
import org.example.physics.primitives.RaycastResult;

public class RaycastInfo implements RaycastCallback {
    public Vector2 point;
    public Vector2 normal;
    public Collider2D hitObject;
    public Collider2D requestObject;
    public boolean hit;

    public RaycastInfo(Collider2D requestObject) {
        this.requestObject = requestObject;
        this.hitObject = null;
        point = new Vector2();
        normal = new Vector2();
        hit = false;
    }

    @Override
    public boolean reportHit(RayCastInput input, RaycastResult result) {
        if (this.requestObject != null && result.getCollider2D() == this.requestObject) {
            return false;
        }

        this.point = new Vector2(result.getPoint());
        this.normal = new Vector2(result.getNormal());
        this.hitObject = result.getCollider2D();
        this.hit = result.isHit();
        return this.hit;
    }
}
