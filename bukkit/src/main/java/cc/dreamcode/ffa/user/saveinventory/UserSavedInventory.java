package cc.dreamcode.ffa.user.saveinventory;

import lombok.Data;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Data
public class UserSavedInventory {

    private transient ItemStack[] inventory;
    private String serializedInventory;

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
        this.serializedInventory = itemStackArrayToBase64(inventory);
    }

    @SneakyThrows
    public ItemStack[] getInventory() {
        if (inventory == null) {
            this.inventory = itemStackArrayFromBase64(serializedInventory);
        }
        return inventory;
    }

    public ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            if (data.equalsIgnoreCase(""))
                return new ItemStack[0];

            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack) dataInput.readObject();

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    public String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            if (items == null)
                return "";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (ItemStack item : items)
                dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            return "";
        }
    }
}
