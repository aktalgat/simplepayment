package com.talgat.simplepayment.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.database.PaymentLog;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Talgat on 11.06.2015.
 */
public class ReportListAdapter extends ArrayAdapter<PaymentLog> {

    private List<PaymentLog> paymentLogs;
    private final Context context;

    public ReportListAdapter(Context context, int resource,
                             List<PaymentLog> objects) {
        super(context, resource, objects);

        this.paymentLogs = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View newRow = convertView;
        if (newRow == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newRow = vi.inflate(R.layout.report_list_item, parent, false);
        }

        PaymentLog paymentLog = paymentLogs.get(position);

        TextView payLogName = (TextView) newRow.findViewById(R.id.pay_log_name);
        payLogName.setText(paymentLog.getTitle());

        TextView payLogPeriod = (TextView) newRow.findViewById(R.id.pay_log_period);
        Date date1 = new Date(paymentLog.getDateFrom());
        Date date2 = new Date(paymentLog.getDateTill());
        if (date1.equals(date2)) {
            payLogPeriod.setText(MainActivity.simpleDateFormat.format(date1));
        } else {
            payLogPeriod.setText(MainActivity.simpleDateFormat.format(date1) + "-" + MainActivity.simpleDateFormat.format(date2));
        }

        TextView paySumIncome = (TextView) newRow.findViewById(R.id.pay_sum_income);
        paySumIncome.setText(String.valueOf(MainActivity.decimalFormat.format(paymentLog.getSumIncome())));

        TextView paySumExpense = (TextView) newRow.findViewById(R.id.pay_sum_expense);
        paySumExpense.setText(String.valueOf(MainActivity.decimalFormat.format(paymentLog.getSumExpense())));

        return newRow;
    }

    @Override
    public void addAll(Collection<? extends PaymentLog> collection) {
        for (PaymentLog paymentLog : collection) {
            add(paymentLog);
        }
    }
}
