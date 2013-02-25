package com.fivecollege.eventup.browseEvents.byCategory;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fivecollege.eventup.Category;
import com.fivecollege.eventup.College;
import com.fivecollege.eventup.R;
import com.fivecollege.eventup.browseEvents.byCollege.BrowseByCollegeTabsActivity;
import com.fivecollege.eventup.db.EventDataSource;
import com.fivecollege.eventup.db.EventDataSource.ItemWithCounts;

public class BrowseByCategoryChooseCategoryActivity extends ListActivity {

	private EventDataSource datasource;

	public static final String CATEGORY_ID = "category_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_by_college_choose_college);

		datasource = new EventDataSource(this);
		datasource.open();

		List<ItemWithCounts<Category>> availableCategories = datasource
				.getAvailableCategory();

		ArrayAdapter<ItemWithCounts<Category>> dateAdapter = new ArrayAdapter<ItemWithCounts<Category>>(
				this, android.R.layout.simple_list_item_1, availableCategories);
		setListAdapter(dateAdapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked

		ArrayAdapter<ItemWithCounts<Category>> adapter = (ArrayAdapter<ItemWithCounts<Category>>) this
				.getListAdapter();
		ItemWithCounts<Category> o = adapter.getItem(position);
		openCollege(o.getItem());
	}

	private void openCollege(Category category) {
		Intent intent = new Intent(this, BrowseByCategoryTabsActivity.class);
		intent.putExtra(CATEGORY_ID, category.getId());
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
