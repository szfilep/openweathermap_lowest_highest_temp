import com.google.gson.Gson;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by pollakt on 2016.07.14..
 */
public class WeatherRetriever implements Callable<CityData> {
    private String cityId;
    private CountDownLatch doneSignal;

    public WeatherRetriever(String cityId, CountDownLatch doneSignal) {
        this.cityId = cityId;
        this.doneSignal = doneSignal;
    }

    private String createRequestURL() {
        String prefix = "http://api.openweathermap.org/data/2.5/weather?";
        String cityIdentifierSection = "id=" + cityId;
        String temperatureUnitSection = "units=metric";
        String appIdentifierSection = "appid=09732f1a4b272ca3a84bc657692b93fc";

        return prefix + cityIdentifierSection + "&" + temperatureUnitSection + "&" + appIdentifierSection;
    }

    @Override
    public CityData call() throws Exception {
        CityData temperatureInformation = null;
        try {
            String requestURL = createRequestURL();
            String jsonResponse = Request.Get(requestURL).execute().returnContent().toString();

            Gson gson = new Gson();
            temperatureInformation = gson.fromJson(jsonResponse, CityData.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            doneSignal.countDown();
        }
        return temperatureInformation;
    }
}