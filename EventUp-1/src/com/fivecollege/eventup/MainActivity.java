package com.fivecollege.eventup;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.fivecollege.eventup.browseEvents.BrowseEventsMainActivity;
import com.fivecollege.eventup.db.EventDataSource;

public class MainActivity extends Activity {

	private EventDataSource datasource;

	private ArrayList<Event> mhcEvents = new ArrayList<Event>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		datasource = new EventDataSource(this);
		datasource.open();
		someeventinit();

		final ListView todayEventListView = (ListView) findViewById(R.id.today_event_list);

		List<Event> todayEvents = datasource.getTodayEvents();

		EventItemAdapter todayEventsAdapter = new EventItemAdapter(this,
				android.R.layout.simple_list_item_1, todayEvents);
		todayEventListView.setAdapter(todayEventsAdapter);

		todayEventListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Event event = (Event) (todayEventListView
						.getItemAtPosition(position));
				openEvent(event);
				Toast.makeText(getBaseContext(), "Click", Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	public final static String EVENT = "com.fivecollege.eventup.EVENT";

	public void openEvent(Event e) {
		Intent intent = new Intent(this, EventDetailActivity.class);
		// Bundle mBundle = new Bundle();
		//
		// mBundle.putParcelable("com.fivecollege.eventup.event", e);
		// intent.putExtras(mBundle);
		// intent.putExtra("com.fivecollege.eventup.la", "s");

		intent.putExtra(EVENT, e);
		startActivity(intent);
	}

	public Event getEvent(Element ele, College college) {
		Event event = new Event();
		String catagory = getValue(ele, "catagory");
		Category c = datasource.getCategoryByFullName(catagory);
		Log.d("catagory", catagory);
		String subject = getValue(ele, "subject");
		Log.d("subject", subject);

		String startDate = getValue(ele, "startDate");
		Log.d("startDate", startDate);
		GregorianCalendar startDateCal = new GregorianCalendar(Event.EST);
		try {
			startDateCal.setTime(EventDataSource.dateFormat.parse(startDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String startTime = getValue(ele, "startTime");
		Log.d("startTime", startTime);
		GregorianCalendar startTimeCal = new GregorianCalendar(Event.EST);
		try {
			startTimeCal.setTime(EventDataSource.timeFormat.parse(startTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String endDate = getValue(ele, "endDate");
		Log.d("endDate", endDate);
		GregorianCalendar endDateCal = new GregorianCalendar(Event.EST);
		try {
			endDateCal.setTime(EventDataSource.dateFormat.parse(endDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String endTime = getValue(ele, "endTime");
		Log.d("eneTime", endTime);
		GregorianCalendar endTimeCal = new GregorianCalendar(Event.EST);
		try {
			endTimeCal.setTime(EventDataSource.timeFormat.parse(endTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String place = getValue(ele, "place");
		Log.d("place", place);

		String des = getValue(ele, "description");
		Log.d("des", des);
		return datasource.createEvent(subject, startDateCal, startTimeCal,
				endDateCal, endTimeCal, college, place, des, c);

	}

	private boolean isNetworkAvailable() {
		// ConnectivityManager connectivityManager = (ConnectivityManager)
		// getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo activeNetworkInfo = connectivityManager
		// .getActiveNetworkInfo();
		// return activeNetworkInfo != null;
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

	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	public void readInEvent() {

		String mhcURL = "http://ruimincai.com/MHCEvent.xml";
		String hampURL = "http://ruimincai.com/HampEvent.xml";

		String mhcXML = getXmlFromUrl(mhcURL);

		Document doc = getDomElement(mhcXML);
		Element docEle = doc.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("Event");
		College mhc = datasource.getCollegeByShortName("MHC");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element event = (Element) nl.item(i);
				Event e = getEvent(event, mhc);
				mhcEvents.add(e);
			}

		}

		String hampXML = getXmlFromUrl(hampURL);
		College hc = datasource.getCollegeByShortName("HC");

		doc = getDomElement(hampXML);
		docEle = doc.getDocumentElement();
		nl = docEle.getElementsByTagName("Event");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Element event = (Element) nl.item(i);
				Event e = getEvent(event, hc);
				// hampEvents.add(e);
			}

		}

	}

	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}

	public String getXmlFromUrl(String url) {
		String xml = null;

		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			xml = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// return XML
		return xml;
	}

	private void someeventinit() {

		if (isNetworkAvailable()) {
			Log.d("net", "yes");
			datasource.deleteAllEvent();
			readInEvent();
		} else {
			Log.d("net", "no");

		}
		// College c0 = datasource.getCollege(1);
		// Log.d("init -c0 =", c0.getName());
		// College c1 = datasource.getCollege(2);
		//
		// College c2 = datasource.getCollege(3);
		// College c3 = datasource.getCollege(4);
		//
		// Category ca1 = datasource.getCategory(1);
		// Category ca2 = datasource.getCategory(2);
		//
		// GregorianCalendar startTime1 = new GregorianCalendar(Event.EST);
		// startTime1.set(2005, Calendar.JANUARY, 20, 12, 5, 5);
		//
		// GregorianCalendar startTime2 = new GregorianCalendar(Event.EST);
		// startTime2.set(2006, Calendar.MAY, 3, 2, 3, 44);
		//
		// GregorianCalendar startTime3 = new GregorianCalendar(Event.EST);
		// startTime3.set(2007, Calendar.MARCH, 14, 15, 5, 22);
		//
		// GregorianCalendar startTime4 = new GregorianCalendar(Event.EST);
		// startTime4.set(2007, Calendar.MARCH, 14, 3, 5, 22);
		//
		// GregorianCalendar endTime = new GregorianCalendar(Event.EST);
		// endTime.set(2005, Calendar.JANUARY, 20, 12, 7, 5);
		// datasource.deleteAllEvent();
		// datasource.createEvent("Dummy Event 1", startTime1, startTime1,
		// endTime, endTime, c1, "loc1", "desc", ca1);
		// datasource.createEvent("Dummy Event 2", startTime2, startTime2,
		// endTime, endTime, c2, "loc2", "desc", ca1);
		// datasource.createEvent("Dummy Event 3", startTime3, startTime3,
		// endTime, endTime, c0, "loc3", "desc", ca1);
		// datasource.createEvent("Dummy Event 4", startTime4, startTime4,
		// endTime, endTime, c3, "loc4", "desc", ca2);
	}

	public void goToBrowse(View view) {
		Intent intent = new Intent(this, BrowseEventsMainActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
