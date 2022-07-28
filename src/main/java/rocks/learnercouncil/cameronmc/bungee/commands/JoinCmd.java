package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;
import rocks.learnercouncil.cameronmc.bungee.util.PluginMessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JoinCmd extends Command implements TabExecutor {
    private static final CameronMC plugin = CameronMC.getInstance();
    public JoinCmd() {
        super("join", "cameron.commands.join");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(!p.hasPermission("cameron.commands.join")) {
                p.sendMessage(new ComponentBuilder("§b[Cameron] §c You don't have permission to execute this command.").create());
                return;
            }
            if(args.length < 1) {
                p.sendMessage(new ComponentBuilder("§b[Cameron] §cYou must specify a location.").create());
                return;
            }
            if(args.length == 1) {
                List<String> keys = plugin.navigatorCfg.getConfig().getKeys().stream().filter(s -> s.equalsIgnoreCase(args[0])).collect(Collectors.toList());
                if(!keys.isEmpty()) {
                    String key = keys.get(0);
                    ServerInfo server = plugin.getProxy().getServerInfo(getConfigString(key + ".server"));
                    String world = getConfigString(key + ".world");
                    String x = getConfigString(key + ".x");
                    String y = getConfigString(key + ".y");
                    String z = getConfigString(key + ".z");
                    String pitch = getConfigString(key + ".pitch");
                    String yaw = getConfigString(key + ".yaw");
                    sendPlayer(p, server, world, x, y, z, pitch, yaw);
                    p.sendMessage(new ComponentBuilder("§b[Cameron] §aSending you to \"" + (key) + "\" now...").create());
                    return;
                }
                p.sendMessage(new ComponentBuilder("§b[Cameron] §cThe location \"" + args[0] + "\" does not exist.").create());
                return;
            }
            p.sendMessage(new ComponentBuilder("§b[Cameron] §cToo many arguments!").create());
            return;
        }
        plugin.getLogger().warning("§b[Cameron] §c'/join' needs to be executed by a player");
    }

    private void sendPlayer(ProxiedPlayer player, ServerInfo server, String world, String x, String y, String z, String pitch, String yaw) {
        if(!player.getServer().getInfo().getName().equals(server.getName())) {
            player.connect(server);
            PluginMessageHandler.sendPluginMessage(server, "teleport-player", player.getUniqueId().toString(), world, x, y, z, pitch, yaw);
        } else {
            PluginMessageHandler.sendPluginMessage(player.getServer().getInfo(), "teleport-player", player.getUniqueId().toString(), world, x, y, z, pitch, yaw);
        }
    }

    private String getConfigString(String path) {
        return plugin.navigatorCfg.getConfig().getString(path);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            List<String> arguments = new ArrayList<>(plugin.navigatorCfg.getConfig().getKeys());
            arguments.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).forEach(completions::add);
            return completions;
        }
        return new ArrayList<>();
    }
}
