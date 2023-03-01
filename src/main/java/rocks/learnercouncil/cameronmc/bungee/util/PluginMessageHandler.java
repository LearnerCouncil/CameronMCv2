package rocks.learnercouncil.cameronmc.bungee.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
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

@SuppressWarnings("UnstableApiUsage")
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
    }

    @EventHandler
    public void onPluginMessageReceived(PluginMessageEvent e) {
        if(!(e.getTag().equals("cameron:main"))) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        String subchannel = in.readUTF();
        if(subchannel.equals("set-navigator")) {
            Configuration cfg = plugin.navigatorCfg.getConfig();
            String entry = in.readUTF();
            ServerInfo server = plugin.getProxy().getPlayer(UUID.fromString(in.readUTF())).getServer().getInfo();
            String world = in.readUTF();
            String x = in.readUTF();
            String y = in.readUTF();
            String z = in.readUTF();
            String pitch = in.readUTF();
            String yaw = in.readUTF();

            cfg.set(entry + ".server", server.getName());
            cfg.set(entry + ".world", world);
            cfg.set(entry + ".x", x);
            cfg.set(entry + ".y", y);
            cfg.set(entry + ".z", z);
            cfg.set(entry + ".pitch", pitch);
            cfg.set(entry + ".yaw", yaw);
            plugin.navigatorCfg.saveConfig();

            NavigatorLocation.getLocations().put(entry, new NavigatorLocation(server, world, x, y, z, pitch, yaw));
        } else if(subchannel.equals("chat-message")) {
            String msg = in.readUTF();
            for(ProxiedPlayer p : plugin.getProxy().getPlayers()) {
                ComponentBuilder builder = new ComponentBuilder(msg);
                String stripped = ComponentSerializer.toString(builder.create());
                int hashCode = ComponentSerializer.toString(builder.create()).hashCode();
                ComponentBuilder delBuilder = new ComponentBuilder(builder);
                delBuilder.append(" [x]").color(ChatColor.RED).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cameronb deleteMessage " + hashCode));
                String message = ComponentSerializer.toString(delBuilder.create());
                ChatMessage cm = new ChatMessage(message, stripped);
                ChatHandler.addMessage(p.getUniqueId(), cm);
                if(p.hasPermission("cameron.chat.delete"))
                    p.sendMessage(delBuilder.create());
                else
                    p.sendMessage(builder.create());
            }
        }
    }
}
