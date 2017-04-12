import java.util.List;

/**
 * Created by pollakt on 2016.07.14..
 */
public class Main {
    static final int CITY_LIMIT = 60;

    public static void main(String[] args) {
        CityReader cityReader = new CityReader(CITY_LIMIT);
        List<String> citiesToProcess = cityReader.readCityIds();

        TemperatureCollector temperatureCollector = new TemperatureCollector(citiesToProcess);

        ColdestAndHotestCityDTO coldestAndHotestCityDTO = temperatureCollector.collectByCallingWeatherService();
        printColdestAndHottestCity(coldestAndHotestCityDTO);
    }

    private static void printColdestAndHottestCity(ColdestAndHotestCityDTO coldestAndHotestCityDTO) {
        CityData coldestCity = coldestAndHotestCityDTO.getColdestCity();
        CityData hottestCity = coldestAndHotestCityDTO.getHottestCity();

        System.out.println("Lowest temperature: " + coldestCity.main.temp + ", City: " + coldestCity.name);
        System.out.println("Highest temperature: " + hottestCity.main.temp + ", City: " + hottestCity.name);
    }
}
