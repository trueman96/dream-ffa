package cc.dreamcode.ffa.user.saveinventory.command;

import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.RequireSender;
import cc.dreamcode.command.sender.SenderType;
import cc.dreamcode.ffa.BukkitFFAPlugin;
import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.UserCache;
import cc.dreamcode.ffa.user.saveinventory.menu.SaveInventoryMenu;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequireSender(type = SenderType.PLAYER)
@RequiredArgsConstructor(onConstructor_= @Inject)
@Command(label = "zapiszeq", description = "Zapisuje nam stan aktualnego ekwipunku.")
public class SaveInventoryCommand {

    private final UserCache userCache;

    private final BukkitFFAPlugin plugin;

    private final PluginConfig pluginConfig;

    @Path
    void handle(Player player) {
        final User user = this.userCache.get(player);
        new SaveInventoryMenu(user, player, this.plugin, this.pluginConfig).open();
    }
}
