package com.example.starajezgragradasplita;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mbrizic.starajezgragradasplita.R;

class Lokacija {

	public String koordinate;
	public String slika;
	public String naslov;
	public String opis;
}

public class Opis extends ActionBarActivity{

	//variable for selection intent
	private final int PICKER = 1;
	//variable to store the currently selected image
	private int currentPic = 0;
	//gallery object
	private Gallery picGallery;
	//image view for larger display
	private ImageView picView;
	
	//adapter for gallery view
	private PicAdapter imgAdapt;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opis);
        
        //get the large image view
        picView = (ImageView) findViewById(R.id.picture); 
        //get the gallery view
        picGallery = (Gallery) findViewById(R.id.gallery);
        //create a new adapter
        imgAdapt = new PicAdapter(this);
        //set the gallery adapter
        picGallery.setAdapter(imgAdapt);
        
        XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			    InputStream in_s = getApplicationContext().getAssets().open("resursi.xml");
		        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	            parser.setInput(in_s, null);
	            Toast.makeText(getApplicationContext(), "Uspjesno otvaranje resursi.xml", Toast.LENGTH_LONG).show();

	            parseXML(parser);
	            Toast.makeText(getApplicationContext(), "Završeno parsiranje resursi.xml", Toast.LENGTH_LONG).show();
	            
		} catch (XmlPullParserException e) {
			Toast.makeText(getApplicationContext(), "Greska u otvaranju resursi.xml", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Greska u otvaranju resursi.xml", Toast.LENGTH_LONG).show();
		}
    }
	
	private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException{
		
		ArrayList<Lokacija> lokacije = null;
        int eventType = parser.getEventType();
        Lokacija trenutnaLokacija = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String atribut = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	lokacije = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    atribut = parser.getName();
                    if (atribut == "lokacija"){
                        trenutnaLokacija = new Lokacija();
                    } else if (trenutnaLokacija != null){
                        if (atribut == "koordinate"){
                            trenutnaLokacija.koordinate = parser.nextText();
                        } else if (atribut == "slika"){
                        	trenutnaLokacija.slika = parser.nextText();
                        } else if (atribut == "naslov"){
                            trenutnaLokacija.naslov= parser.nextText();
                        } else if (atribut == "opis"){
                            trenutnaLokacija.opis= parser.nextText();
                        }  
                    }
                    break;
                case XmlPullParser.END_TAG:
                    atribut = parser.getName();
                    if (atribut.equalsIgnoreCase("lokacija") && trenutnaLokacija != null){
                    	lokacije.add(trenutnaLokacija);
                    } 
            }
            eventType = parser.next();
        }
 
        prikaziLokaciju();
        //ubacit u funkciju ArrayList i pronaæ odgovarajuæe slike
	}
	
	public void prikaziLokaciju(/*String imageUrl*/){
		Toast.makeText(getApplicationContext(), "Tijelo klase pirkaziLokaciju", Toast.LENGTH_LONG).show();
		
		//try {
			  //Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
			//URL url = new URL("http://adria.fesb.hr/~isoric/GIS/img019.jpg");
			//Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			//ImageView img = (ImageView) findViewById(R.id.imageView1);
			BitmapWorkerTask task = new BitmapWorkerTask(picView);
			task.execute("http://adria.fesb.hr/~isoric/GIS/img019.jpg");
	        
	        
			  //picView.setImageBitmap(bitmap); 
			/*} catch (MalformedURLException e) {
			  e.printStackTrace();
			  Toast.makeText(getApplicationContext(), "Greska u pirkaziLokaciju", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
			  e.printStackTrace();
			  Toast.makeText(getApplicationContext(), "Greska u pirkaziLokaciju", Toast.LENGTH_LONG).show();
			}*/
	}
	
	public class PicAdapter extends BaseAdapter {
		
		//use the default gallery background image
		int defaultItemBackground;
		         
		//gallery context
		private Context galleryContext;
		 
		//array to store bitmaps to display
		private Bitmap[] imageBitmaps;
		 
		//placeholder bitmap for empty spaces in gallery
		Bitmap placeholder;
		
		public PicAdapter(Context c) {
			 
		    //instantiate context
		    galleryContext = c;
		 
		    //create bitmap array
		    imageBitmaps  = new Bitmap[10];
		             
		    //decode the placeholder image
		    placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		     
		    //set placeholder as all thumbnail images in the gallery initially
		    for(int i=0; i<imageBitmaps.length; i++)
		        imageBitmaps[i]=placeholder;
		    
		    //get the styling attributes - use default Andorid system resources
		    TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);
		     
		    //get the background resource
		    defaultItemBackground = styleAttrs.getResourceId(
		        R.styleable.PicGallery_android_galleryItemBackground, 0);
		     
		    //recycle attributes
		    styleAttrs.recycle();
		}

		//return number of data items i.e. bitmap images
		public int getCount() {
		    return imageBitmaps.length;
		}

		//return item at specified position
		public Object getItem(int position) {
		    return position;
		}

		//return item ID at specified position
		public long getItemId(int position) {
		    return position;
		}

		//get view specifies layout and display options for each thumbnail in the gallery
		public View getView(int position, View convertView, ViewGroup parent) {
		 
		    //create the view
		    ImageView imageView = new ImageView(galleryContext);
		    //specify the bitmap at this position in the array
		    imageView.setImageBitmap(imageBitmaps[position]);
		    //set layout options
		    imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
		    //scale type within view area
		    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		    //set default gallery item background
		    imageView.setBackgroundResource(defaultItemBackground);
		    //return the view
		    return imageView;
		}
		 
	}
}
