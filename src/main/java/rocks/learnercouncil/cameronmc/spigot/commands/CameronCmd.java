package rocks.learnercouncil.cameronmc.spigot.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.common.CommandResult;
import rocks.learnercouncil.cameronmc.spigot.CameronMC;
import rocks.learnercouncil.cameronmc.spigot.PluginMessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class CameronCmd implements CommandExecutor, TabCompleter {
    private static final CameronMC plugin = CameronMC.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("cameron")) return false;
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(needsPlayer(label));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("cameron.commands.cameron")) {
            p.spigot().sendMessage(NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            p.spigot().sendMessage();
            return true;
        }
        if (args[0].equalsIgnoreCase("setnavigator")) {
            if (args.length != 2) {
                p.spigot().sendMessage(MUST_SPECIFY_LOCATION);
                return true;
            }
            Location l = p.getLocation();
            PluginMessageHandler.sendPluginMessage(p, "set-navigator",
                    args[1],
                    p.getUniqueId().toString(),
                    Objects.requireNonNull(l.getWorld()).getName(),
                    String.valueOf(l.getX()),
                    String.valueOf(l.getY()),
                    String.valueOf(l.getZ()),
                    String.valueOf(l.getPitch()),
                    String.valueOf(l.getYaw()));
            p.spigot().sendMessage(locationSet(Objects.requireNonNull(l.getWorld()).getName(),
                    String.valueOf(l.getX()), String.valueOf(l.getY()), String.valueOf(l.getZ()),
                    String.valueOf(l.getYaw()), String.valueOf(l.getPitch())));
            return true;
        }
        p.spigot().sendMessage(INVALID_ARGS);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        Player p = null;
        if(sender instanceof Player) {
            p = (Player) sender;
        }
        if (args.length == 1) {
            if(p != null) {
                if(p.hasPermission("cameron.commands.cameron.setnavigator"))
                    arguments.add("setnavigator");
                if(p.hasPermission("cameron.commands.cameron.debug"))
                    arguments.add("debug");
            } else {
                arguments.add("setnavgator");
                arguments.add("debug");
            }
        }
        if(args.length > 0) {
            StringUtil.copyPartialMatches(args[args.length - 1], arguments, completions);
            return completions;
        }
        return new ArrayList<>();
    }
}
