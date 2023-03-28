package rocks.learnercouncil.cameronmc.spigot;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import rocks.learnercouncil.cameronmc.spigot.commands.CameronCmd;
import rocks.learnercouncil.cameronmc.spigot.commands.SkullCmd;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerJoin;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerLeave;

public class CameronMC extends JavaPlugin {

    @Getter private static CameronMC instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("CameronMC (Spigot) started.");

        getServer().getMessenger().registerOutgoingPluginChannel(this, "cameron:main");
        getServer().getMessenger().registerIncomingPluginChannel(this, "cameron:main", new PluginMessageHandler());

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);

        getServer().getPluginCommand("skull").setExecutor(new SkullCmd());
        getServer().getPluginCommand("skull").setTabCompleter(new SkullCmd());
        getServer().getPluginCommand("cameron").setExecutor(new CameronCmd());
        getServer().getPluginCommand("cameron").setTabCompleter(new CameronCmd());
    }

    @Override
    public void onDisable() {}
}
