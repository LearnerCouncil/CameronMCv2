package rocks.learnercouncil.cameronmc.spigot.commands.arguments.portal;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.spigot.portals.Selection;
import rocks.learnercouncil.cameronmc.spigot.commands.CommandArgument;

import java.util.Collections;
import java.util.List;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class SelectorArg implements CommandArgument {
    @Override
    public BaseComponent[] execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!args[1].equalsIgnoreCase("selector")) return NONE;
        if(args.length > 2) return TOO_MANY_ARGS;
        Selection.giveSelector((Player) sender);
        return selectorGiven();
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2) return Collections.singletonList("selector");
        return Collections.emptyList();
    }
}
