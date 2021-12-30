package dev.forslund.duckcraftcoordinatemanager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class SaveCoords {
    private DuckcraftCoordinateManager plugin;
    private FileConfiguration data;
    private File dataFile;

    public SaveCoords(DuckcraftCoordinateManager plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getData() {
        return data;
    }

    public void saveDataFile() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createDataFile() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            plugin.saveResource("data.yml", false);
        }

        data = new YamlConfiguration();

        try {
            data.load(dataFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
