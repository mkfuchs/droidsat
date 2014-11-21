
package uk.me.chiandh.Sputnik;

import java.io.*;
import uk.me.chiandh.Lib.Hmelib;
import uk.me.chiandh.Lib.HmelibException;

/**
<p>The <code>Station</code> class represents an observatory (a location on
the Earth's surface) that has a clock.</p>

<p>Copyright: &copy; 2002-2008 Horst Meyerdierks.</p>

<p>This programme is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public Licence as
published by the Free Software Foundation; either version 2 of
the Licence, or (at your option) any later version.</p>

<p>This programme is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public Licence for more details.</p>

<p>You should have received a copy of the GNU General Public Licence
along with this programme; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.</p>

<dl>
<dt><strong>2002-06-15:</strong> hme</dt>
<dd>Translated from C (Sputnik 1.9).</dd>
<dt><strong>2002-06-21:</strong> hme</dt>
<dd>GetXYZ() and GetLat().</dd>
<dt><strong>2002-06-22:</strong> hme</dt>
<dd>CommandShow() moved to Telescope class.</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Consolidate documentation.</dd>
<dt><strong>2003-04-21:</strong> hme</dt>
<dd>Change default to Royal Observatory Edinburgh.</dd>
<dt><strong>2003-06-15:</strong> hme</dt>
<dd>Change default to Edinburgh (the city).</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Add the Copy() method and the Get{Name|Long|Height}() methods it
  requires.</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Changed method name to GetX0Z.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2004-02-02:</strong> hme</dt>
<dd>Added ReadByName.</dd>
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>Port to Sputnik3.  Change ShowToFile() to be Show() and to return a
  string instead of writing to a given stream.</dd>
<dt><strong>2008-08-06:</strong> hme</dt>
<dd>Fix one instance in Show() where a new-line was missing.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see uk.me.chiandh.Lib.Hmelib
 */

public class Station extends Times
{

  /**
   * Earth's equatorial radius in Gm. */

  protected static final double A = 0.006378140;


  /**
   * Earth's polar radius in Gm. */

  protected static final double B = 0.006356775;


  /**
   * A measure of Earth's flattening.
   *
   * <p>B = (1 - 1/F) A */

  protected static final double F = 298.257;


  /**
   * The name of the observatory.
   *
   * <p>This is not only for information, but the name given by the user is
   * also used to look up the observatory location in a file that contains a
   * list of observatories. */

  protected String itsName;


  /**
   * Geographic longitude in rad.
   *
   * <p>Positive to the East. */

  protected double itsLong;


  /**
   * Geodetic (geographic) latitude in rad.
   *
   * <p>This is the elevation of the North Celestial Pole. */

  protected double itsLat;


  /**
   * Height above sea level in Gm.
   *
   * <p>This is the height above the ellipsoidal geoid. */

  protected double itsHeight;


  /**
   * Distance from Earth's axis in Gm.
   *
   * <p>This is the projection onto the equatorial plane of the vector from the
   * geocentre to the station. */

  protected double itsX;


  /**
   * Distance from Earth's equatorial plane in Gm.
   *
   * <p>This is the projection onto the rotation axis of the vector from the
   * geocentre to the station. */

  protected double itsZ;


  /**
   * Serve the <code>station/read</code> command.
   *
   * <p>This reads the parameters of the observatory location from a file.  The
   * command line includes both the file name and the name of the station to
   * look for in the file.  The file with a station list looks somewhat like
   * this:
   *
<pre>
# List of observatories, one per line.
#   East longitude [deg],
#   north latitude [deg],
#   altitude above geoid [m],
#   name (in a pair of double quotes).
 +6.723    +50.570     435 "Stockert"
 +6.885    +50.527     369 "Effelsberg"
 -3.183    +55.925     146 "Royal Observatory Edinburgh"
 -0.528    +44.835      73 "Floirac"
</pre>
   *
   * <p>When it is read, the station names in the double quotes are looked for.
   * The line that makes a match with the requested station will then be
   * scanned for the three numbers at the line's beginning.  The numbers are
   * longitude and latitude in degrees and height above sea level in metres.
   *
   * @param aCommand
   *   The command line, including the parameters to be read. */

  public void CommandRead(String aCommand)
    throws StationNotFoundException, HmelibException, IOException
  {
    String  theString;
    String  theFileName;
    String  theName;

    /* Trim off the command itself and read the file name. */

    theString = aCommand.substring(13);
    theFileName = Hmelib.Rstring(theString);

    /* Trim off the file name and read the station name. */

    theString = Hmelib.Sstring(theString);
    theName   = Hmelib.Rstring(theString);

    /* Delegate the work to another method. */

    ReadByName(theFileName, theName);

    return;
  }


  /**
   * Read Station from file.
   *
   * <p>This reads the parameters of the observatory location from a file.
   * The file with a station list looks somewhat like this:
   *
<pre>
# List of observatories, one per line.
#   East longitude [deg],
#   north latitude [deg],
#   altitude above geoid [m],
#   name (in a pair of double quotes).
 +6.723    +50.570     435 "Stockert"
 +6.885    +50.527     369 "Effelsberg"
 -3.183    +55.925     146 "Royal Observatory Edinburgh"
 -0.528    +44.835      73 "Floirac"
</pre>
   *
   * <p>When it is read, the station names in the double quotes are looked for.
   * The line that makes a match with the requested station will then be
   * scanned for the three numbers at the line's beginning.  The numbers are
   * longitude and latitude in degrees and height above sea level in metres.
   *
   * @param aFileName
   *   The name of the file with the station data.
   * @param aName
   *   The name of the observatory to be looked for in the file. */

  public void ReadByName(String aFileName, String aName)
    throws StationNotFoundException, HmelibException, IOException
  {
    BufferedReader theFile;
    String  theString;
    double  theLong;
    double  theLat;
    double  theHeight;
    boolean success = false;

    /* Open the file named in the first word. */

    theFile = new BufferedReader(new FileReader(aFileName));

    /* Loop through file. */

    for (;;) {

      /* Read line.  An EOF is
       * signalled by returning null.  In that case we break the loop. */

      if ((theString = theFile.readLine()) == null) break;

      /* Trim white space off the start and end.
       * If we have a comment, ignore it. */

      theString = theString.trim();
      if (theString.startsWith("#")) {continue;}

      /* Read the three numbers and the name.
       * We skip the numbers as if they were words separated by blanks. */

      theLong   = Hmelib.Rfndm(theString);
      theString = Hmelib.Sstring(theString);
      theLat    = Hmelib.Rfndm(theString);
      theString = Hmelib.Sstring(theString);
      theHeight = Hmelib.Rfndm(theString);
      theString = Hmelib.Sstring(theString);
      theString = Hmelib.Rstring(theString);

      /* If the station names match, set the instance and break out. */

      if (aName.equals(theString)) {
	SetGeodetic(aName,
	  theLong / Hmelib.DEGPERRAD,
	  theLat  / Hmelib.DEGPERRAD,
	  theHeight / 1E9);
        success = true;
	break;
      }
    }

    theFile.close();
    if (!success) {
      theFile.close();
      throw new StationNotFoundException("no such station");
    }

    return;
  }


  /**
   * Copy the state of a Station instance from another.
   *
   * <p>Invoke this method for the new instance of Station and pass as argument
   * an existing Station instance from which to copy the state.
   * The new instance must have been initialised with Init() before making
   * this call.
   *
   * @param aStation
   *   The time and location to be copied into this instance. */

  public void Copy(Station aStation) {
    super.Copy(aStation);
    SetGeodetic(aStation.GetName(),
      aStation.GetLong(), aStation.GetLat(), aStation.GetHeight());
    return;
  }


  /**
   * Return the station elevation above sea level.
   *
   * <p>This gives access to the stored field itsHeight (in Gm). */

  public final double GetHeight() {return itsHeight;}


  /**
   * Return the station geodetic (geographic) latitude.
   *
   * <p>This gives access to the stored field itsLat (in radian). */

  public final double GetLat() {return itsLat;}


  /**
   * Return the station geographic longitude.
   *
   * <p>This gives access to the stored field itsLong (in radian). */

  public final double GetLong() {return itsLong;}


  /**
   * Return local sidereal time.
   *
   * <p>This returns the local sidereal time (sidereal time since previous
   * culmination of the mean equinox at the observatory) in hours.  LST is the
   * right ascension of the station, which is of course set off from Greenwich
   * by its geographic longitude.  Hence:
   *
   * <p>LST = GST + &lambda; */

  public final double GetLST()
  {
    double theRetval;

    theRetval  = GetGST();
    theRetval += itsLong * 12. / Math.PI;
    while ( 0. >  theRetval) theRetval += 24.;
    while (24. <= theRetval) theRetval -= 24.;

    return theRetval;
  }


  /**
   * Return local sidereal time as triplet.
   *
   * <p>This returns the local sidereal time (sidereal time since previous
   * culmination of the mean equinox at the observatory) as a
   * triplet of hours (0-23), minutes (0-59) and seconds (0-60.0).
   * See {@link #GetLST GetLST} for a definition of LST.
   *
   * @param aTriplet
   *   The returned triplet of hours, minutes and seconds. */

  public final void GetLSThms(double aTriplet[])
  {
    double theTime;
    theTime = GetLST();
    Hmelib.deg2dms(theTime, aTriplet);
    return;
  }


  /**
   * Return the station name.
   *
   * <p>This gives access to the stored field itsName. */

  public final String GetName() {return itsName;}


  /**
   * Return the station rectangular position.
   *
   * <p>This gives access to the stored fields itsX and itsZ.  The second
   * returned number is always zero, as here the x axis is in the meridian of
   * the Station, not of Greenwich.  The numbers are in Gm.
   *
   * @param aTriplet
   *   The geocentric rectangular coordinates in the coordinate system where
   *   the x-z plane is the plane of the observatory's meridian.  The second
   *   number is therefore always returned as zero. */

  public final void GetX0Z(double aTriplet[])
  {
    aTriplet[0] = itsX; aTriplet[1] = 0.; aTriplet[2] = itsZ;
    return;
  }


  /**
   * Initialise the object.
   *
   * <p>This initialises the Times part, then sets the location to
   * Edinburgh. */

  public void Init() {
    super.Init();
    SetGeodetic("Edinburgh",
		 -3.217 / Hmelib.DEGPERRAD,
		+55.950 / Hmelib.DEGPERRAD,
		 50.    / 1E9);
    return;
  }


  /**
   * Set the station parameters.
   *
   * <p>Given the longitude and latitude in radian and the height in Gm, this
   * will store the numbers in the class instance and calculate the remaining
   * station parameters accordingly.  The longitude is normalised to the
   * interval [-&pi;,+&pi;].
   *
   * <p><img src="figs/Station1.png" alt="Meridian cut">
   * <p><em>Cut through the ellipsoidal geoid along
   *   the meridian of the station.</em>
   *
   * <p>The meridian through the station is an ellipse marking the sea level,
   * or the surface of the geoid.  The radii of the geoid are a at the equator
   * and b at the pole.  The equation of the ellipse is
   *
   * <p>(x<sup>2</sup> / a<sup>2</sup>) + (z<sup>2</sup> / b<sup>2</sup>) = 1
   *
   * <p>The horizon is the tangential plane to the geoid.  Since the geoid is
   * an equi-potential surface, the normal to the horizon is defined by the
   * direction of the gravi-centrifugal force.  Hence the geodetic latitude
   * can be defined as the elevation of the north celestial pole above the
   * horizon.  The equation of the horizon is
   * (Helmut Sieber, 1973, <I>Mathematische Begriffe und Formeln</I>, Klett, Stuttgart)
   *
   * <p>(x x<sub>0</sub> / a<sup>2</sup>)
   *  + (z z<sub>0</sub> /b<sup>2</sup>) = 1
   *
   * <p>Considering the slope of the horizon we find
   *
   * <p>cot(&phi;) = &plusmn;
   *   (b<sup>2</sup> / a<sup>2</sup>) (x<sub>0</sub> / z<sub>0</sub>)
   *
   * <p>With some arithmetics these two equations result in expressions of the
   * coordinates as function of the geodetic latitude.  The effect of the
   * station's altitude above the geoid is simple to calculate from the
   * geodetic latitude and this all combines to
   *
   * <p>x<sub>1</sub> = h cos(&phi;)
   *   + a [1 - [1 / {1 + (a/b)<sup>2</sup>
   *   cot<sup>2</sup>(&phi;)}]]<sup>0.5</sup>
   * <p>z<sub>1</sub> = h sin(&phi;) &plusmn;
   *   b [1 / {1 + (a/b)<sup>2</sup> cot<sup>2</sup>(&phi;)}]<sup>0.5</sup>
   * <p>&phi;' = arctan(z<sub>1</sub>/x<sub>1</sub>)
   * <p>r = (x<sub>1</sub><sup>2</sup>
   *     + z<sub>1</sub><sup>2</sup>)<sup>0.5</sup>
   *
   * <p>To give an idea of the magnitude of the difference between geodetic and
   * geocentric latitude, at 56&#176; the geocentric latitude is smaller by
   * 0.18&#176;.
   *
   * @param aName
   *   The name of the observatory.
   * @param aLong
   *   The geographic longitude in rad.
   * @param aLat
   *   The geodetic latitude in rad.
   * @param aHeight
   *   The elevation above sea level in Gm. */

  public final void SetGeodetic(String aName,
    double aLong, double aLat, double aHeight)
  {
    double theFactor;

    /* Copy the parameters into fields, normalise longitude. */

    itsName   = aName;
    itsLong   = Hmelib.NormAngle0(aLong);
    itsLat    = aLat;
    itsHeight = aHeight;

    /* Work out the geocentric description for latitude and height.
     * Above we defined F = 1/f, which we use instead of
     * (a/b)^2 = [F/(F-1)]^2.  The equations in the documentation don't work
     * on the poles or on the equator, where life is in fact simpler. */

    if      (itsLat <= -Math.PI/2.) {itsX = 0.; itsZ = +B;}
    else if (itsLat >= +Math.PI/2.) {itsX = 0.; itsZ = -B;}
    else if (1E-6 > Math.abs(itsLat)) {
      itsX = A * Math.cos(itsLat);
      itsZ = A * Math.sin(itsLat);
    }
    else if (0. < itsLat) {
      theFactor  = F / (F - 1.);
      theFactor /= Math.tan(itsLat);
      theFactor *= theFactor;
      itsX = itsHeight * Math.cos(itsLat)
	   + A * Math.sqrt(1. - 1. / (1. + theFactor));
      itsZ = itsHeight * Math.sin(itsLat)
	   + B * Math.sqrt(1. / (1. + theFactor));
    }
    else {
      theFactor  = F / (F - 1.);
      theFactor /= Math.tan(itsLat);
      theFactor *= theFactor;
      itsX = itsHeight * Math.cos(itsLat)
	   + A * Math.sqrt(1. - 1. / (1. + theFactor));
      itsZ = itsHeight * Math.sin(itsLat)
	   - B * Math.sqrt(1. / (1. + theFactor));
    }
  }


  /**
   * Set the station rectangular position.
   *
   * <p>Given the rectangular coordinates in Gm this fills in the instance
   * correctly.  The x axis points to the Gulf of Guinea (longitude and
   * latitude zero), the y axis to the Indian Ocean and the z axis to the
   * North Pole.
   *
   * <p><img src="figs/Station1.png" alt="Meridian cut">
   * <p><em>Cut through the ellipsoidal geoid along
   *   the meridian of the station.</em>
   *
   * <p>To convert from geocentric latitude &phi;'
   * to geodetic (geographic) latitude &phi; we use the iteration of
   * (USNO/RGO, 1990, <I>The Astronomical Almanach for the Year 1992</I>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.K12).
   * The starting value is the geocentric latitude.  In the iteration the
   * distance r from the axis and the eccentricity e are used.
   *
   * <p>r = (x<sub>1</sub><sup>2</sup>
   *      + z<sub>1</sub><sup>2</sup>)<sup>0.5</sup>
   * <p>e<sup>2</sup> = 1 - (b/a)<sup>2</sup> = 2 f - f<sup>2</sup>
   * <p>&phi;(1) = &phi;'
   * <p>C(i) = [1 / {1 - e<sup>2</sup>
   *   sin<sup>2</sup>(&phi;(i))}<sup>0.5</sup>]
   * <p>&phi;(i+1) = arctan[(z + a C(i) e<sup>2</sup> sin(&phi;(i))) / r]
   * <p>h = [r / cos(&phi;(&infin;))] - a C(&infin;)
   *
   * @param aName
   *   The name of the observatory.
   * @param aTriplet
   *   The geocentric rectangular coordinates in the coordinate system where
   *   the x axis points to the Gulf of Guinea. */

  public final void SetXYZ(String aName, double aTriplet[])
  {
    double theEsquare;
    double theOldLat;
    double theC;
    double theTriplet[] = new double[3];

    /* Copy given name. */

    itsName = aName;

    /* Prepare iteration. */

    theEsquare = 2. / F - 1. / (F * F);
    Hmelib.Spher(aTriplet, theTriplet);
    itsLong = Hmelib.NormAngle0(theTriplet[0]);
    itsLat  = theTriplet[1];
    itsX = Math.sqrt(aTriplet[0] * aTriplet[0] + aTriplet[1] * aTriplet[1]);
    itsZ = aTriplet[2];

    /* Iterate. */

    for (;;) {
      theOldLat = itsLat;
      theC = 1. / Math.sqrt(1.
	   - theEsquare * Math.sin(theOldLat) * Math.sin(theOldLat));
      itsLat = Math.atan((itsZ + A * theC * theEsquare * Math.sin(theOldLat))
             / itsX);
      if (1E-7 > Math.abs(itsLat - theOldLat)) break;
    }

    /* Altitude. */

    itsHeight = itsX / Math.cos(itsLat) - A * theC;
  }


  /**
Display the position and time in various representations.

<p>This returns information about the currently stored observatory position
and time.  The format is</p>

<pre>
Observatory: Royal Observatory Edinburgh
   East long.   -3.182500 deg
   Latitude     55.925000 deg
   Altitude           146 m

  UT: 2003-04-21T19:15:33.1 (JD  2452751.302467)
  TT: 2003-04-21T19:16:38.9 (JDE 2452751.303228)
  Ep: 2003.302678243
             GST 09:13:21.0 = 138.337426 deg
             LST 09:00:37.2 = 135.154926 deg
</pre>
   */

  public final String Show()
  {
    String theOutput = "";
    double theLST[] = new double[3];

    theOutput = "\nObservatory: " + itsName + "\n   East long. "
      + Hmelib.Wfndm(11, 6, itsLong * Hmelib.DEGPERRAD)
      + " deg\n   Latitude    "
      + Hmelib.Wfndm(10, 6, itsLat * Hmelib.DEGPERRAD)
      + " deg\n   Altitude          "
      + Hmelib.Wfndm(4, 0, itsHeight * 1E9) + " m\n\n"
      + super.Show();

    GetLSThms(theLST);
    theOutput = theOutput + "             LST "
      + Hmelib.WTime2(theLST[0], theLST[1], theLST[2]) + " = "
      + Hmelib.Wfndm(10, 6,
		     (theLST[0] + (theLST[1] + theLST[2] / 60.) / 60.) *15.)
      + " deg\n\n";

    return theOutput;
  }

}
