package org.example.physics;

import org.example.physics.primitives.AABB;
import org.example.physics.primitives.Box2D;
import org.example.physics.primitives.Circle;
import org.example.physics.primitives.Collider2D;
import org.example.physics.common.Vector2;
import org.example.utils.JMath;


public class Collisions {

    public static CollisionManifold findCollisionFeatures(Collider2D c1, Collider2D c2) {
        if (c1 instanceof Circle && c2 instanceof Circle) {
            return intersectCircle((Circle) c1, (Circle) c2);
        } else if (c1 instanceof Box2D && c2 instanceof Box2D) {
            return intersectPolygons((Box2D) c1, (Box2D) c2);
        } else if (c1 instanceof Circle && c2 instanceof Box2D) {
            return intersectCirclePolygon((Circle) c1, (Box2D) c2);
        } else if (c1 instanceof Box2D && c2 instanceof Circle) {
            CollisionManifold c = intersectCirclePolygon((Circle) c2, (Box2D) c1);
            c.getNormal().mul(-1);
            return c;
        } else {
            assert false : "Unknown collider '" + c1.getClass() + "' vs '" + c2.getClass() + "'";
        }

        return null;
    }

    public static boolean isCollidingAABB(AABB b1, AABB b2) {
        if (b1.getMax().x <= b2.getMin().x || b2.getMax().x <= b1.getMin().x ||
                b1.getMax().y <= b2.getMin().y || b2.getMax().y <= b1.getMin().y) {
            return false;
        }

        return true;
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

    public static CollisionManifold intersectCirclePolygon(Circle circle, Box2D box) {
        CollisionManifold res = new CollisionManifold();
        res.setNormal(new Vector2());
        res.setDepth(Float.MAX_VALUE);
        res.setColliding(true);

        Vector2[] vertices = box.getVertices();
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

        Vector2 direction = new Vector2(box.getRigidBody().getPosition()).sub(circle.getRigidBody().getPosition());
        res.setDepth(res.getDepth() / res.getNormal().length());
        res.getNormal().normalize();
        if (new Vector2(direction).dot(res.getNormal()) > 0) {
            res.getNormal().mul(-1);
        }


        return res;
    }

    private static int findClosestPointOnPolygon(Vector2 circleCenter, Vector2[] vertices) {
        int result = -1;
        float minDistance = Float.MAX_VALUE;

        for (int i = 0; i < vertices.length; i++) {
            Vector2 v = vertices[i];
            float distance = new Vector2(v).sub(circleCenter).length();
            if (distance < minDistance) {
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

        if (res.min > res.max) {
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

    public static Contact pointSegmentDistance(Vector2 p, Vector2 a, Vector2 b) {
        Contact c = new Contact();
        Vector2 ab = new Vector2(b).sub(a);
        Vector2 ap = new Vector2(p).sub(a);

        float proj = ap.dot(ab);
        float abLenSqr = ab.lengthSquared();
        float d = proj / abLenSqr;

        if (d <= 0f) {
            c.contact = new Vector2(a);
        } else if (d >= 1f) {
            c.contact = new Vector2(b);
        } else {
            c.contact = new Vector2(a).add(ab.mul(d));
        }

        c.distanceSqr = new Vector2(p).sub(c.contact).lengthSquared();

        return c;
    }

    public static Vector2[] findContactPoint(Collider2D c1, Collider2D c2) {
        if (c1 instanceof Circle && c2 instanceof Circle) {
            return findContactPoint((Circle) c1, (Circle) c2);
        } else if (c1 instanceof Box2D && c2 instanceof Box2D) {
            return findContactPoint((Box2D) c1, (Box2D) c2);
        } else if (c1 instanceof Circle && c2 instanceof Box2D) {
            return findContactPoint((Circle) c1, (Box2D) c2);
        } else if (c1 instanceof Box2D && c2 instanceof Circle) {
            return findContactPoint((Circle) c2, (Box2D) c1);
        } else {
            assert false : "Unknown collider '" + c1.getClass() + "' vs '" + c2.getClass() + "'";
        }

        return null;
    }

    public static Vector2[] findContactPoint(Box2D b1, Box2D b2) {
        Vector2[] res = {new Vector2(), new Vector2()};
        Vector2[] verticesA = b1.getVertices();
        Vector2[] verticesB = b2.getVertices();

        float minDistanceSqr = Float.MAX_VALUE;

        for (int i = 0; i < verticesA.length; i++) {
            Vector2 p = verticesA[i];
            for (int j = 0; j < verticesB.length; j++) {
                Vector2 va = verticesB[j];
                Vector2 vb = verticesB[(j + 1) % verticesB.length];

                Contact contact = pointSegmentDistance(p, va, vb);

                if (JMath.compere(contact.distanceSqr, minDistanceSqr)) {
                    if (!JMath.compere(contact.contact, res[0])
                            && !JMath.compere(contact.contact, res[1])) {
                        res[1] = contact.contact;
                    }
                } else if (contact.distanceSqr < minDistanceSqr) {
                    minDistanceSqr = contact.distanceSqr;
                    res[0] = contact.contact;
                }
            }
        }

        for (int i = 0; i < verticesB.length; i++) {
            Vector2 p = verticesB[i];
            for (int j = 0; j < verticesA.length; j++) {
                Vector2 va = verticesA[j];
                Vector2 vb = verticesA[(j + 1) % verticesA.length];

                Contact contact = pointSegmentDistance(p, va, vb);

                if (JMath.compere(contact.distanceSqr, minDistanceSqr)) {
                    if (!JMath.compere(contact.contact, res[0])
                            && !JMath.compere(contact.contact, res[1])) {
                        res[1] = contact.contact;
                    }
                } else if (contact.distanceSqr < minDistanceSqr) {
                    minDistanceSqr = contact.distanceSqr;
                    res[0] = contact.contact;
                }
            }
        }


        return res;
    }

    public static Vector2[] findContactPoint(Circle circle, Box2D box) {
        Vector2 cp = new Vector2();
        Vector2[] vertices = box.getVertices();
        float minDistSq = Float.MAX_VALUE;
        for (int i = 0; i < vertices.length; i++) {
            Vector2 va = vertices[i];
            Vector2 vb = vertices[(i + 1) % vertices.length];
            Contact contact = pointSegmentDistance(circle.getRigidBody().getPosition(), va, vb);

            if (contact.distanceSqr < minDistSq) {
                minDistSq = contact.distanceSqr;
                cp = contact.contact;
            }
        }
        return new Vector2[]{cp};
    }


    public static void findMinContactPoint() {

    }


    public static Vector2[] findContactPoint(Circle c1, Circle c2) {
        Vector2 ab = new Vector2(c2.getRigidBody().getPosition()).sub(c1.getRigidBody().getPosition());
        return new Vector2[]{new Vector2(c1.getRigidBody().getPosition())
                .add(ab.normalize().mul(c1.getRadius()))};
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

    static class Contact {
        public Vector2 contact;
        public float distanceSqr;

        public Contact() {
        }

        public Contact(Vector2 contact, float distanceSqr) {
            this.contact = contact;
            this.distanceSqr = distanceSqr;
        }
    }
}
