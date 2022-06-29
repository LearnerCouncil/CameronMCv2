package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.bukkit.command.TabCompleter;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;
import rocks.learnercouncil.cameronmc.bungee.events.ServerConnected;
import rocks.learnercouncil.cameronmc.bungee.util.PluginMessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
                if(plugin.navigatorCfg.getConfig().contains(args[0])) {
                    ServerInfo server = plugin.getProxy().getServerInfo(getConfigString(args[0] + ".server"));
                    String world = getConfigString(args[0] + ".world");
                    String x = getConfigString(args[0] + ".x");
                    String y = getConfigString(args[0] + ".y");
                    String z = getConfigString(args[0] + ".z");
                    String pitch = getConfigString(args[0] + ".pitch");
                    String yaw = getConfigString(args[0] + ".yaw");
                    sendPlayer(p, server, world, x, y, z, pitch, yaw);
                    p.sendMessage(new ComponentBuilder("§b[Cameron] §aSending you to \"" + (args[0]) + "\" now...").create());
                    return;
                }
                p.sendMessage(new ComponentBuilder("§b[Cameron] §cThe location \"" + args[0] + "\" does not exist.").create());
                return;
            }
            p.sendMessage(new ComponentBuilder("§b[Cameron] §cToo many arguments!").create());
            return;
        }
        plugin.getLogger().warning("§b[Cameron] §cNeeds to be executed by a player");
    }

    private void sendPlayer(ProxiedPlayer player, ServerInfo server, String world, String x, String y, String z, String pitch, String yaw) {
        if(!player.getServer().getInfo().getName().equals(server.getName())) {
            ServerConnected.queuedPlayers.put(player, () -> PluginMessageHandler.sendPluginMessage(server, "teleport-player", player.getUniqueId().toString(), world, x, y, z, pitch, yaw));
            player.connect(server);
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
