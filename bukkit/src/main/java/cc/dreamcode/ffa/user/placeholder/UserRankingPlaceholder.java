package cc.dreamcode.ffa.user.placeholder;

import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.UserCache;
import cc.dreamcode.ffa.user.UserRanking;
import cc.dreamcode.ffa.user.UserStatistics;
import cc.dreamcode.utilities.ParseUtil;
import cc.dreamcode.utilities.bukkit.ChatUtil;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor_= @Inject)
public class UserRankingPlaceholder extends PlaceholderExpansion {

    private final UserRanking userRanking;
    private final MessageConfig messageConfig;

    @Override
    public @NonNull String getIdentifier() {
        return "ffa_tops";
    }

    @Override
    public @NonNull String getAuthor() {
        return "dreamcode";
    }

    @Override
    public @NonNull String getVersion() {
        return "1.0-inDev";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NonNull String params) {
        final String parameter = params.toLowerCase();
        if (parameter.startsWith("points")) {
            final int position = ParseUtil.parseInteger(parameter.replace("points", "").replace("-", "")).orElse(0);
            final User user = this.userRanking.getPointsRanking().get(position);
            if (user != null) {
                return ChatUtil.fixColor(PlaceholderContext.of(CompiledMessage.of(this.messageConfig.pointsRankingFormat))
                        .with("user-name", user.getName())
                        .with("user-points", user.getStatistics().getPoints())
                        .apply());
            }
            return ChatUtil.fixColor(this.messageConfig.pointsRankingNotFound);
        } else if (parameter.startsWith("kill-streak")) {
            final int position = ParseUtil.parseInteger(parameter.replace("kill-streak", "").replace("-", "")).orElse(0);
            final User user = this.userRanking.getKillStreakRanking().get(position);
            if (user != null) {
                return ChatUtil.fixColor(PlaceholderContext.of(CompiledMessage.of(this.messageConfig.killStreakRankingFormat))
                        .with("user-name", user.getName())
                        .with("user-ks", user.getStatistics().getKillStreak())
                        .apply());
            }
            return ChatUtil.fixColor(this.messageConfig.killStreakRankingNotFound);
        } else if (parameter.startsWith("max-kill-streak")) {
            final int position = ParseUtil.parseInteger(parameter.replace("max-kill-streak", "").replace("-", "")).orElse(0);
            final User user = this.userRanking.getKillStreakRanking().get(position);
            if (user != null) {
                return ChatUtil.fixColor(PlaceholderContext.of(CompiledMessage.of(this.messageConfig.maxKillStreakRankingFormat))
                        .with("user-name", user.getName())
                        .with("user-max-ks", user.getStatistics().getMaxKillStreak())
                        .apply());
            }
            return ChatUtil.fixColor(this.messageConfig.maxKillStreakRankingNotFound);
        }
        return "param not found";
    }
}
