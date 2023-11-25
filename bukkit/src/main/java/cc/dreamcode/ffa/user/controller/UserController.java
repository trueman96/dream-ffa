package cc.dreamcode.ffa.user.controller;

import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.user.*;
import cc.dreamcode.ffa.user.saveinventory.UserSavedInventory;
import cc.dreamcode.notice.minecraft.bukkit.BukkitNotice;
import cc.dreamcode.utilities.RoundUtil;
import cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.utilities.bukkit.ItemUtil;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

/**
 * UserController.java
 * Purpose: The UserController is a class that manages the User-related functionality in the game.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-24
 */
@RequiredArgsConstructor(onConstructor_= @Inject)
public final class UserController implements Listener {

    private final Tasker tasker;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    private final UserCache userCache;
    private final UserRepository userRepository;

    @EventHandler
    public void onPlayerJoin(@NonNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.tasker.newChain()
                .async(() -> this.userRepository.findOrCreateByHumanEntity(player))
                .acceptAsync(userSimpleEntry -> {
                    final User user = userSimpleEntry.getValue();
                    user.setName(player.getName());
                    if (!userSimpleEntry.getKey()) {
                        user.setStatistics(new UserStatistics());
                        user.getStatistics().setPoints(this.pluginConfig.initialValueOfPoints);
                        user.setSavedInventory(new UserSavedInventory());
                    }
                    user.setCombat(new UserCombat());
                    user.save();

                    this.userCache.add(user);
                })
                .acceptSync(user -> {
                    setupInventory(player, user.getValue());
                })
                .execute();
    }

    @EventHandler
    public void onPlayerQuit(@NonNull PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.tasker.newChain()
                .async(() -> this.userCache.get(player))
                .acceptSync(user -> {
                    if (user.getCombat().isInCombat()) {
                        player.setHealth(0);
                        this.messageConfig.playerLoggedOutWhileInCombat.sendAll(new MapBuilder<String, Object>()
                                .put("player", player.getName())
                                .build());
                    }
                })
                .acceptAsync(user -> {
                    user.save();
                    this.userCache.remove(user);
                })
                .execute();
    }

    @EventHandler
    public void onPlayerSpawnLocation(@NonNull PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(this.pluginConfig.spawnLocation);
    }

    @EventHandler
    public void onPlayerRespawn(@NonNull PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        this.tasker.newChain()
                .async(() -> this.userCache.get(player))
                .acceptAsync(user -> {
                    setupInventory(player, user);
                })
                .execute();
    }

    private void setupInventory(Player player, User user) {
        player.getInventory().clear();

        final PlayerInventory inventory = player.getInventory();
        this.pluginConfig.equipmentAfterJoin.forEach(inventory::setItem);
        ItemUtil.addItems(this.pluginConfig.itemsAfterJoin, inventory);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        final UserSavedInventory savedInventory = user.getSavedInventory();
        if (savedInventory.getInventory() != null) {
            player.getInventory().setContents(savedInventory.getInventory());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        final Player victim = event.getEntity();
        this.tasker.newChain()
                .async(() -> {
                    final User victimUser = this.userCache.get(victim);
                    final UserStatistics victimStatistics = victimUser.getStatistics();
                    final UserCombat victimCombat = victimUser.getCombat();

                    victimStatistics.resetKillStreak();
                    victimStatistics.addDeath();

                    Player killer = victim.getKiller();
                    if ((killer == null || killer.equals(victim)) && victimCombat.getLastAttackPlayer() != null && victimCombat.isInCombat()) {
                        killer = victimCombat.getLastAttackPlayer();
                    }

                    if (killer == null) {
                        int pointsToRemove = this.pluginConfig.valueOfPointsToRemoveAfterSuicide;
                        victimStatistics.removePoints(pointsToRemove);
                        victimCombat.reset();
                        victimUser.save();

                        this.messageConfig.playerSuicideAnnounce
                                .sendAll(new MapBuilder<String, Object>()
                                        .put("victim", victim.getName())
                                        .put("points-to-remove", pointsToRemove)
                                        .build());
                        return;
                    }

                    final User killerUser = this.userCache.get(killer);
                    final UserStatistics killerStatistics = killerUser.getStatistics();
                    killerUser.getCombat().setLastAttackTime(System.currentTimeMillis() + this.pluginConfig.combatTimeAfterKill);

                    int pointsToAdd = (int) (43.0 + (killerStatistics.getPoints() - victimStatistics.getPoints()) * -0.10),
                            pointsToRemove = (int) (pointsToAdd / 1.7);
                    if (pointsToAdd <= 0) {
                        pointsToAdd = 3;
                    }
                    if (pointsToRemove <= 0) {
                        pointsToRemove = 3;
                    }

                    double killerHealth = RoundUtil.round(killer.getHealth() / 2, 2);
                    this.messageConfig.playerKillAnnounce
                            .sendAll(new MapBuilder<String, Object>()
                                    .put("points-to-add", pointsToAdd)
                                    .put("points-to-remove", pointsToRemove)
                                    .put("victim", victim.getName())
                                    .put("killer", killer.getName())
                                    .put("killer-health", killerHealth)
                                    .build());

                    if (this.pluginConfig.killTitle) {
                        this.messageConfig.playerKillTitleKiller
                                .send(killer, new MapBuilder<String, Object>()
                                        .put("points-to-add", pointsToAdd)
                                        .put("points-to-remove", pointsToRemove)
                                        .put("victim", victim.getName())
                                        .put("killer", killer.getName())
                                        .put("killer-health", killerHealth)
                                        .build());
                    }

                    final Player assistant = victimCombat.getLastAssistPlayer();
                    if (nonNull(assistant) && victimCombat.getLastAssistTime() > System.currentTimeMillis()
                            && !Objects.equals(victimCombat.getLastAssistPlayer().getUniqueId(), killer.getUniqueId())) {

                        final User assistantUser = this.userCache.get(assistant);
                        final UserStatistics assistantStatistics = assistantUser.getStatistics();

                        int pointsToAddAssistant = (int) ((31.0 + (assistantStatistics.getPoints() - victimStatistics.getPoints()) * -0.10) / 3.0);
                        if (pointsToAddAssistant <= 0) {
                            pointsToAddAssistant = 3;
                        }

                        double assistantHealth = RoundUtil.round(assistant.getHealth() / 2, 2);
                        this.messageConfig.playerAssistAnnounce
                                .sendAll(new MapBuilder<String, Object>()
                                        .put("points-to-add", pointsToAdd)
                                        .put("points-to-remove", pointsToRemove)
                                        .put("victim", victim.getName())
                                        .put("killer", killer.getName())
                                        .put("killer-health", killerHealth)
                                        .build());

                        if (this.pluginConfig.assistTitle) {
                            this.messageConfig.playerAssistTitleAssistant
                                    .send(killer, new MapBuilder<String, Object>()
                                            .put("points-to-add", pointsToAdd)
                                            .put("points-to-remove", pointsToRemove)
                                            .put("victim", victim.getName())
                                            .put("killer", killer.getName())
                                            .put("killer-health", killerHealth)
                                            .put("points-to-add-assistant", pointsToAddAssistant)
                                            .put("assistant-health", assistantHealth)
                                            .build());
                        }

                        assistantStatistics.addAssist();
                        assistantStatistics.addPoints(pointsToAddAssistant);
                        assistantUser.save();
                    }

                    victimStatistics.removePoints(pointsToRemove);
                    victimCombat.reset();
                    victimUser.save();

                    killerStatistics.addPoints(pointsToAdd);
                    killerStatistics.addKillStreak();

                    BukkitNotice notice = this.messageConfig.killStreakAnnounce.get(killerStatistics.getKillStreak());
                    if (notice != null) {
                        notice.sendAll(new MapBuilder<String, Object>()
                                .put("killer", killer.getName())
                                .put("current_ks", killerStatistics.getKillStreak())
                                .put("maximum_ks", killerStatistics.getMaxKillStreak())
                                .build());
                    }

                    killerStatistics.addKill();
                    killerUser.save();
                })
                .execute();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(@NonNull EntityDeathEvent event) {
        if (!this.pluginConfig.addItemsToKillerInventory) {
            return;
        }

        final List<ItemStack> drops = event.getDrops();
        if (event.getEntity().getKiller() == null) {
            return;
        }

        final Player killer = event.getEntity().getKiller();
        if (killer.equals(event.getEntity())) {
            return;
        }

        addItems(killer, drops, event.getEntity().getLocation().getBlock());
        killer.giveExp(event.getDroppedExp());
        event.getDrops().clear();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        getAttackerFrom(event).ifPresent(attacker -> {
            if (victim.equals(attacker)) {
                return;
            }

            final User victimUser = this.userCache.get(victim);
            final User attackerUser = this.userCache.get(attacker);
            final UserCombat victimCombat = victimUser.getCombat();
            final UserCombat attackerCombat = attackerUser.getCombat();

            long combatTime = System.currentTimeMillis() + this.pluginConfig.combatTimeAfterDamage;
            victimCombat.setLastAttackTime(combatTime);
            attackerCombat.setLastAttackTime(combatTime);

            if (victimCombat.getLastAttackPlayer() != attacker) {
                victimCombat.setLastAssistPlayer(victimCombat.getLastAttackPlayer());
                victimCombat.setLastAssistTime(System.currentTimeMillis() + this.pluginConfig.assistTimeAfterDamage);
            }

            victimCombat.setLastAttackPlayer(attacker);
            attackerCombat.setLastAttackPlayer(victim);

            if (event.getDamager() instanceof Snowball || event.getDamager() instanceof Arrow) {
                this.messageConfig.healthInfo
                        .send(attacker, new MapBuilder<String, Object>()
                                .put("health", RoundUtil.round(victim.getHealth(), 2))
                                .build());
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(@NonNull PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        if (player.hasPermission("dreamcode.antilogout.bypass")) {
            return;
        }

        final UserCombat userCombat = this.userCache.get(player).getCombat();
        if (!userCombat.isInCombat()) {
            return;
        }

        final String message = event.getMessage().toLowerCase();
        for (String it : this.pluginConfig.disallowedCommands) {
            if (message.contains("/" + it)) {
                event.setCancelled(true);
                this.messageConfig.commandIsDisallowedWhileInCombat.send(player);
                break;
            }
        }

    }

    private Optional<Player> getAttackerFrom(@NonNull EntityDamageByEntityEvent event) {
        final Entity attacker = event.getDamager();
        boolean isPlayer = attacker instanceof Player, isProjectile = attacker instanceof Projectile;
        if (!isPlayer && !isProjectile) {
            return Optional.empty();
        }

        if (isProjectile) {
            ProjectileSource source = ((Projectile) attacker).getShooter();
            if (source instanceof Player) {
                return Optional.of((Player) source);
            }
        }

        return isPlayer ? Optional.of((Player) attacker) : Optional.empty();
    }

    private void addItems(Player player, List<ItemStack> items, Block block) {
        final Map<Integer, ItemStack> leftOver = player.getInventory().addItem(items.toArray(new ItemStack[0]));
        for (Map.Entry<Integer, ItemStack> en : leftOver.entrySet()) {
            block.getWorld().dropItemNaturally(block.getLocation(), en.getValue());
        }
    }
}
