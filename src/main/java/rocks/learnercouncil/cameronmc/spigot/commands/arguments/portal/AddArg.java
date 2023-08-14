package rocks.learnercouncil.cameronmc.spigot.commands.arguments.portal;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rocks.learnercouncil.cameronmc.spigot.Portal;
import rocks.learnercouncil.cameronmc.spigot.Selection;
import rocks.learnercouncil.cameronmc.spigot.commands.CommandArgument;

import java.util.*;
import java.util.stream.Collectors;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class AddArg implements CommandArgument {

    public static Set<String> navigatorLocations = new HashSet<>();

    @Override
    public BaseComponent[] execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!args[1].equalsIgnoreCase("add")) return NONE;
        if(args.length == 2) return MUST_SPECIFY_PORTAL_NAME;
        if(args.length == 3) return MUST_SPECIFY_PORTAL_DESTINATION;
        if(args.length > 5) return TOO_MANY_ARGS;
        Optional<Selection> selection = Selection.getSelection((Player) sender);
        if(!selection.isPresent()) return MUST_SELECT_AREA;
        Material[] materials = new Material[0];
        if(args.length == 5) materials = Arrays.stream(args[4].split(",")).map(Material::getMaterial).toArray(Material[]::new);

        Portal.add(args[2], args[3], selection.get().getSelectionBox(), materials);
        return PORTAL_CREATED;
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2) return Collections.singletonList("add");
        if(args.length == 4 && args[1].equalsIgnoreCase("add")) return new ArrayList<>(navigatorLocations);
        if(args.length == 5 && args[1].equalsIgnoreCase("add")) {
            if(!args[4].contains(","))
                return Arrays.stream(Material.values()).map(Enum::toString).collect(Collectors.toList());
            else
                return Arrays.stream(Material.values()).map(m -> {
                    String s = m.toString();
                    return args[4].substring(0, args[4].lastIndexOf(",")) + s;
                }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
