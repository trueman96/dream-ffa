package cc.dreamcode.ffa.user.saveinventory.command;

import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.ffa.BukkitFFAPlugin;
import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.UserCache;
import cc.dreamcode.ffa.user.saveinventory.menu.SaveInventoryMenu;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SaveInventoryCommand extends BukkitCommand {

    private final UserCache userCache;

    private final BukkitFFAPlugin plugin;

    private final PluginConfig pluginConfig;

    @Inject
    public SaveInventoryCommand(UserCache userCache, BukkitFFAPlugin plugin, PluginConfig pluginConfig) {
        super("zapiszeq");
        this.userCache = userCache;
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
    }

    @Override
    public void content(@NonNull CommandSender sender, @NonNull String[] args) {
        if (!(sender instanceof Player player)) {
            return;
        }
        final User user = this.userCache.get(player);
        new SaveInventoryMenu(user, player, this.plugin, this.pluginConfig).open();
    }

    @Override
    public List<String> tab(@NonNull CommandSender sender, @NonNull String[] args) {
        return null;
    }
}
