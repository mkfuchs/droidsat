
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Sun</code> class extends the {@link VSOP87 VSOP87} and
holds information needed for the Sun.  Most notably these are the
coefficients of the polynomials in time for the Earth's heliocentric
longitude, latitude and distance.  The coefficients are A<sub>kji</sub>,
B<sub>kji</sub> and C<sub>kji</sub>, where k is L, B or R, j is between 0
and 5 and i between 1 and up to 64.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in the series, the precision of calculated longitudes,
latitudes and distances should be on the order of 1.7" (0.0012&nbsp;Gm),
0.3" (0.0002&nbsp;Gm) and 0.00049&nbsp;Gm, resp.
It is therefore not necessary to apply the correction from the VSOP
dynamical ecliptic and equinox to FK5, as this amounts to less than
0.1".</p>

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
<dt><strong>2002/06/23:</strong> hme</dt>
<dd>New class.</dd>
<dt><strong>2002/07/02:</strong> hme</dt>
<dd>SetSun() now calculates velocity.  However, GetHelio() seems to return
  reversed coordinates!?</dd>
<dt><strong>2002/07/02:</strong> hme</dt>
<dd>That bug was in VSOP87 and is now fixed.  In fact it gave a completely
  wrong heliocentric longitude, which happened to look like a negated
  vector.</dd>
<dt><strong>2002/07/02:</strong> hme</dt>
<dd>Corrected the magnitude of the velocity.  Add GetSunVel() so that users
  can use the velocity.</dd>
<dt><strong>2002/07/07:</strong> hme</dt>
<dd>Add SetPhysics() and use it from SetSun() to set physical
  ephemeris.</dd>
<dt><strong>2002/07/13:</strong> hme</dt>
<dd>Add GetPos() and rename GetSunVel() to GetVel().</dd>
<dt><strong>2002/07/13:</strong> hme</dt>
<dd>Consolidate documentation.</dd>
<dt><strong>2002/12/30:</strong> hme</dt>
<dd>The previous storage of the A VSOP87 coefficients did not work when
  different sub-classes began to co-exist.  Now the coefficients are class
  variables in the sub-classes (Sun and Mercury), and passed as arguments
  to the GetHelio() method of the VSOP87 class.</dd>
<dt><strong>2003/09/16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2003/09/18:</strong> hme</dt>
<dd>Add NextRiseSet() method.</dd>
<dt><strong>2005/12/27:</strong> hme</dt>
<dd>Change handling of physical ephemeris.  Before, they were geocentric,
  J2000, caclulated by Update calling SetPhysics, and stored in class
  fields.  Now they are calculated by GetPhysics when needed, and they
  are topocentric EOD.</dd>
<dt><strong>2005/12/28:</strong> hme</dt>
<dd>Version 2.1.3.</dd>
<dt><strong>2009-01-26:</strong> hme</dt>
<dd>Extend the octet array to be a decatet, so that rotation systems I,
  II and III can be accommodated.</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Revert to using geocentric J2000 in GetPhysics.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Times
@see uk.me.chiandh.Lib.Hmelib
 */


public class Sun extends VSOP87
{

  /** VSOP87 longitude terms for the Earth, 0th order in time. */

  protected static final double itsL0[] = {
           25., 3.16,      4690.48,
           30., 2.74,      1349.87,
           30., 0.44,     83996.85,
           33., 0.59,     17789.85,
           36., 1.78,      6812.77,
           36., 1.71,      2352.87,
           37., 2.57,      1059.38,
           37., 6.04,     10213.29,
           39., 6.17,     10447.39,
           41., 2.40,     19651.05,
           41., 5.37,      8429.24,
           49., 0.49,      1194.45,
           51., 0.28,      5856.48,
           52., 1.33,      1748.02,
           52., 0.19,     12139.55,
           56., 3.47,      6279.55,
           56., 4.39,     14143.50,
           57., 2.78,      6286.60,
           61., 1.82,      7084.90,
           62., 3.98,      8827.39,
           70., 0.83,      9437.76,
           74., 4.68,       801.82,
           74., 3.50,      3154.69,
           75., 1.76,      5088.63,
           79., 3.04,     12036.46,
           80., 1.81,     17260.15,
           85., 3.67,     71430.70,
           85., 1.30,      6275.96,
           86., 5.98,    161000.69,
           98., 0.68,       155.42,
           99., 6.21,      2146.17,
          102., 4.267,        7.114,
          102., 0.976,    15720.839,
          103., 0.636,     4694.003,
          115., 0.645,        0.980,
          126., 1.083,       20.775,
          132., 3.411,     2942.463,
          156., 0.833,      213.299,
          202., 2.458,     6069.777,
          205., 1.869,     5573.143,
          206., 4.806,     2544.314,
          243., 0.345,     5486.778,
          271., 0.315,    10977.079,
          284., 1.899,      796.298,
          317., 5.849,    11790.629,
          357., 2.920,        0.067,
          492., 4.205,      775.523,
          505., 4.583,    18849.228,
          753., 2.533,     5507.553,
          780., 1.179,     5223.694,
          857., 3.508,      398.149,
          902., 2.045,       26.298,
          990., 5.233,     5884.927,
         1199., 1.1096,    1577.3435,
         1273., 2.0371,     529.6910,
         1324., 0.7425,   11506.7698,
         2343., 6.1352,    3930.2097,
         2676., 4.4181,    7860.4194,
         3136., 3.6277,   77713.7715,
         3418., 2.8289,       3.5231,
         3497., 2.7441,    5753.3849,
        34894., 4.62610,  12566.15170,
      3341656., 4.6692568, 6283.0758500,
    175347046., 0.,           0.,
            0., 0.,           0.
  };  /* 64 terms, error < 1.65" or 0.0012 Gm. */


  /** VSOP87 longitude terms for the Earth, 1st order in time. */

  protected static final double itsL1[] = {
               6., 4.67,     4690.48,
               6., 2.65,     9437.76,
               8., 5.30,     2352.87,
               9., 5.64,      951.72,
               9., 2.70,      242.73,
              10., 4.24,     1349.87,
              10., 1.30,     6286.60,
              11., 0.77,      553.57,
              12., 2.08,     4694.00,
              12., 5.27,     1194.45,
              12., 3.26,     5088.63,
              12., 2.83,     1748.02,
              15., 1.21,    10977.08,
              16., 1.43,     2146.17,
              16., 0.03,     2544.31,
              17., 2.99,     6275.96,
              19., 4.97,      213.30,
              19., 1.85,     5486.78,
              21., 5.34,        0.98,
              29., 2.65,        7.11,
              36., 0.47,      775.52,
              45., 0.40,      796.30,
              56., 2.17,      155.42,
              59., 2.89,     5223.69,
              67., 4.41,     5507.55,
              68., 1.87,      398.15,
              72., 1.14,      529.69,
              93., 2.59,    18849.23,
             109., 2.966,    1577.344,
             119., 5.796,      26.298,
             425., 1.590,       3.523,
            4303., 2.6351,  12566.1517,
          206059., 2.678235, 6283.075850,
    628331966747., 0.,          0.,
               0., 0.,          0.
  };  /* 34 terms, error < 0.14" t or 0.0001 Gm t. */


  /** VSOP87 longitude terms for the Earth, 2nd order in time. */

  protected static final double itsL2[] = {
        2., 3.75,      0.98,
        2., 4.38,   5223.69,
        3., 2.28,    553.57,
        3., 0.31,    398.15,
        3., 6.12,    529.69,
        3., 1.19,    242.73,
        3., 6.05,   5507.55,
        3., 5.14,    796.30,
        4., 3.44,   5573.14,
        4., 1.03,      7.11,
        5., 4.66,   1577.34,
        7., 0.83,    775.52,
        9., 2.06,  77713.77,
       10., 0.76,  18849.23,
       16., 3.68,    155.42,
       16., 5.19,     26.30,
       27., 0.05,      3.52,
      309., 0.867, 12566.152,
     8720., 1.0721, 6283.0758,
    52919., 0.,        0.,
        0., 0.,        0.
  };  /* 20 terms, error < 0.036" t^2 or 0.00003 Gm t^2. */


  /** VSOP87 longitude terms for the Earth, 3rd order in time. */

  protected static final double itsL3[] = {
      1., 5.97,   242.73,
      1., 5.30, 18849.23,
      1., 4.72,     3.52,
      3., 5.20,   155.42,
     17., 5.49, 12566.15,
     35., 0.,       0.,
    289., 5.844, 6283.076,
      0., 0.,       0.
  };  /* 7 terms, error < 0.011" t^3 or 0.000008 Gm t^3. */


  /** VSOP87 longitude terms for the Earth, 4th order in time. */

  protected static final double itsL4[] = {
      1., 3.84, 12566.15,
      8., 4.13,  6283.08,
    114., 3.142,    0.,
      0., 0.,       0.
  };  /* 3 terms, error < 0.007" t^4 or 0.000005 Gm t^4. */


  /** VSOP87 longitude terms for the Earth, 5th order in time. */

  protected static final double itsL5[] = {
    1., 3.14, 0.,
    0., 0.,   0.
  };  /* 1 term, error < 0.004" t^5 or 0.000003 Gm t^5. */


  /** VSOP87 latitude terms for the Earth, 0th order in time. */

  protected static final double itsB0[] = {
     32., 4.00,   1577.34,
     44., 3.70,   2352.87,
     80., 3.88,   5223.69,
    102., 5.422,  5507.553,
    280., 3.199, 84334.662,
      0., 0.,        0.
  };  /* 5 terms, error < 0.30" or 0.0002 Gm. */


  /** VSOP87 latitude terms for the Earth, 1st order in time. */

  protected static final double itsB1[] = {
    6., 1.73, 5223.69,
    9., 3.90, 5507.55,
    0., 0.,      0.
  };  /* 2 terms, error < 0.035" t or 0.00003 Gm t. */


  /** VSOP87 latitude terms for the Earth, 2nd order in time. */

  protected static final double itsB2[] = {0., 0., 0.};


  /** VSOP87 latitude terms for the Earth, 3rd order in time. */

  protected static final double itsB3[] = {0., 0., 0.};


  /** VSOP87 latitude terms for the Earth, 4th order in time. */

  protected static final double itsB4[] = {0., 0., 0.};


  /** VSOP87 latitude terms for the Earth, 5th order in time. */

  protected static final double itsB5[] = {0., 0., 0.};


  /** VSOP87 distance terms for the Earth, 0th order in time. */

  protected static final double itsR0[] = {
           26., 4.59,     10447.39,
           28., 1.90,      6279.55,
           28., 1.21,      6286.60,
           32., 1.78,       398.15,
           32., 0.18,      5088.63,
           33., 0.24,      7084.90,
           35., 1.84,      2942.46,
           36., 1.67,     12036.46,
           37., 4.90,     12139.55,
           37., 0.83,     19651.05,
           38., 2.39,      8827.39,
           39., 5.36,      4694.00,
           43., 6.01,      6275.96,
           45., 5.54,      9437.76,
           47., 2.58,       775.52,
           49., 3.25,      2544.31,
           56., 5.24,     71430.70,
           57., 2.01,     83996.85,
           63., 0.92,       529.69,
           65., 0.27,     17260.15,
           86., 1.27,    161000.69,
           86., 5.69,     15720.84,
           98., 0.89,      6069.78,
          110., 5.055,     5486.778,
          175., 3.012,    18849.228,
          186., 5.022,    10977.079,
          212., 5.847,     1577.344,
          243., 4.273,    11790.629,
          307., 0.299,     5573.143,
          329., 5.900,     5223.694,
          346., 0.964,     5507.553,
          472., 3.661,     5884.927,
          542., 4.564,     3930.210,
          925., 5.453,    11506.770,
         1576., 2.8469,    7860.4194,
         1628., 1.1739,    5753.3849,
         3084., 5.1985,   77713.7715,
        13956., 3.05525,  12566.15170,
      1670700., 3.0984635, 6283.0758500,
    100013989., 0.,           0.,
            0., 0.,           0.
  };  /* 40 terms, error < 0.00049 Gm. */


  /** VSOP87 distance terms for the Earth, 1st order in time. */

  protected static final double itsR1[] = {
         9., 0.27,     5486.78,
         9., 1.42,     6275.96,
        10., 5.91,    10977.08,
        18., 1.42,     1577.34,
        25., 1.32,     5223.69,
        31., 2.84,     5507.55,
        32., 1.02,    18849.23,
       702., 3.142,       0.,
      1721., 1.0644,  12566.1517,
    103019., 1.107490, 6283.075850,
         0., 0.,          0.
  };  /* 10 terms, error < 0.000085 Gm t. */


  /** VSOP87 distance terms for the Earth, 2nd order in time. */

  protected static final double itsR2[] = {
       3., 5.47,  18849.23,
       6., 1.87,   5573.14,
       9., 3.63,  77713.77,
      12., 3.14,      0.,
     124., 5.579, 12566.152,
    4359., 5.7846, 6283.0758,
       0., 0.,        0.
  };  /* 6 terms, error < 0.000022 Gm t^2. */


  /** VSOP87 distance terms for the Earth, 3rd order in time. */

  protected static final double itsR3[] = {
      7., 3.92, 12566.15,
    145., 4.273, 6283.076,
      0., 0.,       0.
  };  /* 2 terms, error < 0.000030 Gm t^3. */


  /** VSOP87 distance terms for the Earth, 4th order in time. */

  protected static final double itsR4[] = {
    4., 2.56, 6283.08,
    0., 0.,      0.
  };  /* 1 term, error < 0.000012 Gm t^4. */


  /** VSOP87 distance terms for the Earth, 5th order in time. */

  protected static final double itsR5[] = {0., 0., 0.};


  /** The spatial velocity of the Sun in km/s. */

  protected double[] itsV;


  /**
   * Return the spatial geocentric position of the Sun.
   *
   * <p>The returned position is in Gm along the axes of the J2000 system.</p>
   *
   * @param aTriplet
   *   The returned position. */

  public final void GetPos(double aTriplet[])
  {
    aTriplet[0] = itsR[0]; aTriplet[1] = itsR[1]; aTriplet[2] = itsR[2];
    return;
  }


  /**
   * Return the spatial geocentric velocity of the Sun.
   *
   * <p>The returned velocity is in km/s along the axes of the
   * J2000 system.</p>
   *
   * @param aTriplet
   *   The returned velocity. */

  public final void GetVel(double aTriplet[])
  {
    aTriplet[0] = itsV[0]; aTriplet[1] = itsV[1]; aTriplet[2] = itsV[2];
    return;
  }


  /**
   * Initialise the Sun.
   *
   * <p>This initialises the Sun object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal equinox), the
   * velocity is initialised to zero.</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for the Sun";
    itsV = new double[3];
    itsV[0] = 0.; itsV[1] = 0.; itsV[2] = 0.;
    return;
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

    Sun       theObject;
    theObject = new Sun();
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
   * Set the Sun for the given time.
   *
   * <p>This calculates for the given time the Sun's spatial velocity, J2000
   * coordinates.  The velocity is calulated by taking
   * the difference of the position 500&nbsp;s before and 500&nbsp;s after
   * the given time.</p>
   *
   * @param aTime
   *   The time for which to calculate the ephemeris. */

  public final void Update(Times aTime)
  {
    Times  otherTime;
    double t[] = new double[3];

    /* Set the name of the NamedObject. */

    itsName = "Sun";

    /* Before calculating the state for the given time, do the calculations
     * that require time differentials, namely the velocity.  We take a copy
     * of the given time, decrement it by 500 s, obtain the Sun's position.
     * Then we increment time by 1000 s and take the Sun's position again.
     * The position difference gives us the velocity.
     * Note that here t[] and itsR[] are the heliocentric positions of the
     * Earth and not the Sun's geocentric positions.  The multiplication by
     * 1000 takes care of the 1000 s interval and the unit conversion from
     * Gm/s to km/s.
     * The GetHelio method does not affect the object's state at all, it
     * merely takes the polynomial coefficients from the static class variables
     * and returns the heliocentric geometric position.  The itsR field in
     * particular remains unchanged by GetHelio(). */

    otherTime = new Times(); otherTime.Init(); otherTime.Copy(aTime);
    otherTime.Add(-5./864.);
    GetHelio(otherTime, t,
      itsL0, itsL1, itsL2, itsL3, itsL4, itsL5,
      itsB0, itsB1, itsB2, itsB3, itsB4, itsB5,
      itsR0, itsR1, itsR2, itsR3, itsR4, itsR5);
    itsR[0] = t[0]; itsR[1] = t[1]; itsR[2] = t[2];
    otherTime.Add(10./864.);
    GetHelio(otherTime, t,
      itsL0, itsL1, itsL2, itsL3, itsL4, itsL5,
      itsB0, itsB1, itsB2, itsB3, itsB4, itsB5,
      itsR0, itsR1, itsR2, itsR3, itsR4, itsR5);
    itsV[0] = (itsR[0] - t[0]) * 1E3;
    itsV[1] = (itsR[1] - t[1]) * 1E3;
    itsV[2] = (itsR[2] - t[2]) * 1E3;

    GetHelio(aTime, t,
      itsL0, itsL1, itsL2, itsL3, itsL4, itsL5,
      itsB0, itsB1, itsB2, itsB3, itsB4, itsB5,
      itsR0, itsR1, itsR2, itsR3, itsR4, itsR5);
    itsR[0] = -t[0]; itsR[1] = -t[1]; itsR[2] = -t[2];

    return;
  }


  /**
   * Return geocentric physical ephemeris.
   *
   * <p>These comprise the elongation from the Sun (zero), the phase angle
   * (180&deg;), the illuminated fraction of the disc (one), the magnitude,
   * the apparent radius, the inclination and position angle of the rotation
   * axis, and the central meridian.</p>
   *
   * <p>The apparent radius is calulated from the equatorial radius from
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.K7
   * and the geocentric distance:</p>
   *
   * <p>&rho; = asin(696 Gm/r)</p>
   *
   * <p>The V magnitude is calculated from the value listed by
   * Albrecht Uns&ouml;ld, Bodo Baschek, 1999, <em>Der neue Kosmos, Sechste Auflage</em>, Springer Verlag, Berlin, Heidelberg, New York, Barcelona, Hongkong, London, Mailand, Paris, Singapur, Tokio, p.181,
   * corrected for the geocentric distance.</p>
   *
   * <p>V = -26.70 + 5 lg(r/au)</p>
   *
   * <p>To calcluate the orientation of the planet and its state of rotation we
   * use the expressions of
   * M.E. Davies, V.K. Abalakin, M. Bursa, J.H. Lieske, B.Morando, D. Morrison, P.K. Seidelmann, A.T. Sinclair, B. Yallop, Y.S. Tjuflin, 1996, Report of the IAU/IAG/COSPAR working group on cartographic coordinates and rotational elements of the planets and satellites: 1994, <em>Celestial Mechanics and Dynamical Astronomy</em>, <strong>63</strong>, p.127ff,
   * for the celestial coordinates of the planet's pole of rotation and the
   * prime meridian W:</p>
   *
   * <p>t = JDE - 2451545 d</p>
   * <p>&alpha;<sub>1</sub> = 286.13&#176;
   * <br>&delta;<sub>1</sub> = +63.87&#176;
   * <br>W = 84.1&#176; + 14.1844&#176;/d (t - r/c)</p>
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
   * <p>sin(PA) = sin(&alpha;<sub>1</sub> - &alpha;)
   *              cos(&delta;<sub>1</sub>) / cos(i)
   * <br>cos(PA) = [sin(&delta;<sub>1</sub>) cos(&delta;)
   *             -  cos(&delta;<sub>1</sub>) sin(&delta;)
   *                cos(&alpha;<sub>1</sub> - &alpha;)] / cos(i)</p>
   *
   * <p>To calculate the central meridian we need a similar angle K:</p>
   *
   * <p>sin(K) = [-cos(&delta;<sub>1</sub>) sin(&delta;)
   *           +   sin(&delta;<sub>1</sub>) cos(&delta;)
   *               cos(&alpha;<sub>1</sub> - &alpha;)] / cos(i)
   * <br>cos(K) = sin(&alpha;<sub>1</sub> - &alpha;) cos(&delta;) / cos(i)
   * <br>CM = K - W</p>
   *
   * <p>This routine uses geocentric &alpha; and &delta; here.</p>
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
   *   The time and topocentre for which to calculate the ephemeris. */

  public final void GetPhysics(double aOctet[], Telescope aTelescope)
  {
    double theSpher[] = new double[3];
    double theR[]     = new double[3];
    double theElong;
    double thePhase;
    double theIllum;
    double theMag;
    double theRho;
    double theBeta;
    double thePA;
    double theCM;
    double ra, theRA1, theDec1, theW, theD;

    theElong = 0.;
    thePhase = Math.PI;
    theIllum = 1.;

    /* Get the position of the Sun, geocentric J2000. */

    GetXYZ(theR);

    /* The distance of the Sun from Earth is needed below for
     * the magnitude, and various other calculations. */

    ra = Math.sqrt(theR[0] * theR[0] + theR[1] * theR[1] + theR[2] * theR[2]);

    Hmelib.Spher(theR, theSpher);

    theRho = Math.asin(0.696 / ra);
    /* lg(x) = ln(x) / ln(10) */
    theMag = -26.7 + 5. * Math.log(ra / AU) / Math.log(10.);

    theD    = aTelescope.GetJDE() - 1545.;
    theRA1  =  286.13 / Hmelib.DEGPERRAD;
    theDec1 =   63.87 / Hmelib.DEGPERRAD;
    theW    = ( 84.10  + 14.1844000 * (theD - ra / LIGHTGMD))
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
    theCM = Hmelib.NormAngle180(-theCM);

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
