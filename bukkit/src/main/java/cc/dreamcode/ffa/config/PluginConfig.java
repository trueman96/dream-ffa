package cc.dreamcode.ffa.config;

import cc.dreamcode.ffa.mcversion.VersionProvider;
import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.utilities.builder.ListBuilder;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import com.cryptomorin.xseries.XMaterial;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public int initialValueOfPoints = 1000;

    @Comment("Czy itemy po zabiciu gracza mają być usuwane z ziemi, i dodawane do ekwipunku zabójcy?")
    public boolean addItemsToKillerInventory = false;

    @Comment("Ilość punktów do odjęcia po popełnieniu samobójstwa przez gracza.")
    public int valueOfPointsToRemoveAfterSuicide = 5;

    @Comment("Długość trwania walki po zabiciu gracza.")
    public long combatTimeAfterKill = TimeUnit.SECONDS.toMillis(15);

    @Comment("Długość trwania walki po zaatakowaniu gracza.")
    public long combatTimeAfterDamage = TimeUnit.SECONDS.toMillis(21);

    @Comment("Czy title po zabiciu gracza ma być włączony?")
    public boolean killTitle = true;

    @Comment("Czy title po asyscie gracza ma być włączony?")
    public boolean assistTitle = true;

    @Comment("Długość trwania asysty po zaatakowaniu gracza")
    public long assistTimeAfterDamage = TimeUnit.SECONDS.toMillis(61);

    @Comment("Zablokowane komendy podczas antylogouta")
    public List<String> disallowedCommands = new ListBuilder<String>()
            .addAll(List.of(
                    "wb",
                    "workbench",
                    "kit",
                    "home",
                    "sethome",
                    "spawn",
                    "tpa",
                    "tpaccept",
                    "warp",
                    "ec",
                    "heal",
                    "feed",
                    "repair"
            ))
            .build();

    @Comment("Itemki których nadmiar będzie zabierany")
    public Map<Integer, Material> depositItemsMap = new MapBuilder<Integer, Material>()
            .put(1, Material.ENCHANTED_GOLDEN_APPLE)
            .put(12, Material.GOLDEN_APPLE)
            .build();

    public BukkitMenuBuilder saveInventoryMenu = new BukkitMenuBuilder("&6&lZapisywanie ekwipunku", 3, new MapBuilder<Integer, ItemStack>()
            .put(10, VersionProvider.getItemIdentifyManager().wrapIdentity(ItemBuilder.of(Material.BOOK).setName("&cResetuj").toItemStack(),
                    "reset-items", "reset-items"))
            .put(17, VersionProvider.getItemIdentifyManager().wrapIdentity(ItemBuilder.of(Material.BOOK).setName("&aZapisz").toItemStack(),
                    "save-items", "save-items"))
            .put(0, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(1, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(3, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(4, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(5, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(6, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(7, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(8, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(9, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(11, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(12, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(13, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(14, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(15, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(16, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(18, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(19, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(20, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(21, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(22, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(23, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(24, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(25, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .put(26, new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
            .build());
}