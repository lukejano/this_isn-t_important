package org.asourcious.plusbot.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandRegistry {
    private CommandRegistry() {}

    private static Map<String, CommandEntry> registry = new ConcurrentHashMap<>();

    public static void registerCommand(String name, Command command) {
        registry.put(name.toLowerCase(), new CommandEntry(command, false));
    }

    public static void registerAlias(String command, String alias) {
        registry.put(alias.toLowerCase(), new CommandEntry(registry.get(command.toLowerCase()).getCommand(), true));
    }

    public static boolean hasCommand(String name) {
        return registry.containsKey(name.toLowerCase());
    }

    public static CommandEntry getCommand(String name) {
        return registry.get(name.toLowerCase());
    }

    public static List<CommandEntry> getRegisteredCommands() {
        return Collections.unmodifiableList(new ArrayList<>(registry.values()));
    }

    public static class CommandEntry {

        private Command command;
        private boolean isAlias;

        private CommandEntry(Command command, boolean isAlias) {
            this.isAlias = isAlias;
            this.command = command;
        }

        public String getName() {
            return command.getDescription().getName();
        }

        public String getArgs() {
            return command.getDescription().getArguments() == null ? "None" : Arrays.toString(command.getDescription().getArguments());
        }

        public boolean isAlias() {
            return isAlias;
        }

        public Command getCommand() {
            return command;
        }
    }
}
