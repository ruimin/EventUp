package com.fivecollege.eventup.browseEvents.byCollege;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.fivecollege.eventup.R;
import com.fivecollege.eventup.db.EventDataSource;

public class BrowseByCollegeTabsActivity extends TabActivity {

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
				BrowseByCollegeSortByDateActivity.class);
		intent.putExtras(getIntent().getExtras());
		spec = tabHost.newTabSpec("by_date").setIndicator("By Date")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this,
				BrowseByCollegeSortByCategoryActivity.class);
		intent.putExtras(getIntent().getExtras());

		spec = tabHost.newTabSpec("by_category").setIndicator("By Category")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 35;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 35;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_browse_by_college_tabs, menu);
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
