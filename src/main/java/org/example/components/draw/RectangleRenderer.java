package org.example.components.draw;

import java.awt.*;

public class RectangleRenderer extends Drawer {
    private Color color;
    private float height;
    private float width;

    public RectangleRenderer(float width, float height) {
        this(width, height, Color.black);
    }

    public RectangleRenderer(float width, float height, Color color) {
        this.color = color;
        this.height = height;
        this.width = width;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        Graphics2D oldGraphics = (Graphics2D) g2.create();

        oldGraphics.translate((this.gameObject.transform.position.x - width / 2), (this.gameObject.transform.position.y - height / 2));
        oldGraphics.rotate(Math.toRadians(this.gameObject.transform.rotation), width / 2.0, height / 2.0);
        oldGraphics.drawRect(0, 0, (int) width, (int) height);
/*
        oldGraphics.drawRect((int) (this.gameObject.transform.position.x - width / 2),
                (int) (this.gameObject.transform.position.y - height / 2),
                (int) width, (int) height);*/
        oldGraphics.dispose();
    }
}
