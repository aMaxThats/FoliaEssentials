package at.rotesskelett.foliaEssentials.commands;


import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.entity.Player;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import at.rotesskelett.foliaEssentials.utils.Permissions;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class VanischCommand implements CommandExecutor {

    private final FoliaEssentials plugin;
    public static HashMap<Player, Integer> vanischedPlayers = new HashMap<>();

    public VanischCommand(FoliaEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Permissions.hasPermission(Permissions.command+"vanish", sender)) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Permissions.console);
                return false;
            }
            Player player = (Player) sender;
            if (vanischedPlayers.get(player) == null) {
                // Get the highest vanish level from the user.
                player.getScheduler().execute(plugin, () -> {

                    // Setze den Namen in der Tab-Liste
                    player.setPlayerListName(player.getName() + " §f§l[§a§lV§f§l]");
                    // Setze den Namen über dem Kopf des Spielers
                    player.setCustomName(player.getName() + " §f§l[§a§lV§f§l]");
                    player.setDisplayName(player.getName() + " §f§l[§a§lV§f§l]");
                    changePlayerName(player,player.getName() + " §f§l[§a§lV§f§l]");

                    player.setCustomNameVisible(true); // Stelle sicher, dass der Name sichtbar ist
                }, null, 0);


                Bukkit.getRegionScheduler().execute(plugin,player.getLocation(), () -> {
                    int vanishLevel = getVanishLevel(player);
                    vanischedPlayers.put(player,vanishLevel);
                    for (Player target: Bukkit.getServer().getOnlinePlayers()) {
                        vanishPlayer(player,target,vanishLevel);
                    }

                    player.sendMessage(FoliaEssentials.prefix +"§fDu bist nun unsichtbar mit dem Level §6" + vanishLevel +"§f!");


                });
            }else {



                Bukkit.getRegionScheduler().execute(plugin,player.getLocation(), () -> {
                    vanischedPlayers.remove(player);

                    player.getScheduler().execute(plugin, () -> {
                        player.setDisplayName(player.getName());
                        player.setCustomName(player.getName());
                        player.setCustomNameVisible(true);
                    }, null, 0);

                    for (Player target: Bukkit.getServer().getOnlinePlayers()) {
                        target.getScheduler().execute(plugin, () -> {
                            target.hidePlayer(player);
                            target.showPlayer(player);
                        }, null,0);
                    }

                    player.sendMessage(FoliaEssentials.prefix +"§fDu bist nun nicht mehr unsichtbar!");


                });
            }
        }

        return false;
    }

    public void vanishPlayer(Player vanishedPlayer, Player target, int vanishLevel) {
        target.getScheduler().execute(plugin, () -> {
            if (getVanishLevel(target) < vanishLevel) {
                target.hidePlayer(plugin,vanishedPlayer);
            }else {
                target.hidePlayer(plugin,vanishedPlayer);
                target.showPlayer(plugin,vanishedPlayer);
            }
        }, null,0);
    }
    public int getVanishLevel(Player player) {
        int highestLevel = -1; // Standardwert, falls keine Berechtigung gefunden wird
        if (player.hasPermission(Permissions.basic+"vanish")) {
            highestLevel = 1;
        }
        for (PermissionAttachmentInfo permInfo : player.getEffectivePermissions()) {
            String permission = permInfo.getPermission();

            // Überprüfen, ob die Berechtigung mit "vanished.level." beginnt
            if (permission.startsWith(Permissions.basic+"vanished.level.")) {
                try {
                    // Extrahiere die Zahl nach "vanished.level."
                    String levelString = permission.substring((Permissions.basic+"vanished.level.").length());
                    int level = Integer.parseInt(levelString);

                    // Überprüfe, ob die Berechtigung aktiv ist
                    if (permInfo.getValue()) {
                        // Höchsten Level speichern
                        highestLevel = Math.max(highestLevel, level);
                    }
                } catch (NumberFormatException e) {
                    // Falls der Teil nach "vanished.level." keine Zahl ist

                }
            }
        }
        return highestLevel;
    }


    public void changePlayerName(Player player, String newName) {
        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        profile.getProperties().clear(); // Entferne existierende Eigenschaften

        try {
            // Ändere den Namen im Profil
            PacketContainer packet = ProtocolLibrary.getProtocolManager()
                    .createPacket(com.comphenix.protocol.PacketType.Play.Server.PLAYER_INFO);

            packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
            packet.getPlayerInfoDataLists().write(0, Arrays.asList(
                    new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(newName))
            ));

            // Sende das manipulierte Paket an alle Spieler
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
