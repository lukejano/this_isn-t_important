package org.asourcious.plusbot.commands;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.utils.PermissionUtil;
import org.asourcious.plusbot.PlusBot;

public enum PermissionLevel {

    EVERYONE(0),
    SERVER_MODERATOR(1),
    SERVER_OWNER(2),
    OWNER(3);

    private final int value;

    PermissionLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PermissionLevel getPermissionLevel(User user, Guild guild) {
        if (user.getId().equals(PlusBot.OWNER_ID))
            return PermissionLevel.OWNER;
        if (guild.getOwner().equals(user))
            return PermissionLevel.SERVER_OWNER;
        if (PermissionUtil.checkPermission(guild, user, Permission.MANAGE_SERVER))
            return PermissionLevel.SERVER_MODERATOR;

        return PermissionLevel.EVERYONE;
    }

    @Override
    public String toString() {
        switch (this) {
            case EVERYONE:
                return "Everyone";
            case SERVER_MODERATOR:
                return "Server Moderator";
            case SERVER_OWNER:
                return "Server Owner";
            case OWNER:
                return "Owner";
            default:
                return "UNKNOWN";
        }
    }
}
