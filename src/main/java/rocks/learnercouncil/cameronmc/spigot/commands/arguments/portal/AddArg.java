package rocks.learnercouncil.cameronmc.spigot.commands.arguments.portal;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.spigot.commands.CommandArgument;

import java.util.*;
import java.util.stream.Collectors;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class AddArg implements CommandArgument {

    public static Set<String> navigatorLocations = new HashSet<>();

    @Override
    public BaseComponent[] execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2) return MUST_SPECIFY_PORTAL_NAME;
        if(args.length == 3) return MUST_SPECIFY_PORTAL_DESTINATION;
        return NONE;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2) return Collections.singletonList("add");
        if(args.length == 4 && args[1].equalsIgnoreCase("add")) return new ArrayList<>(navigatorLocations);
        if(args.length > 4 && args[1].equalsIgnoreCase("add")) return Arrays.stream(Material.values()).map(Enum::toString).collect(Collectors.toList());
        return Collections.emptyList();
    }
}
