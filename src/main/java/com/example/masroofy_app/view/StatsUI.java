package com.example.masroofy_app.view;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class StatsUI {

    private PieChart pieChart;
    private Label totalLabel;


    public StatsUI(PieChart pieChart, Label totalLabel) {
        this.pieChart = pieChart;
        this.totalLabel = totalLabel;
    }



    public void setTotal(String total) {
        totalLabel.setText(total);
    }

    public void setChartData(double food, double transport, double entertainment) {
        pieChart.getData().clear();

        pieChart.getData().add(new PieChart.Data("Food", food));
        pieChart.getData().add(new PieChart.Data("Transportation", transport));
        pieChart.getData().add(new PieChart.Data("Entertainment", entertainment));
    }

    public void clearChart() {
        pieChart.getData().clear();
    }
}