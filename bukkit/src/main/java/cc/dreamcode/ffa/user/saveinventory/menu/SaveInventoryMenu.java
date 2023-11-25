package cc.dreamcode.ffa.user.saveinventory.menu;

import cc.dreamcode.ffa.BukkitFFAPlugin;
import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.mcversion.VersionProvider;
import cc.dreamcode.ffa.mcversion.api.ItemIdentifyManager;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.saveinventory.UserSavedInventory;
import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class SaveInventoryMenu {

    private final User user;
    private final Player player;

    private final BukkitFFAPlugin plugin;

    private final PluginConfig pluginConfig;

    private final ItemIdentifyManager identifyManager = VersionProvider.getItemIdentifyManager();

    public void open() {
        BukkitMenuBuilder menuBuilder = this.pluginConfig.saveInventoryMenu;
        BukkitMenu menu = menuBuilder.buildEmpty();
        menu.setDisposeWhenClose(true);

        for (Map.Entry<Integer, ItemStack> entry : menuBuilder.getItems().entrySet()) {
            final int slot = entry.getKey();
            final ItemStack item = entry.getValue();

            menu.setItem(slot, ItemBuilder.of(item)
                    .fixColors()
                    .toItemStack(), event -> getInventoryAction(item));
        }

        menu.open(player);
    }

    void getInventoryAction(ItemStack item) {
        final UserSavedInventory savedInventory = user.getSavedInventory();
        if (!identifyManager.getItemIdentityTagName(item, "reset-items").isEmpty()) {
            savedInventory.setInventory(null);
            this.plugin.runAsync(user::save);
        } else if (!identifyManager.getItemIdentityTagName(item, "save-items").isEmpty()) {
            savedInventory.setInventory(player.getInventory().getContents());
            this.plugin.runAsync(user::save);
        }
    }

}