package at.rotesskelett.foliaEssentials;

import at.rotesskelett.foliaEssentials.commands.EnderChestCommand;
import at.rotesskelett.foliaEssentials.commands.FolieEssentialsCommand;
import at.rotesskelett.foliaEssentials.commands.InventorySeeCommand;
import at.rotesskelett.foliaEssentials.listener.EnderChestListener;
import at.rotesskelett.foliaEssentials.listener.InventoryAllowListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class FoliaEssentials extends JavaPlugin {

    private static File enderChestFile;
    private static FileConfiguration enderChestsFileConfiguration;

    public static String prefix = "§f§l[§aFoliaEssentials§f§l] §r";

    @Override
    public void onEnable() {

        // EnderChestConfig LOAD
        createEnderChestFile();
        saveEnderChestFile();


        Bukkit.getConsoleSender().sendMessage(prefix + "Plugin Started");
        getCommand("enderchest").setExecutor(new EnderChestCommand(this));
        getCommand("foliaessentials").setExecutor(new FolieEssentialsCommand());
        getCommand("invsee").setExecutor(new InventorySeeCommand(this));
        Bukkit.getPluginManager().registerEvents(new InventoryAllowListener(),this);
        Bukkit.getPluginManager().registerEvents(new EnderChestListener(this), this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static void saveEnderChestFile() {
        try {
            enderChestsFileConfiguration.save(enderChestFile);
        }catch (IOException e) {

        }
    }

    private void createEnderChestFile() {
        enderChestFile = new File(getDataFolder(), "savings/enderchest.yml");
        if (!enderChestFile.exists()) {
            enderChestFile.getParentFile().mkdirs();
            saveResource("savings/enderchest.yml", false);
        }

        enderChestsFileConfiguration = new YamlConfiguration();
        try {
            enderChestsFileConfiguration.load(enderChestFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public static FileConfiguration getEnderChestsFileConfiguration() {
        return enderChestsFileConfiguration;
    }
}
