package at.rotesskelett.foliaEssentials.listener;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import at.rotesskelett.foliaEssentials.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryAllowListener implements Listener {

    private final FoliaEssentials plugin;

    public InventoryAllowListener(FoliaEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Spieler-Objekt holen und sicherstellen, dass es im richtigen Thread ausgeführt wird
        Player player = (Player) event.getWhoClicked();

        Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
            // Prüfen, ob ein Inventar eines anderen Spielers betroffen ist
            if (isOtherPlayerInventory(event)) {
                if (event.getClickedInventory().getType() == InventoryType.ENDER_CHEST) {
                    if (EnderChestListener.customEnderChestsUUID.get(event.getClickedInventory()) == null
                            || EnderChestListener.customEnderChestsUUID.get(event.getClickedInventory()).equals(player.getUniqueId().toString())) {
                        return;
                    }

                    if (Permissions.hasPermission(Permissions.command + "enderchest.other.move", player)) {
                        return;
                    }
                } else if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
                    if (Permissions.hasPermission(Permissions.command + "invsee.other.move", player)) {
                        return;
                    }
                } else if (EnderChestListener.customEnderChestsUUID.get(event.getClickedInventory()) != null
                        && !EnderChestListener.customEnderChestsUUID.get(event.getClickedInventory()).equals(player.getUniqueId().toString())) {
                    if (Permissions.hasPermission(Permissions.command + "enderchest.other.move", player)) {
                        return;
                    }
                }

                player.closeInventory();
                event.setCancelled(true); // Aktion abbrechen
                player.sendMessage(ChatColor.RED + "Du kannst dieses Inventar nicht bearbeiten.");
            }
        });
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // Spieler-Objekt holen und sicherstellen, dass es im richtigen Thread ausgeführt wird
        Player player = (Player) event.getWhoClicked();

        Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
            if (isOtherPlayerInventory(event)) {
                if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
                    if (EnderChestListener.customEnderChestsUUID.get(event.getInventory()) == null
                            || EnderChestListener.customEnderChestsUUID.get(event.getInventory()).equals(player.getUniqueId().toString())) {
                        return;
                    }

                    if (Permissions.hasPermission(Permissions.command + "enderchest.other.move", player)) {
                        return;
                    }
                } else if (event.getInventory().getType() == InventoryType.PLAYER) {
                    if (Permissions.hasPermission(Permissions.command + "invsee.other.move", player)) {
                        return;
                    }
                } else if (EnderChestListener.customEnderChestsUUID.get(event.getInventory()) != null
                        && !EnderChestListener.customEnderChestsUUID.get(event.getInventory()).equals(player.getUniqueId().toString())) {
                    if (Permissions.hasPermission(Permissions.command + "enderchest.other.move", player)) {
                        return;
                    }
                }

                event.setCancelled(true); // Aktion abbrechen
                player.sendMessage(ChatColor.RED + "Du kannst dieses Inventar nicht bearbeiten.");
            }
        });
    }

    private boolean isOtherPlayerInventory(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Player) {
            Player owner = (Player) event.getClickedInventory().getHolder();
            return !event.getWhoClicked().getUniqueId().equals(owner.getUniqueId());
        }
        if (event.getClickedInventory().getType() == InventoryType.ENDER_CHEST) {
            return event.getWhoClicked().getEnderChest() != event.getClickedInventory();
        }
        return false;
    }

    private boolean isOtherPlayerInventory(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof Player) {
            Player owner = (Player) event.getInventory().getHolder();
            return !event.getWhoClicked().getUniqueId().equals(owner.getUniqueId());
        }
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            return event.getWhoClicked().getEnderChest() != event.getInventory();
        }
        return false;
    }
}
