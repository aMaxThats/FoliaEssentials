package at.rotesskelett.foliaEssentials.listener;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import at.rotesskelett.foliaEssentials.utils.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EnderChestListener implements Listener {

    private final FoliaEssentials plugin;

    public static HashMap<UUID, Inventory> customEnderChests = new HashMap<UUID, Inventory>();

    public EnderChestListener(FoliaEssentials plugin) {
        this.plugin = plugin;
    }

    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (customEnderChests.get(player.getUniqueId()) == null && player.hasPermission(Permissions.basic + "enderchest.big")) {
            Inventory bigEnderChest = Bukkit.createInventory(event.getPlayer(), 56);
            if (FoliaEssentials.getEnderChestsFileConfiguration().get(player.getUniqueId() +"") == null) {
                bigEnderChest.setContents(player.getEnderChest().getContents());
                saveEnderChestToFile(bigEnderChest, player);
                customEnderChests.put(player.getUniqueId(), bigEnderChest);
                player.sendMessage(FoliaEssentials.prefix + " §fDu hast nun eine §a§fGROßE §d§lEnderChest§f!");
            } else {
                bigEnderChest.setContents(itemListFromYaml(FoliaEssentials.getEnderChestsFileConfiguration(),player.getUniqueId() + "", 56));
                customEnderChests.put(player.getUniqueId(), bigEnderChest);
                player.sendMessage(FoliaEssentials.prefix + " §fDeine §d§lEnderChest §fwurde geladen!");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
            }

        }
    }





    private void saveEnderChestToFile(Inventory inventory, Player player) {
        String path = player.getUniqueId() + "";
        inventoryItemStacksToYaml(inventory, path);
    }


    private ItemStack[] itemListFromYaml(FileConfiguration fileConfiguration, String key, int size) {
        // Retrieve the list of serialized items from YAML
        List<Map<?, ?>> itemList = fileConfiguration.getMapList(key);

        // Initialize the ItemStack array with the specified size
        ItemStack[] itemStacks = new ItemStack[size];

        // Loop through the itemList and deserialize each item
        for (int i = 0; i < itemList.size() && i < size; i++) {
            // Cast each entry to a Map<String, Object> for deserialization
            Map<String, Object> itemData = (Map<String, Object>) itemList.get(i);

            // Deserialize the item data to an ItemStack and add it to the array
            itemStacks[i] = ItemStack.deserialize(itemData);

        }

        // Return the array of ItemStacks
        return itemStacks;
    }

    private void inventoryItemStacksToYaml(Inventory inventory, String key) {
        // Saving inventory data to YAML
        List<Map<?, ?>> itemList = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                itemList.add(item.serialize()); // Serialize item data
            }
        }
        FoliaEssentials.getEnderChestsFileConfiguration().set(key, itemList);
        FoliaEssentials.saveEnderChestFile();
    }
}
