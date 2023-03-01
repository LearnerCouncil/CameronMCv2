package rocks.learnercouncil.cameronmc.bungee.util;

import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

public class NavigatorLocation {

    private static final CameronMC plugin = CameronMC.getInstance();
    private static @Getter HashMap<String, NavigatorLocation> locations = new HashMap<>();

    public static Optional<NavigatorLocation> get(String name) {
        return Optional.ofNullable(locations.get(name));
    }

    private final @Getter ServerInfo server;
    private final @Getter String world, x, y, z, pitch, yaw;

    public NavigatorLocation(ServerInfo server, String world, String x, String y, String z, String pitch, String yaw) {
        this.server = server;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static void initialize() {
        locations = new HashMap<>();
        Collection<String> locationNames = plugin.navigatorCfg.getConfig().getKeys();
        for(String key : locationNames) {
            locations.put(key, new NavigatorLocation(
                     plugin.getProxy().getServerInfo(plugin.navigatorCfg.getConfig().getString(key + ".server")),
                     plugin.navigatorCfg.getConfig().getString(key + ".world"),
                     getConfigString(key, "x", false),
                     getConfigString(key, "y", false),
                     getConfigString(key, "z", false),
                     getConfigString(key, "pitch", true),
                     getConfigString(key, "yaw", true)
            ));
        }
    }

    private static String getConfigString(String key, String value, boolean isFloat) {
        return checkParseable(plugin.navigatorCfg.getConfig().getString(key + "." + value), key, isFloat);
    }

    private static String checkParseable(String input, String key, boolean isFloat) {
        if(isFloat) {
            try {
                Float.parseFloat(input);
            } catch (NumberFormatException ignored) {
                plugin.getLogger().warning("Key '" + key + "' (Value: " + input + ") not parseable as float.");
            }
            return input;
        }
        try {
            Double.parseDouble(input);
        } catch (NumberFormatException ignored) {
            plugin.getLogger().warning("Key '" + key + "' (Value: " + input + ") not parseable as double.");
        }
        return input;
    }

}
