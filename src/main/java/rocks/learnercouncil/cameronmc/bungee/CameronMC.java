package rocks.learnercouncil.cameronmc.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import rocks.learnercouncil.cameronmc.bungee.commands.*;
import rocks.learnercouncil.cameronmc.bungee.events.PlayerJoin;
import rocks.learnercouncil.cameronmc.bungee.util.ConfigFile;
import rocks.learnercouncil.cameronmc.bungee.util.NavigatorLocation;
import rocks.learnercouncil.cameronmc.bungee.util.PluginMessageHandler;

import java.util.logging.Level;

public final class CameronMC extends Plugin {

    private static CameronMC instance;

    public ConfigFile navigatorCfg;

    public static CameronMC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().setLevel(Level.INFO);
        getLogger().info("CameronMC (Bungee) started.");
        instance = this;
        navigatorCfg = new ConfigFile(this, "navigator.yml");

        NavigatorLocation.initialize();

        getProxy().registerChannel("cameron:main");
        PluginManager manager = getProxy().getPluginManager();
        manager.registerListener(this, new PluginMessageHandler());
        manager.registerListener(this, new PlayerJoin());

        manager.registerCommand(this, new JoinCmd());
        manager.registerCommand(this, new CameronCmd());
        manager.registerCommand(this, new LCMsgCmd());
        manager.registerCommand(this, new HubCmd());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
