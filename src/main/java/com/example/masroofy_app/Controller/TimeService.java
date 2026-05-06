package com.example.masroofy_app.Controller;

import java.time.LocalDate;

/**
 * Utility service class responsible for handling date-related operations.
 * Used to determine whether a new day has started compared to the last update.
 */
public class TimeService {

    /**
     * Checks whether the current date is different from the last update date.
     *
     * @param lastUpdate the last recorded update date
     * @return true if today is different from lastUpdate, false otherwise
     */
    public static boolean isNewDay(LocalDate lastUpdate) {
        LocalDate today = LocalDate.now();
        return !today.equals(lastUpdate);
    }
}
