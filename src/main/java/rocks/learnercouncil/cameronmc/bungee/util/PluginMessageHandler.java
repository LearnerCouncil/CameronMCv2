package rocks.learnercouncil.cameronmc.bungee.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class PluginMessageHandler implements Listener {
    private static final CameronMC plugin = CameronMC.getInstance();

    public static void sendPluginMessage(ServerInfo server, String subchannel, String... message) {

        if(plugin.getProxy().getPlayers() == null || plugin.getProxy().getPlayers().isEmpty()) {
            plugin.getLogger().severe("§4No players online, message couldn't be sent.");
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        Arrays.stream(message).forEach(out::writeUTF);

        server.sendData("cameron:main", out.toByteArray());
        plugin.getLogger().fine("(PluginMessageHandler) Plugin message " + subchannel + " received.");
    }

    @EventHandler
    public void onPluginMessageReceived(PluginMessageEvent e) {
        if(!(e.getTag().equals("cameron:main"))) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        String subchannel = in.readUTF();
        plugin.getLogger().fine("(PluginMessageHandler) Plugin message " + subchannel + " received.");
        if(subchannel.equals("set-navigator")) {
            Configuration cfg = plugin.navigatorCfg.getConfig();
            String entry = in.readUTF();
            cfg.set(entry + ".server", plugin.getProxy().getPlayer(UUID.fromString(in.readUTF())).getServer().getInfo().getName());
            cfg.set(entry + ".world", in.readUTF());
            cfg.set(entry + ".x", in.readUTF());
            cfg.set(entry + ".y", in.readUTF());
            cfg.set(entry + ".z", in.readUTF());
            cfg.set(entry + ".pitch", in.readUTF());
            cfg.set(entry + ".yaw", in.readUTF());
            plugin.navigatorCfg.saveConfig();
        } else if(subchannel.equals("chat-message")) {
            String msg = in.readUTF();
            for(ProxiedPlayer p : plugin.getProxy().getPlayers()) {
                ComponentBuilder b = new ComponentBuilder(msg);
                if(p.hasPermission("cameron.chat.delete")) {
                    b.append(" [x]").color(ChatColor.RED);
                    int hashCode = ComponentSerializer.toString(b.create()).hashCode();
                    b.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cameronb deleteMessage " + hashCode));
                }
                p.sendMessage(b.create());
            }
        }
    }
}
