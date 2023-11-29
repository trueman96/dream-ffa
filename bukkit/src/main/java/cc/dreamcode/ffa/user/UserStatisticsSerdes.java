package cc.dreamcode.ffa.user;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class UserStatisticsSerdes implements ObjectSerializer<UserStatistics> {

    @Override
    public boolean supports(@NonNull Class<? super UserStatistics> type) {
        return UserStatistics.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull UserStatistics object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("points", object.getPoints());
        data.add("kills", object.getKills());
        data.add("deaths", object.getDeaths());
        data.add("assists", object.getAssists());
        data.add("killStreak", object.getKillStreak());
        data.add("maxKillStreak", object.getMaxKillStreak());
    }

    @Override
    public UserStatistics deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new UserStatistics(
                data.get("points", Integer.class),
                data.get("kills", Integer.class),
                data.get("deaths", Integer.class),
                data.get("assists", Integer.class),
                data.get("killStreak", Integer.class),
                data.get("maxKillStreak", Integer.class)
        );
    }
}
