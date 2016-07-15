package weather.data;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Daniel Zvir on 15.07.2016.
 * This class is supposed to provide all the weather data from selected source.
 */
public class WundergroundDataProvider {
    private double currentTempCelsius;
    private double currentWindKph;
    private String currentWeatherString;
    private String currentWeatherIcon;
    private String currentHumidity;

    public WundergroundDataProvider(String url) {
        JSONObject jsonData = null;
        try {
            jsonData = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject currentWeather = jsonData.getJSONObject("current_observation");
        currentWindKph = currentWeather.getDouble("wind_kph");
        currentTempCelsius = currentWeather.getDouble("temp_c");
        currentWeatherString = currentWeather.getString("weather");
        currentWeatherIcon = currentWeather.getString("icon_url");
        currentHumidity = currentWeather.getString("relative_humidity");
    }

    public double getCurrentTempCelsius() {
        return currentTempCelsius;
    }

    public double getCurrentWindKph() {
        return currentWindKph;
    }

    public String getCurrentWeatherString() {
        return currentWeatherString;
    }

    public String getCurrentWeatherIcon() {
        return currentWeatherIcon;
    }

    public String getCurrentHumidity() {
        return currentHumidity;
    }

    @Override
    public String toString() {
        return "WundergroundDataProvider{" +
                "currentTempCelsius=" + currentTempCelsius +
                ", currentWindKph=" + currentWindKph +
                ", currentWeatherString='" + currentWeatherString + '\'' +
                ", currentWeatherIcon='" + currentWeatherIcon + '\'' +
                ", currentHumidity='" + currentHumidity + '\'' +
                '}';
    }
}
