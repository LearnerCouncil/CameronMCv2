package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;
import rocks.learnercouncil.cameronmc.bungee.util.NavigatorLocation;
import rocks.learnercouncil.cameronmc.bungee.util.PluginMessageHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class JoinCmd extends Command implements TabExecutor {
    private static final CameronMC plugin = CameronMC.getInstance();
    public JoinCmd() {
        super("join", "cameron.commands.join");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(needsPlayer("/join"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("cameron.commands.join")) {
            p.sendMessage(NO_PERMISSION);
            return;
        }
        if (args.length < 1) {
            p.sendMessage(MUST_SPECIFY_LOCATION);
            return;
        }
        if (args.length != 1) {
            p.sendMessage(TOO_MANY_ARGS);
            return;
        }
        Optional<NavigatorLocation> locationOptional = NavigatorLocation.get(args[0]);
        if (!locationOptional.isPresent()) {
            p.sendMessage(locationNotExist(args[0]));
            return;
        }
        NavigatorLocation location = locationOptional.get();
        sendPlayer(p, location);
    }

    public static void sendPlayer(ProxiedPlayer player, NavigatorLocation location) {
        ServerInfo server = location.getServer();
        if(server == null) {
            player.sendMessage(SERVER_OFFLINE);
            return;
        }
        if(!player.getServer().getInfo().getName().equals(server.getName())) {
            server.ping(((result, error) -> {
                if (error != null) {
                    player.sendMessage(SERVER_OFFLINE);
                    return;
                }
                player.connect(server);
            }));
        }
        PluginMessageHandler.sendPluginMessage(server, "teleport-player", player.getUniqueId().toString(), location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            List<String> arguments = new ArrayList<>(NavigatorLocation.getLocations().keySet());
            arguments.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).forEach(completions::add);
            return completions;
        }
        return Collections.emptyList();
    }
}
