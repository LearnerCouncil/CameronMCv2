package rocks.learnercouncil.cameronmc.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import rocks.learnercouncil.cameronmc.bungee.CameronMC;

import java.util.ArrayList;
import java.util.Collections;

public class LCMsgCmd extends Command implements TabExecutor {

    private static final CameronMC plugin = CameronMC.getInstance();

    public LCMsgCmd() {
        super("lc", "lcmsg.use");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String user;
        if(sender instanceof ProxiedPlayer) {
            user = sender.getName();
            if(!sender.hasPermission("lcmsg.use")) {
                sender.sendMessage(new ComponentBuilder("§b[Cameron] §c You don't have permission to execute this command.").create());
                return;
            }
        } else {
            user = "[Server]";
        }
        plugin.getProxy().getPlayers().stream().filter(p -> p.hasPermission("lcmsg.use")).forEach(p -> p.sendMessage(new ComponentBuilder("§b[LC] " + user + ": §f" + String.join(" ", args)).create()));
        plugin.getLogger().info("§b[LC] " + user + ": §f" + String.join(" ", args));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return args[0].equals("") ? Collections.singletonList("<message>") : new ArrayList<>();
    }
}
