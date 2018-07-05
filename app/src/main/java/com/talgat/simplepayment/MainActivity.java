package com.talgat.simplepayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.talgat.simplepayment.database.Category;
import com.talgat.simplepayment.database.CategoryDataSource;
import com.talgat.simplepayment.database.Payment;
import com.talgat.simplepayment.database.PaymentsDataSource;
import com.talgat.simplepayment.tabs.CategoryListAdapter;
import com.talgat.simplepayment.tabs.TabLog;
import com.talgat.simplepayment.tabs.TabReport;
import com.talgat.simplepayment.tabs.ViewPagerAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity  {

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    //SlidingTabLayout tabs;
    private String titles[];
    private int numberOfTabs = 2;
    private Fragment fragments[];

    public static CategoryDataSource categoryDataSource;
    public static PaymentsDataSource paymentDataSource;

    private static CategoryListAdapter categoryExpAdapter;
    private static CategoryListAdapter categoryIncAdapter;
    public static CategoryListAdapter catIncomeAdapter;
    public static CategoryListAdapter catExpenseAdapter;

    public static SimpleDateFormat simpleDateFormat;
    public static DecimalFormat decimalFormat;

    public static List<Payment> payments;
    public static Map<Long, String> categoryMap;

    public static Locale currentLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLocale = getResources().getConfiguration().locale;

        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", currentLocale);
        decimalFormat = new DecimalFormat("#.##");

        categoryDataSource = new CategoryDataSource(this);
        categoryDataSource.open();

        paymentDataSource = new PaymentsDataSource(this);
        paymentDataSource.open();

        payments = paymentDataSource.getAllPayments();
        categoryMap = MainActivity.categoryDataSource.getCategoryMap();

        setTitle(getString(R.string.title_activity_main));

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        initTab();

        initCategoryAdapters();
    }

    private void initTab() {
        titles = new String[] {getString(R.string.tab_log), getString(R.string.tab_report)};
        fragments = new Fragment[] {new TabLog(), new TabReport()};

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), titles, numberOfTabs, fragments);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(pager);

        final TextView tab1 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab1.setText(getString(R.string.tab_log));
        tab1.setTextColor(getResources().getColor(R.color.primary_text));
        tabLayout.getTabAt(0).setCustomView(tab1);

        final TextView tab2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab2.setText(getString(R.string.tab_report));
        tabLayout.getTabAt(1).setCustomView(tab2);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (String.valueOf(tab.getText()).equals(getString(R.string.tab_log))) {
                    tab1.setTextColor(getResources().getColor(R.color.primary_text));
                    tab.setCustomView(tab1);
                } else {
                    tab2.setTextColor(getResources().getColor(R.color.primary_text));
                    tab.setCustomView(tab2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (String.valueOf(tab.getText()).equals(getString(R.string.tab_log))) {
                    tab1.setTextColor(getResources().getColor(R.color.tabsScrollColor1));
                    tab.setCustomView(tab1);
                } else {
                    tab2.setTextColor(getResources().getColor(R.color.tabsScrollColor1));
                    tab.setCustomView(tab2);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.setTabTextColors(getResources().getColor(R.color.tabsScrollColor1),
                getResources().getColor(R.color.primary_text));
    }

    private void initCategoryAdapters() {
        Category category = new Category();
        category.setId(-1);
        category.setName(getString(R.string.new_category));

        List<Category> exCategories = categoryDataSource.getExpCategories();
        exCategories.add(category);

        List<Category> inCategories = categoryDataSource.getIncCategories();
        inCategories.add(category);

        categoryExpAdapter = new CategoryListAdapter(this,
                android.R.layout.simple_spinner_item, exCategories);
        categoryIncAdapter = new CategoryListAdapter(this,
                android.R.layout.simple_spinner_item, inCategories);

        categoryExpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryIncAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adapters for Category list
        List<Category> categories = MainActivity.categoryDataSource.getIncCategories();
        catIncomeAdapter = new CategoryListAdapter(this, R.layout.category_list_item, categories);

        categories = MainActivity.categoryDataSource.getExpCategories();
        catExpenseAdapter = new CategoryListAdapter(this, R.layout.category_list_item, categories);
    }

    public static CategoryListAdapter getCategoryExpAdapter() {
        return categoryExpAdapter;
    }

    public static CategoryListAdapter getCategoryIncAdapter() {
        return categoryIncAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_clear_log) {
            showDeleteAlertDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoryDataSource.open();
        paymentDataSource.open();
    }

    @Override
    protected void onDestroy() {
        categoryDataSource.close();
        paymentDataSource.close();
        super.onDestroy();
    }

    private void showDeleteAlertDialog() {
        AlertDialog.Builder builderDelete = new AlertDialog.Builder(this);
        builderDelete.setMessage(getString(R.string.clear_log_mess) + "?");
        builderDelete.setCancelable(true);
        builderDelete.setPositiveButton(getString(R.string.clear_but_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        TabLog.getRecAdapter().clear();
                        paymentDataSource.deleteAllPayments();

                        updatePayments();
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

    public static void updatePayments() {
        payments = paymentDataSource.getAllPayments();
    }
}
