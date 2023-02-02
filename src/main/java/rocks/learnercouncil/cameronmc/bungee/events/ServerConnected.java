package rocks.learnercouncil.cameronmc.bungee.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.HashMap;

public class ServerConnected implements Listener {

    private static final CameronMC plugin = CameronMC.getInstance();

    public static HashMap<ProxiedPlayer, Runnable> queuedPlayers = new HashMap<>();

    @EventHandler
    public void onServerConnected(ServerConnectedEvent e) {
        for(ProxiedPlayer p : queuedPlayers.keySet()) {
            if(p.getUniqueId().equals(e.getPlayer().getUniqueId())) {
                plugin.getLogger().fine("(ServerConnected) Firing queued action for player " + p.getName() + ".");
                queuedPlayers.get(p).run();
                queuedPlayers.remove(p);
            }
        }
    }
}
