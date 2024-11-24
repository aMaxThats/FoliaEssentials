package at.rotesskelett.foliaEssentials.utils;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permissions {

    public static final String command = "foliaessetials.command.";

    public static final String basic = "foliaessetials.";

    public static final String console = FoliaEssentials.prefix + "§cDu musst ein Spieler seinen um diesen Command auszuführen";

    public static boolean hasPermission(String permission, CommandSender sender) {
        if (sender.hasPermission(permission) || sender.isOp() || sender.hasPermission("foliaessetials.*") || sender.hasPermission("foliaessetials.command.*" )) {
            return true;
        }else {
            sender.sendMessage(FoliaEssentials.prefix + "§cDu hast keine Berechtigung diesen Command auszuführen! §7(" + permission +")!");
            return false;
        }
    }

    public static boolean hasPermission(String permission, Player sender) {
        if (sender.hasPermission(permission) || sender.isOp() || sender.hasPermission("foliaessetials.*") || sender.hasPermission("foliaessetials.command.*" )) {
            return true;
        }else {
            sender.sendMessage(FoliaEssentials.prefix + "§cDu hast keine Berechtigung diesen Command auszuführen! §7(" + permission +")!");
            return false;
        }
    }



}
