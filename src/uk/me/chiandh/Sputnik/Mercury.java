
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Mercury</code> class extends the {@link VSOP87 VSOP87}
and holds information needed for the planet Mercury.  Most notably
these are the coefficients of the polynomials in time for the planet's
heliocentric longitude, latitude and distance.  The coefficients are
A<SUB>kji</SUB>, B<SUB>kji</SUB> and C<SUB>kji</SUB>, where k is L, B or
R, j is between 0 and 5 and i between 1 and up to 38.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <I>Astronomical Algorithms</I>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in
the series, the precision of calculated longitudes, latitudes and
distances should be on the order of 2.6" (0.0008&nbsp;Gm), 1.7"
(0.0006&nbsp;Gm) and 0.001&nbsp;Gm, resp.  It is therefore not necessary
to apply the correction from the VSOP dynamical ecliptic and equinox to
FK5, as this amounts to less than 0.1".</p>

<p>Copyright: &copy; 2002-2009 Horst Meyerdierks.</p>

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
<dt><strong>2002-12-30:</strong> hme</dt>
<dd>New class.</dd>
<dt><strong>2003-04-21:</strong> hme</dt>
<dd>Fix bug whereby magnitude depended on sign of phase angle.</dd>
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

public class Mercury extends VSOP87
{

  /** VSOP87 longitude terms for Mercury, 0th order in time. */

  protected static final double itsL0[] = {
          106., 4.206,      19804.827,
          118., 2.781,      77204.327,
          125., 3.721,      39609.655,
          138., 0.291,      10213.286,
          142., 3.360,      37410.567,
          173., 2.452,      24498.830,
          176., 4.536,      51066.428,
          182., 2.434,      25661.305,
          183., 2.629,      27043.503,
          209., 2.092,      47623.853,
          217., 0.660,      13521.751,
          235., 0.267,      11322.664,
          239., 0.113,       1059.382,
          260., 0.987,       4551.953,
          264., 3.917,      57837.138,
          273., 2.495,        529.691,
          325., 1.337,      53285.185,
          339., 5.863,      25558.212,
          343., 5.765,        955.600,
          345., 2.792,      15874.618,
          352., 5.242,      20426.571,
          404., 3.282,     208703.225,
          451., 6.050,      51116.424,
          644., 5.303,      21535.950,
          714., 1.541,      24978.525,
         1017., 0.8803,     31749.2352,
         1365., 4.5992,     27197.2817,
         1590., 2.9951,     25028.5212,
         1726., 0.3583,    182615.3220,
         1803., 4.1033,      5661.3320,
         3560., 1.5120,      1109.3786,
         7584., 3.7135,    156527.4189,
        34562., 0.77931,   130439.51571,
       165590., 4.119692,  104351.612566,
       855347., 1.165203,   78263.709425,
      5046294., 4.4778549,  52175.8062831,
     40989415., 1.48302034, 26087.90314157,
    440250710., 0.,             0.,
            0., 0.,             0.
  };  /* 38 terms, error < 2.6" or 0.0008 Gm. */


  /** VSOP87 longitude terms for Mercury, 1st order in time. */

  protected static final double itsL1[] = {
               27., 5.09,     234791.13,
               28., 3.04,      51066.43,
               44., 4.57,      25028.52,
               52., 5.62,       5661.33,
               91., 0.00,      24978.52,
               94., 6.12,      27197.28,
              103., 2.149,    208703.225,
              352., 3.052,      1109.379,
              388., 5.480,    182615.322,
             1472., 2.5185,   156527.4188,
             5592., 5.8268,   130439.5157,
            21245., 2.83532,  104351.61257,
            80538., 6.10455,   78263.70942,
           303471., 3.055655,  52175.806283,
          1126008., 6.2170397, 26087.9031416,
    2608814706223., 0.,            0.,
                0., 0.,            0.
  };  /* 16 terms, error < 0.45" t or 0.00015 Gm t. */


  /** VSOP87 longitude terms for Mercury, 2nd order in time. */

  protected static final double itsL2[] = {
       12., 0.79,   208703.23,
       15., 4.63,     1109.38,
       39., 4.08,   182615.32,
      123., 1.069,  156527.419,
      378., 4.320,  130439.516,
     1107., 1.2623, 104351.6126,
     3018., 4.4564,  78263.7094,
     7397., 1.3474,  52175.8063,
    16904., 4.69072, 26087.90314,
    53050., 0.,          0.,
        0., 0.,          0.
  };  /* 10 terms, error < 0.15" t^2 or 0.00005 Gm t^2. */


  /** VSOP87 longitude terms for Mercury, 3rd order in time. */

  protected static final double itsL3[] = {
      3., 2.57, 182615.32,
      7., 5.82, 156527.42,
     18., 2.78, 130439.52,
     35., 0.00,      0.00,
     44., 6.02, 104351.61,
     97., 3.00,  78263.71,
    142., 3.125, 26087.903,
    188., 0.035, 52175.806,
      0., 0.,        0.
  };  /* 8 terms, error < 0.04" t^3 or 0.0001 Gm t^3. */


  /** VSOP87 longitude terms for Mercury, 4th order in time. */

  protected static final double itsL4[] = {
      1., 1.27, 130439.52,
      1., 4.50, 104351.61,
      2., 4.50,  52175.81,
      2., 1.42,  78263.71,
      3., 2.03,  26087.90,
    114., 3.1416,    0.,
      0., 0.,        0.
  };  /* 6 terms, error < 0.005" t^4 or 0.00001 Gm t^4. */


  /** VSOP87 longitude terms for Mercury, 5th order in time. */

  protected static final double itsL5[] = {
    1., 3.14, 0.,
    0., 0.,   0.
  };  /* 1 term, error < 0.004" t^5 or 0.00001 Gm t^5. */


  /** VSOP87 latitude terms for Mercury, 0th order in time. */

  protected static final double itsB0[] = {
         100., 5.657,      20426.571,
         121., 1.813,      53285.185,
         132., 1.119,     234791.128,
         208., 4.918,      27197.282,
         209., 2.020,      24978.525,
         514., 4.378,     208703.225,
        2014., 1.3532,    182615.3220,
        7963., 4.6097,    156527.4188,
       31867., 1.58088,   130439.51571,
      129779., 4.832325,  104351.612566,
      543252., 1.796444,   78263.709425,
     1222840., 3.1415927,      0.000000,
     2388077., 5.0373896,  52175.8062831,
    11737529., 1.98357499, 26087.90314157,
           0., 0.,             0.
  };  /* 14 terms, error < 1.7" or 0.0006 Gm. */


  /** VSOP87 latitude terms for Mercury, 1st order in time. */

  protected static final double itsB1[] = {
        26., 5.98,    234791.13,
        28., 0.29,     27197.28,
        86., 2.95,    208703.23,
       278., 6.210,   182615.322,
       860., 3.185,   156527.419,
      2496., 0.1605,  130439.5157,
      6353., 3.4294,  104351.6126,
     10895., 0.48540,  78263.70942,
     22675., 0.01515,  52175.80628,
    146234., 3.141593,     0.00000,
    429151., 3.501698, 26087.903142,
         0., 0.,           0.
  };  /* 11 terms, error < 0.15" t or 0.00004 Gm t. */


  /** VSOP87 latitude terms for Mercury, 2nd order in time. */

  protected static final double itsB2[] = {
        7., 1.43,   208703.23,
       18., 4.67,   182615.32,
       45., 1.61,   156527.42,
       96., 4.80,   130439.52,
      170., 1.623,  104351.613,
      266., 4.434,   78263.709,
     1045., 1.2122,  52175.8063,
     1914., 0.0000,      0.0000,
    11831., 4.79066, 26087.90314,
        0., 0.,          0.
  };  /* 9 terms, error < 0.1" t^2 or 0.00003 Gm t^2. */


  /** VSOP87 latitude terms for Mercury, 3rd order in time. */

  protected static final double itsB3[] = {
      2., 6.27, 156527.42,
      3., 3.12, 130439.52,
      5., 6.14, 104351.61,
      6., 2.51,  78263.71,
     19., 4.36,  52175.81,
    161., 0.00,      0.00,
    235., 0.354, 26087.903,
      0., 0.,        0.
  };  /* 7 terms, error < 0.02" t^3 or 0.000006 Gm t^3. */


  /** VSOP87 latitude terms for Mercury, 4th order in time. */

  protected static final double itsB4[] = {
    1., 3.14,     0.00,
    4., 1.75, 26087.90,
    0., 0.,       0.
  };  /* 2 terms, error < 0.006" t^4 or 0.0000015 Gm t^4. */


  /** VSOP87 latitude terms for Mercury, 5th order in time. */

  protected static final double itsB5[] = {0., 0., 0.};


  /** VSOP87 distance terms for Mercury, 0th order in time. */

  protected static final double itsR0[] = {
         100., 3.734,     21535.950,
         142., 6.253,     24978.525,
         201., 5.592,     31749.235,
         202., 5.647,    182615.322,
         260., 3.028,     27197.282,
         290., 1.424,     25028.521,
         918., 2.597,    156527.419,
        4354., 5.8289,   130439.5157,
       21922., 2.77820,  104351.61257,
      121282., 6.010642,  78263.709425,
      795526., 2.959897,  52175.806283,
     7834132., 6.1923372, 26087.9031416,
    39528272., 0.0000000,     0.0000000,
           0., 0.,            0.
  };  /* 13 terms, error < 0.001 Gm. */


  /** VSOP87 distance terms for Mercury, 1st order in time. */

  protected static final double itsR1[] = {
        39., 4.11,    182615.32,
       153., 1.061,   156527.419,
       604., 4.293,   130439.516,
      1624., 0.000,        0.000,
      2433., 1.2423,  104351.6126,
     10094., 4.47466,  78263.70942,
     44142., 1.42386,  52175.80628,
    217348., 4.656172, 26087.903142,
         0., 0.,           0.
  };  /* 8 terms, error < 0.0004 Gm t. */


  /** VSOP87 distance terms for Mercury, 2nd order in time. */

  protected static final double itsR2[] = {
      13., 5.80,  156527.42,
      22., 3.14,       0.00,
      42., 2.75,  130439.52,
     136., 5.980, 104351.613,
     425., 2.926,  78263.709,
    1245., 6.1518, 52175.8063,
    3118., 3.0823, 26087.9031,
       0., 0.,         0.
  };  /* 7 terms, error < 0.0001 Gm t^2. */


  /** VSOP87 distance terms for Mercury, 3rd order in time. */

  protected static final double itsR3[] = {
     2., 1.21, 130439.52,
     5., 4.44, 104351.61,
    12., 1.39,  78263.71,
    24., 4.63,  52175.81,
    33., 1.68,  26087.90,
     0., 0.,        0.
  };  /* 5 terms, error < 0.000015 Gm t^3. */


  /** VSOP87 distance terms for Mercury, 4th order in time. */

  protected static final double itsR4[] = {0., 0., 0.};


  /** VSOP87 distance terms for Mercury, 5th order in time. */

  protected static final double itsR5[] = {0., 0., 0.};


  /**
   * Initialise.
   *
   * <p>This initialises the Mercury object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal
   * equinox).</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for Mercury";
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

    Mercury   theObject;
    theObject = new Mercury();
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
   * Set Mercury for the given time.
   *
   * <p>This calculates for the given time Mercury's J2000 coordinates.</p>
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

    itsName = "Mercury";

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
   * USNO/RGO, 1990, <I>The Astronomical Almanach for the Year 1992</I>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E88,
   * and the geocentric distance:</p>
   *
   * <p>&rho; = asin(2439 km/r)</p>
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
   * <p>&alpha;<SUB>1</SUB> = 281.02&deg; - 0.033&deg; T
   * <br>&delta;<SUB>1</SUB> = 61.45&deg; - 0.005&deg; T
   * <br>W = 329.68&deg; + 6.1385025&deg;/d (t - r/c)</p>
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
   * <p>A = Ph/100&deg;</p>
   * <p>V = -0.42 + 5 lg(r/au) + 5 lg(R/au)
   *      + 3.80 A - 2.73 A<sup>2</sup> + 2.00 A<sup>3</sup></p>
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
    double ra, rb, theRA1, theDec1, theW, theD, theT, theA;

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

    theRho = Math.asin(0.002439 / ra);

    theRA1  = (281.02 - 0.033     * theT) / Hmelib.DEGPERRAD;
    theDec1 = ( 61.45 - 0.005     * theT) / Hmelib.DEGPERRAD;
    theW    = (329.68 + 6.1385025 * (theD - ra / LIGHTGMD))
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
    theCM = Hmelib.NormAngle180(theCM);

    theMag = -0.42 + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag += 3.80 * theA - 2.73 * theA * theA
                          + 2.00 * theA * theA * theA;

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
