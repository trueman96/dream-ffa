package cc.dreamcode.ffa.user.combat;

import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * UserCombat.java
 * Purpose: The UserCombat is a class that contains User combat-related stuff.
 * @author vkie
 * @version 1.0-inDev
 * @since 2023-11-24
 */
@Data
public class UserCombat {

    private UUID lastAttackPlayer, lastAssistPlayer;
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

    public void setLastAttackPlayer(UUID lastAttackPlayer) {
        this.lastAttackPlayer = lastAttackPlayer;
    }

    public void setLastAssistPlayer(UUID lastAssistPlayer) {
        this.lastAssistPlayer = lastAssistPlayer;
    }

    public void setLastAssistPlayer(Player lastAssistPlayer) {
        this.lastAssistPlayer = lastAssistPlayer.getUniqueId();
    }

    public void setLastAttackPlayer(Player lastAttackPlayer) {
        this.lastAttackPlayer = lastAttackPlayer.getUniqueId();
    }
}
