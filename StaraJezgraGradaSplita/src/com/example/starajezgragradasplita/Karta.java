package com.example.starajezgragradasplita;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Karta{
	
	GoogleMap mapa;
	private Polyline linija;
	private int vrsta;
	
	public Karta(){ //konstruktor, zasad prazan
		
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
	
	protected void prikaziMeNaMapi(){
		
		
	}

}
