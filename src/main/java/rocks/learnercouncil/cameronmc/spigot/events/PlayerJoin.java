package rocks.learnercouncil.cameronmc.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerJoin implements Listener {

    private static final Map<Player, Location> queuedPlayers = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        //Handle join commmand
        Player player = e.getPlayer();
        if(queuedPlayers.containsKey(player)) {
            player.teleport(queuedPlayers.get(player));
        }

        //Handle Join Message
        e.setJoinMessage("");
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("cameron.view-connection-messages"))
                .forEach(p -> p.sendMessage("§e" + player.getName() + " joined the game"));
    }

    public static void queueTeleport(Player player, Location location) {
        queuedPlayers.put(player, location);
    }
}
