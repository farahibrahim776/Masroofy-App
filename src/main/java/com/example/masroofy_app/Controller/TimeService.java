package com.example.masroofy_app.Controller;

import java.time.LocalDate;

/**
 * FIX #8: TimeService is now actually used — BudgetManager.checkNewDay() delegates here
 * instead of duplicating this logic inline. This is the single source of truth for
 * determining whether a new calendar day has started since the last update.
 */
public class TimeService {

    public static boolean isNewDay(LocalDate lastUpdate) {
        LocalDate today = LocalDate.now();
        return !today.equals(lastUpdate);
    }
}