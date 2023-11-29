package cc.dreamcode.ffa.user.task;

import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

/**
 * UserItemsDepositTask.java
 * Purpose: The UserItemsDepositTask is a class that takes excess items from config.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-25
 */
@RequiredArgsConstructor(onConstructor_= @Inject)
@Scheduler(delay = 0L, interval = 60L)
public class UserItemsDepositTask implements Runnable {

    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @Override
    public void run() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            for (final Map.Entry<Integer, Material> entry : this.pluginConfig.depositItemsMap.entrySet()) {
                final int depositLimit = entry.getKey();
                final ItemStack depositItem = new ItemStack(entry.getValue());
                final int itemToRemove = countItemsIgnoreItemMeta(player, depositItem);

                if (itemToRemove > depositLimit) {
                    final int amountToRemove = itemToRemove - depositLimit;
                    ItemStack itemStack = new ItemStack(depositItem);
                    itemStack.setAmount(amountToRemove);
                    removeItemIgnoreItemMeta(player, itemStack);

                    this.messageConfig.depositMessages.get(depositItem.getType())
                            .send(player, new MapBuilder<String, Object>()
                                    .put("amount", amountToRemove)
                                    .build());
                }
            }
        }
    }

    public static void removeItemIgnoreItemMeta(Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        int amountLeft = item.getAmount();
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            if (itemStack != null && itemStack.getType() == item.getType()) {
                if (amountLeft >= itemStack.getAmount()) {
                    amountLeft -= itemStack.getAmount();

                    player.getInventory().setItem(i, new ItemStack(Material.AIR));
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amountLeft);
                }

                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }

    public static int countItemsIgnoreItemMeta(Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return 0;
        }

        PlayerInventory inventory = player.getInventory();
        int count = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            if (itemStack == null || item.getType() != itemStack.getType()) {
                continue;
            }

            count += itemStack.getAmount();
        }

        return count;
    }
}
