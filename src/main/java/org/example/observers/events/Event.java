package org.example.observers.events;

public class Event {
    public EventType type;
    public Object payload;

    public Event(EventType type) {
        this.type = type;
    }

    public Event(EventType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    // q: what does SOLID stand for in OO programing?


}
