package org.asourcious.plusbot.commands;

public class Argument {
    private String name;
    private boolean isRequired;

    public Argument(String name, boolean isRequired) {
        this.name = name;
        this.isRequired = isRequired;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + (isRequired ? "" : " (Optional)");
    }
}
