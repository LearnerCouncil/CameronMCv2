package rocks.learnercouncil.cameronmc.spigot.portals;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

public class Cooldown {
    private static final HashMap<Player, Cooldown> cooldowns = new HashMap<>();

    private final Instant timestamp;
    private final int time;

    public Cooldown(Instant timestamp, int time) {
        this.timestamp = timestamp;
        this.time = time;
    }

    public static void set(Player player, int time) {
        cooldowns.put(player, new Cooldown(Instant.now(), time));
    }

    public static boolean isActive(Player player) {
        Cooldown cooldown = cooldowns.get(player);
        Duration duration = Duration.between(cooldown.timestamp, Instant.now());
        return duration.getSeconds() < cooldown.time;
    }
}
