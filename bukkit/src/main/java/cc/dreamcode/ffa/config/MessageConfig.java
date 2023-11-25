package cc.dreamcode.ffa.config;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * MessageConfig.java
 * Purpose: The MessageConfig class is utilised by other classes to access messages sent to users.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-24
 */
@Configuration(child = "message.yml")
@Headers({
        @Header("## Dream-FFA (Message-Config) ##"),
        @Header("Dostepne type: (DO_NOT_SEND, CHAT, ACTION_BAR, SUBTITLE, TITLE, TITLE_SUBTITLE)")
})
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public final class MessageConfig extends OkaeriConfig {

    public BukkitNotice usage = new BukkitNotice(MinecraftNoticeType.CHAT, "&7Poprawne uzycie: &c{usage}");
    public BukkitNotice noPermission = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Nie posiadasz uprawnien.");
    public BukkitNotice noPlayer = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Podanego gracza &cnie znaleziono.");
    public BukkitNotice playerIsOffline = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Podany gracz &cjest offline.");
    public BukkitNotice notPlayer = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Nie jestes graczem.");
    public BukkitNotice notNumber = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Podana liczba &cnie jest cyfra.");
    public BukkitNotice playerIsMe = new BukkitNotice(MinecraftNoticeType.CHAT, "&4Nie rob tego &cna sobie.");

    @Comment("Dostępne placeholdery: (points-to-remove, victim)")
    public BukkitNotice playerSuicideAnnounce = new BukkitNotice(MinecraftNoticeType.CHAT, "&c{victim} &8(&7-{points-to-remove}&8) &cpopełnił samobójstwo!");

    @Comment("Dostępne placeholdery: (points-to-add, points-to-remove, victim, killer, killer-health)")
    public BukkitNotice playerKillAnnounce = new BukkitNotice(MinecraftNoticeType.CHAT, "&c{victim} &8(&7-{points-to-remove}&8) &czostał zabity przez &c{killer} &8(&7+{points-to-add}&8, &c{killer-health}❤&8)");

    @Comment("Dostępne placeholdery: (points-to-add, points-to-remove, victim, killer, killer-health)")
    public BukkitNotice playerKillTitleKiller = new BukkitNotice(MinecraftNoticeType.TITLE_SUBTITLE,
            "&aZabójstwo!", "&cZabiłeś gracza {victim} &8(&7+{points-to-add}, &c{killer-health}❤&8)");

    @Comment("Dostępne placeholdery: (points-to-add, points-to-remove, victim, killer, killer-health, points-to-add-assistant)")
    public BukkitNotice playerAssistTitleAssistant = BukkitNotice.of(MinecraftNoticeType.TITLE_SUBTITLE,
            "&aAsysta!", "&cAsystowałeś przy zabiciu {victim} &8(&7+{points-to-add-assistant}, &c{assistant-health}❤&8)");

    @Comment("Dostępne placeholdery: (points-to-add, points-to-remove, victim, killer, killer-health, points-to-add-assistant)")
    public BukkitNotice playerAssistAnnounce = BukkitNotice.of(MinecraftNoticeType.TITLE_SUBTITLE,
            "&c{assistant} asystował przy zabiciu {victim} &8(&7+{ASSISTANT_POINTS}&8, &c{ASSISTANT_HEARTS}❤&8)");
    public BukkitNotice healthInfo = BukkitNotice.of(MinecraftNoticeType.ACTION_BAR, "&7HP zaatakowanego gracza: &c{health}");
    public BukkitNotice commandIsDisallowedWhileInCombat = BukkitNotice.of(MinecraftNoticeType.CHAT, "&cTa komenda zostala zablokowana podczas walki!");
    public BukkitNotice playerLoggedOutWhileInCombat = BukkitNotice.of(MinecraftNoticeType.CHAT, "&cGracz {player} wylogował się podczas walki!");

    public BukkitNotice combatInfo = BukkitNotice.of(MinecraftNoticeType.ACTION_BAR,
            "&c&lAntiLogout &8(&c{combat-time}&7, {combat-progress}&7, &c{combat-percent}&6%&8)");

    @Comment("Symbol używany w placeholderze: combat-progress")
    public String progressSymbol = "■";

    @Comment("Wiadomości o zabraniu nadmiaru danych itemków")
    public Map<Material, BukkitNotice> depositMessages = new MapBuilder<Material, BukkitNotice>()
            .put(Material.ENCHANTED_GOLDEN_APPLE, BukkitNotice.of(MinecraftNoticeType.CHAT, "&7Zabrano nadmiar &8(&7{amount}&8) &7koxów z ekwipunku!"))
            .put(Material.GOLDEN_APPLE, BukkitNotice.of(MinecraftNoticeType.CHAT, "&7Zabrano nadmiar &8(&7{amount}&8) &7refili z ekwipunku!"))
            .build();
}
