package com.example.ghost_storage.Model;

public enum State {
    ACTIVE(100), CANCELED(200), REPLACED(300);

    private int value;

    State(int value) { this.value = value; }

    public int getValue() { return value; }

    public static State parse(int id) {
        State state = null; // Default
        for (State item : State.values()) {
            if (item.getValue()==id) {
                state = item;
                break;
            }
        }
        return state;
    }
};
