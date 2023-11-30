package cc.dreamcode.ffa.user.ranking;

import cc.dreamcode.ffa.user.User;
import cc.dreamcode.ffa.user.UserRepository;
import eu.okaeri.injector.annotation.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_= @Inject)
public class UserRanking {

    private final UserRepository userRepository;
    private final @Getter List<User> pointsRanking = new ArrayList<>(), killStreakRanking = new ArrayList<>(), maxKillStreakRanking = new ArrayList<>();

    public void refreshRankings() {
        this.pointsRanking.clear();
        this.killStreakRanking.clear();
        this.maxKillStreakRanking.clear();
        this.pointsRanking.addAll(topPoints());
        this.killStreakRanking.addAll(topKillStreak());
        this.maxKillStreakRanking.addAll(topMaxKillStreak());
    }

    /**
     * This method returns a list of 20 users who have the highest number of points in their statistics.
     * It streams all the users from the database, sorts them based on the points in their statistics,
     * limits the list to the top 20 users, and collects the list into a list object.
     *
     * @return List of 20 users with the highest points
     */
    private List<User> topPoints() {
        return this.userRepository.streamAll()
                .sorted(Comparator.comparing(user -> user.getStatistics().getPoints()))
                .limit(20)
                .collect(Collectors.toList());
    }

    /**
     * This method returns a list of 20 users who have the highest kill streak in their statistics.
     * It streams all the users from the database, sorts them based on the kill streak in their statistics,
     * limits the list to the top 20 users, and collects the list into a list object.
     *
     * @return List of 20 users with the highest kill streak
     */
    private List<User> topKillStreak() {
        return this.userRepository.streamAll()
                .sorted(Comparator.comparing(user -> user.getStatistics().getKillStreak()))
                .limit(20)
                .collect(Collectors.toList());
    }

    /**
     * This method returns a list of 20 users who have the highest maximum kill streak in their statistics.
     * It streams all the users from the database, sorts them based on the maximum kill streak in their statistics,
     * limits the list to the top 20 users, and collects the list into a list object.
     *
     * @return List of 20 users with the highest maximum kill streak
     */
    private List<User> topMaxKillStreak() {
        return this.userRepository.streamAll()
                .sorted(Comparator.comparing(user -> user.getStatistics().getMaxKillStreak()))
                .limit(20)
                .collect(Collectors.toList());
    }
}
