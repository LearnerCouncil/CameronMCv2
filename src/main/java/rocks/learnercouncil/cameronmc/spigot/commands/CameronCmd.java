package rocks.learnercouncil.cameronmc.spigot.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.spigot.PluginMessageHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class CameronCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("cameron")) return false;
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(needsPlayer(label));
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("cameron.commands.cameron")) {
            player.spigot().sendMessage(NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            player.spigot().sendMessage(NOT_ENOUGH_ARGS);
            return true;
        }
        if (args[0].equalsIgnoreCase("setnavigator")) {
            if (args.length != 2) {
                player.spigot().sendMessage(MUST_SPECIFY_LOCATION);
                return true;
            }
            Location l = player.getLocation();
            PluginMessageHandler.sendPluginMessage(player, "set-navigator",
                    args[1],
                    player.getUniqueId().toString(),
                    Objects.requireNonNull(l.getWorld()).getName(),
                    String.valueOf(l.getX()),
                    String.valueOf(l.getY()),
                    String.valueOf(l.getZ()),
                    String.valueOf(l.getPitch()),
                    String.valueOf(l.getYaw()));
            player.spigot().sendMessage(locationSet(Objects.requireNonNull(l.getWorld()).getName(),
                    String.valueOf(l.getX()), String.valueOf(l.getY()), String.valueOf(l.getZ()),
                    String.valueOf(l.getYaw()), String.valueOf(l.getPitch())));
            return true;
        } else if (args[0].equalsIgnoreCase("flyspeed")) {
            if (args.length != 2) {
                player.spigot().sendMessage(MUST_SPECIFY_SPEED);
                return true;
            }
            float speed;
            try {
                speed = Float.parseFloat(args[1]);
            } catch (NumberFormatException e) {
                player.spigot().sendMessage(invalidNumber(args[1]));
                return true;
            }
            player.setFlySpeed(speed / 10);
            player.spigot().sendMessage(speedSet(args[1]));
        }
        player.spigot().sendMessage(INVALID_ARGS);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("cameron.commands.cameron")) {
            arguments.add("setnavigator");
            arguments.add("flyspeed");
        }
        if(args.length > 0) {
            StringUtil.copyPartialMatches(args[args.length - 1], arguments, completions);
            return completions;
        }
        return Collections.emptyList();
    }
}
