package com.example.starajezgragradasplita;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
	
	//stvari za navigation drawer (koji je nedovršen)
	private String[] mItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    
    private LatLng[] koordinateLokacija;
    private String[] imenaLokacija;
		
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
		inicijalizirajMapu();
        inicijalizirajNavigationDrawer();
        
        koordinateLokacija = new LatLng[]{
        		new LatLng(43.509298, 16.440778), // sj vrata
        		new LatLng(43.509559, 16.439833), // sz kula
        		new LatLng(43.508125, 16.441258), // ist vrata
        		new LatLng(43.507156, 16.440795), // ji kula
        		new LatLng(43.508778, 16.439190), // zap vrata
        		new LatLng(43.507424, 16.439698), // juž vrata
        		new LatLng(43.507926, 16.439969), // vestibul
        		new LatLng(43.508085, 16.440432), // mauzolej
        		new LatLng(43.508288, 16.439656), // jupiterov
        		new LatLng(43.507597, 16.440360), // blagovaonica
        		new LatLng(43.507551, 16.440129), // kriptoportik
        		new LatLng(43.508132, 16.440154), // peristil
        		new LatLng(43.508091, 16.440757) // istoèno od mauzolej        		
        };
        
        imenaLokacija = new String[]{        		
        		"Sjeverna vrata", 
        		"Sjeverozapadna kula",
        		"Istoèna vrata", 
        		"Jugoistoèna kula", 
        		"Zapadna vrata" , 
        		"Južna vrata", 
        		"Vestibul", 
        		"Mauzolej", 
        		"Jupiterov hram", 
        		"Blagovaonica", 
        		"Kriptoportik", 
        		"Peristil", 
        		"Zgrada istoèno od Mauzoleja"
        };
        
        
        nacrtajMarkere(imenaLokacija, koordinateLokacija); //poslije ubaciti i nizeve imenaPodruma i koodinatePodruma

    }
    
    public void nacrtajMarkere(String[] imena, LatLng[] koordinate){
    	String dotakniZaVise = getResources().getString(R.string.tap_for_more);
    	
    	for(int i=0; i<13; i++){
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
    
    private void inicijalizirajMapu() {
    	karta = new Karta();    	
    	
    	//postavljanje svojstva 'mapa' je moguæe izvesti jedino iz ove aktivnosti, jer je vezano za elemente suèelja. Zato nije odvojena u posebnu klasu.
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

    private void inicijalizirajNavigationDrawer(){
    	
    	mItems = getResources().getStringArray(R.array.navigation_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        getActionBar().setDisplayHomeAsUpEnabled(true); //treba dozvolit ove dvije stvari da 
        getActionBar().setHomeButtonEnabled(true);		//se može dodat custom ikona
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			        		R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
        		
            /** poziva se kad se ladica skroz zatvori */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // ne znam zašto, ali potrebno
            }

            /** poziva se kad se ladica skroz otvori */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // takoðer, nemam pojma što je ovo, al potrebno je
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.text, mItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener()); //definiran kao unutarnja klasa, ispod
    }
    
    @Override //nije mi toèno jasno što ovo radi, al je potrebno
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

    
	//unutarnja klasa koja obraðuje klikove na navbar
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

    	//obradi dogaðaj
    	switch(position){
    	case 0:
    		nacrtajMarkere(imenaLokacija, koordinateLokacija);
    		poruka = getResources().getString(R.string.toast_ground_checked);
    		Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show();
    		break;
    	case 1:
    		nacrtajMarkere(imenaLokacija, koordinateLokacija);
    		poruka = getResources().getString(R.string.toast_basements_checked);
    		Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show();
    		break;
    	case 2:
    		karta.promjeniVrstuMape();
    		break;
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