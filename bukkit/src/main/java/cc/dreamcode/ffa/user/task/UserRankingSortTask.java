package cc.dreamcode.ffa.user.task;

import cc.dreamcode.ffa.user.UserRanking;
import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import lombok.RequiredArgsConstructor;

/**
 * UserRankingSortTask.java
 * Purpose: The UserRankingSortTask is a class that re-sorts user-ranking.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-25
 */
@RequiredArgsConstructor
@Scheduler(delay = 160L, interval = 160L)
public class UserRankingSortTask implements Runnable {

    private final UserRanking userRanking;

    @Override
    public void run() {
        this.userRanking.refreshRankings();
    }
}
