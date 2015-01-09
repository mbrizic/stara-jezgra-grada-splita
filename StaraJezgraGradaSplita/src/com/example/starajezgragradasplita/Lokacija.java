package com.example.starajezgragradasplita;

class Lokacija {

	private String[] slikaUrl = new String[4];
	private String panorama = "";
	private String naslov;
	private String opis;

	public String getSlikaUrl(int index) {
		return slikaUrl[index];
	}

	public void setSlikaUrl(String slikaUrl, int index) {
		this.slikaUrl[index] = slikaUrl;
	}

	public int getSlikaIndex() {
		int temp = slikaUrl.length;
		return --temp;
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
