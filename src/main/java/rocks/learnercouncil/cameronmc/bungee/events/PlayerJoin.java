package rocks.learnercouncil.cameronmc.bungee.events;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import rocks.learnercouncil.cameronmc.bungee.util.ChatHandler;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(ServerConnectEvent e) {
        if(e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
            ProxiedPlayer p = e.getPlayer();
            ChatHandler.clearMessageHistory(p);

            if(e.getPlayer().getPendingConnection().getVirtualHost().getHostString().equals("play.learnercouncil.rocks")) {
                p.sendMessage(new ComponentBuilder("§4§lATTENTION! §c§lYou connected to the server using §f§lplay.learnercouncil.rocks§c§l, that server address works for now, but it won't for long! In the future, please connect using §f§lmc.learnercouncil.rocks§c§l, Thank you.").create());
            } else
            if(!e.getPlayer().getPendingConnection().getVirtualHost().getHostString().contains("learnercouncil.rocks")) {
                p.sendMessage(new ComponentBuilder("§4§lATTENTION! §c§lYou connected to the server using a unknown address, that address is not controlled by us and may be unsafe. In the future, please connect using §f§lmc.learnercouncil.rocks§c§l, Thank you.").create());
            }
        }
    }
}
