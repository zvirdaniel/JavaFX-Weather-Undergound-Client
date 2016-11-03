package org.weather.data

import javafx.scene.image.Image
import org.apache.commons.io.IOUtils
import org.json.JSONObject

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

/**
 * Created by Daniel Zvir on 15.07.2016.
 * This class downloads current org.weather data from Weather Underground, converts them from JSON to POJO,
 * including one JavaFX Image object, and makes all this data available via getters.
 */

class WundergroundProvider
/**
 * @param api_key     every developer needs his own api_key, suitable for his needs
 * *
 * @param api_country make sure it exists on the website, no error checking
 * *
 * @param api_city    make sure it exists on the website, no error checking
 * *
 * @throws IllegalArgumentException if specified location is not found
 */
@Throws(IllegalArgumentException::class)
constructor(api_key: String, api_country: String, api_city: String) {
    val currentTempCelsius: Double
    val currentTempFeelsLikeCelsius: Double
    val currentWindKph: Double
    val currentWeatherString: String?
    val currentWindDirection: String?
    val countryName: String?
    val city: String?
    private val currentWeather: JSONObject?

    init {
        val url = makeWundergroundURL(api_key, api_country, api_city)

        // Create a JSON object from url (download raw text data, convert it to JSON object).
        val data: String?
        var jsonData: JSONObject? = null
        var locationData: JSONObject? = null
        try {
            data = IOUtils.toString(URL(url), Charset.forName("UTF-8"))
            jsonData = JSONObject(data!!)
            locationData = jsonData.getJSONObject("location")
        } catch (e: Exception) {
            if (e.toString().contains("java.net.UnknownHostException: api.wunderground.com")) {
                System.err.println("No internet connection!")
                throw IllegalArgumentException("No internet connection!")
            }

            if (e.message!!.contains("not found")) {
                System.err.println("Location not found!")
                throw IllegalArgumentException("Location not found!")
            }

            e.printStackTrace()
        }

        currentWeather = jsonData?.getJSONObject("current_observation")
        currentWindKph = if (currentWeather != null) currentWeather.getDouble("wind_kph") else 0.0
        currentWindDirection = currentWeather?.getString("wind_dir")
        currentTempCelsius = if (currentWeather != null) currentWeather.getDouble("temp_c") else 0.0
        currentTempFeelsLikeCelsius = if (currentWeather != null) currentWeather.getDouble("feelslike_c") else 0.0
        currentWeatherString = currentWeather?.getString("weather")
        countryName = locationData?.getString("country_name")
        city = if (locationData != null) locationData.getString("city") else null
    }


    private fun makeWundergroundURL(api_key: String, api_country: String, api_city: String): String {
        return "http://api.wunderground.com/api/$api_key/geolookup/conditions/forecast/q/$api_country/$api_city.json"
    }

    /**
     * Downloads the image file from the internet, and returns it as an Image object.

     * @return (JavaFX) Image object of current org.weather
     */
    val currentWeatherImage: Image?
        get() {
            var img: Image? = null
            try {
                var imageURL: URL? = null
                if (currentWeather != null) imageURL = URL(currentWeather.getString("icon_url"))
                var `in`: InputStream? = null
                if (imageURL != null) `in` = BufferedInputStream(imageURL.openStream())
                if (`in` != null) img = Image(`in`)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return img
        }
}
