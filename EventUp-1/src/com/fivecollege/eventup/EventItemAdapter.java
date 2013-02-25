package com.fivecollege.eventup;

import java.util.List;

import com.fivecollege.eventup.db.EventDataSource;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventItemAdapter extends ArrayAdapter<Event> {
	private List<Event> events;
	Context context;

	public EventItemAdapter(Context context, int textViewResourceId,
			List<Event> events) {
		super(context, textViewResourceId, events);
		this.events = events;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Event event = events.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			// row = inflater.inflate(layoutResourceId, parent, false);

			row = inflater.inflate(R.layout.listitem, null);
		}
		if (event != null) {
			TextView username = (TextView) row.findViewById(R.id.eventname);
			TextView email = (TextView) row.findViewById(R.id.dateAndTime);

			if (username != null) {
				username.setText(event.getSubject());
			}

			if (email != null) {

				email.setText(EventDataSource.dateFormat.format(event
						.getStartDate().getTime())
						+ "  "
						+ EventDataSource.timeFormat.format(event.getStartTime()
								.getTime())
						+ "-"
						+ EventDataSource.timeFormat.format(event.getEndTime()
								.getTime()));
			}
		}
		return row;
	}
}
