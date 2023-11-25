package cc.dreamcode.ffa.user;

import lombok.Data;

/**
 * UserStatistics.java
 * Purpose: The UserStatistics is a class that contains User statistics as points etc.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-24
 */
@Data
public final class UserStatistics {

    private int points, kills, deaths, assists, killStreak, maxKillStreak;

    /**
     * Adds the given points to the current points total.
     * @param index The number of points to add.
     */
    public void addPoints(int index) {
        points += index;
    }

    /**
     * Removes the given points from the current points total.
     * @param index The number of points to remove.
     */
    public void removePoints(int index) {
        points -= index;
    }

    /**
     * Adds one kill to the current kill total.
     */
    public void addKill() {
        kills++;
    }

    /**
     * Adds one death to the current death total.
     */
    public void addDeath() {
        deaths++;
    }

    /**
     * Adds one assist to the current assist total.
     */
    public void addAssist() {
        assists++;
    }

    /**
     * Adds one kill streak to the current kill streak total.
     */
    public void addKillStreak() {
        killStreak++;
        if (killStreak > maxKillStreak) {
            maxKillStreak = killStreak;
        }
    }

    /**
     * Resets the current kill streak total back to 0.
     */
    public void resetKillStreak() {
        killStreak = 0;
    }

}