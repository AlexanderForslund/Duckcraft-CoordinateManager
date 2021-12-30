package commands;

import org.bukkit.Bukkit;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
            listSavedLocationsLore.add("§7Maximum 15 locations can be saved.");
            meta.setLore(listSavedLocationsLore);
            listSavedLocationsItem.setItemMeta(meta);

            mainGUI.addItem(saveCurrentLocationItem);
            mainGUI.addItem(listSavedLocationsItem);


            p.openInventory(mainGUI);
        }
        return true;
    }
}

