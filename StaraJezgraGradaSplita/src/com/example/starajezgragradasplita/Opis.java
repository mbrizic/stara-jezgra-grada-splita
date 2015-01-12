package com.example.starajezgragradasplita;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mbrizic.starajezgragradasplita.R;

public class Opis extends ActionBarActivity {

	private TextView opis;

	private ImageView picView;

	// Lista svih ImageView-ova
	ArrayList<ImageView> imgArray = new ArrayList<ImageView>();
	// Index liste imgArray
	int indexImgView = 0;
	// Objekt kojim se dohvacaju elementi klase Lokacija
	Lokacija curLokacijaObj = null;

	// lokacijaPostoji = true -> lokacija postoji u XML datoteci
	boolean lokacijaPostoji;

	String markerTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opis);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		dohvatiMarker();

		opis = (TextView) findViewById(R.id.opis);
		
		// Postavljanje svih stvari da se otvori resursi.xml i pozove funkcija
		// za parsiranje xml-a -> parseXML(parser)
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			InputStream in_s = getApplicationContext().getAssets().open("resursi.xml");
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in_s, null);

			parseXML(parser);

			in_s.close();

		} catch (XmlPullParserException e) {
			Toast.makeText(getApplicationContext(), "Greska u otvaranju resursi.xml", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(),	"Greska u otvaranju resursi.xml", Toast.LENGTH_LONG).show();
		}
		
		
		picView = (ImageView) findViewById(R.id.bigPicture);
		
		if(curLokacijaObj.getSlikaIndex() >= 0 || !curLokacijaObj.isPanoramaEmpty()) {
			final ImageView picView1 = (ImageView) findViewById(R.id.picture1);
			picView1.setVisibility(View.VISIBLE);
			imgArray.add(picView1);
			
			imgArray.get(0).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!curLokacijaObj.isPanoramaEmpty() && indexImgView == 0) {
						pozivKlasePanorama();
					} else
						picView.setImageDrawable(picView1.getDrawable());
				}
			});
		}
		if(curLokacijaObj.getSlikaIndex() >= 1 || !curLokacijaObj.isPanoramaEmpty()) {
			final ImageView picView2 = (ImageView) findViewById(R.id.picture2);
			if((curLokacijaObj.getSlikaIndex() + curLokacijaObj.getPanoramSize()) >= 1)
				picView2.setVisibility(View.VISIBLE);
			imgArray.add(picView2);
			
			imgArray.get(1).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!curLokacijaObj.isPanoramaEmpty() && indexImgView == 1) {
						pozivKlasePanorama();
					} else
						picView.setImageDrawable(picView2.getDrawable());
				}
			});
		}
		if(curLokacijaObj.getSlikaIndex() >= 2 || !curLokacijaObj.isPanoramaEmpty()) {
			final ImageView picView3 = (ImageView) findViewById(R.id.picture3);
			if((curLokacijaObj.getSlikaIndex() + curLokacijaObj.getPanoramSize()) >= 2)
				picView3.setVisibility(View.VISIBLE);
			imgArray.add(picView3);
			
			imgArray.get(2).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!curLokacijaObj.isPanoramaEmpty() && indexImgView == 2) {
						pozivKlasePanorama();
					} else
						picView.setImageDrawable(picView3.getDrawable());
				}
			});
		}
		if(curLokacijaObj.getSlikaIndex() == 3 || !curLokacijaObj.isPanoramaEmpty()) {
			final ImageView picView4 = (ImageView) findViewById(R.id.picture4);
			if((curLokacijaObj.getSlikaIndex() + curLokacijaObj.getPanoramSize()) == 3)
				picView4.setVisibility(View.VISIBLE);
			imgArray.add(picView4);
			
			imgArray.get(3).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (!curLokacijaObj.isPanoramaEmpty() && indexImgView == 3) {
						pozivKlasePanorama();
					} else
						picView.setImageDrawable(picView4.getDrawable());
				}
			});
		}

		// Ako postoji lokacija u XML datoteci onda prikazi njen sadrzaj
		if(lokacijaPostoji)
			prikaziLokaciju(curLokacijaObj);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

	// Otvaranje Pnorama activity-a i slanje lokacije slike iz XML-a
	public void pozivKlasePanorama() {
		Intent intent = new Intent(this, Panorama.class);
		intent.putExtra("panorama", curLokacijaObj.getPanorama());
		startActivity(intent);
	}

	// Funkcija za prasiranje XML-a
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,
			IOException {

		int eventType = parser.getEventType();
		String curText = "";
		int indexSlika = 0;
		
		// pronadeno = true -> trenutno se obraduje lokacija
		boolean pronadeno = false;
		
		lokacijaPostoji = false;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tagname = parser.getName();

			switch (eventType) {
			case XmlPullParser.START_TAG:
				if (tagname.equalsIgnoreCase("lokacija")) {
					if (parser.getAttributeValue(null, "ime").equals(markerTitle)) {
						pronadeno = true;
						lokacijaPostoji = true;
						// Ako pocinje <lokacija> napravi novi objekt klase
						// Lokacija
						curLokacijaObj = new Lokacija();
						indexSlika = 0;
					}
				}
				break;

			case XmlPullParser.TEXT:
				if (pronadeno) {
					// Dohvati tekst izmedu tagova
					curText = parser.getText();
					break;
				}

			case XmlPullParser.END_TAG:
				if (pronadeno) {
					if (tagname.equalsIgnoreCase("lokacija")) {
						// ako je </lokacija> prekini s pretrazivanjem
						pronadeno = false;
					} else if (tagname.equalsIgnoreCase("slika")) {
						curLokacijaObj.setSlikaUrl(curText, indexSlika);
						indexSlika++;
					} else if (tagname.equalsIgnoreCase("naslov")) {
						curLokacijaObj.setNaslov(curText);
					} else if (tagname.equalsIgnoreCase("panorama")) {
						curLokacijaObj.setPanorama(curText);
					} else if (tagname.equalsIgnoreCase("opis")) {
						curLokacijaObj.setOpis(curText);
					}
				}
				break;

			default:
				break;

			}
			eventType = parser.next();
		}
	}

	// Funkcija za prikaz sadrzaja lokacije na ekran
	public void prikaziLokaciju(Lokacija curLokacijaObj) {
		setTitle(curLokacijaObj.getNaslov());
		opis.setText(curLokacijaObj.getOpis());

		// Postavljanje malih slika
		for (indexImgView = 0; indexImgView <= curLokacijaObj.getSlikaIndex(); indexImgView++) {
			BitmapWorkerTask task = new BitmapWorkerTask(imgArray.get(indexImgView));
			//inHorizontalScrollView.addView(imgArray.get(indexImgView));
			task.execute(curLokacijaObj.getSlikaUrl(indexImgView));
		}
		// Postavljenje velike slike na prvu malu
		BitmapWorkerTask task = new BitmapWorkerTask(picView);
		if(indexImgView > 0)
			task.execute(curLokacijaObj.getSlikaUrl(0));
		

		// Postavljanje panorame na zadnje mjesto malih slika
		if (!curLokacijaObj.isPanoramaEmpty() && indexImgView <= imgArray.size()) {
			Resources res = getResources();
			int resourceId = res.getIdentifier(curLokacijaObj.getPanorama(), "drawable", getPackageName());
			Drawable drawable = res.getDrawable(resourceId);
			imgArray.get(indexImgView).setImageDrawable(drawable);
		}
	}

	public void dohvatiMarker() {
		Bundle podaci = getIntent().getExtras();
		if (podaci != null) {
			// Odabra san markerTitle jer to sami mozemo podesit
			markerTitle = podaci.getString("markerTitle");
		}
	}

	// Postavljanje tipke za povratak na MainActivity
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Opis.this, MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
	    startActivity(intent);
		return true;
	}
}
