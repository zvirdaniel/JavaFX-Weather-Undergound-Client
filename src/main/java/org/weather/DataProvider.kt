package org.weather

import org.weather.data.WundergroundProvider
import org.weather.utils.BasicUtils.showError


/**
 * Created by Daniel Zvir on 21.9.16.
 */
object DataProvider {
    val currentKey = "5d2ff9078b329570"

    var state: String? = null
        set(value) {
            if (value?.toLowerCase() == "czech republic") {
                field = "CZ"
            } else {
                field = value
            }
        }

    var city: String? = null
        set(value) {
            if (value == "Kupka") {
                showError("Kupka", "Počkej až se dostanu k moci!")
            } else {
                field = value
            }
        }

    var wunderground: WundergroundProvider? = null
        set(value) {
            field = value
            println("Weather data updated.")
        }
}