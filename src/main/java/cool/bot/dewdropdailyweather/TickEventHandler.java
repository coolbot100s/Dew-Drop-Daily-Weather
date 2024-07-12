package cool.bot.dewdropdailyweather;

import net.minecraft.server.level.ServerLevel;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.util.List;

import static cool.bot.botslib.util.RNG.irandRange;
import static cool.bot.botslib.util.RNG.weightedChoice;

public class TickEventHandler {

    public static List<WeatherEvent> schedule = updateSchedule();

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

    private static ArrayList<WeatherEvent> updateSchedule() {
        List<Integer> times = new ArrayList<>(List.copyOf(Config.weatherTimes));
        List<List<Integer>> timesRanges = List.copyOf(Config.weatherRanges);

        List<List<Integer>> weightss = List.copyOf(Config.weatherWeights);
        List<List<String>> pools = List.copyOf(Config.weatherOptions);

        int events = times.size();

        ArrayList<WeatherEvent> trueSchedule = new ArrayList<>(List.of());

        for (int i = 0; i < events; i++) {
            if (!timesRanges.isEmpty()) {
                times.set(i, times.get(i) + irandRange(timesRanges.get(i).get(0), timesRanges.get(i).get(1)));
            }

            List<String> cpool = pools.get(i);
            List<Integer> cweights = weightss.get(i);


            //Decide weather
            String weatherType = weightedChoice(cpool, cweights);

            trueSchedule.add(new WeatherEvent(times.get(i), weatherType));
        }

        // Sort trueSchedule by time
        trueSchedule.sort(Comparator.comparingInt(WeatherEvent::getTime));
        return trueSchedule;
    }


    @SubscribeEvent
    public static void onTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ServerLevel level = event.getServer().overworld();

            // If weather cycle or daylight is off, do nothing
            if (!(level.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE) && level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT))) {
                return;
            }

            int dayTime = (int) level.getDayTime() % 24000;

            if (dayTime == 1) {
                schedule = updateSchedule();
                if (Config.logSchedule) {
                    logSchedule(schedule);
                }
            } else if (schedule.stream().anyMatch(weatherEvent -> weatherEvent.getTime() == dayTime)) {
                String weatherType = schedule.stream().filter(weatherEvent -> weatherEvent.getTime() == dayTime).findFirst().get().getWeather();

                if(Config.logSchedule) DewDropDailyWeather.LOGGER.info("Current Weather: {}", weatherType);

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

    }

    private static void logSchedule(List<WeatherEvent> schedule) {
        DewDropDailyWeather.LOGGER.info("Today's Forecast:");
        for (WeatherEvent event : schedule) {
            DewDropDailyWeather.LOGGER.info("{}: {}", event.getTime(), event.getWeather());
        }
    }

}
