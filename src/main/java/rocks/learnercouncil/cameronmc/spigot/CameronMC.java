package rocks.learnercouncil.cameronmc.spigot;

import org.bukkit.plugin.java.JavaPlugin;
import rocks.learnercouncil.cameronmc.spigot.commands.CameronCmd;
import rocks.learnercouncil.cameronmc.spigot.commands.SkullCmd;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerChat;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerJoin;
import rocks.learnercouncil.cameronmc.spigot.events.PlayerLeave;
import rocks.learnercouncil.cameronmc.spigot.util.PluginMessageHandler;

import java.util.logging.Level;

public class CameronMC extends JavaPlugin {
    private static CameronMC instance;

    public static CameronMC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getLogger().setLevel(Level.INFO);
        getLogger().info("CameronMC (Spigot) started.");
        checkIfValid();
        saveDefaultConfig();

        PlayerChat.chatStyle = getConfig().getString("chat-style");
        PlayerChat.enabled = getConfig().getBoolean("global-chat");

        getServer().getMessenger().registerOutgoingPluginChannel(this, "cameron:main");
        getServer().getMessenger().registerIncomingPluginChannel(this, "cameron:main", new PluginMessageHandler());

        getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);

        getServer().getPluginCommand("skull").setExecutor(new SkullCmd());
        getServer().getPluginCommand("skull").setTabCompleter(new SkullCmd());
        getServer().getPluginCommand("cameron").setExecutor(new CameronCmd());
        getServer().getPluginCommand("cameron").setTabCompleter(new CameronCmd());
    }

    @Override
    public void onDisable() {

    }

    private void checkIfValid()
    {
        if ( !getServer().spigot().getConfig().getBoolean( "settings.bungeecord" ) )
        {
            getLogger().severe( "This server is not BungeeCord." );
            getLogger().severe( "If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell." );
            getLogger().severe( "Plugin disabled!" );
            getServer().getPluginManager().disablePlugin( this );
        }
    }
}
