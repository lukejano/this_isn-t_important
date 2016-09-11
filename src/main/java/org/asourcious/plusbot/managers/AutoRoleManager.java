package org.asourcious.plusbot.managers;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.managers.GuildManager;
import org.asourcious.plusbot.Configuration;

import java.util.ArrayList;
import java.util.List;

public class AutoRoleManager {
    private Configuration configuration;

    public AutoRoleManager(Configuration configuration) {
        this.configuration = configuration;
    }

    public void handleMemberJoin(Guild guild, User user) {
        List<String> humanRoleIds = configuration.getAutoHumanRoles(guild);
        List<String> botRoleIds = configuration.getAutoBotRoles(guild);

        if (humanRoleIds.isEmpty() && !user.isBot())
            return;
        if (botRoleIds.isEmpty() && user.isBot())
            return;

        List<Role> humanRoles = compileRoleList(humanRoleIds, guild, user);
        List<Role> botRoles = compileRoleList(botRoleIds, guild, user);

        GuildManager guildManager = guild.getManager();
        if (user.isBot()) {
            guildManager.addRoleToUser(user, botRoles.toArray(new Role[botRoles.size()]));
        } else {
            guildManager.addRoleToUser(user, humanRoles.toArray(new Role[humanRoles.size()]));
        }
        guildManager.update();
    }

    private List<Role> compileRoleList(List<String> ids, Guild guild, User user) {
        List<Role> roles = new ArrayList<>();
        for (String id : ids) {
            if (guild.getRoleById(id) == null) {
                if (user.isBot()) {
                    configuration.removeAutoBotRole(guild, id);
                } else {
                    configuration.removeAutoHumanRole(guild, id);
                }
                continue;
            }
            roles.add(guild.getRoleById(id));
        }
        return roles;
    }
}
