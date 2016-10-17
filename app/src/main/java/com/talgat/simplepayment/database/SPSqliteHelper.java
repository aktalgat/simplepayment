package com.talgat.simplepayment.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SPSqliteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_PAYMENTS = "payments";
	public static final String PAYMENTS_COLUMN_ID = "_id";
	public static final String PAYMENTS_COLUMN_TYPE = "type";
	public static final String PAYMENTS_COLUMN_CATEGORY = "category";
	public static final String PAYMENTS_COLUMN_SUM = "sum";
	public static final String PAYMENTS_COLUMN_COMMENT = "comment";
	public static final String PAYMENTS_COLUMN_PDATE = "pdate";
	
	public static final String TABLE_CATEGORIES = "categories";
	public static final String CATEGORIES_COLUMN_ID = "_id";
	public static final String CATEGORIES_COLUMN_TYPE = "type";
	public static final String CATEGORIES_COLUMN_NAME = "name";
	
	
	private static final String DATABASE_CREATE_1 = "create table " +
			TABLE_PAYMENTS + "(" +
			PAYMENTS_COLUMN_ID + " integer primary key autoincrement," +
			PAYMENTS_COLUMN_TYPE + " integer," +
			PAYMENTS_COLUMN_CATEGORY + " integer," +
			PAYMENTS_COLUMN_SUM + " real," +
			PAYMENTS_COLUMN_COMMENT + " text," +
			PAYMENTS_COLUMN_PDATE + " integer not null" +
			");";
	
	private static final String DATABASE_CREATE_2 = "create table " +
			TABLE_CATEGORIES + "(" +
			CATEGORIES_COLUMN_ID + " integer primary key autoincrement," +
			CATEGORIES_COLUMN_TYPE + " integer," +
			CATEGORIES_COLUMN_NAME + " text" + 
			");";
	
	private static final String DATABASE_NAME = "simplepayemt.db";
	private static final int DATABASE_VERSION = 3;
	
	public SPSqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		database.execSQL(DATABASE_CREATE_1);
		database.execSQL(DATABASE_CREATE_2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
	    onCreate(database);
	}

}
