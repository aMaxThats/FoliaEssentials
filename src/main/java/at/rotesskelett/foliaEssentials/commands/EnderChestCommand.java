package at.rotesskelett.foliaEssentials.commands;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import at.rotesskelett.foliaEssentials.utils.Permissions;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.awt.print.Paper;

public class EnderChestCommand implements CommandExecutor {

    private final FoliaEssentials plugin;



    public EnderChestCommand(FoliaEssentials plugin) {
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Permissions.hasPermission(Permissions.command + "enderchest", sender)) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Permissions.console);
                return false;
            }
            Player player = (Player) sender;
            if (args.length == 0) {


                RegionScheduler scheduler = plugin.getServer().getRegionScheduler();
                scheduler.execute(plugin, player.getLocation(), () -> {
                    // Öffne die Enderchest des Zielspielers für den Command-Sender
                    player.openInventory(player.getEnderChest());
                    player.sendMessage(FoliaEssentials.prefix + "§fDu hast die Enderchest von §e" + player.getName() + "§f geöffnet.");
                });


                return true;
            } else if (args.length >= 1) {
                if (Permissions.hasPermission(Permissions.command + "enderchest.others", sender)) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if (target == null) {
                        sender.sendMessage(FoliaEssentials.prefix + "§cDer Spieler §6" + args[0] + " §c ist nicht online.");
                        return false;
                    }

                    RegionScheduler scheduler = plugin.getServer().getRegionScheduler();
                    scheduler.execute(plugin, target.getLocation(), () -> {
                        // Öffne die Enderchest des Zielspielers für den Command-Sender
                        player.openInventory(target.getEnderChest());
                        player.sendMessage(FoliaEssentials.prefix + "§fDu hast die Enderchest von §e" + target.getName() + "§f geöffnet.");
                    });
                    return true;

                }

            }
        }
        return false;
    }
}