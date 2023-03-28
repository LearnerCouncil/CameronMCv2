package rocks.learnercouncil.cameronmc.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage("");
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("cameron.view-connection-messages"))
                .forEach(p -> p.sendMessage("§e" + e.getPlayer().getName() + " left the game"));
    }
}
