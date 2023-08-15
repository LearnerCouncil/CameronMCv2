package rocks.learnercouncil.cameronmc.spigot.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.spigot.commands.arguments.FlySpeedArg;
import rocks.learnercouncil.cameronmc.spigot.commands.arguments.SetNavigatorArg;
import rocks.learnercouncil.cameronmc.spigot.commands.arguments.portal.PortalArg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class CameronCmd implements TabExecutor {

    private final CommandArgument[] arguments = {
            new SetNavigatorArg(),
            new FlySpeedArg(),
            new PortalArg()
    };

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("cameron")) return false;
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(needsPlayer(label));
            return true;
        }
        if (!sender.hasPermission("cameron.commands.cameron")) {
            sender.spigot().sendMessage(NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            sender.spigot().sendMessage(NOT_ENOUGH_ARGS);
            return true;
        }
        for (CommandArgument a : arguments) {
            BaseComponent[] result = a.execute(sender, cmd, label, args);
            if (result.length != 0) {
                sender.spigot().sendMessage(result);
                return true;
            }
        }
        sender.spigot().sendMessage(INVALID_ARGS);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> arguments, completions = new ArrayList<>();
        if(!sender.hasPermission("cameron.commands.cameron")) return Collections.emptyList();
        arguments = Arrays.stream(this.arguments).flatMap(a -> a.tabComplete(sender, command, alias, args).stream()).collect(Collectors.toList());
        if(args.length > 0) StringUtil.copyPartialMatches(args[args.length - 1], arguments, completions);
        return completions;
    }
}
