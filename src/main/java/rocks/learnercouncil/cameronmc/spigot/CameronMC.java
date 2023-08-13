package rocks.learnercouncil.cameronmc.spigot;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import rocks.learnercouncil.cameronmc.spigot.commands.CameronCmd;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerJoin;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerLeave;

import java.util.List;

public class CameronMC extends JavaPlugin {

    @Getter private static CameronMC instance;
    public static List<String> navigatorLocations;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CameronMC (Spigot) started.");

        saveDefaultConfig();
        Portal.initialize();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "cameron:main");
        getServer().getMessenger().registerIncomingPluginChannel(this, "cameron:main", new PluginMessageHandler());

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);

        getServer().getPluginCommand("cameron").setExecutor(new CameronCmd());
        getServer().getPluginCommand("cameron").setTabCompleter(new CameronCmd());
    }

    @Override
    public void onDisable() {
        getConfig().set("portals", Portal.portals.values());
        saveConfig();
    }
}
