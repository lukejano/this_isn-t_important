package org.asourcious.plusbot.commands;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandRegistry {
    private CommandRegistry() {}

    private static Map<String, CommandEntry> registry = new ConcurrentHashMap<>();
    private static Map<String, String> aliasMap = new ConcurrentHashMap<>();

    public static void registerCommand(String name, Command command) {
        registry.put(name.toLowerCase(), new CommandEntry(command));
    }

    public static void registerAlias(String command, String alias) {
        aliasMap.put(alias.toLowerCase(), command.toLowerCase());
        registry.get(command.toLowerCase()).addAlias(alias);
    }

    public static boolean hasCommand(String name) {
        return registry.containsKey(name.toLowerCase()) || aliasMap.containsKey(name.toLowerCase());
    }

    public static CommandEntry getCommand(String name) {
        return registry.containsKey(name) ? registry.get(name.toLowerCase()) : registry.get(aliasMap.get(name.toLowerCase()));
    }

    public static List<CommandEntry> getRegisteredCommands() {
        return Collections.unmodifiableList(new ArrayList<>(registry.values()));
    }

    public static class CommandEntry {

        private Command command;
        private List<String> aliases;

        private CommandEntry(Command command) {
            aliases = new ArrayList<>();
            this.command = command;
        }

        public String getName() {
            return command.getDescription().getName();
        }

        public String getArgs() {
            return command.getDescription().getArguments() == null ? "None" : Arrays.toString(command.getDescription().getArguments());
        }

        private void addAlias(String alias) {
            aliases.add(alias);
        }

        public List<String> getAliases() {
            return Collections.unmodifiableList(new ArrayList<>(aliases));
        }

        public Command getCommand() {
            return command;
        }
    }
}
