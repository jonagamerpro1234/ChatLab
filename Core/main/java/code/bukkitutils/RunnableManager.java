package code.bukkitutils;

import code.Manager;
import code.methods.player.PlayerMessage;
import code.methods.player.PlayerStatic;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RunnableManager{

    private final Manager manager;

    public RunnableManager(Manager manager){
        this.manager = manager;
    }


    public void waitSecond(Player player, int second, String path){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), () -> {
            PlayerMessage playersender = manager.getPlayerMethods().getSender();
            playersender.sendMessage(player, path);
        }, 20L * second);
    }
    public void waitTicks(Player player, long ticks, String path){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), () -> {
            PlayerMessage playersender = manager.getPlayerMethods().getSender();
            playersender.sendMessage(player, path);
        }, ticks);
    }

    public void waitSecond(Player player, int second, String... paths){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), () -> {

            PlayerMessage playersender = manager.getPlayerMethods().getSender();
            for (String path : paths) {
                playersender.sendMessage(player, path);

            }
        }, 20L * second);
    }

    public void waitTicks(Player player, long ticks, String... paths){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), () -> {

            PlayerMessage playersender = manager.getPlayerMethods().getSender();
            for (String path : paths) {
                playersender.sendMessage(player, path);

            }
        }, ticks);
    }


    public void sendCommand(CommandSender sender, String path){
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(manager.getPlugin(), () -> {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                Bukkit.dispatchCommand(sender, PlayerStatic.setFormat(player, path));
                return;
            }

            Bukkit.dispatchCommand(sender, PlayerStatic.setColor(path));

        }, 20L);
    }


}
