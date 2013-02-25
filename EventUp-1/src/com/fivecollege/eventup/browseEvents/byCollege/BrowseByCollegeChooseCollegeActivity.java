package com.fivecollege.eventup.browseEvents.byCollege;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fivecollege.eventup.College;
import com.fivecollege.eventup.R;
import com.fivecollege.eventup.db.EventDataSource;
import com.fivecollege.eventup.db.EventDataSource.ItemWithCounts;

public class BrowseByCollegeChooseCollegeActivity extends ListActivity {

	private EventDataSource datasource;

	public static final String COLLEGE_ID = "college_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_by_college_choose_college);

		datasource = new EventDataSource(this);
		datasource.open();

		List<ItemWithCounts<College>> availableColleges = datasource
				.getAvailableCollege();

		ArrayAdapter<ItemWithCounts<College>> dateAdapter = new ArrayAdapter(
				this, android.R.layout.simple_list_item_1, availableColleges);
		setListAdapter(dateAdapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		ArrayAdapter<ItemWithCounts<College>> adapter = (ArrayAdapter<ItemWithCounts<College>>) this
				.getListAdapter();
		ItemWithCounts<College> o = adapter.getItem(position);
		openCollege(o.getItem());
	}

	private void openCollege(College college) {
		Intent intent = new Intent(this, BrowseByCollegeTabsActivity.class);
		intent.putExtra(COLLEGE_ID, college.getId());
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
