package cc.dreamcode.ffa.mcversion.api;

import org.bukkit.inventory.ItemStack;

public interface ItemIdentifyManager {
    String getItemIdentityTagName(ItemStack item, String key);

    ItemStack wrapIdentity(ItemStack item, String key, String identity);

    boolean compareIdentity(ItemStack item, String identity);
}
