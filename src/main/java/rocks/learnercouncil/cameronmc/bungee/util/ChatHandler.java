package rocks.learnercouncil.cameronmc.bungee.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.*;

public class ChatHandler {

    public static CameronMC plugin = CameronMC.getInstance();

    private static final HashMap<UUID, List<String>> messages = new HashMap<>();

    public static void deleteMessage(UUID deleter, int hashCode) {
        plugin.getLogger().info("§aDeleting: " + hashCode);
        String message = messages.get(deleter).stream().filter(m -> {
            plugin.getLogger().info("Before: " + m);
            if(m.contains(",\"clickEvent\"")) {
                StringBuilder buffer = new StringBuilder(m);
                int start = m.indexOf(",\"clickEvent\"");
                int end = m.indexOf('}', start) + 1;
                String s = buffer.replace(start, end, "").toString();
                plugin.getLogger().info("Message: " + s + ", HashCode: " + s.hashCode() + ", Needed: " + hashCode);
                return s.hashCode() == hashCode;
            }
            return m.hashCode() == hashCode;
        }).findFirst().orElse("");
        if(message.equals("")) return;
        plugin.getLogger().info("§aFound!");
        for(UUID uuid : messages.keySet()) {
            List<String> msgs = messages.get(uuid);
            if(msgs.contains(message)) {
                int index = msgs.indexOf(message);
                plugin.getLogger().info("§aFound at index " + index);
                ComponentBuilder deletedMsg = new ComponentBuilder().color(ChatColor.RED).append("<removed>");
                ProxiedPlayer player = plugin.getProxy().getPlayer(uuid);
                if (player != null && player.hasPermission("cameron.chat.delete")) {
                    deletedMsg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ComponentSerializer.parse(message))));
                }
                String json = ComponentSerializer.toString(deletedMsg.create());
                plugin.getLogger().info("§aCreated JSON: §r" + json);
                msgs.remove(index);
                msgs.add(index, json);
                messages.put(uuid, msgs);
                resend(uuid);
            }
        }
    }

    public static void addMessage(UUID id, String message) {
        if(!messages.containsKey(id)) messages.put(id, new ArrayList<>());
        List<String> msgs = messages.get(id);
        msgs.add(message);
        if(msgs.size() > 100) msgs.remove(100);
    }

    public static void resend(UUID k) {
        ProxiedPlayer p = plugin.getProxy().getPlayer(k);
        if(p == null) return;
        List<String> msgs = new ArrayList<>(messages.get(k));
        messages.get(k).clear();
        for(String s : msgs) {
            p.sendMessage(ComponentSerializer.parse(s));
        }
    }

}
