package dev.forslund.duckcraftcoordinatemanager;

import commands.CMDGui;
import events.ChatListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class DuckcraftCoordinateManager extends JavaPlugin {
    private SaveCoords data;
    private Set<String> chatListenPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        data = new SaveCoords(this);
        data.createDataFile();
        saveDefaultConfig();

        // Events
        getServer().getPluginManager().registerEvents(new MenuHandler(this, data), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this, data), this);

        // Cmds
        getCommand("coordinatemanager").setExecutor(new CMDGui());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public Set<String> getChatListenPlayers() {
        return chatListenPlayers;
    }
}
