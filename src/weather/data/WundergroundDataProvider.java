package weather.data;

import javafx.scene.image.Image;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

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
    private Path tmpDir; // path to temporary directory (assigned by method)

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
        JSONObject currentWeather = (jsonData != null) ? jsonData.getJSONObject("current_observation") : null;
        JSONObject locationData = (jsonData != null) ? jsonData.getJSONObject("location") : null;
        currentWindKph = (currentWeather != null) ? currentWeather.getDouble("wind_kph") : 0;
        currentWindDirection = (currentWeather != null) ? currentWeather.getString("wind_dir") : null;
        currentTempCelsius = (currentWeather != null) ? currentWeather.getDouble("temp_c") : 0;
        currentTempFeelsLikeCelsius = (currentWeather != null) ? currentWeather.getDouble("feelslike_c") : 0;
        currentWeatherString = (currentWeather != null) ? currentWeather.getString("weather") : null;
        countryName = (locationData != null) ? locationData.getString("country_name") : null;
        city = (locationData != null) ? locationData.getString("city") : null;

        // This downloads the image for current weather from the internet.
        try {
            URL imageURL = null;
            if (currentWeather != null) {
                imageURL = new URL(currentWeather.getString("icon_url"));
            }
            InputStream in = new BufferedInputStream(imageURL.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();

            // How convenient! A method for temporary directories!
            tmpDir = Files.createTempDirectory("DunoWeatherApp-");
            System.out.println("Temp dir created at: " + tmpDir);

            FileOutputStream fos = new FileOutputStream(tmpDir + "/current_weather.gif");
            fos.write(response);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This little thing deletes the image with current weather after the app is closed.
        File weatherImage = new File(tmpDir + "/current_weather.gif");
        weatherImage.deleteOnExit();
    }

    /**
     * @return (JavaFX) Image object of current weather
     */
    public Image getCurrentWeatherImage() {
        return new Image("file:" + tmpDir + "/current_weather.gif");
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
                ", tmpDir=" + tmpDir +
                '}';
    }
}