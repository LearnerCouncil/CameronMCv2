package rocks.learnercouncil.cameronmc.spigot.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import rocks.learnercouncil.cameronmc.spigot.CameronMC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkullCmd implements CommandExecutor, TabCompleter {
    private static final CameronMC plugin = CameronMC.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("skull")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(!p.hasPermission("cameron.commands.skull")) {
                    p.sendMessage("§b[Cameron] §c You don't have permission to execute this command.");
                    return true;
                }
                if(args.length < 1) {
                    p.sendMessage("§b[Cameron] §cYou must sepcify a player name.");
                    return true;
                }
                if(args.length == 1) {
                    getSkull(args[0], p);
                    p.sendMessage("§b[Cameron] §ePoof! You now have the head of " + args[0] + ".");
                    return true;
                }
                p.sendMessage("§b[Cameron] §cToo many arguments!");
                return true;
            }
            plugin.getLogger().warning("[Cameron] Needs to be executed by a player");
            return true;
        }
        return false;
    }

    private void getSkull(String name, Player p) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        assert skullMeta != null;
        new BukkitRunnable() {
            @Override
            public void run() {
                skullMeta.setDisplayName("§r" + name + "'s Head");
                skullMeta.setOwner(name);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        skull.setItemMeta(skullMeta);
                        p.getInventory().addItem(skull);
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1) {
            return args[0].equals("") ? Collections.singletonList("<username>") : new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
