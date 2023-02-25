package org.example.engine;

import org.example.components.Component;
import org.example.components.draw.Drawer;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private List<Component> components;
    private String name;
    public Transform transform;
    public int zIndex;
    private Drawer drawer;
    private boolean isDebugEnabled = false;

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
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

    public List<Drawer> getDrawers() {
        List<Drawer> drawers = new ArrayList<>();
        for (Component c : components) {
            if(Drawer.class.isAssignableFrom(c.getClass())) {
                drawers.add((Drawer) c);
            }
        }
        return drawers;
    }

    public List<Component> getAllComponents() {
        return components;
    }

    public void addComponent(Component c) {
        components.add(c);
        c.gameObject = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.removeIf(c -> componentClass.isAssignableFrom(c.getClass()));
    }

    public void update(float dt) {
        for (Component component : components) {
            component.update(dt);
        }
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        isDebugEnabled = debugEnabled;
    }

    public boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    public String getName() {
        return name;
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
