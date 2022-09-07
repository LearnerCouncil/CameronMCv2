package rocks.learnercouncil.cameronmc.spigot.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rocks.learnercouncil.cameronmc.spigot.CameronMC;
import rocks.learnercouncil.cameronmc.spigot.util.PluginMessageHandler;

public class PlayerChat implements Listener {
    public static String chatStyle = "";
    public static boolean enabled = false;
    private final CameronMC plugin = CameronMC.getInstance();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if(!enabled) return;
        if(chatStyle == null) chatStyle = "&f<%player_name%> ";
        String msg =
                ChatColor.translateAlternateColorCodes( '&',
                PlaceholderAPI.setPlaceholders(e.getPlayer(), chatStyle)
                ) + e.getMessage();
        PluginMessageHandler.sendPluginMessage(e.getPlayer(), "chat-message", msg);
        plugin.getLogger().info("[CHAT] " + msg);
        e.setCancelled(true);
    }
}
