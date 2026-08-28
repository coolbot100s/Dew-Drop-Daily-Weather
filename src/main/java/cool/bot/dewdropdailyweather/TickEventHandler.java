package cool.bot.dewdropdailyweather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cool.bot.botslib.util.RNG.irandRange;
import static cool.bot.botslib.util.RNG.weightedChoice;
import static cool.bot.botslib.util.Seasons.getSeason;
import static cool.bot.dewdropdailyweather.DewDropDailyWeather.useSeasons;

public class TickEventHandler {

    public static List<WeatherEvent> schedule = updateSchedule(null);
    public static class WeatherEvent {
        public int time;
        public String weather;
        public int getTime() {return time;}
        public String getWeather() {return weather;}
        public WeatherEvent(int time, String weather) {
            this.time = time;
            this.weather = weather;
        }

    }

    private static ArrayList<WeatherEvent> updateSchedule(ServerLevel level) {

        List<Integer> times;
        List<List<Integer>> timesRanges;

        List<List<Integer>> weights;
        List<List<String>> pools;

        int events;

        ArrayList<WeatherEvent> trueSchedule = new ArrayList<>(List.of());

        if (!useSeasons || level == null) {
            times = new ArrayList<>(List.copyOf(DewDropDailyWeather.CONFIG.weatherTimes.get()));
            timesRanges = List.copyOf(DewDropDailyWeather.CONFIG.weatherRanges.get());
            weights = List.copyOf(DewDropDailyWeather.CONFIG.weatherWeights.get());
            pools = List.copyOf(DewDropDailyWeather.CONFIG.weatherOptions.get());
        } else {
            switch (getSeason(level))
            {
                case SPRING:
                    times = new ArrayList<>(List.copyOf(DewDropDailyWeather.CONFIG.weatherTimesSpring.get()));
                    timesRanges = List.copyOf(DewDropDailyWeather.CONFIG.weatherRangesSpring.get());
                    weights = List.copyOf(DewDropDailyWeather.CONFIG.weatherWeightsSpring.get());
                    pools = List.copyOf(DewDropDailyWeather.CONFIG.weatherOptionsSpring.get());
                    break;
                case SUMMER:
                    times = new ArrayList<>(List.copyOf(DewDropDailyWeather.CONFIG.weatherTimesSummer.get()));
                    timesRanges = List.copyOf(DewDropDailyWeather.CONFIG.weatherRangesSummer.get());
                    weights = List.copyOf(DewDropDailyWeather.CONFIG.weatherWeightsSummer.get());
                    pools = List.copyOf(DewDropDailyWeather.CONFIG.weatherOptionsSummer.get());
                    break;
                case FALL:
                    times = new ArrayList<>(List.copyOf(DewDropDailyWeather.CONFIG.weatherTimesFall.get()));
                    timesRanges = List.copyOf(DewDropDailyWeather.CONFIG.weatherRangesFall.get());
                    weights = List.copyOf(DewDropDailyWeather.CONFIG.weatherWeightsFall.get());
                    pools = List.copyOf(DewDropDailyWeather.CONFIG.weatherOptionsFall.get());
                    break;
                case WINTER:
                    times = new ArrayList<>(List.copyOf(DewDropDailyWeather.CONFIG.weatherTimesWinter.get()));
                    timesRanges = List.copyOf(DewDropDailyWeather.CONFIG.weatherRangesWinter.get());
                    weights = List.copyOf(DewDropDailyWeather.CONFIG.weatherWeightsWinter.get());
                    pools = List.copyOf(DewDropDailyWeather.CONFIG.weatherOptionsWinter.get());
                    break;
                default:
                    times = new ArrayList<>(List.copyOf(DewDropDailyWeather.CONFIG.weatherTimes.get()));
                    timesRanges = List.copyOf(DewDropDailyWeather.CONFIG.weatherRanges.get());
                    weights = List.copyOf(DewDropDailyWeather.CONFIG.weatherWeights.get());
                    pools = List.copyOf(DewDropDailyWeather.CONFIG.weatherOptions.get());
            }
        }
        events = times.size();

        for (int i = 0; i < events; i++) {
            if (!timesRanges.isEmpty()) {
                times.set(i, times.get(i) + irandRange(timesRanges.get(i).get(0), timesRanges.get(i).get(1)));
            }

            List<String> cpool = pools.get(i);
            List<Integer> cweights = weights.get(i);


            //Decide weather
            String weatherType = weightedChoice(cpool, cweights);

            trueSchedule.add(new WeatherEvent(times.get(i), weatherType));
        }

        // Sort trueSchedule by time
        trueSchedule.sort(Comparator.comparingInt(WeatherEvent::getTime));
        return trueSchedule;
    }


    @SubscribeEvent
    public static void onTickEvent(ServerTickEvent.Post event) {
            ServerLevel level = event.getServer().overworld();

            // If weather cycle or daylight is off, do nothing
            if (!(level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE) && level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT))) {
                if (DewDropDailyWeather.CONFIG.logSchedule.get()) {
                    DewDropDailyWeather.LOGGER.info("Weather cycle or daylight is off, no forecast generated.");
                }
                return;
            }

            int dayTime = (int) level.getDayTime() % 24000;

            if (dayTime == 1) {
                schedule = updateSchedule(level);
                if (DewDropDailyWeather.CONFIG.logSchedule.get()) {
                    logSchedule(schedule, level);
                }
            } else if (schedule.stream().anyMatch(weatherEvent -> weatherEvent.getTime() == dayTime)) {
                String weatherType = schedule.stream().filter(weatherEvent -> weatherEvent.getTime() == dayTime).findFirst().get().getWeather();

                if(DewDropDailyWeather.CONFIG.logSchedule.get()) DewDropDailyWeather.LOGGER.info("Current Weather: {}", weatherType);

                switch (weatherType) {
                    case "clear":
                        level.setWeatherParameters(0,Integer.MAX_VALUE, false, false);
                        break;
                    case "rain":
                        level.setWeatherParameters(0,Integer.MAX_VALUE, true, false);
                        break;
                    case "storm":
                        level.setWeatherParameters(0,Integer.MAX_VALUE, true, true);
                        break;
                    case "ignore":
                        break;
                    default:
                        break;
                }
            }


    }

    private static void logSchedule(List<WeatherEvent> schedule, ServerLevel level) {
        DewDropDailyWeather.LOGGER.info("Today's Forecast:");
        if (useSeasons) {
            DewDropDailyWeather.LOGGER.info("Season: {}", getSeason(level));
        }
        for (WeatherEvent event : schedule) {
            DewDropDailyWeather.LOGGER.info("{}: {}", event.getTime(), event.getWeather());
        }
    }

}
