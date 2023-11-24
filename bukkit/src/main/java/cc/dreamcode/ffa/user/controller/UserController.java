package cc.dreamcode.ffa.user.controller;

import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.UserCache;
import cc.dreamcode.ffa.user.UserRepository;
import cc.dreamcode.utilities.bukkit.ItemUtil;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.tasker.core.Tasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.Map;

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

    private final UserCache userCache;
    private final UserRepository userRepository;

    @EventHandler
    public void onPlayerJoin(@NonNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.tasker.newChain()
                .async(() -> this.userRepository.findOrCreateByHumanEntity(player))
                .acceptAsync(userSimpleEntry -> {
                    User user = userSimpleEntry.getValue();
                    user.setName(player.getName());
                    if (!userSimpleEntry.getKey()) {
                        user.setPoints(this.pluginConfig.initialValueOfPoints);
                    }
                    user.save();

                    this.userCache.add(user);
                })
                .acceptSync(user -> {
                    setupInventory(player);
                })
                .execute();
    }

    @EventHandler
    public void onPlayerQuit(@NonNull PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.tasker.newChain()
                .async(() -> this.userCache.get(player))
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
                .async(() -> this.userRepository.findOrCreateByHumanEntity(player))
                .acceptSync(user -> {
                    user.setName(player.getName());
                })
                .acceptAsync(user -> {
                    user.save();
                })
                .acceptSync(user -> {
                    setupInventory(player);
                })
                .execute();
    }

    private void setupInventory(Player player) {
        final PlayerInventory inventory = player.getInventory();
        this.pluginConfig.equipmentAfterJoin.forEach(inventory::setItem);
        ItemUtil.addItems(this.pluginConfig.itemsAfterJoin, inventory);
    }

}
