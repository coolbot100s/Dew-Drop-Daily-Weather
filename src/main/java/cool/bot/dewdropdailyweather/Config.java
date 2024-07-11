package cool.bot.dewdropdailyweather;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = DewDropDailyWeather.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    /*  Weather Times Config
     *  This list defines when the mod should attempt to start a new weather event in ticks (DayTime)
     *  Values must be between 1 and 24000 (inclusive)
     *  Default is 20 ticks and 16000 ticks
     */
    private static final int MIN_DAYTIME = 1;
    private static final int MAX_DAYTIME = 24000;

    private static final List<Integer> defaultWeatherTimes = List.of(20, 16000);

    private static final Predicate<Object> daytimeValidator = o -> o instanceof Integer && ((Integer) o >= MIN_DAYTIME && (Integer) o <= MAX_DAYTIME);

    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_TIMES = BUILDER
            .defineList("weatherTimes", defaultWeatherTimes, daytimeValidator);
    public static List<Integer> weatherTimes;

    /*  Weather Times Ranges Config
     *  This is a list of lists, with a minimum and maximum value for random variance in weather Times, this list should be the same length as weatherTimes.
     *  Example: a Weather Time of 12000 with a corresponding Weather Range of [-1000, 1000] will result in a Weather Time randomly being selected between 11000 and 13000 each day.
     *  Values must be between -24000 and 24000 (inclusive)
     *  Default value: [[0,0],[0,2000]]
     */
    private static final List<? extends List<? extends Integer>> defaultWeatherRanges = List.of(List.of(0, 0), List.of(0, 2000));

    private static final Predicate<Object> weatherRangeValidator = o -> o instanceof List && ((List<?>) o).size() == 2 && ((List<?>) o).get(0) instanceof Integer && ((List<?>) o).get(1) instanceof Integer && ((Integer) ((List<?>) o).get(0)) <= ((Integer) ((List<?>) o).get(1)) && ((Integer) ((List<?>) o).get(0)) >= -24000 &&  ((Integer) ((List<?>) o).get(1)) <= 24000;

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_RANGES = BUILDER
            .defineListAllowEmpty("weatherRanges", defaultWeatherRanges, weatherRangeValidator);
    public static List<List<Integer>> weatherRanges;

//    /*  Weather Ends Config
//     *  This list defines when a weather event should end (currently the same as scheduling a "clear" weather).
//     *  If empty, weather events will continue until the next weather event starts, a value of 0 mean that event will continue until the next event starts.
//     *  Values must be between 1 and 24000 (inclusive)
//     *  Default is an empty list.
//     */
//    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_ENDS = BUILDER
//            .defineListAllowEmpty("weatherEnds", Collections.emptyList(), daytimeValidator);
//    public static List<Integer> weatherEnds;
//
//    /*  Weather End Ranges Config
//     *  This is a list of lists, with a minimum and maximum value for random variance in weather Times, this list should be the same length as weatherTimes.
//     *  Example: a Weather Time of 12000 with a corresponding Weather Range of [-1000, 1000] will result in a Weather Time randomly being selected between 11000 and 13000 each day.
//     *  Values must be between -24000 and 24000 (inclusive
//     *  Default is an empty list.
//     */
//    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_END_RANGES = BUILDER
//            .defineListAllowEmpty("weatherEndRanges", Collections.emptyList(), weatherRangeValidator);
//    public static List<List<Integer>> weatherEndRanges;


    /*  Weather Options Config
     *  This list of lists defines weather options that can be applied to a weather event, this list should be the same length as weatherTimes.
     *  Valid Events are: "clear", "rain", "storm"
     *  Default value: [["clear", "rain", "storm"],["clear", "rain", "storm"]]
     */
    private static final List<String> VALID_WEATHER_OPTIONS = List.of("ignore", "clear", "rain", "storm");

    private static final Predicate<Object> weatherOptionValidator = o -> {
        if (!(o instanceof List)) {
            return false;
        }
        for (Object option : ((List<?>) o)) {
            if (!(option instanceof String)) {
                return false;
            }
            if (!VALID_WEATHER_OPTIONS.contains((String) option)) {
                return false;
            }
        }
        return true;
    };


    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends String>>> WEATHER_OPTIONS = BUILDER
            .defineList("weatherOptions", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);
    public static List<List<String>> weatherOptions;

    /*  Weather Weights Config
     *  This list of lists defines the weights of each weather event, this list should be the same length as weatherTimes, each sublist should be the same length as the corresponding Weather Options.
     *  Default value: [[6,3,1],[12,6,3]]
     */
    private static final Predicate<Object> weatherWeightsValidator = o -> {
        if (!(o instanceof List)) {
            return false;
        }
        //TODO: This needs a proper validator

        return true;
    };

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_WEIGHTS = BUILDER
            .defineList("weatherWeights", List.of(List.of(6, 3, 1),List.of(6,2,2,2)), weatherWeightsValidator);
    public static List<List<Integer>> weatherWeights;

    // Normal Config stuff
    public static final ForgeConfigSpec.BooleanValue LOG_SCHEDULE = BUILDER
            .comment("When enabled, print the scheduled weather events to server console at the beginning of the day.")
            .define("logSchedule", false);
    public static boolean logSchedule;


    static final ForgeConfigSpec SERVER_CONFIG = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        weatherTimes = (List<Integer>) WEATHER_TIMES.get();
        weatherRanges = (List<List<Integer>>) WEATHER_RANGES.get();
//        weatherEnds = (List<Integer>) WEATHER_ENDS.get();
//        weatherEndRanges = (List<List<Integer>>) WEATHER_END_RANGES.get();
        weatherOptions = (List<List<String>>) WEATHER_OPTIONS.get();
        weatherWeights = (List<List<Integer>>) WEATHER_WEIGHTS.get();
        logSchedule = LOG_SCHEDULE.get();


    }
}
