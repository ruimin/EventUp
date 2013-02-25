package com.fivecollege.eventup.browseEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fivecollege.eventup.R;
import com.fivecollege.eventup.R.id;
import com.fivecollege.eventup.R.layout;
import com.fivecollege.eventup.browseEvents.byCategory.BrowseByCategoryChooseCategoryActivity;
import com.fivecollege.eventup.browseEvents.byCollege.BrowseByCollegeChooseCollegeActivity;
import com.fivecollege.eventup.browseEvents.byDate.BrowseByDateChooseDateActivity;
import com.fivecollege.eventup.browseEvents.byDate.BrowseByDateTabsActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Browse main menu, gives user to choose options between browse by data,
 * college, or category
 * 
 * @author Angela
 * 
 */
public class BrowseEventsMainActivity extends ListActivity {

	public enum BrowseFilter {
		BROWSE_BY_DATE("By Date",
				//BrowseByDateTabsActivity.class
				BrowseByDateChooseDateActivity.class
				), BROWSE_BY_COLLEGE(
				"By College", BrowseByCollegeChooseCollegeActivity.class), BROWSE_BY_CATEGORY(
				"By Category", BrowseByCategoryChooseCategoryActivity.class);
		private String string;
		private static Map<Integer, BrowseFilter> ss = new TreeMap<Integer, BrowseFilter>();
		private int id;
		private static final int START_VALUE = 0;
		private Class associatedActivityClass;

		static {
			for (int i = 0; i < values().length; i++) {
				values()[i].id = START_VALUE + i;
				ss.put(values()[i].id, values()[i]);
			}
		}

		public Class getAssociatedActivityClass() {
			return associatedActivityClass;
		}

		BrowseFilter(String string, Class associatedActivityClass) {
			this.string = string;
			this.associatedActivityClass = associatedActivityClass;
		}

		@Override
		public String toString() {
			return string;
		}

		public static BrowseFilter fromInt(int i) {
			return ss.get(i);
		}

		public int value() {
			return id;
		}
	}

	List<String> browseMenu = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_main);

		ArrayAdapter<BrowseFilter> browseOptionsAdapter = new ArrayAdapter<BrowseFilter>(
				this, android.R.layout.simple_list_item_1,
				BrowseFilter.values());
		setListAdapter(browseOptionsAdapter);

		final BrowseEventsMainActivity act = this;
		getListView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {
						TextView tx = (TextView) findViewById(R.id.choose_button);
						tx.setText("" + id);
						act.startNew(id);
					}
				});

	}

	public void startNew(long id) {
		BrowseFilter filter = BrowseFilter.fromInt((int) id);
		Intent intent = new Intent(this, filter.getAssociatedActivityClass());
		startActivity(intent);
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {

		// ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>)
		// getListAdapter();
		// Comment comment = null;
		// ArrayAdapter<College> adapter = (ArrayAdapter<College>)
		// getListAdapter();
		//
		// College college = null;
		// switch (view.getId()) {
		// case R.id.add:
		// // List<String[]> colls = new ArrayList<String[]>();
		// // String[] m = { "MHC", "m" };
		// // String[] a = { "AC", "a" };
		// // String[] h = { "HC", "h" };
		// // colls.add(m);
		// // colls.add(a);
		// // colls.add(h);
		// // // String[] comments = new String[][] { { "" }, { "" } };
		// // int nextInt = new Random().nextInt(3);
		// // // Save the new comment to the database
		// // String[] c = colls.get(nextInt);
		// // college = datasource.createCollege(c[0], c[1]);
		// // adapter.add(college);
		// break;
		// case R.id.delete:
		// // if (getListAdapter().getCount() > 0) {
		// // college = (College) getListAdapter().getItem(0);
		// // datasource.deleteCollege(college);
		// // adapter.remove(college);
		// // }
		// break;
		// }
		// adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		// datasource.close();
		super.onPause();
	}

}