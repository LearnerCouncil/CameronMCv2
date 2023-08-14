package rocks.learnercouncil.cameronmc.spigot;

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
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Selection {
    private static final Map<Player, Selection> selections = new HashMap<>();
    public static Optional<Selection> getSelection(Player player) {
        if(!selections.containsKey(player)) return Optional.empty();
        Selection selection = selections.get(player);
        if(selection.selectionBoxMax == null || selection.selectionBoxMin == null) return Optional.empty();
        return Optional.of(selection);
    }
    private ItemStack selector = null;
    public Optional<ItemStack> getSelector() {
        return Optional.ofNullable(selector);
    }
    private Vector selectionBoxMax, selectionBoxMin;
    public BoundingBox getSelectionBox() {
        if(selectionBoxMax == null || selectionBoxMin == null) throw new NullPointerException("Selection box was retrieved, but was null.");
        return BoundingBox.of(selectionBoxMin, selectionBoxMax);
    }

    public static void giveSelector(Player player) {
        if(!selections.containsKey(player)) selections.put(player, new Selection());
        Selection selection = selections.get(player);
        selection.getSelector().ifPresent(i -> player.getInventory().remove(i));
        selection.selector = new ItemStack(Material.DIAMOND_AXE, 1);
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
                selection.selectionBoxMin = Objects.requireNonNull(event.getClickedBlock()).getLocation().toVector();
                messageComponent.setText(String.format(message, 1));
            }
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                selection.selectionBoxMax = blockLocation;
                messageComponent.setText(String.format(message, 1));
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, messageComponent);
        }
    }
}
