package rocks.learnercouncil.cameronmc.bungee.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandSpyHandler implements Listener {

    private static final CameronMC plugin = CameronMC.getInstance();
    public static final List<String> spies = new ArrayList<>();
    private static final List<ProxiedPlayer> onlineSpies = new ArrayList<>();

    public static void reloadSpies() {
        spies.clear();
        spies.addAll(plugin.cfg.getConfig().getStringList("spying-players"));
    }

    public static void addPlayer(ProxiedPlayer p) {
        spies.add(p.getUniqueId().toString());
        onlineSpies.add(p);
    }
    public static void removePlayer(ProxiedPlayer p) {
        spies.remove(p.getUniqueId().toString());
        onlineSpies.remove(p);
    }
    public static boolean isSpying(ProxiedPlayer p) {
        return onlineSpies.contains(p);
    }


    @EventHandler
    public void onConnected(ServerConnectedEvent e) {
        if(spies.contains(e.getPlayer().getUniqueId().toString()))
            onlineSpies.add(e.getPlayer());
    }

    @EventHandler
    public void onDisconnected(ServerDisconnectEvent e) {
        onlineSpies.remove(e.getPlayer());
    }

    public static void sendMessage(UUID uuid, String command) {
        ProxiedPlayer p = plugin.getProxy().getPlayer(uuid);
        if(p == null) return;
        onlineSpies.forEach(s -> s.sendMessage(new ComponentBuilder(p.getName() + ": " + command).color(ChatColor.GOLD).create()));
    }
}
