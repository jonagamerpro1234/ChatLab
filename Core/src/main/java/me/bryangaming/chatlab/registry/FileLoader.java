package me.bryangaming.chatlab.registry;

import me.bryangaming.chatlab.ChatLab;
import me.bryangaming.chatlab.PluginService;
import me.bryangaming.chatlab.debug.DebugLogger;
import me.bryangaming.chatlab.utils.Configuration;

import java.util.Map;

public class FileLoader {

    // Tranquilos, esta clase no pesa mucho, pero no confunde a nadie :xd:.

    private Configuration config;
    private Configuration command;
    private Configuration players;
    private Configuration messages;
    private Configuration sounds;
    private Configuration utils;

    private final ChatLab plugin;
    private final PluginService pluginService;

    public FileLoader(ChatLab plugin, PluginService pluginService) {
        this.plugin = plugin;
        this.pluginService = pluginService;
    }

    public void setup() {
        config = this.setConfiguration("config.yml");
        command = this.setConfiguration("commands.yml");
        players = this.setConfiguration("players.yml");
        messages = this.setConfiguration("messages.yml");
        sounds = this.setConfiguration("sounds.yml");
        utils = this.setConfiguration("formats.yml");

        plugin.getLogger().info("Config loaded!");
    }

    public Configuration setConfiguration(String string) {

        DebugLogger log = pluginService.getLogs();
        Map<String, Configuration> configFiles = pluginService.getCache().getConfigFiles();

        Configuration config = new Configuration(plugin, string);
        configFiles.put(string.split("\\.")[0], config);

        log.log(string + " loaded!");
        return config;
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration getBasicUtils() {
        return utils;
    }

    public Configuration getCommand() {
        return command;
    }

    public Configuration getPlayers() {
        return players;
    }

    public Configuration getMessages() {
        return messages;
    }

    public Configuration getSounds() {
        return sounds;
    }
}
