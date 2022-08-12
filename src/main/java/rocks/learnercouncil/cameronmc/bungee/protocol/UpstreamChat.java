package rocks.learnercouncil.cameronmc.bungee.protocol;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.protocol.packet.Chat;
import rocks.learnercouncil.cameronmc.bungee.util.ChatHandler;
import rocks.learnercouncil.cameronmc.bungee.util.ChatMessage;
import rocks.learnercouncil.cameronmc.bungee.util.CommandSpyHandler;

import java.lang.reflect.Proxy;
import java.util.UUID;

public class UpstreamChat extends AbstractPacketListener<Chat> {

    public UpstreamChat() {
        super(Chat.class, Direction.UPSTREAM, 100);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<Chat> packetReceiveEvent) {
        Chat packet = packetReceiveEvent.packet();
        CommandSpyHandler.sendMessage(packetReceiveEvent.player().uniqueId(), packet.getMessage());
    }

    @Override
    public void packetSend(PacketSendEvent<Chat> packetSendEvent) {
        Chat packet = packetSendEvent.packet();
        if(packet.getPosition() == 0 || (packet.getPosition() == 1)) {
            UUID uuid = packetSendEvent.player().uniqueId();
            if(ChatHandler.hasMessage(uuid, packet.getMessage())) return;
            ChatHandler.addMessage(uuid, new ChatMessage(packet.getMessage()));
        }
    }
}
