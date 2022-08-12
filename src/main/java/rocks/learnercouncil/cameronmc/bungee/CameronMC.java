package rocks.learnercouncil.cameronmc.bungee;

import dev.simplix.protocolize.api.Protocolize;
import net.md_5.bungee.api.plugin.Plugin;
import rocks.learnercouncil.cameronmc.bungee.commands.CameronCmd;
import rocks.learnercouncil.cameronmc.bungee.commands.HubCmd;
import rocks.learnercouncil.cameronmc.bungee.commands.JoinCmd;
import rocks.learnercouncil.cameronmc.bungee.commands.LCMsgCmd;
import rocks.learnercouncil.cameronmc.bungee.events.PlayerJoin;
import rocks.learnercouncil.cameronmc.bungee.events.ServerConnected;
import rocks.learnercouncil.cameronmc.bungee.protocol.UpstreamChat;
import rocks.learnercouncil.cameronmc.bungee.protocol.DownstreamChat;
import rocks.learnercouncil.cameronmc.bungee.util.ConfigFile;
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
        Protocolize.listenerProvider().registerListener(new UpstreamChat());
        Protocolize.listenerProvider().registerListener(new DownstreamChat());
        // Plugin startup logic
        getLogger().setLevel(Level.INFO);
        getLogger().info("CameronMC (Bungee) started.");
        instance = this;
        navigatorCfg = new ConfigFile(this, "navigator.yml");

        getProxy().registerChannel("cameron:main");
        getProxy().getPluginManager().registerListener(this, new PluginMessageHandler());
        getProxy().getPluginManager().registerListener(this, new ServerConnected());
        getProxy().getPluginManager().registerListener(this, new PlayerJoin());

        getProxy().getPluginManager().registerCommand(this, new JoinCmd());
        getProxy().getPluginManager().registerCommand(this, new CameronCmd());
        getProxy().getPluginManager().registerCommand(this, new LCMsgCmd());
        getProxy().getPluginManager().registerCommand(this, new HubCmd());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
