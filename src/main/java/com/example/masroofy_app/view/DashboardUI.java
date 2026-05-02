package com.example.masroofy_app.view;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.Map;
import java.util.function.Function;

public class DashboardUI {

    private final Label dailyLimitLabel;
    private final Label balanceLabel;
    private final Label daysLabel;
    private final PieChart pieChart;

    public DashboardUI(Label dailyLimitLabel,
                       Label balanceLabel,
                       Label daysLabel,
                       PieChart pieChart) {

        this.dailyLimitLabel = dailyLimitLabel;
        this.balanceLabel = balanceLabel;
        this.daysLabel = daysLabel;
        this.pieChart = pieChart;
    }


    public void updateDailyLimit(double value) {
        dailyLimitLabel.setText(String.format("%.2f EGP", value));
    }

    public void updateBalance(double value) {
        balanceLabel.setText(String.format("%.2f EGP", value));
    }
    public void updateDays(int days) {
        daysLabel.setText(days + " Days Left");
    }
    public void updateChart(Map<Integer, Float> data,
                            Function<Integer, String> resolver) {

        pieChart.getData().clear();

        for (Map.Entry<Integer, Float> entry : data.entrySet()) {

            String name = resolver.apply(entry.getKey());

            PieChart.Data slice =
                    new PieChart.Data(name, entry.getValue());

            pieChart.getData().add(slice);
        }
    }
}