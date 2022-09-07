package rocks.learnercouncil.cameronmc.bungee.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.Chat;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.*;
import java.util.stream.Collectors;

public class ChatHandler {

    public static CameronMC plugin = CameronMC.getInstance();

    private static final HashMap<UUID, List<ChatMessage>> messages = new HashMap<>();

    public static void deleteMessage(UUID deleter, int hashCode) {
        for(int i = 0; messages.get(deleter).stream().anyMatch(m -> m.hashCode() == hashCode) && i < 100; i++) {
            ChatMessage message = messages.get(deleter).stream().filter(m -> m.hashCode() == hashCode).findFirst().orElse(null);
            if (message == null) return;
            for (UUID uuid : messages.keySet()) {
                List<ChatMessage> msgs = messages.get(uuid);
                List<ChatMessage> msgs2 = new ArrayList<>(messages.get(uuid));
                for (ChatMessage m : msgs2) {
                    if (m.equals(message)) {
                        int index = msgs2.indexOf(m);
                        msgs.remove(index);
                        msgs.add(index, getRemovedMessage(m, uuid));
                    }
                }
            }
        }
        messages.keySet().forEach(ChatHandler::resend);
    }

    private static ChatMessage getRemovedMessage(ChatMessage message, UUID uuid) {
            ComponentBuilder deletedMsg = new ComponentBuilder().color(ChatColor.RED).append("<removed>");
            ProxiedPlayer player = plugin.getProxy().getPlayer(uuid);
            if (player != null && player.hasPermission("cameron.chat.delete")) {
                deletedMsg.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ComponentSerializer.parse(message.getStripped()))));
            }
            return new ChatMessage(ComponentSerializer.toString(deletedMsg.create()));
    }

    public static void addMessage(UUID id, ChatMessage message) {
        if(!messages.containsKey(id)) messages.put(id, emptyList());
        List<ChatMessage> msgs = messages.get(id);
        msgs.add(message);
        if(msgs.size() > 100) msgs.remove(0);
    }

    public static void resend(UUID k) {
        ProxiedPlayer p = plugin.getProxy().getPlayer(k);
        if(p == null) return;
        List<ChatMessage> msgs = messages.get(k);
        List<ChatMessage> msgsCopy = new ArrayList<>(messages.get(k));
        for(ChatMessage m : msgsCopy) {
            p.sendMessage(ComponentSerializer.parse(p.hasPermission("cameron.chat.delete") ? m.getMessage() : m.getStripped()));
        }
        msgs.clear();
        msgs.addAll(msgsCopy);
    }

    public static boolean hasMessage(UUID id, String message) {
        if(!messages.containsKey(id)) return false;
        List<ChatMessage> msgs = messages.get(id);
        for(ChatMessage m : msgs) {
            if(m.contains(message))
                return true;
        }
        return false;
    }

    public static void clearMessageHistory(ProxiedPlayer p) {
        messages.put(p.getUniqueId(), emptyList());
    }

    private static List<ChatMessage> emptyList() {
        return new ArrayList<>(Collections.nCopies(100, new ChatMessage("")));
    }

}
