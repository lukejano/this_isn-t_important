package org.asourcious.plusbot.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandRegistry {
    private CommandRegistry() {}

    private static HashMap<String, CommandEntry> registry = new HashMap<>();

    public static void registerCommand(String name, Command command) {
        registry.put(name.toLowerCase(), new CommandEntry(command));
    }

    public static void registerAlias(String command, String alias) {
        registry.put(alias.toLowerCase(), registry.get(command.toLowerCase()));
    }

    public static boolean hasCommand(String name) {
        return registry.containsKey(name.toLowerCase());
    }

    public static CommandEntry getCommand(String name) {
        return registry.get(name.toLowerCase());
    }

    public static List<CommandEntry> getRegisteredCommands() {
        return registry.keySet().parallelStream().map(key -> registry.get(key)).collect(Collectors.toList());
    }

    public static class CommandEntry {

        private Command command;

        private CommandEntry(Command command) {
            this.command = command;
        }

        public String getName() {
            return command.getDescription().getName();
        }

        public String getArgs() {
            return command.getDescription().getArguments() == null ? "None" : Arrays.toString(command.getDescription().getArguments());
        }

        public Command getCommand() {
            return command;
        }
    }
}
