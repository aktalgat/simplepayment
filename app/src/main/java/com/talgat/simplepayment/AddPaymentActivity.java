package com.talgat.simplepayment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.talgat.simplepayment.database.Category;
import com.talgat.simplepayment.database.Payment;
import com.talgat.simplepayment.tabs.TabLog;
import com.talgat.simplepayment.tabs.TabReport;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddPaymentActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Spinner spinnerCat;
    private EditText editTextDate;
    private EditText editTextSum;
    private EditText editTextComm;
    private Payment payment;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        payment = (Payment) intent.getSerializableExtra("selectedPayment");
        selectedPosition = intent.getIntExtra("selectedPosition", -1);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Spinner spinnerCat = (Spinner) findViewById(R.id.spinnerCat);
                switch (checkedId) {
                    case R.id.radioExpense:
                        spinnerCat.setAdapter(MainActivity.getCategoryExpAdapter());
                        if (payment != null) {
                            spinnerCat.setSelection(getCategoryPos(
                                    MainActivity.getCategoryExpAdapter(),
                                    payment.getCategory()));
                        }
                        break;
                    case R.id.radioIncome:
                        spinnerCat.setAdapter(MainActivity.getCategoryIncAdapter());
                        if (payment != null) {
                            spinnerCat.setSelection(getCategoryPos(
                                    MainActivity.getCategoryIncAdapter(),
                                    payment.getCategory()));
                        }
                        break;
                }
            }
        });

        if (payment != null && payment.getType() == 1) {
            radioGroup.check(R.id.radioIncome);
        }

        spinnerCat = (Spinner) findViewById(R.id.spinnerCat);

        if (payment != null) {
            int spinPos;
            if (payment.getType() == 0) {
                spinnerCat.setAdapter(MainActivity.getCategoryExpAdapter());
                spinPos = getCategoryPos(MainActivity.getCategoryExpAdapter(), payment.getCategory());
            } else {
                spinnerCat.setAdapter(MainActivity.getCategoryIncAdapter());
                spinPos = getCategoryPos(MainActivity.getCategoryIncAdapter(), payment.getCategory());
            }
            spinnerCat.setSelection(spinPos);
        } else {
            spinnerCat.setAdapter(MainActivity.getCategoryExpAdapter());
        }

        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setInputType(InputType.TYPE_NULL);
        Calendar newCalendar = Calendar.getInstance();
        editTextDate.setText(MainActivity.simpleDateFormat.format(newCalendar.getTime()));
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = createDatePickerDialog(editTextDate);
                dialog.show();
            }
        });
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog dialog = createDatePickerDialog(editTextDate);
                    dialog.show();
                }
            }
        });

        editTextSum = (EditText) findViewById(R.id.editTextSum);
        editTextComm = (EditText) findViewById(R.id.editTextComment);
        editTextSum.requestFocus();

        if (payment != null) {
            editTextDate.setText(MainActivity.simpleDateFormat.format(new Date(payment.getPdate())));
            editTextSum.setText(String.valueOf(MainActivity.decimalFormat.format(payment.getSum())));
            editTextComm.setText(payment.getComment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_payment) {
            savePayment();
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePayment() {
        try {
            int type = 0;
            int id = radioGroup.getCheckedRadioButtonId();
            if (id != -1) {
                if (id == R.id.radioIncome) {
                    type = 1;
                }
            }
            Category category = (Category) spinnerCat.getSelectedItem();
            if (category.getId() == -1) {
                CategoryActivity.showAddCategoryDialog(this, type, spinnerCat, radioGroup);
                return;
            }

            Date pdate = MainActivity.simpleDateFormat.parse(String.valueOf(editTextDate.getText()));
            String comment = String.valueOf(editTextComm.getText());

            if (String.valueOf(editTextSum.getText()).length() == 0) {
                buildAlertDialog().show();
            } else {
                double sum = Double.parseDouble(String.valueOf(editTextSum.getText()));
                if (payment == null) {
                    Payment p = MainActivity.paymentDataSource.createPayment(type,
                            category.getId(), sum, comment, pdate.getTime());
                    //TabLog.getAdapter().insert(p, 0);
                    //TabLog.getAdapter().notifyDataSetChanged();
                    TabLog.getRecAdapter().addData(p, 0);
                } else {
                    MainActivity.paymentDataSource.updatePayment(payment.getId(),
                            type, category.getId(), sum, comment, pdate.getTime());

                    Payment p = TabLog.getRecAdapter().getItem(selectedPosition);//TabLog.getAdapter().getItem(selectedPosition);
                    p.setType(type);
                    p.setCategory(category.getId());
                    p.setSum(sum);
                    p.setComment(comment);
                    p.setPdate(pdate.getTime());
                    TabLog.getRecAdapter().notifyDataSetChanged();
                    //TabLog.getAdapter().notifyDataSetChanged();
                }

                if (TabReport.getInstance() != null) {
                    MainActivity.updatePayments();
                    TabReport.getAdapter().clear();
                    //noinspection ConstantConditions
                    TabReport.getAdapter().addAll(TabReport.getInstance().getReport());
                    //noinspection ConstantConditions
                    TabReport.getInstance().updateBalance();
                }

                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(i);
                finish();
            }
        } catch (ParseException e) {
            Log.e("", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private DatePickerDialog createDatePickerDialog(final EditText editText) {
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editText.setText(MainActivity.simpleDateFormat.format(newDate.getTime()));
            }
        };

        return new DatePickerDialog(this, datePickerListener,
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private AlertDialog buildAlertDialog() {
        AlertDialog.Builder builderDelete = new AlertDialog.Builder(this);
        builderDelete.setMessage(getString(R.string.summ_wrong));
        builderDelete.setCancelable(true);
        builderDelete.setPositiveButton(getString(R.string.ok_but_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        editTextSum.requestFocus();
                    }
                });

        return builderDelete.create();
    }

    public static int getCategoryPos(ArrayAdapter<Category> adapter, long catId) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getId() == catId) {
                return i;
            }
        }

        return 0;
    }
}
