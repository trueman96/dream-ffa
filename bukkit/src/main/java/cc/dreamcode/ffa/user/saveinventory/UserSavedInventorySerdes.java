package cc.dreamcode.ffa.user.saveinventory;

import cc.dreamcode.ffa.user.saveinventory.UserSavedInventory;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserSavedInventorySerdes implements ObjectSerializer<UserSavedInventory> {

    @Override
    public boolean supports(@NonNull Class<? super UserSavedInventory> type) {
        return UserSavedInventory.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull UserSavedInventory object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("inventory", itemStackArrayToBase64(object.getInventory()));
    }

    @Override
    @SneakyThrows
    public UserSavedInventory deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new UserSavedInventory(
                itemStackArrayFromBase64(data.get("inventory", String.class))
        );
    }

    public ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            if (data.equalsIgnoreCase(""))
                return null;

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
