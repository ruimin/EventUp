package com.fivecollege.eventup;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.fivecollege.eventup.db.EventDataSource;

public class EventDetailActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);

		Event e = (Event) getIntent().getParcelableExtra(MainActivity.EVENT);
		Log.d("has e", getIntent().getParcelableExtra(MainActivity.EVENT)
				.toString());
		TextView subject = (TextView) findViewById(R.id.event_detail_event_title);
		subject.setText(e.getSubject());

		TextView category = (TextView) findViewById(R.id.event_detail_event_category);
		category.setText(e.getCategory().getName());

		TextView location = (TextView) findViewById(R.id.event_detail_event_location);
		location.setText(e.getCollege().getName() + " : " + e.getLocation());
		TextView time = (TextView) findViewById(R.id.event_detail_event_time);
		time.setText(EventDataSource.dateFormat.format(e.getStartDate()
				.getTime())
				+ " "
				+ EventDataSource.timeFormat.format(e.getStartTime().getTime())
				+ " - "
				+ EventDataSource.timeFormat.format(e.getEndTime().getTime()));
		TextView description = (TextView) findViewById(R.id.event_detail_event_description);
		String des = e.getDescription();

		if ((des.substring(0, 4)).equals("http")) {

			if (isNetworkAvailable()) {
				Log.e("isNetworkAvailable", "yes");

				WebView webView = (WebView) findViewById(R.id.event_detail_event_description_url);
				webView.loadUrl(des);
				description.setText("");
			} else {
				Log.e("isNetworkAvailable", "no");

				description
						.setText("connect to the internet to view more details about the event.");
			}

		} else {
			description.setText(des);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_event_detail, menu);
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

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {

			return true;
			// notify user you are online

		}
		// NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		// boolean isConnected = activeNetwork.isConnectedOrConnecting();
		// return isConnected;
		return false;
		
	}

}
