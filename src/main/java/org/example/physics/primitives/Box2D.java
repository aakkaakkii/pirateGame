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

    public Box2D() {
        this.halfSize = new Vector2(size).mul(0.5f);
//        this.vertices = createVertices();
//        this.transformVertices = new Vector2[vertices.length];
        transformUpdateRequired = true;
    }

    public Box2D(Vector2 size) {
        this.size = size;
        this.halfSize = new Vector2(size).mul(0.5f);
//        this.vertices = createVertices();
//        this.transformVertices = new Vector2[vertices.length];
        transformUpdateRequired = true;
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
        if(this.transformUpdateRequired) {
            float cos = (float) Math.cos(this.rigidBody.getRotation());
            float sin = (float) Math.sin(this.rigidBody.getRotation());

            for(int i = 0; i < this.vertices.length; i++) {
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

        return vertices;
    }

    public RigidBody getRigidBody() {
        return rigidBody;
    }

    public void setRigidBody(RigidBody rigidBody) {
        this.rigidBody = rigidBody;
        this.rigidBody.setCollider(this);
    }

}
