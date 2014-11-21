
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Venus</code> class extends the {@link VSOP87
VSOP87} and holds information needed for the planet Venus.  Most notably
these are the coefficients of the polynomials in time for the planet's
heliocentric longitude, latitude and distance.  The coefficients are
A<sub>kji</sub>, B<sub>kji</sub> and C<sub>kji</sub>, where k is L, B or
R, j is between 0 and 5 and i between 1 and up to 38.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in
the series, the precision of calculated longitudes, latitudes and
distances should be on the order of 2.1" (0.0011&nbsp;Gm), 1.3"
(0.0007&nbsp;Gm) and 0.001&nbsp;Gm, resp.  It is therefore not necessary
to apply the correction from the VSOP dynamical ecliptic and equinox to
FK5, as this amounts to less than 0.1".</p>

<p>Copyright: &copy; 2003-2009 Horst Meyerdierks.</p>

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
<dt><strong>2003-06-07:</strong> hme</dt>
<dd>New class.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2003-09-18:</strong> hme</dt>
<dd>Add NextRiseSet() method.</dd>
<dt><strong>2004-01-03:</strong> hme</dt>
<dd>Fix bug whereby light time correction was omitted.</dd>
<dt><strong>2005-12-27:</strong> hme</dt>
<dd>Change handling of physical ephemeris.  Before, they were geocentric,
  J2000, caclulated by Update calling SetPhysics, and stored in class
  fields.  Now they are calculated by GetPhysics when needed, and they
  are topocentric EOD.</dd>
<dt><strong>2005-12-28:</strong> hme</dt>
<dd>Version 2.1.3.</dd>
<dt><strong>2009-01-26:</strong> hme</dt>
<dd>Extend the octet array to be a decatet, so that rotation systems I,
  II and III can be accommodated.</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Revert to using geocentric J2000 in GetPhysics.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */

public class Venus extends VSOP87
{

  /** VSOP87 longitude terms for Venus, 0th order in time. */

  protected static final double itsL0[] = {
          106., 1.537,       801.821,
          128., 0.962,      5661.332,
          128., 4.226,        20.775,
          155., 5.570,     19651.048,
          180., 4.653,      1109.379,
          232., 3.163,      9153.904,
          326., 4.591,     10404.734,
          327., 5.677,      5507.553,
          429., 3.586,     19367.189,
          500., 4.123,     15720.839,
          585., 3.998,       191.448,
          708., 1.065,       775.523,
          761., 1.950,       529.691,
          769., 0.816,      9437.763,
         1201., 6.1536,    30639.8566,
         1317., 5.1867,       26.2983,
         1438., 4.1575,     9683.5946,
         1664., 4.2502,     1577.3435,
         2372., 2.9938,     3930.2097,
         3456., 2.6996,    11790.6291,
         5477., 4.4163,     7860.4194,
        89892., 5.30650,   20426.57109,
      1353968., 5.5931332, 10213.2855462,
    317614667., 0.,            0.,
            0., 0.,            0.
  };  /* 24 terms, error < 2.1" or 0.0011 Gm. */


  /** VSOP87 longitude terms for Venus, 1st order in time. */

  protected static final double itsL1[] = {
               25., 6.11,    10404.73,
               30., 1.25,     5507.55,
               38., 1.03,      529.69,
               52., 3.60,      775.52,
               70., 2.68,     9437.76,
               82., 5.70,      191.45,
              152., 6.106,    1577.344,
              174., 2.655,      26.298,
              213., 1.795,   30639.857,
            14445., 0.51625, 20426.57109,
            95708., 2.46424, 10213.28555,
    1021352943053., 0.,          0.,
                0., 0.,          0.
  };


  /** VSOP87 longitude terms for Venus, 2nd order in time. */

  protected static final double itsL2[] = {
        6., 1.00,     191.45,
        7., 1.52,    1577.34,
       10., 3.97,     775.52,
       19., 3.54,   30639.86,
       24., 2.05,      26.30,
     1338., 2.0201, 20426.5711,
     3891., 0.3451, 10213.2855,
    54127., 0.,         0.,
        0., 0.,         0.
  };


  /** VSOP87 longitude terms for Venus, 3rd order in time. */

  protected static final double itsL3[] = {
     26., 0.,        0.,
     78., 3.67,  20426.57,
    136., 4.804, 10213.286,
      0., 0.,        0.
  };


  /** VSOP87 longitude terms for Venus, 4th order in time. */

  protected static final double itsL4[] = {
      2., 2.51, 10213.29,
      3., 5.21, 20426.57,
    114., 3.142,    0.,
      0., 0.,       0.
  };


  /** VSOP87 longitude terms for Venus, 5th order in time. */

  protected static final double itsL5[] = {
    1., 3.14, 0.,
    0., 0.,   0.
  };


  /** VSOP87 latitude terms for Venus, 0th order in time. */

  protected static final double itsB0[] = {
        108., 4.539,     22003.915,
        120., 3.705,      2352.866,
        130., 3.672,      9437.763,
        138., 0.860,      1577.344,
        149., 6.254,     18073.705,
       1011., 1.0895,    30639.8566,
      32815., 3.14159,       0.,
      40108., 1.14737,   20426.57109,
    5923638., 0.2670278, 10213.2855462,
          0., 0.,            0.
  };  /* 9 terms, error < 1.3" or 0.0007 Gm. */


  /** VSOP87 latitude terms for Venus, 1st order in time. */

  protected static final double itsB1[] = {
       197., 2.530,    30639.857,
       199., 0.,           0.,
      4380., 3.3862,   20426.5711,
    513348., 1.803643, 10213.285546,
         0., 0.,           0.
  };


  /** VSOP87 latitude terms for Venus, 2nd order in time. */

  protected static final double itsB2[] = {
       27., 3.87,   30639.86,
      173., 5.256,  20426.571,
      282., 0.,         0.,
    22378., 3.3851, 10213.28555,
        0., 0.,         0.
  };


  /** VSOP87 latitude terms for Venus, 3rd order in time. */

  protected static final double itsB3[] = {
      3., 5.44,  30639.86,
      6., 0.77,  20426.57,
     20., 3.14,      0.,
    647., 4.992, 10213.286,
      0., 0.,        0.
  };


  /** VSOP87 latitude terms for Venus, 4th order in time. */

  protected static final double itsB4[] = {
    14., 0.32, 10213.29,
     0., 0.,       0.
  };


  /** VSOP87 latitude terms for Venus, 5th order in time. */

  protected static final double itsB5[] = {0., 0., 0.};


  /** VSOP87 distance terms for Venus, 0th order in time. */

  protected static final double itsR0[] = {
         119., 3.020,    10404.734,
         126., 2.728,     1577.344,
         222., 2.013,    19367.189,
         237., 2.551,    15720.839,
         264., 5.529,     9437.763,
         374., 1.423,     3930.210,
         498., 2.587,     9683.595,
        1378., 1.1285,   11790.6291,
        1632., 2.8455,    7860.4194,
        1658., 4.9021,   20426.5711,
      489824., 4.021518, 10213.285546,
    72334821., 0.,           0.,
           0., 0.,           0.
  };  /* 12 terms, error < 0.001 Gm. */


  /** VSOP87 distance terms for Venus, 1st order in time. */

  protected static final double itsR1[] = {
      234., 3.142,       0.,
      234., 1.772,   20426.571,
    34551., 0.89199, 10213.28555,
        0., 0.,          0.
  };


  /** VSOP87 distance terms for Venus, 2nd order in time. */

  protected static final double itsR2[] = {
      13., 0.,         0.,
      16., 5.47,   20426.57,
    1407., 5.0637, 10213.2855,
       0., 0.,         0.
  };


  /** VSOP87 distance terms for Venus, 3rd order in time. */

  protected static final double itsR3[] = {
    50., 3.22, 10213.29,
     0., 0.,       0.
  };


  /** VSOP87 distance terms for Venus, 4th order in time. */

  protected static final double itsR4[] = {
    1., 0.92, 10213.29,
    0., 0.,       0.
  };


  /** VSOP87 distance terms for Venus, 5th order in time. */

  protected static final double itsR5[] = {0., 0., 0.};


  /**
   * Initialise.
   *
   * <p>This initialises the Venus object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal
   * equinox).</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for Venus";
  }


  /**
   * Return the times of the next rise and set.
   *
   * <p>See {@link Moon#NextRiseSet Moon.NextRiseSet}, of which this method
   * is a copy.</p>
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
    throws NamedObjectCircPolException
  {
    Telescope theTelescope;
    double    theTriplet[] = new double[3];
    double    theStart, theH0, theH1;
    double    theT, theT0, theT1;
    Times     theRise, theRise0, theRise1;
    int       i;

    /* Clone this instance of this class into one we can merrily update to
     * different times.  This can be done by new, Init and Update to the same
     * time.  We leave out the Update, since that will be called later
     * anyway.  These two lines are the only ones to change when copying this
     * method to another subclass of NamedObject.  Only subclasses with an
     * Update method are liable to have this method. */

    Venus     theObject;
    theObject = new Venus();
    theObject.Init();

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
   * Set Venus for the given time.
   *
   * <p>This calculates for the given time Venus's J2000 coordinates.</p>
   *
   * @param aTelescope
   *   Primarily the time for which to calculate the ephemeris.
   *   Also the position of the Sun. */

  public final void Update(Telescope aTelescope)
  {
    Times  theTime;
    double theLightTime;
    double theSunPos[] = new double[3];
    int i;

    itsName = "Venus";

    theTime = new Times(); theTime.Init(); theTime.Copy(aTelescope);
    aTelescope.itsSun.GetPos(theSunPos);
    GetHelio(aTelescope, itsR,
      itsL0, itsL1, itsL2, itsL3, itsL4, itsL5,
      itsB0, itsB1, itsB2, itsB3, itsB4, itsB5,
      itsR0, itsR1, itsR2, itsR3, itsR4, itsR5);
    for (i = 0; i < 3; i++) {itsR[i] += theSunPos[i];}

    /* Calculate the light time, go back in time that far, re-calculate
     * the planet but not the Sun. */

    theLightTime  = -Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1]
                             + itsR[2] * itsR[2]) / LIGHTGMD;
    theTime.Add(theLightTime);
    GetHelio(theTime, itsR,
      itsL0, itsL1, itsL2, itsL3, itsL4, itsL5,
      itsB0, itsB1, itsB2, itsB3, itsB4, itsB5,
      itsR0, itsR1, itsR2, itsR3, itsR4, itsR5);
    for (i = 0; i < 3; i++) {itsR[i] += theSunPos[i];}

    return;
  }


  /**
   * Return geocentric physical ephemeris.
   *
   * <p>These comprise the elongation from the Sun, the phase angle, the
   * illuminated fraction of the disc, the magnitude, the apparent radius, the
   * inclination and position angle of the rotation axis, and the central
   * meridian.</p>
   *
   * <p>The elongation and phase angle are calulcated from the triangle formed
   * by the geocentre, the geocentric position of the Sun and the geocentric
   * position of the planet.  The elongation is therefore correct, but the
   * phase angle is not properly corrected for light time effects.  Both are
   * calculated as the spherical distance angle, but then
   * their sign is adjusted according to the three vector product of the
   * vectors to Sun, planet and North Celestial Pole:</p>
   *
   * <p>sgn(El) = sgn((<strong>r</strong><sub>S</sub> &times; <strong>r</strong><sub>P</sub>) <strong>k</strong>)
   * = sgn(y<sub>S</sub> x<sub>P</sub> - x<sub>S</sub> y<sub>P</sub>)</p>
   *
   * <p>sgn(Ph) = sgn(El)</p>
   *
   * <p>Hence a western elongation is negative and the phase angle is then also
   * negative.</p>
   *
   * <p>The illuminated fraction results from the phase angle as</p>
   *
   * <p>L = [1 + cos(Ph)] / 2</p>
   *
   * <p>The apparent radius is calulated from the equatorial radius from
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E88,
   * and the geocentric distance:</p>
   *
   * <p>&rho; = asin(6052 km/r)</p>
   *
   * <p>To calcluate the orientation of the planet and its state of rotation we
   * use the expressions of
   * M.E. Davies, V.K. Abalakin, M. Bursa, J.H. Lieske, B.Morando, D. Morrison, P.K. Seidelmann, A.T. Sinclair, B. Yallop, Y.S. Tjuflin, 1996, Report of the IAU/IAG/COSPAR working group on cartographic coordinates and rotational elements of the planets and satellites: 1994, <em>Celestial Mechanics and Dynamical Astronomy</em>, <strong>63</strong>, p.127ff,
   * for the celestial coordinates of the planet's pole of rotation and the
   * prime meridian W:</p>
   * 
   * <p>t = JDE - 2451545 d</p>
   * 
   * <p>T = t / 36525 d</p>
   *
   * <p>&alpha;<sub>1</sub> = 272.76&deg;
   * <br />&delta;<sub>1</sub> = 67.16&deg;
   * <br />W = 160.20&deg; - 1.4813688&deg;/d (t - r/c)</p>
   *
   * <p>According to
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E87
   * we calculate first W for the time in question, corrected for light time
   * effects.  The inclination of the axis then is</p>
   *
   * <p>sin(i) = -sin(&delta;<sub>1</sub>) sin(&delta;)
   *           -  cos(&delta;<sub>1</sub>) cos(&delta;)
   *              cos(&alpha;<sub>1</sub> - &alpha;)</p>
   *
   * <p>and the position angle of the axis is</p>
   *
   * <p>sin(PA)  = sin(&alpha;<sub>1</sub> - &alpha;)
   *               cos(&delta;<sub>1</sub>) / cos(i)
   * <br />cos(PA) = [sin(&delta;<sub>1</sub>) cos(&delta;)
   *             -  cos(&delta;<sub>1</sub>) sin(&delta;)
   *                cos(&alpha;<sub>1</sub> - &alpha;)] / cos(i)</p>
   *
   * <p>To calculate the central meridian we need a similar angle K:</p>
   *
   * <p>sin(K)  = [-cos(&delta;<sub>1</sub>) sin(&delta;)
   *            +   sin(&delta;<sub>1</sub>) cos(&delta;)
   *                cos(&alpha;<sub>1</sub> - &alpha;)] / cos(i)
   * <br />cos(K) = sin(&alpha;<sub>1</sub> - &alpha;) cos(&delta;) / cos(i)
   * <br />CM = K - W</p>
   *
   * <p>This routine uses geocentric &alpha; and &delta; here.</p>
   *
   * <p>The V magnitude is calculated from the V(0,1) values listed in
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E88.
   * These are first corrected for heliocentric and geocentric distance, then
   * the phase angle Ph according to
   * Daniel L. Harris, 1961, Photometry and colorometry of planets and satellites, in: <em>Gerard P. Kuiper &amp; Barbara M. Middlehurst (eds.), Planets and satellites,</em> University of Chicago Press, Chicago, p.272ff:</p>
   *
   * <p>A = Ph/100&deg;</p>
   * <p>V = -4.40 + 5 lg(r/au) + 5 lg(R/au)
   *      + 0.09 A + 2.39 A<sup>2</sup>
   *               - 0.65 A<sup>3</sup></p>
   *
   * @param aOctet
   *   The eight returned numbers are
   *   the brightness in magnitudes,
   *   the apparent radius,
   *   the elongation from the Sun,
   *   the phase angle,
   *   the illuminated fraction of the disc,
   *   the inclination of the rotation axis,
   *   the position angle,
   *   the central meridian,
   *   -999,
   *   -999.
   *   All angles are in radian.
   * @param aTelescope
   *   Primarily the time for which to calculate the ephemeris.
   *   Also the position of the Sun. */

  public final void GetPhysics(double aOctet[], Telescope aTelescope)
  {
    double theSunPos[] = new double[3];
    double theSpher[]  = new double[3];
    double aTriplet[]  = new double[3];
    double theR[]      = new double[3];
    double theElong;
    double thePhase;
    double theIllum;
    double theMag;
    double theRho;
    double theBeta;
    double thePA;
    double theCM;
    double ra, rb, theRA1, theDec1, theW, theD, theA;

    /* Get the position of the Sun and of this planet, geocentric J2000. */

    aTelescope.itsSun.GetPos(theSunPos);
    GetXYZ(theR);

    /* The time parameters as used below. */

    theD = aTelescope.GetJDE() - 1545.;
    

    /* The vector from Sun to Moon, used for the phase angle.
     * Initially theElong and thePhase are positive, and that is used
     * to store theA, which is used for the magnitude below. */

    aTriplet[0] = theR[0] - theSunPos[0];
    aTriplet[1] = theR[1] - theSunPos[1];
    aTriplet[2] = theR[2] - theSunPos[2];
    theElong = Hmelib.SpherDist(theR, theSunPos);
    thePhase = Hmelib.SpherDist(theR, aTriplet);
    theA = thePhase * Hmelib.DEGPERRAD / 100.;
    if (theSunPos[1] * theR[0] > theSunPos[0] * theR[1]) {
      theElong *= -1.; thePhase *= -1.;
    }
    theIllum = (1. + Math.cos(thePhase)) / 2.;

    /* The distances of the planet from Earth and Sun are needed below for
     * the magnitude, and various other calculations. */

    ra = Math.sqrt(theR[0] * theR[0] + theR[1] * theR[1] + theR[2] * theR[2]);
    rb = Math.sqrt(aTriplet[0] * aTriplet[0] + aTriplet[1] * aTriplet[1]
                 + aTriplet[2] * aTriplet[2]);

    Hmelib.Spher(theR, theSpher);

    theRho = Math.asin(0.006052 / ra);

    theRA1  =  272.76                        / Hmelib.DEGPERRAD;
    theDec1 =   67.16                        / Hmelib.DEGPERRAD;
    theW    = (160.20  -   1.4813688 * (theD - ra / LIGHTGMD))
            / Hmelib.DEGPERRAD;
    theW    = Hmelib.NormAngle180(theW);

    theBeta = Math.asin(
            - Math.sin(theDec1) * Math.sin(theSpher[1])
            - Math.cos(theDec1) * Math.cos(theSpher[1])
            * Math.cos(theRA1 - theSpher[0]));
    thePA = Math.atan2(
            Math.sin(theRA1 - theSpher[0]) * Math.cos(theDec1),
            Math.sin(theDec1) * Math.cos(theSpher[1])
          - Math.cos(theDec1) * Math.sin(theSpher[1])
          * Math.cos(theRA1 - theSpher[0]));
    theCM = theW - Math.atan2(
          - Math.cos(theDec1) * Math.sin(theSpher[1])
          + Math.sin(theDec1) * Math.cos(theSpher[1])
          * Math.cos(theRA1 - theSpher[0]),
            Math.sin(theRA1 - theSpher[0]) * Math.cos(theSpher[1]));
    theCM = Hmelib.NormAngle180(-1. * theCM);

    theMag = -4.40 + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag += 0.09 * theA + 2.39 * theA * theA
                          - 0.65 * theA * theA * theA;

    aOctet[0] = theMag;
    aOctet[1] = theRho;
    aOctet[2] = theElong;
    aOctet[3] = thePhase;
    aOctet[4] = theIllum;
    aOctet[5] = theBeta;
    aOctet[6] = thePA;
    aOctet[7] = theCM;
    aOctet[8] = -999.;
    aOctet[9] = -999.;

    return;
  }

}
