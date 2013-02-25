package com.fivecollege.eventup.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fivecollege.eventup.Category;
import com.fivecollege.eventup.College;
import com.fivecollege.eventup.Event;

public class EventDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;

	public static final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm");

	public static String LECTURE = "Lectures";
	public static String PERFORMANCE = "Performance";

	public static String MOUNT_HOLYOKE_FULL = "Mount Holyoke College";
	public static String MOUNT_HOLYOKE_SHORT = "MHC";

	public static String SMITH_FULL = "Smith College";
	public static String SMITH_SHORT = "SC";

	public static String HAMPSHIRE_FULL = "Hampshire College";
	public static String HAMPSHIRE_SHORT = "HC";

	public static String AMHERST_FULL = "Amherst College";
	public static String AMHERST_SHORT = "AC";

	public static String UMASS_FULL = "Umass";
	public static String UMASS_SHORT = "UM";

	public EventDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Event createEvent(String subject, GregorianCalendar startDate,
			GregorianCalendar startTime, GregorianCalendar endDate,
			GregorianCalendar endTime, College college, String location,
			String description, Category category) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.EVENT_SUBJECT, subject);

		values.put(MySQLiteHelper.EVENT_STARTDATE,
				dateFormat.format(startDate.getTime()));
		values.put(MySQLiteHelper.EVENT_ENDDATE,
				dateFormat.format(endDate.getTime()));

		values.put(MySQLiteHelper.EVENT_STARTTIME,
				timeFormat.format(startTime.getTime()));
		values.put(MySQLiteHelper.EVENT_ENDTIME,
				timeFormat.format(endTime.getTime()));
		values.put(MySQLiteHelper.EVENT_COLLEGE_ID, college.getId());
		values.put(MySQLiteHelper.EVENT_LOCATION, location);
		values.put(MySQLiteHelper.EVENT_DESCRIPTION, description);
		if (category != null) {
			values.put(MySQLiteHelper.EVENT_CATEGORY_ID, category.getId());
		}
		long insertId = database.insert(MySQLiteHelper.TABLE_EVENTS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
				MySQLiteHelper.EVENTS_ALL_COLUMNS,
				MySQLiteHelper.EVENT_COLUMN_ID + " = " + insertId, null, null,
				null, null);
		cursor.moveToFirst();
		Event newEvent = cursorToEvent(cursor);
		cursor.close();
		return newEvent;
	}

	public College createCollege(String name, String shortName) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLLEGE_NAME, name);
		values.put(MySQLiteHelper.COLLEGE_ABBREVIATION, shortName);
		long insertId = database.insert(MySQLiteHelper.TABLE_COLLEGES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_COLLEGES,
				MySQLiteHelper.COLLEGE_ALL_COLUMNS,
				MySQLiteHelper.COLLEGE_COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		College newCollege = cursorToCollege(cursor);
		cursor.close();
		return newCollege;
	}

	public Category getCategoryByFullName(String name) {
		categories = getAllCategories();
		for (Category c : categories) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return createCategory(name, " ");
	}

	public College getCollegeByShortName(String shortname) {
		colleges = getAllColleges();
		for (College c : colleges) {
			if (c.getShortName().equals(shortname)) {
				return c;
			}
		}
		return null;
	}

	public Category createCategory(String name, String shortName) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.CATEGORY_NAME, name);
		values.put(MySQLiteHelper.CATEGORY_ABBREVIATION, shortName);
		long insertId = database.insert(MySQLiteHelper.TABLE_CATEGORY, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CATEGORY,
				MySQLiteHelper.CATEGORY_ALL_COLUMNS,
				MySQLiteHelper.CATEGORY_COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Category newCategory = cursorToCategory(cursor);
		cursor.close();
		updateCategories();
		return newCategory;
	}

	public void deleteAllEvent() {
		database.delete(MySQLiteHelper.TABLE_EVENTS, null, null);
	}

	private College cursorToCollege(Cursor cursor) {
		College college = new College();
		college.setId(cursor.getLong(0));
		college.setName(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.COLLEGE_NAME)));
		college.setShortName(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.COLLEGE_ABBREVIATION)));
		return college;
	}

	private Category cursorToCategory(Cursor cursor) {
		Category category = new Category();
		category.setId(cursor.getLong(0));
		category.setName(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.COLLEGE_NAME)));
		category.setShortName(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.COLLEGE_ABBREVIATION)));
		return category;
	}

	private Event cursorToEvent(Cursor cursor) {
		Event event = new Event();
		event.setId(cursor.getLong(0));
		event.setSubject(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.EVENT_SUBJECT)));
		College college = getCollege(cursor.getLong(cursor
				.getColumnIndex(MySQLiteHelper.EVENT_COLLEGE_ID)));
		if (college != null) {
			event.setCollege(college);
		}
		try {

			GregorianCalendar startDate = new GregorianCalendar(Event.EST);
			startDate.setTime(dateFormat.parse(cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_STARTDATE))));
			event.setStartDate(startDate);

			GregorianCalendar startTime = new GregorianCalendar(Event.EST);
			startTime.setTime(timeFormat.parse(cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_STARTTIME))));
			event.setStartTime(startTime);

			GregorianCalendar endDate = new GregorianCalendar(Event.EST);
			endDate.setTime(dateFormat.parse(cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_ENDDATE))));
			event.setEndDate(endDate);

			GregorianCalendar endTime = new GregorianCalendar(Event.EST);
			endTime.setTime(timeFormat.parse(cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_ENDTIME))));
			event.setEndTime(endTime);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		event.setDescription(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.EVENT_DESCRIPTION)));

		event.setLocation(cursor.getString(cursor
				.getColumnIndex(MySQLiteHelper.EVENT_LOCATION)));
		// null maybe problem
		Category category = getCategory(cursor.getLong(cursor
				.getColumnIndex(MySQLiteHelper.EVENT_CATEGORY_ID)));
		if (category != null) {
			event.setCategory(category);
		}
		return event;
	}

	public void deleteCollege(College college) {
		long id = college.getId();
		System.out.println("College deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_COLLEGES,
				MySQLiteHelper.COLLEGE_COLUMN_ID + " = " + id, null);
	}

	List<College> colleges = null;

	public College getCollege(long collegeId) {
		List<College> colleges = getAllColleges();
		for (College coll : colleges) {
			if (coll.getId() == collegeId) {
				return coll;
			}
		}
		return null;
	}

	public List<College> getAllColleges() {

		if (colleges != null) {
			return colleges;
		} else {
			colleges = new ArrayList<College>();
			Cursor cursor = database.query(MySQLiteHelper.TABLE_COLLEGES,
					MySQLiteHelper.COLLEGE_ALL_COLUMNS, null, null, null, null,
					null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				College college = cursorToCollege(cursor);
				colleges.add(college);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return colleges;
		}
	}

	List<Category> categories = null;

	public Category getCategory(long categoryId) {
		List<Category> categorys = getAllCategories();
		for (Category coll : categorys) {
			if (coll.getId() == categoryId) {
				return coll;
			}
		}
		return null;
	}

	public List<Category> getAllCategories() {

		if (categories != null) {
			return categories;
		} else {
			updateCategories();
			return categories;
		}
	}

	private void updateCategories() {
		categories = new ArrayList<Category>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CATEGORY,
				MySQLiteHelper.CATEGORY_ALL_COLUMNS, null, null, null, null,
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
	}

	//
	// public List<Event> get() {
	//
	//
	// }

	public List<Event> getTodayEvents() {

		List<Event> events = new ArrayList<Event>();
		Cursor cursor = database
				.query(MySQLiteHelper.TABLE_EVENTS,
						MySQLiteHelper.EVENTS_ALL_COLUMNS, null, null, null,
						null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = cursorToEvent(cursor);
			events.add(event);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return events;

	}

	public class ItemWithCounts<E> {

		int count = 0;
		E item;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public E getItem() {
			return item;
		}

		public void setItem(E item) {
			this.item = item;
		}

		public String toString() {

			if (item instanceof GregorianCalendar) {
				return dateFormat.format(((Calendar) item).getTime()) + " ("
						+ count + ") ";
			}

			return item.toString() + " (" + count + ") ";

		}
	}

	public College getCollegeFromId(int id) {
		for (College c : colleges) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	public List<College> getColleges(String date) {

		Log.d("here", "here");
		System.out.println("here");

		List<College> collegesToReturn = new ArrayList<College>();

		String query = "select distinct " + MySQLiteHelper.EVENT_COLLEGE_ID
				+ " from " + MySQLiteHelper.TABLE_EVENTS + " where "
				+ MySQLiteHelper.EVENT_STARTDATE + " = \"" + date + "\"";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);

		colleges = getAllColleges();

		int i = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			i++;
			Log.d("i", i + "");
			int collegeId = cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_COLLEGE_ID));
			College c = getCollegeFromId(collegeId);
			if (c != null) {
				collegesToReturn.add(c);
			}
			cursor.moveToNext();
		}
		return collegesToReturn;

	}

	public List<College> getColleges(int categoryId) {

		Log.d("here", "here");
		System.out.println("here");

		List<College> collegesToReturn = new ArrayList<College>();

		String query = "select distinct " + MySQLiteHelper.EVENT_COLLEGE_ID
				+ " from " + MySQLiteHelper.TABLE_EVENTS + " where "
				+ MySQLiteHelper.EVENT_CATEGORY_ID + " = \"" + categoryId
				+ "\"";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);

		colleges = getAllColleges();

		int i = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			i++;
			Log.d("i", i + "");
			int collegeId = cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_COLLEGE_ID));
			College c = getCollegeFromId(collegeId);
			if (c != null) {
				collegesToReturn.add(c);
			}
			cursor.moveToNext();
		}
		return collegesToReturn;

	}

	public List<Category> getCategories(String date) {

		Log.d("here", "here");
		System.out.println("here");

		List<Category> collegesToReturn = new ArrayList<Category>();

		String query = "select distinct " + MySQLiteHelper.EVENT_CATEGORY_ID
				+ " from " + MySQLiteHelper.TABLE_EVENTS + " where "
				+ MySQLiteHelper.EVENT_STARTDATE + " = \"" + date + "\"";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);

		int i = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			i++;
			Log.d("i", i + "");
			int categoryId = cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_CATEGORY_ID));
			Category c = getCategory((long) categoryId);
			if (c != null) {
				collegesToReturn.add(c);
			}
			cursor.moveToNext();
		}
		return collegesToReturn;

	}

	public List<GregorianCalendar> getDates(int collegeId) {
		Log.d("here", "here");
		System.out.println("here");

		List<GregorianCalendar> datesToReturn = new ArrayList<GregorianCalendar>();

		String query = "select distinct " + MySQLiteHelper.EVENT_STARTDATE
				+ " from " + MySQLiteHelper.TABLE_EVENTS + " where "
				+ MySQLiteHelper.EVENT_COLLEGE_ID + " = \"" + collegeId + "\"";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			String dateString = cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_STARTDATE));
			GregorianCalendar startDate = new GregorianCalendar(Event.EST);
			try {
				startDate.setTime(dateFormat.parse(dateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			datesToReturn.add(startDate);

			cursor.moveToNext();
		}
		Collections.sort(datesToReturn);
		return datesToReturn;

	}

	public List<GregorianCalendar> getDatesByCategory(int categoryId) {
		Log.d("here", "here");
		System.out.println("here");

		List<GregorianCalendar> datesToReturn = new ArrayList<GregorianCalendar>();

		String query = "select distinct " + MySQLiteHelper.EVENT_STARTDATE
				+ " from " + MySQLiteHelper.TABLE_EVENTS + " where "
				+ MySQLiteHelper.EVENT_CATEGORY_ID + " = \"" + categoryId
				+ "\"";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			String dateString = cursor.getString(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_STARTDATE));
			GregorianCalendar startDate = new GregorianCalendar(Event.EST);
			try {
				startDate.setTime(dateFormat.parse(dateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			datesToReturn.add(startDate);

			cursor.moveToNext();
		}
		Collections.sort(datesToReturn);
		return datesToReturn;

	}

	public List<Category> getCategories(int collegeId) {
		Log.d("here", "here");
		System.out.println("here");

		List<Category> categoriesToReturn = new ArrayList<Category>();

		String query = "select distinct " + MySQLiteHelper.EVENT_CATEGORY_ID
				+ " from " + MySQLiteHelper.TABLE_EVENTS + " where "
				+ MySQLiteHelper.EVENT_COLLEGE_ID + " = \"" + collegeId + "\"";
		Log.d("query", query);
		Cursor cursor = database.rawQuery(query, null);

		int i = 0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			i++;
			Log.d("i", i + "");
			int categoryId = cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_CATEGORY_ID));
			Category c = getCategory((long) categoryId);
			if (c != null) {
				categoriesToReturn.add(c);
			}
			cursor.moveToNext();
		}
		return categoriesToReturn;

	}

	public List<Event> getEvent(College college, String date) {

		Log.d("here", "getEvent");

		List<Event> eventsToReturn = new ArrayList<Event>();

		String selection = MySQLiteHelper.EVENT_STARTDATE + "=?  AND "
				+ MySQLiteHelper.EVENT_COLLEGE_ID + "=?";
		String[] selectionArgs = { date, college.getId() + "" };
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
				MySQLiteHelper.EVENTS_ALL_COLUMNS, selection, selectionArgs,
				null, null, null);

		List<College> collegesToReturn = new ArrayList<College>();

		String query = "select distinct " + MySQLiteHelper.EVENT_COLLEGE_ID
				+ " from " + MySQLiteHelper.TABLE_EVENTS + " where "
				+ MySQLiteHelper.EVENT_STARTDATE + " = \"" + date + "\"";
		Log.d("query", query);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = cursorToEvent(cursor);
			eventsToReturn.add(event);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return eventsToReturn;

		// return collegesToReturn;

	}

	public List<Event> getEvent(Category category, String date) {

		Log.d("here", "getEvent");

		List<Event> eventsToReturn = new ArrayList<Event>();

		String selection = MySQLiteHelper.EVENT_STARTDATE + "=?  AND "
				+ MySQLiteHelper.EVENT_CATEGORY_ID + "=?";
		String[] selectionArgs = { date, category.getId() + "" };
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
				MySQLiteHelper.EVENTS_ALL_COLUMNS, selection, selectionArgs,
				null, null, null);

		//
		// String query = "select distinct " + MySQLiteHelper.EVENT_CATEGORY_ID
		// + " from " + MySQLiteHelper.TABLE_EVENTS + " where "
		// + MySQLiteHelper.EVENT_STARTDATE + " = \"" + date + "\"";
		// Log.d("query", query);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = cursorToEvent(cursor);
			eventsToReturn.add(event);
			cursor.moveToNext();
		}
		cursor.close();
		return eventsToReturn;

	}

	public List<Event> getEvent(Category category, int collegeId) {

		Log.d("here", "getEvent");

		List<Event> eventsToReturn = new ArrayList<Event>();

		String selection = MySQLiteHelper.EVENT_COLLEGE_ID + "=?  AND "
				+ MySQLiteHelper.EVENT_CATEGORY_ID + "=?";
		String[] selectionArgs = { collegeId + "", category.getId() + "" };
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
				MySQLiteHelper.EVENTS_ALL_COLUMNS, selection, selectionArgs,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = cursorToEvent(cursor);
			eventsToReturn.add(event);
			cursor.moveToNext();
		}
		cursor.close();
		return eventsToReturn;

	}

	public List<ItemWithCounts<GregorianCalendar>> getAvailableDates() {

		List<ItemWithCounts<GregorianCalendar>> availableDates = new ArrayList<ItemWithCounts<GregorianCalendar>>();
		// Cursor android.database.sqlite.SQLiteDatabase.query(String table,
		// String[] columns, String selection, String[] selectionArgs, String
		// groupBy, String having, String orderBy)

		String query = "SELECT COUNT(*) AS COUNT,"
				+ MySQLiteHelper.EVENT_STARTDATE + " FROM "
				+ MySQLiteHelper.TABLE_EVENTS + " GROUP BY "
				+ MySQLiteHelper.EVENT_STARTDATE;
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ItemWithCounts<GregorianCalendar> pair = new ItemWithCounts<GregorianCalendar>();

			GregorianCalendar startDate = new GregorianCalendar(Event.EST);
			try {
				startDate.setTime(dateFormat.parse(cursor.getString(cursor
						.getColumnIndex(MySQLiteHelper.EVENT_STARTDATE))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pair.setItem(startDate);

			int count = cursor.getInt(cursor.getColumnIndex("COUNT"));
			pair.setCount(count);
			availableDates.add(pair);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return availableDates;

	}

	public List<ItemWithCounts<College>> getAvailableCollege() {

		List<ItemWithCounts<College>> availableCollege = new ArrayList<ItemWithCounts<College>>();
		// Cursor android.database.sqlite.SQLiteDatabase.query(String table,
		// String[] columns, String selection, String[] selectionArgs, String
		// groupBy, String having, String orderBy)

		String query = "SELECT COUNT(*) AS COUNT,"
				+ MySQLiteHelper.EVENT_COLLEGE_ID + " FROM "
				+ MySQLiteHelper.TABLE_EVENTS + " GROUP BY "
				+ MySQLiteHelper.EVENT_COLLEGE_ID;
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ItemWithCounts<College> pair = new ItemWithCounts<College>();

			int collegeId = cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_COLLEGE_ID));
			College college = getCollege(collegeId);
			pair.setItem(college);

			int count = cursor.getInt(cursor.getColumnIndex("COUNT"));
			pair.setCount(count);
			availableCollege.add(pair);
			cursor.moveToNext();
		}

		// Make sure to close the cursor
		cursor.close();
		return availableCollege;

	}

	public List<ItemWithCounts<Category>> getAvailableCategory() {

		List<ItemWithCounts<Category>> availableCategories = new ArrayList<ItemWithCounts<Category>>();

		String query = "SELECT COUNT(*) AS COUNT,"
				+ MySQLiteHelper.EVENT_CATEGORY_ID + " FROM "
				+ MySQLiteHelper.TABLE_EVENTS + " GROUP BY "
				+ MySQLiteHelper.EVENT_CATEGORY_ID;
		Cursor cursor = database.rawQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ItemWithCounts<Category> pair = new ItemWithCounts<Category>();

			int categoryId = cursor.getInt(cursor
					.getColumnIndex(MySQLiteHelper.EVENT_CATEGORY_ID));
			Category cat = getCategory(categoryId);
			pair.setItem(cat);
			int count = cursor.getInt(cursor.getColumnIndex("COUNT"));
			pair.setCount(count);
			availableCategories.add(pair);
			cursor.moveToNext();
		}

		cursor.close();
		return availableCategories;

	}
}
