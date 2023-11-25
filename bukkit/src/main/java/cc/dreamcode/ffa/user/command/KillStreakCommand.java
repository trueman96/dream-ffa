package cc.dreamcode.ffa.user.command;

import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Path;
import cc.dreamcode.command.annotation.RequireSender;
import cc.dreamcode.command.sender.SenderType;
import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.UserCache;
import cc.dreamcode.ffa.user.UserStatistics;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequireSender(type = SenderType.PLAYER)
@RequiredArgsConstructor(onConstructor_= @Inject)
@Command(label = "killstreak", description = "Pokazuje nam aktualny i maksymalny killstreak.")
public class KillStreakCommand {

    private final UserCache userCache;

    private final MessageConfig messageConfig;

    @Path
    void handle(Player player) {
        final User user = this.userCache.get(player);
        final UserStatistics statistics = user.getStatistics();
        this.messageConfig.killStreakInfo.send(player, new MapBuilder<String, Object>()
                        .put("current_ks", statistics.getKillStreak())
                        .put("maximum_ks", statistics.getMaxKillStreak())
                        .build());
    }
}
