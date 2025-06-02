package cool.bot.dewdropdailyweather;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DewDropDailyWeather.MODID)
public class DewDropDailyWeather {

    public static final String MODID = "dew_drop_daily_weather";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean useSeasons = false;

    public DewDropDailyWeather() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(TickEventHandler.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
    }

}
