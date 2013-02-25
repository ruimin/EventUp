package com.fivecollege.eventup;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
	private long id;
	private String name;
	private String shortName;

	public Category(Parcel in) {
		id = in.readLong();
		name = in.readString();
		shortName = in.readString();
	}

	public Category() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
		public Category createFromParcel(Parcel in) {
			return new Category(in);
		}

		public Category[] newArray(int size) {
			return new Category[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(shortName);
	}
}