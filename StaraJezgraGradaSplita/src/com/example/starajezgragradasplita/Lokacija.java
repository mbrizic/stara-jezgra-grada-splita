package com.example.starajezgragradasplita;

import java.util.ArrayList;

class Lokacija {

	//private String[] slikaUrl = new String[4];
	private ArrayList<String> slikaUrl = new ArrayList<String>();
	private String panorama = "";
	private String naslov;
	private String opis;

	public String getSlikaUrl(int index) {
		//return slikaUrl[index];
		return slikaUrl.get(index);
	}

	public void setSlikaUrl(String slikaUrl, int index) {
		//this.slikaUrl[index] = slikaUrl;
		this.slikaUrl.add(index, slikaUrl);
	}

	public int getSlikaIndex() {
		//int temp = slikaUrl.length;
		//return --temp;
		int temp = slikaUrl.size();
		return --temp;
	}
	
	public boolean isSlikaUrlEmpty() {
		if(this.slikaUrl.isEmpty())
			return true;
		else
			return false;
	}

	public String getPanorama() {
		return panorama;
	}

	public void setPanorama(String panorama) {
		this.panorama = panorama;
	}
	
	public boolean isPanoramaEmpty() {
		if(this.panorama.equals(""))
			return true;
		else
			return false;
	}
	
	public int getPanoramSize() {
		if(this.panorama.equals(""))
			return 0;
		else
			return 1;
	}

	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}
}
