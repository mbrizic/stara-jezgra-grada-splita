package com.example.starajezgragradasplita;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

public class Panorama extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		String panorama = intent.getExtras().getString("panorama");

		Resources res = getResources();
		String mDrawableName = panorama;
		int resourceId = res.getIdentifier(mDrawableName, "drawable", getPackageName());

		TouchImageView img = new TouchImageView(this);
		img.setImageResource(resourceId);
		img.setMaxZoom(4f);
		setContentView(img);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}
}
