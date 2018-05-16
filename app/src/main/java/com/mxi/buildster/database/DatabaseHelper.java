package com.mxi.buildster.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mxi.buildster.model.ProjectData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = "DatabaseHelper";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "/mnt/sdcard/buildsterone.db";

	// Tab	le Names
	private static final String TABLE_STATUS = "buildsterdata";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	// status one Table - column nmaes
	private static final String KEY_PROJECT_NAME = "project_name";
	private static final String KEY_PROJECT_ADDRESS = "project_address";
	private static final String KEY_PROJECT_MANAGER = "project_manager";
	private static final String KEY_COMPANY_NAME = "company_name";
	private static final String KEY_SELECTED_PATH = "image_path";
	private static final String KEY_SELECTED_PRO_PIC = "selectedImage";



	// status one Create Statements
	// Todo table create statement
	private static final String CREATE_TABLE_ONE = "CREATE TABLE "
			+ TABLE_STATUS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PROJECT_NAME + " TEXT,"+ KEY_PROJECT_ADDRESS + " TEXT,"+ KEY_PROJECT_MANAGER + " TEXT,"+ KEY_COMPANY_NAME + " TEXT,"+ KEY_SELECTED_PATH + " TEXT,"+ KEY_SELECTED_PRO_PIC + " BLOB NOT NULL," + KEY_CREATED_AT
			+ " DATETIME" + ")";



	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_ONE);


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATUS);



		// create new tables
		onCreate(db);
	}


	// ------------------------ "Status" table methods ----------------//

	/*
	 * Creating status table
	 */
	public long createTableOne(ProjectData points) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, points.getProject_name());
		values.put(KEY_PROJECT_ADDRESS, points.getProject_address());
		values.put(KEY_PROJECT_MANAGER, points.getProject_manager());
		values.put(KEY_COMPANY_NAME, points.getCompany_name());
		values.put(KEY_SELECTED_PATH, points.getImage_path());
		values.put(KEY_SELECTED_PRO_PIC, points.getSelectedImage());
		values.put(KEY_CREATED_AT, getDateTime());

		// insert row
		long tag_id = db.insert(TABLE_STATUS, null, values);

		return tag_id;
	}

	/**
	 * getting all status from table one
	 * */
	public List<ProjectData> getAllPointsFromTableOne() {

		List<ProjectData> pointsList = new ArrayList<ProjectData>();

		String selectQuery = "SELECT  * FROM " + TABLE_STATUS;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				ProjectData t = new ProjectData();
				t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				t.setProject_name(c.getString(c.getColumnIndex(KEY_PROJECT_NAME)));
				t.setProject_address(c.getString(c.getColumnIndex(KEY_PROJECT_ADDRESS)));
				t.setProject_manager(c.getString(c.getColumnIndex(KEY_PROJECT_MANAGER)));
				t.setCompany_name(c.getString(c.getColumnIndex(KEY_COMPANY_NAME)));
				t.setImage_path(c.getString(c.getColumnIndex(KEY_SELECTED_PATH)));
				t.setSelectedImage(c.getBlob(c.getColumnIndex(KEY_SELECTED_PRO_PIC)));

				// adding to point list
				pointsList.add(t);
			} while (c.moveToNext());
		}
		return pointsList;
	}

	/*
	 * Updating a status table one
	 */
	public int updatePointTable(ProjectData points) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PROJECT_NAME, points.getProject_name());
		values.put(KEY_PROJECT_ADDRESS, points.getProject_address());
		values.put(KEY_PROJECT_MANAGER, points.getProject_manager());
		values.put(KEY_COMPANY_NAME, points.getCompany_name());
		values.put(KEY_SELECTED_PATH, points.getImage_path());
		values.put(KEY_SELECTED_PRO_PIC, points.getSelectedImage());

		// updating row
		return db.update(TABLE_STATUS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(points.getId()) });
	}

	public void deleteStatus(long tado_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_STATUS, KEY_ID + " = ?",
				new String[] { String.valueOf(tado_id) });
	}


	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
}
