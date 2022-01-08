package dev.forslund.duckcraftcoordinatemanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuHandler implements Listener {
    private DuckcraftCoordinateManager plugin;
    private SaveCoords data;
    private InventoryClickEvent e;

    private final Material LOCATION_MATERIAL = Material.ENDER_EYE;

    public MenuHandler(DuckcraftCoordinateManager plugin, SaveCoords data) {
        this.plugin = plugin;
        this.data = data;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        this.e = e;

        ItemStack item = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        String pUUID = p.getUniqueId().toString();

        if (item == null) {
            return;
        }

        // Main menu
        if (e.getView().getTitle().equals("Coordinate Manager")) {
            // Add new coordinate
            if (item.getType().equals(Material.LILY_PAD) && item.getItemMeta().getDisplayName().equals("§2Add current location")) {
                e.setCancelled(true); // Stops inventory actions (Have to put this in every single one to allow users to have chests named ei "cm")
                e.getWhoClicked().sendMessage(ChatColor.GRAY + "Please enter the name of this location or " + ChatColor.BOLD + "cancel"
                        + ChatColor.RESET + ChatColor.GRAY + " to cancel:" );
                plugin.getChatListenPlayers().add(p.getUniqueId().toString());

                e.getWhoClicked().closeInventory();
                return;
            }

            // Show list of locations
            if (item.getType().equals(Material.RAIL) && item.getItemMeta().getDisplayName().equals("§bList saved locations")) {
                e.setCancelled(true); // Stops inventory actions (Have to put this in every single one to allow users to have chests named ei "cm")
                Inventory locationListGUI = renderLocationListGUI(p, plugin, false);
                p.openInventory(locationListGUI);
            }
        }

        // Locations window
        if (e.getView().getTitle().equals("Locations")) {

        }
    }

    public Inventory renderLocationListGUI(Player p, DuckcraftCoordinateManager plugin, boolean isDeleteMode) {
        String pUUID = p.getUniqueId().toString();
        int coordinateCounter = 0;
        int size = 0;

        if (data.getData().isConfigurationSection("players." + pUUID)) {
            ConfigurationSection cs = data.getData().getConfigurationSection("players." + pUUID);
            if (!cs.getKeys(false).isEmpty()) {
                for (String str : cs.getKeys(false)) {
                    size++;
                }
            }
        }

        size = (((size + 3) / 9) + 1) * 9; // No modulo mudlo is for noob

        Inventory locationListGUI = Bukkit.createInventory(null, (size), "Locations");

        if (data.getData().isConfigurationSection("players." + pUUID)) {
            ConfigurationSection cs = data.getData().getConfigurationSection("players." + pUUID);
            if (!cs.getKeys(false).isEmpty()) {
                for (String str : cs.getKeys(false)) {
                    ItemStack locationItem = new ItemStack(LOCATION_MATERIAL, coordinateCounter + 1);
                    ItemMeta meta = locationItem.getItemMeta();
                    meta.setDisplayName("§6" + str);
                    List<String> locationLore = new ArrayList<>();
                    locationLore.add("§7" + data.getData().getString("players." + pUUID + "." + str));

                    if (isDeleteMode) {
                        locationLore.add("§4Click to delete.");
                    }

                    meta.setLore(locationLore);
                    locationItem.setItemMeta(meta);

                    locationListGUI.addItem(locationItem);
                    coordinateCounter++;
                }
            }
        }

        while (((size - 3) - coordinateCounter) > 0) { // Add blank spaces in the form of panes

            coordinateCounter++;

            ItemStack fillerItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
            ItemMeta meta = fillerItem.getItemMeta();
            meta.setDisplayName(" ");
            List<String> fillerLore = new ArrayList<>();
            fillerLore.add("");
            meta.setLore(fillerLore);
            fillerItem.setItemMeta(meta);

            locationListGUI.setItem(coordinateCounter-1, fillerItem);
        }
        return locationListGUI;
    }
}
