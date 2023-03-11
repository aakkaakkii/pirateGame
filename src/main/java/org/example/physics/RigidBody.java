package org.example.physics;

import org.example.components.Component;
import org.example.physics.primitives.Collider2D;
import org.example.physics2d.common.Vector2;

public class RigidBody extends Component {
    private Collider2D collider;
    private Vector2 position;
    private Vector2 linearVelocity = new Vector2();
    private float rotation = 0.0f;
    private float angularVelocity = 0.0f;

    private float mass = 0;
    private float inverseMass = 0;
    private float density = 0.0f;
    private float restitution = .5f;
    private float area;

    private BodyType bodyType = BodyType.Dynamic;

    //TODO: change to how it is in mario
    private Vector2 force = new Vector2();
    private Vector2 forceAccum = new Vector2();

    public void move(Vector2 amount) {
        this.position.add(amount);
        synchTransformsToGameObject();
        rbTransformComplete();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        synchTransformsToGameObject();
        rbTransformComplete();
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public void updatePhysic(float dt) {
        if(this.bodyType == BodyType.Static)
        {
            return;
        }
        Vector2 acceleration = new Vector2(forceAccum).mul(inverseMass);
        this.linearVelocity.add(acceleration);

        this.position.add(new Vector2(linearVelocity).mul(dt));
        this.rotation += this.angularVelocity * dt;
        synchTransformsToGameObject();
        clearAccumulators();
        rbTransformComplete();
    }

    public void rbTransformComplete() {
        if(this.collider!=null) {
            this.collider.rbTransformCallback();
        }
    }

    public void synchTransformsToGameObject() {
        if (this.gameObject != null) {
            this.gameObject.transform.position = new Vector2(position);
            this.gameObject.transform.rotation = this.rotation;
        }
    }

    public Collider2D getCollider() {
        return collider;
    }

    public void setCollider(Collider2D collider) {
        this.collider = collider;
    }

    public void clearAccumulators() {
        this.forceAccum.zero();
    }
    public void addForce(Vector2 force) {
        this.forceAccum.add(force);
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
//        this.inverseMass = 1/mass;
        if(bodyType == BodyType.Static) {
            this.inverseMass = 0;
        } else {
            this.inverseMass = 1 / mass;
        }
    }

    public float getRestitution() {
        return restitution;
    }

    public float getInverseMass() {
        return this.inverseMass;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    @Override
    public String toString() {
        return "RigidBody{" +
                ", position=" + position +
                ", linearVelocity=" + linearVelocity +
                ", rotation=" + rotation +
                ", angularVelocity=" + angularVelocity +
                ", mass=" + mass +
                ", inverseMass=" + inverseMass +
                ", density=" + density +
                ", restitution=" + restitution +
                ", area=" + area +
                ", bodyType=" + bodyType +
                ", force=" + force +
                '}';
    }
}
