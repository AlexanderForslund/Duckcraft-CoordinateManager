package dev.forslund.duckcraftcoordinatemanager;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuHandler implements Listener {
    DuckcraftCoordinateManager plugin;
    private SaveCoords data;

    public MenuHandler(DuckcraftCoordinateManager plugin, SaveCoords data) {
        this.plugin = plugin;
        this.data = data;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        String pUUID = p.getUniqueId().toString();

        if (item == null) {
            return;
        }

        // Main menu
        if (e.getView().getTitle().equalsIgnoreCase("Coordinate Manager")) {

            // Add new coordinate
            if (item.getType().equals(Material.LILY_PAD) && item.getItemMeta().getDisplayName().equals("§2Add current location")) {
                e.setCancelled(true); // Stops inventory actions (Have to put this in every single one to allow users to have chests named ei "cm")
                e.getWhoClicked().sendMessage(ChatColor.GRAY + "Please enter the name of this location:");
                plugin.getChatListenPlayers().add(p.getUniqueId().toString());

                e.getWhoClicked().closeInventory();
            }
        }
    }
}
