package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

public class HubCmd extends Command {

    private final CameronMC plugin = CameronMC.getInstance();

    public HubCmd() {
        super("hub", "cameron.commands.join");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!plugin.navigatorCfg.getConfig().contains("Hub")) return;
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(!p.hasPermission("cameron.commands.join")) {
                p.sendMessage(new ComponentBuilder("§b[Cameron] §c You don't have permission to execute this command.").create());
                return;
            }
            plugin.getProxy().getPluginManager().dispatchCommand(p, "join Hub", null);
        }

        plugin.getLogger().warning("§b[Cameron] §c'/hub' needs to be executed by a player");
    }
}
