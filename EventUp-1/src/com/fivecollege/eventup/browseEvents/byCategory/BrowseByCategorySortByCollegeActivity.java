package com.fivecollege.eventup.browseEvents.byCategory;

import java.util.GregorianCalendar;
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
import com.fivecollege.eventup.College;
import com.fivecollege.eventup.Event;
import com.fivecollege.eventup.EventDetailActivity;
import com.fivecollege.eventup.EventItemAdapter;
import com.fivecollege.eventup.MainActivity;
import com.fivecollege.eventup.R;
import com.fivecollege.eventup.SectionedAdapter;
import com.fivecollege.eventup.db.EventDataSource;

public class BrowseByCategorySortByCollegeActivity extends ListActivity {

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
		setContentView(R.layout.activity_browse_by_category_sort_by_date);
		int categoryId = (int) getIntent().getLongExtra(
				BrowseByCategoryChooseCategoryActivity.CATEGORY_ID, 0);

		Log.d("categoryId", categoryId + "");
		datasource = new EventDataSource(this);
		datasource.open();
		Category co = datasource.getCategory(categoryId);
		List<College> colleges = datasource.getColleges(categoryId);
		for (College c : colleges) {
			Log.d("coleg", c.getName());
			List<Event> events = datasource.getEvent(co, (int) (c.getId()));
			adapter.addSection(c.getName(), new EventItemAdapter(this,
					android.R.layout.simple_list_item_1, events));
			Log.d("tryget-0", adapter.getItem(0).toString());
		}

		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.activity_browse_by_category_sort_by_college, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
