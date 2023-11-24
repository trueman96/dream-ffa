package cc.dreamcode.ffa.user;

import lombok.Data;
import org.bukkit.entity.Player;

/**
 * UserCombat.java
 * Purpose: The UserCombat is a class that contains User combat-related stuff.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-24
 */
@Data
public class UserCombat {

    private transient Player lastAttackPlayer, lastAssistPlayer;
    private long lastAttackTime, lastAssistTime;

    public boolean isInCombat() {
        return this.lastAttackTime > System.currentTimeMillis();
    }

    public void reset() {
        this.lastAttackTime = 0L;
        this.lastAssistTime = 0L;
        this.lastAssistPlayer = null;
        this.lastAttackPlayer = null;
    }
}
