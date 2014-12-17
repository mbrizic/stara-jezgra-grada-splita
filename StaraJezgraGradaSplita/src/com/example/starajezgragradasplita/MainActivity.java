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
		
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
		inicijalizirajMapu();
        inicijalizirajNavigationDrawer();
		
		//Testni markeri
		karta.dodajMarker(karta.koordinateCentraPalace, "Prvi marker", "dotakni tu za više");
		karta.dodajMarker(karta.koordinateSIRubaSlike, "Drugi marker", "dotakni tu za više");
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
            
        	
            if (karta.mapa == null) 
            	Toast.makeText(this, "Nešto je pošlo po krivu", Toast.LENGTH_LONG).show();            
    	}    	
    }

    private void inicijalizirajNavigationDrawer(){
    	
    	mItems = new String[]{"Obièna mapa", "Transparentna", "Promjeni podlogu", "test1"}; //inaèe koristit getResources().getStringArray(R.array.navbar);
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

    	//obradi dogaðaj
    	switch(position){
    	case 0:
    		karta.promjeniSloj(R.drawable.mapa, 0.3f);
    		Toast.makeText(this, "Postavljen obièan sloj", Toast.LENGTH_SHORT).show();
    		break;
    	case 1:
    		karta.promjeniSloj(R.drawable.mapa_transp, 0.0f);
    		Toast.makeText(this, "Postavljen transparentan sloj", Toast.LENGTH_SHORT).show();
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
		
		namjera.putExtra("markerId", marker.getId().substring(1));
		namjera.putExtra("markerTitle", marker.getTitle());
				
		startActivity(namjera);
	}
}