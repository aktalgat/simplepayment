package com.talgat.simplepayment.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.talgat.simplepayment.MainActivity;

public class CategoryDataSource {

	
	private SQLiteDatabase database;
	private SPSqliteHelper dbHelper;
	private String[] allColumns = { 
			SPSqliteHelper.CATEGORIES_COLUMN_ID,
			SPSqliteHelper.CATEGORIES_COLUMN_TYPE,
			SPSqliteHelper.CATEGORIES_COLUMN_NAME};

	public CategoryDataSource(Context context) {
	    dbHelper = new SPSqliteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Category createCategory(int type, String name) {
		ContentValues values = new ContentValues();
		values.put(SPSqliteHelper.CATEGORIES_COLUMN_TYPE, type);
		values.put(SPSqliteHelper.CATEGORIES_COLUMN_NAME, name);
		long insertId = database.insert(SPSqliteHelper.TABLE_CATEGORIES, null, values);
		
		Cursor cursor = database.query(SPSqliteHelper.TABLE_CATEGORIES,
				allColumns, SPSqliteHelper.CATEGORIES_COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Category newCategory = cursorToCategory(cursor);
		cursor.close();
		
		return newCategory;
	}
	
	public void deleteCategory(Category category) {
		MainActivity.paymentDataSource.deletePaymentByCategory(category);
		long id = category.getId();
		database.delete(SPSqliteHelper.TABLE_CATEGORIES, SPSqliteHelper.CATEGORIES_COLUMN_ID
				+ " = " + id, null);
	}

	public void deleteAllCategories() {
		MainActivity.paymentDataSource.deleteAllPayments();
		database.delete(SPSqliteHelper.TABLE_CATEGORIES, null, null);
	}

	public void updateCategory(Category category, long catId, int catType, String catName) {
        MainActivity.paymentDataSource.updatePaymentByCategory(category, catType);

		ContentValues values = new ContentValues();
		values.put(SPSqliteHelper.CATEGORIES_COLUMN_TYPE, catType);
		values.put(SPSqliteHelper.CATEGORIES_COLUMN_NAME, catName);
		database.update(SPSqliteHelper.TABLE_CATEGORIES, values,
				SPSqliteHelper.CATEGORIES_COLUMN_ID + " = " + catId, null);
	}

	public List<Category> getExpCategories() {
		List<Category> categories = new ArrayList<>();

		Cursor cursor = database.query(SPSqliteHelper.TABLE_CATEGORIES,
				allColumns, SPSqliteHelper.CATEGORIES_COLUMN_TYPE + " = 0", null, null, null,
				SPSqliteHelper.CATEGORIES_COLUMN_NAME);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		
		cursor.close();
		return categories;
	}
	
	public List<Category> getIncCategories() {
		List<Category> categories = new ArrayList<>();

		Cursor cursor = database.query(SPSqliteHelper.TABLE_CATEGORIES,
				allColumns, SPSqliteHelper.CATEGORIES_COLUMN_TYPE + " = 1", null, null, null,
				SPSqliteHelper.CATEGORIES_COLUMN_NAME);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		
		cursor.close();
		return categories;
	}
	
	public Map<Long, String> getCategoryMap() {
		
		Map<Long, String> map = new TreeMap<>();
		
		Cursor cursor = database.query(SPSqliteHelper.TABLE_CATEGORIES,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			map.put(category.getId(), category.getName());
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return map;
	}
	
	private Category cursorToCategory(Cursor cursor) {
		
		Category category = new Category();
		category.setId(cursor.getLong(0));
		category.setType(cursor.getInt(1));
		category.setName(cursor.getString(2));
		
	    return category;
	}
}
