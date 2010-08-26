
package uk.me.chiandh.Sputnik;

import java.io.*;
import uk.me.chiandh.Lib.Hmelib;
import uk.me.chiandh.Lib.HmelibException;

/**
<p>The <code>Comet</code> class extends the {@link NamedObject
NamedObject} class such that it stores not only one position and a name,
but also holds information needed for a comet or asteroid.  This class
therefore is similar to its sister class {@link VSOP87 VSOP87}
and its subclasses, such as Sun.</p>

<p>Orbits of comets and asteroids are described by osculating elements of
elliptic, parabolic or hyperbolic orbits.  These change over the course of
months or years, for comets human knowledge of them also improves as more
observations of a new comet are made.  Therefore it is necessary to base
the calculation on data that the user can obtain from the
IAU Central Bureau for Astronomical Telegrams (<a href="http://cfa-www.harvard.edu/iau/Ephemerides/">http://cfa-www.harvard.edu/iau/Ephemerides</a>).
They have a current list of comets as well as annual lists of bright
asteroids that have an opposition during that year.  They provide these
data in formats for a variety of astronomy software, the format required
here is that of Xephem
(Elwood Charles Downey, 1999, <em>XEphem Version 3.2</em>, <a href="http://www.ClearSkyInstitute.com/xephem/xephem.html">http://www.ClearSkyInstitute.com/xephem/xephem.html</a>).</p>

<p>Copyright: &copy; 2002-2006 Horst Meyerdierks.</p>

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
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>New class.</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Consolidate documentation.</dd>
<dt><strong>2003-04-03:</strong> hme</dt>
<dd>Actually read H and G for type 4 objects, else the magnitudes for
  asteroids are completely wrong.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.
  Review of use of Exceptions.</dd>
<dt><strong>2003-09-18:</strong> hme</dt>
<dd>Add NextRiseSet() method.</dd>
<dt><strong>2006-11-13:</strong> hme</dt>
<dd>Port to Sputnik3.
  Change ShowToFile() to become Show() and
  to return a string rather than write to a stream.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */


public class Comet extends NamedObject
{

  /**
   * The type of object.
   *
   * <p>1: hyperbolic comet,
   * 2: parabolic comet,
   * 3: elliptic comet,
   * 4: elliptic asteroid. */

  protected int    itsType;


  /**
   * Time when the mean anomaly M equals M0.
   *
   * <p>Also the epoch of osculation. */

  protected Times  its_T0;


  /** Semimajor axis in au. */

  protected double its_a;


  /** Eccentricity. */

  protected double its_e;


  /** Perihelion distance in au. */

  protected double its_q;


  /** Inclination in rad. */

  protected double its_i;


  /** Argument of perihelion in rad. */

  protected double its_omega;


  /** Longitude of ascending node in rad. */

  protected double its_OMEGA;


  /** Mean motion in rad/d. */

  protected double its_n;


  /** Mean anomaly at T0 in rad. */

  protected double its_M0;


  /** Asteroid magnitude, first parameter. */

  protected double its_H;


  /** Asteroid magnitude, second parameter. */

  protected double its_G;


  /** Comet magnitude, first parameter. */

  protected double its_g;


  /** Comet magnitude, second parameter. */

  protected double its_k;


  /**
   * Alternate elements.
   *
   * <p>Alternate elements for the orientation of the orbit w.r.t. the equator
   * and equinox of date.  Cf.
   * Jean Meeus, 1991, <I>Astronomical Algorithms</I>, Willmann-Bell, Richmond VA, p.214.
   */

  protected double itsMeeusA, itsMeeusB, itsMeeusC,
                   itsMeeusa, itsMeeusb, itsMeeusc;


  /** Elongation from the Sun. */

  protected double itsElong;


  /** Phase angle. */

  protected double itsPhase;


  /** Illuminated fraction of disc. */

  protected double itsIllum;


  /** Magnitude. */

  protected double itsMag;


  /**
   * Initialise the Comet object.
   *
   * <p>This sets the object to be a point 100&nbsp;Gm from Earth in the
   * direction of the vernal equinox. */

  public void Init()
  {
    final double t[] = {100., 0., 0.};
    super.Init();
    itsName = "Unspecified Comet object";
    SetJ2000(0, t);
    its_T0 = new Times();
    its_T0.Init();
  }


  /**
   * Return the times of the next rise and set.
   *
   * <p>See {@link Moon#NextRiseSet Moon.NextRiseSet}, of which this method
   * is a copy.
   *
   * @param aTelescope
   *   The rise and set time are calculated for this given location on the
   *   Earth.  Also, the rise and set times are the next occurences after the
   *   time given in this instance of Telescope.  Finally, the given Telescope
   *   itself is used in calls to the Update method.
   * @param aElev
   *   The elevation in radian that defines the event.
   *   See {@link NamedObject#NaiveRiseSet NamedObject.NaiveRiseSet}.
   * @param findRise
   *   Give true for rise, false for set. */

  public Times NextRiseSet(Telescope aTelescope, double aElev,
    boolean findRise)
      throws CometNoConvException, NamedObjectCircPolException
  {
    Telescope theTelescope;
    double    theTriplet[] = new double[3];
    double    theStart, theH0, theH1;
    double    theT, theT0, theT1;
    Times     theRise, theRise0, theRise1;
    int       i;

    /* Clone this instance of this class into one we can merrily update to
     * different times.  For other objects this could be done by creating a
     * new one and just updating it, but here we would have to read elements
     * from a file, without knowledge of the file name.  So we must tediously
     * copy most of the fields. */

    Comet     theObject;
    theObject = new Comet();
    theObject.Init();
    theObject.its_T0    = its_T0;
    theObject.its_a     = its_a;
    theObject.its_e     = its_e;
    theObject.its_q     = its_q;
    theObject.its_i     = its_i;
    theObject.its_omega = its_omega;
    theObject.its_OMEGA = its_OMEGA;
    theObject.its_n     = its_n;
    theObject.its_M0    = its_M0;
    theObject.its_H     = its_H;
    theObject.its_G     = its_G;
    theObject.its_g     = its_g;
    theObject.its_k     = its_k;
    theObject.itsMeeusA = itsMeeusA;
    theObject.itsMeeusB = itsMeeusB;
    theObject.itsMeeusC = itsMeeusC;
    theObject.itsMeeusa = itsMeeusa;
    theObject.itsMeeusb = itsMeeusb;
    theObject.itsMeeusc = itsMeeusc;

    /* Also clone the given Telescope so we can loop in time. */

    theTelescope = new Telescope();
    theTelescope.Init();

    /* Try the straighforward iteration. */

    try {

      /* Initialise iteration. */

      theTelescope.Copy(aTelescope);
      theObject.Update(theTelescope);
      theRise0 = theObject.NaiveRiseSet(theTelescope, aElev, findRise);
      while (0. > theRise0.Sub(aTelescope)) {theRise0.Add(1./SIDSOL);}

      /* Iterate at most 100 times. */

      for (i = 0; i < 100; i++) {

        /* Update the clone to the current guess for rise (set). */

	theTelescope.SetJD(theRise0.GetJD());
	theObject.Update(theTelescope);

	/* Determine the rise (set) time assuming that the position is
	 * constantly that of theRise0.  If that results in circumpolarity,
	 * ignore that exception so that we bail out. */

	theRise1 = theObject.NaiveRiseSet(theTelescope, aElev, findRise);
	while (0. > theRise1.Sub(aTelescope)) {theRise1.Add(1./SIDSOL);}

	/* If the rise correction is less than 0.01 s, break out of loop.
	 * Either way move Rise1 to Rise0. */

	if (0.01 / 3600. / 24 > Math.abs(theRise1.Sub(theRise0))) {
	  theRise0 = theRise1;
	  break;
	}

	theRise0 = theRise1;
      }

      theRise = theRise0;
    }

    /* If the straightforward iteration failed, try the sledge hammer. */

    catch (NamedObjectCircPolException e) {

      /* Initialise loop. */

      theTelescope.Copy(aTelescope);
      theObject.Update(theTelescope);
      theObject.GetHori(0, theTelescope, theTriplet); theH1 = theTriplet[1];

      /* Loop through time in 1 minute steps up to 2000 minutes far.
       * This loop is for finding a rise, not a set. */

      theRise  = null;
      theStart = aTelescope.GetJD();
      theT1    = theStart;

      if (findRise) {
	for (i = 1; i < 2000; i++) {
	  theT0 = theT1;
	  theH0 = theH1;
	  theT1 = theStart + (double) i / 1440.;
	  theTelescope.SetJD(theT1);
	  theObject.Update(theTelescope);
	  theObject.GetHori(0, theTelescope, theTriplet);
	  theH1 = theTriplet[1];
	  if (theH0 <= aElev && theH1 > aElev) {
	    theT = (theT0 * (theH1 - aElev) + theT1 * (aElev - theH0))
		 / (theH1 - theH0);
	    theRise = new Times(); theRise.Init(); theRise.SetJD(theT);
	    break;
	  }
	}
      }
      else {
	for (i = 1; i < 2000; i++) {
	  theT0 = theT1;
	  theH0 = theH1;
	  theT1 = theStart + (double) i / 1440.;
	  theTelescope.SetJD(theT1);
	  theObject.Update(theTelescope);
	  theObject.GetHori(0, theTelescope, theTriplet);
	  theH1 = theTriplet[1];
	  if (theH0 >= aElev && theH1 < aElev) {
	    theT = (theT0 * (theH1 - aElev) + theT1 * (aElev - theH0))
		 / (theH1 - theH0);
	    theRise = new Times(); theRise.Init(); theRise.SetJD(theT);
	    break;
	  }
	}
      }

      /* If the loop completed, throw an exception. */

      if (1999 < i)
	throw new NamedObjectCircPolException("object circumpolar");
    }

    return theRise;
  }


  /**
Display the Comet.

<p>This writes information about the currently stored object to the given
open file.  The format is

<pre>
Observatory: Royal Observatory Edinburgh
   East long.   -3.182500 deg
   Latitude     55.925000 deg
   Altitude           146 m

  UT: 2003-01-01T00:00:00.0 (JD  2452640.500000)
  TT: 2003-01-01T00:01:05.6 (JDE 2452640.500759)
  Ep: 2002.999317616
             GST 06:40:57.0 = 100.237309 deg
             LST 06:28:13.2 =  97.054809 deg

Object: C/2002 X5 (Kudo-Fujikawa)

  coord. system      deg      deg     h  m  s     deg ' "      Gm
  --------------  --------  -------  ----------  ---------  --------
  gal.   lII,bII    55.017   21.123
  B1950  RA,Dec    271.831   28.445  18:07:19.5   28:26:41
  J2000  RA,Dec    272.322   28.455  18:09:17.2   28:27:17
  ecl.   lam,bet   273.348   51.867
  mean   RA,Dec    272.351   28.455  18:09:24.2   28:27:20   146.138
  topo   HA,Dec   -175.296   28.453  12:18:48.9   28:27:11   146.139
  hori   A,h         4.154   -5.526

     q      3.015 deg   parallactic angle
  vrot      0.019 km/s  geocentric radial velocity of topocentre
  vhel      2.247 km/s  heliocentric radial velocity of topocentre
  vLSR     22.235 km/s  LSR radial velocity of topocentre
  vGSR    190.376 km/s  GSR radial velocity of topocentre

   mag      5.8         V magnitude
    El    -52.2 deg     elongation from the Sun
   phi    -64.3 deg     phase angle
     L      0.717       illuminated fraction of the disc
</pre>

<p>This method calls the superclass method to make the output for
NamedObject and only adds the physical ephemeris at the end.  These
will previously have been calculated by the class.

@param aTelescope
  Some of the coordinate transforms require the time or the location of
  the observatory to be known.
  The spatial velocity of the Sun is needed in order to reduce
  the radial velocity of the observatory from geocentric to
  heliocentric.
   */

  public final String Show(Telescope aTelescope)
  {
    return (super.Show(aTelescope)

      + "   mag   "
      + Hmelib.Wfndm(6, 1, itsMag)
      + "         V magnitude"

      + "\n    El   "
      + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * itsElong)
      + " deg     elongation from the Sun"

      + "\n   phi   "
      + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * itsPhase)
      + " deg     phase angle"

      + "\n     L   "
      + Hmelib.Wfndm(8, 3, itsIllum)
      + "       illuminated fraction of the disc\n\n");
  }


  /**
   * Set the Comet for the given time.
   *
   * <p>This calculates for the given time the comet or asteroid's astrometric
   * J2000 coordinates and physical ephemeris.  Integration of the orbit is
   * delegated to Elliptic(), Parabolic() or Hyperbolic().  The first orbit
   * integration yields the geocentric distance and hence the light time.
   * The given time is reduced by this amount and the orbit integration
   * repeated.  Using the same geocentric position of the Sun but this
   * revised heliocentric position of the comet yields the astrometric
   * geocentric position.
   *
   * <p>The elongation and phase angle are calulcated from the triangle formed
   * by the geocentre, the geocentric position of the Sun and the astrometric
   * position of the planet.  The elongation is therefore correct, but the
   * phase angle is not properly corrected for light time effects.  Both are
   * calculated as the spherical distance angle, but then
   * their sign is adjusted according to the three vector product of the
   * vectors to Sun, planet and North Celestial Pole:
   *
   * <p>sgn(El) = sgn((<strong>r</strong><sub>S</sub> &times; <strong>r</strong><sub>P</sub>) <strong>k</strong>)
   * = sgn(y<sub>S</sub> x<sub>P</sub> - x<sub>S</sub> y<sub>P</sub>)
   *
   * <p>sgn(Ph) = sgn(El)
   *
   * <p>Hence a western elongation is negative and the phase angle is then also
   * negative.
   *
   * <p>The illuminated fraction results from the phase angle as
   *
   * <p>L = [1 + cos(Ph)] / 2
   *
   * <p>The elements of the comet or asteroid include two parameters that
   * describe the magnitude and its variation with distance from the Sun or
   * with phase angle.  For an asteroid the given parameters are H and G.
   * Edward Bowell, Bruce Hapke, Deborah Domingue, Kari Lumme, Jouni Peltoniemi, Alan W. Harris, 1989, Application of photometric models to asteroids, in: Richard P. Binzel, Tom Gehrels, Mildred Shapley Matthews (eds.), <I>Asteroids II</I>, University of Arizona Press, Tuscon, p.524ff,
   * give the following expression for the magnitude at 1 au distance from
   * the Earth as well as the Sun
   *
   * <p>&phi;<SUB>1</SUB> = exp[-3.33 tan<SUP>0.63</SUP>(Ph)]
   *
   * <p>&phi;<SUB>2</SUB> = exp[-1.87 tan<SUP>1.22</SUP>(Ph)]
   *
   * <p>V<SUB>0</SUB> = H
   *                  - 2.5 lg [(1-G) &phi;<SUB>1</SUB> G &phi;<SUB>2</SUB>]
   *
   * <p>V = V<SUB>0</SUB> + 5 lg(r/au) + 5 lg(&Delta;/au)
   *
   * <p>For a comet the given parameters are g and k.
   * Jean Meeus, 1991, <I>Astronomical Algorithms</I>, Willmann-Bell, Richmond VA, p.216,
   * gives the expression
   *
   * <p>V = g + 2.5 k lg(r/au) + 5 lg(&Delta;/au)
   *
   * <p>In fact, Meeus writes with with &kappa;&nbsp;=&nbsp;2.5&nbsp;k.
   * The meaning of k as listed in the xephem-format elements has been
   * gleaned from the xephem 3.2.3 source code
   * (Elwood Charles Downey, 1999, <I>XEphem Version 3.2</I>, <A HREF="http://www.ClearSkyInstitute.com/xephem/xephem.html">http://www.ClearSkyInstitute.com/xephem/xephem.html</A>).
   * Hopefully the data from the minor planet people at CfA/Harvard
   * (<A HREF="http://cfa-www.harvard.edu/iau/Ephemerides/Comets/Soft03Cmt.txt">http://cfa-www.harvard.edu/iau/Ephemerides/Comets/Soft03Cmt.txt</A>)
   * are compatible this use.
   *
   * @param aTelescope
   *   Primarily the time for which to calculate the ephemeris.
   *   Also the position of the Sun. */

  public final void Update(Telescope aTelescope)
    throws CometNoConvException
  {
    Times  theTime;
    double theLightTime;
    double YpsDist[]   = new double[2];
    double theSunPos[] = new double[3];
    double aTriplet[]  = new double[3];
    double ra, rb;

    /* Copy the time into our own instance.  This will help with light time.
     * Also get the geocentric position of the Sun. */

    theTime = new Times(); theTime.Init(); theTime.Copy(aTelescope);
    aTelescope.itsSun.GetPos(theSunPos);

    /* Calculate anomaly and heliocentric distance.  With the alternate
     * elements A B C a b c, the rectangular equatorial coordinates are then a
     * piece of cake.  Add the Sun.  Note the conversion from au to Gm. */

    switch (itsType) {
    case 1: Hyperbolic(theTime, YpsDist); break;
    case 2:  Parabolic(theTime, YpsDist); break;
    default:  Elliptic(theTime, YpsDist); break;
    }

    itsR[0] = theSunPos[0] + AU * YpsDist[1] * itsMeeusa
            * Math.sin(itsMeeusA + its_omega + YpsDist[0]);
    itsR[1] = theSunPos[1] + AU * YpsDist[1] * itsMeeusb
            * Math.sin(itsMeeusB + its_omega + YpsDist[0]);
    itsR[2] = theSunPos[2] + AU * YpsDist[1] * itsMeeusc
            * Math.sin(itsMeeusC + its_omega + YpsDist[0]);

    /* The geocentric distance gives the light time, here in days.
     * Go back that far in time and calculate the object position again.
     * Add the Sun (same coordinates as before, in order not to take account of
     * the Earth's velocity). */

    theLightTime  = -Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1]
                             + itsR[2] * itsR[2]) / LIGHTGMD;
    theTime.Add(theLightTime);

    switch (itsType) {
    case 1: Hyperbolic(theTime, YpsDist); break;
    case 2:  Parabolic(theTime, YpsDist); break;
    default:  Elliptic(theTime, YpsDist); break;
    }

    itsR[0] = theSunPos[0] + AU * YpsDist[1] * itsMeeusa
            * Math.sin(itsMeeusA + its_omega + YpsDist[0]);
    itsR[1] = theSunPos[1] + AU * YpsDist[1] * itsMeeusb
            * Math.sin(itsMeeusB + its_omega + YpsDist[0]);
    itsR[2] = theSunPos[2] + AU * YpsDist[1] * itsMeeusc
            * Math.sin(itsMeeusC + its_omega + YpsDist[0]);

    /* Physical ephemeris.
     * Magnitude according to equations 32.14 and 32.13 of
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA,
     * p.216f. */

    aTriplet[0] = itsR[0] - theSunPos[0];
    aTriplet[1] = itsR[1] - theSunPos[1];
    aTriplet[2] = itsR[2] - theSunPos[2];
    itsElong = Hmelib.SpherDist(itsR, theSunPos);
    itsPhase = Hmelib.SpherDist(itsR, aTriplet);
    if (theSunPos[1] * itsR[0] > theSunPos[0] * itsR[1]) {
      itsElong *= -1.; itsPhase *= -1.;
    }
    itsIllum = (1. + Math.cos(itsPhase)) / 2.;


    ra = Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1] + itsR[2] * itsR[2]);
    rb = Math.sqrt(aTriplet[0] * aTriplet[0] + aTriplet[1] * aTriplet[1]
                 + aTriplet[2] * aTriplet[2]);

    if (4 == itsType) { /* Asteroid. */
      itsMag
        = its_H
        - 2.5 * Math.log(
	  (1. - its_G) * Math.exp(
            -3.33 * Math.pow(Math.abs(Math.tan(itsPhase / 2.)), 0.63))
              + its_G  * Math.exp(
            -1.87 * Math.pow(Math.abs(Math.tan(itsPhase / 2.)), 1.22)))
          / Math.log(10.)
        + 5. * Math.log(ra / AU) / Math.log(10.)
        + 5. * Math.log(rb / AU) / Math.log(10.);
    }
    else {              /* Comet.    */
      itsMag = its_g + 5.          * Math.log(ra / AU) / Math.log(10.)
                     + 2.5 * its_k * Math.log(rb / AU) / Math.log(10.);
    }

    return;
  }


  /**
   * Convert semi major axis to mean daily motion.
   *
   * <p>This is listed in
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.214.
   * a is the semi major axis and must be given in au, the mean daily motion
   * is returned in radian per day by this routine.
   *
   * <p>dM/dt = (0.9856076686&#176;/d) / a<SUP>1.5</SUP>
   *
   * @param a_a
   *   The semi major axis in astronomical units (au). */

  protected final double AxisToMdot(double a_a)
  {
    return (0.9856076686 / a_a / Math.sqrt(a_a) / Hmelib.DEGPERRAD);
  }


  /**
   * Convert semi major axis to perihelion distance.
   *
   * <p>This is a well-known formula, but also listed in
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.214.
   * a is the semi major axis, e the eccentricity, q the perihelion distance.
   *
   * <p>q = a (1 - e)
   *
   * @param a_a
   *   The semi major axis in astronomical units (au).
   * @param a_e
   *   The numeric eccentricity of the orbit. */

  protected final double AxisToPeri(double a_a, double a_e)
  {
    return (a_a * (1. - a_e));
  }


  /**
   * Integrate elliptic orbit.
   *
   * <p>Given a time, integrate the elliptic orbit to return the true anomaly
   * in radian and the heliocentric distance in au.  For fast results even for
   * e &gt; 0.5 this does not use the Kepler iteration, but equation 29.7 of
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.187:
   *
   * <p>E<SUB>0</SUB> = M
   * <br>E<SUB>i+1</SUB> = E<SUB>i</SUB>
   *     + [M + e sin(E<SUB>i</SUB>) - E<SUB>i</SUB>]
   *     / [1 - e cos(E<SUB>i</SUB>)]
   *
   * <p>where E is the eccentric anomaly, M the mean anomaly and e the
   * eccentricity.
   *
   * <p>Even so, if |M| &lt; 0.5 and e &gt; 0.975 this routine will devolve
   * calculation to the Hyperbolic method.
   *
   * <p>If the elliptical integration is going ahead, the returned values are
   * calculated according to
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.183:
   *
   * <p>tan(&upsilon;/2) = [(1+e)/(1-e)]<SUP>0.5</SUP> tan(E/2)
   *
   * <p>r = a [1 - e cos(E)]
   *
   * @param aTime
   *   The time for which to integrate the orbit.
   * @param YpsDist
   *   An array of two numbers.  The first is the returned true anomaly in
   *   radian, the second the heliocentric distance in au. */

  protected final void Elliptic(Times aTime, double YpsDist[])
    throws CometNoConvException
  {
    double theTime;
    double the_M;
    double the_E0, the_E;
    int    i;

    YpsDist[0] = 0.; YpsDist[1] = 0.;

    /* Calculate the time since the osculation epoch, then the mean anomaly. */

    theTime = aTime.Sub(its_T0);
    the_M = its_M0 + its_n * theTime;
    the_M = Hmelib.NormAngle0(the_M);

    /* If the eccentricity is nearly parabolic (> 0.975) and if also the
     * mean anomaly is not large (< 0.5 rad by absolute value), then even the
     * better-than-Kepler equation is not very good, and we defer to the
     * Hyperbolic method, which works for any near parabolic orbit. */

    if (0.975 < its_e && 0.5 > Math.abs(the_M)) {
      Hyperbolic(aTime, YpsDist); return;
    }

    /* Find the eccentric anomaly E by iteration of equation 29.7 of
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA,
     * p.187. */

    the_E0 = the_M;
    the_E  = the_E0 + (the_M + its_e * Math.sin(the_E0) - the_E0)
                    / (1. - its_e * Math.cos(the_E0));
    for (i = 0; i < 100 && 1E-9 < Math.abs(the_E - the_E0); i++) {
      the_E0 = the_E;
      the_E  = the_E0 + (the_M + its_e * Math.sin(the_E0) - the_E0)
                         / (1. - its_e * Math.cos(the_E0));
    }
    if (99 < i) {throw new CometNoConvException("no Kepler convergence");}
    the_E = Hmelib.NormAngle0(the_E);

    /* Now we need to use equation 29.1 of
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA, p.183,
     * whereby tan(yps/2) = tan(E/2) * sqrt[(1+e)/(1-e)].  E is in the
     * range [-pi,+pi], E/2 in [-pi/2,+pi/2], its tangens therefore defined.
     * The square root greater than 1, so it could be that near E = +-pi this
     * pushes the numbers above the allowed range for doubles.  If so, set yps
     * to pi.  Otherwise the atan can be taken without problem. */

    YpsDist[0] = 2. * Math.atan(Math.sqrt((1. + its_e)/(1. - its_e))
                              * Math.tan(the_E / 2.));

    /* For distance we use the first variant (29.2) of
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA,
     * p.183. */

    YpsDist[1] = its_a * (1. - its_e * Math.cos(the_E));

    return;
  }


  /**
   * Integrate hyperbolic orbit.
   *
   * <p>Given a time, integrate the hyperbolic orbit to return the true anomaly
   * in radian and the heliocentric distance in au.  Following
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.229ff,
   * this uses Landgraf's programme
   * (Werner Landgraf, 1987, <em>Sky and Telescope</em>, <strong>73</strong>, p.535f)
   * translated to C++ then to Java.
   *
   * @param aTime
   *   The time for which to integrate the orbit.
   * @param YpsDist
   *   An array of two numbers.  The first is the returned true anomaly in
   *   radian, the second the heliocentric distance in au. */

  protected final void Hyperbolic(Times aTime, double YpsDist[])
    throws CometNoConvException
  {
    double theTime;
    double the_Q1;
    double the_Q2;
    double the_Q3;
    double the_gamma;
    double the_gamma1;
    double the_s;
    double the_s0;
    double the_s1;
    double y, f, z1;
    int    l, z;

    YpsDist[0] = 0.; YpsDist[1] = 0.;

    /* No check that the orbit is hyperbolic.  In fact this routine is to be
     * used for the perihelion region of elliptical orbits that are almost
     * parabolic.  It also can be used for parabolic orbits. */

    /* Calculate the time since perihelion.  We take into account its_M0 and
     * its_n.  For non-elliptical orbits these are set zero.  For
     * elliptical orbits it is vital to take them into account. */

    theTime = aTime.Sub(its_T0);
    if (1. > its_e && 1E-9 < Math.abs(its_M0)) {
      if (1E-9 > its_n) {
	throw new CometNoConvException("invalid daily motion");
      }
      theTime -= its_M0 / its_n;
    }

    /* The rest is reverse engineered from Landgraf's BASIC programme as in
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA, p.230.
     */

    if (1E-9 > Math.abs(theTime)) {
      YpsDist[0] = 0.; YpsDist[1] = its_q; return;
    }

    the_Q1 = GAUSSK * Math.sqrt((1. + its_e) / its_q) / (2. * its_q);
    the_gamma = (1. - its_e) / (1 + its_e);

    the_Q2 = the_Q1 * theTime;
    the_s  = 2. / (3. * Math.abs(the_Q2));
    the_s  = 2. / Math.tan(2.
           * Math.atan(Math.pow(Math.tan(Math.atan(the_s) / 2.), 1./3.)));
    if (0. > theTime) {the_s *= -1.;}

    if (1E-9 < Math.abs(1. - its_e)) {
      for (l = 0, the_s0 = the_s;
	   l == 0 || 1E-9 < Math.abs(the_s - the_s0); l++) {
	the_s0 = the_s;
	y = the_s * the_s;
	the_gamma1 = -y * the_s;
	the_Q3 = the_Q2 + 2. * the_gamma * the_s * y / 3.;
	for (z = 1, f = 1.; z == 1 || 1E-9 < Math.abs(f); ) {
	  z++;
	  the_gamma1 *= -1. * the_gamma * y;
	  z1 = ((double)z - (double)(z + 1.) * the_gamma)
             / (double)(2 * z + 1);
	  f = z1 * the_gamma1;
	  the_Q3 += f;
	  if (50 < z || 1E4 < Math.abs(f)) {
	    throw new CometNoConvException("no Landgraf convergence");
	  }
	}
	if (49 < l) {
	  throw new CometNoConvException("no Landgraf convergence");
	}
	the_s1 = the_s;
	the_s = (2. * the_s * the_s * the_s / 3. + the_Q3)
              / (the_s * the_s + 1.);
	for ( ; 1E-9 < Math.abs(the_s - the_s1); ) {
	  the_s1 = the_s;
	  the_s = (2. * the_s * the_s * the_s / 3. + the_Q3)
                / (the_s * the_s + 1.);
	}
      }
    }

    YpsDist[0]  = 2. * Math.atan(the_s);
    YpsDist[1] = its_q * (1. + its_e) / (1. + its_e * Math.cos(YpsDist[0]));

    return;
  }


  /**
   * Integrate parabolic orbit.
   *
   * <p>Given a time, integrate the parabolic orbit to return the true anomaly
   * in radian and the heliocentric distance in au.  Following
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.225ff,
   * this solves the Baker equation by iteration of Meeus equation 33.4:
   *
   * <p>W = 0.03649116245 (t - T) / q<SUP>1.5</SUP>
   *
   * <p>s<SUB>0</SUB> = 0
   * <br>s<SUB>i+1</SUB> = [2 s<SUB>i</SUB><SUP>3</SUP> + W]
   *     / [3 (s<SUB>i</SUB><SUP>2</SUP> + 1)];
   *
   * <p>where q is the perihelion distance in au, t the time and T the time of
   * perihelion.  The constant is 3k/2<SUP>0.5</SUP> where k is the
   * Gau&szlig; gravitational constant 0.01720209895.  The returned values
   * are
   *
   * <p>tan(&upsilon;/2) = s
   *
   * <p>r = q [1 + s<SUP>2</SUP>]
   *
   * @param aTime
   *   The time for which to integrate the orbit.
   * @param YpsDist
   *   An array of two numbers.  The first is the returned true anomaly in
   *   radian, the second the heliocentric distance in au. */

  protected final void Parabolic(Times aTime, double YpsDist[])
    throws CometNoConvException
  {
    double theTime;
    double the_W;
    double the_s0;
    double the_s;
    int    i;

    YpsDist[0] = 0.; YpsDist[1] = 0.;

    /* Calculate the time since perihelion. */

    theTime = aTime.Sub(its_T0);

    /* Equations 33.1 and 33.4 of
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA,
     * p.225f to solve Baker's equation.  The constant here is 3 k / sqrt(2)
     * with k = 0.01720209895 Gauss' gravitational constant. */

    the_W  = 0.03649116245 * theTime / its_q / Math.sqrt(its_q);
    the_s0 = 0.;
    the_s  = (2. * the_s0 * the_s0 * the_s0 + the_W)
           / (the_s0 * the_s0 + 1.) / 3.;
    for (i = 0; i < 100 && 1E-9 < Math.abs(the_s - the_s0); i++) {
      the_s0 = the_s;
      the_s  = (2. * the_s0 * the_s0 * the_s0 + the_W)
             / (the_s0 * the_s0 + 1.) / 3.;
    }
    if (99 < i) {
      throw new CometNoConvException("no Baker convergence");
    }

    /* Equations 33.2 and 33.3 of
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA,
     * p.225f. */

    YpsDist[0] = 2. * Math.atan(the_s);
    YpsDist[1] = its_q * (the_s * the_s + 1.);

    return;
  }


  /**
   * Read orbital elements of a comet or asteroid from file.
   *
   * <p>Given a file name and the name of a comet or asteroid, scan the
   * xephem-format file for the body of that name.  Read the orbital elements
   * and close the file.
   *
   * <p>The file format differs for the three shapes of orbit (elliptic,
   * parabolic, hyperbolic).  For elliptic orbits the format also differs
   * between comets and asteroids due to the different parametrisation of
   * their brightness.  The files have no leading space, but may contain
   * comment lines as indicated by a hash (#) in the first column.
   * Otherwise, one line contains all data for one object.  The data field
   * separator is the comma (,), except that there is an American-style date
   * where there are two slashes (/) as field separators.  Here is an example
   * line for type 1, hyperbolic comet:
   *
   * <p><table border>
   *   <col><col><col>
   *   <tr>
   *     <th scope="col">#</th>
   *     <th scope="col">example</th>
   *     <th scope="col">meaning</th>
   *   </tr><tr align="center">
   *     <td>1</td><td>C/1997 J2 (Meunier-Dupouy),</td><td>name</td>
   *   </tr><tr align="center">
   *     <td>2</td><td>h,</td><td>type of orbit</td>
   *   </tr><tr align="center">
   *     <td>3</td><td>03/</td><td>month of perihelion</td>
   *   </tr><tr align="center">
   *     <td>4</td><td>10.3032/</td><td>day of perihelion</td>
   *   </tr><tr align="center">
   *     <td>5</td><td>1998,</td><td>year of perihelion</td>
   *   </tr><tr align="center">
   *     <td>6</td><td>91.2814,</td><td>inclination in &deg;</td>
   *   </tr><tr align="center">
   *     <td>7</td><td>148.9101,</td><td>longitude of asc. node in &deg;</td>
   *   </tr><tr align="center">
   *     <td>8</td><td>122.6897,</td><td>argument of perihelion in &deg;</td>
   *   </tr><tr align="center">
   *     <td>9</td><td>1.000605,</td><td>eccentricity, &gt; 1.0</td>
   *   </tr><tr align="center">
   *     <td>10</td><td>3.051664,</td><td>perihelion distance in au</td>
   *   </tr><tr align="center">
   *     <td>11</td><td>2000,</td><td>ecliptic and equinox</td>
   *   </tr><tr align="center">
   *     <td>12</td><td>3.5,</td><td>magnitude parameter g</td>
   *   </tr><tr align="center">
   *     <td>13</td><td>4.0</td><td>magnitude parameter k</td>
   *   </tr>
   * </table>
   *
   * <p>Here is an example line for type 2, parabolic comet:
   *
   * <p><table border>
   *   <col><col><col>
   *   <tr>
   *     <th scope="col">#</th>
   *     <th scope="col">example</th>
   *     <th scope="col">meaning</th>
   *   </tr><tr align="center">
   *     <td>1</td><td>C/2002 F1 (Utsunomiya),</td><td>name</td>
   *   </tr><tr align="center">
   *     <td>2</td><td>p,</td><td>type of orbit</td>
   *   </tr><tr align="center">
   *     <td>3</td><td>04/</td><td>month of perihelion</td>
   *   </tr><tr align="center">
   *     <td>4</td><td>22.8953/</td><td>day of perihelion</td>
   *   </tr><tr align="center">
   *     <td>5</td><td>2002,</td><td>year of perihelion</td>
   *   </tr><tr align="center">
   *     <td>6</td><td>80.8695,</td><td>inclination in &deg;</td>
   *   </tr><tr align="center">
   *     <td>7</td><td>125.8826,</td><td>argument of perihelion in &deg;</td>
   *   </tr><tr align="center">
   *     <td>8</td><td>0.438381,</td><td>perihelion distance in au</td>
   *   </tr><tr align="center">
   *     <td>9</td><td>289.0304,</td><td>longitude of asc. node in &deg;</td>
   *   </tr><tr align="center">
   *     <td>10</td><td>2000,</td><td>ecliptic and equinox</td>
   *   </tr><tr align="center">
   *     <td>11</td><td>8.5,</td><td>magnitude parameter g</td>
   *   </tr><tr align="center">
   *     <td>12</td><td>4.0</td><td>magnitude parameter k</td>
   *   </tr>
   * </table>
   *
   * <p>The eccentricity is not listed, because it is equal to 1 for a parabola
   * and not a variable.  For elliptic orbits the formats are more complex:
   * Because the parametrisation of magnitude differs between comets and
   * asteroids, this distinction must be detectable when reading the data.
   * The first magnitude parameter may begin with a "g", which indicates that
   * we have a comet, here a type 3.  It may begin with an "H", which
   * indicates that we have an asteroid and the data are from MPEC.  If there
   * is no indicator, we have an asteroid and the data are as distributed
   * with XEphem 3.2.3.  Here is an example line for type 3, elliptic comet:
   *
   * <p><table border>
   *   <col><col><col>
   *   <tr>
   *     <th scope="col">#</th>
   *     <th scope="col">example</th>
   *     <th scope="col">meaning</th>
   *   </tr><tr align="center">
   *     <td>1</td><td>36P/Whipple,</td><td>name</td>
   *   </tr><tr align="center">
   *     <td>2</td><td>e,</td><td>type of orbit</td>
   *   </tr><tr align="center">
   *     <td>3</td><td>9.9298,</td><td>inclination in &deg;</td>
   *   </tr><tr align="center">
   *     <td>4</td><td>182.4160,</td><td>longitude of asc. node in &deg;</td>
   *   </tr><tr align="center">
   *     <td>5</td><td>202.1551,</td><td>argument of perihelion in &deg;</td>
   *   </tr><tr align="center">
   *     <td>6</td><td>4.171249,</td><td>semi major axis in au</td>
   *   </tr><tr align="center">
   *     <td>7</td><td>0.1156924,</td><td>mean daily motion in &deg;/d</td>
   *   </tr><tr align="center">
   *     <td>8</td><td>0.25968617,</td><td>eccentricity, &lt; 1.0</td>
   *   </tr><tr align="center">
   *     <td>9</td><td>287.5395,</td><td>mean anomaly at ref. date in &deg;</td>
   *   </tr><tr align="center">
   *     <td>10</td><td>10/</td><td>month of reference date</td>
   *   </tr><tr align="center">
   *     <td>11</td><td>18.0/</td><td>day of reference date</td>
   *   </tr><tr align="center">
   *     <td>12</td><td>2001,</td><td>year of reference date</td>
   *   </tr><tr align="center">
   *     <td>13</td><td>2000,</td><td>ecliptic and equinox</td>
   *   </tr><tr align="center">
   *     <td>14</td><td>g 8.5,</td><td>cometary magnitude parameter g</td>
   *   </tr><tr align="center">
   *     <td>12</td><td>6.0</td><td>cometary magnitude parameter k</td>
   *   </tr>
   * </table>
   *
   * <p>Here, finally, is an example line for type 4, elliptic asteroid.  The
   * "H" marker in field #14 is optional.
   *
   * <p><table border>
   *   <col><col><col>
   *   <tr>
   *     <th scope="col">#</th>
   *     <th scope="col">example</th>
   *     <th scope="col">meaning</th>
   *   </tr><tr align="center">
   *     <td>1</td><td>43 Ariadne,</td><td>name</td>
   *   </tr><tr align="center">
   *     <td>2</td><td>e,</td><td>type of orbit</td>
   *   </tr><tr align="center">
   *     <td>3</td><td>3.4700,</td><td>inclination in &deg;</td>
   *   </tr><tr align="center">
   *     <td>4</td><td>265.0258,</td><td>longitude of asc. node in &deg;</td>
   *   </tr><tr align="center">
   *     <td>5</td><td>15.7076,</td><td>argument of perihelion in &deg;</td>
   *   </tr><tr align="center">
   *     <td>6</td><td>2.202810,</td><td>semi major axis in au</td>
   *   </tr><tr align="center">
   *     <td>7</td><td>0.3014662,</td><td>mean daily motion in &deg;/d</td>
   *   </tr><tr align="center">
   *     <td>8</td><td>0.168693,</td><td>eccentricity, &lt; 1.0</td>
   *   </tr><tr align="center">
   *     <td>9</td><td>70.0031,</td><td>mean anomaly at ref. date in &deg;</td>
   *   </tr><tr align="center">
   *     <td>10</td><td>05/</td><td>month of reference date</td>
   *   </tr><tr align="center">
   *     <td>11</td><td>06.0/</td><td>day of reference date</td>
   *   </tr><tr align="center">
   *     <td>12</td><td>2002,</td><td>year of reference date</td>
   *   </tr><tr align="center">
   *     <td>13</td><td>2000,</td><td>ecliptic and equinox</td>
   *   </tr><tr align="center" VALIGN="TOP">
   *     <td rowspan="2">14</td><td>H 7.93,</td>
   *     <td rowspan="2">asteroidal magnitude parameter H</td>
   *   </tr><tr align="center">
   *     <td>7.93,</td>
   *   </tr><tr align="center">
   *     <td>15</td><td>0.11</td><td>asteroidal magnitude parameter G</td>
   *   </tr>
   * </table>
   *
   * <p>The ecliptic and equinox must always be 2000.  This is what you get
   * from the
   * Minor Planet Electronic Circular web site (<A HREF="http://cfa-www.harvard.edu/iau/Ephemerides/">http://cfa-www.harvard.edu/iau/</A>).
   * but also what you get with
   * Elwood Charles Downey, 1999, <I>XEphem Version 3.2</I>, <A HREF="http://www.ClearSkyInstitute.com/xephem/xephem.html">http://www.ClearSkyInstitute.com/xephem/xephem.html</A>.
   *
   * @param aFileName
   *   The name of the file with the xephem-format orbit data.
   * @param aName
   *   The name of the comet or asteroid, i.e. the first data field in the
   *   file.  The file is searched until a line starts with this string.  So
   *   the same object cannot be stored twice in the same file.  And if you
   *   give only the beginning of the name you will pick up the first object
   *   that matches. */

  protected final void ReadByName(String aFileName, String aName)
    throws CometNotFoundException, CometInvLineException,
	   TimesException, HmelibException, IOException
  {
    BufferedReader theFile;
    String  theString;
    Times   theTEqui = new Times();
    boolean success = false;
    double  theEps;
    double  theF, theG, theH, theP, theQ, theR;
    double  y1, y2, m ,d;
    int     n;

    /* Open the file. */

    theFile = new BufferedReader(new FileReader(aFileName));

    /* Loop through file. */

    for (;;) {

      /* Read line.  Any error is reported and ignored.  An EOF is
       * signalled by returning null.  In that case we break the loop. */

      if ((theString = theFile.readLine()) == null) break;

      /* Trim white space off the start and end.
       * If we have a comment, ignore it. */

      theString = theString.trim();
      if (theString.startsWith("#")) {continue;}

      /* If the object names match, break out. */

      if (theString.startsWith(aName)) {
        success = true;
	break;
      }
    }

    /* Close the file. */

    theFile.close();

    /* Report if object not found. */

    if (!success) throw new CometNotFoundException("no such object");

    /* Read the name. */

    n = theString.indexOf(',');
    if (0 > n) throw new CometInvLineException("invalid line");
    itsName = theString.substring(0, n);

    /* Read the type.
     * If 'e' we need to distinguish comet and asteroid from a marker in the
     * first magnitude field.  The way MPEC distribute xephem format, an
     * asteroid has 'H' to mark H and G parameters, while a comet has 'g' to
     * mark g and k parameters.  xephem 3.2.3 itself has only the 'g' but
     * nothing for asteroids.  Another difference between MPEC and xephem is
     * that MPEC give us the correct mean motion for asteroids, whereas xephem
     * 3.2.3's own files have these as zero. */

    switch(theString.charAt(n+1)) {
    case 'h': itsType = 1; break;
    case 'p': itsType = 2; break;
    case 'e':
      if (-1 < (theString.substring(n+1)).indexOf('g'))
	itsType = 3;
      else
	itsType = 4;
      break;
    default:
      throw new CometInvLineException("invalid line");
    }

    /* Make theString start with first type-specific element (behind type
     * itself). */

    theString = theString.substring(n+3);

    /* Read the rest according to type. */

    if (1 == itsType) { /* hyperbolic comet */

      m = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      d = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y1 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_i = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_OMEGA = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_omega = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_e = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_q = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y2 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_g = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_k = Hmelib.Rfndm(theString);

      its_a = 0.; its_n = 0.; its_M0 = 0.; its_H = 0.; its_G = 0.;

    }

    else if (2 == itsType) { /* parabolic comet */

      m = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      d = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y1 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_i = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_omega = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_q = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_OMEGA = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y2 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_g = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_k = Hmelib.Rfndm(theString);

      its_e = 1.; its_a = 0.; its_n = 0.; its_M0 = 0.; its_H = 0.; its_G = 0.;

    }

    else if (3 == itsType) { /* elliptic comet */

      its_i = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_OMEGA = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_omega = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_a = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_n = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_e = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_M0 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      m = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      d = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y1 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y2 = Hmelib.Rfndm(theString);

      n = theString.indexOf('g');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_g = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_k = Hmelib.Rfndm(theString);

      its_H = 0.; its_G = 0.;
      its_q = AxisToPeri(its_a, its_e);
      if (0. >= its_n) {its_n = AxisToMdot(its_a);}
      else             {its_n /= Hmelib.DEGPERRAD;}

    }

    else if (4 == itsType) { /* asteroid */

      its_i = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_OMEGA = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_omega = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_a = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_n = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_e = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_M0 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      m = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      d = Hmelib.Rfndm(theString);

      n = theString.indexOf('/');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y1 = Hmelib.Rfndm(theString);

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      y2 = Hmelib.Rfndm(theString);

      if (-1 < theString.indexOf('H')) { /* MPEC xephem format. */
	n = theString.indexOf('H');
        if (0 > n) throw new CometInvLineException("invalid line");
	theString = theString.substring(n+1);
	its_H = Hmelib.Rfndm(theString);
      }
      else { /* xephem 3.2.3 distributed format. */
	n = theString.indexOf(',');
        if (0 > n) throw new CometInvLineException("invalid line");
	theString = theString.substring(n+1);
	its_H = Hmelib.Rfndm(theString);
      }

      n = theString.indexOf(',');
      if (0 > n) throw new CometInvLineException("invalid line");
      theString = theString.substring(n+1);
      its_G = Hmelib.Rfndm(theString);

      its_g = 0.; its_k = 0.;
      its_q = AxisToPeri(its_a, its_e);
      if (0. >= its_n) {its_n = AxisToMdot(its_a);}
      else             {its_n /= Hmelib.DEGPERRAD;}

    }

    else {
      throw new CometInvLineException("invalid line");
    }

    /* It seems J2000.0 is always used in orbital data, both from xephem itself
     * and from MPEC.  So we only have to check this here, and don't have to
     * precess at the end. */

    if (1E-9 < Math.abs(2000. - y2)) {
      throw new CometInvLineException("equinox not J2000");
    }

    /* Convert the given American calendar date to Times. */

    its_T0.SetTT((int)Math.floor(y1), (int)Math.floor(m), (int)Math.floor(d),
      24.*(d-Math.floor(d)), 0., 0.);

    /* Convert angle elements to rad. */

    its_omega /= Hmelib.DEGPERRAD;
    its_i     /= Hmelib.DEGPERRAD;
    its_OMEGA /= Hmelib.DEGPERRAD;
    its_M0    /= Hmelib.DEGPERRAD;

    /* Calculate the alternate elements A B C a b c.
     * This is from
     * Jean Meeus, 1991, Astronomical algorithms, Willmann-Bell, Richmond VA,
     * p.214f.
     * Since these elements yield equatorial coordinates, the obliquity of the
     * ecliptic is involved. */

    theTEqui.SetTT(2000, 1, 1, 12., 0., 0.);
    theEps = Obliquity(theTEqui);

    theF =  Math.cos(its_OMEGA);
    theG =  Math.sin(its_OMEGA) * Math.cos(theEps);
    theH =  Math.sin(its_OMEGA) * Math.sin(theEps);
    theP = -Math.sin(its_OMEGA) * Math.cos(its_i);
    theQ =  Math.cos(its_OMEGA) * Math.cos(its_i) * Math.cos(theEps)
	 -                        Math.sin(its_i) * Math.sin(theEps);
    theR =  Math.cos(its_OMEGA) * Math.cos(its_i) * Math.sin(theEps)
	 +                        Math.sin(its_i) * Math.cos(theEps);
    itsMeeusA = Math.atan2(theF, theP);
    itsMeeusB = Math.atan2(theG, theQ);
    itsMeeusC = Math.atan2(theH, theR);
    itsMeeusa = Math.sqrt(theF * theF + theP * theP);
    itsMeeusb = Math.sqrt(theG * theG + theQ * theQ);
    itsMeeusc = Math.sqrt(theH * theH + theR * theR);

    return;
  }

}
