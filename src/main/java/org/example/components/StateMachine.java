package org.example.components;

import org.example.components.draw.SpriteRenderer;
import org.example.engine.AnimationState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StateMachine extends Component {

    private class StateTrigger {
        public String state;
        public String trigger;

        public StateTrigger() {}

        public StateTrigger(String state, String trigger) {
            this.state = state;
            this.trigger = trigger;
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, trigger);
        }

        @Override
        public boolean equals(Object obj) {
            if(obj.getClass() != StateTrigger.class) return false;
            StateTrigger t2 = (StateTrigger) obj;
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }
    }

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void setDefaultState(String animationTitle) {
        for (AnimationState state : states) {
            if(state.title.equals(animationTitle)) {
                defaultStateTitle = animationTitle;
                if(currentState == null) {
                    currentState = state;
                }
                return;
            }
        }
        System.out.println("Unable to find default state '" + animationTitle + "'");
    }

    public void addState(String from, String to, String onTrigger) {
        this.stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void addState(AnimationState state) {
        this.states.add(state);
    }

    public void trigger(String trigger) {
        for (StateTrigger stateTrigger : stateTransfers.keySet()) {
            if(stateTrigger.state.equals(currentState.title) && stateTrigger.trigger.equals(trigger)) {
                if(stateTransfers.get(stateTrigger) != null) {
                    int newStateIndex = stateIndexOf(stateTransfers.get(stateTrigger));
                    if (newStateIndex > -1) {
                        currentState = states.get(newStateIndex);
                    }
                }
                return;
            }
        }
    }

    private int stateIndexOf(String stateTitle) {
        int index = 0;
        for (AnimationState state : states) {
            if (state.title.equals(stateTitle)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    @Override
    public void start() {
        for (AnimationState state : states) {
            if (state.title.equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (currentState != null) {
            currentState.update(dt);
            SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
            if(spr != null) {
                spr.setSprite(currentState.getCurrentSprite());
            }


            if(currentState.isComplete()) {
                for (AnimationState state : states) {
                    if(state.title == defaultStateTitle) {
                        currentState = state;
                        return;
                    }
                }

            }
        }
    }

    public AnimationState getCurrentState() {
        return currentState;
    }
}
