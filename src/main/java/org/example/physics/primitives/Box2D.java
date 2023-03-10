package org.example.physics.primitives;

import org.example.components.Component;
import org.example.engine.Transform;
import org.example.physics.RigidBody;
import org.example.physics2d.common.Matrix3;
import org.example.physics2d.common.Vector2;
import org.example.utils.JMath;

public class Box2D extends Component implements Collider2D {
    private Vector2 size = new Vector2();
    private Vector2 halfSize = new Vector2();
    private RigidBody rigidBody = null;
    private Vector2[] vertices;
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

    private Vector2[] createVertices() {
        Vector2 min = getLocalMin();
        Vector2 max = getLocalMax();

        return new Vector2[]{
                new Vector2(min.x, max.y), new Vector2(max.x, max.y),
                new Vector2(min.x, min.y), new Vector2(max.x, min.y)
        };
    }

    public Vector2[] getTransformVertices() {
        if (this.transformUpdateRequired) {
            float cos = (float) Math.cos(this.rigidBody.getRotation());
            float sin = (float) Math.sin(this.rigidBody.getRotation());

            for (int i = 0; i < this.vertices.length; i++) {
                Matrix3 matrix = new Matrix3(
                        cos, -sin, this.rigidBody.getPosition().x,
                        sin, cos, this.rigidBody.getPosition().y,
                        0, 0, 1
                );
                this.vertices[i].mulPosition(matrix).add(rigidBody.getPosition());

/*                float x = this.rigidBody.getPosition().x;
                float y = this.rigidBody.getPosition().y;
                this.transformVertices[i] = new Vector2(cos * x - sin * x + x,
                        sin * y - cos * y + y);*/
            }
        }


        return this.transformVertices;
    }

    public Vector2[] getVertices() {
        if(this.transformUpdateRequired) {
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

    public RigidBody getRigidBody() {
        return rigidBody;
    }

    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
        this.rigidBody.setCollider(this);
    }

    @Override
    public AABB getAABB() {
        if(this.aabbUpdateRequired) {
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
