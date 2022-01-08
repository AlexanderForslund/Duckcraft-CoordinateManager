package events;

import dev.forslund.duckcraftcoordinatemanager.DuckcraftCoordinateManager;
import dev.forslund.duckcraftcoordinatemanager.SaveCoords;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

public class ChatListener implements Listener {
    private DuckcraftCoordinateManager plugin;
    private SaveCoords data;

    public ChatListener(DuckcraftCoordinateManager plugin, SaveCoords data) {
        this.plugin = plugin;
        this.data = data;
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent e) {
        Set<String> players = plugin.getChatListenPlayers();

        if (players.contains(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
            players.remove(e.getPlayer().getUniqueId().toString());

            if (e.getMessage().equalsIgnoreCase("cancel")) {
                e.getPlayer().sendMessage(ChatColor.GRAY + "Canceled.");
                return;
            }

            if (e.getMessage().equals("last_death")) {
                e.getPlayer().sendMessage(ChatColor.RED + "That location name is reserved.");
                return;
            }

            if (data.getData().isConfigurationSection("players." + e.getPlayer().getUniqueId())) {
                int size = 0;
                ConfigurationSection cs = data.getData().getConfigurationSection("players." + e.getPlayer().getUniqueId());
                if (!cs.getKeys(false).isEmpty()) {
                    for (String str : cs.getKeys(false)) {
                        size++;
                    }
                }

                if (size >= plugin.getConfig().getInt("max-locations")) {
                    e.getPlayer().sendMessage(ChatColor.RED + "Your location list is full.");
                    return;
                }
            }

            if (data.getData().isString("players." + e.getPlayer().getUniqueId().toString() + "." + e.getMessage())) {
                e.getPlayer().sendMessage(ChatColor.RED + "A location with that name already exists!");
//                e.getPlayer().sendMessage(ChatColor.RED + "no");
                return;
            }

            String coordinates = "x: " +
                    e.getPlayer().getLocation().getBlockX() + ", y: " +
                    e.getPlayer().getLocation().getBlockY() + ", z: " +
                    e.getPlayer().getLocation().getBlockZ();
            data.getData().set("players." + e.getPlayer().getUniqueId() + "." + e.getMessage(), coordinates);
            data.saveDataFile();

            e.getPlayer().sendMessage(ChatColor.GREEN + "Location \"" + e.getMessage() + "\" with coordinates: " +
                    coordinates + " was added " + ChatColor.BOLD + "successfully.");
        }

    }

}
