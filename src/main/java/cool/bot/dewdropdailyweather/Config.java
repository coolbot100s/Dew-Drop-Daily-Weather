package cool.bot.dewdropdailyweather;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class Config
{
    // Weather Times
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> weatherTimes;

    private static final int MIN_DAYTIME = 1;
    private static final int MAX_DAYTIME = 24000;

    private static final List<Integer> defaultWeatherTimes = List.of(20, 14000);

    private static final Predicate<Object> daytimeValidator = o -> o instanceof Integer && ((Integer) o >= MIN_DAYTIME && (Integer) o <= MAX_DAYTIME);


    // Weather Ranges
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherRanges;

    private static final List<List<Integer>> defaultWeatherRanges = List.of(List.of(0, 0), List.of(0, 2000));

    private static final Predicate<Object> weatherRangeValidator = o -> o instanceof List && ((List<?>) o).size() == 2 && ((List<?>) o).get(0) instanceof Integer && ((List<?>) o).get(1) instanceof Integer && ((Integer) ((List<?>) o).get(0)) <= ((Integer) ((List<?>) o).get(1)) && ((Integer) ((List<?>) o).get(0)) >= -24000 &&  ((Integer) ((List<?>) o).get(1)) <= 24000;


    // Options
    public final ModConfigSpec.ConfigValue<List<? extends List<String>>> weatherOptions;

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

    // Weights
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherWeights;

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

    public final ModConfigSpec.BooleanValue logSchedule;

    public final ModConfigSpec.BooleanValue enableSeasons;

    // Spring
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> weatherTimesSpring;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherRangesSpring;
    public final ModConfigSpec.ConfigValue<List<? extends List<String>>> weatherOptionsSpring;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherWeightsSpring;
    // Summer
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> weatherTimesSummer;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherRangesSummer;
    public final ModConfigSpec.ConfigValue<List<? extends List<String>>> weatherOptionsSummer;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherWeightsSummer;
    // Fall
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> weatherTimesFall;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherRangesFall;
    public final ModConfigSpec.ConfigValue<List<? extends List<String>>> weatherOptionsFall;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherWeightsFall;
    // Winter
    public final ModConfigSpec.ConfigValue<List<? extends Integer>> weatherTimesWinter;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherRangesWinter;
    public final ModConfigSpec.ConfigValue<List<? extends List<String>>> weatherOptionsWinter;
    public final ModConfigSpec.ConfigValue<List<? extends List<Integer>>> weatherWeightsWinter;


    public Config(final ModConfigSpec.Builder builder) {
        weatherTimes = builder.comment("""
                    This is a list of when the mod should attempt to start a new weather event in ticks (DayTime)
                    Values must be between 1 and 24000 (inclusive)
                    Default is [20, 14000]""")
                .defineList("weatherTimes", defaultWeatherTimes, daytimeValidator);

        weatherRanges = builder.comment("""
                        This is a list of lists, with a minimum and maximum value for random variance in weather Times, this list should be the same length as weatherTimes.
                        Example: a Weather Time of 12000 with a corresponding Weather Range of [-1000, 1000] will result in a Weather Time randomly being selected between 11000 and 13000 each day.
                        Values must be between -24000 and 24000 (inclusive), extreme values may result in the event not happening at all.
                        Default value: [[0,0],[0,2000]]""")
                .defineListAllowEmpty("weatherRanges", defaultWeatherRanges, weatherRangeValidator);

        weatherOptions = builder.comment("""
                        This list of lists defines weather options that can be applied to a weather event, this list should be the same length as weatherTimes.
                        Valid Events are: "clear", "rain", "storm", "ignore"
                        the first three behave as you would expect from vanilla weather, ignore does nothing, allowing the current weather event to continue uninterrupted.
                        Default value: [["clear", "rain", "storm"],["ignore", "clear", "rain", "storm"]]""")
                .defineList("weatherOptions", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

        weatherWeights = builder.comment("""
                        This list of lists defines the weights of each weather event, this list should be the same length as weatherTimes, each sublist should be the same length as the corresponding Weather Options.
                        Values must be above 0
                        Default value: [[7,2,1],[7,1,1,1]]""")
                .defineList("weatherWeights", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);


        logSchedule = builder.comment("When enabled, print the scheduled weather events to server console at the beginning of the day, and the current event when a new event starts (including ignore events)")
                .define("logSchedule", false);

        enableSeasons = builder .comment("If Serene Seasons is present, you can enable this to use an alternative set of lists for each season, these options work the same as the ones listed above and have identical default values.")
                .define("enableSeasons", false);


        // Spring
        weatherTimesSpring = builder.comment("Spring weather times")
                .defineList("weatherTimesSpring", defaultWeatherTimes, daytimeValidator);

        weatherRangesSpring = builder.comment("Spring weather ranges")
                .defineListAllowEmpty("weatherRangesSpring", defaultWeatherRanges, weatherRangeValidator);

        weatherOptionsSpring = builder.comment("Spring weather options")
                .defineList("weatherOptionsSpring", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

        weatherWeightsSpring = builder.comment("Spring weather weights")
                .defineList("weatherWeightsSpring", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);

        // Summer
        weatherTimesSummer = builder.comment("Summer weather times")
                .defineList("weatherTimesSummer", defaultWeatherTimes, daytimeValidator);

        weatherRangesSummer = builder.comment("Summer weather ranges")
                .defineListAllowEmpty("weatherRangesSummer", defaultWeatherRanges, weatherRangeValidator);

        weatherOptionsSummer = builder.comment("Summer weather options")
                .defineList("weatherOptionsSummer", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

        weatherWeightsSummer = builder.comment("Summer weather weights")
                .defineList("weatherWeightsSummer", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);

        // Fall
        weatherTimesFall = builder.comment("Fall weather times")
                .defineList("weatherTimesFall", defaultWeatherTimes, daytimeValidator);

        weatherRangesFall = builder.comment("Fall weather ranges")
                .defineListAllowEmpty("weatherRangesFall", defaultWeatherRanges, weatherRangeValidator);

        weatherOptionsFall = builder.comment("Fall weather options")
                .defineList("weatherOptionsFall", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

        weatherWeightsFall = builder.comment("Fall weather weights")
                .defineList("weatherWeightsFall", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);

        // Winter
        weatherTimesWinter = builder.comment("Winter weather times")
                .defineList("weatherTimesWinter", defaultWeatherTimes, daytimeValidator);

        weatherRangesWinter = builder.comment("Winter weather ranges")
                .defineListAllowEmpty("weatherRangesWinter", defaultWeatherRanges, weatherRangeValidator);

        weatherOptionsWinter = builder.comment("Winter weather options")
                .defineList("weatherOptionsWinter", List.of(List.of("clear", "rain", "storm"), List.of("ignore", "clear", "rain", "storm")), weatherOptionValidator);

        weatherWeightsWinter = builder.comment("Winter weather weights")
                .defineList("weatherWeightsWinter", List.of(List.of(7, 2, 1),List.of(7,1,1,1)), weatherWeightsValidator);
    }
}