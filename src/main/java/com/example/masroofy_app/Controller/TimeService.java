package com.example.masroofy_app.Controller;

import java.time.LocalDate;

public class TimeService {

    public static boolean isNewDay(LocalDate lastUpdate) {
        LocalDate today = LocalDate.now();
        return !today.equals(lastUpdate);
    }
}
