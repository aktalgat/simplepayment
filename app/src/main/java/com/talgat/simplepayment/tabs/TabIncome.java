package com.talgat.simplepayment.tabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.talgat.simplepayment.MainActivity;
import com.talgat.simplepayment.R;
import com.talgat.simplepayment.database.Category;
import com.talgat.simplepayment.database.Payment;

import java.util.List;

/**
 * Created by Talgat on 25.05.2015.
 */
public class TabIncome extends Fragment {

    private Category selectedItem;
    //private static CategoryListAdapter adapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_income, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        /*List<Category> categories = MainActivity.categoryDataSource.getIncCategories();

        adapter = new CategoryListAdapter(getActivity(), R.layout.category_list_item, categories);*/

        listView = (ListView) getView().findViewById(R.id.incCatList);
        listView.setAdapter(getAdapter());

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (Category) getAdapter().getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setItems(R.array.edit_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            buildDialog();
                        }
                        if (which == 1) {
                            AlertDialog.Builder builderDelete = new AlertDialog.Builder(getActivity());
                            builderDelete.setMessage(getString(R.string.delete_mess) + " " +
                                    selectedItem.getName() + "?");
                            builderDelete.setPositiveButton(getString(R.string.delete_but_title),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MainActivity.categoryDataSource.deleteCategory(selectedItem);
                                            getAdapter().remove(selectedItem);
                                            getAdapter().notifyDataSetChanged();
                                        }
                                    });
                            builderDelete.setNegativeButton(getString(R.string.delete_cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builderDelete.setCancelable(true);
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
                selectedItem = (Category) getAdapter().getItem(position);
                buildDialog();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    public static CategoryListAdapter getAdapter() {

        return MainActivity.catIncomeAdapter;
    }

    private void buildDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(
                R.layout.add_category, null);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupCat);
        if (selectedItem.getType() == 1) {
            radioGroup.check(R.id.radioIncomeCat);
        }

        EditText editText = (EditText) view.findViewById(R.id.editCatName);
        editText.setText(selectedItem.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.title_activity_add_category));
        builder.setPositiveButton(getString(R.string.add_but),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton(getString(R.string.delete_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();

        Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) view.findViewById(R.id.editCatName);
                String name = editText.getText().toString();
                int type = 0;
                int id = radioGroup.getCheckedRadioButtonId();
                if (id != -1) {
                    if (id == R.id.radioIncomeCat) {
                        type = 1;
                    }
                }
                if (name.length() == 0) {

                } else {
                    MainActivity.categoryDataSource.updateCategory(selectedItem,
                            selectedItem.getId(), type, name);

                    TabExpense.getAdapter().clear();
                    List<Category> categoriesExp = MainActivity.categoryDataSource.getExpCategories();
                    TabExpense.getAdapter().addAll(categoriesExp);
                    TabExpense.getAdapter().notifyDataSetChanged();

                    TabIncome.getAdapter().clear();
                    List<Category> categoriesInc = MainActivity.categoryDataSource.getIncCategories();
                    TabIncome.getAdapter().addAll(categoriesInc);
                    TabIncome.getAdapter().notifyDataSetChanged();

                    TabLog.getAdapter().clear();
                    List<Payment> payments = MainActivity.paymentDataSource.getAllPayments();
                    TabLog.getAdapter().addAll(payments);
                    TabLog.getAdapter().notifyDataSetChanged();

                    alert.cancel();
                }
            }
        });
    }
}
