package me.bryangaming.chatlab.registry;

import me.bryangaming.chatlab.ChatLab;
import me.bryangaming.chatlab.PluginService;
import me.bryangaming.chatlab.debug.DebugLogger;
import me.bryangaming.chatlab.listeners.*;
import me.bryangaming.chatlab.listeners.command.CommandSpyListener;
import me.bryangaming.chatlab.listeners.command.HelpOpListener;
import me.bryangaming.chatlab.listeners.command.SocialSpyListener;
import me.bryangaming.chatlab.listeners.text.ChatListener;
import me.bryangaming.chatlab.managers.click.ChatClickEvent;
import me.bryangaming.chatlab.revisor.tabcomplete.TabFitler;
import me.bryangaming.chatlab.utils.StringFormat;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    private final ChatLab plugin;
    private final PluginService pluginService;

    public EventManager(ChatLab plugin, PluginService pluginService) {
        this.plugin = plugin;
        this.pluginService = pluginService;
    }

    public void setup() {

        StringFormat stringFormat = pluginService.getStringFormat();

        loadEvents(
                new JoinListener(pluginService),
                new QuitListener(pluginService),
                new SendTextListener(pluginService),
                new ChatClickEvent(pluginService),
                new GuiListener(pluginService),
                new HelpOpListener(pluginService),
                new SocialSpyListener(pluginService),
                new CommandSpyListener(pluginService),
                new ServerChangeListener(pluginService),
                new RevisorListener(pluginService),
                new ChatListener(pluginService));

        if (stringFormat.containsVersion(stringFormat.getVersion(Bukkit.getServer()), stringFormat.getLegacyVersion())) {
            loadEvents(new TabListener(pluginService));
        }

        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            TabFitler tabFitler = new TabFitler(pluginService);
        }
        plugin.getLogger().info("Events loaded!");
    }

    public void loadEvents(Listener... listeners) {

        DebugLogger debug = pluginService.getLogs();
        PluginManager pl = Bukkit.getServer().getPluginManager();

        for (Listener listener : listeners) {
            String className = listener.getClass().getName();
            debug.log(className + " loaded!");
            pl.registerEvents(listener, plugin);
        }

    }

}
