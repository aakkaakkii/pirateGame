package org.example.engine;

import org.example.observers.EventSystem;
import org.example.observers.events.Event;
import org.example.observers.events.EventType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ML extends MouseAdapter {
    public boolean mousePressed = false;
    public boolean mouseDragged = false;
    public int mouseButton = -1;
    public float x = -1.0f, y = -1.0f;
    public float dx = -1.0f, dy = -1.0f;
    private boolean mouseButtonPressed[] = new boolean[9];

    @Override
    public void mousePressed(MouseEvent e) {
        this.mousePressed = true;
        this.mouseButton = e.getButton();
        this.mouseButtonPressed[this.mouseButton] = true;
        EventSystem.notify(null, new Event(EventType.MOUSE_CLICKED, e));
    }

    public boolean mouseClicked(int btn) {

        if (mouseButton == btn && mousePressed && !mouseButtonPressed[btn]) {

            mouseButtonPressed[btn] = true;
            return true;
        }

   /*     if(mouseButton == btn && mousePressed && !pressing) {
            pressing = true;
            return true;
        }*/

        return false;
    }

    public boolean isMousePressed(int btn) {
        return mouseButtonPressed[btn];
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mousePressed = false;
        this.mouseDragged = false;
        this.mouseButtonPressed[e.getButton()] = false;
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
        this.mouseDragged = true;
        this.dx = e.getX() - this.x;
        this.dy = e.getY() - this.y;

    }

    public static class MouseKeys {
        public static final int LEFT_KEY = 1;
        public static final int RIGHT_KEY = 3;
    }
}
