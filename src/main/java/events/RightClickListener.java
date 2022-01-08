package events;

import dev.forslund.duckcraftcoordinatemanager.DuckcraftCoordinateManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class RightClickListener implements Listener {
    DuckcraftCoordinateManager plugin;

    public RightClickListener(DuckcraftCoordinateManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        boolean enabled = plugin.getConfig().getBoolean("enable-compass-rightclick");

        if (e.getItem() != null && enabled && e.getItem().getType().equals(Material.COMPASS)) {
            e.getPlayer().performCommand("cm");
        }
    }
}
