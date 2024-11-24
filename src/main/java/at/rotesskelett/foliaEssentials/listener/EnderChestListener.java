package at.rotesskelett.foliaEssentials.listener;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import at.rotesskelett.foliaEssentials.utils.Permissions;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EnderChestListener implements Listener {

    private final FoliaEssentials plugin;

    public static HashMap<String, Inventory> customEnderChests = new HashMap<>();
    public static HashMap<Inventory, String> customEnderChestsUUID = new HashMap<>();
    public static HashMap<Inventory, Inventory> customEnderChestsFromEnderChest = new HashMap<>();

    public EnderChestListener(FoliaEssentials plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getRegionScheduler().execute(plugin, player.getLocation(), () -> {
            // Check if the custom EnderChest already exists
            if (!customEnderChests.containsKey(player.getUniqueId().toString())) {
                Inventory bigEnderChest = Bukkit.createInventory(player, 54, "§f§l" + player.getName() + "'s §d§l EnderChest");

                // Check if EnderChest data exists in the configuration file
                if (FoliaEssentials.getEnderChestsFileConfiguration().get(player.getUniqueId().toString()) == null) {
                    // Check if the player has permission for a large EnderChest
                    if (player.hasPermission(Permissions.basic + "enderchest.big")) {
                        bigEnderChest.setContents(player.getEnderChest().getContents());
                        // Save EnderChest to file and register in maps
                        saveEnderChestToFile(bigEnderChest, player);
                        customEnderChests.put(player.getUniqueId().toString(), bigEnderChest);
                        customEnderChestsUUID.put(bigEnderChest, player.getUniqueId().toString());
                        customEnderChestsFromEnderChest.put(player.getEnderChest(), bigEnderChest);
                        player.sendMessage(FoliaEssentials.prefix + " §fDu hast nun eine große §d§lEnderChest§f!");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    }


                } else {
                    // Load EnderChest from file
                    bigEnderChest.setContents(itemListFromYaml(FoliaEssentials.getEnderChestsFileConfiguration(), player.getUniqueId() + "", 54));
                    customEnderChests.put(player.getUniqueId().toString(), bigEnderChest);
                    customEnderChestsUUID.put(bigEnderChest, player.getUniqueId().toString());
                    customEnderChestsFromEnderChest.put(player.getEnderChest(), bigEnderChest);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
                    player.sendMessage(FoliaEssentials.prefix + " §fDeine §d§lEnderChest §fwurde geladen!");
                }


            }else {
                customEnderChestsFromEnderChest.put(event.getPlayer().getEnderChest(), customEnderChests.get(event.getPlayer().getUniqueId().toString()));
                player.sendMessage(FoliaEssentials.prefix + " §fDeine §d§lEnderChest§f wurde erneut registriert!");
            }
        });
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // Check if the Inventory opened is an Ender Chest
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            // Get the related EnderChest out of the HashMap
            Inventory customEnderChest = customEnderChestsFromEnderChest.get(event.getInventory());
            // Check if the inventory is null
            if (customEnderChest != null) {
                // Close the Inventory
                event.getPlayer().closeInventory();
                // Cancel opening the EnderChest
                event.setCancelled(true);
                // Get the Region Scheduler for proper Folia handling
                RegionScheduler scheduler = Bukkit.getRegionScheduler();
                // Execute a task with Proper Folia Syntax
                scheduler.execute(plugin, event.getPlayer().getLocation(), () -> {
                    event.getPlayer().openInventory(customEnderChest);
                });
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Get the UUID from the HashMap
        String uuid = customEnderChestsUUID.get(event.getInventory());
        // Check if the uuid is null meaning that the Inventory does not have to bes saved in yaml
        if (uuid != null) {
            // Execute a task with Proper Folia Syntax
            Bukkit.getRegionScheduler().execute(plugin, Bukkit.getPlayer(UUID.fromString(uuid)).getLocation(), () -> {
                // Save the enderChest to the File.
                saveEnderChestToFile(event.getInventory(), Bukkit.getPlayer(UUID.fromString(uuid)));
            });
        }
    }

    private void saveEnderChestToFile(Inventory inventory, Player player) {
        // Get the uuid from the player
        String path = player.getUniqueId() + "";
        // save the inventory to the uuid in yaml
        inventoryItemStacksToYaml(inventory, path);
    }

    private ItemStack[] itemListFromYaml(FileConfiguration fileConfiguration, String key, int size) {
        List<Map<?, ?>> itemList = fileConfiguration.getMapList(key);
        ItemStack[] itemStacks = new ItemStack[size];
        for (int i = 0; i < itemList.size() && i < size; i++) {
            Map<String, Object> itemData = (Map<String, Object>) itemList.get(i);
            itemStacks[i] = ItemStack.deserialize(itemData);
        }
        return itemStacks;
    }

    private void inventoryItemStacksToYaml(Inventory inventory, String key) {
        List<Map<?, ?>> itemList = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                itemList.add(item.serialize());
            }
        }
        FoliaEssentials.getEnderChestsFileConfiguration().set(key, itemList);
        FoliaEssentials.saveEnderChestFile();
    }
}
