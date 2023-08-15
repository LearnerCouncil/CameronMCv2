package rocks.learnercouncil.cameronmc.spigot.portals;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;

public class Selection {
    private static final Map<Player, Selection> selections = new HashMap<>();
    public static Optional<Selection> getSelection(Player player) {
        if(!selections.containsKey(player)) return Optional.empty();
        Selection selection = selections.get(player);
        if(selection.corner1 == null || selection.corner2 == null) return Optional.empty();
        return Optional.of(selection);
    }
    private ItemStack selector = null;
    public Optional<ItemStack> getSelector() {
        return Optional.ofNullable(selector);
    }
    private Vector corner1, corner2;
    public BoundingBox getSelectionBox() {
        if(corner1 == null || corner2 == null) throw new NullPointerException("Selection box was retrieved, but was null.");
        Vector max = Vector.getMaximum(corner2, corner1);
        Vector min = Vector.getMinimum(corner2, corner1);
        return BoundingBox.of(min, max.add(new Vector(1, 1, 1)));
    }

    public static void giveSelector(Player player) {
        if(!selections.containsKey(player)) selections.put(player, new Selection());
        Selection selection = selections.get(player);
        selection.getSelector().ifPresent(i -> player.getInventory().remove(i));
        ItemStack selector = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta selectorMeta = selector.getItemMeta();
        Objects.requireNonNull(selectorMeta, "Selector ItemMeta is null.");
        selectorMeta.setDisplayName(ChatColor.RESET.toString() + ChatColor.GREEN + "Portal Selector");
        selector.setItemMeta(selectorMeta);
        selection.selector = selector;
        player.getInventory().addItem(selection.selector);
    }


    public static class Events implements Listener {

        @EventHandler
        public void onPLayerInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            if(!selections.containsKey(player)) return;
            Selection selection = selections.get(player);
            if(!selection.getSelector().isPresent()) return;
            if(!Objects.equals(event.getItem(), selection.selector)) return;
            if(event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

            Vector blockLocation = Objects.requireNonNull(event.getClickedBlock()).getLocation().toVector();
            String message = "Position #%d Set. ("
                    + blockLocation.getBlockX() + ", "
                    + blockLocation.getBlockY() + ", "
                    + blockLocation.getBlockZ() + ")";
            TextComponent messageComponent = new TextComponent();
            messageComponent.setColor(ChatColor.GREEN);

            if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                selection.corner2 = Objects.requireNonNull(event.getClickedBlock()).getLocation().toVector();
                messageComponent.setText(String.format(message, 1));
            }
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                selection.corner1 = blockLocation;
                messageComponent.setText(String.format(message, 2));
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, messageComponent);
            event.setCancelled(true);
        }
    }
}
