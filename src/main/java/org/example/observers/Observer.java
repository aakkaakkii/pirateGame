package org.example.observers;

import org.example.engine.GameObject;
import org.example.observers.events.Event;

public interface Observer {
    void onNotify(GameObject object, Event event);
}
