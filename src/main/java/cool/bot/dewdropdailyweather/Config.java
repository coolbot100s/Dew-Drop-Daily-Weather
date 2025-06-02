package cool.bot.dewdropdailyweather;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import cool.bot.botslib.util.Compatibility;

import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = DewDropDailyWeather.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    // Weather Times
    public static List<Integer> weatherTimes;

    private static final int MIN_DAYTIME = 1;
    private static final int MAX_DAYTIME = 24000;

    private static final List<Integer> defaultWeatherTimes = List.of(20, 14000);

    private static final Predicate<Object> daytimeValidator = o -> o instanceof Integer && ((Integer) o >= MIN_DAYTIME && (Integer) o <= MAX_DAYTIME);

    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_TIMES = BUILDER
            .comment("""
                    This is a list of when the mod should attempt to start a new weather event in ticks (DayTime)
                    Values must be between 1 and 24000 (inclusive)
                    Default is [20, 14000]""")
            .defineList("weatherTimes", defaultWeatherTimes, daytimeValidator);


    // Weather Ranges
    public static List<List<Integer>> weatherRanges;

    private static final List<? extends List<? extends Integer>> defaultWeatherRanges = List.of(List.of(0, 0), List.of(0, 2000));

    private static final Predicate<Object> weatherRangeValidator = o -> o instanceof List && ((List<?>) o).size() == 2 && ((List<?>) o).get(0) instanceof Integer && ((List<?>) o).get(1) instanceof Integer && ((Integer) ((List<?>) o).get(0)) <= ((Integer) ((List<?>) o).get(1)) && ((Integer) ((List<?>) o).get(0)) >= -24000 &&  ((Integer) ((List<?>) o).get(1)) <= 24000;

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_RANGES = BUILDER
            .comment("""
                        This is a list of lists, with a minimum and maximum value for random variance in weather Times, this list should be the same length as weatherTimes.
                        Example: a Weather Time of 12000 with a corresponding Weather Range of [-1000, 1000] will result in a Weather Time randomly being selected between 11000 and 13000 each day.
                        Values must be between -24000 and 24000 (inclusive), extreme values may result in the event not happening at all.
                        Default value: [[0,0],[0,2000]]""")
            .defineListAllowEmpty("weatherRanges", defaultWeatherRanges, weatherRangeValidator);


    // Options
    public static List<List<String>> weatherOptions;

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
            .comment("""
                        This list of lists defines weather options that can be applied to a weather event, this list should be the same length as weatherTimes.
                        Valid Events are: "clear", "rain", "storm", "ignore"
                        the first three behave as you would expect from vanilla weather, ignore does nothing, allowing the current weather event to continue uninterrupted.
                        Default value: [["clear", "rain", "storm"],["ignore", "clear", "rain", "storm"]]""")
            .defineList("weatherOptions", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);


    // Weights
    public static List<List<Integer>> weatherWeights;

    private static final Predicate<Object> weatherWeightsValidator = o -> {
        if (!(o instanceof List)) {
            return false;
        }
        for (Object weight : ((List<Integer>) o)) {
            if (!(weight instanceof Integer)) {
                return false;
            }
            if (((int) weight) <= 0) {
                return false;
            }
        }
        return true;
    };

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_WEIGHTS = BUILDER
            .comment("""
                        This list of lists defines the weights of each weather event, this list should be the same length as weatherTimes, each sublist should be the same length as the corresponding Weather Options.
                        Values must be above 0
                        Default value: [[7,2,1],[7,1,1,1]]""")
            .defineList("weatherWeights", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);


    // Debugger
    public static boolean logSchedule;

    public static final ForgeConfigSpec.BooleanValue LOG_SCHEDULE = BUILDER
            .comment("When enabled, print the scheduled weather events to server console at the beginning of the day, and the current event when a new event starts (including ignore events)")
            .define("logSchedule", false);


    // Seasons compat
    public static boolean enableSeasons;

    public static final ForgeConfigSpec.BooleanValue ENABLE_SEASONS = BUILDER
            .comment("If Serene Seasons is present, you can enable this to use an alternative set of lists for each season, these options work the same as the ones listed above and have identical default values.")
            .define("enableSeasons", false);

    // Spring
    public static List<Integer> weatherTimesSpring;
    public static List<List<Integer>> weatherRangesSpring;
    public static List<List<String>> weatherOptionsSpring;
    public static List<List<Integer>> weatherWeightsSpring;

    // Summer
    public static List<Integer> weatherTimesSummer;
    public static List<List<Integer>> weatherRangesSummer;
    public static List<List<String>> weatherOptionsSummer;
    public static List<List<Integer>> weatherWeightsSummer;

    // Fall
    public static List<Integer> weatherTimesFall;
    public static List<List<Integer>> weatherRangesFall;
    public static List<List<String>> weatherOptionsFall;
    public static List<List<Integer>> weatherWeightsFall;

    // Winter
    public static List<Integer> weatherTimesWinter;
    public static List<List<Integer>> weatherRangesWinter;
    public static List<List<String>> weatherOptionsWinter;
    public static List<List<Integer>> weatherWeightsWinter;

    // Spring
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_TIMES_SPRING = BUILDER
            .comment("Spring weather times")
            .defineList("weatherTimesSpring", defaultWeatherTimes, daytimeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_RANGES_SPRING = BUILDER
            .comment("Spring weather ranges")
            .defineListAllowEmpty("weatherRangesSpring", defaultWeatherRanges, weatherRangeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends String>>> WEATHER_OPTIONS_SPRING = BUILDER
            .comment("Spring weather options")
            .defineList("weatherOptionsSpring", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_WEIGHTS_SPRING = BUILDER
            .comment("Spring weather weights")
            .defineList("weatherWeightsSpring", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);

    // Summer
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_TIMES_SUMMER = BUILDER
            .comment("Summer weather times")
            .defineList("weatherTimesSummer", defaultWeatherTimes, daytimeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_RANGES_SUMMER = BUILDER
            .comment("Summer weather ranges")
            .defineListAllowEmpty("weatherRangesSummer", defaultWeatherRanges, weatherRangeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends String>>> WEATHER_OPTIONS_SUMMER = BUILDER
            .comment("Summer weather options")
            .defineList("weatherOptionsSummer", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_WEIGHTS_SUMMER = BUILDER
            .comment("Summer weather weights")
            .defineList("weatherWeightsSummer", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);

    // Fall
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_TIMES_FALL = BUILDER
            .comment("Fall weather times")
            .defineList("weatherTimesFall", defaultWeatherTimes, daytimeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_RANGES_FALL = BUILDER
            .comment("Fall weather ranges")
            .defineListAllowEmpty("weatherRangesFall", defaultWeatherRanges, weatherRangeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends String>>> WEATHER_OPTIONS_FALL = BUILDER
            .comment("Fall weather options")
            .defineList("weatherOptionsFall", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_WEIGHTS_FALL = BUILDER
            .comment("Fall weather weights")
            .defineList("weatherWeightsFall", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);

    // Winter
    private static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> WEATHER_TIMES_WINTER = BUILDER
            .comment("Winter weather times")
            .defineList("weatherTimesWinter", defaultWeatherTimes, daytimeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_RANGES_WINTER = BUILDER
            .comment("Winter weather ranges")
            .defineListAllowEmpty("weatherRangesWinter", defaultWeatherRanges, weatherRangeValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends String>>> WEATHER_OPTIONS_WINTER = BUILDER
            .comment("Winter weather options")
            .defineList("weatherOptionsWinter", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

    private static final ForgeConfigSpec.ConfigValue<List<? extends List<? extends Integer>>> WEATHER_WEIGHTS_WINTER = BUILDER
            .comment("Winter weather weights")
            .defineList("weatherWeightsWinter", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);


    // This stays at the bottom!
    static final ForgeConfigSpec SERVER_CONFIG = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        // Load non-seasonal values
        weatherTimes = (List<Integer>) WEATHER_TIMES.get();
        weatherRanges = (List<List<Integer>>) WEATHER_RANGES.get();
        weatherOptions = (List<List<String>>) WEATHER_OPTIONS.get();
        weatherWeights = (List<List<Integer>>) WEATHER_WEIGHTS.get();
        logSchedule = LOG_SCHEDULE.get();
        enableSeasons = ENABLE_SEASONS.get();

        // Load seasonal values
        // Spring
        weatherTimesSpring = (List<Integer>) WEATHER_TIMES_SPRING.get();
        weatherRangesSpring = (List<List<Integer>>) WEATHER_RANGES_SPRING.get();
        weatherOptionsSpring = (List<List<String>>) WEATHER_OPTIONS_SPRING.get();
        weatherWeightsSpring = (List<List<Integer>>) WEATHER_WEIGHTS_SPRING.get();

        // Summer
        weatherTimesSummer = (List<Integer>) WEATHER_TIMES_SUMMER.get();
        weatherRangesSummer = (List<List<Integer>>) WEATHER_RANGES_SUMMER.get();
        weatherOptionsSummer = (List<List<String>>) WEATHER_OPTIONS_SUMMER.get();
        weatherWeightsSummer = (List<List<Integer>>) WEATHER_WEIGHTS_SUMMER.get();

        // Fall
        weatherTimesFall = (List<Integer>) WEATHER_TIMES_FALL.get();
        weatherRangesFall = (List<List<Integer>>) WEATHER_RANGES_FALL.get();
        weatherOptionsFall = (List<List<String>>) WEATHER_OPTIONS_FALL.get();
        weatherWeightsFall = (List<List<Integer>>) WEATHER_WEIGHTS_FALL.get();

        // Winter
        weatherTimesWinter = (List<Integer>) WEATHER_TIMES_WINTER.get();
        weatherRangesWinter = (List<List<Integer>>) WEATHER_RANGES_WINTER.get();
        weatherOptionsWinter = (List<List<String>>) WEATHER_OPTIONS_WINTER.get();
        weatherWeightsWinter = (List<List<Integer>>) WEATHER_WEIGHTS_WINTER.get();

        DewDropDailyWeather.useSeasons = Config.enableSeasons && Compatibility.sereneSeasonsLoaded();
    }
}
