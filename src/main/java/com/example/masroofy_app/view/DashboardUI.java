package view;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.Map;

public class DashboardUI {

    @FXML
    private Label dailyLimitLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label daysLabel;

    @FXML
    private PieChart pieChart;

    @FXML
    public void initialize() {
        // فاضي: مفيش بيانات تجريبية هنا
        // بس ممكن تظبطي شكل الـUI لو حابة
    }

    // ====== Methods to update UI ======

    public void setUserData(float limit, double balance, int daysLeft, Map<String, Float> expenses) {
        showDailyLimit(limit);
        showBalance(balance);
        showDaysLeft(daysLeft);
        showPieChart(expenses);
    }

    public void showDailyLimit(float limit) {
        dailyLimitLabel.setText(limit + " EGP");
    }

    public void showBalance(double balance) {
        balanceLabel.setText(balance + " EGP");
    }

    public void showDaysLeft(int days) {
        daysLabel.setText(String.valueOf(days));
    }

    public void showPieChart(Map<String, Float> data) {

        pieChart.getData().clear();

        for (Map.Entry<String, Float> entry : data.entrySet()) {
            pieChart.getData().add(
                    new PieChart.Data(entry.getKey(), entry.getValue())
            );
        }
    }
}