package rocks.learnercouncil.cameronmc.bungee;

import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import rocks.learnercouncil.cameronmc.bungee.commands.*;
import rocks.learnercouncil.cameronmc.bungee.events.PlayerJoin;
import rocks.learnercouncil.cameronmc.bungee.events.ServerConnected;
import rocks.learnercouncil.cameronmc.bungee.protocol.UpstreamChat;
import rocks.learnercouncil.cameronmc.bungee.protocol.DownstreamChat;
import rocks.learnercouncil.cameronmc.bungee.util.CommandSpyHandler;
import rocks.learnercouncil.cameronmc.bungee.util.ConfigFile;
import rocks.learnercouncil.cameronmc.bungee.util.PluginMessageHandler;

import java.util.logging.Level;

public final class CameronMC extends Plugin {

    private static CameronMC instance;

    public ConfigFile navigatorCfg;
    public ConfigFile cfg;

    public static CameronMC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Protocolize.listenerProvider().registerListener(new UpstreamChat());
        Protocolize.listenerProvider().registerListener(new DownstreamChat());
        // Plugin startup logic
        getLogger().setLevel(Level.INFO);
        getLogger().info("CameronMC (Bungee) started.");
        instance = this;
        navigatorCfg = new ConfigFile(this, "navigator.yml");
        cfg = new ConfigFile(this, "bungee_config.yml");

        getProxy().registerChannel("cameron:main");
        PluginManager manager = getProxy().getPluginManager();
        manager.registerListener(this, new PluginMessageHandler());
        manager.registerListener(this, new ServerConnected());
        manager.registerListener(this, new PlayerJoin());
        manager.registerListener(this, new CommandSpyHandler());

        manager.registerCommand(this, new JoinCmd());
        manager.registerCommand(this, new CameronCmd());
        manager.registerCommand(this, new LCMsgCmd());
        manager.registerCommand(this, new HubCmd());
        manager.registerCommand(this, new CommandSpyCmd());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        cfg.getConfig().set("spying-players", CommandSpyHandler.spies);
        cfg.saveConfig();
    }
}
