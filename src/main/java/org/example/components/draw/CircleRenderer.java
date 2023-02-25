package org.example.components.draw;

import org.example.components.draw.Drawer;

import java.awt.*;

public class CircleRenderer extends Drawer {
    private Color color;
    private float radius;

    public CircleRenderer(float radius) {
        this(radius, Color.black);
    }


    public CircleRenderer(float radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.drawOval((int) (this.gameObject.transform.position.x - radius),
                (int) (this.gameObject.transform.position.y - radius), (int) radius * 2, (int) radius * 2);
    }
}
