package rocks.learnercouncil.cameronmc.spigot.commands.arguments.portal;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rocks.learnercouncil.cameronmc.spigot.Portal;
import rocks.learnercouncil.cameronmc.spigot.commands.CommandArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class PortalArg implements CommandArgument {

    private final CommandArgument[] arguments = {
            new AddArg(),
            new RemoveArg(),
            new SelectorArg()
    };

    @Override
    public BaseComponent[] execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!args[0].equalsIgnoreCase("portal")) return NONE;
        if(args.length < 2) return NOT_ENOUGH_ARGS;
        for(CommandArgument a : arguments) {
            BaseComponent[] result = a.execute(sender, command, label, args);
            if(result.length != 0) return result;
        }
        return INVALID_ARGS;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) return Collections.singletonList("portal");

        List<String> arguments, completions = new ArrayList<>();
        arguments = Arrays.stream(this.arguments).flatMap(a -> a.tabComplete(sender, command, alias, args).stream()).collect(Collectors.toList());
        if(args.length > 0) StringUtil.copyPartialMatches(args[args.length - 1], arguments, completions);
        return completions;
    }
}
