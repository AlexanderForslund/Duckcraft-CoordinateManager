package dev.forslund.duckcraftcoordinatemanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
    private boolean isDeleteMode;

    private final Material LOCATION_MATERIAL = Material.ENDER_EYE;
    private final int RESERVED_SLOTS = 1;

    public MenuHandler(DuckcraftCoordinateManager plugin, SaveCoords data) {
        this.plugin = plugin;
        this.data = data;
        this.isDeleteMode = false;
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
                e.getWhoClicked().sendMessage(ChatColor.GRAY + "Please enter the name of this location.");
                e.getWhoClicked().sendMessage(ChatColor.GRAY + "Type " + ChatColor.BOLD + "cancel" + ChatColor.RESET + ChatColor.GRAY + " to cancel:");
                plugin.getChatListenPlayers().add(p.getUniqueId().toString());

                e.getWhoClicked().closeInventory();
                return;
            }

            // Show list of locations
            if (item.getType().equals(Material.RAIL) && item.getItemMeta().getDisplayName().equals("§bList saved locations")) {
                e.setCancelled(true); // Stops inventory actions (Have to put this in every single one to allow users to have chests named ei "cm")
                isDeleteMode = false; // Resets delete mode
                Inventory locationListGUI = renderLocationListGUI(p, plugin);
                p.openInventory(locationListGUI);
            }

            if (item.getType().equals((Material.BLACK_STAINED_GLASS_PANE)) && item.getItemMeta().getDisplayName().equals(" ")) {
                e.setCancelled(true);
            }
        }

        // Locations window
        if (e.getView().getTitle().equals("Locations")) {
            if (item.getType().equals((Material.BLACK_STAINED_GLASS_PANE)) && item.getItemMeta().getDisplayName().equals(" ")) {
                e.setCancelled(true);
            }

            if (data.getData().isConfigurationSection("players." + pUUID)) {
                ConfigurationSection cs = data.getData().getConfigurationSection("players." + pUUID);
                if (!cs.getKeys(false).isEmpty()) {
                    for (String str : cs.getKeys(false)) {
                        if (item.getType().equals(Material.ENDER_EYE) && item.getItemMeta().getDisplayName().equals("§6" + str)) {
                            e.setCancelled(true);
                            if (isDeleteMode) {
                                data.getData().set("players." + pUUID + "." + str, null);
                                data.saveDataFile();

                                p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + str + ChatColor.RESET + ChatColor.RED + " has been removed.");
                                p.closeInventory();
                                p.openInventory(renderLocationListGUI(p, plugin));
                            } else {
                                String coordinateString = data.getData().getString("players." + pUUID + "." + str);

                                int x = Integer.parseInt(coordinateString.substring(coordinateString.indexOf("x:") + 3, coordinateString.indexOf(',')));
                                int y = Integer.parseInt(coordinateString.substring(coordinateString.indexOf("y:") + 3, coordinateString.lastIndexOf(',')));
                                int z = Integer.parseInt(coordinateString.substring(coordinateString.indexOf("z:") + 3));

                                p.setCompassTarget(new Location(p.getWorld(), x, y, z));
                                p.sendMessage(ChatColor.DARK_GREEN + "Compass set to location " + ChatColor.BOLD + str + ".");
                            }
                        }
                    }
                }
            }

            // Delete mode toggle
            if (item.getType().equals(Material.COMPASS) && item.getItemMeta().getDisplayName().equals("§2Enable compass mode")) {
                e.setCancelled(true);
                isDeleteMode = false;
                p.closeInventory();
                Inventory gui = renderLocationListGUI(p, plugin);
                p.openInventory(gui);
            }

            if (item.getType().equals(Material.BARRIER) && item.getItemMeta().getDisplayName().equals("§4Enable delete mode")) {
                e.setCancelled(true);
                isDeleteMode = true;
                p.closeInventory();
                Inventory gui = renderLocationListGUI(p, plugin);
                p.openInventory(gui);
            }
        }
    }

    public Inventory renderLocationListGUI(Player p, DuckcraftCoordinateManager plugin) {
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

        size = (((size + RESERVED_SLOTS) / 9) + 1) * 9; // No modulo mudlo is for noob

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
                    } else {
                        locationLore.add("§2Click to set compass to location.");
                    }

                    meta.setLore(locationLore);
                    locationItem.setItemMeta(meta);

                    locationListGUI.addItem(locationItem);
                    coordinateCounter++;
                }
            }
        }

        while (((size - RESERVED_SLOTS) - coordinateCounter) > 0) { // Add blank spaces in the form of panes

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

        // Delete mode button
        ItemStack deleteToggleItem = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = deleteToggleItem.getItemMeta();
        if (isDeleteMode) {
            meta.setDisplayName("§2Enable compass mode");
            deleteToggleItem.setType(Material.COMPASS);
        } else {
            meta.setDisplayName("§4Enable delete mode");
        }
        List<String> deleteToggleLore = new ArrayList<>();
        deleteToggleLore.add("");
        meta.setLore(deleteToggleLore);
        deleteToggleItem.setItemMeta(meta);

        locationListGUI.addItem(deleteToggleItem);

        return locationListGUI;
    }
}
