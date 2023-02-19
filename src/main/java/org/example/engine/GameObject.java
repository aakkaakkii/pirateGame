package org.example.engine;

import org.example.components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private List<org.example.components.Component> components;
    private String name;
    public Transform transform;
    private boolean serializable = true;
    public int zIndex;

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
    }

    public <T extends org.example.components.Component> T getComponent(Class<T> componentClass) {
        for (org.example.components.Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }

        return null;
    }

    public List<org.example.components.Component> getAllComponents() {
        return components;
    }

    public void addComponent(org.example.components.Component c) {
        components.add(c);
        c.gameObject = this;
    }

    public <T extends org.example.components.Component> void removeComponent(Class<T> componentClass) {
        components.removeIf(c -> componentClass.isAssignableFrom(c.getClass()));
    }

    public void update(float dt) {
        for (Component component : components) {
            component.update(dt);
        }
    }


/*    public GameObject copy() {
        GameObject newGameObject = new GameObject("Generated", transform.copy(), this.zIndex);
        for (Component c : components) {
            Component copy = c.copy();
            if(copy !=null) {
                newGameObject.addComponent(copy);
            }
        }
        return newGameObject;
    }*/
}
