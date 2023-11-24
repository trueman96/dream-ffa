package cc.dreamcode.ffa.config;

import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import org.bukkit.Location;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * PluginConfig.java
 * Purpose: The PluginConfig is a class utilised by other classes to access plugin-related to things.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-24
 */
@Configuration(child = "config.yml")
@Header("## Dream-FFA (Main-Config) ##")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class PluginConfig extends OkaeriConfig {

    @Comment("Debug pokazuje dodatkowe informacje do konsoli. Lepiej wyłączyć. :P")
    public boolean debug = true;

    @Comment("Uzupełnij poniższe menu danymi.")
    public StorageConfig storageConfig = new StorageConfig("dreamffa");

    @Comment({
            "Itemki które gracz automatycznie ubiera, po wejściu na serwer",
            "Dostepne type: (HAND, OFF_HAND, FEET, LEGS, CHEST, HEAD)"
    })
    public Map<EquipmentSlot, ItemStack> equipmentAfterJoin = new MapBuilder<EquipmentSlot, ItemStack>()
            .put(EquipmentSlot.CHEST, ItemBuilder.of(requireNonNull(XMaterial.DIAMOND_CHESTPLATE.parseMaterial())).toItemStack())
            .build();

    @Comment({
            "Itemki które gracz dostaje do ekwipunku, po wejściu na serwer",
            "Dostepne type: (HAND, OFF_HAND, FEET, LEGS, CHEST, HEAD)"
    })
    public List<ItemStack> itemsAfterJoin = new ListBuilder<ItemStack>()
            .add(ItemBuilder.of(requireNonNull(XMaterial.STONE_SWORD.parseMaterial())).toItemStack())
            .build();

    @Comment("Lokalizacja spawna")
    public Location spawnLocation = new Location(null, 0, 100, 0);

    @Comment("Początkowa wartość punktów")
    private int initialValueOfPoints = 1000;

}
