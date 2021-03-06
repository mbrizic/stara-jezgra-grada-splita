package com.example.starajezgragradasplita;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mbrizic.starajezgragradasplita.R;


public class MainActivity extends ActionBarActivity {

	private Karta karta;
	
	private String[] mItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    
    private String lang;
    
    private LatLng[] koordinateLokacija;
    private LatLng[] koordinatePodruma;
    private String[] imenaLokacijaHr;
    private String[] imenaLokacijaEn;
    private String[] imenaPodrumaHr;
    private String[] imenaPodrumaEn;
    
    private enum Kat{
    	PODRUMI, IZNAD
    }
		
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
		inicijalizirajMapu();
        inicijalizirajNavigationDrawer();
        
        lang = Locale.getDefault().getLanguage();
        Log.e("jezik", lang);
        
        koordinateLokacija = new LatLng[]{
        		new LatLng(43.509298, 16.440778), // sj vrata
        		new LatLng(43.509559, 16.439833), // sz kula
        		new LatLng(43.508125, 16.441258), // ist vrata
        		new LatLng(43.507156, 16.440795), // ji kula
        		new LatLng(43.508778, 16.439190), // zap vrata
        		new LatLng(43.507424, 16.439698), // ju� vrata
        		new LatLng(43.507926, 16.439969), // vestibul
        		new LatLng(43.508085, 16.440432), // mauzolej
        		new LatLng(43.508288, 16.439656), // jupiterov
        		new LatLng(43.507597, 16.440360), // blagovaonica
        		new LatLng(43.507551, 16.440129), // kriptoportik
        		new LatLng(43.508132, 16.440154), // peristil
        		new LatLng(43.508091, 16.440757)  // isto�no od mauzoleja      		
        };
        
        imenaLokacijaHr = new String[]{        		
        		"Sjeverna vrata", 			"Sjeverozapadna kula",	"Isto�na vrata", 
        		"Jugoisto�na kula", 		"Zapadna vrata", 		"Ju�na vrata", 
        		"Vestibul", 				"Mauzolej",  			"Jupiterov hram", 
        		"Blagovaonica", 			"Kriptoportik", 		"Peristil", 
        		"Zgrada isto�no od Mauzoleja"
        };
        
        imenaLokacijaEn = new String[]{
        		"Northern gate", 			"Northwestern tower",	"East Gate", 
        		"South tower", 				"West gate", 			"South gate", 
        		"Vestibule", 				"Mausoleum",  			"Temple of Jupiter", 
        		"Dining room", 				"Cryptoporticus", 		"Peristyle", 
        		"Building east of the Mausoleum"
        };
        
        
        koordinatePodruma = new LatLng[]{
        		new LatLng(43.507888, 16.439446), // 6A
        		new LatLng(43.507880, 16.439244), // 4A
        		new LatLng(43.507967, 16.439121), // 2C
        		new LatLng(43.508059, 16.439122), // 2D
        		new LatLng(43.508068, 16.439231), // 3D
        		new LatLng(43.507754, 16.439145), // juzno od 4A
        		new LatLng(43.507743, 16.439844), // 11A
        		new LatLng(43.507597, 16.440360), // 17B
        		new LatLng(43.507685, 16.440265), // 15C
        };
        
        imenaPodrumaHr = new String[]{
        		"Velika dvorana",     	"Mala dvorana",         						"Drvene grede",        			
        		"Sarkofag",	        	"Tijesak za proizvodnju maslinovog ulja",      	"Dijelovi kamenih cijevi anti�ke kanalizacije", 	
        		"Tablinum",         	"Triklinij",        							"Nimfej"
        };
        
        imenaPodrumaEn = new String[]{
        		"Great Hall",     	"Small Hall",         								"Beams of wood",        			
        		"Sarcophagus",	    "Pressure equipment for production of olive oil",   "Parts of stone pipes from the ancient sewage system", 	
        		"Tablinum  ",       "Triclinium",        								"Nymphaeum"
        };
        
        nacrtajMarkere(Kat.IZNAD);
    }
    
    private void inicijalizirajMapu() {
    	karta = new Karta();    	
    	
    	//postavljanje svojstva 'mapa' je mogu�e izvesti jedino iz ove aktivnosti, jer je vezano za elemente su�elja. Zato nije odvojena u posebnu klasu.
    	if (karta.mapa == null) { 
        	karta.mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
 
        	karta.postavi();        	
        	karta.mapa.setOnInfoWindowClickListener(new OnInfoWindowClickListener()); //definiran dolje kao unutranja klasa
            
        	
            if (karta.mapa == null){ 
            	String errorMessage = getResources().getString(R.string.map_error);
            	Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }            
    	}    	
    }
    
    public void nacrtajMarkere(Kat kat){
    	String dotakniZaVise = getResources().getString(R.string.tap_for_more);
    	String[] imena;
    	LatLng[] koordinate;
    	
    	if(kat == Kat.IZNAD){
    		if(lang.equalsIgnoreCase("en")){
    			imena = imenaLokacijaEn;
    		}else{
    			imena = imenaLokacijaHr;
    		}
    		
    	
       		
    		koordinate = koordinateLokacija;
    	}else{
    		if(lang.equalsIgnoreCase("en")){
    			imena = imenaPodrumaEn;
    		}else{
    			imena = imenaPodrumaHr;
    		}
    
    		
    		koordinate = koordinatePodruma;
    	}    	
    	    	
    	karta.mapa.clear();
    	karta.promjeniSloj(R.drawable.mapa, 0.3f);
    	
    	for(int i=0; i<imena.length; i++){
    		karta.dodajMarker(koordinate[i], imena[i], dotakniZaVise);
    	}
    }
    
    
    @Override //ovo je za postavke
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true; //otvori izbornik klikom na ikonu aplikacije
        }
        
        int id = item.getItemId();
        if (id == R.id.action_change_map) {
        	karta.promjeniVrstuMape();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    


    private void inicijalizirajNavigationDrawer(){
    	
    	mItems = getResources().getStringArray(R.array.navigation_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        getActionBar().setDisplayHomeAsUpEnabled(true); //treba dozvolit ove dvije stvari da 
        getActionBar().setHomeButtonEnabled(true);		//se mo�e dodat custom ikona
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			        		R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
        		
            /** poziva se kad se ladica skroz zatvori */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // ne znam za�to, ali potrebno
            }

            /** poziva se kad se ladica skroz otvori */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // tako�er, nemam pojma �to je ovo, al potrebno je
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.text, mItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener()); //definiran kao unutarnja klasa, ispod
    }
    
    @Override //nije mi to�no jasno �to ovo radi, al je potrebno
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
         mDrawerToggle.syncState();
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	private class OnInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener{
		
		@Override
		public void onInfoWindowClick(Marker marker) {
			handleMarkerClick(marker);			
		}			
	}	

    
	//unutarnja klasa koja obra�uje klikove na navbar
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    //klikovi na elemente ladice
    private void selectItem(int position) {

    	mDrawerLayout.closeDrawer(mDrawerList); //zatvori ladicu
    	
    	String poruka;

    	//obradi doga�aj
    	switch(position){
    	case 0:
    		nacrtajMarkere(Kat.IZNAD);
    		karta.pomakni(koordinateLokacija[11], 17);
    		poruka = getResources().getString(R.string.toast_ground_checked);
    		Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show();
    		break;
    	case 1:
    		nacrtajMarkere(Kat.PODRUMI);
    		karta.pomakni(koordinatePodruma[6], 18);
    		poruka = getResources().getString(R.string.toast_basements_checked);
    		Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show();
    		break;
    	case 2:
    		karta.promjeniVrstuMape();
    		break;
    	case 3:
    			
    	default:
    		Toast.makeText(this, "odabrana je stavka " + String.valueOf(position)+ " - " + mItems[position], Toast.LENGTH_LONG).show();
    		break;

    	}	    

    }
    
	//klikovi na markere
	private void handleMarkerClick(Marker marker){
		
		Intent namjera = new Intent(this, Opis.class);
		
		namjera.putExtra("markerTitle", marker.getTitle());
				
		startActivity(namjera);
	}
}