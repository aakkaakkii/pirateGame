package org.example.engine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ML extends MouseAdapter {
    public boolean mousePressed = false;
    public boolean mouseDragged = false;
    public int mouseButton = -1;
    public float x = -1.0f, y = -1.0f;
    public float dx = -1.0f, dy = -1.0f;
    public boolean pressing = false;

    @Override
    public void mousePressed(MouseEvent e) {
        this.mousePressed = true;
        this.mouseButton = e.getButton();
    }

    public boolean mouseClicked(int btn) {
        if(mouseButton == btn && mousePressed && !pressing) {
            pressing = true;
            return true;
        }

        return false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mousePressed=false;
        this.mouseDragged=false;
        this.pressing=false;
        this.dx = 0;
        this.dy = 0;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.x = e.getX();
        this.y = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseDragged=true;
        this.dx = e.getX() - this.x;
        this.dy = e.getY() - this.y;

    }

    public static class MouseKeys {
        public static final int LEFT_KEY = 1;
        public static final int RIGHT_KEY = 3;
    }
}
