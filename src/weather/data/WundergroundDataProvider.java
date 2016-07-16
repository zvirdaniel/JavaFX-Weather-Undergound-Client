package weather.data;

import javafx.scene.image.Image;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Daniel Zvir on 15.07.2016.
 * This class provides all the weather data from selected source (sent via parameter).
 */
public class WundergroundDataProvider {
    private double currentTempCelsius;
    private double currentWindKph;
    private String currentWeatherString;
    private String currentWeatherImageURL;
    private String currentHumidity;

    public WundergroundDataProvider(String url) {
        JSONObject jsonData = null;
        try {
            jsonData = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject currentWeather = (jsonData != null) ? jsonData.getJSONObject("current_observation") : null;
        currentWindKph = (currentWeather != null) ? currentWeather.getDouble("wind_kph") : 0;
        currentTempCelsius = (currentWeather != null) ? currentWeather.getDouble("temp_c") : 0;
        currentWeatherString = (currentWeather != null) ? currentWeather.getString("weather") : null;
        currentWeatherImageURL = (currentWeather != null) ? currentWeather.getString("icon_url") : null;
        currentHumidity = (currentWeather != null) ? currentWeather.getString("relative_humidity") : null;
    }

    /**
     * This method downloads the image for current weather from the internet,
     * stores it in the application folder as resources/images/current_weather.gif,
     * and returns the image as an Image object.
     *
     * @return Image object of current weather
     * @throws IOException
     */
    public Image getCurrentWeatherImage() {
        try {
            URL url = new URL(currentWeatherImageURL);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream("resources/images/current_weather.gif");
            fos.write(response);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Image("file:resources/images/current_weather.gif");
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

    public String getCurrentHumidity() {
        return currentHumidity;
    }

    @Override
    public String toString() {
        return "WundergroundDataProvider{" +
                "currentTempCelsius=" + currentTempCelsius +
                ", currentWindKph=" + currentWindKph +
                ", currentWeatherString='" + currentWeatherString + '\'' +
                ", currentWeatherImageURL='" + currentWeatherImageURL + '\'' +
                ", currentHumidity='" + currentHumidity + '\'' +
                '}';
    }
}