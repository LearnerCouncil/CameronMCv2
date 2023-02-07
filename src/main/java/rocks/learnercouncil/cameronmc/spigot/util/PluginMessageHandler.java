package rocks.learnercouncil.cameronmc.spigot.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.spigot.CameronMC;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerJoin;

import java.util.Arrays;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class PluginMessageHandler implements PluginMessageListener {
    private static final CameronMC plugin = CameronMC.getInstance();

    public static void sendPluginMessage(Player p, String subchannel, String... message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        Arrays.stream(message).forEach(out::writeUTF);
        p.sendPluginMessage(plugin, "cameron:main", out.toByteArray());
    }
    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if(!(channel.equals("cameron:main"))) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if(subchannel.equalsIgnoreCase("teleport-player")) {
            UUID uuid = UUID.fromString(in.readUTF());
            World world = Bukkit.getServer().getWorld(in.readUTF());
            double x = Double.parseDouble(in.readUTF());
            double y = Double.parseDouble(in.readUTF());
            double z = Double.parseDouble(in.readUTF());
            float pitch = Float.parseFloat(in.readUTF());
            float yaw = Float.parseFloat(in.readUTF());
            Location location = new Location(world, x, y, z, yaw, pitch);
            teleport(uuid, location);
        }
    }

    private void teleport(UUID uuid, Location location) {
        Player player = Bukkit.getPlayer(uuid);
        if(player != null) {
            player.teleport(location);
            return;
        }
        new BukkitRunnable() {
            int failsafe = 0;
            @Override
            public void run() {
                Player player = Bukkit.getPlayer(uuid);
                if(failsafe++ >= 15) cancel();
                if(player == null) return;
                player.teleport(location);
                cancel();
            }
        }.runTaskTimer(plugin, 0, 20);

    }
}
