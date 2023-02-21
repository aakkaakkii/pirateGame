package org.example.physics2d.rigidbody;

import org.example.physics2d.common.Vector2;
import org.example.physics2d.primitives.AABB;
import org.example.physics2d.primitives.Circle;

public class Collisions {
    public static CollisionManifold findCollisionFeatures(Circle c1, Circle c2) {
        CollisionManifold result = new CollisionManifold();
        float sumRadi = c1.getRadius() + c2.getRadius();
        Vector2 distance = new Vector2(c1.getCenter()).sub(c2.getCenter());
        if(distance.lengthSquared() - (sumRadi * sumRadi) > 0) {
            return result;
        }

        float depth = Math.abs(distance.length() - sumRadi) * 0.5f;
        Vector2 normal = new Vector2(distance).normalize();
        float distanceToPoint = c1.getRadius() - depth;
        Vector2 contactPoint = new Vector2(c1.getCenter()).add(
                        new Vector2(normal).mul(distanceToPoint));


        result = new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);
        return result;

    }


}
