package com.talgat.simplepayment.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.ReportDetailActivity;
import com.talgat.simplepayment.database.Payment;
import com.talgat.simplepayment.database.PaymentLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Talgat on 27.05.2015.
 */
public class TabReport extends Fragment {

    private PaymentLog selectedItem;
    private static ReportListAdapter adapter;
    private TextView balanceText;
    private static TabReport tabReport;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab_report, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        List<PaymentLog> paymentLogs = getReport();
        adapter = new ReportListAdapter(getActivity(), R.layout.report_list_item, paymentLogs);
        ListView listView = (ListView) getView().findViewById(R.id.reportList);
        listView.setAdapter(adapter);

        tabReport = this;

        double balance = getBalance();
        balanceText = (TextView) getView().findViewById(R.id.balanceText);
        if (balance >= 0) {
            balanceText.setText(getString(R.string.balance) + ": " +
                    MainActivity.decimalFormat.format(balance));
            balanceText.setTextColor(getResources().getColor(R.color.sum_income));
        } else {
            balanceText.setText(getString(R.string.debt) + ": " +
                    MainActivity.decimalFormat.format(Math.abs(balance)));
            balanceText.setTextColor(getResources().getColor(R.color.sum_expense));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date dayDate = getDayDate();
                Date weekStartDate = getWeekStartDate();
                Date monthStartDate = getMonthStartDate();
                Date yearStartDate = getYearStartDate();

                Intent intent = new Intent(parent.getContext(), ReportDetailActivity.class);
                if (position == 0) {
                    intent.putExtra("startDate", dayDate);
                    intent.putExtra("endDate", dayDate);
                    startActivity(intent);
                }
                if (position == 1) {
                    intent.putExtra("startDate", weekStartDate);
                    intent.putExtra("endDate", dayDate);
                    startActivity(intent);
                }
                if (position == 2) {
                    intent.putExtra("startDate", monthStartDate);
                    intent.putExtra("endDate", dayDate);
                    startActivity(intent);
                }
                if (position == 3) {
                    intent.putExtra("startDate", yearStartDate);
                    intent.putExtra("endDate", dayDate);
                    startActivity(intent);
                }

            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    public static ReportListAdapter getAdapter() {
        return adapter;
    }

    public void updateBalance() {
        double balance = getBalance();

        if (balance >= 0) {
            balanceText.setText(getString(R.string.balance) + ": " +
                    MainActivity.decimalFormat.format(balance));
            balanceText.setTextColor(getResources().getColor(R.color.sum_income));
        } else {
            balanceText.setText(getString(R.string.debt) + ": " +
                    MainActivity.decimalFormat.format(Math.abs(balance)));
            balanceText.setTextColor(getResources().getColor(R.color.sum_expense));
        }
    }

    public static TabReport getInstance() {
        if (tabReport == null) {
            return null;
        }
        return tabReport;
    }

    public List<PaymentLog> getReport() {
        List<PaymentLog> paymentLogs = new ArrayList<>();

        Date dayDate = getDayDate();
        Date weekStartDate = getWeekStartDate();
        Date monthStartDate = getMonthStartDate();
        Date yearStartDate = getYearStartDate();

        double dayInc = 0;
        double dayExp = 0;
        double weekInc = 0;
        double weekExp = 0;
        double monthInc = 0;
        double monthExp = 0;
        double yearInc = 0;
        double yearExp = 0;

        for (Payment payment : MainActivity.payments) {
            Date pDate = new Date(payment.getPdate());
            if (dayDate.equals(pDate)) {
                if (payment.getType() == 1) {
                    dayInc += payment.getSum();
                } else {
                    dayExp += payment.getSum();
                }
            }
            if (weekStartDate.equals(pDate) ||
                    (pDate.after(weekStartDate) && pDate.before(dayDate)) ||
                    pDate.equals(dayDate)) {

                if (payment.getType() == 1) {
                    weekInc += payment.getSum();
                } else {
                    weekExp += payment.getSum();
                }
            }
            if (monthStartDate.equals(pDate) ||
                    (pDate.after(monthStartDate) && pDate.before(dayDate)) ||
                    pDate.equals(dayDate)) {

                if (payment.getType() == 1) {
                    monthInc += payment.getSum();
                } else {
                    monthExp += payment.getSum();
                }
            }
            if (yearStartDate.equals(pDate) ||
                    (pDate.after(yearStartDate) && pDate.before(dayDate)) ||
                    pDate.equals(dayDate)) {

                if (payment.getType() == 1) {
                    yearInc += payment.getSum();
                } else {
                    yearExp += payment.getSum();
                }
            }
        }

        PaymentLog day = new PaymentLog();
        day.setTitle(getString(R.string.today));
        day.setDateFrom(dayDate.getTime());
        day.setDateTill(dayDate.getTime());
        day.setSumExpense(dayExp);
        day.setSumIncome(dayInc);

        PaymentLog week = new PaymentLog();
        week.setTitle(getString(R.string.week));
        week.setDateFrom(weekStartDate.getTime());
        week.setDateTill(new Date().getTime());
        week.setSumExpense(weekExp);
        week.setSumIncome(weekInc);

        PaymentLog month = new PaymentLog();
        month.setTitle(getString(R.string.month));
        month.setDateFrom(monthStartDate.getTime());
        month.setDateTill(new Date().getTime());
        month.setSumExpense(monthExp);
        month.setSumIncome(monthInc);

        PaymentLog year = new PaymentLog();
        year.setTitle(getString(R.string.year));
        year.setDateFrom(yearStartDate.getTime());
        year.setDateTill(new Date().getTime());
        year.setSumExpense(yearExp);
        year.setSumIncome(yearInc);

        paymentLogs.add(day);
        paymentLogs.add(week);
        paymentLogs.add(month);
        paymentLogs.add(year);

        return paymentLogs;
    }

    public double getBalance() {
        double sumExpense = 0;
        double sumIncome = 0;
        for (Payment payment : MainActivity.payments) {
            if (payment.getType() == 1) {
                sumIncome += payment.getSum();
            } else {
                sumExpense += payment.getSum();
            }
        }

        return sumIncome - sumExpense;
    }

    private Date getDayDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    private Date getWeekStartDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        return c.getTime();
    }

    private Date getMonthStartDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    private Date getYearStartDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }
}
