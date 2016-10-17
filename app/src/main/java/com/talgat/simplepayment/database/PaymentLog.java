package com.talgat.simplepayment.database;

/**
 * Created by Talgat on 11.06.2015.
 */

public class PaymentLog {

    private String title;
    private long dateFrom;
    private long dateTill;
    private double sumIncome;
    private double sumExpense;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getDateTill() {
        return dateTill;
    }

    public void setDateTill(long dateTill) {
        this.dateTill = dateTill;
    }

    public double getSumIncome() {
        return sumIncome;
    }

    public void setSumIncome(double sumIncome) {
        this.sumIncome = sumIncome;
    }

    public double getSumExpense() {
        return sumExpense;
    }

    public void setSumExpense(double sumExpense) {
        this.sumExpense = sumExpense;
    }
}
