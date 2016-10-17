package com.talgat.simplepayment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.talgat.simplepayment.database.ReportDetail;
import com.talgat.simplepayment.tabs.TabReportExpense;
import com.talgat.simplepayment.tabs.TabReportIncome;
import com.talgat.simplepayment.tabs.ViewPagerAdapter;

import java.util.List;


public class ReportDetailActivity extends AppCompatActivity {

    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private String titles[];
    private int numberOfTabs = 2;
    private Fragment fragments[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initTab();
    }

    private void initTab() {
        final String tabExpTitle = getString(R.string.exp_text).toUpperCase();
        final String tabIncTitle = getString(R.string.inc_text).toUpperCase();
        titles = new String[] {tabExpTitle, tabIncTitle};
        fragments = new Fragment[] {new TabReportExpense(), new TabReportIncome()};

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), titles, numberOfTabs, fragments);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setupWithViewPager(pager);

        final TextView tab1 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab1.setTextColor(getResources().getColor(R.color.primary_text));
        tab1.setText(tabExpTitle);
        tabLayout.getTabAt(0).setCustomView(tab1);

        final TextView tab2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab2.setText(tabIncTitle);
        tabLayout.getTabAt(1).setCustomView(tab2);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (String.valueOf(tab.getText()).equals(tabExpTitle)) {
                    tab1.setTextColor(getResources().getColor(R.color.primary_text));
                    tab.setCustomView(tab1);
                } else {
                    tab2.setTextColor(getResources().getColor(R.color.primary_text));
                    tab.setCustomView(tab2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (String.valueOf(tab.getText()).equals(tabExpTitle)) {
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

    public static double getSumReportDetails(List<ReportDetail> reportDetails) {
        double sum = 0;
        for (ReportDetail reportDetail : reportDetails) {
            sum += reportDetail.getSum();
        }

        return sum;
    }
}
