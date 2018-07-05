package com.talgat.simplepayment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.talgat.simplepayment.database.Category;
import com.talgat.simplepayment.tabs.SlidingTabLayout;
import com.talgat.simplepayment.tabs.TabExpense;
import com.talgat.simplepayment.tabs.TabIncome;
import com.talgat.simplepayment.tabs.ViewPagerAdapter;

public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private String titles[];
    private Fragment fragments[];
    private int numberOfTabs = 2;
    private CategoryActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titles = new String[] {getString(R.string.tab_expense), getString(R.string.tab_income)};
        fragments = new Fragment[] {new TabExpense(), new TabIncome()};

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), titles, numberOfTabs, fragments);

        pager = (ViewPager) findViewById(R.id.catPager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.catTabs);
        tabs.setDistributeEvenly(true);


        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor2);
            }
        });

        tabs.setViewPager(pager);

        instance = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_category) {
            showAddCategoryDialog(this, pager.getCurrentItem(), null, null);
            return true;
        }
        if (id == R.id.action_clear_cat) {
            showDeleteAllCatAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void showAddCategoryDialog(Context context, int paymentType,
                                             final Spinner spinner, final RadioGroup radioPayment) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(
                R.layout.add_category, null);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupCat);
        if (paymentType == 1) {
            radioGroup.check(R.id.radioIncomeCat);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle(context.getString(R.string.title_activity_add_category));
        builder.setPositiveButton(context.getString(R.string.add_but),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton(context.getString(R.string.delete_cancel),
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
                    //editText.setError(getString(R.string.cat_name_wrong));
                    //editText.getBackground().setColorFilter(getResources().getColor(R.color.error_color), PorterDuff.Mode.MULTIPLY);
                } else {
                    Category cat = MainActivity.categoryDataSource.createCategory(type, name);
                    if (type == 0) {
                        TabExpense.getAdapter().add(cat);
                        TabExpense.getAdapter().notifyDataSetChanged();
                        MainActivity.getCategoryExpAdapter().add(cat, 0);
                        MainActivity.getCategoryExpAdapter().notifyDataSetChanged();
                        if (spinner != null) {
                            spinner.setSelection(AddPaymentActivity.getCategoryPos(
                                    MainActivity.getCategoryExpAdapter(),
                                    cat.getId()));
                        }
                        if (radioPayment != null) {
                            radioPayment.check(R.id.radioExpense);
                        }
                    } else {
                        TabIncome.getAdapter().add(cat);
                        TabIncome.getAdapter().notifyDataSetChanged();
                        MainActivity.getCategoryIncAdapter().add(cat, 0);
                        MainActivity.getCategoryIncAdapter().notifyDataSetChanged();
                        if (spinner != null) {
                            spinner.setSelection(AddPaymentActivity.getCategoryPos(
                                    MainActivity.getCategoryIncAdapter(),
                                    cat.getId()));
                        }
                        if (radioPayment != null) {
                            radioPayment.check(R.id.radioIncome);
                        }
                    }
                    alert.cancel();
                }
            }
        });
    }

    private void showDeleteAllCatAlertDialog() {
        AlertDialog.Builder builderDelete = new AlertDialog.Builder(this);
        builderDelete.setMessage(getString(R.string.clear_cat_mess) + "?");
        builderDelete.setCancelable(true);
        builderDelete.setPositiveButton(getString(R.string.clear_cat_but_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        MainActivity.categoryDataSource.deleteAllCategories();

                        TabExpense.getAdapter().clear();
                        TabExpense.getAdapter().notifyDataSetChanged();

                        TabIncome.getAdapter().clear();
                        TabIncome.getAdapter().notifyDataSetChanged();

                        MainActivity.getCategoryExpAdapter().clear();
                        MainActivity.getCategoryIncAdapter().clear();
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
