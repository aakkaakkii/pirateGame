package org.example.physics.forces;

import org.example.physics.RigidBody;

import java.util.Objects;

public class ForceRegistration {
    public ForceGenerator fg;
    public RigidBody rb;

    public ForceRegistration(ForceGenerator fg, RigidBody rb) {
        this.fg = fg;
        this.rb = rb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForceRegistration that = (ForceRegistration) o;
        return that.rb == this.rb && that.fg == this.fg;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fg, rb);
    }
}
