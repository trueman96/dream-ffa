package cc.dreamcode.ffa.mcversion.v1_19_R2;

import cc.dreamcode.ffa.mcversion.api.ItemIdentifyManager;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class V1_19_R2_ItemIdentifyManager implements ItemIdentifyManager {
    public String getItemIdentityTagName(ItemStack item, String key){
        net.minecraft.world.item.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.v();
        return compound.l(key);
    }

    public ItemStack wrapIdentity(ItemStack item, String key, String identity) {
        net.minecraft.world.item.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.v();
        compound.a(key, identity);
        itemClone.c(compound);
        return CraftItemStack.asBukkitCopy(itemClone);
    }

    public boolean compareIdentity(ItemStack item, String identity) {
        net.minecraft.world.item.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.v();
        return compound != null && compound.e(identity);
    }
}
