package code.methods.player;

import code.Manager;
import code.bukkitutils.RunnableManager;
import code.utils.StringFormat;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PlayerMessage{

    private final Manager manager;
    private int pathLength = 45;

    public PlayerMessage(Manager manager){
        this.manager = manager;
    }

    public void sendMessage(CommandSender sender, String path, String message) {

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            path = getPlaceholders(sender, path);
        }

        sender.spigot().sendMessage(getMessage(path, message));
    }


    public void sendMessage(Player player, String path) {

        Logger logger = manager.getPlugin().getLogger();
        if (path == null){
            logger.info("Error - The path that should send you is null.");
            logger.info("Please copy the lines and post in: https://discord.gg/wpSh4Bf4Es");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try{
                path = getPlaceholders(player, path);
            } catch (NullPointerException nullPointerException){
                sendLines(nullPointerException);
                return;
            }
        }

        try{
            player.spigot().sendMessage(getMessage(path));
        } catch (NullPointerException nullPointerException){
            sendLines(nullPointerException);
        }
    }

    public BaseComponent[] getMessage(String message) {
        message = manager.getStringFormat().replaceString(message);
        
        return PlayerStatic.convertText(PlayerStatic.setColor(message));
    }


    public BaseComponent[] getMessage(String path, String message) {
        message = manager.getStringFormat().replaceString(message);

        return PlayerStatic.convertText(PlayerStatic.setColor(path)
                .replace("%message%", message));
    }

    private String getPlaceholders(CommandSender sender, String path){

        Player player = (Player) sender;
        return PlayerStatic.setVariables(player, path);
    }

    public void sendLines(NullPointerException nullPointerException){
        Logger logger = manager.getPlugin().getLogger();

        logger.info("Main line: " + nullPointerException.getStackTrace()[0].toString());
        logger.info("Second line: " + nullPointerException.getStackTrace()[1].toString());
        logger.info("Third line: " + nullPointerException.getStackTrace()[2].toString());
        logger.info("Fourth line: " + nullPointerException.getStackTrace()[3].toString());
    }
}
