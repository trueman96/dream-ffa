package cc.dreamcode.ffa.user.saveinventory.menu;

import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.saveinventory.UserSavedInventory;
import cc.dreamcode.ffa.user.saveinventory.util.InventoryUtil;
import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.tasker.core.Tasker;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class SaveInventoryMenu {

    private final User user;
    private final Player player;

    private final Tasker tasker;

    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    public void open() {
        BukkitMenuBuilder menuBuilder = this.pluginConfig.saveInventoryMenu;
        BukkitMenu menu = menuBuilder.buildEmpty();
        menu.setDisposeWhenClose(true);

        for (Map.Entry<Integer, ItemStack> entry : menuBuilder.getItems().entrySet()) {
            final int slot = entry.getKey();
            final ItemStack item = entry.getValue();

            menu.setItem(slot, ItemBuilder.of(item)
                    .fixColors()
                    .toItemStack(), event -> {
                System.out.println("click");
                if (!item.hasItemMeta()) {
                    return;
                }
                System.out.println("click-pass");
                final UserSavedInventory savedInventory = user.getSavedInventory();
                if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_DESTROYS)) {
                    savedInventory.setInventory(null);
                    this.messageConfig.resetedInventory.send(player);
                    this.tasker.newSharedChain(player.getUniqueId().toString())
                            .async(() -> user.save())
                            .execute();
                    InventoryUtil.setupInventory(player, user, this.pluginConfig);
                } else if (item.getItemMeta().hasItemFlag(ItemFlag.HIDE_ENCHANTS)) {
                    savedInventory.setInventory(player.getInventory().getContents());
                    this.messageConfig.savedInventory.send(player, new MapBuilder<String, Object>()
                            .put("items-saved", savedInventory.getInventory().length)
                            .build());
                    this.tasker.newSharedChain(player.getUniqueId().toString())
                            .async(() -> user.save())
                            .execute();
                }
            });
        }

        menu.open(player);
    }

}