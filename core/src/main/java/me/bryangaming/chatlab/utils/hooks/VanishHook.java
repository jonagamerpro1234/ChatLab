package me.bryangaming.chatlab.utils.hooks;

import de.myzelyam.api.vanish.VanishAPI;
import me.bryangaming.chatlab.PluginService;
import me.bryangaming.chatlab.api.Hook;
import me.bryangaming.chatlab.debug.LoggerTypeEnum;
import me.bryangaming.chatlab.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class VanishHook implements Hook {

    private final PluginService pluginService;

    public VanishHook(PluginService pluginService) {
        this.pluginService = pluginService;
        setup();
    }

    public void setup() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PremiumVanish") || !Bukkit.getPluginManager().isPluginEnabled("SuperVanish")) {
            pluginService.getPlugin().getLogger().info("Addons - SuperVanish not enabled! Disabling support..");
            pluginService.getDebugger().log("Addons - SuperVanish not enabled! Disabling support..", LoggerTypeEnum.WARNING);
            return;
        }

        if (pluginService.getFiles().getConfigFile().getBoolean("options.hooks.protocollib")){
            return;
        }

        pluginService.getPlugin().getLogger().info("Addons - Loaded SuperVanish..");
    }

    public boolean isVanished(OfflinePlayer offlinePlayer){
        return isVanished(offlinePlayer.getPlayer());
    }

    public boolean isVanished(Player player){
        if (!Bukkit.getPluginManager().isPluginEnabled("PremiumVanish") || !Bukkit.getPluginManager().isPluginEnabled("SuperVanish")) {
            return false;
        }

        if (!TextUtils.isHookEnabled("SuperVanish")) {
            return false;
        }

        return VanishAPI.isInvisible(player);
    }
}
