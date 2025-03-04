package parallelmc.parallelutils.modules.paralleltowns.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import parallelmc.parallelutils.modules.paralleltowns.ParallelTowns;
import parallelmc.parallelutils.modules.paralleltowns.gui.GUIInventory;

public class OnMenuInteract implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        GUIInventory inv = ParallelTowns.get().guiManager.getOpenInventory(player);
        if (inv != null && (inv.inventory.equals(event.getClickedInventory()) || inv.inventory.equals(event.getInventory()))) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            // don't fire event for "placeholder" items
            if (item == null || item.getType() == Material.AIR || item.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE) {
                return;
            }
            inv.onSlotClicked(player, event.getRawSlot(), item);
        }
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent event) {
        Player player = (Player)event.getWhoClicked();
        GUIInventory inv = ParallelTowns.get().guiManager.getOpenInventory(player);
        if (inv != null && inv.inventory.equals(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMenuMoveItem(InventoryMoveItemEvent event) {
        if (ParallelTowns.get().guiManager.openInventoryExists(event.getDestination())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCloseMenu(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        GUIInventory inv = ParallelTowns.get().guiManager.getOpenInventory(player);
        if (inv != null && event.getInventory().equals(inv.inventory)) {
            ParallelTowns.get().guiManager.closeMenu(player);
        }
    }

    @EventHandler
    public void onLeaveWhileInMenu(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GUIInventory inv = ParallelTowns.get().guiManager.getOpenInventory(player);
        if (inv != null) {
            ParallelTowns.get().guiManager.closeMenu(player);
        }
    }

}
