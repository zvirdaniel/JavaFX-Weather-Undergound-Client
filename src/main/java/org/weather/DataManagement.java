package org.weather;

import org.weather.data.WundergroundProvider;

/**
 * Created by zvird on 21.9.16.
 */
public class DataManagement {
    // This is a singleton
    private static DataManagement ourInstance = new DataManagement();

    public static DataManagement getInstance() {
        return ourInstance;
    }

    private WundergroundProvider wunderground;
    private String state;
    private String city;

    private DataManagement() {

    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getCurrentKey() {
        return "5d2ff9078b329570";
    }

    public WundergroundProvider getWunderground() {
        return wunderground;
    }

    public void setWunderground(WundergroundProvider wunderground) {
        this.wunderground = wunderground;
        System.out.println("Data updated.");
    }
}
