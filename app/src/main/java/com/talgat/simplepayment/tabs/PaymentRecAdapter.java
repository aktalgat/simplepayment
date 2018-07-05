package com.talgat.simplepayment.tabs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talgat.simplepayment.AddPaymentActivity;
import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.database.Payment;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Talgat on 16.06.2015.
 */
public class PaymentRecAdapter extends RecyclerView.Adapter<PaymentRecAdapter.ViewHolder> {

    private List<Payment> payments;
    private Payment selectedItem;
    private int selectedItemPosition;

    public PaymentRecAdapter(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public PaymentRecAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Payment payment = payments.get(position);
        holder.payName.setText(MainActivity.categoryMap.get(payment.getCategory()));
        holder.payComment.setText(payment.getComment());
        holder.paySum.setText(String.valueOf(MainActivity.decimalFormat.format(payment.getSum())));
        Date date = new Date(payment.getPdate());
        holder.payDate.setText(MainActivity.simpleDateFormat.format(date));
        if (payment.getType() == 1) {
            holder.imageView.setImageResource(R.drawable.ic_content_income);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_content_expense);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = getItem(position);
                selectedItemPosition = position;
                showAddPaymentActivity(v.getContext());
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedItem = getItem(position);
                selectedItemPosition = position;
                final Context context = v.getContext();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setItems(R.array.edit_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showAddPaymentActivity(context);
                        }
                        if (which == 1) {
                            AlertDialog.Builder builderDelete = new AlertDialog.Builder(context);
                            builderDelete.setMessage(context.getString(R.string.delete_mess) + "?");
                            builderDelete.setCancelable(true);
                            builderDelete.setPositiveButton(context.getString(R.string.delete_but_title),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            MainActivity.paymentDataSource.deletePayment(selectedItem);
                                            removeData(selectedItemPosition);
                                            notifyItemRemoved(selectedItemPosition);

                                            MainActivity.updatePayments();
                                            TabReport.getAdapter().clear();
                                            TabReport.getAdapter().addAll(TabReport.getInstance().getReport());
                                            TabReport.getInstance().updateBalance();
                                        }
                                    });
                            builderDelete.setNegativeButton(context.getString(R.string.delete_cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.cancel();
                                        }
                                    });

                            builderDelete.create();
                            builderDelete.show();
                        }

                    }
                });

                builder.create();
                builder.show();

                return true;
            }
        });
    }

    public void addData(Payment payment, int position) {
        payments.add(position, payment);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        payments.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        payments.clear();
    }

    public Payment getItem(int position) {
        return payments.get(position);
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView payName;
        public TextView payComment;
        public TextView paySum;
        public TextView payDate;
        public ImageView imageView;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            payName = (TextView) itemView.findViewById(R.id.pay_name);
            payComment = (TextView) itemView.findViewById(R.id.pay_comment);
            paySum = (TextView) itemView.findViewById(R.id.pay_sum);
            payDate = (TextView) itemView.findViewById(R.id.pay_date);
            imageView = (ImageView) itemView.findViewById(R.id.list_image);
        }
    }

    private void showAddPaymentActivity(Context context) {
        Intent intent = new Intent(context, AddPaymentActivity.class);
        intent.putExtra("selectedPayment", selectedItem);
        intent.putExtra("selectedPosition", selectedItemPosition);
        context.startActivity(intent);
    }
}
