package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class CameronCmd extends Command implements TabExecutor {
    private static final CameronMC plugin = CameronMC.getInstance();

    public CameronCmd() {
        super("cameronbungee", "cameron.commands.cameronbungee", "cameronmcbungee", "cameronb");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("cameron.commands.cameronbungee")) {
            sender.sendMessage(NO_PERMISSION);
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(NOT_ENOUGH_ARGS);
            return;
        }
        if (args.length > 1) {
            sender.sendMessage(TOO_MANY_ARGS);
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.navigatorCfg.reloadConfig();
            sender.sendMessage(RELOADED);
            return;
        }
        sender.sendMessage(INVALID_ARGS);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            arguments.add("reload");
            arguments.stream().filter(a -> a.toLowerCase().startsWith(args[0].toLowerCase())).forEach(completions::add);
            return completions;
        }
        return Collections.emptyList();
    }
}
