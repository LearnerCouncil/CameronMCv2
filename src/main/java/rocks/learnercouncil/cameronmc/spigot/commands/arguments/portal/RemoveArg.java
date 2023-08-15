package rocks.learnercouncil.cameronmc.spigot.commands.arguments.portal;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.spigot.portals.Portal;
import rocks.learnercouncil.cameronmc.spigot.commands.CommandArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class RemoveArg implements CommandArgument {
    @Override
    public BaseComponent[] execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!args[1].equalsIgnoreCase("remove")) return NONE;
        if(args.length == 2) return MUST_SPECIFY_PORTAL;
        if(args.length > 3) return TOO_MANY_ARGS;
        return Portal.remove(args[2]) ? PORTAL_REMOVED : NO_PORTAL;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2) return Collections.singletonList("remove");
        if(args.length == 3 && args[1].equalsIgnoreCase("remove")) return new ArrayList<>(Portal.portals.keySet());
        return Collections.emptyList();
    }
}
