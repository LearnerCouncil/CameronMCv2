package rocks.learnercouncil.cameronmc.spigot;

import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import rocks.learnercouncil.cameronmc.spigot.commands.CameronCmd;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerJoin;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerLeave;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CameronMC extends JavaPlugin {

    @Getter private static CameronMC instance;
    public static List<String> navigatorLocations;
    public static boolean recievedNavigatorLocations = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CameronMC (Spigot) started.");

        ConfigurationSerialization.registerClass(Portal.class);
        saveDefaultConfig();
        Portal.initialize();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "cameron:main");
        getServer().getMessenger().registerIncomingPluginChannel(this, "cameron:main", new PluginMessageHandler());

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);
        getServer().getPluginManager().registerEvents(new Selection.Events(), this);

        getServer().getPluginCommand("cameron").setExecutor(new CameronCmd());
        getServer().getPluginCommand("cameron").setTabCompleter(new CameronCmd());
    }

    @Override
    public void onDisable() {
        getConfig().set("portals", new ArrayList<>(Portal.portals.values()));
        saveConfig();
    }
}
