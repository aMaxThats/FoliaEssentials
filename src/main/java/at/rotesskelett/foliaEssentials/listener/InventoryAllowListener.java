package at.rotesskelett.foliaEssentials.listener;

import at.rotesskelett.foliaEssentials.utils.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryAllowListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Prüfen, ob ein Inventar eines anderen Spielers betroffen ist
        if (isOtherPlayerInventory(event)) {
            if (event.getClickedInventory().getType() == InventoryType.ENDER_CHEST) {
                if (Permissions.hasPermission(Permissions.command+"enderchest.other.move", (Player) event.getWhoClicked()))  {
                    return;
                }
            }else if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                if (Permissions.hasPermission(Permissions.command+"invsee.other.move", (Player) event.getWhoClicked()))  {
                    return;
                }
            }
            event.setCancelled(true); // Aktion abbrechen
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // Prüfen, ob ein Inventar eines anderen Spielers betroffen ist
        if (isOtherPlayerInventory(event)) {
            if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
                if (Permissions.hasPermission(Permissions.command+"enderchest.other.move", (Player) event.getWhoClicked()))  {
                    return;
                }
            } else if (event.getInventory().getType() == InventoryType.PLAYER) {
                if (Permissions.hasPermission(Permissions.command+"invsee.other.move", (Player) event.getWhoClicked()))  {
                    return;
                }
            }
            event.setCancelled(true); // Aktion abbrechen
        }
    }

    private boolean isOtherPlayerInventory(InventoryClickEvent event) {
        // Beispiel: Inventar des Besitzers überprüfen (benutzerdefinierte Logik einfügen)
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player owner = (org.bukkit.entity.Player) event.getClickedInventory().getHolder();
            return !event.getWhoClicked().getUniqueId().equals(owner.getUniqueId());
        }
        return false;
    }

    private boolean isOtherPlayerInventory(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof org.bukkit.entity.Player) {
            org.bukkit.entity.Player owner = (org.bukkit.entity.Player) event.getInventory().getHolder();
            return !event.getWhoClicked().getUniqueId().equals(owner.getUniqueId());
        }
        return false;
    }

}
