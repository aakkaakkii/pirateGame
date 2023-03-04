package org.example.physics2d.forces;


import org.example.physics.RigidBody;

public class ForceRegistration {
    public ForceGenerator fg;
    public RigidBody rb;

    public ForceRegistration(ForceGenerator fg, RigidBody rb) {
        this.fg = fg;
        this.rb = rb;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) return false;
        if(other.getClass() != ForceRegistration.class) return false;

        ForceRegistration fr = (ForceRegistration) other;
        return fr.rb == this.rb && fr.fg == this.fg;
    }
}
