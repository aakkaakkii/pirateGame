package org.example.physics;

import org.example.physics.common.Vector2;
import org.example.physics.forces.ForceRegistry;
import org.example.physics.forces.Gravity2D;
import org.example.physics.primitives.*;
import org.example.utils.JMath;

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
        this.gravity = new Gravity2D(new Vector2(0f, 6));
        this.bodyList = new ArrayList<>();
//        this.collisions = new ArrayList<>();
    }

    public void addRigidBody(RigidBody body) {
        bodyList.add(body);
        this.forceRegistry.add(body, gravity);
    }

    public void addRigidBody(RigidBody body, boolean hasGravity) {
        bodyList.add(body);
        if (hasGravity) {
            this.forceRegistry.add(body, gravity);
        }
    }

    public void removeRigidBody(RigidBody body) {
        this.bodyList.remove(body);
    }

    public RigidBody getBody(int index) {
        if (index < 0 || index >= bodyList.size())
            return null;
        return bodyList.get(index);
    }

    float time = 0;

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

                if (!b1.getCollider().getAABB().overlap(b2.getCollider().getAABB())) {
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

                    if (contact != null) {
                        cm.addAllContactPoint(contact);
                        contactPoint.addAll(Arrays.asList(contact));
                    }
                    collisions.add(cm);
                }
            }
        }


        //resolve collision
        for (CollisionManifold cm : collisions) {
            this.resolveCollision(cm.getB1(), cm.getB2(), cm.getNormal());
//            this.resolveCollisionWithFriction(cm, dt);

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

        float e = Math.max(rb1.getRestitution(), rb2.getRestitution());
        float j = -(1f + e) * relativeVelocity.dot(normal);
        j /= rb1.getInverseMass() + rb2.getInverseMass();

        rb1.getLinearVelocity()
                .sub(new Vector2(normal).mul(j)
                        .mul(rb1.getInverseMass()));

        rb2.getLinearVelocity()
                .add(new Vector2(normal).mul(j)
                        .mul(rb2.getInverseMass()));

    }

    public void resolveCollisionWithFriction(CollisionManifold cm, float dt) {
        RigidBody rb1 = cm.getB1();
        RigidBody rb2 = cm.getB2();
        Vector2 normal = cm.getNormal();
        Vector2 relativeVelocity = new Vector2(rb2.getLinearVelocity()).sub(rb1.getLinearVelocity());

        if (relativeVelocity.dot(normal) < 0f) {
            return;
        }

        Vector2 tangent = new Vector2(relativeVelocity).sub(
                new Vector2(normal).mul(relativeVelocity.dot(normal)));
        tangent.normalize();


        float e = Math.max(rb1.getRestitution(), rb2.getRestitution());
        float j = -(1f + e) * relativeVelocity.dot(normal);
        j /= rb1.getInverseMass() + rb2.getInverseMass();

        float sf = (rb1.getStaticFriction() + rb2.getStaticFriction()) * 0.5f;
        float df = (rb1.getDynamicFriction() + rb2.getDynamicFriction()) * 0.5f;

        Vector2 impulse = new Vector2(normal).mul(j);

        if (!JMath.compere(tangent, new Vector2())) {
            impulse.add(new Vector2(tangent).mul(-1).mul(df));
        }

        rb1.getLinearVelocity()
                .sub(new Vector2(impulse)
                        .mul(rb1.getInverseMass()));
        if (rb1.getLinearVelocity().x < .5 && rb1.getLinearVelocity().x > -.5)
            rb1.getLinearVelocity().x = 0;
        if (rb1.getLinearVelocity().y < .5 && rb1.getLinearVelocity().y > -.5)
            rb1.getLinearVelocity().y = 0;

        rb2.getLinearVelocity()
                .add(new Vector2(impulse)
                        .mul(rb2.getInverseMass()));
        if (rb2.getLinearVelocity().x < .5 && rb2.getLinearVelocity().x > -.5)
            rb2.getLinearVelocity().x = 0;
        if (rb2.getLinearVelocity().y < .5 && rb2.getLinearVelocity().y > -.5)
            rb2.getLinearVelocity().y = 0;

    }


    public void resolveCollisionWithRotation(CollisionManifold cm) {
        RigidBody rb1 = cm.getB1();
        RigidBody rb2 = cm.getB2();
        Vector2 normal = cm.getNormal();
        Vector2[] impulseList = {new Vector2(), new Vector2()};
        Vector2[] raList = {new Vector2(), new Vector2()};
        Vector2[] rbList = {new Vector2(), new Vector2()};

        float e = Math.max(rb1.getRestitution(), rb2.getRestitution());


        for (int i = 0; i < cm.getContactPoint().size(); i++) {
            Vector2 v = cm.getContactPoint().get(i);
            Vector2 ra = new Vector2(v).sub(rb1.getPosition());
            Vector2 rb = new Vector2(v).sub(rb2.getPosition());

            raList[i] = ra;
            rbList[i] = rb;

            Vector2 raPer = new Vector2(-ra.y, ra.x);
            Vector2 rbPer = new Vector2(-rb.y, rb.x);

            Vector2 angularLinearVelocityA = new Vector2(raPer).mul(rb1.getLinearVelocity());
            Vector2 angularLinearVelocityB = new Vector2(rbPer).mul(rb2.getLinearVelocity());

            Vector2 relativeVelocity = new Vector2(rb2.getLinearVelocity()).add(angularLinearVelocityB)
                    .sub(rb1.getLinearVelocity()).sub(angularLinearVelocityA);

            float contactVelocityMag = relativeVelocity.dot(normal);

            if (contactVelocityMag < 0f) {
                continue;
            }

            float raPerpDotN = raPer.dot(normal);
            float rbPerpDotN = rbPer.dot(normal);
            float denom = rb1.getInverseMass() + rb2.getInverseMass()
                    + (raPerpDotN * raPerpDotN) * rb1.getInvInertia()
                    + (rbPerpDotN * rbPerpDotN) * rb2.getInvInertia();

            float j = -(1f + e) * contactVelocityMag;
            j /= denom;
            j /= cm.getContactPoint().size();

            impulseList[i] = new Vector2(normal).mul(j);
        }

        for (int i = 0; i < impulseList.length; i++) {
            Vector2 impulse = impulseList[i];

            Vector2 ra = raList[i];
            Vector2 rb = rbList[i];

            rb1.getLinearVelocity()
                    .sub(new Vector2(impulse)
                            .mul(rb1.getInverseMass()));
            rb1.setAngularVelocity(rb1.getAngularVelocity() - ra.cross(impulse) * rb1.getInvInertia());

            rb2.getLinearVelocity()
                    .add(new Vector2(impulse)
                            .mul(rb2.getInverseMass()));
            rb2.setAngularVelocity(rb2.getAngularVelocity() + rb.cross(impulse) * rb2.getInvInertia());

        }

    }

    public void raycast(RaycastCallback rayCastCallback, Vector2 p1, Vector2 p2) {
        Vector2 min = new Vector2(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
        Vector2 max =new Vector2(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));

        AABB rayAABB = new AABB(min, max);
        List<Collider2D> potentialColliders = new ArrayList<>();

        for (RigidBody rb : this.bodyList) {
            if(rayAABB.overlap(rb.getCollider().getAABB())) {
                potentialColliders.add(rb.getCollider());
            }
        }


        potentialColliders.sort((b1, b2) -> {
            float firstLengthSquared = p1.lengthSquared() - b1.getRigidBody().getPosition().lengthSquared();
            float secondLengthSquared = p1.lengthSquared() - b2.getRigidBody().getPosition().lengthSquared();
            return (int) (firstLengthSquared - secondLengthSquared) * (-1);
        });

        RayCastInput input = new RayCastInput(p1, p2);
        RaycastResult result = new RaycastResult();
        for (Collider2D potentialCollider : potentialColliders) {
            if(potentialCollider.raycast(input, result)) {
                result.setCollider2D(potentialCollider);
                if(rayCastCallback.reportHit(input, result)) {
                    break;
                }
            }

        }
    }

}
