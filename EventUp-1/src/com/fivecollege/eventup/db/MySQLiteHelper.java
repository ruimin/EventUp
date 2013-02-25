package com.fivecollege.eventup.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	

	private static final String DATABASE_NAME = "eventup.db";
	private static final int DATABASE_VERSION = 11;

	public static final String TABLE_COLLEGES = "COLLEGES";
	public static final String COLLEGE_COLUMN_ID = "college_id";
	public static final String COLLEGE_NAME = "name";
	public static final String COLLEGE_ABBREVIATION = "short_name";
	public static final String[] COLLEGE_ALL_COLUMNS = { COLLEGE_COLUMN_ID,
			COLLEGE_NAME, COLLEGE_ABBREVIATION };

	private static final String DB_CREATE_COLLEGE_TABLE = "CREATE TABLE "
			+ TABLE_COLLEGES + "(" + COLLEGE_COLUMN_ID
			+ " integer primary key autoincrement, " + COLLEGE_NAME
			+ " text not null, " + COLLEGE_ABBREVIATION + " text not null"
			+ ");";

	public static final String TABLE_CATEGORY = "CATEGORIES";
	public static final String CATEGORY_COLUMN_ID = "category_id";
	public static final String CATEGORY_NAME = "name";
	public static final String CATEGORY_ABBREVIATION = "short_name";
	public static final String[] CATEGORY_ALL_COLUMNS = { CATEGORY_COLUMN_ID,
			CATEGORY_NAME, CATEGORY_ABBREVIATION };

	private static final String DB_CREATE_CATEGORY_TABLE = "CREATE TABLE "
			+ TABLE_CATEGORY + "(" + CATEGORY_COLUMN_ID
			+ " integer primary key autoincrement, " + CATEGORY_NAME
			+ " text not null, " + CATEGORY_ABBREVIATION + " text not null"
			+ ");";

	private static final String INSERT_LECTURE = "insert into "
			+ TABLE_CATEGORY + "(" + CATEGORY_NAME + ","
			+ CATEGORY_ABBREVIATION + ") values('" + EventDataSource.LECTURE
			+ "','" + EventDataSource.LECTURE + "');";

	private static final String INSERT_PERFORMANCE = "insert into "
			+ TABLE_CATEGORY + "(" + CATEGORY_NAME + ","
			+ CATEGORY_ABBREVIATION + ") values('"
			+ EventDataSource.PERFORMANCE + "','" + EventDataSource.PERFORMANCE
			+ "');";

	private static final String INSERT_MHC = "insert into " + TABLE_COLLEGES
			+ "(" + COLLEGE_NAME + "," + COLLEGE_ABBREVIATION + ") values('"
			+ EventDataSource.MOUNT_HOLYOKE_FULL + "','"
			+ EventDataSource.MOUNT_HOLYOKE_SHORT + "');";

	private static final String INSERT_SC = "insert into " + TABLE_COLLEGES
			+ "(" + COLLEGE_NAME + "," + COLLEGE_ABBREVIATION + ") values('"
			+ EventDataSource.SMITH_FULL + "','" + EventDataSource.SMITH_SHORT
			+ "');";

	private static final String INSERT_HC = "insert into " + TABLE_COLLEGES
			+ "(" + COLLEGE_NAME + "," + COLLEGE_ABBREVIATION + ") values('"
			+ EventDataSource.HAMPSHIRE_FULL + "','"
			+ EventDataSource.HAMPSHIRE_SHORT + "');";

	private static final String INSERT_AC = "insert into " + TABLE_COLLEGES
			+ "(" + COLLEGE_NAME + "," + COLLEGE_ABBREVIATION + ") values('"
			+ EventDataSource.AMHERST_FULL + "','"
			+ EventDataSource.AMHERST_SHORT + "');";

	private static final String INSERT_UM = "insert into " + TABLE_COLLEGES
			+ "(" + COLLEGE_NAME + "," + COLLEGE_ABBREVIATION + ") values('"
			+ EventDataSource.UMASS_FULL + "','" + EventDataSource.UMASS_SHORT
			+ "');";

	public static final String TABLE_EVENTS = "EVENTS";
	public static final String EVENT_COLUMN_ID = "event_id";
	public static final String EVENT_SUBJECT = "subject";
	public static final String EVENT_STARTDATE = "start_date";
	public static final String EVENT_STARTTIME = "start_time";
	public static final String EVENT_ENDDATE = "end_date";
	public static final String EVENT_ENDTIME = "end_time";
	public static final String EVENT_COLLEGE_ID = "college_id";
	public static final String EVENT_CATEGORY_ID = "category_id";

	public static final String EVENT_LOCATION = "location";
	public static final String EVENT_DESCRIPTION = "description";

	public static final String[] EVENTS_ALL_COLUMNS = { EVENT_COLUMN_ID,
			EVENT_SUBJECT, EVENT_STARTDATE, EVENT_STARTTIME, EVENT_ENDDATE,EVENT_ENDTIME, EVENT_COLLEGE_ID,
			EVENT_LOCATION, EVENT_DESCRIPTION, EVENT_CATEGORY_ID };

	private static final String DB_CREATE_EVENT_TABLE = "CREATE TABLE "
			+ TABLE_EVENTS + "(" + EVENT_COLUMN_ID
			+ " integer primary key autoincrement, " + EVENT_SUBJECT
			+ " text not null, " + EVENT_STARTDATE + " text not null, "
			+ EVENT_STARTTIME + " text not null, " + EVENT_ENDDATE + " text, "
			+ EVENT_ENDTIME + " text, " + EVENT_LOCATION + " text, "
			+ EVENT_DESCRIPTION + " text, " + EVENT_COLLEGE_ID + " integer,"
			+ EVENT_CATEGORY_ID + " integer," + "FOREIGN KEY("
			+ EVENT_COLLEGE_ID + ")REFERENCES " + TABLE_COLLEGES + "("
			+ COLLEGE_COLUMN_ID + ")" + ", FOREIGN KEY(" + EVENT_CATEGORY_ID
			+ ")REFERENCES " + TABLE_CATEGORY + "(" + CATEGORY_COLUMN_ID + ")"
			+ ");";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// colleges
		database.execSQL(DB_CREATE_COLLEGE_TABLE);
		database.execSQL(INSERT_MHC);
		database.execSQL(INSERT_SC);
		database.execSQL(INSERT_HC);
		database.execSQL(INSERT_AC);
		database.execSQL(INSERT_UM);
		database.execSQL(DB_CREATE_CATEGORY_TABLE);
		database.execSQL(INSERT_LECTURE);
		database.execSQL(INSERT_PERFORMANCE);
		database.execSQL(DB_CREATE_EVENT_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLEGES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);

		onCreate(db);
	}

}