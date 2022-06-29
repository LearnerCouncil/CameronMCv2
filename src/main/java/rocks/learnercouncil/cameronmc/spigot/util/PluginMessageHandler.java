package rocks.learnercouncil.cameronmc.spigot.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import rocks.learnercouncil.cameronmc.spigot.CameronMC;

import java.util.Arrays;
import java.util.UUID;

public class PluginMessageHandler implements PluginMessageListener {
    private static final CameronMC plugin = CameronMC.getInstance();

    public static void sendPluginMessage(Player p, String subchannel, String... message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        Arrays.stream(message).forEach(out::writeUTF);
        p.sendPluginMessage(plugin, "cameron:main", out.toByteArray());
        plugin.getLogger().fine("(PluginMessageHandler) Plugin message " + subchannel + " sent.");
    }
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!(channel.equals("cameron:main"))) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        plugin.getLogger().fine("(PluginMessageHandler) Plugin message " + subchannel + " received.");
        if(subchannel.equalsIgnoreCase("teleport-player")) {
            Player p = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
            if(p == null) return;
            World world = Bukkit.getServer().getWorld(in.readUTF());
            double x = Double.parseDouble(in.readUTF());
            double y = Double.parseDouble(in.readUTF());
            double z = Double.parseDouble(in.readUTF());
            float pitch = Float.parseFloat(in.readUTF());
            float yaw = Float.parseFloat(in.readUTF());
            p.teleport(new Location(world, x, y, z, yaw, pitch));
            plugin.getLogger().fine("{PluginMessageHandler) Teleporting " + p.getName() + ", World: " + world + ", XYZ: " + x + ", " + y + ", " + z + ", Rotation: " + yaw + ", " + pitch);
        }

    }
}
