package com.fivecollege.eventup.browseEvents.byDate;

import java.util.GregorianCalendar;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fivecollege.eventup.R;
import com.fivecollege.eventup.db.EventDataSource;
import com.fivecollege.eventup.db.EventDataSource.ItemWithCounts;

public class BrowseByDateChooseDateActivity extends ListActivity {

	private EventDataSource datasource;

	public static final String DATE = "date";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_by_date_choose_date);

		datasource = new EventDataSource(this);
		datasource.open();

		List<ItemWithCounts<GregorianCalendar>> availableDates = datasource
				.getAvailableDates();

		ArrayAdapter<ItemWithCounts<GregorianCalendar>> dateAdapter = new ArrayAdapter(
				this, android.R.layout.simple_list_item_1, availableDates);
		setListAdapter(dateAdapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		ArrayAdapter<ItemWithCounts<GregorianCalendar>> adapter = (ArrayAdapter<ItemWithCounts<GregorianCalendar>>) this
				.getListAdapter();
		ItemWithCounts<GregorianCalendar> o = adapter.getItem(position);
		openDate(o.getItem());
	}

	private void openDate(GregorianCalendar date) {
		Intent intent = new Intent(this, BrowseByDateTabsActivity.class);
		intent.putExtra(DATE, EventDataSource.dateFormat.format(date.getTime()));
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

}