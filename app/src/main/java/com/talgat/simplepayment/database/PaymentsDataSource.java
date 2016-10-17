package com.talgat.simplepayment.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.talgat.simplepayment.MainActivity;


public class PaymentsDataSource {

	private SQLiteDatabase database;
	private SPSqliteHelper dbHelper;
	private String[] allColumns = { 
			SPSqliteHelper.PAYMENTS_COLUMN_ID,
			SPSqliteHelper.PAYMENTS_COLUMN_TYPE,
			SPSqliteHelper.PAYMENTS_COLUMN_CATEGORY,
			SPSqliteHelper.PAYMENTS_COLUMN_SUM,
			SPSqliteHelper.PAYMENTS_COLUMN_COMMENT,
			SPSqliteHelper.PAYMENTS_COLUMN_PDATE};

	public PaymentsDataSource(Context context) {
	    dbHelper = new SPSqliteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Payment createPayment(int type, long category, double sum, String comment, long date) {
		ContentValues values = new ContentValues();
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_TYPE, type);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_CATEGORY, category);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_SUM, sum);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_COMMENT, comment);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_PDATE, date);
		long insertId = database.insert(SPSqliteHelper.TABLE_PAYMENTS, null, values);
		
		Cursor cursor = database.query(SPSqliteHelper.TABLE_PAYMENTS,
				allColumns, SPSqliteHelper.PAYMENTS_COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Payment newPayment = cursorToPayment(cursor);
		cursor.close();
		
		return newPayment;
	}

	public void updatePayment(long paymentId, int type, long category, double sum, String comment, long date) {
		ContentValues values = new ContentValues();
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_TYPE, type);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_CATEGORY, category);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_SUM, sum);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_COMMENT, comment);
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_PDATE, date);
		database.update(SPSqliteHelper.TABLE_PAYMENTS, values,
				SPSqliteHelper.PAYMENTS_COLUMN_ID + " = " + paymentId, null);
	}

	public void updatePaymentByCategory(Category category, int catType) {
		ContentValues values = new ContentValues();
		values.put(SPSqliteHelper.PAYMENTS_COLUMN_TYPE, catType);
		database.update(SPSqliteHelper.TABLE_PAYMENTS, values,
				SPSqliteHelper.PAYMENTS_COLUMN_CATEGORY + " = " + category.getId(), null);
	}
	
	public void deletePayment(Payment payment) {
		long id = payment.getId();
		database.delete(SPSqliteHelper.TABLE_PAYMENTS, SPSqliteHelper.PAYMENTS_COLUMN_ID
				+ " = " + id, null);
	}

	public void deletePaymentByCategory(Category category) {
		long id = category.getId();
		database.delete(SPSqliteHelper.TABLE_PAYMENTS, SPSqliteHelper.PAYMENTS_COLUMN_CATEGORY
				+ " = " + id, null);
	}

	public void deleteAllPayments() {
		database.delete(SPSqliteHelper.TABLE_PAYMENTS, null, null);
	}

	public List<Payment> getAllPayments() {
		List<Payment> payments = new ArrayList<>();

		Cursor cursor = database.query(SPSqliteHelper.TABLE_PAYMENTS,
				allColumns, null, null, null, null, SPSqliteHelper.PAYMENTS_COLUMN_PDATE + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Payment payment = cursorToPayment(cursor);
			payments.add(payment);
			cursor.moveToNext();
		}
		
		cursor.close();
		return payments;
	}

	private Payment cursorToPayment(Cursor cursor) {
		
		Payment payment = new Payment();
		payment.setId(cursor.getLong(0));
		payment.setType(cursor.getInt(1));
		payment.setCategory(cursor.getInt(2));
		payment.setSum(cursor.getDouble(3));
		payment.setComment(cursor.getString(4));
		payment.setPdate(cursor.getLong(5));
		
	    return payment;
	}

	public List<ReportDetail> getReportDetails(Date from, Date to, int type) {
		List<ReportDetail> reportDetails = new ArrayList<>();

        double allSum = 0;

        Cursor cursor = database.query(SPSqliteHelper.TABLE_PAYMENTS,
                new String[] {"sum(sum)"},
                SPSqliteHelper.PAYMENTS_COLUMN_TYPE + " = " + type + " and " +
                    SPSqliteHelper.PAYMENTS_COLUMN_PDATE + " >= " + from.getTime() + " and " +
                    SPSqliteHelper.PAYMENTS_COLUMN_PDATE + " <= " + to.getTime(),
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            allSum = cursor.getDouble(0);
            cursor.moveToNext();
        }
        cursor.close();

        cursor = database.query(SPSqliteHelper.TABLE_PAYMENTS,
                new String[] {SPSqliteHelper.PAYMENTS_COLUMN_CATEGORY, "sum(sum)"},
                SPSqliteHelper.PAYMENTS_COLUMN_TYPE + " = " + type + " and " +
                    SPSqliteHelper.PAYMENTS_COLUMN_PDATE + " >= " + from.getTime() + " and " +
                    SPSqliteHelper.PAYMENTS_COLUMN_PDATE + " <= " + to.getTime(),
                null,
                SPSqliteHelper.PAYMENTS_COLUMN_CATEGORY,
                null, SPSqliteHelper.PAYMENTS_COLUMN_SUM + " desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ReportDetail reportDetail = new ReportDetail();
            reportDetail.setCategoryName(MainActivity.categoryMap.get(cursor.getLong(0)));
            double sum = cursor.getDouble(1);
            reportDetail.setSum(sum);
            reportDetail.setPercent(sum * 100 / allSum);

            reportDetails.add(reportDetail);
            cursor.moveToNext();
        }
        cursor.close();

		return reportDetails;
	}
}
