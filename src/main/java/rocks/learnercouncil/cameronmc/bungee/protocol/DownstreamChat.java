package rocks.learnercouncil.cameronmc.bungee.protocol;

import dev.simplix.protocolize.api.Direction;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import dev.simplix.protocolize.api.listener.PacketReceiveEvent;
import dev.simplix.protocolize.api.listener.PacketSendEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.protocol.packet.Chat;
import rocks.learnercouncil.cameronmc.bungee.util.ChatHandler;

public class DownstreamChat extends AbstractPacketListener<Chat> {

    public DownstreamChat() {
        super(Chat.class, Direction.DOWNSTREAM, 100);
    }

    @Override
    public void packetReceive(PacketReceiveEvent<Chat> packetReceiveEvent) {
        Chat packet = packetReceiveEvent.packet();
        if(packet.getPosition() == 0 || (packet.getPosition() == 1)) {
            ProxyServer.getInstance().getLogger().warning("[Downstream] (PacketReceiveEvent): " + "Position: " + packet.getPosition() + ", Message: " + packet.getMessage());
            ChatHandler.addMessage(packetReceiveEvent.player().uniqueId(), packet.getMessage());
        }
    }

    @Override
    public void packetSend(PacketSendEvent<Chat> packetSendEvent) {}
}
