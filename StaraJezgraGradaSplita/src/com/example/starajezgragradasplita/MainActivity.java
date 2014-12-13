package com.example.starajezgragradasplita;

import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mbrizic.starajezgragradasplita.R;


public class MainActivity extends ActionBarActivity {

	private Karta karta;
	private final int vrstaMape = GoogleMap.MAP_TYPE_HYBRID; //inaèe može biti i TERRAIN, NORMAL ili SATTELITE;
	private int zoomPalace = 17;
	private LatLng koordinatePalace; //postavljeno u onCreate
	
	//stvari za navigation drawer (koji je nedovršen)
	private String[] mItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
		
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        koordinatePalace = new LatLng(43.5081322, 16.4410438);
        
        inicijalizirajNavigationDrawer();
		inicijalizirajMapu();
		
		karta.dodajMarker(koordinatePalace, "Naslov markera", "tu se nalazi ovo i ono... ");
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true; //otvori izbornik klikom na ikonu aplikacije
        }
        
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void inicijalizirajMapu() {
    	karta = new Karta();    	
    	
    	//postavljanje svojstva 'mapa' je moguæe izvesti jedino iz ove aktivnosti, jer je vezano za elemente suèelja. Zato nije odvojena u posebnu klasu.
    	if (karta.mapa == null) { 
        	karta.mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
 
            karta.mapa.setMapType(vrstaMape); 
            karta.pomakni(koordinatePalace, zoomPalace);            
            
            if (karta.mapa == null) //ako nije dobro postavljena
                Toast.makeText(this, "Nešto je pošlo po krivu", Toast.LENGTH_LONG).show();            
    	}    	
    }

    private void inicijalizirajNavigationDrawer(){
    	
    	mItems = new String[]{"jedan", "dva", "tri"}; //inaèe koristit getResources().getStringArray(R.array.navbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        getActionBar().setDisplayHomeAsUpEnabled(true); //treba dozvolit ove dvije stvari da 
        getActionBar().setHomeButtonEnabled(true);		//se može dodat custom ikona
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
			        		R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
        		
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle("Stara jezgra grada Splita");
                invalidateOptionsMenu(); // ne znam zašto, ali potrebno
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle("Opcije");
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

   
    
//unutarnja klasa koja obraðuje klikove na navbar
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
		//klikovi na elemente ladice
		private void selectItem(int position) {
		
		    // Oznaèi odabrani element, postavi naslov i zatvori ladicu
		    mDrawerList.setItemChecked(position, true);
		    setTitle(mItems[position]);
		    mDrawerLayout.closeDrawer(mDrawerList);
		    
		    //tu ispod možeš dodati da otvara novi activity
		}
}