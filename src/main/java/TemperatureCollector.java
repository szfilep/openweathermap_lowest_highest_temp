import java.util.*;
import java.util.concurrent.*;

/**
 * Created by pollakt on 2016.07.14..
 */
public class TemperatureCollector {
    private CopyOnWriteArrayList<Future<CityData>> retrieverResults = new CopyOnWriteArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private CountDownLatch doneSignal;

    private Queue<String> citiesToProcess;
    private List<CityData> cityDatas;

    TemperatureCollector(List<String> cities) {
        int numberOfTasks = cities.size();
        doneSignal = new CountDownLatch(numberOfTasks);

        citiesToProcess = new LinkedList<String>(cities);
    }

    public ColdestAndHotestCityDTO collectByCallingWeatherService() {
        startWorkerThreads();

        waitForWorkerThreadsToFinish();

        sortCitiesToAscendingOrder();
        
        ColdestAndHotestCityDTO coldestAndHotestCityDTO = assembleMeasurementResult();

        return coldestAndHotestCityDTO;
    }

    private void startWorkerThreads() {
        while (citiesToProcess.isEmpty() == false) {
            String cityId = citiesToProcess.poll();

            WeatherRetriever weatherRetriever = new WeatherRetriever(cityId, doneSignal);
            Future<CityData> future = executor.submit(weatherRetriever);
            retrieverResults.add(future);
        }
        

    }

    private void waitForWorkerThreadsToFinish() {
        try {
            boolean isAllFinished = doneSignal.await(10, TimeUnit.MINUTES);
            if (!isAllFinished) {
                System.out.println("WARNING: not all threads finished in time.");
            } else {
                copyCityDataFromFutureObjects();
            }
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void copyCityDataFromFutureObjects() throws InterruptedException, ExecutionException {
        cityDatas = new ArrayList<>();
        for (Future<CityData> cityDataFuture : retrieverResults) {
            cityDatas.add(cityDataFuture.get());
        }
    }

    private void sortCitiesToAscendingOrder() {
        cityDatas.sort((CityData city1, CityData city2) -> {
            return Double.compare(city1.main.temp, city2.main.temp);
        });
    }

    private ColdestAndHotestCityDTO assembleMeasurementResult() {
        CityData coldestCity = cityDatas.get(0);
        CityData hottestCity = cityDatas.get(cityDatas.size()-1);

        return new ColdestAndHotestCityDTO(coldestCity, hottestCity);
    }
}
