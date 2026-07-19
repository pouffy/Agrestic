package io.github.pouffy.agrestic;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class AgresticConfig {
    private final static String CATEGORY_GENERAL = "all.general";
    private final static String CATEGORY_BEES = "all.bees";
    private final static String CATEGORY_WORLD = "all.world";
    private final static String CATEGORY_COMPAT = "all.compat";

    public static class Server {
        public final ModConfigSpec.ConfigValue<Integer> minBrewQualityChange;
        public final ModConfigSpec.ConfigValue<Integer> maxBrewQualityChange;

        Server(ModConfigSpec.Builder builder) {
            builder.push(CATEGORY_GENERAL);
            minBrewQualityChange = builder.comment("the minimum amount of increase that booze culture will provide to the new brew, in percent").defineInRange("minQualityChange", -1, -50, 50);
            maxBrewQualityChange = builder.comment("the maximum amount of increase that booze culture will provide to the new brew, in percent").defineInRange("maxQualityChange", 4, -50, 50);
        }
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue foodEffectTooltips;

        Client(ModConfigSpec.Builder builder) {
            builder.push(CATEGORY_GENERAL);
            foodEffectTooltips = builder.define("foodEffectTooltips", true);
        }
    }

    public static class Common {

        Common(ModConfigSpec.Builder builder) {

        }
    }

    public static final ModConfigSpec serverSpec;
    public static final Server SERVER;
    public static final ModConfigSpec clientSpec;
    public static final Client CLIENT;
    public static final ModConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Server, ModConfigSpec> server = new ModConfigSpec.Builder().configure(Server::new);
        serverSpec = server.getRight();
        SERVER = server.getLeft();
        final Pair<Client, ModConfigSpec> client = new ModConfigSpec.Builder().configure(Client::new);
        clientSpec = client.getRight();
        CLIENT = client.getLeft();
        final Pair<Common, ModConfigSpec> common = new ModConfigSpec.Builder().configure(Common::new);
        commonSpec = common.getRight();
        COMMON = common.getLeft();
    }
}
