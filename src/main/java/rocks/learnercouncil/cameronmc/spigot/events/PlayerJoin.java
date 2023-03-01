package rocks.learnercouncil.cameronmc.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("cameron.view-connection-messages"))
                .forEach(p -> p.sendMessage("§e" + e.getPlayer().getName() + " joined the game"));
    }
}
