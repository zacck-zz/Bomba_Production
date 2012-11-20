package com.melanie.bomba;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.melanie.listview.CustomizedListView;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class TabView extends TabActivity {
	private Bundle bundle;
	private Bundle extras;
	private TabHost tabHost;
	private TextView activityTitle;
	private static final int VIEW_SEARCH = 0;
	public static Typeface tf;

	Facebook facebook = new Facebook("280604498727857");
	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	private SharedPreferences mPrefs;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabs);
		// facebook login code
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}
		if (!facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "publish_stream" },
					new DialogListener() {

						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub
							Log.v("TAG", e.toString());

						}

						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							Log.v("TAG	", e.toString());

						}

						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = mPrefs.edit();
		                    editor.putString("access_token", facebook.getAccessToken());
		                    editor.putLong("access_expires", facebook.getAccessExpires());
		                    editor.commit();

						}

						public void onCancel() {
							// TODO Auto-generated method stub

						}
					});
		}

		new Handler();
		bundle = new Bundle();
		extras = this.getIntent().getExtras();

		//activityTitle = (TextView) findViewById(R.id.title_text);
		//tf = Typeface.createFromAsset(getAssets(),
		//		"data/fonts/BROKEN_GHOST.ttf");
		//activityTitle.setTextSize(24);
		//activityTitle.setTypeface(tf);

		tabHost = getTabHost();
		PopulateTab();
		tabHost.setCurrentTab(0);
		setTabColor(tabHost);
		// set tab colors on tab change as well
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String arg0) {
				setTabColor(tabHost);
			}

		});

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	// Vile i will handle tab selection
	public static void setTabColor(TabHost tabhost) {
		for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
			// unselected
			tabhost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.parseColor("#606060"));

			TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title); // Unselected Tabs
			tv.setTextColor(Color.parseColor("#ffffff"));

		}

		// selected
		tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())

		.setBackgroundColor(Color.parseColor("#3D3D3D"));
		TextView tv = (TextView) tabhost.getCurrentTabView().findViewById(
				android.R.id.title);
		tv.setTextColor(Color.parseColor("#ffffff"));
	}

	public void PopulateTab() {
		if (activityTitle != null)
			activityTitle.setText(getTitle());
		// category
		tabHost.addTab(tabHost
				.newTabSpec("Songs")
				.setIndicator(getString(R.string.home_tab),
						getResources().getDrawable(R.drawable.chromeiconhome))
				.setContent(new Intent(TabView.this, CustomizedListView.class)));

		// Map of clubs
		Intent Player = new Intent(TabView.this, MainActivity.class);

		tabHost.addTab(tabHost
				.newTabSpec("Tracks")
				.setIndicator(
						getString(R.string.discover_tab),
						getResources().getDrawable(
								R.drawable.chromeiconnowplaying))
				.setContent(new Intent(Player)));

		tabHost.addTab(tabHost
				.newTabSpec("Search")
				.setIndicator(getString(R.string.search_tab),
						getResources().getDrawable(R.drawable.chromesearch))
				.setContent(new Intent(Player)));

	}

}
