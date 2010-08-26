
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;
import uk.me.chiandh.Lib.HmelibException;

/**
<p>The <code>NamedObject</code> class extends the {@link Catalog
Catalog} class such that it stores only one position, but also a name.  It
provides some astronomical constants to its subclasses.</p>

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
<dd>New class.</dd>
<dt><strong>2002-06-17:</strong> hme</dt>
<dd>Add ShowToFile().</dd>
<dt><strong>2002-06-21:</strong> hme</dt>
<dd>Change ShowToFile() to use a Station instead of Time, add topo and
  hori output to it.</dd>
<dt><strong>2002-06-22:</strong> hme</dt>
<dd>Add CommandSet().</dd>
<dt><strong>2002-07-02:</strong> hme</dt>
<dd>Change ShowToFile() to use a Sun as well as a Station, and to
  calculate and display heliocentric, LSR and GSR radial velocities.</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Consolidate documentation.</dd>
<dt><strong>2003-04-06:</strong> hme</dt>
<dd>Change output of azimuth to range 0 to 360 deg.</dd>
<dt><strong>2003-04-06:</strong> hme</dt>
<dd>Change distance output to km for small distances.</dd>
<dt><strong>2003-06-14:</strong> hme</dt>
<dd>Show the station before the object.</dd>
<dt><strong>2003-06-14:</strong> hme</dt>
<dd>Add GetName() and GetXYZ() methods.</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Add Copy() method.</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Avoid the use of Times.SetTT for the 1900 epoch, as it would imply
  propagating an exception upwards that will never occur.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2003-09-17:</strong> hme</dt>
<dd>Add NextRiseSet() method.</dd>
<dt><strong>2003-09-18:</strong> hme</dt>
<dd>Add protected NaiveRiseSet() method to which the semi day arc
  calculation is delegated.  While NextRiseSet is overriden by subclasses,
  NaiveRiseSet is inherited by them.</dd>
<dt><strong>2004-08-03:</strong> hme</dt>
<dd>Fix bug in ShowToFile whereby the parallactic angle was calculated
  with the NCP not above the N horizon but above the S horizon.  This bug
  probably propagated from Sputnik 1.9.</dd>
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>Port to Sputnik3.  Change ShowToFile() to be Show() and to return a
  string instead of writing to a given stream.</dd>
<dt><strong>2008-12-17:</strong> hme</dt>
<dd>Calculate and display heliocentric correction in Show().</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */


public class NamedObject extends Catalog
{

  /** Astronomical unit (au) in units of Gm. */

  public static final double AU = 149.597870;


  /** Speed of light in Gm/d. */

  public static final double LIGHTGMD = 25902.068371;


  /** Gauss gravitational constant. */

  public static final double GAUSSK = 0.01720209895;


  /** Factor to convert sideral and solar time. */

  public static final double SIDSOL = 366.2422/365.2422;


  /** Elevation of ordinary rise and set (refraction).
   *
   * <p>An object rises or sets when its elevation is approximately -34'. */

  public static final double RISE = -34. * Math.PI / 60. / 180.;


  /** Elevation of solar/lunar rise and set (refraction).
   *
   * <p>The Sun or Moon (their top limb) rises or sets when its elevation is
   * approximately -50'. */

  public static final double RISESUN = -50. * Math.PI / 60. / 180.;


  /** Solar elevation at civil dawn and dusk.
   *
   * <p>Civil twilight is defined as the Sun being less than 6&deg; below
   * the mathematical horizon. */

  public static final double RISECIVIL = -6. * Math.PI / 180.;


  /** Solar elevation at nautical dawn and dusk.
   *
   * <p>Nautical twilight is defined as the Sun being less than 12&deg; below
   * the mathematical horizon. */

  public static final double RISENAUTI = -12. * Math.PI / 180.;


  /** Solar elevation at astronomical dawn and dusk.
   *
   * <p>Astronomical twilight is defined as the Sun being less than 18&deg;
   * below the mathematical horizon. */

  public static final double RISEASTRO = -18. * Math.PI / 180.;


  /** The name of the object. */

  protected String itsName;


  /**
   * Serve the <code>object/whatever</code> commands that set the position.
   *
   * @param aCommand
   *   The full command string, from which the parameters will be read.
   * @param aStation
   *   Some of the commands served here require either the time (for the
   *   obliquity of the ecliptic or in case of equinox of date coordiantes) or
   *   the location of the observatory (if horizontal of topocentric
   *   coordinates are involved).  This given parameter will contain that
   *   information. */

  public final void CommandSet(String aCommand, Station aStation)
    throws HmelibException
  {
    double t1[] = new double[3];
    double t2[] = new double[3];
    String  theString;

    /* Strip the first word (the command) off the command string to be left
     * with the parameters.  Then read each and skip each.  At the end of
     * this block theString contains the name of the object. */

    theString = Hmelib.Sstring(aCommand);
    t1[0]     = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    t1[1]     = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    t1[2]     = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    theString = Hmelib.Rstring(theString);

    /* Convert to rectangular. */

    t1[0] /= Hmelib.DEGPERRAD;
    t1[1] /= Hmelib.DEGPERRAD;
    Hmelib.Rect(t1, t2);

    /* Set the fields. */

    itsName = theString;
    if      (aCommand.startsWith("object/gal"))   {SetGal(  0, t2);}
    else if (aCommand.startsWith("object/B1950")) {SetB1950(0, t2);}
    else if (aCommand.startsWith("object/J2000")) {SetJ2000(0, t2);}
    else if (aCommand.startsWith("object/mean"))  {SetMean( 0, aStation, t2);}
    else if (aCommand.startsWith("object/ecl"))   {SetEcl(  0, aStation, t2);}
    else if (aCommand.startsWith("object/topo"))  {SetTopo( 0, aStation, t2);}
    else if (aCommand.startsWith("object/hori"))  {SetHori( 0, aStation, t2);}

    return;
  }


  /**
   * Copy the state of a NamedObject instance from another.
   *
   * <p>Invoke this method for the new instance of NamedObject and pass as
   * argument an existing NamedObject instance from which to copy the state.
   * The new instance must have been initialised with Init() before making
   * this call.
   *
   * @param aNamedObject
   *   The object to be copied into this instance. */

  public void Copy(NamedObject aNamedObject) {
    aNamedObject.GetXYZ(itsR);
    itsName = aNamedObject.GetName();
    return;
  }


  /**
   * Return the object name.
   *
   * <p>This returns the stored object name. */

  public final String GetName() {return(itsName);}


  /**
   * Return the object position.
   *
   * <p>This gives access to the stored position.  The numbers are in Gm and
   * the axis orientation is equatorial for J2000.
   *
   * @param aTriplet
   *   The geocentric rectangular coordinates in the J2000 coordinate
   *   system. */

  public final void GetXYZ(double aTriplet[])
  {
    aTriplet[0] = itsR[0]; aTriplet[1] = itsR[1]; aTriplet[2] = itsR[2];
    return;
  }


  /**
   * Initialise the object.
   *
   * This initialises the object to be the Galactic Centre, which is
   * 8.5&nbsp;kpc away. */

  public void Init()
  {
    /* The constant is 8.5 kpc. */
    final double t[] = {2.623E11, 0., 0.};
    super.Init(1);
    itsName = "Galactic Centre";
    SetGal(0, t);
  }


  /**
   * Return the times of the next rise and set.
   *
   * <p>This takes the given station and its time to scan forward for the next
   * rise and set times.  The set may occur before the rise, of course.  The
   * given Telescope instance is left alone.
   *
   * <p>See {@link #NaiveRiseSet NaiveRiseSet} for details, but note that this
   * method makes sure that the time is in the future (greater than the time
   * in the given Telescope).
   *
   * @param aTelescope
   *   The rise and set time are calculated for this given location on the
   *   Earth.  Also, the rise and set times are the next occurences after the
   *   time given in this instance of Telescope.  So for this method only
   *   a Station would be required, but subclasses need to override this
   *   method and do require a Telescope.
   * @param aElev
   *   The elevation in radian that defines the event.
   * @param findRise
   *   Give true for rise, false for set. */

  public Times NextRiseSet(Telescope aTelescope, double aElev,
    boolean findRise)
    throws CometNoConvException, NamedObjectCircPolException
  {
    Times theRise;

    /* Calculate a nearby rise (set). */

    theRise = NaiveRiseSet(aTelescope, aElev, findRise);

    /* If that is in the past, go forward one sidereal day. */

    while (0. > theRise.Sub(aTelescope)) {theRise.Add(1./SIDSOL);}

    return theRise;
  }


  /**
Display the NamedObject.

<p>This returns information about the currently stored object.  The format
is</p>

<pre>
Observatory: Royal Observatory Edinburgh
   East long.   -3.182500 deg
   Latitude     55.925000 deg
   Altitude           146 m

  UT: 2003-06-14T15:24:14.4 (JD  2452805.141834)
  TT: 2003-06-14T15:25:20.3 (JDE 2452805.142596)
  Ep: 2003.450082399
             GST 08:54:18.3 = 133.576169 deg
             LST 08:41:34.5 = 130.393669 deg
  HJD-JD:            -507.0 s         -0.005869 d

Object: Galactic Centre

  coord. system      deg      deg     h  m  s     deg ' "      Gm
  --------------  --------  -------  ----------  ---------  --------
  gal.   lII,bII   360.000   -0.000
  B1950  RA,Dec    265.611  -28.917  17:42:26.6  -28:55:00
  J2000  RA,Dec    266.405  -28.936  17:45:37.2  -28:56:10
  ecl.   lam,bet   266.888   -5.537
  mean   RA,Dec    266.460  -28.937  17:45:50.4  -28:56:15  2.623E11
  topo   HA,Dec   -136.066  -28.937  14:55:44.1  -28:56:15  2.623E11
  hori   A,h        67.547  -48.928

     q     53.520 deg   parallactic angle
  vrot      0.159 km/s  geocentric radial velocity of topocentre
  vhel      2.213 km/s  heliocentric radial velocity of topocentre
  vLSR     12.484 km/s  LSR radial velocity of topocentre
  vGSR     12.484 km/s  GSR radial velocity of topocentre
</pre>

<p>The rotation of the Earth according to
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.79f.
is 0.00007292115018 rad/s.  Reduction to heliocentric velocities involves
the spatial velocity of the Sun, which the Sun class can deliver from
the difference in position over a 1000&nbsp;s interval.  Reduction to
the local standard of rest requires us to precess the Sun's LSR velocity
from equinox 1900 to J2000.  The velocity in question is 20&nbsp;km/s
toward RA 270&deg;, Dec +30&deg; for equinox 1900.  Reduction to the
galactic standard of rest involves the rotation of the Galaxy of
220&nbsp;km/s at the galactocentric distance of the Sun.</p>

   * @param aTelescope
   *   Some of the coordinate transforms require the time or the location of
   *   the observatory to be known.
   *   The spatial velocity of the Sun is needed in order to reduce
   *   the radial velocity of the observatory from geocentric to
   *   heliocentric.
   *   The position of the Sun is needed to calculate the HJD-JD correction. */

  public String Show(Telescope aTelescope)
  {
    String theOutput  = "";
    Times  the1900    = new Times();
    double theGal[]   = new double[3];
    double theB50[]   = new double[3];
    double theDat[]   = new double[3];
    double theEcl[]   = new double[3];
    double theTop[]   = new double[3];
    double theHor[]   = new double[3];
    double theSpher[] = new double[3];
    double theRect[]  = new double[3];
    double theHMS[]   = new double[3];
    double theV3[]    = new double[3];
    double Triplet1[] = new double[3];
    double Triplet2[] = new double[3];
    double Triplets[] = new double[9];
    double theParAng, theVRot, theVHel, theVLSR, theVGSR;
    double distMag;
    double theHJD;
    boolean doHJD;

    /* The distance tells whether to calculate HJD correction or not.
     * distMag tells whether to use normal Gm format, exponential Gm format
     * or km format. */

    distMag = Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1]
		      + itsR[2] * itsR[2]);

    doHJD = true;
    if  (10000000. > distMag) {doHJD = false;}
    if      (1.    > distMag) {distMag = -1.;}
    else if (9999. < distMag) {distMag = +1.;}
    else {                     distMag =  0.;}

    /* Transform away from J2000 to all systems. */

    J20002B1950(1, itsR, theB50);
    B19502Gal(1, theB50, theGal);
    J20002Mean(1, aTelescope, itsR, theDat);
    Mean2Ecl(1, aTelescope, theDat, theEcl);
    Mean2Topo(1, aTelescope, theDat, theTop);
    Topo2Hori(1, aTelescope, theTop, theHor);

    /* Parallactic angle. */

    Triplets[0] = 0.;
    Triplets[1] = 0.;
    Triplets[2] = 1.;
    Triplets[3] = theHor[0];
    Triplets[4] = theHor[1];
    Triplets[5] = theHor[2];
    Triplets[6] = Math.cos(aTelescope.GetLat());
    Triplets[7] = 0.;
    Triplets[8] = Math.sin(aTelescope.GetLat());
    theParAng   = Hmelib.SpherAng(Triplets);
    if (Math.PI < theParAng) theParAng -= 2. * Math.PI;

    /* Rotation of the Earth. */

    aTelescope.GetX0Z(Triplets);
    theVRot = -72.92115018 * Triplets[0] * theTop[1]
            / Math.sqrt(theTop[0] * theTop[0]
		      + theTop[1] * theTop[1] + theTop[2] * theTop[2]);

    /* Revolution of the Earth. */

    aTelescope.itsSun.GetVel(theV3);
    theVHel = theVRot
      - (itsR[0] * theV3[0] + itsR[1] * theV3[1] + itsR[2] * theV3[2])
      / Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1] + itsR[2] * itsR[2]);

    /* Peculiar motion of the Sun.
     * This is in 1900 coordinates 20 km/s towards RA 270 deg, Dec 30 deg. */

    theSpher[0] = 270. / Hmelib.DEGPERRAD;
    theSpher[1] =  30. / Hmelib.DEGPERRAD;
    theSpher[2] =  20.;
    Hmelib.Rect(theSpher, theRect);
    the1900.SetJD(-34979.99998); /* TT 1900-01-00-12:00:00 */
    Mean2J2000(1, the1900, theRect, theV3);
    theVLSR = theVHel
      + (itsR[0] * theV3[0] + itsR[1] * theV3[1] + itsR[2] * theV3[2])
      / Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1] + itsR[2] * itsR[2]);

    /* Rotation of the Galaxy. */

    Hmelib.Spher(theGal, theSpher);
    theVGSR = theVLSR + 220. * Math.sin(theSpher[0]) * Math.cos(theSpher[1]);

    /* HJD correction. */

    theHJD = 0.;
    if (doHJD) {
      aTelescope.itsSun.GetPos(Triplet1);
      J20002Mean( 1, aTelescope, Triplet1, Triplet2);
      Mean2Ecl(   1, aTelescope, Triplet2, Triplet1);
      Mean2Topo(  1, aTelescope, Triplet1, Triplet2);
      Hmelib.Spher(Triplet2, Triplet1);
      theHJD = -Math.cos(Hmelib.SpherDist(Triplet2, theTop))
	     * Triplet1[2] / LIGHTGMD;
    }

    /* Print it all out.  theSpher currently holds lII, bII. */

    theOutput = aTelescope.Show();

    if (doHJD) {
      theOutput = theOutput + "  HJD-JD:           "
        + Hmelib.Wfndm(7, 1, 86400. * theHJD) + " s         "
	+ Hmelib.Wfndm(9, 6, theHJD) + " d\n\n";
    }

    theOutput = theOutput + "Object: " + itsName;

    if (-0.5 > distMag) {
      theOutput = theOutput +
"\n\n  coord. system      deg      deg     h  m  s     deg ' \"      km\n" +
"  --------------  --------  -------  ----------  ---------  --------";
    }
    else {
      theOutput = theOutput +
"\n\n  coord. system      deg      deg     h  m  s     deg ' \"      Gm\n" +
"  --------------  --------  -------  ----------  ---------  --------";
    }

    theOutput = theOutput + "\n  gal.   lII,bII  "
      + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
      + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD);

    Hmelib.Spher(theB50, theSpher);
    theOutput = theOutput + "\n  B1950  RA,Dec   "
      + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
      + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD);
    Hmelib.deg2dms(theSpher[0] * Hmelib.DEGPERRAD / 15., theHMS);
    theOutput = theOutput + "  "
      + Hmelib.WTime2(theHMS[0], theHMS[1], theHMS[2]) + "  "
      + Hmelib.Wdms(theSpher[1] * Hmelib.DEGPERRAD);

    Hmelib.Spher(itsR, theSpher);
    theOutput = theOutput + "\n  J2000  RA,Dec   "
      + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
      + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD);
    Hmelib.deg2dms(theSpher[0] * Hmelib.DEGPERRAD / 15., theHMS);
    theOutput = theOutput + "  "
      + Hmelib.WTime2(theHMS[0], theHMS[1], theHMS[2]) + "  "
      + Hmelib.Wdms(theSpher[1] * Hmelib.DEGPERRAD);

    Hmelib.Spher(theEcl, theSpher);
    theOutput = theOutput + "\n  ecl.   lam,bet  "
      + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
      + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD);

    Hmelib.Spher(theDat, theSpher);
    theOutput = theOutput + "\n  mean   RA,Dec   "
      + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
      + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD);
    Hmelib.deg2dms(theSpher[0] * Hmelib.DEGPERRAD / 15., theHMS);
    theOutput = theOutput + "  "
      + Hmelib.WTime2(theHMS[0], theHMS[1], theHMS[2]) + "  "
      + Hmelib.Wdms(theSpher[1] * Hmelib.DEGPERRAD) + "  ";
    if (-0.5 > distMag) {
      theOutput = theOutput + Hmelib.Wfndm(8, 0, theSpher[2] * 1E6);
    }
    else if (0.5 < distMag) {
      theOutput = theOutput + Hmelib.Wfexp(theSpher[2]);
    }
    else {
      theOutput = theOutput + Hmelib.Wfndm(8, 3, theSpher[2]);
    }

    Hmelib.Spher(theTop, theSpher);
    Hmelib.deg2dms(theSpher[0] * Hmelib.DEGPERRAD / 15., theHMS);
    theSpher[0] = Hmelib.NormAngle0(theSpher[0]);
    theOutput = theOutput + "\n  topo   HA,Dec   "
      + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
      + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD) + "  "
      + Hmelib.WTime2(theHMS[0], theHMS[1], theHMS[2])     + "  "
      + Hmelib.Wdms(theSpher[1] * Hmelib.DEGPERRAD)        + "  ";
    if (-0.5 > distMag) {
      theOutput = theOutput + Hmelib.Wfndm(8, 0, theSpher[2] * 1E6);
    }
    else if (0.5 < distMag) {
      theOutput = theOutput + Hmelib.Wfexp(theSpher[2]);
    }
    else {
      theOutput = theOutput + Hmelib.Wfndm(8, 3, theSpher[2]);
    }

    Hmelib.Spher(theHor, theSpher);
    theSpher[0] = Hmelib.NormAngle180(theSpher[0]);
    theOutput = theOutput + "\n  hori   A,h      "
      + Hmelib.Wfndm(8, 3, theSpher[0] * Hmelib.DEGPERRAD)
      + Hmelib.Wfndm(9, 3, theSpher[1] * Hmelib.DEGPERRAD)

      + "\n\n     q   "
      + Hmelib.Wfndm(8, 3, theParAng * Hmelib.DEGPERRAD)
      + " deg   parallactic angle"

      + "\n  vrot   "
      + Hmelib.Wfndm(8, 3, theVRot)
      + " km/s  geocentric radial velocity of topocentre"

      + "\n  vhel   "
      + Hmelib.Wfndm(8, 3, theVHel)
      + " km/s  heliocentric radial velocity of topocentre"

      + "\n  vLSR   "
      + Hmelib.Wfndm(8, 3, theVLSR)
      + " km/s  LSR radial velocity of topocentre"

      + "\n  vGSR   "
      + Hmelib.Wfndm(8, 3, theVGSR)
      + " km/s  GSR radial velocity of topocentre\n\n";

    return theOutput;
  }


  /**
   * Return the times of the next rise and set.
   *
   * <p>This takes the given station and its time to scan for a nearby rise
   * or set time.  The set may occur before the rise, of course.  The
   * given Station instance is left alone.
   *
   * <p>We generalise the concept of rise and set to mean the rising and
   * setting passage through a given elevation.  The caller has to specify the
   * elevation required:
   *
   * <p><table border>
   *   <col><col>
   *   <tr><th>h</th><th>object, event etc.</th></tr>
   *   <tr valign="top">
   *     <td align="right">0</td>
   *     <td>rise and set of any object (centre), ignoring refraction</td>
   *   </tr>
   *   <tr valign="top">
   *     <td align="right">-50'</td>
   *     <td>rise and set of Sun or Moon (upper limb), taking account of
   *       average refraction and average apparent diameter</td>
   *   </tr>
   *   <tr valign="top">
   *     <td align="right">-34'</td>
   *     <td>rise and set of an object other than Sun or Moon,
   *       taking account of average refraction</td>
   *   </tr>
   *   <tr valign="top">
   *     <td align="right">-6&deg;</td>
   *     <td>civil dawn and dusk when applied to the Sun</td>
   *   </tr>
   *   <tr valign="top">
   *     <td align="right">-12&deg;</td>
   *     <td>nautical dawn and dusk when applied to the Sun</td>
   *   </tr>
   *   <tr valign="top">
   *     <td align="right">-18&deg;</td>
   *     <td>astronomical dawn and dusk when applied to the Sun</td>
   *   </tr>
   * </table>
   *
   * <p>(Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA).
   * All these values are available as public constants of this class.
   *
   * <p>Since the calculation depends merely on the elevation below the
   * mathematical horizon, the height above sea level is irrelevant.
   *
   * <p>For objects fixed on the celestial sphere the hour angles of rise and
   * set are a function of geodetic latitude and object declination according
   * to
   *
   * <p>HA = &plusmn;[(sin h - sin &phi;) sin &delta;]
   *       / (cos &phi; cos &delta;)
   *
   * @param aStation
   *   The rise and set time are calculated for this given location on the
   *   Earth.  Also, the rise and set times are the next occurences near the
   *   time given in this instance of Station.
   * @param aElev
   *   The elevation in radian that defines the event.
   * @param findRise
   *   Give true for rise, false for set. */

  protected Times NaiveRiseSet(Station aStation, double aElev,
    boolean findRise)
    throws NamedObjectCircPolException
  {
    Times  theTimes;
    double theTopo[] = new double[3];
    double theSDA, theRise;

    /* Calculate the topocentric HA and Dec. */

    GetTopo(0, aStation, theTopo);

    /* If the declination is circumpolar, throw an exception. */
    /* Calculate the semi daily arc in the process. */

    theSDA  = Math.cos(aStation.GetLat()) * Math.cos(theTopo[1]);
    if (0. == theSDA)
      throw new NamedObjectCircPolException("object circumpolar");
    theSDA = (Math.sin(aElev) - Math.sin(aStation.GetLat())
	   *  Math.sin(theTopo[1])) / theSDA;
    if (1. <= Math.abs(theSDA))
      throw new NamedObjectCircPolException("object circumpolar");
    theSDA  = Math.acos(theSDA);

    /* A rise occurs (-SDA - HA) from now in sidereal time.
     * We do nothing here to make sure the result is in the future. */

    /* If rise requested. */

    if (findRise) {
      theRise = Hmelib.NormAngle0(-theSDA - theTopo[0]);
    }

    /* Else (set requested). */

    else {
      theRise = Hmelib.NormAngle0(+theSDA - theTopo[0]);
    }

    /* Copy given time to the returned Times.
     * Then apply the above increment.
     * We need to scale the increment from sidereal to solar and from
     * radian to days. */

    theRise *= Hmelib.DEGPERRAD / SIDSOL / 360.;
    theTimes = new Times(); theTimes.Init();
    theTimes.Copy(aStation); theTimes.Add(theRise);

    return theTimes;
  }

}
