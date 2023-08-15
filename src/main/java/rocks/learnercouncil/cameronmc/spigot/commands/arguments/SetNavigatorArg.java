package rocks.learnercouncil.cameronmc.spigot.commands.arguments;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rocks.learnercouncil.cameronmc.spigot.PluginMessageHandler;
import rocks.learnercouncil.cameronmc.spigot.commands.CommandArgument;
import rocks.learnercouncil.cameronmc.spigot.commands.arguments.portal.AddArg;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class SetNavigatorArg implements CommandArgument {
    @Override
    public BaseComponent[] execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!args[0].equalsIgnoreCase("setnavigator")) return NONE;
        if (args.length != 2) return MUST_SPECIFY_LOCATION;
        Player player = (Player) sender;
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
        AddArg.navigatorLocations.add(args[1]);
        return locationSet(Objects.requireNonNull(l.getWorld()).getName(),
                String.valueOf(l.getX()), String.valueOf(l.getY()), String.valueOf(l.getZ()),
                String.valueOf(l.getYaw()), String.valueOf(l.getPitch()));
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) return Collections.singletonList("setnaviator");
        return Collections.emptyList();
    }
}
