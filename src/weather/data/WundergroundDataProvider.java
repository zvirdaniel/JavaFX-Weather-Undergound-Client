package weather.data;

import javafx.scene.image.Image;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Daniel Zvir on 15.07.2016.
 * This class downloads current weather data from Weather Underground, converts them from JSON to POJO,
 * including one JavaFX Image object, and makes all this data available via getters.
 */
public class WundergroundDataProvider {
    private double currentTempCelsius;
    private double currentTempFeelsLikeCelsius;
    private double currentWindKph;
    private String currentWeatherString;
    private String currentWindDirection;
    private String countryName;
    private String city;
    private Image img;
    private JSONObject currentWeather;

    /**
     * @param api_key     every developer needs his own api_key, suitable for his needs
     * @param api_country make sure it exists on the website
     * @param api_city    make sure it exists on the website
     */
    public WundergroundDataProvider(String api_key, String api_country, String api_city) {
        String url = "http://api.wunderground.com/api/" + api_key + "/geolookup/conditions/forecast/q/"
                + api_country + '/' + api_city + ".json";

        JSONObject jsonData = null;
        try {
            // Create a JSON object from url (download raw text data, convert it to JSON object).
            jsonData = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * This ugly thing is to prevent null pointer exceptions. It was automatically generated,
         * and it seems like a better solution to me, than having if/else for every single method.
         *
         * It makes my IntelliJ happy.
         */
        JSONObject locationData = (jsonData != null) ? jsonData.getJSONObject("location") : null;
        currentWeather = (jsonData != null) ? jsonData.getJSONObject("current_observation") : null;
        currentWindKph = (currentWeather != null) ? currentWeather.getDouble("wind_kph") : 0;
        currentWindDirection = (currentWeather != null) ? currentWeather.getString("wind_dir") : null;
        currentTempCelsius = (currentWeather != null) ? currentWeather.getDouble("temp_c") : 0;
        currentTempFeelsLikeCelsius = (currentWeather != null) ? currentWeather.getDouble("feelslike_c") : 0;
        currentWeatherString = (currentWeather != null) ? currentWeather.getString("weather") : null;
        countryName = (locationData != null) ? locationData.getString("country_name") : null;
        city = (locationData != null) ? locationData.getString("city") : null;
    }

    /**
     * Downloads the image file from the internet, and returns it as an Image object.
     * @return (JavaFX) Image object of current weather
     */
    public Image getCurrentWeatherImage() {
        try {
            URL imageURL = null;
            if (currentWeather != null) {
                imageURL = new URL(currentWeather.getString("icon_url"));
            }
            InputStream in = new BufferedInputStream(imageURL.openStream());
            img = new Image(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public double getCurrentTempCelsius() {
        return currentTempCelsius;
    }

    public double getCurrentTempFeelsLikeCelsius() {
        return currentTempFeelsLikeCelsius;
    }

    public double getCurrentWindKph() {
        return currentWindKph;
    }

    public String getCurrentWeatherString() {
        return currentWeatherString;
    }

    public String getCurrentWindDirection() {
        return currentWindDirection;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "WundergroundDataProvider{" +
                "currentTempCelsius=" + currentTempCelsius +
                ", currentTempFeelsLikeCelsius=" + currentTempFeelsLikeCelsius +
                ", currentWindKph=" + currentWindKph +
                ", currentWeatherString='" + currentWeatherString + '\'' +
                ", currentWindDirection='" + currentWindDirection + '\'' +
                ", countryName='" + countryName + '\'' +
                ", city='" + city + '\'' +
                ", currentWeather=" + currentWeather +
                '}';
    }
}