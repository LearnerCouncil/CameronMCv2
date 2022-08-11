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
import java.util.stream.Collectors;

public class ChatHandler {

    public static CameronMC plugin = CameronMC.getInstance();

    private static final HashMap<UUID, List<ChatMessage>> messages = new HashMap<>();

    public static void deleteMessage(UUID deleter, int hashCode) {

        List<ChatMessage> messageList = messages.get(deleter).stream().filter(m -> m.hashCode() == hashCode).collect(Collectors.toList());
        for(ChatMessage message : messageList) {
            for(UUID uuid : messages.keySet()) {
                List<ChatMessage> msgs = messages.get(uuid);
                if(msgs.contains(message)) {
                    int index = msgs.indexOf(message);
                    msgs.remove(index);
                    msgs.add(index, getRemovedMessage(message, uuid));
                    resend(uuid);
                }
            }
        }
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
        if(!messages.containsKey(id)) messages.put(id, new ArrayList<>());
        List<ChatMessage> msgs = messages.get(id);
        msgs.add(message);
        if(msgs.size() > 100) msgs.remove(100);
    }

    public static void resend(UUID k) {
        ProxiedPlayer p = plugin.getProxy().getPlayer(k);
        if(p == null) return;
        List<String> msgs = messages.get(k).stream().map(ChatMessage::getMessage).collect(Collectors.toList());
        messages.get(k).clear();
        for(String s : msgs) {
            p.sendMessage(ComponentSerializer.parse(s));
        }
    }

    public static boolean hasMessage(UUID id, String message) {
        for(ChatMessage m : messages.get(id)) {
            if(m.contains(message))
                return true;
        }
        return false;
    }

}
