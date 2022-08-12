package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;
import rocks.learnercouncil.cameronmc.bungee.util.CommandSpyHandler;

import java.util.ArrayList;

public class CommandSpyCmd extends Command implements TabExecutor {

    private final CameronMC plugin = CameronMC.getInstance();

    public CommandSpyCmd() {
        super("CommandSpy", "cameron.commands.commandspy");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(args.length == 0) {
                if(CommandSpyHandler.isSpying(p)) {
                    CommandSpyHandler.removePlayer(p);
                    p.sendMessage(new ComponentBuilder("§b[Cameron] §eYou are no longer spying on commands.").create());
                } else {
                    CommandSpyHandler.addPlayer(p);
                    p.sendMessage(new ComponentBuilder("§b[Cameron] §eYou are now spying on commands.").create());
                }
                return;
            }
            p.sendMessage(new ComponentBuilder("§b[Cameron] §cToo many arguments!").create());
            return;
        }
        plugin.getLogger().warning("§b[Cameron] §c'/commandspy' must be executed by a player.");
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
