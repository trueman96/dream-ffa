package cc.dreamcode.ffa.mcversion.v1_18_R2;

import cc.dreamcode.ffa.mcversion.api.ItemIdentifyManager;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class V1_18_R2_ItemIdentifyManager implements ItemIdentifyManager {
    public String getItemIdentityTagName(ItemStack item, String key){
        net.minecraft.world.item.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.t();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        return compound.l(key);
    }

    public ItemStack wrapIdentity(ItemStack item, String key, String identity) {
        net.minecraft.world.item.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.t();
        if (compound == null) {
            compound = new NBTTagCompound();
        }
        compound.a(key, identity);
        itemClone.c(compound);
        return CraftItemStack.asBukkitCopy(itemClone);
    }

    public boolean compareIdentity(ItemStack item, String identity) {
        net.minecraft.world.item.ItemStack itemClone = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = itemClone.t();
        return compound != null && compound.e(identity);
    }
}
