package commands;

import dev.forslund.duckcraftcoordinatemanager.DuckcraftCoordinateManager;
import dev.forslund.duckcraftcoordinatemanager.SaveCoords;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CMDGui implements CommandExecutor {
    private DuckcraftCoordinateManager plugin;
    private SaveCoords data;

    public CMDGui(DuckcraftCoordinateManager plugin, SaveCoords data) {
        this.plugin = plugin;
        this.data = data;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.GRAY + "Reloading config...");
            plugin.reloadConfig();
            data.reloadDataFile();
            sender.sendMessage(ChatColor.GREEN + "Config has been reloaded.");
            return true;
        }

        if (sender instanceof Player) {
            Player p = (Player) sender;

            Inventory mainGUI = Bukkit.createInventory(null, 9, "Coordinate Manager");



            // Save location
            ItemStack saveCurrentLocationItem = new ItemStack(Material.LILY_PAD, 1);
            ItemMeta meta = saveCurrentLocationItem.getItemMeta();
            meta.setDisplayName("§2Add current location");
            List<String> saveCurrentLocationLore = new ArrayList<>();
            saveCurrentLocationLore.add("§7Saves the current player coordinates.");
            meta.setLore(saveCurrentLocationLore);
            saveCurrentLocationItem.setItemMeta(meta);

            // List locations
            ItemStack listSavedLocationsItem = new ItemStack(Material.RAIL, 1);
            meta = listSavedLocationsItem.getItemMeta();
            meta.setDisplayName("§bList saved locations");
            List<String> listSavedLocationsLore = new ArrayList<>();
            listSavedLocationsLore.add("§7Lists all saved locations.");
            listSavedLocationsLore.add("§7Maximum " + plugin.getConfig().getInt("max-locations") + " locations can be saved.");
            meta.setLore(listSavedLocationsLore);
            listSavedLocationsItem.setItemMeta(meta);

            mainGUI.setItem(3, saveCurrentLocationItem);
            mainGUI.setItem(5, listSavedLocationsItem);

            // 9 is chest size
            for (int i = 0; i < 9; i++) {
                ItemStack fillerItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                ItemMeta fillerMeta = fillerItem.getItemMeta();
                fillerMeta.setDisplayName(" ");
                List<String> fillerLore = new ArrayList<>();
                fillerLore.add("");
                fillerMeta.setLore(fillerLore);
                fillerItem.setItemMeta(fillerMeta);

                if (i == 3 || i == 5) {
                    // Skip if going over slot with item
                    continue;
                }

                mainGUI.setItem(i, fillerItem);
            }

            p.openInventory(mainGUI);
        }
        return true;
    }
}

