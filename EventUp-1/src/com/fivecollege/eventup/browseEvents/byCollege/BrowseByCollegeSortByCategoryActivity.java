package com.fivecollege.eventup.browseEvents.byCollege;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fivecollege.eventup.Category;
import com.fivecollege.eventup.Event;
import com.fivecollege.eventup.EventDetailActivity;
import com.fivecollege.eventup.EventItemAdapter;
import com.fivecollege.eventup.MainActivity;
import com.fivecollege.eventup.R;
import com.fivecollege.eventup.SectionedAdapter;
import com.fivecollege.eventup.browseEvents.byDate.BrowseByDateChooseDateActivity;
import com.fivecollege.eventup.db.EventDataSource;

public class BrowseByCollegeSortByCategoryActivity extends ListActivity {

	private EventDataSource datasource;

	SectionedAdapter adapter = new SectionedAdapter() {
		protected View getHeaderView(String caption, int index,
				View convertView, ViewGroup parent) {
			TextView result = (TextView) convertView;

			if (convertView == null) {
				result = (TextView) getLayoutInflater().inflate(
						R.layout.header, null);
			}

			result.setText(caption);

			return (result);
		}
	};

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

	protected void onListItemClick(ListView parent, View v, int pos, long id) {

		openEvent((Event) adapter.getItem(pos));

	}

	public void openEvent(Event e) {
		Intent intent = new Intent(this, EventDetailActivity.class);
		intent.putExtra(MainActivity.EVENT, e);
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_by_college_sort_by_category);
		int CollegeId = (int) getIntent().getLongExtra(
				BrowseByCollegeChooseCollegeActivity.COLLEGE_ID, 0);

		Log.d("collegeid", CollegeId + "");
		datasource = new EventDataSource(this);
		datasource.open();

		List<Category> categories = datasource.getCategories(CollegeId);
		for (Category c : categories) {
			Log.d("Category", c.getName());
			List<Event> events = datasource.getEvent(c, CollegeId);
			adapter.addSection(c.getName(), new EventItemAdapter(this,
					android.R.layout.simple_list_item_1, events));
			Log.d("tryget-0", adapter.getItem(0).toString());

		}

		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(
				R.menu.activity_browse_by_date_sort_by_category, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
