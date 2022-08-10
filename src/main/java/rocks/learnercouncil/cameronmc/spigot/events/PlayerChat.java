package rocks.learnercouncil.cameronmc.spigot.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rocks.learnercouncil.cameronmc.spigot.util.PluginMessageHandler;

public class PlayerChat implements Listener {
    public static String chatStyle = "";

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if(chatStyle == null) chatStyle = "&f<%player_name%> ";
        String msg = PlaceholderAPI.setPlaceholders(e.getPlayer(), chatStyle) + e.getMessage();
        PluginMessageHandler.sendPluginMessage(e.getPlayer(), "chat_message", e.getPlayer().getUniqueId().toString(), msg);
        e.setCancelled(true);
    }
}
