package org.example.components.draw;

import org.example.physics.common.Vector2;

import java.awt.*;

public class LineDrawer extends Drawer  {
    private Color color;
    private Vector2 start;
    private Vector2 end;

    public LineDrawer(Vector2 start, Vector2 end) {
        this(start, end, Color.black);
    }

    public LineDrawer(Vector2 start, Vector2 end, Color color) {
        this.color = color;
        this.start = start;
        this.end = end;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
    }
}
