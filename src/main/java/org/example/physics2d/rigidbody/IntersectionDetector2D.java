package org.example.physics2d.rigidbody;

import org.example.physics2d.Line2D;
import org.example.physics2d.primitives.AABB;
import org.example.physics2d.primitives.Box2D;
import org.example.physics2d.primitives.Circle;
import org.example.physics2d.common.Vector2;
import org.example.utils.JMath;

public class IntersectionDetector2D {

    public static boolean pointOnLine(Vector2 point, Line2D line) {
        float dy = line.getEnd().y - line.getStart().y;
        float dx = line.getEnd().x - line.getStart().x;

        if (dx == 0f) {
            return JMath.compere(point.x, line.getStart().x);
        }

        float m = dy / dx;

        float b = line.getEnd().y - (m * line.getEnd().x);

        return point.y == m * point.x + b;
    }

    public static boolean pointInCircle(Vector2 point, Circle circle) {
        Vector2 circleCenter = circle.getCenter();
        Vector2 centerToPoint = new Vector2(point).sub(circleCenter);

        return centerToPoint.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean pointInAABB(Vector2 point, AABB box) {
        Vector2 min = box.getMin();
        Vector2 max = box.getMax();

        return point.x <= max.x && min.x <= point.x
                && point.y <= max.y && min.y <= point.y;
    }

    public static boolean pointInBox2D(Vector2 point, Box2D box) {
        Vector2 pointLocalBoxSpace = new Vector2(point);
        JMath.rotate(pointLocalBoxSpace, box.getRigidBody().getRotation(), box.getRigidBody().getPosition());


        Vector2 min = box.getLocalMin();
        Vector2 max = box.getLocalMax();

        return pointLocalBoxSpace.x <= max.x && min.x <= pointLocalBoxSpace.x
                && pointLocalBoxSpace.y <= max.y && min.y <= pointLocalBoxSpace.y;
    }

    public static boolean lineAndCircle(Line2D line, Circle circle) {
        if (pointInCircle(line.getStart(), circle) || pointInCircle(line.getEnd(), circle)) {
            return true;
        }

        Vector2 ab = new Vector2(line.getEnd()).sub(line.getStart());
        //Project point onto ab line
        // t percentage
        Vector2 circleCenter = circle.getCenter();
        Vector2 centerToLineStart = new Vector2(circleCenter).sub(line.getStart());

        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        Vector2 closestPoint = new Vector2(line.getStart()).add(ab.mul(t));

        return pointInCircle(closestPoint, circle);
    }

    public static boolean lineAndAABB(Line2D line, AABB box) {
        if (pointInAABB(line.getStart(), box) || pointInAABB(line.getEnd(), box)) {
            return true;
        }

        Vector2 unitVector = new Vector2(line.getEnd())
                .sub(line.getStart())
                .normalize();

        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;


        Vector2 min = box.getMin();
        min.sub(line.getStart()).mul(unitVector);
        Vector2 max = box.getMax();
        max.sub(line.getStart()).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));

        if (tmax < 0 || tmin > tmax) {
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        return t > 0f && t * t < line.lengthSquared();
    }

    public static boolean lineAndBox2D(Line2D line2D, Box2D box) {
        float theta = -box.getRigidBody().getRotation();
        Vector2 center = box.getRigidBody().getPosition();
        Vector2 localStart = new Vector2(line2D.getStart());
        Vector2 localEnd = new Vector2(line2D.getEnd());

        JMath.rotate(localStart, theta, center);
        JMath.rotate(localEnd, theta, center);

        Line2D localLine = new Line2D(localStart, localEnd);
        AABB aabb = new AABB(box.getLocalMin(), box.getLocalMax());

        return lineAndAABB(localLine, aabb);
    }

    public static boolean circleAndCircle(Circle c1, Circle c2) {
        Vector2 vectorBetweenCenters = new Vector2(c1.getCenter()).sub(c2.getCenter());
        float radiusSum = c1.getRadius() + c2.getRadius();
        return vectorBetweenCenters.lengthSquared() <= radiusSum * radiusSum;
    }

    public static boolean circleAndAABB(Circle circle, AABB box) {
        Vector2 min = box.getMin();
        Vector2 max = box.getMax();

        Vector2 closestPointToCircle = new Vector2(circle.getCenter());
        closestPointToCircle.x = clamp(min.x, max.x, closestPointToCircle.x);
        closestPointToCircle.y = clamp(min.y, max.y, closestPointToCircle.y);

        Vector2 circleToBox = new Vector2(circle.getCenter())
                .sub(closestPointToCircle);

        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static boolean circleAndBox2D(Circle circle, Box2D box) {
        Vector2 min = new Vector2();
        Vector2 max = new Vector2(box.getHalfSize().mul(2.0f));

        Vector2 r = new Vector2(circle.getCenter()).sub(box.getRigidBody().getPosition());
        JMath.rotate(r, -box.getRigidBody().getRotation(), new Vector2(0, 0));
        Vector2 localCirclePos = new Vector2(r).add(box.getHalfSize());

        Vector2 closestPointToCircle = new Vector2(localCirclePos);
        closestPointToCircle.x = clamp(min.x, max.x, closestPointToCircle.x);
        closestPointToCircle.y = clamp(min.y, max.y, closestPointToCircle.y);

        Vector2 circleToBox = new Vector2(localCirclePos)
                .sub(closestPointToCircle);

        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    public static float clamp(float min, float max, float value) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        } else {
            return value;
        }
    }

    public static boolean AABBAndBox2D(AABB b1, Box2D b2) {
        Vector2 axesToTest[] = {
                new Vector2(0, 1), new Vector2(1, 0),
                new Vector2(0, 1), new Vector2(1, 0)
        };
        JMath.rotate(axesToTest[2], b2.getRigidBody().getRotation(), new Vector2());
        JMath.rotate(axesToTest[3], b2.getRigidBody().getRotation(), new Vector2());
        for (int i=0; i < axesToTest.length; i++) {
            if (!overlapOnAxis(b1, b2, axesToTest[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean AABBandAABB(AABB b1, AABB b2) {
        Vector2 axesToTest[] = {new Vector2(0, 1), new Vector2(1, 0)};

        for (Vector2 vector2 : axesToTest) {
            if (!overlapOnAxis(b1, b2, vector2)) {
                return false;
            }
        }
        return true;
    }

    private static boolean overlapOnAxis(AABB b1, AABB b2, Vector2 axis) {
        Vector2 interval1 = getInterval(b1, axis);
        Vector2 interval2 = getInterval(b2, axis);
        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static boolean overlapOnAxis(AABB b1, Box2D b2, Vector2 axis) {
        Vector2 interval1 = getInterval(b1, axis);
        Vector2 interval2 = getInterval(b2, axis);
        return ((interval2.x <= interval1.y) && (interval1.x <= interval2.y));
    }

    private static Vector2 getInterval(AABB rect, Vector2 axis) {
        Vector2 result = new Vector2(0, 0);

        Vector2 min = rect.getMin();
        Vector2 max = rect.getMax();


        Vector2[] vertices = {
                new Vector2(min.x, min.y),
                new Vector2(min.x, max.y),
                new Vector2(max.x, min.y),
                new Vector2(max.x, max.y),
        };

        result.x = axis.dot(vertices[0]);
        result.y = result.x;

        for (int i = 1; i < 4; i++) {
            float projection = axis.dot(vertices[i]);
            if (projection < result.x) {
                result.x = projection;
            }

            if (projection > result.y) {
                result.y = projection;
            }
        }


        return result;
    }

    private static Vector2 getInterval(Box2D rect, Vector2 axis) {
        Vector2 result = new Vector2(0, 0);

        Vector2 vertices[] = rect.getVertices();

        result.x = axis.dot(vertices[0]);
        result.y = result.x;
        for (int i=1; i < 4; i++) {
            float projection = axis.dot(vertices[i]);
            if (projection < result.x) {
                result.x = projection;
            }
            if (projection > result.y) {
                result.y = projection;
            }
        }
        return result;
    }
}
