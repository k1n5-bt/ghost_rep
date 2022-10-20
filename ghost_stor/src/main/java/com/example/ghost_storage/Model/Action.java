package com.example.ghost_storage.Model;

public enum Action {
    New(100), Edit(200), Archive(300), Replace(400);

    private int value;

    Action(int value) { this.value = value; }

    public int getValue() { return value; }

    public static Action parse(int id) {
        Action action = null; // Default
        for (Action item : Action.values()) {
            if (item.getValue()==id) {
                action = item;
                break;
            }
        }
        return action;
    }
}
