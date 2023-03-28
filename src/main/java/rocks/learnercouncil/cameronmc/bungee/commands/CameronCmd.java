package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.ArrayList;
import java.util.List;

public class CameronCmd extends Command implements TabExecutor {
    private static final CameronMC plugin = CameronMC.getInstance();

    public CameronCmd() {
        super("cameronbungee", "cameron.commands.cameronbungee", "cameronmcbungee", "cameronb");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(!p.hasPermission("cameron.commands.cameronbungee")) {
                p.sendMessage(new ComponentBuilder("§b[Cameron] §c You don't have permission to execute this command.").create());
                return;
            }
            if(args.length < 1) {
                p.sendMessage(new ComponentBuilder("§b[Cameron] §cNot enough arguments!").create());
                return;
            }
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {
                    if(!p.hasPermission("cameron.commands.cameronbungee.reload")) {
                        p.sendMessage(new ComponentBuilder("§b[Cameron] §c You don't have permission to execute this command.").create());
                        return;
                    }
                    plugin.navigatorCfg.reloadConfig();
                    p.sendMessage(new ComponentBuilder("§b[Cameron] §aConfig reloaded.").create());
                    return;
                }
                p.sendMessage(new ComponentBuilder("§b[Cameron] §cInvalid arguments").create());
                return;
            }
            p.sendMessage(new ComponentBuilder("§b[Cameron] §cToo many arguments!").create());
            return;
        }
        plugin.getLogger().warning("§b[Cameron] §c'/cameronb' Must be executed by a player.");
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
        return new ArrayList<>();
    }
}
