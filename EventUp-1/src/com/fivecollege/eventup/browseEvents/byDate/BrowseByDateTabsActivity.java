package com.fivecollege.eventup.browseEvents.byDate;

import com.fivecollege.eventup.R;
import com.fivecollege.eventup.R.layout;
import com.fivecollege.eventup.R.menu;
import com.fivecollege.eventup.db.EventDataSource;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.support.v4.app.NavUtils;

public class BrowseByDateTabsActivity extends TabActivity {

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
		setContentView(R.layout.activity_browse_by_date_tabs);

		datasource = new EventDataSource(this);
		datasource.open();


		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this,
				BrowseByDateSortByCollegeActivity.class);
		intent.putExtras(getIntent().getExtras());
		spec = tabHost.newTabSpec("by_college").setIndicator("By College")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this,
				BrowseByDateSortByCategoryActivity.class);
		intent.putExtras(getIntent().getExtras());

		spec = tabHost.newTabSpec("by_date").setIndicator("By Category")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 35;
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 35;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_browse_by_date_tabs, menu);
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
