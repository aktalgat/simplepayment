package com.talgat.simplepayment.tabs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.database.Payment;
import com.talgat.simplepayment.database.ReportDetail;

import java.util.Date;
import java.util.List;

/**
 * Created by Talgat on 01.07.2015.
 */
public class ReportDetailRecAdapter extends RecyclerView.Adapter<ReportDetailRecAdapter.ViewHolder> {

    private List<ReportDetail> reportDetails;

    public ReportDetailRecAdapter(List<ReportDetail> reportDetails) {
        this.reportDetails = reportDetails;
    }

    @Override
    public ReportDetailRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_detail_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportDetailRecAdapter.ViewHolder holder, int position) {
        ReportDetail reportDetail = reportDetails.get(position);
        holder.catName.setText(reportDetail.getCategoryName());
        holder.catSum.setText(MainActivity.decimalFormat.format(reportDetail.getSum()));
        holder.catPercentage.setText(MainActivity.decimalFormat.format(reportDetail.getPercent()) + "%");
    }

    @Override
    public int getItemCount() {
        return reportDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView catName;
        public TextView catSum;
        public TextView catPercentage;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            catName = (TextView) itemView.findViewById(R.id.catName);
            catSum = (TextView) itemView.findViewById(R.id.catSum);
            catPercentage = (TextView) itemView.findViewById(R.id.catPercentage);
        }
    }
}
