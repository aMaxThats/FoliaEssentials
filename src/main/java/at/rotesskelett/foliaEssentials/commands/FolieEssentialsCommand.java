package at.rotesskelett.foliaEssentials.commands;

import at.rotesskelett.foliaEssentials.FoliaEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class FolieEssentialsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(FoliaEssentials.prefix + " Version: 1.0, gecoded von rotesSkelett");
        return false;
    }
}
