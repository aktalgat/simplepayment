package com.talgat.simplepayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class PasswordActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        sharedPreferences = getSharedPreferences("MySettings", MODE_PRIVATE);
        boolean usePassword = sharedPreferences.getBoolean("use_password", false);

        if (!usePassword) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        editText = (EditText) findViewById(R.id.editText2);
    }

    public void enterClick(View view) {
        String enterPassword = String.valueOf(editText.getText());
        String savedPassword = sharedPreferences.getString("password", "");

        if (enterPassword.equals(savedPassword)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(getString(R.string.wrong_password));
        alertBuilder.setCancelable(true);
        alertBuilder.setPositiveButton(getString(R.string.ok_but_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        editText.requestFocus();
                    }
                });

        alertBuilder.create().show();
    }
}
