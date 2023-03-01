package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;
import rocks.learnercouncil.cameronmc.bungee.util.NavigatorLocation;
import rocks.learnercouncil.cameronmc.bungee.util.PluginMessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                Optional<NavigatorLocation> locationOptional = NavigatorLocation.get(args[0]);
                if(!locationOptional.isPresent()) {
                    p.sendMessage(new ComponentBuilder("§b[Cameron] §cThe location \"" + args[0] + "\" does not exist.").create());
                    return;
                }
                NavigatorLocation location = locationOptional.get();
                sendPlayer(p, location.getServer(), location);
            }
            p.sendMessage(new ComponentBuilder("§b[Cameron] §cToo many arguments!").create());
            return;
        }
        plugin.getLogger().warning("§b[Cameron] §cNeeds to be executed by a player");
    }

    private void sendPlayer(ProxiedPlayer player, ServerInfo server, NavigatorLocation location) {
        if(server == null) {
            player.sendMessage(new ComponentBuilder("§b[Cameron] §cThis server is offline.").create());
            return;
        }
        if(!player.getServer().getInfo().getName().equals(server.getName())) {
            server.ping(((result, error) -> {
                if (error != null) {
                    player.sendMessage(new ComponentBuilder("§b[Cameron] §cThis server is offline.").create());
                    return;
                }
                player.connect(server);
            }));
        }
        PluginMessageHandler.sendPluginMessage(player.getServer().getInfo(), "teleport-player", player.getUniqueId().toString(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
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
