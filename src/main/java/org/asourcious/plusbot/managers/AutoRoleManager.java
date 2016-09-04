package org.asourcious.plusbot.managers;

import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.managers.GuildManager;
import net.dv8tion.jda.utils.PermissionUtil;
import org.asourcious.plusbot.PlusBot;

import java.util.ArrayList;
import java.util.List;

public class AutoRoleManager {
    private PlusBot plusBot;

    public AutoRoleManager(PlusBot plusBot) {
        this.plusBot = plusBot;
    }

    public void handleMemberJoin(GuildMemberJoinEvent event) {
        List<String> humanRoleIds = plusBot.getConfiguration().getAutoHumanRoles(event.getGuild());
        List<String> botRoleIds = plusBot.getConfiguration().getAutoBotRoles(event.getGuild());

        if (humanRoleIds.isEmpty() && !event.getUser().isBot())
            return;
        if (botRoleIds.isEmpty() && event.getUser().isBot())
            return;

        List<Role> humanRoles = compileRoleList(humanRoleIds, event);
        List<Role> botRoles = compileRoleList(botRoleIds, event);

        GuildManager guildManager = event.getGuild().getManager();
        if (event.getUser().isBot()) {
            guildManager.addRoleToUser(event.getUser(), botRoles.toArray(new Role[botRoles.size()]));
        } else {
            guildManager.addRoleToUser(event.getUser(), humanRoles.toArray(new Role[humanRoles.size()]));
        }
        guildManager.update();
    }

    private List<Role> compileRoleList(List<String> ids, GuildMemberJoinEvent event) {
        List<Role> roles = new ArrayList<>();
        for (String id : ids) {
            if (event.getGuild().getRoleById(id) == null) {
                TextChannel channel = event.getGuild().getPublicChannel();
                if (PermissionUtil.canTalk(channel)) {
                    channel.sendMessageAsync("Error retrieving role of ID " + id + ", removing from auto roles.", null);
                }
                if (event.getUser().isBot()) {
                    plusBot.getConfiguration().removeAutoBotRole(event.getGuild(), id);
                } else {
                    plusBot.getConfiguration().removeAutoHumanRole(event.getGuild(), id);
                }
                continue;
            }
            roles.add(event.getGuild().getRoleById(id));
        }
        return roles;
    }
}
