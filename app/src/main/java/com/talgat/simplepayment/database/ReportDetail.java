package com.talgat.simplepayment.database;

/**
 * Created by Talgat on 01.07.2015.
 */
public class ReportDetail {

    private String categoryName;
    private double sum;
    private double percent;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
