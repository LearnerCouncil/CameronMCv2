package rocks.learnercouncil.cameronmc.bungee.events;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;
import rocks.learnercouncil.cameronmc.bungee.util.NavigatorLocation;
import rocks.learnercouncil.cameronmc.bungee.util.PluginMessageHandler;

import java.util.Optional;

public class PlayerJoin implements Listener {

    public static final CameronMC plugin = CameronMC.getInstance();

    @EventHandler
    public void onPlayerJoin(ServerConnectEvent e) {
        if(!e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) return;
        ProxiedPlayer p = e.getPlayer();
        sendToHub(p, e.getTarget());

        if(p.getPendingConnection().getVirtualHost().getHostString().equals("play.learnercouncil.rocks"))
            p.sendMessage(new ComponentBuilder("§4§lATTENTION! §c§lYou connected to the server using §f§lplay.learnercouncil.rocks§c§l, that server address works for now, but it won't for long! In the future, please connect using §f§lmc.learnercouncil.rocks§c§l, Thank you.").create());
        else if(!p.getPendingConnection().getVirtualHost().getHostString().contains("learnercouncil.rocks") && !p.getName().startsWith("+"))
            p.sendMessage(new ComponentBuilder("§4§lATTENTION! §c§lYou connected to the server using a unknown address, that address is not controlled by us and may be unsafe. In the future, please connect using §f§lmc.learnercouncil.rocks§c§l, Thank you.").create());

    }

    private void sendToHub(ProxiedPlayer player, ServerInfo targetServer) {
        NavigatorLocation.get("Hub").ifPresent(hub -> hub.getServer().ping((result, error) -> {
            if(error != null) return;
            if(!targetServer.equals(hub.getServer())) return;
            PluginMessageHandler.sendPluginMessage(hub.getServer(), "teleport-player", player.getUniqueId().toString(), hub.getWorld(), hub.getX(), hub.getY(), hub.getZ(), hub.getPitch(), hub.getYaw());
        }));
    }
}
