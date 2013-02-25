package com.fivecollege.eventup;

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.fivecollege.eventup.db.EventDataSource;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Event implements Parcelable {
	public static final TimeZone EST = TimeZone.getTimeZone("America/New_York");
	private long id;
	private String subject = "";
	private GregorianCalendar startTime = new GregorianCalendar(Event.EST);
	private GregorianCalendar endTime = new GregorianCalendar(Event.EST);
	private GregorianCalendar startDate = new GregorianCalendar(Event.EST);
	private GregorianCalendar endDate = new GregorianCalendar(Event.EST);

	public GregorianCalendar getStartDate() {
		return startDate;
	}

	public void setStartDate(GregorianCalendar startDate) {
		this.startDate = startDate;
	}

	public GregorianCalendar getEndDate() {
		return endDate;
	}

	public void setEndDate(GregorianCalendar endDate) {
		this.endDate = endDate;
	}

	private College college;
	private String location = "";
	private String description = "";
	private Category category;

	public Event() {

	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
		public Event createFromParcel(Parcel in) {
			return new Event(in);
		}

		public Event[] newArray(int size) {
			return new Event[size];
		}
	};

	public Event(Parcel in) {
		id = in.readLong();
		subject = in.readString();
		try {
			String s = in.readString();
			startDate.setTime(EventDataSource.dateFormat.parse(s));
			Log.i("startDate", startDate.getTime().toString());
			s = in.readString();
			startTime.setTime(EventDataSource.timeFormat.parse(s));
			Log.i("startTime", startTime.getTime().toString());
			s = in.readString();
			endDate.setTime(EventDataSource.dateFormat.parse(s));
			Log.i("endDate", endDate.getTime().toString());
			s = in.readString();
			endTime.setTime(EventDataSource.timeFormat.parse(s));
			Log.i("endTime", endTime.getTime().toString());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		college = in.readParcelable(College.class.getClassLoader());
		location = in.readString();
		Log.i("location", location + " : ");

		description = in.readString();
		category = in.readParcelable(Category.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(subject);

		dest.writeString(EventDataSource.dateFormat.format(startDate.getTime()));
		dest.writeString(EventDataSource.timeFormat.format(startTime.getTime()));
		dest.writeString(EventDataSource.dateFormat.format(endDate.getTime()));
		dest.writeString(EventDataSource.timeFormat.format(endTime.getTime()));

		dest.writeParcelable(college, 0);
		Log.d("write location", location);
		
		dest.writeString(location);
		dest.writeString(description);
		dest.writeParcelable(category, 0);

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public GregorianCalendar getStartTime() {
		return startTime;
	}

	public void setStartTime(GregorianCalendar startTime) {
		this.startTime = startTime;
	}

	public GregorianCalendar getEndTime() {
		return endTime;
	}

	public void setEndTime(GregorianCalendar endTime) {
		this.endTime = endTime;
	}

	public College getCollege() {
		return college;
	}

	public void setCollege(College college) {
		this.college = college;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return subject;
	}

}
