package cc.dreamcode.ffa.deposit;

import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import eu.okaeri.configs.OkaeriConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.inventory.ItemStack;

@Data
@EqualsAndHashCode(callSuper = false)
public class DepositItem extends OkaeriConfig {

    private final BukkitNotice notice;
    private final int limit;
    private final ItemStack item;
}
