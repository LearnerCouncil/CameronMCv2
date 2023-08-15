package rocks.learnercouncil.cameronmc.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import rocks.learnercouncil.cameronmc.spigot.CameronMC;
import rocks.learnercouncil.cameronmc.spigot.PluginMessageHandler;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("cameron.view-connection-messages"))
                .forEach(p -> p.sendMessage("§e" + e.getPlayer().getName() + " joined the game"));

        if(!CameronMC.recievedNavigatorLocations) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    PluginMessageHandler.sendPluginMessage(e.getPlayer(), "request-navigator-locations");
                }
            }.runTaskLater(CameronMC.getInstance(), 20);
        }
    }
}
