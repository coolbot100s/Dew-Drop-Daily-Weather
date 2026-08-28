package cool.bot.dewdropdailyweather;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(DewDropDailyWeather.MODID)
public class DewDropDailyWeather {

    public static final String MODID = "dew_drop_daily_weather";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final ModConfigSpec.Builder CONFIG_BUILDER = new ModConfigSpec.Builder();
    public static final Config CONFIG = new Config(CONFIG_BUILDER);

    public static boolean useSeasons = false;

    public DewDropDailyWeather(ModContainer container) {
        NeoForge.EVENT_BUS.register(TickEventHandler.class);
        container.registerConfig(ModConfig.Type.COMMON, CONFIG_BUILDER.build());

    }
}
