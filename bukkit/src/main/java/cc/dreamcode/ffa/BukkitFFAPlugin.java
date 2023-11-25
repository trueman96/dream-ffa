package cc.dreamcode.ffa;

import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.ffa.config.PluginConfig;
import cc.dreamcode.ffa.handler.InvalidInputValueHandler;
import cc.dreamcode.ffa.handler.InvalidSenderTypeHandler;
import cc.dreamcode.ffa.handler.InvalidUsageHandler;
import cc.dreamcode.ffa.handler.NoPermissionHandler;
import cc.dreamcode.ffa.mcversion.VersionProvider;
import cc.dreamcode.ffa.user.UserCache;
import cc.dreamcode.ffa.user.UserRepository;
import cc.dreamcode.ffa.user.command.KillStreakCommand;
import cc.dreamcode.ffa.user.controller.UserController;
import cc.dreamcode.ffa.user.saveinventory.command.SaveInventoryCommand;
import cc.dreamcode.ffa.user.task.UserCombatInfoUpdateTask;
import cc.dreamcode.ffa.user.task.UserItemsDepositTask;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.bukkit.okaeri.MenuBuilderSerdes;
import cc.dreamcode.notice.minecraft.bukkit.serdes.BukkitNoticeSerdes;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.ConfigurationComponentResolver;
import cc.dreamcode.platform.bukkit.component.ListenerComponentResolver;
import cc.dreamcode.platform.bukkit.component.RunnableComponentResolver;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.component.resolver.CommandBindComponentResolver;
import cc.dreamcode.platform.component.resolver.CommandComponentResolver;
import cc.dreamcode.platform.component.resolver.CommandExtensionComponentResolver;
import cc.dreamcode.platform.component.resolver.CommandHandlerComponentResolver;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.platform.persistence.component.DocumentPersistenceComponentResolver;
import cc.dreamcode.platform.persistence.component.DocumentRepositoryComponentResolver;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;

public final class BukkitFFAPlugin extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence {

    @Getter private static BukkitFFAPlugin bukkitFFAPlugin;

    @Override
    public void load(@NonNull ComponentManager componentManager) {
        bukkitFFAPlugin = this;
    }

    @Override
    public void enable(@NonNull ComponentManager componentManager) {
        this.registerInjectable(BukkitTasker.newPool(this));
        this.registerInjectable(BukkitMenuProvider.create(this));
        this.registerInjectable(BukkitCommand.create(this));

        componentManager.registerResolver(ConfigurationComponentResolver.class);
        componentManager.registerComponent(MessageConfig.class);

        componentManager.registerResolver(CommandComponentResolver.class);
        componentManager.registerResolver(CommandBindComponentResolver.class);
        componentManager.registerResolver(CommandHandlerComponentResolver.class);
        componentManager.registerResolver(CommandExtensionComponentResolver.class);
        componentManager.registerResolver(ListenerComponentResolver.class);
        componentManager.registerResolver(RunnableComponentResolver.class);

        componentManager.registerComponent(InvalidInputValueHandler.class);
        componentManager.registerComponent(InvalidSenderTypeHandler.class);
        componentManager.registerComponent(InvalidUsageHandler.class);
        componentManager.registerComponent(NoPermissionHandler.class);

        componentManager.registerComponent(VersionProvider.class);

        componentManager.registerComponent(PluginConfig.class, pluginConfig -> {
            componentManager.setDebug(pluginConfig.debug);

            this.registerInjectable(pluginConfig.storageConfig);

            componentManager.registerResolver(DocumentPersistenceComponentResolver.class);
            componentManager.registerResolver(DocumentRepositoryComponentResolver.class);

            componentManager.registerComponent(DocumentPersistence.class);

            componentManager.registerComponent(UserRepository.class);
            componentManager.registerComponent(UserCache.class);
            componentManager.registerComponent(UserController.class);

            componentManager.registerComponent(UserCombatInfoUpdateTask.class);
            componentManager.registerComponent(UserItemsDepositTask.class);

            componentManager.registerComponent(KillStreakCommand.class);
            componentManager.registerComponent(SaveInventoryCommand.class);
        });

    }

    @Override
    public void disable() {
        // features need to be call when server is stopping
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("dream-ffa", "1.0-InDEV", "vkie");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerdes());
            registry.register(new MenuBuilderSerdes());
        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(new SerdesBukkit());
        };
    }

}
