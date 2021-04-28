package me.bryangaming.chatlab.managers;

import me.bryangaming.chatlab.PluginService;
import me.bryangaming.chatlab.managers.sound.SoundEnum;
import me.bryangaming.chatlab.managers.sound.SoundManager;
import me.bryangaming.chatlab.utils.Configuration;
import me.bryangaming.chatlab.utils.string.TextUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Logger;

public class SenderManager {

    private final PluginService pluginService;

    private final Configuration config;
    private final Configuration sounds;

    public SenderManager(PluginService pluginService) {
        this.pluginService = pluginService;
        this.config = pluginService.getFiles().getConfigFile();
        this.sounds = pluginService.getFiles().getSoundsFile();
    }


    public boolean hasPermission(Player player, String path) {

        Logger logger = pluginService.getPlugin().getLogger();
        String permission = config.getString("perms." + path);

        if (permission == null) {

            if (config.getString("perms." + StringUtils.remove(path, ".main")) == null) {

                if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                    logger.info("Please change the configuration section! Your config is old.");

                } else {
                    logger.info("Error - Could not find the path in config.");
                    logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");

                }
            } else {
                permission = config.getString("perms." + StringUtils.remove(path, ".main"));
            }

        } else {
            if (permission.equalsIgnoreCase("none")) {
                return true;
            }
        }


        try {
            return player.hasPermission(permission);

        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);

        }
        return false;
    }


    public void sendMessage(Player sender, String path) {

        Logger logger = pluginService.getPlugin().getLogger();
        if (path == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");
            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");
            }
        }


        Audience player = pluginService.getPlugin().getBukkitAudiences().player(sender);

        try {
            player.sendMessage(TextUtils.convertTextToComponent(sender, path));
        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);
        }
    }

    public void sendMessageTo(List<Player> playerList, String path) {

        Logger logger = pluginService.getPlugin().getLogger();
        if (path == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");
            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");

            }
        }

        for (Player online : playerList) {
            Audience player = pluginService.getPlugin().getBukkitAudiences().player(online);

            try {
                player.sendMessage(TextUtils.convertTextToComponent(online, path));
            } catch (NullPointerException nullPointerException) {
                sendLines(nullPointerException);
                return;
            }
        }
    }

    public void sendMessage(Player sender, String path, String message) {
        Logger logger = pluginService.getPlugin().getLogger();
        if (path == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");
            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");

            }
        }

        Audience player = pluginService.getPlugin().getBukkitAudiences().player(sender);
        try {
            player.sendMessage(TextUtils.convertTextToComponent(sender, path, message));
        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);
        }
    }

    public void sendMessage(Player sender, List<String> messages) {
        Logger logger = pluginService.getPlugin().getLogger();
        if (messages == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");
            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");

            }
        }

        Audience player = pluginService.getPlugin().getBukkitAudiences().player(sender);
        try {
            messages
                    .replaceAll(message -> TextUtils.convertText(sender, message));

        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);
            return;
        }

        for (String message : messages) {
            player.sendMessage(MiniMessage.get().parse(message));
        }
    }


    public void playSound(Player player, SoundEnum soundType) {

        String path = soundType.getName();

        Configuration soundsFile = pluginService.getFiles().getSoundsFile();
        SoundManager soundManager = pluginService.getPlayerManager().getSoundManager();

        Logger logger = pluginService.getPlugin().getLogger();
        String soundPath = soundsFile.getString("sounds." + path);

        if (soundPath == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");

            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");
            }
        }

        try {
            soundManager.setSound(player.getUniqueId(), "sounds." + path);

        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);
        }
    }

    public SenderManager playSound(Player player, SoundEnum soundType, String command) {

        SoundManager sound = pluginService.getPlayerManager().getSoundManager();

        if (soundType != SoundEnum.ARGUMENT) {
            return this;
        }

        if (!sounds.getBoolean("sounds.argument")) {
            return this;
        }

        for (String keys : sounds.getConfigurationSection("group").getKeys(false)) {
            for (String newCommand : sounds.getStringList("group." + keys + ".commands")) {
                if (command.equalsIgnoreCase(newCommand.trim().replace(",", " "))) {
                    sound.setSound(player.getUniqueId(), "group." + keys);
                    return this;
                }
            }
        }

        if (sounds.getConfigurationSection("argument." + command) == null) {
            return this;
        }

        sound.setSound(player.getUniqueId(), "argument." + command);
        return this;
    }

    public void sendMessageLater(Player sender, int time, String command) {

        RunnableManager runnableManager = pluginService.getPlayerManager().getRunnableManager();

        Logger logger = pluginService.getPlugin().getLogger();

        if (command == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");

            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");
            }
        }

        try {
            runnableManager.waitSecond(sender, time, command);

        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);
            return;
        }

        runnableManager.waitSecond(sender, time, command);
    }


    public void openInventory(HumanEntity sender, String guiName) {
        GuiManager guiManager = pluginService.getPlayerManager().getGuiManager();
        guiManager.openInventory(sender.getUniqueId(), guiName, 0);
    }
    public void openInventory(HumanEntity sender, String guiName, int page) {
        GuiManager guiManager = pluginService.getPlayerManager().getGuiManager();
        guiManager.openInventory(sender.getUniqueId(), guiName, page);
    }

    public void openInventory(Player sender, String guiName, int page) {

        GuiManager guiManager = pluginService.getPlayerManager().getGuiManager();
        guiManager.openInventory(sender.getUniqueId(), guiName, page);
    }

    public void openInventory(Player sender, String guiName) {

        GuiManager guiManager = pluginService.getPlayerManager().getGuiManager();
        guiManager.openInventory(sender.getUniqueId(), guiName, 0);
    }

    public void sendMessagesLater(Player sender, int time, String... messages) {

        RunnableManager runnableManager = pluginService.getPlayerManager().getRunnableManager();

        Logger logger = pluginService.getPlugin().getLogger();

        if (messages == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");

            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");
            }
        }

        try {
            runnableManager.waitSecond(sender, time, messages);

        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);
            return;
        }

        runnableManager.waitSecond(sender, time, messages);
    }


    public void sendCommand(CommandSender sender, String command) {

        RunnableManager runnableManager = pluginService.getPlayerManager().getRunnableManager();

        Logger logger = pluginService.getPlugin().getLogger();

        if (command == null) {
            if (!config.getString("version", "1.0").equalsIgnoreCase(pluginService.getPlugin().getDescription().getVersion())) {
                logger.info("Please change the configuration section! Your config is old.");

            } else {
                logger.info("Error - Could not find the path in config.");
                logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");
            }
        }

        try {
            runnableManager.sendCommand(sender, command);

        } catch (NullPointerException nullPointerException) {
            sendLines(nullPointerException);
            return;
        }

        runnableManager.sendCommand(sender, command);
    }


    private void sendLines(NullPointerException nullPointerException) {
        Logger logger = pluginService.getPlugin().getLogger();

        logger.info("Code line:");
        for (StackTraceElement stackTraceElement : nullPointerException.getStackTrace()) {

            String errorLine = stackTraceElement.toString();

            if (errorLine.contains("java.")) {
                break;
            }

            if (!errorLine.contains("code.")) {
                continue;
            }

            logger.info("- " + stackTraceElement.toString());

        }
    }
}
