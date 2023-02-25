package org.example.physics;

import org.example.physics.primitives.Box2D;
import org.example.physics.primitives.Circle;
import org.example.physics.primitives.Collider2D;
import org.example.physics2d.common.Vector2;


public class Collisions {

    public static CollisionManifold findCollisionFeatures(Collider2D c1, Collider2D c2) {
        if(c1 instanceof Circle && c2 instanceof Circle) {
            return intersectCircle((Circle) c1, (Circle) c2);
        } else if (c1 instanceof Box2D && c2 instanceof Box2D) {
            return intersectPolygons((Box2D) c1, (Box2D) c2);
        } else if(c1 instanceof Circle && c2 instanceof Box2D) {
            return intersectCircle((Circle) c1, (Box2D) c2);
        } else if(c1 instanceof Box2D && c2 instanceof Circle) {
            return intersectCircle((Circle) c2, (Box2D) c1);
        } else {
            assert false : "Unknown collider '" + c1.getClass() + "' vs '" + c2.getClass() + "'";
        }

        return null;
    }

    public static CollisionManifold intersectCircle(Circle c1, Circle c2) {
        Vector2 distance = new Vector2(c1.getCenter()).sub(c2.getCenter());
        float sumRadi = c1.getRadius() + c2.getRadius();

        CollisionManifold res = new CollisionManifold();

        if (distance.lengthSquared() - (sumRadi * sumRadi) > 0) {
            res.setColliding(false);
            return res;
        }

        res.setColliding(true);
        res.setNormal(new Vector2(distance).normalize());
        res.setDepth(Math.abs(sumRadi - new Vector2(distance).length()));

        return res;
    }

    public static CollisionManifold intersectCircle(Circle circle, Box2D box) {
        CollisionManifold res = new CollisionManifold();
        Vector2[] vertices = box.getVertices();
        res.setColliding(true);
        res.setDepth(Float.MAX_VALUE);

        for (int i = 0; i < vertices.length; i++) {
            Vector2 va = vertices[i];
            Vector2 vb = vertices[(i + 1) % vertices.length];

            Vector2 edge = new Vector2(vb).sub(va);
            Vector2 axis = new Vector2(-edge.y, edge.x).normalize();

            ProjectionRes projA = projectVertices(vertices, axis);
            ProjectionRes projB = ProjectCircle(circle, axis);

            if (projA.min >= projB.max || projB.min >= projA.max) {
                res.setColliding(false);
                return res;
            }
            float axisDepth = Math.min(projB.max - projA.min, projA.max - projB.min);
            if (axisDepth < res.getDepth()) {
                res.setDepth(axisDepth);
                res.setNormal(axis);
            }
        }

        int cpIndex = findClosestPointOnPolygon(circle.getCenter(), vertices);
        Vector2 cp = vertices[cpIndex];

        Vector2 axis = new Vector2(cp).sub(circle.getCenter()).normalize();


        ProjectionRes projA = projectVertices(vertices, axis);
        ProjectionRes projB = ProjectCircle(circle, axis);

        if (projA.min >= projB.max || projB.min >= projA.max) {
            res.setColliding(false);
            return res;
        }
        float axisDepth = Math.min(projB.max - projA.min, projA.max - projB.min);
        if (axisDepth < res.getDepth()) {
            res.setDepth(axisDepth);
            res.setNormal(axis);
        }


        Vector2 direction = new Vector2(circle.getRigidBody().getPosition()).sub(box.getRigidBody().getPosition());
//        res.setDepth(res.getDepth() / res.getNormal().length());
//        res.getNormal().normalize();
        if (new Vector2(direction).dot(res.getNormal()) > 0) {
            res.getNormal().mul(-1);
        }


        return res;
    }

    private static int findClosestPointOnPolygon(Vector2 circleCenter, Vector2[] vertices) {
        int result = -1;
        float minDistance = Float.MAX_VALUE;

        for(int i = 0; i < vertices.length; i++) {
            Vector2 v = vertices[i];
            float distance = new Vector2(v).sub(circleCenter).length();
            if(distance < minDistance) {
                minDistance = distance;
                result = i;
            }
        }

        return result;
    }

    private static ProjectionRes ProjectCircle(Circle circle, Vector2 axis) {
        Vector2 direction = new Vector2(axis).normalize();
        Vector2 directionAndRadius = new Vector2(direction).mul(circle.getRadius());

        Vector2 p1 = new Vector2(circle.getCenter()).add(directionAndRadius);
        Vector2 p2 = new Vector2(circle.getCenter()).sub(directionAndRadius);

        ProjectionRes res = new ProjectionRes();
        res.min = p1.dot(axis);
        res.max = p2.dot(axis);

        if(res.min > res.max) {
            float tmp = res.min;
            res.min = res.max;
            res.max = tmp;
        }

        return res;
    }


    public static CollisionManifold intersectPolygons(Box2D box1, Box2D box2) {
        CollisionManifold res = new CollisionManifold();
        res.setNormal(new Vector2());
        res.setDepth(Float.MAX_VALUE);
        res.setColliding(true);

        Vector2[] verticesA = box1.getVertices();
        Vector2[] verticesB = box2.getVertices();

        checkOnAxis(verticesA, verticesB, res);
        if (!res.isColliding()) return res;
        checkOnAxis(verticesB, verticesA, res);
        if (!res.isColliding()) return res;

        Vector2 direction = new Vector2(box2.getRigidBody().getPosition()).sub(box1.getRigidBody().getPosition());
        res.setDepth(res.getDepth() / res.getNormal().length());
        res.getNormal().normalize();
        if (new Vector2(direction).dot(res.getNormal()) > 0) {
            res.getNormal().mul(-1);
        }
//        res.setNormal(new Vector2(res.getNormal()).normalize());
        return res;
    }


    private static void checkOnAxis(Vector2[] verticesA, Vector2[] verticesB, CollisionManifold res) {
        for (int i = 0; i < verticesA.length; i++) {
            Vector2 va = verticesA[i];
            Vector2 vb = verticesA[(i + 1) % verticesA.length];

            Vector2 edge = new Vector2(vb).sub(va);
            Vector2 axis = new Vector2(-edge.y, edge.x);

            ProjectionRes projA = projectVertices(verticesA, axis);
            ProjectionRes projB = projectVertices(verticesB, axis);

            if (projA.min >= projB.max || projB.min >= projA.max) {
                res.setColliding(false);
                break;
            }
            float axisDepth = Math.min(projB.max - projA.min, projA.max - projB.min);
            if (axisDepth < res.getDepth()) {
                res.setDepth(axisDepth);
                res.setNormal(axis);
            }
        }

    }

    private static ProjectionRes projectVertices(Vector2[] vertices, Vector2 axis) {
        ProjectionRes res = new ProjectionRes();
        res.min = Float.MAX_VALUE;
        res.max = Float.MIN_VALUE;

        for (int i = 0; i < vertices.length; i++) {
            Vector2 v = vertices[i];
            float proj = new Vector2(axis).dot(v);
            res.min = Math.min(res.min, proj);
            res.max = Math.max(res.max, proj);
        }

        return res;
    }

    static class ProjectionRes {
        public ProjectionRes() {
        }

        public ProjectionRes(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public float min;
        public float max;
    }
}
