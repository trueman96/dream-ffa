package cc.dreamcode.ffa.user.saveinventory.util;

import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.saveinventory.UserSavedInventory;
import cc.dreamcode.utilities.bukkit.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class InventoryUtil {

    public static void setupInventory(Player player, User user, PluginConfig pluginConfig) {
        player.getInventory().clear();

        final PlayerInventory inventory = player.getInventory();
        for (Map.Entry<EquipmentSlot, ItemStack> entry : pluginConfig.equipmentAfterJoin.entrySet()) {
            EquipmentSlot key = entry.getKey();
            ItemStack value = entry.getValue();
            switch (key) {
                case HAND:
                    inventory.setItemInHand(value);
                    break;
                case FEET:
                    inventory.setBoots(value);
                    break;
                case LEGS:
                    inventory.setLeggings(value);
                    break;
                case CHEST:
                    inventory.setChestplate(value);
                    break;
                case HEAD:
                    inventory.setHelmet(value);
                    break;
            }
        }
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        ItemUtil.addItems(pluginConfig.itemsAfterJoin, inventory);
        final UserSavedInventory savedInventory = user.getSavedInventory();
        if (savedInventory != null && savedInventory.getInventory() != null) {
            player.getInventory().setContents(savedInventory.getInventory());
        }
    }
}
