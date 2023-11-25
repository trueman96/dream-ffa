package cc.dreamcode.ffa.mcversion.v1_16_R3;

import cc.dreamcode.ffa.mcversion.api.ItemIdentifyManager;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class V1_16_R3_ItemIdentifyManager implements ItemIdentifyManager {
    public String getItemIdentityTagName(ItemStack item, String key){
        net.minecraft.server.v1_16_R3.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        return compound.getString(key);
    }

    public ItemStack wrapIdentity(ItemStack item, String key, String identity) {
        net.minecraft.server.v1_16_R3.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.getTag();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        compound.setString(key, identity);
        itemClone.setTag(compound);
        return CraftItemStack.asBukkitCopy(itemClone);
    }

    public boolean compareIdentity(ItemStack item, String identity) {
        net.minecraft.server.v1_16_R3.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.getTag();
        return compound != null && compound.hasKey(identity);
    }
}
