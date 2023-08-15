package rocks.learnercouncil.cameronmc.spigot.commands.arguments;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rocks.learnercouncil.cameronmc.spigot.commands.CommandArgument;

import java.util.Collections;
import java.util.List;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class FlySpeedArg implements CommandArgument {
    @Override
    public BaseComponent[] execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!args[0].equalsIgnoreCase("flyspeed")) return NONE;
        if (args.length != 2) return MUST_SPECIFY_SPEED;
        float speed;
        try {
            speed = Float.parseFloat(args[1]);
        } catch (NumberFormatException e) {
            return invalidNumber(args[1]);
        }

        ((Player) sender).setFlySpeed(speed / 10);
        return speedSet(args[1]);
    }

    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) return Collections.singletonList("flyspeed");
        return Collections.emptyList();
    }
}
