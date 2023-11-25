package cc.dreamcode.ffa.mcversion;

import cc.dreamcode.ffa.mcversion.api.ItemIdentifyManager;
import cc.dreamcode.ffa.mcversion.v1_12_R1.V1_12_R1_ItemIdentifyManager;
import cc.dreamcode.ffa.mcversion.v1_16_R3.V1_16_R3_ItemIdentifyManager;
import cc.dreamcode.ffa.mcversion.v1_17_R1.V1_17_R1_ItemIdentifyManager;
import cc.dreamcode.ffa.mcversion.v1_18_R2.V1_18_R2_ItemIdentifyManager;
import cc.dreamcode.ffa.mcversion.v1_19_R2.V1_19_R2_ItemIdentifyManager;
import cc.dreamcode.ffa.mcversion.v1_19_R3.V1_19_R3_ItemIdentifyManager;
import cc.dreamcode.ffa.mcversion.v1_20_R1.V1_20_R1_ItemIdentifyManager;
import com.cryptomorin.xseries.ReflectionUtils;

public class VersionProvider {
    public static ItemIdentifyManager getItemIdentifyManager() {
        return switch (ReflectionUtils.NMS_VERSION) {
            case "v1_12_R1" -> new V1_12_R1_ItemIdentifyManager();
            case "v1_16_R3" -> new V1_16_R3_ItemIdentifyManager();
            case "v1_17_R1" -> new V1_17_R1_ItemIdentifyManager();
            case "v1_18_R2" -> new V1_18_R2_ItemIdentifyManager();
            case "v1_19_R2" -> new V1_19_R2_ItemIdentifyManager();
            case "v1_19_R3" -> new V1_19_R3_ItemIdentifyManager();
            case "v1_20_R1" -> new V1_20_R1_ItemIdentifyManager();
            default ->
                    throw new RuntimeException("Plugin doesn't support this server version, update to: 1.12.2, 1.16.5, 1.17.1, 1.18.2, 1.19.3, 1.19.4 and 1.20.1.");
        };
    }
}
