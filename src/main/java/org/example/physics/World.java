package org.example.physics;

import org.example.physics.primitives.Collider2D;
import org.example.physics2d.common.Vector2;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static final float MIN_BODY_SIZE = 0.1f * 0.1f;

    private List<RigidBody> bodyList;
    private Vector2 gravity;

    public World() {
        this.gravity = new Vector2(0f, 10);
        this.bodyList = new ArrayList<>();
    }

    public void addRigidBody(RigidBody body) {
        bodyList.add(body);
    }

    public void removeRigidBody(RigidBody body) {
        this.bodyList.remove(body);
    }

    public RigidBody getBody(int index) {
        if (index < 0 || index >= bodyList.size())
            return null;
        return bodyList.get(index);
    }

    public void update(float dt) {
        //movement
        for (RigidBody body : bodyList) {
            body.updatePhysic(dt);
        }

        for (int i = 0; i < this.bodyList.size() - 1; i++) {
            RigidBody b1 = this.bodyList.get(i);
            for (int j = i + 1; j < this.bodyList.size(); j++) {
                RigidBody b2 = this.bodyList.get(j);
                CollisionManifold cm = Collisions
                        .findCollisionFeatures(b1.getCollider(), b2.getCollider());

                if(b1.getBodyType() == BodyType.Static && b2.getBodyType() == BodyType.Static ) {
                    continue;
                }

                if (cm != null && cm.isColliding()) {
                    if(b1.getBodyType() == BodyType.Static) {
                        b2.move(new Vector2(cm.getNormal()).mul(cm.getDepth()).mul(-1));

                    } else if (b2.getBodyType() == BodyType.Static) {
                        b1.move(new Vector2(cm.getNormal()).mul(cm.getDepth() ));

                    } else {
                        b1.move(new Vector2(cm.getNormal()).mul(cm.getDepth() / 2f));
                        b2.move(new Vector2(cm.getNormal()).mul(cm.getDepth() / 2f).mul(-1));
                    }

                    this.resolveCollision(b1, b2, cm.getNormal());
                }

            }
            if(b1.gameObject.getName().equals("player")) {
//                System.out.println(b1);
            }
        }


        //collision
    }

    public void resolveCollision(RigidBody rb1, RigidBody rb2, Vector2 normal) {
        Vector2 relativeVelocity = new Vector2(rb2.getLinearVelocity()).sub(rb1.getLinearVelocity());

    /*    if(relativeVelocity.dot(normal) > 0f) {
            return;
        }
*/
        float e = Math.min(rb1.getRestitution(), rb2.getRestitution());
        float j = -(1f + e) * relativeVelocity.dot(normal);
        j /= rb1.getInverseMass() + rb2.getInverseMass();

        rb1.getLinearVelocity()
                .sub(new Vector2(normal).mul(j)
                        .mul(rb1.getInverseMass()));

        rb2.getLinearVelocity()
                .add(new Vector2(normal).mul(j)
                        .mul(rb1.getInverseMass()));

    }

}
