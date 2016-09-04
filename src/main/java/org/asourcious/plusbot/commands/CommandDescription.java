package org.asourcious.plusbot.commands;

public class CommandDescription {

    private final String name;
    private final String description;
    private final String example;
    private final Argument[] arguments;
    private final PermissionLevel requiredPermissions;

    public CommandDescription(String name, String description, String example, Argument[] arguments, PermissionLevel requiredPermissions) {
        this.name = name;
        this.description = description;
        this.example = example;
        this.arguments = arguments;
        this.requiredPermissions = requiredPermissions;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExample() {
        return example;
    }

    public Argument[] getArguments() {
        return arguments;
    }

    public PermissionLevel getRequiredPermissions() {
        return requiredPermissions;
    }
}
