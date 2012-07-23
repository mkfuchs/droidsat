package uk.me.chiandh.Sputnik;

public class StarPosition {
	public String name;
	public StringBuffer displayString;
	public double azimuth;
	public double azRadians;
	public double elRadians;
	public double elevation;
	public long positionTime;
	public NamedObject star;

	public StarPosition(String theName, String theDisplayString, 
			double theAzimuth, double theAzRadians, double theElRadians, double theElevation, NamedObject theStar){
		name = theName;
		displayString = new StringBuffer(theDisplayString);
		azimuth = theAzimuth;
		azRadians = theAzRadians;
		elevation = theElevation;
		elRadians = theElRadians;
		star = theStar;
	}
	
	public String toString(){
		return this.displayString.toString();
	}

}
