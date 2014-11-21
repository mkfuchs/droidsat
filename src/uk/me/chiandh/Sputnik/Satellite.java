
package uk.me.chiandh.Sputnik;

import java.io.*;
import java.util.ArrayList;
import uk.me.chiandh.Lib.SDP4;
import uk.me.chiandh.Lib.SDP4Exception;
import uk.me.chiandh.Lib.Hmelib;
import uk.me.chiandh.Sputnik.SatellitePosition;

/**
<p>The <code>Satellite</code> class extends the
{@link NamedObject NamedObject} class such that it stores not
only one position and a name, but also owns an instance of
{@link uk.me.chiandh.Lib.SDP4 SDP4} so that it can calculate ephemeris of an
artificial Earth satellite.</p>

<p>Copyright: &copy; 2003-2006 Horst Meyerdierks.</p>

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
<dt><strong>2003-03-09:</strong> hme</dt>
<dd>New class.</dd>
<dt><strong>2003-04-06:</strong> hme</dt>
<dd>Add visibility/eclipse.<br>
  Also add the reflection angle routine by Randy John.  This should
  probably be the basis of a subclass Iridium rather than be in this
  class.</dd>
<dt><strong>2003-04-06:</strong> hme</dt>
<dd>Add output of Iridium angles (for any satellite).</dd>
<dt><strong>2003-09-14:</strong> hme</dt>
<dd>Add ShowAllToFile to do the leg work for the satellite/all command.</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Add ShowFlare to do support the iridium/flare command.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2004-02-01:</strong> hme</dt>
<dd>Add ShowPass to support the satellite/pass command.
  Also change ShowFlare to return an indication of whether it wrote a
  line.</dd>
<dt><strong>2004-02-03:</strong> hme</dt>
<dd>Add TestFlare to delegate ShowFlare calculation.</dd>
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>Port to Sputnik3.
  Change ShowAllToFile() to be ShowAll() and to return a
  string instead of writing to a given stream.
  Change ShowToFile() to be Show() and to return a
  string instead of writing to a given stream.
  Change ShowPass(), ShowFlare()
  to return a string (or null pointer) instead of writing
  to a stream.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.SDP4
@see uk.me.chiandh.Lib.Hmelib
 */


public class Satellite extends NamedObject
{

  /**
   * The J2000 velocity vector. */

  protected double itsV[];

  /**
   * Whether the satellite is sunlit. */

  public int itsIsSunlit;

  /**
   * The instance of the SDP4 class that is used to calculate the ephemeris. */

  protected SDP4 itsSDP4;


  /**
   * Initialise the Satellite object.
   *
   * <p>This sets the object to be a point 0.01&nbsp;Gm from Earth in the
   * direction of the J2000 vernal equinox.  The velocity is set zero. */

  public void Init()
  {
    final double t[] = {0.01, 0., 0.};

    super.Init();
    itsName = "Unspecified Satellite object";
    SetJ2000(0, t);

    itsV = new double[3 * itsNpos];
    itsV[0] = 0.; itsV[1] = 0.; itsV[2] = 0.;
    itsIsSunlit = 0;

    itsSDP4 = new SDP4();
    itsSDP4.Init();
  }

static double theSpherShowAllSats[] = {0.,0.,0.};
public static final ArrayList<SatellitePosition> showAllSats(InputStream is,
			Telescope aTelescope, ArrayList<SatellitePosition> satellitePositions) {
		//ArrayList<SatellitePosition>  = new ArrayList<SatellitePosition>();
	

    
    

    if (is !=null){
    	BufferedReader theFile = new BufferedReader(new InputStreamReader(is),65535);


    	for (;;) {

    		
    		Satellite sat = new Satellite();
    		sat.Init();
    		try {sat.itsSDP4.NoradNext(theFile);}
    		catch(Exception e) {break;}
    		sat.itsName = sat.itsSDP4.itsName;
    		sat.Update(aTelescope);
    		sat.GetHori(0, aTelescope, theSpherShowAllSats);
    		
    		String theOutput = sat.itsName 
    		+ Hmelib.Wfndm(4, 0, theSpherShowAllSats[0] * Hmelib.DEGPERRAD)
    		+ Hmelib.Wfndm(5, 0, theSpherShowAllSats[1] * Hmelib.DEGPERRAD)
    		+ Hmelib.Wfndm(8, 0, theSpherShowAllSats[2] * 1E6);


    		SatellitePosition satPosn = 
    			new SatellitePosition(sat.itsName, theOutput, 
    					theSpherShowAllSats[0] * Hmelib.DEGPERRAD, 
    					theSpherShowAllSats[0],
    					theSpherShowAllSats[1] * Hmelib.DEGPERRAD, 
    					theSpherShowAllSats[1],
    					theSpherShowAllSats[2] * 1E6,
    					sat);
    		satellitePositions.add(satPosn);
    		

    	}

    	try {
    		theFile.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    }
    else{
    	//iterate through the arraylist, get name and update
			for (SatellitePosition satPosn : satellitePositions) {
				synchronized (satPosn) {
					satPosn.sat.Update(aTelescope);
					satPosn.sat.GetHori(0, aTelescope, theSpherShowAllSats);
					String theOutput = satPosn.sat.itsName
							+ Hmelib.Wfndm(4, 0, theSpherShowAllSats[0]
									* Hmelib.DEGPERRAD)
							+ Hmelib.Wfndm(5, 0, theSpherShowAllSats[1]
									* Hmelib.DEGPERRAD)
							+ Hmelib.Wfndm(8, 0, theSpherShowAllSats[2] * 1E6);
					satPosn.displayString.replace(0, satPosn.displayString
							.length(), theOutput);
					satPosn.azimuth = theSpherShowAllSats[0] * Hmelib.DEGPERRAD;
					satPosn.azRadians = theSpherShowAllSats[0];
					satPosn.elevation = theSpherShowAllSats[1]
							* Hmelib.DEGPERRAD;
					satPosn.elRadians = theSpherShowAllSats[1];
					satPosn.range = theSpherShowAllSats[2] * 1E6;
				}
    	}
    	
    }
	
	
	return satellitePositions;
	

}

  /**
Display all satellites from a given file.

<p>This opens the given file, reads in each satellite in turn, works out
where it is at the given time (and whether it is lit by the given Sun).
The azimuth, elevation, distance and sunlit status are returned as a table.<p>

<p>Only satellites with positive elevation are included.  The output
format is:</p>
   *
<pre>
Observatory: Edinburgh
   East long.   -3.217000 deg
   Latitude     55.950000 deg
   Altitude             0 m

  UT: 2003-09-08T21:11:00.0 (JD  2452891.382639)
  TT: 2003-09-08T21:12:06.0 (JDE 2452891.383403)
  Ep: 2003.686196860
             GST 20:21:04.6 = 305.269121 deg
             LST 20:08:12.5 = 302.052121 deg

Satellites from file: /home/hme/lib/sats/iridium.txt

      A        h        r    sun  Name
     deg      deg      km    lit
  --------  -------  ------  ---  ----------------------
   200.384    3.615    2851  yes  IRIDIUM 914
   211.951   43.517    1077  yes  IRIDIUM 16
   194.928   40.399    1105  yes  IRIDIUM 911
   322.443    9.581    2374  yes  IRIDIUM 37
    44.444   12.416    2172  yes  IRIDIUM 41
    66.612    7.525    2535  yes  IRIDIUM 66
    86.910   13.731    1944   no  IRIDIUM 77
   198.773   11.924    2053  yes  IRIDIUM 86
     5.287    0.154    3256  yes  IRIDIUM 83
</pre>

<p>This method calls the method of the given Station instance to make the
output for Station and only adds the table of satellites.  The coordinate
transforms are done similar to the parent class NamedObject's ShowToFile
method.</p>

@param aFileName
  This is the name of the input file that contains the NORAD TLE
  information for all the satellites we are interested in.
@param aTelescope
  Some of the coordinate transforms require the time or the location of
  the observatory to be known.  This contains that information.  It also
  contains an instance of the Sun class that has been updated to the same
  time.  The spatial velocity of the Sun is
  obtained from this in order to reduce the radial velocity of the
  observatory from geocentric to heliocentric.
 */

  public final String ShowAll(String aFileName, Telescope aTelescope)
    throws IOException
  {
    String theOutput  = "";
    double theSpher[] = new double[3];
    BufferedReader theFile;

    /* Open the input file. */

    theFile = new BufferedReader(new FileReader(aFileName));

    /* Get the station to show itself. */

    theOutput = aTelescope.Show();

    /* Write the table header. */

    theOutput = theOutput
      + "Satellites from file: " + aFileName + "\n\n"
      + "    A        h        r    sun  Name\n"
      + "   deg      deg      km    lit\n"
      + "--------  -------  ------  ---  ----------------------\n";

    /* Loop through the input file. */

    for (;;) {

      /* Read next satellite from file.
       * NoradNext needs to raise an exception if a partial or no further entry
       * is found.  Also if an entry is invalid.  When we encounter an
       * exception we catch and ignore it, but break the loop. */

      try {itsSDP4.NoradNext(theFile);}
      catch(Exception e) {break;}
      itsName = itsSDP4.itsName;

      /* Update(aStation, aSun); */

      Update(aTelescope);

      /* Extract the position and convert it to horizontal. */

      GetHori(0, aTelescope, theSpher);

      /* Write the name, position and sunlit status to the output stream. */

      //if (0. < theSpher[1]) {
      if (true){
	theOutput = theOutput
	  + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
	  + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD)
	  + Hmelib.Wfndm(8, 0, theSpher[2] * 1E6);
	if (0 == itsIsSunlit) {
	  theOutput = theOutput + "   no  ";
	}
	else {
	  theOutput = theOutput + "  yes  ";
	}
	theOutput = theOutput + itsName + "\n";
      }
    }

    /* Add a blank line and close the file. */

    theOutput = theOutput + "\n";
    theFile.close();

    return theOutput;
  }


  /**
Show a Satellite if it is passing.

<p>This returns one line with UT and horizontal position
if the elevation is positive, the satellite is sunlit and
the the Sun is below -6&deg; elevation.  The return value is null
otherwise.</p>

<pre>
2004-01-26T18:16:00.0   228.7    2.7    1924.2 
</pre>

@param aTelescope
  This supplies the time for ephemeris, station for coordinate transform,
  and solar position for sunlit status and solar position.
   */

  public final String ShowPass(Telescope aTelescope)
  {
    String theOutput    = "";
    double theTriplet[] = new double[3];
    double theSun[]     = new double[3];
    double theDate[]    = new double[3];
    double theTime[]    = new double[3];
    int    val;

    /* Update to current time and Sun, get horizontal coordinates. */

    Update(aTelescope);

    /* If satellite is sunlit */

    val = 0;
    if (0 != itsIsSunlit) {

      /* If satellite is above horizon. */

      GetHori(0, aTelescope, theTriplet);
      if (0. < theTriplet[1]) {

	/* If Sun is below civil twilight threshold. */

	aTelescope.itsSun.GetHori(0, aTelescope, theSun);
	if (RISECIVIL > theSun[1]) {
	  val = 1;
	  aTelescope.GetDate(theDate); aTelescope.GetUThms(theTime);
	  theOutput = Hmelib.WTime3(theDate[0], theDate[1], theDate[2],
				    theTime[0], theTime[1], theTime[2])
	    + Hmelib.Wfndm( 8, 1, theTriplet[0] * Hmelib.DEGPERRAD)
	    + Hmelib.Wfndm( 7, 1, theTriplet[1] * Hmelib.DEGPERRAD)
	    + Hmelib.Wfndm(10, 1, theTriplet[2] * 1E6) + "\n";
	}
      }
    }

    if (1 == val) return theOutput;
    else          return null;
  }


  /**
Show an Iridium satellite if it flares.

<p>This returns one line with UT, horizontal position and smallest
Iridium angle if the elevation is positive, the satellite is sunlit and
the smallest Iridium angle is below 2&deg;.  Otherwise the return value is
null.</p>

<pre>
2003-09-15T20:43:30.0   357.704   15.800    1964  2.0  IRIDIUM 16
</pre>

@param aTelescope
  This supplies the time for ephemeris, station for coordinate transform,
  and solar position for sunlit status and Iridium angles.
   */

  public final String ShowFlare(Telescope aTelescope)
  {
    String theOutput    = "";
    double theTriplet[] = new double[3];
    double theDate[]    = new double[3];
    double theTime[]    = new double[3];
    double t;
    int    val;

    val = 0;
    t   = TestFlare(aTelescope);

    if (2. >= t) {
      val = 1;
      GetHori(0, aTelescope, theTriplet);
      aTelescope.GetDate(theDate); aTelescope.GetUThms(theTime);
      theOutput = Hmelib.WTime3(theDate[0], theDate[1], theDate[2],
				theTime[0], theTime[1], theTime[2])
        + Hmelib.Wfndm(10, 3, theTriplet[0] * Hmelib.DEGPERRAD)
        + Hmelib.Wfndm( 9, 3, theTriplet[1] * Hmelib.DEGPERRAD)
        + Hmelib.Wfndm( 8, 0, theTriplet[2] * 1E6)
        + Hmelib.Wfndm( 5, 1, t) + "  " + itsName + "\n";
    }

    if (1 == val) return theOutput;
    else          return null;
  }


  /**
   * Test an Iridium satellite whether it flares.
   *
   * <p>This tests whether
   * the smallest Iridium angle is below 2&deg;.  The return value is that
   * smallest Iridium angle, or 3 if no flare is in progress.
   *
   * @param aTelescope
   *   This supplies the time for ephemeris, station for coordinate transform,
   *   and solar position for sunlit status and Iridium angles. */

  public final double TestFlare(Telescope aTelescope)
  {
    double t1[] = new double[3];
    double t2[] = new double[3];
    double t;
    double theTriplet[] = new double[3];

    /* Update to current time and Sun, get horizontal coordinates. */

    Update(aTelescope);

    /* If the satellite is sunlit. */

    if (0 != itsIsSunlit) {

      /* If the satellite is above the horizon. */

      GetHori(0, aTelescope, theTriplet);
      if (0. < theTriplet[1]) {

        /* To calculate the Iridium angles
	 * we need the topocentric J2000 vector of the satellite here. */

        t1[0] = 0.; t1[1] = 0.; t1[2] = 0.; /* The topocentre ... */
	Topo2Mean(1, aTelescope, t1, t2);   /* in geocentric EOD ... */
	Mean2J2000(1, aTelescope, t2, t1);  /* and in J2000. */
	t2[0] = itsR[0] - t1[0];            /* Satellite topocentric J2000. */
	t2[1] = itsR[1] - t1[1];
	t2[2] = itsR[2] - t1[2];
	aTelescope.itsSun.GetPos(t1);

	/* Forward reflector. */

	t = SAT_REFLECTION(itsR[0], itsR[1], itsR[2],
          itsV[0], itsV[1], itsV[2],
	  t2[0], t2[1], t2[2], t1[0], t1[1], t1[2],
	  -40./Hmelib.DEGPERRAD, 0.);
	t *= Hmelib.DEGPERRAD;

	/* If no forward flare. */

	if (2. < t) {

	  /* Left rear reflector. */

	  t = SAT_REFLECTION(itsR[0], itsR[1], itsR[2],
            itsV[0], itsV[1], itsV[2],
            t2[0], t2[1], t2[2], t1[0], t1[1], t1[2],
            -40./Hmelib.DEGPERRAD, 120./Hmelib.DEGPERRAD);
	  t *= Hmelib.DEGPERRAD;

	  /* If no left rear flare either. */

	  if (2. < t) {

	    /* Right rear reflector. */

	    t = SAT_REFLECTION(itsR[0], itsR[1], itsR[2],
              itsV[0], itsV[1], itsV[2],
	      t2[0], t2[1], t2[2], t1[0], t1[1], t1[2],
              -40./Hmelib.DEGPERRAD, 240./Hmelib.DEGPERRAD);
	    t *= Hmelib.DEGPERRAD;
	  }
	}
	if (2. >= t) {
	  return t;
	}
      }
    }

    return 3.;
  }


  /**
Display the Satellite.

<p>This returns information about the currently stored object.
The format is</p>

<pre>
Observatory: 71 Cameron Toll Gdns, Edinburgh
   East long.   -3.154000 deg
   Latitude     55.928000 deg
   Altitude            45 m

  UT: 2003-04-08T20:00:57.0 (JD  2452738.333993)
  TT: 2003-04-08T20:02:02.8 (JDE 2452738.334754)
  Ep: 2003.267172497
             GST 09:07:37.1 = 136.904626 deg
             LST 08:55:00.2 = 133.750626 deg

Object: IRIDIUM 19

  coord. system      deg      deg     h  m  s     deg ' "      km
  --------------  --------  -------  ----------  ---------  --------
  gal.   lII,bII   160.741   42.461
  B1950  RA,Dec    138.481   55.957  09:13:55.4   55:57:25
  J2000  RA,Dec    139.391   55.747  09:17:33.9   55:44:50
  ecl.   lam,bet   122.772   37.780
  mean   RA,Dec    139.450   55.733  09:17:48.1   55:44:00      7150
  topo   HA,Dec    -43.306   48.057  21:06:46.5   48:03:25       872
  hori   A,h        88.274   62.699

     q     54.807 deg   parallactic angle
  vrot      0.120 km/s  geocentric radial velocity of topocentre
  vhel    -22.577 km/s  heliocentric radial velocity of topocentre
  vLSR    -20.778 km/s  LSR radial velocity of topocentre
  vGSR     32.754 km/s  GSR radial velocity of topocentre

  In sunlight

    I1      152.7 deg   forward Iridium angle
    I2        1.7 deg   left Iridium angle
    I3      129.7 deg   right Iridium angle
</pre>

<p>This method calls the superclass method to make the output for
NamedObject and only adds the eclipse state.</p>

@param aTelescope
  Some of the coordinate transforms require the time or the location of
  the observatory to be known.
  The spatial velocity of the Sun is needed in order to reduce
  the radial velocity of the observatory from geocentric to
  heliocentric.
   */

  public final String Show(Telescope aTelescope)
  {
    if (0 == itsIsSunlit) {
      return (super.Show(aTelescope) + "  In shadow\n\n");
    }
    else {
      return (super.Show(aTelescope) + "  In sunlight\n\n");
    }
  }


  /**
   * Set the Satellite for the given time.
   *
   * <p>This calculates for the given time the satellite's geocentric
   * J2000 coordinates and physical ephemeris.  Integration of the orbit is
   * delegated to the SDP4 class, which uses the SGP4 or SDP4 model.
   *
   * <p>To calculate whether the satellite is sunlit the Sun is assumed to be
   * point-like at infinite distance and the Earth
   * is assumed spherical with the same equatorial radius as the ellipsoidal
   * geoid.  Given the components of the satellite's geocentric vector parallel
   * and perpendicular to the geocentric vector of the Sun, the satellite is
   * sunlit if and only if the parallel component is positive or the
   * perpendicular component greater than the Earth's radius.
   *
   * <p>This routine uses {@link Station#A Station.A}
   * as the Earth's radius.
   *
   * @param aTelescope
   *   Primarily the time for which to calculate the ephemeris.
   *   Also the position of the Sun. */
static double theSunPos[] = {0.,0.,0.};
  public final void Update(Telescope aTelescope)
  {
    
    double r, r_para, r_perp;

    /* Tell the SPD4 instance we own to calculate position and velocity.
     * Then use its position to set this instance's position.
     * There it is mean EOD, here it is J2000.
     * Also extract/transform the velocity. */

    itsSDP4.GetPosVel(aTelescope.GetJD());
    SetMean(0, aTelescope, itsSDP4.itsR);
    Mean2J2000(1, aTelescope, itsSDP4.itsV, itsV);

    /* Is it sunlit?
     * Calculate the unit vector pointing from the geocentre to the Sun.
     * Calculate the satellite vector components parallel and perpendicular:
     * r_para = scalar product of itsR with theSunRHat
     * r_perp = sqrt(itsR^2 - r_para^2)
     */

    aTelescope.itsSun.GetPos(theSunPos);
    r = Math.sqrt(theSunPos[0] * theSunPos[0] + theSunPos[1] * theSunPos[1]
      + theSunPos[2] * theSunPos[2]);
    theSunPos[0] /= r; theSunPos[1] /= r; theSunPos[2] /= r;
    r = Math.sqrt(itsSDP4.itsR[0] * itsSDP4.itsR[0]
                + itsSDP4.itsR[1] * itsSDP4.itsR[1]
		+ itsSDP4.itsR[2] * itsSDP4.itsR[2]);
    r_para = itsSDP4.itsR[0] * theSunPos[0]
           + itsSDP4.itsR[1] * theSunPos[1]
           + itsSDP4.itsR[2] * theSunPos[2];
    if (0. <= r_para) {
      itsIsSunlit = 1;
    }
    else {
      r_perp = Math.sqrt(r * r - r_para * r_para);
      if (Station.A < r_perp) {
        itsIsSunlit = 1;
      }
      else {
        itsIsSunlit = 0;
      }
    }

    return;
  }


  /**
   * Read orbital elements of a satellite from file.
   *
   * <p>Given a file name and the name of a satellite, scan the
   * NORAD TLE format file for the satellite of that name.  Read the orbital
   * elements and close the file.
   *
   * <p>For the file format see the methods
   * {@link uk.me.chiandh.Lib.SDP4#NoradNext SDP4.NoradNext} and
   * {@link uk.me.chiandh.Lib.SDP4#ReadNorad12 SDP4.ReadNorad12}.
   *
   * @param aFileName
   *   The name of the file with the NORAD TLE format orbit data.
   * @param aName
   *   The name of the satellite, i.e. the string from the first of the three
   *   lines.  A case-sensitive equality check is performed after trimming
   *   off leading and trailing blanks. */

  protected final void ReadByName(String aFileName, String aName)
    throws SDP4Exception, IOException
  {
    itsSDP4.NoradByName(aFileName, aName);
    itsName = itsSDP4.itsName;
    return;
  }


  /**
   * Read orbital elements of a satellite from file.
   *
   * <p>Given an open file read from the current file position the set of
   * three lines describing a satellite orbit in NORAD TLE format.  Leave the
   * file open and positioned behind the lines read.
   *
   * <p>For the file format see the methods
   * {@link uk.me.chiandh.Lib.SDP4#NoradNext SDP4.NoradNext} and
   * {@link uk.me.chiandh.Lib.SDP4#ReadNorad12 SDP4.ReadNorad12}.
   *
   * @param aFile
   *   The file with the NORAD TLE format orbit data. */

  protected final void ReadNext(BufferedReader aFile)
    throws SDP4Exception, IOException
  {
    itsSDP4.NoradNext(aFile);
    itsName = itsSDP4.itsName;
    return;
  }


  /**
   * Calculate where the reflection of the observer's gaze would point to.
   *
   * <p>Assume that a mirror is attached to the satellite and is pointing
   * into the direction of motion.  It is now swiveled in the
   * orbit plane (like an altitude axis, towards earth is negative) and
   * then again about the (almost) earth-sat direction (like an azimuth
   * axis, a positive value would point the mirror West if the sat was
   * moving North).  By "almost" I [Randy John] really mean about the in-plane
   * vector normal to motion).
   *
   * <p>This routine is ported from Pascal code from Randy John's SKYSAT
   * programme
   * (Randy John, 2002, SKYSAT v0.64, <a href="http://home.attbi.com/~skysat">http://home.attbi.com/~skysat</a>).
   * The interface is different here in that the angles must be given in radian
   * rather than degrees and that the Sun has to be given as vector rather than
   * right ascension and declination.
   *
   * <p>The routine is used for Iridium satellites.  A flash occurs when
   * the returned angle is small.  From John's plot such a satellite is
   * brighter than -3&nbsp;mag (0&nbsp;mag) when the angle is smaller than
   * 0.5&deg; (2&deg;).  The brightest flashes are -8 or -9&nbsp;mag.
   *
   * <p>The Iridium satellite main body is a long triangular prism that is kept
   * vertical (in fact perpendicular to the orbital velocity).  Each of the
   * three sides has a large flat'ish antenna pointing 40&deg; down from the
   * "horizontal".  One of these is pointing forward, the other two
   * are looking between sideways and backward.  While the satellite is sunlit
   * any two of the three antennae reflect sunlight, often to places on the
   * Earth; these places can observe an Iridium flash.  This routine calculates
   * how far from the centre of the flash a given observer is in terms of the
   * angle seen from the satellite.
   *
   * @param EQ_X
   * @param EQ_Y
   * @param EQ_Z
   *   The geocentric position of the satellite in arbitrary units.
   * @param V_X
   * @param V_Y
   * @param V_Z
   *   The velocity vector of the satellite in arbitrary units.
   * @param SAT_X_TOPO_EQ
   * @param SAT_Y_TOPO_EQ
   * @param SAT_Z_TOPO_EQ
   *   The topocentric position of the satellite in arbitrary units.
   * @param SUN_X
   * @param SUN_Y
   * @param SUN_Z
   *   The vector of the object to be reflected.  Usually the Sun,
   *   but if you want to use the Moon be sure to use the position as seen by
   *   the satellite.
   * @param ROT_1
   *   The first mirror rotation angle in rad.  The first angle says how much
   *   the mirror is tilted upwards (away from the Earth).  Use -40&deg; for
   *   Iridium satellites.
   * @param ROT_2
   *   The second mirror rotation angle in rad.  The second angle says how much
   *   the mirror is rotated away from the forward direction.  Use 0&deg;,
   *   120&deg; and -120&deg; for Iridium satellites.  The three mirrors of
   *   these satellites require three calls to this routine. */

  protected final double SAT_REFLECTION(
    double EQ_X, double EQ_Y, double EQ_Z,
    double V_X, double V_Y, double V_Z,
    double SAT_X_TOPO_EQ, double SAT_Y_TOPO_EQ, double SAT_Z_TOPO_EQ,
    double SUN_X, double SUN_Y, double SUN_Z,
    double ROT_1, double ROT_2)
  {
    double SIN_ROT_1, COS_ROT_1, SIN_ROT_2, COS_ROT_2;
    double RR[] = new double[9];
    double XX[] = new double[3];
    double YY[] = new double[3];
    double ZZ[] = new double[3];
    double TT[] = new double[3];
    double NTT[] = new double[3];
    double SUN_REF_X, SUN_REF_Y, SUN_REF_Z;
    double SUN_REF_ANG;
    double TEMP;
    double t1[] = new double[3];
    double t2[] = new double[3];

    /*
     * Step 1 - Create a new coordinate system oriented to the satellite's
     *          local situation.  The vectors for x, y and z in the
     *          equatorial system are :
     *             x is the velocity vector
     *             y is the position-vector cross x
     *             z is x cross y
     *          Normalize vectors.
     *          Create the transform matrix RR using direction cosines.
     *          (see Fund. of Astrod., p. 82).
     */

    XX[0] = V_X; XX[1] = V_Y; XX[2] = V_Z;
    TEMP = Math.sqrt(XX[0]*XX[0] + XX[1]*XX[1] + XX[2]*XX[2]);
    XX[0] = V_X / TEMP; XX[1] = V_Y / TEMP; XX[2] = V_Z / TEMP;

    YY[0] = EQ_Y * V_Z - EQ_Z * V_Y;
    YY[1] = EQ_Z * V_X - EQ_X * V_Z;
    YY[2] = EQ_X * V_Y - EQ_Y * V_X;
    TEMP = Math.sqrt(YY[0]*YY[0] + YY[1]*YY[1] + YY[2]*YY[2]);
    YY[0] = YY[0] / TEMP; YY[1] = YY[1] / TEMP; YY[2] = YY[2] / TEMP;

    ZZ[0] = XX[1] * YY[2] - XX[2] * YY[1];
    ZZ[1] = XX[2] * YY[0] - XX[0] * YY[2];
    ZZ[2] = XX[0] * YY[1] - XX[1] * YY[0];
    TEMP = Math.sqrt(ZZ[0]*ZZ[0] + ZZ[1]*ZZ[1] + ZZ[2]*ZZ[2]);
    ZZ[0] = ZZ[0] / TEMP; ZZ[1] = ZZ[1] / TEMP; ZZ[2] = ZZ[2] / TEMP;

    /*
     * The xx, yy, zz vectors are mutually perpendicular and define the
     * coordinate system of the sat.
     *   xx is forward (velocity vector)
     *   yy is left (normal to the orbit plane)
     *   zz is up (well, not up, but perpendicular to the other two;
     *     almost 'up')
     * Everything that we know about the MMA's is given with respect to this
     * coordinate system. Since the vectors are equatorial, we now know the
     * orientation of the sat in equatorial coordinates. The rr matrix (below)
     * can transform a vector in sat coordinates into equatorial coordinates.
     * It should be able to transform (1, 0, 0) back into your original
     * velocity vector (but normalized). I hope I got all that right.
     */

    RR[0] = XX[0]; RR[1] = YY[0]; RR[2] = ZZ[0];
    RR[3] = XX[1]; RR[4] = YY[1]; RR[5] = ZZ[1];
    RR[6] = XX[2]; RR[7] = YY[2]; RR[8] = ZZ[2];

    /*
     * Step 2 - Initialize an x unit vector to (1, 0, 0).
     *          Rotate it about the y-axis the desired amount.
     *          Rotate it about the z-axis the desired amount.
     *          Transform it to equatorial using RR and call it XX.
     *          Repeat for y (0, 1, 0) and z (0, 0, 1).
     *          These vectors define the coordinate system for the mirror.
     *          The XX vector is normal to the surface of the mirror.
     *          Recreate the transform matrix RR.
     */

    SIN_ROT_1 = Math.sin(ROT_1); /* Rotate the y-axis. */
    COS_ROT_1 = Math.cos(ROT_1);
    SIN_ROT_2 = Math.sin(ROT_2); /* Rotate the z-axis. */
    COS_ROT_2 = Math.cos(ROT_2);

    SIN_ROT_1 = Math.sin(ROT_1); /* Rotate the y-axis. */
    COS_ROT_1 = Math.cos(ROT_1);
    SIN_ROT_2 = Math.sin(ROT_2); /* Rotate the z-axis. */
    COS_ROT_2 = Math.cos(ROT_2);

    TT[0] = 1.; TT[1] = 0.; TT[2] = 0.;
    NTT[0] = TT[0] * COS_ROT_1 -  TT[2]  * SIN_ROT_1;
    NTT[1] = TT[1];
    NTT[2] = TT[0] * SIN_ROT_1 +  TT[2]  * COS_ROT_1;
    TT[0] =  NTT[0] * COS_ROT_2 +  NTT[1] * SIN_ROT_2;
    TT[1] = -NTT[0] * SIN_ROT_2 +  NTT[1] * COS_ROT_2;
    TT[2] =  NTT[2];
    XX[0] = TT[0] * RR[0] + TT[1] * RR[1] + TT[2] * RR[2];
    XX[1] = TT[0] * RR[3] + TT[1] * RR[4] + TT[2] * RR[5];
    XX[2] = TT[0] * RR[6] + TT[1] * RR[7] + TT[2] * RR[8];

    TT[0] = 0.; TT[1] = 1.; TT[2] = 0.;
    NTT[0] = TT[0] * COS_ROT_1 -  TT[2]  * SIN_ROT_1;
    NTT[1] = TT[1];
    NTT[2] = TT[0] * SIN_ROT_1 +  TT[2]  * COS_ROT_1;
    TT[0] =  NTT[0] * COS_ROT_2 +  NTT[1] * SIN_ROT_2;
    TT[1] = -NTT[0] * SIN_ROT_2 +  NTT[1] * COS_ROT_2;
    TT[2] =  NTT[2];
    YY[0] = TT[0] * RR[0] + TT[1] * RR[1] + TT[2] * RR[2];
    YY[1] = TT[0] * RR[3] + TT[1] * RR[4] + TT[2] * RR[5];
    YY[2] = TT[0] * RR[6] + TT[1] * RR[7] + TT[2] * RR[8];

    TT[0] = 0.; TT[1] = 0.; TT[2] = 1.;
    NTT[0] = TT[0] * COS_ROT_1 -  TT[2]  * SIN_ROT_1;
    NTT[1] = TT[1];
    NTT[2] = TT[0] * SIN_ROT_1 +  TT[2]  * COS_ROT_1;
    TT[0] =  NTT[0] * COS_ROT_2 +  NTT[1] * SIN_ROT_2;
    TT[1] = -NTT[0] * SIN_ROT_2 +  NTT[1] * COS_ROT_2;
    TT[2] =  NTT[2];
    ZZ[0] = TT[0] * RR[0] + TT[1] * RR[1] + TT[2] * RR[2];
    ZZ[1] = TT[0] * RR[3] + TT[1] * RR[4] + TT[2] * RR[5];
    ZZ[2] = TT[0] * RR[6] + TT[1] * RR[7] + TT[2] * RR[8];

    RR[0] = XX[0]; RR[1] = YY[0]; RR[2] = ZZ[0];
    RR[3] = XX[1]; RR[4] = YY[1]; RR[5] = ZZ[1];
    RR[6] = XX[2]; RR[7] = YY[2]; RR[8] = ZZ[2];

    /*
     * Step 3 - Convert the observer-sat vector to the mirror coord system.
     *          Negate the x coordinate (the reflection).
     *          Convert it back to the equatorial system.
     *          Convert to RA and Dec and find the angle between that
     *          position and the sun's.
     *          Make sure that the sun is in front of the mirror.
     */

    TT[0] = SAT_X_TOPO_EQ * RR[0] + SAT_Y_TOPO_EQ * RR[3]
	  + SAT_Z_TOPO_EQ * RR[6];
    TT[1] = SAT_X_TOPO_EQ * RR[1] + SAT_Y_TOPO_EQ * RR[4]
	  + SAT_Z_TOPO_EQ * RR[7];
    TT[2] = SAT_X_TOPO_EQ * RR[2] + SAT_Y_TOPO_EQ * RR[5]
	  + SAT_Z_TOPO_EQ * RR[8];

    TT[0] = -TT[0];

    if (TT[0] >= 0.) {
      SUN_REF_X = TT[0] * RR[0] + TT[1] * RR[1] + TT[2] * RR[2];
      SUN_REF_Y = TT[0] * RR[3] + TT[1] * RR[4] + TT[2] * RR[5];
      SUN_REF_Z = TT[0] * RR[6] + TT[1] * RR[7] + TT[2] * RR[8];

      /*
       * Code modification by hme.
      SUN_REF_ALPHA = ARCTAN_D2(SUN_REF_Y, SUN_REF_X);
      SUN_REF_DELTA = ASIN_D(SUN_REF_Z /
                           Math.sqrt(SUN_REF_X*SUN_REF_X +
                                     SUN_REF_Y*SUN_REF_Y +
                                     SUN_REF_Z*SUN_REF_Z));
      SUN_REF_ANG = ANG_DIS(SUN_ALPHA, SUN_DELTA,
                            SUN_REF_ALPHA, SUN_REF_DELTA);
       * This code determines the angular distance between the Sun
       * (SUN_ALPHA/DELTA) and the reflection of the observer in the
       * satellite mirror (SUN_REF_ALPHA/DELTA).
       * Now, we have Hmelib.SpherDist for this sort of thing, but it
       * takes two vectors.  They don't even have to be unit vectors.
       * This saves us the hassle of inverse trigonometric functions here,
       * and it intices us to replace the Sun RA/Dec with its vector in the
       * given arguments.
       */

      t1[0] = SUN_X;     t1[1] = SUN_Y;     t1[2] = SUN_Z;
      t2[0] = SUN_REF_X; t2[1] = SUN_REF_Y; t2[2] = SUN_REF_Z;
      SUN_REF_ANG = Hmelib.SpherDist(t1, t2);
    }
    else {
      SUN_REF_ANG = Math.PI;
    }

    return SUN_REF_ANG;
  }

}
