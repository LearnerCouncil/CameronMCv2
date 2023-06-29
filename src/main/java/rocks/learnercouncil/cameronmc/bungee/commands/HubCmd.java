package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import static rocks.learnercouncil.cameronmc.common.CommandResult.*;

public class HubCmd extends Command {

    private final CameronMC plugin = CameronMC.getInstance();

    public HubCmd() {
        super("hub", "cameron.commands.join");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!plugin.navigatorCfg.getConfig().contains("Hub")) return;
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(needsPlayer("/hub"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!p.hasPermission("cameron.commands.join")) {
            p.sendMessage(NO_PERMISSION);
            return;
        }
        plugin.getProxy().getPluginManager().dispatchCommand(p, "join Hub", null);
        return;

    }
}
