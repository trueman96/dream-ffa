package cc.dreamcode.ffa.deposit;

import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public class DepositItem {

    private final BukkitNotice notice;
    private final int limit;
    private final ItemStack item;
}
