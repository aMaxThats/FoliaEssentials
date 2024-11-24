package at.rotesskelett.foliaEssentials.commands;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import at.rotesskelett.foliaEssentials.utils.Permissions;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InventorySeeCommand implements CommandExecutor {


    private final FoliaEssentials plugin;

    public InventorySeeCommand(FoliaEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Permissions.hasPermission(Permissions.command +"invsee", sender)) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Permissions.console);
                return false;
            }
            if (args.length < 1) {
                sender.sendMessage(FoliaEssentials.prefix + "§cVerwende /invsee spielername");
                return false;
            }

            if (args[0].toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
                sender.sendMessage(FoliaEssentials.prefix + "§cDu kannst dein eigenes Inventar mit dem Command nicht öffnen!");
                return true;
            }
            Player player = (Player) sender;
            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                sender.sendMessage(FoliaEssentials.prefix + "§cDer Spieler §6" + args[0] + " §c ist nicht online.");
                return false;
            }

            RegionScheduler scheduler = plugin.getServer().getRegionScheduler();
            scheduler.execute(plugin, target.getLocation(), () -> {
                // Öffne das Inventar des Zielspielers für den Command-Sender
                player.openInventory(target.getInventory());
                player.sendMessage(FoliaEssentials.prefix + "§fDu hast das Inventar von §e" + target.getName() + "§f geöffnet.");
            });
            return true;
        }
        return false;
    }
}
