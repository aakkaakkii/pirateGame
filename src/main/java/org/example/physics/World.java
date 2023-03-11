package org.example.physics;

import org.example.physics.forces.ForceRegistry;
import org.example.physics.forces.Gravity2D;
import org.example.physics2d.common.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {
    private ForceRegistry forceRegistry;
    private Gravity2D gravity;

    private List<RigidBody> bodyList;
    public static List<CollisionManifold> collisions = new ArrayList<>();
    public static List<Vector2> contactPoint = new ArrayList<Vector2>();

    public World() {
        this.forceRegistry = new ForceRegistry();
        this.gravity = new Gravity2D(new Vector2(0f, 2));
        this.bodyList = new ArrayList<>();
//        this.collisions = new ArrayList<>();
    }

    public void addRigidBody(RigidBody body) {
        bodyList.add(body);
        this.forceRegistry.add(body, gravity);
    }

    public void removeRigidBody(RigidBody body) {
        this.bodyList.remove(body);
    }

    public RigidBody getBody(int index) {
        if (index < 0 || index >= bodyList.size())
            return null;
        return bodyList.get(index);
    }

    float time =0;

    public void update(float dt) {
        collisions.clear();
        contactPoint.clear();

        //detect collision
        for (int i = 0; i < this.bodyList.size() - 1; i++) {
            RigidBody b1 = this.bodyList.get(i);
            for (int j = i + 1; j < this.bodyList.size(); j++) {
                RigidBody b2 = this.bodyList.get(j);
                CollisionManifold cm = Collisions
                        .findCollisionFeatures(b1.getCollider(), b2.getCollider());

                if (b1.getBodyType() == BodyType.Static && b2.getBodyType() == BodyType.Static) {
                    continue;
                }

                if(!Collisions.isCollidingAABB(b1.getCollider().getAABB(), b2.getCollider().getAABB())) {
                    continue;
                }

                if (cm != null && cm.isColliding()) {
                    if (b1.getBodyType() == BodyType.Static) {
                        b2.move(new Vector2(cm.getNormal()).mul(cm.getDepth()).mul(-1));

                    } else if (b2.getBodyType() == BodyType.Static) {
                        b1.move(new Vector2(cm.getNormal()).mul(cm.getDepth()));

                    } else {
                        b1.move(new Vector2(cm.getNormal()).mul(cm.getDepth() / 2f));
                        b2.move(new Vector2(cm.getNormal()).mul(cm.getDepth() / 2f).mul(-1));
                    }

                    cm.setB1(b1);
                    cm.setB2(b2);
                    Vector2[] contact = Collisions.findContactPoint(b1.getCollider(), b2.getCollider());
                    if(time > 0.3) {
//                        System.out.println(Arrays.toString(contact));
                        time = 0;
                    }
                    time+=dt;

                    if(contact != null) {
                        contactPoint.addAll(Arrays.asList(contact));
                    }


//                    cm.addContactPoint(contact);
                    collisions.add(cm);
                }
            }
        }


        //resolve collision
        for (CollisionManifold cm : collisions) {
            this.resolveCollision(cm.getB1(), cm.getB2(), cm.getNormal());

        }


        // Update the forces
        forceRegistry.updateForces(dt);

        //movement
        for (RigidBody body : bodyList) {
            body.updatePhysic(dt);
        }


        //collision
    }

    public void resolveCollision(RigidBody rb1, RigidBody rb2, Vector2 normal) {
        Vector2 relativeVelocity = new Vector2(rb2.getLinearVelocity()).sub(rb1.getLinearVelocity());

        if (relativeVelocity.dot(normal) < 0f) {
            return;
        }

        float e = Math.min(rb1.getRestitution(), rb2.getRestitution());
        float j = -(1f + e) * relativeVelocity.dot(normal);
        j /= rb1.getInverseMass() + rb2.getInverseMass();

        rb1.getLinearVelocity()
                .sub(new Vector2(normal).mul(j)
                        .mul(rb1.getInverseMass()));

        rb2.getLinearVelocity()
                .add(new Vector2(normal).mul(j)
                        .mul(rb2.getInverseMass()));

    }

}
