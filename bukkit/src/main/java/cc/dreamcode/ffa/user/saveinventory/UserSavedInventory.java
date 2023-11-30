package cc.dreamcode.ffa.user.saveinventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSavedInventory {

    private ItemStack[] inventory;

}
