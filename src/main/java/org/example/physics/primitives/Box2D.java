package org.example.physics.primitives;

import org.example.components.Component;
import org.example.physics.RigidBody;
import org.example.physics.common.Matrix3;
import org.example.physics.common.Vector2;
import org.example.utils.JMath;

import java.awt.*;

public class Box2D extends Component implements Collider2D {
    private Vector2 size = new Vector2();
    private Vector2 halfSize = new Vector2();
    private RigidBody rigidBody = null;
    private Vector2[] transformVertices;
    private boolean transformUpdateRequired;
    private boolean aabbUpdateRequired;
    private AABB aabb;

    public Box2D() {
        this.halfSize = new Vector2(size).mul(0.5f);
        this.transformVertices = new Vector2[4];
        this.stateUpdateRequired(true);
    }

    public Box2D(Vector2 size) {
        this.size = size;
        this.halfSize = new Vector2(size).mul(0.5f);
        this.transformVertices = new Vector2[4];
        this.stateUpdateRequired(true);
    }

    public Vector2 getLocalMin() {
        return new Vector2(this.rigidBody.getPosition()).sub(new Vector2(this.halfSize));
    }

    public Vector2 getLocalMax() {
        return new Vector2(this.rigidBody.getPosition()).add(new Vector2(this.halfSize));
    }

    public Vector2[] getVertices() {
        if (this.transformUpdateRequired) {
            Vector2 min = getLocalMin();
            Vector2 max = getLocalMax();

            Vector2[] vertices = {
                    new Vector2(min.x, min.y), new Vector2(max.x, min.y),
                    new Vector2(max.x, max.y), new Vector2(min.x, max.y),
            };

            if (this.rigidBody.getRotation() != 0.0f) {
                float cos = (float) Math.cos(Math.toRadians(this.rigidBody.getRotation()));
                float sin = (float) Math.sin(Math.toRadians(this.rigidBody.getRotation()));
                Matrix3 matrix = new Matrix3(
                        cos, -sin, this.rigidBody.getPosition().x,
                        sin, cos, this.rigidBody.getPosition().y,
                        0, 0, 1
                );
                for (Vector2 vert : vertices) {
                    vert.sub(rigidBody.getPosition()).mulPosition(matrix);
                }

            }
            this.transformVertices = vertices;
        }
        this.transformUpdateRequired = false;

        return new Vector2[]{
                new Vector2(transformVertices[0]),
                new Vector2(transformVertices[1]),
                new Vector2(transformVertices[2]),
                new Vector2(transformVertices[3])
        };
    }

    @Override
    public float calculateRotationalInertia() {
        if (this.rigidBody != null) {
            return (1f / 12) * this.rigidBody.getMass() * (this.size.x * this.size.x + this.size.y * this.size.y);
        }
        return 0;
    }

    @Override
    public boolean raycast(RayCastInput input, RaycastResult result) {
        RaycastResult.reset(result);

        Vector2 p1 = new Vector2(input.p1);
        Vector2 p2 = new Vector2(input.p2);

        float cos = (float) Math.cos(Math.toRadians(-this.rigidBody.getRotation()));
        float sin = (float) Math.sin(Math.toRadians(-this.rigidBody.getRotation()));
        Matrix3 matrix = new Matrix3(
                cos, -sin, this.rigidBody.getPosition().x,
                sin, cos, this.rigidBody.getPosition().y,
                0, 0, 1
        );

//         p1.mulPosition(matrix);
//         p2.mulPosition(matrix);
        p1.sub(rigidBody.getPosition()).mulPosition(matrix);
        p2.sub(rigidBody.getPosition()).mulPosition(matrix);


//        JMath.rotate(p1, -this.rigidBody.getRotation(), new Vector2());
//        JMath.rotate(p2, -this.rigidBody.getRotation(), new Vector2());


        Vector2 direction = new Vector2(p2).sub(p1).normalize();
        direction.x = JMath.compere(direction.x, 0) ? 0.0001f : direction.x;
        direction.y = JMath.compere(direction.x, 0) ? 0.0001f : direction.y;

        Vector2 unitVector = new Vector2(direction);
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2 min = new Vector2(new Vector2(this.rigidBody.getPosition()).sub(this.halfSize));
        min.sub(p1).mul(unitVector);
        Vector2 max = new Vector2(new Vector2(this.rigidBody.getPosition()).add(this.halfSize));
        max.sub(p1).mul(unitVector);

        float tmin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tmax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));


        if (tmax < 0 || tmin > tmax) {
            return false;
        }

        if (tmin == -0)
            tmin = 0;

        float t = (tmin < 0f) ? tmax : tmin;

        boolean hit = t > 0f; //&& t * t < ray.getMaximum();

        if (!hit) {
            return false;
        }

        Vector2 point = new Vector2(p1);


        Vector2 pointOld = new Vector2(point).add(
                new Vector2(direction).mul(t));


        cos = (float) Math.cos(Math.toRadians(-this.rigidBody.getRotation()));
        sin = (float) Math.sin(Math.toRadians(-this.rigidBody.getRotation()));
        matrix = new Matrix3(
                cos, -sin, p1.x,
                sin, cos, p1.y,
                0, 0, 1
        );


        point = new Vector2(point).add(
                new Vector2(direction).mul(t));

        point.sub(p1).mulPosition(matrix);

        if (result != null) {

            Vector2 normal = new Vector2(this.rigidBody.getPosition()).sub(point);
            normal.normalize();

            result.init(point, normal, true);
        }


        return true;
    }
    public RigidBody getRigidBody() {
        return rigidBody;
    }

    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
        this.rigidBody.setCollider(this);
    }

    @Override
    public AABB getAABB() {
        if (this.aabbUpdateRequired) {
            Vector2 min = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);
            Vector2 max = new Vector2(Float.MIN_VALUE, Float.MIN_VALUE);

            Vector2[] vertices = getVertices();

            for (Vector2 v : vertices) {
                min.x = Math.min(v.x, min.x);
                max.x = Math.max(v.x, max.x);
                min.y = Math.min(v.y, min.y);
                max.y = Math.max(v.y, max.y);
            }

            this.aabb = new AABB(min, max);
        }
        this.aabbUpdateRequired = false;
        return this.aabb;
    }

    @Override
    public void rbTransformCallback() {
        stateUpdateRequired(true);
    }

    public void stateUpdateRequired(boolean b) {
        this.transformUpdateRequired = b;
        this.aabbUpdateRequired = b;
    }

}
