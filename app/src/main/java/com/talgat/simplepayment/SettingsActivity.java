package com.talgat.simplepayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingsActivity extends AppCompatActivity {

    private ListView listView;
    private ListView listView2;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);

        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> datum = new HashMap<>();
        datum.put("First", getString(R.string.category));
        datum.put("Second", getString(R.string.category_desc));
        data.add(datum);

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.settings_list_item, new String[] {"First", "Second" },
                new int[] {android.R.id.text1, android.R.id.text2 });

        listView = (ListView) findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    Intent intent = new Intent(SettingsActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
            }
        });

        listView.setAdapter(adapter);


        List<Map<String, String>> data2 = new ArrayList<>();
        Map<String, String> datum1 = new HashMap<>();
        datum1.put("First", getString(R.string.password));
        datum1.put("Second", getString(R.string.password_desc));
        data2.add(datum1);

        SimpleAdapter adapter2 = new SimpleAdapter(this, data2, R.layout.settings_list_item, new String[] {"First", "Second" },
                new int[] {android.R.id.text1, android.R.id.text2 });

        listView2 = (ListView) findViewById(R.id.list2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    boolean usePassword = sharedPreferences.getBoolean("use_password", false);
                    if (usePassword) {
                        showPasswordDialog();
                    }
                }
            }
        });

        listView2.setAdapter(adapter2);

        boolean usePassword = sharedPreferences.getBoolean("use_password", false);

        CheckBox checkBox = (CheckBox) findViewById(R.id.usePassword);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("use_password", isChecked);
                editor.commit();
            }
        });
        checkBox.setChecked(usePassword);
    }

    private void showPasswordDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(
                R.layout.password_dialog, null);

        final TextView textView = (TextView) view.findViewById(R.id.editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.set_password));
        builder.setPositiveButton(getString(R.string.set_but),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = String.valueOf(textView.getText());
                        if (password.length() != 0) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("password", password);
                            editor.commit();
                        }
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
    }
}
