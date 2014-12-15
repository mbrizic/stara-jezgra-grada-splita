package com.example.starajezgragradasplita;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mbrizic.starajezgragradasplita.R;

public class Karta{
	
	GoogleMap mapa;
	private Polyline linija;
	private int vrstaMape;
	private int defaultniZoomPalace;
	private float defaultnaTransparentnost;
	private GroundOverlay sloj;
	
	public LatLng koordinateCentraPalace;
	public LatLng koordinateJZRubaSlike;
	public LatLng koordinateSIRubaSlike;
	
	public Karta(){
		
		vrstaMape = GoogleMap.MAP_TYPE_HYBRID; //ina�e mo�e biti i TERRAIN, NORMAL ili SATTELITE;
        koordinateCentraPalace = new LatLng(43.5081322, 16.4410438);
        
        koordinateJZRubaSlike = new LatLng(43.507170, 16.438616);
        koordinateSIRubaSlike = new LatLng(43.509588, 16.441715);
        
        defaultniZoomPalace = 17;
        defaultnaTransparentnost = 0.3f;
        
	}
	
	/* postavlja pocetne vrijednosti mape*/
	public void postavi(){ 
		mapa.setMapType(vrstaMape); 
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setCompassEnabled(true);
        
        pomakni(koordinateCentraPalace, defaultniZoomPalace); 
        
       promjeniSloj(R.drawable.mapa, defaultnaTransparentnost); //mo�e biti i R.drawable.mapa_transp, ili bilo koja nova         
        
	}
	
	public void promjeniSloj(int noviSloj, float transparentnost){
		if (sloj != null)
			sloj.remove();
		
		GroundOverlayOptions mapaPalace = new GroundOverlayOptions()
			.image(BitmapDescriptorFactory.fromResource(noviSloj)) 
			.positionFromBounds(new LatLngBounds(koordinateJZRubaSlike, koordinateSIRubaSlike))
			.transparency(defaultnaTransparentnost);
		
		sloj = mapa.addGroundOverlay(mapaPalace);
	}
	
	public void pomakni(LatLng koordinate, int zoom){
		CameraPosition cameraPosition = new CameraPosition.Builder()
											.target(koordinate)
											.zoom(zoom).build();
		mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	protected void dodajLiniju(LatLng... args){
		linija.remove();
		PolylineOptions opcije = new PolylineOptions().add(args).color(Color.CYAN);
		
		linija = mapa.addPolyline(opcije);
	}
	
	
	protected void dodajMarker(LatLng koordinate, String naslov, String opis){
		MarkerOptions marker = new MarkerOptions()
							.position(koordinate)
							.title(naslov).snippet(opis)
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));		
		mapa.addMarker(marker);
	}
	
	
	

}
