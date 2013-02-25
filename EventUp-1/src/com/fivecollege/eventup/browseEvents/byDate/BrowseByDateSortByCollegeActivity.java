package com.fivecollege.eventup.browseEvents.byDate;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.fivecollege.eventup.College;
import com.fivecollege.eventup.Event;
import com.fivecollege.eventup.EventDetailActivity;
import com.fivecollege.eventup.EventItemAdapter;
import com.fivecollege.eventup.MainActivity;
import com.fivecollege.eventup.R;
import com.fivecollege.eventup.SectionedAdapter;
import com.fivecollege.eventup.db.EventDataSource;

public class BrowseByDateSortByCollegeActivity extends ListActivity {

	private EventDataSource datasource;

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

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_browse_by_date_sort_by_college);
		String date = getIntent().getStringExtra(
				BrowseByDateChooseDateActivity.DATE);

		Log.d("date", date);
		datasource = new EventDataSource(this);
		datasource.open();

		List<College> colleges = datasource.getColleges(date);

		int section = 0;

		for (College c : colleges) {
			Log.d("College", c.getName());
			List<Event> events = datasource.getEvent(c, date);
			adapter.addSection(c.getName(), new EventItemAdapter(this,
					android.R.layout.simple_list_item_1, events));
			section++;
			Log.d("tryget-0", adapter.getItem(0).toString());
			// Log.d("tryget-0", adapter.getView(section,null,null).toString());

			// final ListView listView= (ListView)

			//
			// listView.setOnItemClickListener(new OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> a, View v, int position,
			// long id) {
			// Event event = (Event) (listView
			// .getItemAtPosition(position));
			// openEvent(event);
			// Toast.makeText(getBaseContext(), "Click", Toast.LENGTH_LONG)
			// .show();
			// }
			// });

		}

		setListAdapter(adapter);
	}

	protected void onListItemClick(ListView parent, View v, int pos, long id) {

		openEvent((Event) adapter.getItem(pos));

	}

	public void openEvent(Event e) {
		Intent intent = new Intent(this, EventDetailActivity.class);
		intent.putExtra(MainActivity.EVENT, e);
		startActivity(intent);
	}

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
}
