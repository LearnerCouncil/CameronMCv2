package rocks.learnercouncil.cameronmc.spigot;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static rocks.learnercouncil.cameronmc.spigot.Portal.Serializers.*;

public class Portal implements ConfigurationSerializable {
    private static final CameronMC plugin = CameronMC.getInstance();

    public static Map<String, Portal> portals;
    @SuppressWarnings("unchecked")
    public static void initialize() {
        portals = new HashMap<>();
        List<?> list = plugin.getConfig().getList("portals");
        if (list == null || list.isEmpty() || !(list.get(0) instanceof Portal)) return;
        List<Portal> portalList = (List<Portal>) list;
        portalList.forEach(p -> portals.put(p.name, p));
        DetectionLoop.start();
    }
    private Portal(String name, String target, BoundingBox boundingBox, Material[] materials) {
        this.name = name;
        this.destination = target;
        this.boundingBox = boundingBox;
        this.materials = materials;
    }
    public static void add(String name, String target, BoundingBox boundingBox, Material[] materials) {
        if(portals.isEmpty()) DetectionLoop.start();
        portals.put(name, new Portal(name, target, boundingBox, materials));
    }
    public static boolean remove(String name) {
        Portal removed = portals.remove(name);
        if(portals.isEmpty()) DetectionLoop.stop();
        return removed != null;
    }


    private final String name;
    private final String destination;
    private final BoundingBox boundingBox;
    private final Material[] materials;

    @Override
    public String toString() {
        return "Portal{" +
                "name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", boundingBox=" + boundingBox +
                ", materials=" + Arrays.toString(materials) +
                '}';
    }

    @SuppressWarnings({"unchecked", "unused"})
    public Portal(Map<String, Object> m) {
        name = (String) m.get("name");
        destination = (String) m.get("destination");
        boundingBox = toBoundingBox((String) m.get("boundingBox"));
        materials = toMaterials((List<String>) m.get("materials"));
    }
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("name", name);
        m.put("destination", destination);
        m.put("boundingBox", fromBoundingBox(boundingBox));
        m.put("materials", fromMaterials(materials));
        return m;
    }

    public static class DetectionLoop extends BukkitRunnable {
        private static BukkitTask loop = null;

        public static void start() {
            if(loop != null) stop();
            loop = new DetectionLoop().runTaskTimer(plugin, 0, 10);
        }

        public static void stop() {
            loop.cancel();
        }

        @Override
        public void run() {
            for(Portal portal : portals.values()) {
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    Block block = player.getLocation().getBlock();
                    if(!portal.boundingBox.contains(block.getLocation().toVector())) return;
                    if(portal.materials.length == 0)
                        PluginMessageHandler.sendPluginMessage(player, "teleport-player", player.getUniqueId().toString(), portal.destination);
                    for(Material material : portal.materials) {
                        if(block.getType() != material) return;
                        PluginMessageHandler.sendPluginMessage(player, "teleport-player", player.getUniqueId().toString(), portal.destination);
                    }
                });
            }
        }
    }

    static class Serializers {
        public static String fromBoundingBox(BoundingBox box) {
            return box.getMinX() + "," + box.getMinY() + "," + box.getMinZ() + ","
                    + box.getMaxX() + "," + box.getMaxY() + "," + box.getMaxZ();
        }
        public static BoundingBox toBoundingBox(String string) {
            String[] segments = string.split(",");
            if(segments.length != 6) throw new IllegalArgumentException("Bounding Box (" + string + ") cannot be deserialized.");
            try {
                return new BoundingBox(Double.parseDouble(segments[0]),
                        Double.parseDouble(segments[1]),
                        Double.parseDouble(segments[2]),
                        Double.parseDouble(segments[3]),
                        Double.parseDouble(segments[4]),
                        Double.parseDouble(segments[5]));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Bounding Box (" + string + ") cannot be deserialized.");
            }
        }

        public static List<String> fromMaterials(Material[] materials) {
            return Arrays.stream(materials).map(Enum::toString).collect(Collectors.toList());
        }
        public static Material[] toMaterials(List<String> strings) {
            return strings.stream().map(Material::getMaterial).toArray(Material[]::new);
        }
    }
}