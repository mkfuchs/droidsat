
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Neptune</code> class extends the {@link VSOP87
VSOP87} and holds information needed for the planet Neptune.  Most notably
these are the coefficients of the polynomials in time for the planet's
heliocentric longitude, latitude and distance.  The coefficients are
A<SUB>kji</SUB>, B<SUB>kji</SUB> and C<SUB>kji</SUB>, where k is L, B or
R, j is between 0 and 5 and i between 1 and up to 38.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in
the series, the precision of calculated longitudes, latitudes and
distances should be on the order of 2.6" (0.0056&nbsp;Gm), 2.4"
(0.0052&nbsp;Gm) and 0.001&nbsp;Gm, resp.  It is therefore not necessary
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
<dt><strong>2005-12-28:</strong> hme</dt>
<dd>Change handling of physical ephemeris.  Before, they were geocentric,
  J2000, caclulated by Update calling SetPhysics, and stored in class
  fields.  Now they are calculated by GetPhysics when needed, and they
  are topocentric EOD.</dd>
<dt><strong>2005-12-28:</strong> hme</dt>
<dd>Version 2.1.3.</dd>
<dt><strong>2009-01-26:</strong> hme</dt>
<dd>Extend the octet array to be a decatet (not a word known to humans
  until now), so that rotation systems I, II and III can be accommodated.</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Revert to using geocentric J2000 in GetPhysics.<br />
  Fix a typo in the physical ephemeris, where the start value of N was
  257.85&deg; instead of 357.85&deg;.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */

public class Neptune extends VSOP87
{

  /** VSOP87 longitude terms for Neptune, 0th order in time. */

  protected static final double itsL0[] = {
          102., 5.705,      0.112,
          103., 4.404,     70.328,
          103., 0.041,      0.261,
          109., 2.416,    183.243,
          119., 3.677,      2.448,
          148., 0.860,    111.430,
          150., 2.997,      5.938,
          151., 2.192,     33.940,
          170., 3.324,    108.461,
          227., 1.797,    453.425,
          233., 2.505,    137.033,
          245., 1.247,      9.561,
          252., 5.782,    388.465,
          267., 4.889,      0.963,
          282., 2.246,    146.594,
          287., 4.505,      0.048,
          306., 0.497,      0.521,
          323., 2.248,     32.195,
          340., 3.304,     77.751,
          345., 3.462,     41.102,
          400., 0.350,   1021.249,
          506., 5.748,    114.399,
          745., 3.190,     71.813,
          900., 2.076,    109.946,
         1434., 2.7834,    74.7816,
         2285., 4.2061,     4.4534,
         3365., 1.0359,    33.6796,
         4216., 1.9871,    73.2971,
         8994., 0.2746,   175.1661,
         9199., 4.9375,    39.6175,
        16483., 0.00008,  491.55793,
        33785., 1.24489,   76.26607,
        37715., 6.09222,   35.16409,
        42064., 5.41055,    2.96895,
       124532., 4.830081,  36.648563,
      1019728., 0.4858092,  1.4844727,
      1798476., 2.9010127, 38.1330356,
    531188633., 0.,         0.,
            0., 0.,         0.
  };  /* 38 terms, error < 2.6" or 0.0056 Gm. */


  /** VSOP87 longitude terms for Neptune, 1st order in time. */

  protected static final double itsL1[] = {
            26., 5.25,   168.05,
            29., 5.17,     2.45,
            29., 5.17,     9.56,
            30., 3.67,   388.47,
            32., 5.90,    77.75,
            35., 4.52,    74.78,
            57., 5.22,     0.52,
            57., 1.86,   114.40,
            73., 5.49,    36.65,
           106., 2.755,   33.680,
           107., 2.451,    4.453,
           179., 3.453,   39.618,
           605., 1.505,   35.164,
          1306., 3.6732,   2.9689,
          3335., 3.6820,  76.2661,
         15807., 2.27923, 38.13304,
         16604., 4.86319,  1.48447,
    3837687717., 0.,       0.,
             0., 0.,       0.
  };


  /** VSOP87 longitude terms for Neptune, 2nd order in time. */

  protected static final double itsL2[] = {
        7., 0.54,   2.45,
        9., 4.43,  35.16,
       23., 1.21,   2.97,
      270., 5.721, 76.266,
      281., 1.191, 38.133,
      296., 1.855,  1.484,
    53893., 0.,     0.,
        0., 0.,     0.
  };


  /** VSOP87 longitude terms for Neptune, 3rd order in time. */

  protected static final double itsL3[] = {
    12., 6.11, 38.13,
    12., 6.04,  1.48,
    15., 1.35, 76.27,
    31., 0.,    0.,
     0., 0.,    0.
  };


  /** VSOP87 longitude terms for Neptune, 4th order in time. */

  protected static final double itsL4[] = {
    114., 3.142, 0.,
      0., 0.,    0.
  };


  /** VSOP87 longitude terms for Neptune, 5th order in time. */

  protected static final double itsL5[] = {0., 0., 0.};


  /** VSOP87 latitude terms for Neptune, 0th order in time. */

  protected static final double itsB0[] = {
        140., 3.530,    137.033,
        206., 4.257,    529.691,
        254., 3.271,    453.425,
        262., 3.767,    213.299,
        280., 1.682,     77.751,
        402., 4.169,    114.399,
        589., 3.187,      2.969,
        595., 2.129,     41.102,
        606., 2.802,     73.297,
       1015., 3.2156,    35.1641,
       1968., 4.3778,     1.4845,
       2000., 1.5100,    74.7816,
      15355., 2.52124,   36.64856,
      15448., 3.50877,   39.61751,
      27624., 0.,         0.,
      27780., 5.91272,   76.26607,
    3088623., 1.4410437, 38.1330356,
          0., 0.,         0.
  };  /* 17 terms, error < 2.4" or 0.0052 Gm. */


  /** VSOP87 latitude terms for Neptune, 1st order in time. */

  protected static final double itsB1[] = {
        26., 5.22,    213.30,
        37., 5.76,      2.97,
        37., 4.89,     41.10,
        43., 0.31,    114.40,
        52., 5.05,     73.30,
        70., 6.19,     35.16,
       136., 0.478,     1.484,
       148., 3.858,    74.782,
      1073., 6.0805,   39.6175,
      1386., 4.8256,   36.6486,
      1433., 3.1416,    0.,
      1803., 1.9758,   76.2661,
    227279., 3.807931, 38.133036,
         0., 0.,        0.
  };


  /** VSOP87 latitude terms for Neptune, 2nd order in time. */

  protected static final double itsB2[] = {
       6., 5.61,   74.78,
      30., 1.61,   39.62,
      59., 3.14,    0.,
      72., 0.45,   36.65,
      79., 3.63,   76.27,
    9691., 5.5712, 38.1330,
       0., 0.,      0.
  };


  /** VSOP87 latitude terms for Neptune, 3rd order in time. */

  protected static final double itsB3[] = {
      2., 5.33,  76.27,
      2., 2.37,  36.65,
      2., 0.,     0.,
    273., 1.017, 38.133,
      0., 0.,     0.
  };


  /** VSOP87 latitude terms for Neptune, 4th order in time. */

  protected static final double itsB4[] = {
    6., 2.67, 38.13,
    0., 0.,    0.
  };


  /** VSOP87 latitude terms for Neptune, 5th order in time. */

  protected static final double itsB5[] = {0., 0., 0.};


  /** VSOP87 distance terms for Neptune, 0th order in time. */

  protected static final double itsR0[] = {
          2087., 0.6186,     33.9402,
          2306., 2.8096,     70.3282,
          2523., 0.4863,    493.0424,
          2530., 5.7984,    490.0735,
          2636., 3.0976,    213.2991,
          2879., 3.6742,    350.3321,
          2881., 1.9860,    137.0330,
          3381., 0.8481,    183.2428,
          4270., 3.4134,    453.4249,
          4354., 0.6799,     32.1951,
          4421., 1.7499,    108.4612,
          4483., 2.9057,    529.6910,
          4840., 1.9069,     41.1020,
          5721., 2.5906,      4.4534,
          7572., 1.0715,    388.4652,
          8395., 0.6782,    146.5943,
         12012., 1.92062,  1021.24889,
         14230., 1.07786,    74.78160,
         16939., 1.59422,    71.81265,
         24594., 0.50802,   109.94569,
         46688., 5.74938,    33.67962,
         69792., 3.79617,     2.96895,
        100895., 0.377027,   73.297126,
        121802., 5.797544,   76.266071,
        135134., 3.372206,   39.617508,
        274572., 1.845523,  175.166060,
        495726., 1.571057,  491.557929,
        537761., 4.521139,   35.164090,
        807831., 5.185928,    1.484473,
       1691764., 3.2518614,  36.6485629,
      27062259., 1.32999459, 38.13303564,
    3007013206., 0.,          0.,
             0., 0.,          0.
  };  /* 32 terms, error < 0.0035 Gm. */


  /** VSOP87 distance terms for Neptune, 1st order in time. */

  protected static final double itsR1[] = {
       561., 2.887,   498.671,
       572., 3.401,   484.444,
       607., 1.077,  1021.249,
       760., 0.021,   182.280,
       790., 0.533,   168.053,
       898., 5.241,   388.465,
      1136., 3.9189,   36.6486,
      1464., 1.1842,   33.6796,
      1603., 0.,        0.,
      2153., 5.1687,   76.2661,
      2155., 2.0943,    2.9689,
      2702., 1.8814,   39.6175,
      8622., 6.2163,   35.1641,
     13220., 3.32015,   1.48447,
    236339., 0.704980, 38.133036,
         0., 0.,        0.
  };


  /** VSOP87 distance terms for Neptune, 2nd order in time. */

  protected static final double itsR2[] = {
     127., 2.848,  35.164,
     156., 4.594, 182.280,
     163., 2.239, 168.053,
     218., 0.346,   1.484,
    4247., 5.8991, 38.1330,
       0., 0.,      0.
  };


  /** VSOP87 distance terms for Neptune, 3rd order in time. */

  protected static final double itsR3[] = {
    166., 4.552, 38.133,
      0., 0.,     0.
  };


  /** VSOP87 distance terms for Neptune, 4th order in time. */

  protected static final double itsR4[] = {0., 0., 0.};


  /** VSOP87 distance terms for Neptune, 5th order in time. */

  protected static final double itsR5[] = {0., 0., 0.};


  /**
   * Initialise.
   *
   * <p>This initialises the Neptune object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal
   * equinox).</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for Neptune";
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

    Neptune   theObject;
    theObject = new Neptune();
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
   * Set Neptune for the given time.
   *
   * <p>This calculates for the given time Neptune's J2000 coordinates.</p>
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

    itsName = "Neptune";

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
   * <p>&rho; = asin(25269.10 km/r)</p>
   *
   * <p>To calcluate the orientation of the planet and its state of rotation we
   * use the expressions of
   * M.E. Davies, V.K. Abalakin, M. Bursa, J.H. Lieske, B.Morando, D. Morrison, P.K. Seidelmann, A.T. Sinclair, B. Yallop, Y.S. Tjuflin, 1996, Report of the IAU/IAG/COSPAR working group on cartographic coordinates and rotational elements of the planets and satellites: 1994, <I>Celestial Mechanics and Dynamical Astronomy</I>, <B>63</B>, p.127ff,
   * for the celestial coordinates of the planet's pole of rotation and the
   * prime meridian W:</p>
   * 
   * <p>t = JDE - 2451545 d</p>
   * 
   * <p>T = t / 36525 d</p>
   *
   * <p>N = (357.85&deg; + 52.316&deg; T)
   * <br />&alpha;<SUB>1</SUB> = [299.36&deg; + 0.70&deg; sin(N)]
   * <br />&delta;<SUB>1</SUB> = [43.46&deg; - 0.51&deg; cos(N)]
   * <br />W = [253.18&deg; + 536.3128492&deg;/d (t - r/c) - 0.48&deg; sin(N)]</p>
   *
   * <p>W here refers to System III, which applies to the magnetic field of
   * Neptune.</p>
   *
   * <p>According to
   * USNO/RGO, 1990, <I>The Astronomical Almanach for the Year 1992</I>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E87
   * we calculate first W for the time in question, corrected for light time
   * effects.  The inclination of the axis then is</p>
   *
   * <p>sin(i) = -sin(&delta;<SUB>1</SUB>) sin(&delta;)
   *           -  cos(&delta;<SUB>1</SUB>) cos(&delta;)
   *              cos(&alpha;<SUB>1</SUB> - &alpha;)</p>
   *
   * <p>and the position angle of the axis is</p>
   *
   * <p>sin(PA)  = sin(&alpha;<SUB>1</SUB> - &alpha;)
   *               cos(&delta;<SUB>1</SUB>) / cos(i)
   * <br />cos(PA) = [sin(&delta;<SUB>1</SUB>) cos(&delta;)
   *             -  cos(&delta;<SUB>1</SUB>) sin(&delta;)
   *                cos(&alpha;<SUB>1</SUB> - &alpha;)] / cos(i)</p>
   *
   * <p>To calculate the central meridian we need a similar angle K:</p>
   *
   * <p>sin(K)  = [-cos(&delta;<SUB>1</SUB>) sin(&delta;)
   *            +   sin(&delta;<SUB>1</SUB>) cos(&delta;)
   *                cos(&alpha;<SUB>1</SUB> - &alpha;)] / cos(i)
   * <br />cos(K) = sin(&alpha;<SUB>1</SUB> - &alpha;) cos(&delta;) / cos(i)
   * <br />CM = W - K</p>
   *
   * <p>This routine uses geocentric &alpha; and &delta; here.</p>
   *
   * <p>The V magnitude is calculated from the V(0,1) values listed in
   * USNO/RGO, 1990, <I>The Astronomical Almanach for the Year 1992</I>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E88.
   * These are first corrected for heliocentric and geocentric distance, then
   * the phase angle Ph according to
   * Daniel L. Harris, 1961, Photometry and colorometry of planets and satellites, in: <em>Gerard P. Kuiper &amp; Barbara M. Middlehurst (eds.), Planets and satellites,</em> University of Chicago Press, Chicago, p.272ff:</p>
   *
   * <p>V = -6.87 + 5 lg(r/au) + 5 lg(R/au) - 2.5 lg(L)</p>
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
   *   -999,
   *   -999,
   *   the central meridian in System III (magnetic field).
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
    double ra, rb, theRA1, theDec1, theW, theD, theT, theN;

    /* Get the position of the Sun and of this planet, geocentric J2000. */

    aTelescope.itsSun.GetPos(theSunPos);
    GetXYZ(theR);

    /* The time parameters as used below. */

    theD = aTelescope.GetJDE() - 1545.;
    theT = theD / 36525.;

    /* The vector from Sun to planet, used for the phase angle.
     * Initially theElong and thePhase are positive, and that is used
     * to store theA, which is used for the magnitude below. */

    aTriplet[0] = theR[0] - theSunPos[0];
    aTriplet[1] = theR[1] - theSunPos[1];
    aTriplet[2] = theR[2] - theSunPos[2];
    theElong = Hmelib.SpherDist(theR, theSunPos);
    thePhase = Hmelib.SpherDist(theR, aTriplet);
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

    theRho = Math.asin(0.02526910 / ra);

    theN    = (357.85  +  52.316     * theT)      / Hmelib.DEGPERRAD;
    theRA1  = (299.36  +   0.70 * Math.sin(theN)) / Hmelib.DEGPERRAD;
    theDec1 = ( 43.46  -   0.51 * Math.cos(theN)) / Hmelib.DEGPERRAD;
    theW    = (253.18  + 536.3128492 * (theD - ra / LIGHTGMD)
	               -   0.48 * Math.sin(theN)) / Hmelib.DEGPERRAD;
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
    theCM = Hmelib.NormAngle180(theCM);

    theMag = -6.87 + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag -= 2.5 * Math.log(theIllum) / Math.log(10.);

    aOctet[0] = theMag;
    aOctet[1] = theRho;
    aOctet[2] = theElong;
    aOctet[3] = thePhase;
    aOctet[4] = theIllum;
    aOctet[5] = theBeta;
    aOctet[6] = thePA;
    aOctet[7] = -999.;
    aOctet[8] = -999.;
    aOctet[9] = theCM;

    return;
  }

}
