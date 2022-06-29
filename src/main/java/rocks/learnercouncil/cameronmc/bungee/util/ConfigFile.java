package rocks.learnercouncil.cameronmc.bungee.util;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.io.*;
import java.util.logging.Level;

public class ConfigFile {

    private Configuration config;
    private final File configFile;

    private final CameronMC plugin;
    private final String name;

    public ConfigFile(CameronMC plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        configFile = getDefault();
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create configuration file", e);
        }
    }

    private File getDefault() {
        if(!plugin.getDataFolder().exists()) {
            plugin.getLogger().info("Created plugin data folder: " + plugin.getDataFolder().mkdir());
        }

        File configFile = new File(plugin.getDataFolder(), name);

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = plugin.getResourceAsStream(name);
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
        return configFile;
    }

    public Configuration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Unable to save config.", e);
        }
    }
    public void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration file", e);
        }
    }
}
