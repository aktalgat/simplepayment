package com.talgat.simplepayment.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talgat.simplepayment.AddPaymentActivity;
import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.database.Payment;

/**
 * Created by Talgat on 27.05.2015.
 */
public class TabLog extends Fragment {

    private Payment selectedItem;
    private int selectedItemPosition;
    private static PaymentListAdapter adapter;
    private RecyclerView recyclerView;
    private static RecyclerView.Adapter recAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab_log, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPaymentActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) getView().findViewById(R.id.paymentList);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recAdapter = new PaymentRecAdapter(MainActivity.payments);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recAdapter);

        /*adapter = new PaymentListAdapter(getActivity(), R.layout.payment_list_item, MainActivity.payments);
        ListView listView = (ListView) getView().findViewById(R.id.paymentList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int position, long id) {
                selectedItem = getAdapter().getItem(position);
                selectedItemPosition = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setItems(R.array.edit_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            showAddPaymentActivity();
                        }
                        if (which == 1) {
                            AlertDialog.Builder builderDelete = new AlertDialog.Builder(getActivity());
                            builderDelete.setMessage(getString(R.string.delete_mess) + "?");
                            builderDelete.setCancelable(true);
                            builderDelete.setPositiveButton(getString(R.string.delete_but_title),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            MainActivity.paymentDataSource.deletePayment(selectedItem);
                                            adapter.remove(selectedItem);
                                            adapter.notifyDataSetChanged();

                                            MainActivity.updatePayments();
                                            TabReport.getAdapter().clear();
                                            TabReport.getAdapter().addAll(TabReport.getInstance().getReport());
                                            TabReport.getInstance().updateBalance();
                                        }
                                    });
                            builderDelete.setNegativeButton(getString(R.string.delete_cancel),
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = adapter.getItem(position);
                selectedItemPosition = position;
                showAddPaymentActivity();
            }
        });*/

        super.onActivityCreated(savedInstanceState);
    }

    public static PaymentListAdapter getAdapter() {
        return adapter;
    }

    public static PaymentRecAdapter getRecAdapter() {
        return (PaymentRecAdapter) recAdapter;
    }

    private void showAddPaymentActivity() {
        Intent intent = new Intent(getActivity(), AddPaymentActivity.class);
        intent.putExtra("selectedPayment", selectedItem);
        intent.putExtra("selectedPosition", selectedItemPosition);
        startActivity(intent);
    }
}
