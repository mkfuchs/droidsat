package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.SDP4;

public class SatellitePosition {
	public String name;
	public StringBuffer displayString;
	public double azimuth;
	public double azRadians;
	public double elRadians;
	public double elevation;
	public double range;
	public Satellite sat;
	
	public SatellitePosition(String theName, String theDisplayString, 
			double theAzimuth, double theAzRadians, double theElRadians, double theElevation, double theRange, Satellite theSatellite){
		name = theName;
		displayString = new StringBuffer(theDisplayString);
		azimuth = theAzimuth;
		azRadians = theAzRadians;
		elevation = theElevation;
		elRadians = theElRadians;
		range = theRange;
		sat = theSatellite;
		
	}
	
	public String toString(){
		return this.displayString.toString();
	}

}
