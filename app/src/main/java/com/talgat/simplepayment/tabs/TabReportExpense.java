package com.talgat.simplepayment.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.ReportDetailActivity;
import com.talgat.simplepayment.database.ReportDetail;

import java.util.Date;
import java.util.List;

/**
 * Created by Talgat on 30.06.2015.
 */
public class TabReportExpense extends Fragment {

    private RecyclerView recyclerView;
    private static RecyclerView.Adapter recAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab_report_expense, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Date startDate = (Date) intent.getSerializableExtra("startDate");
        Date endDate = (Date) intent.getSerializableExtra("endDate");

        EditText editTextStart = (EditText) getView().findViewById(R.id.editTextStart);
        editTextStart.setInputType(InputType.TYPE_NULL);
        if (startDate != null) {
            editTextStart.setText(MainActivity.simpleDateFormat.format(startDate));
        }

        EditText editTextEnd = (EditText) getView().findViewById(R.id.editTextEnd);
        editTextEnd.setInputType(InputType.TYPE_NULL);
        if (endDate != null) {
            editTextEnd.setText(MainActivity.simpleDateFormat.format(endDate));
        }

        List<ReportDetail> reportDetails = MainActivity.paymentDataSource
                .getReportDetails(startDate, endDate, 0);

        TextView textViewAll = (TextView) getView().findViewById(R.id.textViewAll);
        textViewAll.setText(getString(R.string.sum_of_all) + " " +
                MainActivity.decimalFormat.format(
                        ReportDetailActivity.getSumReportDetails(reportDetails)));

        recyclerView = (RecyclerView) getView().findViewById(R.id.reportList);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recAdapter = new ReportDetailRecAdapter(reportDetails);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recAdapter);

        super.onActivityCreated(savedInstanceState);
    }
}
