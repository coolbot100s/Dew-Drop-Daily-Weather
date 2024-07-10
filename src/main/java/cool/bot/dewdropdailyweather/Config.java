package cool.bot.dewdropdailyweather;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = DewDropDailyWeather.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    /*  Weather Times Config
     *  This list defines when the mod should roll to start a new weather event in ticks (DayTime)
     *  Values must be between 0 and 24000 (inclusive)
     *  Default is 20 ticks and 16000 ticks
     */
    private static final int MIN_DAYTIME = 0;
    private static final int MAX_DAYTIME = 24000;

    private static final List<Integer> default_weather_times = List.of(20, 16000);

    private static final Predicate<Object> validator = o -> o instanceof Integer && ((Integer) o >= MIN_DAYTIME && (Integer) o <= MAX_DAYTIME);

    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_TIMES = BUILDER
            .defineList("weatherTimes", default_weather_times, validator);

    public static List<? extends Integer> weatherTimes;

    /*  Weather Ends Config
     *  This list defines when a weather event should end, this list should be the same length as weatherTimes, and the values should be greater than the corresponding values in weatherTimes or 0.
     *  If empty, weather events will continue until the next weather event starts, a value of 0 mean that event will continue until the next event starts.
     *  Values must be between 0 and 24000 (inclusive)
     *  Default is an empty list.
     */
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_ENDS = BUILDER
            .defineListAllowEmpty("weatherEnds", Collections.emptyList(), validator);

    public static List<? extends Integer> weatherEnds;

    /*  Weather Ranges Config
     *  This is a list of lists, with a minimum and maximum value for random variance in weather Times, this list should be the same length as weatherTimes.
     *  Example: a Weather Time of 12000 with a corresponing Weather Range of [-1000, 1000] will result in a Weather Time randomly being selected between 11000 and 13000 each day.
     *  Values must be between -24000 and 24000 (inclusive
     *  Default value: [[0,0],[0,2000]]
     */


    /*  Weather Options Config
     *  This list of lists defines weather options that can be applied to a weather event, this list should be the same length as weatherTimes.
     *  Valid Events are: "clear", "rain", "storm"
     *  Default value: [["clear", "rain", "storm"],["clear", "rain", "storm"]]
     */

    /*  Weather Weights Config
     *  This list of lists defines the wieghted of each weather event, this list should be the same length as weatherTimes, each sublist should be the same length as the corresponding Weather Options.
     *  Default value: [[6,3,1],[6,3,2]]
     */



    static final ForgeConfigSpec SERVER_CONFIG = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        weatherTimes = WEATHER_TIMES.get();
        weatherEnds = WEATHER_ENDS.get();

        LogUtils.getLogger().info("Weather Times: {}", weatherTimes);

    }
}
