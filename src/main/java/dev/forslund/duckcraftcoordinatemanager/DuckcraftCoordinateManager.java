package dev.forslund.duckcraftcoordinatemanager;

import org.bukkit.plugin.java.JavaPlugin;

public final class DuckcraftCoordinateManager extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
