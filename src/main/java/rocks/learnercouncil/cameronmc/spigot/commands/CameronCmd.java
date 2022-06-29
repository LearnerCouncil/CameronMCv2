package rocks.learnercouncil.cameronmc.spigot.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import rocks.learnercouncil.cameronmc.spigot.CameronMC;
import rocks.learnercouncil.cameronmc.spigot.util.PluginMessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CameronCmd implements CommandExecutor, TabCompleter {
    private static final CameronMC plugin = CameronMC.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("cameron")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(!p.hasPermission("cameron.commands.cameron")) {
                    p.sendMessage("§b[Cameron] §c You don't have permission to execute this command.");
                    return true;
                }
                if(args.length >= 1) {
                    if(args[0].equalsIgnoreCase("setnavigator")) {
                        if(!p.hasPermission("cameron.commands.cameron.setnavigator")) {
                            p.sendMessage("§b[Cameron] §c You don't have permission to execute this command.");
                            return true;
                        }
                        if(args.length == 2) {
                            Location loc = p.getLocation();
                            PluginMessageHandler.sendPluginMessage(p, "set-navigator",
                                    args[1],
                                    p.getUniqueId().toString(),
                                    loc.getWorld().getName(),
                                    String.valueOf(loc.getX()),
                                    String.valueOf(loc.getY()),
                                    String.valueOf(loc.getZ()),
                                    String.valueOf(loc.getPitch()),
                                    String.valueOf(loc.getYaw()));
                            p.sendMessage("§b[Cameron] §eNavigator location set. (World: " + loc.getWorld().getName() + ", XYZ: " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", Angle: " + loc.getYaw() + ", " + loc.getPitch() + ")");
                            return true;
                        }
                        p.sendMessage("§b[Cameron] §cYou must specify a navigator location");
                        return true;
                    } else if(args[0].equalsIgnoreCase("debug")) {
                        if(!p.hasPermission("cameron.commands.cameron.debug")) {
                            p.sendMessage("§b[Cameron] §c You don't have permission to execute this command.");
                            return true;
                        }
                        if(args.length == 1) {
                            if(plugin.getLogger().getLevel() == Level.INFO) {
                                plugin.getLogger().setLevel(Level.FINE);
                                p.sendMessage("§b[Cameron] §aDebug logging enabled.");
                            } else {
                                plugin.getLogger().setLevel(Level.INFO);
                                p.sendMessage("§b[Cameron] §aDebug logging disabled.");
                            }
                            return true;
                        }
                    }
                    p.sendMessage("§b[Cameron] §cInvalid arguments");
                    return true;
                }
                p.sendMessage("§b[Cameron] §cNot enough arguments.");
                return true;
            }
            CameronMC.getInstance().getLogger().warning("[Cameron] Must be executed by a player.");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arguments = new ArrayList<>();
        List<String> completions = new ArrayList<>();
        Player p = null;
        if(sender instanceof Player) {
            p = (Player) sender;
        }
        if (args.length == 1) {
            if(p != null) {
                if(p.hasPermission("cameron.commands.cameron.setnavigator"))
                    arguments.add("setnavigator");
                if(p.hasPermission("cameron.commands.cameron.debug"))
                    arguments.add("debug");
            } else {
                arguments.add("setnavgator");
                arguments.add("debug");
            }
        }
        if(args.length > 0) {
            StringUtil.copyPartialMatches(args[args.length - 1], arguments, completions);
            return completions;
        }
        return new ArrayList<>();
    }
}
