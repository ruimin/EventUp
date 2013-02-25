package com.fivecollege.eventup.browseEvents.byCategory;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.fivecollege.eventup.R;
import com.fivecollege.eventup.browseEvents.byCollege.BrowseByCollegeSortByCategoryActivity;
import com.fivecollege.eventup.browseEvents.byCollege.BrowseByCollegeSortByDateActivity;
import com.fivecollege.eventup.db.EventDataSource;

public class BrowseByCategoryTabsActivity extends TabActivity {

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_by_college_tabs);

		datasource = new EventDataSource(this);
		datasource.open();

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this,
				BrowseByCategorySortByCollegeActivity.class);
		intent.putExtras(getIntent().getExtras());
		spec = tabHost.newTabSpec("by_college").setIndicator("By College")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this,
				BrowseByCategorySortByDateActivity.class);
		intent.putExtras(getIntent().getExtras());

		spec = tabHost.newTabSpec("by_date").setIndicator("By Date")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 35;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 35;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater()
				.inflate(R.menu.activity_browse_by_category_tabs, menu);
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
