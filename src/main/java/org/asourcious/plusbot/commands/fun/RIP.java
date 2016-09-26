package org.asourcious.plusbot.commands.fun;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import org.asourcious.plusbot.PlusBot;
import org.asourcious.plusbot.commands.Argument;
import org.asourcious.plusbot.commands.Command;
import org.asourcious.plusbot.commands.CommandDescription;
import org.asourcious.plusbot.commands.PermissionLevel;
import org.asourcious.plusbot.utils.CommandUtils;
import org.asourcious.plusbot.utils.FormatUtils;
import org.asourcious.plusbot.utils.MiscUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RIP implements Command {

    private CommandDescription description = new CommandDescription(
            "RIP",
            "Generates an image of a tombstone with the specified text or user on it.",
            "rip me, rip @person",
            new Argument[] { new Argument("Text", false) },
            PermissionLevel.EVERYONE
    );

    @Override
    public String checkArgs(String[] args) {
        if (args.length > 1)
            return "The RIP command takes up to 1 argument";

        return null;
    }

    @Override
    public void execute(PlusBot plusBot, String[] args, TextChannel channel, Message message) {
        List<User> mentionedUsers = new ArrayList<>(message.getMentionedUsers());
        String selfMention = message.getJDA().getSelfInfo().getAsMention();
        if (CommandUtils.getPrefixForMessage(plusBot, message).equals(selfMention)
                && !message.getRawContent().replaceFirst(selfMention, "").contains(selfMention))
            mentionedUsers.remove(message.getJDA().getSelfInfo());

        if (args.length == 0 && mentionedUsers.isEmpty()) {
            channel.sendMessageAsync(FormatUtils.error("If no arguments are supplied, you must mention a user!"), null);
            return;
        }

        if (mentionedUsers.size() > 1) {
            channel.sendMessageAsync("You can only mention one user!", null);
            return;
        }

        String text = args.length == 0
                ? channel.getGuild().getEffectiveNameForUser(mentionedUsers.get(0))
                : args[0];

        try {
            BufferedImage image = ImageIO.read(new File("media/tombstone.png"));

            Graphics2D g2d = image.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(scaleFont(text, new Rectangle(70, 460, 450, 100), g2d));
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, 70, 470);

            File file = new File("media/tempGrave.png");
            for (int i = 0; !file.createNewFile(); i++) {
                file = new File("media/tempGrave" + i + ".png");
                if (i > 1000)
                    PlusBot.LOG.warn("More than 1000 tmp grave images!");
            }

            try {
                BufferedImage avatar = null;
                if (!mentionedUsers.isEmpty()) {
                    InputStream stream = MiscUtils.getDataStream(channel.getJDA(), mentionedUsers.get(0).getAvatarUrl());
                    if(stream != null) {
                        avatar = ImageIO.read(stream);
                    }

                    g2d.drawImage(avatar, 225, 510, 125, 125, Color.white, null);
                }
            } catch (IOException ex) {
                PlusBot.LOG.warn(mentionedUsers.get(0).getAvatarUrl());
                PlusBot.LOG.log(ex);
            }

            ImageIO.write(image, "png", file);

            final File toDelete = file;
            channel.sendFileAsync(file, null, msg -> toDelete.delete());
        } catch (IOException e) {
            channel.sendMessageAsync(FormatUtils.error("Error generating tombstone!"), null);
        }
    }

    @Override
    public CommandDescription getDescription() {
        return description;
    }

    private Font scaleFont(String text, Rectangle rect, Graphics g) {
        float fontSize = 10.0f;

        Font font = g.getFont().deriveFont(fontSize);
        int width = g.getFontMetrics(font).stringWidth(text);
        fontSize = (rect.width / width ) * fontSize;
        font = g.getFont().deriveFont(fontSize);
        if (font.getSize() > 160)
            font = font.deriveFont(160.0f);
        return font;
    }
}
