
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Mars</code> class extends the {@link VSOP87
VSOP87} and holds information needed for the planet Mars.  Most notably
these are the coefficients of the polynomials in time for the planet's
heliocentric longitude, latitude and distance.  The coefficients are
A<SUB>kji</SUB>, B<SUB>kji</SUB> and C<SUB>kji</SUB>, where k is L, B or
R, j is between 0 and 5 and i between 1 and up to 64.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in the series, the precision of calculated longitudes,
latitudes and distances should be on the order of 3.5" (0.004&nbsp;Gm),
2.3" (0.0025&nbsp;Gm) and 0.003&nbsp;Gm, resp.  It is therefore not
necessary to apply the correction from the VSOP dynamical ecliptic and
equinox to FK5, as this amounts to less than 0.1".</p>

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
<dt><strong>2003-04-21:</strong> hme</dt>
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

public class Mars extends VSOP87
{

  /** VSOP87 longitude terms for Mars, 0th order in time. */

  protected static final double itsL0[] = {
          100., 3.243,     11773.377,
          105., 0.785,      8827.390,
          110., 1.052,       242.729,
          113., 3.701,      1589.073,
          117., 3.128,      7903.073,
          128., 1.807,      5088.629,
          128., 2.208,      1592.596,
          131., 4.045,     12303.068,
          138., 4.301,         7.114,
          140., 3.326,      2700.715,
          144., 1.419,       135.065,
          160., 3.949,      4562.461,
          172., 0.439,      5486.778,
          174., 2.414,       553.569,
          179., 1.006,       951.718,
          189., 1.491,      9492.146,
          193., 3.357,         3.590,
          204., 2.821,      1221.849,
          221., 3.505,       382.897,
          231., 1.282,      3870.303,
          236., 5.755,      3333.499,
          239., 5.372,      4136.910,
          274., 0.134,      3340.680,
          274., 0.542,      3340.545,
          281., 5.882,      1349.867,
          284., 5.769,      3149.164,
          293., 4.221,        20.775,
          299., 2.783,      6254.627,
          302., 4.486,      3532.061,
          307., 0.381,      6684.748,
          312., 0.999,      6677.702,
          415., 0.497,       213.299,
          426., 0.554,      6283.076,
          472., 3.625,      1194.447,
          550., 3.810,         0.980,
          553., 4.475,      1748.016,
          636., 2.922,      8432.764,
          655., 0.489,      3127.313,
          713., 3.663,      1059.382,
          724., 0.675,      3738.761,
          749., 3.822,       155.420,
          833., 2.464,      3340.595,
          833., 4.495,      3340.630,
          859., 2.401,      2914.014,
          892., 0.183,     16703.062,
         1025., 3.6933,     8962.4553,
         1264., 3.6228,     5092.1520,
         1286., 3.0680,     2146.1654,
         1528., 1.1498,     6151.5339,
         1546., 2.9158,     1751.5395,
         1799., 0.6563,      529.6910,
         2389., 5.0390,      796.2980,
         2580., 0.0300,     3344.1355,
         2628., 0.6481,     3337.0893,
         2938., 6.0789,        0.0673,
         3075., 0.8570,      191.4483,
         3575., 1.6619,     2544.3144,
         4161., 0.2281,     2942.4634,
         6798., 0.3646,      398.1490,
         7775., 3.3397,     5621.8429,
         8716., 6.1101,    13362.4497,
         8927., 4.1570,        0.0173,
        10610., 2.93959,    2281.23050,
        12316., 0.84956,    2810.92146,
        27745., 5.97050,       3.52312,
        91798., 5.75479,   10021.83728,
      1108217., 5.4009984,  6681.2248534,
     18656368., 5.05037100, 3340.61242670,
    620347712., 0.,            0.,
            0., 0.,            0.
  };  /* 69 terms, error < 3.5" or 0.004 Gm. */


  /** VSOP87 longitude terms for Mars, 1st order in time. */

  protected static final double itsL1[] = {
              27., 5.11,      2700.72,
              27., 3.89,      1221.85,
              28., 0.05,      9492.15,
              33., 5.41,      6283.08,
              40., 5.32,     20043.67,
              40., 2.73,         7.11,
              41., 0.71,      1592.60,
              47., 1.31,      3185.19,
              48., 1.18,      3333.50,
              48., 4.87,       213.30,
              57., 3.89,      4136.91,
              62., 4.15,      3149.16,
              65., 1.02,      3340.60,
              65., 3.05,      3340.63,
              68., 5.02,       382.90,
              72., 3.86,      2914.01,
              73., 5.84,       242.73,
              73., 2.50,       951.72,
              80., 2.25,      8962.46,
              81., 4.43,       529.69,
              83., 5.30,      6684.75,
              85., 3.91,       553.57,
              91., 1.10,      1349.87,
             114., 5.428,     3738.761,
             114., 2.129,     1194.447,
             117., 2.213,     1059.382,
             118., 6.024,     6151.534,
             134., 5.974,     1748.016,
             134., 2.233,        0.980,
             158., 4.185,     1751.540,
             169., 1.329,     3337.089,
             206., 4.569,     2146.165,
             283., 3.160,     2544.314,
             314., 4.963,    16703.062,
             382., 3.539,      796.298,
             430., 5.317,      155.420,
             433., 2.561,      191.448,
             521., 4.994,     3344.136,
             538., 5.016,      398.149,
             842., 4.459,     2281.230,
            2485., 4.6128,   13362.4497,
            3452., 4.7321,       3.5231,
           19963., 4.26594,  10021.83728,
          164901., 3.926313,  6681.224853,
         1458227., 3.6042605, 3340.6124267,
    334085627474., 0.,           0.,
               0., 0.,           0.
  };  /* 46 terms, error < 0.76" t or 0.0008 Gm t. */


  /** VSOP87 longitude terms for Mars, 2nd order in time. */

  protected static final double itsL2[] = {
        6., 2.34,    3097.88,
        6., 5.48,    1592.60,
        7., 2.38,    4136.91,
        7., 2.58,    3149.16,
        8., 5.46,    1751.54,
        9., 3.88,    3738.76,
        9., 3.83,   20043.67,
        9., 0.68,    1059.38,
       10., 0.25,     382.90,
       11., 4.72,    2544.31,
       12., 3.86,    6684.75,
       13., 0.60,    1194.45,
       14., 2.62,    1349.87,
       14., 4.02,     951.72,
       15., 6.10,    3185.19,
       16., 1.22,    1748.02,
       16., 6.11,    2146.17,
       16., 0.66,       0.98,
       20., 5.42,     553.57,
       22., 3.45,     398.15,
       23., 4.33,     242.73,
       30., 2.00,     796.30,
       32., 4.14,     191.45,
       34., 6.00,    2281.23,
       54., 3.54,    3344.14,
       62., 3.49,   16703.06,
      121., 0.543,    155.420,
      222., 3.194,      3.523,
      398., 3.141,  13362.450,
     2465., 2.8000, 10021.8373,
    13908., 2.45742, 6681.22485,
    54188., 0.,         0.,
    58016., 2.04979, 3340.61243,
        0., 0.,         0.
  };  /* 33 terms, error < 0.14" t^2 or 0.00016 Gm t^2. */


  /** VSOP87 longitude terms for Mars, 3rd order in time. */

  protected static final double itsL3[] = {
       3., 0.65,    553.57,
       3., 4.59,   3185.19,
       4., 2.02,   3344.14,
       5., 2.82,    242.73,
       8., 2.00,  16703.06,
      10., 1.58,      3.52,
      23., 2.05,    155.42,
      26., 0.,        0.,
      41., 1.65,  13362.45,
     188., 1.288, 10021.837,
     662., 0.885,  6681.225,
    1482., 0.4443, 3340.6124,
       0., 0.,        0.
  };  /* 12 terms, error < 0.04" t^3 or 0.00005 Gm t^3. */


  /** VSOP87 longitude terms for Mars, 4th order in time. */

  protected static final double itsL4[] = {
      1., 0.49, 16703.06,
      1., 1.32,   242.73,
      3., 3.56,   155.42,
      3., 0.13, 13362.45,
     11., 6.03, 10021.84,
     24., 5.14,  3340.61,
     29., 5.64,  6681.22,
    114., 3.1416,   0.,
      0., 0.,       0.
  };  /* 8 terms, error < 0.018" t^4 or 0.000003 Gm t^4. */


  /** VSOP87 longitude terms for Mars, 5th order in time. */

  protected static final double itsL5[] = {
    1., 4.04, 6681.22,
    1., 3.14,    0.,
    0., 0.,      0.
  };  /* 2 terms, error < 0.006" t^5 or 0.0000065 Gm t^5. */


  /** VSOP87 latitude terms for Mars, 0th order in time. */

  protected static final double itsB0[] = {
        139., 2.418,     8962.455,
        143., 1.182,     3340.595,
        143., 3.213,     3340.630,
        149., 2.165,     5621.843,
        160., 2.232,     1059.382,
        163., 4.264,      529.691,
        182., 6.136,     6151.534,
        293., 3.793,     2281.230,
        399., 5.131,    16703.062,
        443., 5.652,     3337.089,
        443., 5.026,     3344.136,
       3484., 4.7881,   13362.4497,
      31366., 4.44651,  10021.83728,
     289105., 0.,           0.,
     298033., 4.106170,  6681.224853,
    3197135., 3.7683204, 3340.6124267,
          0., 0.,           0.
  };  /* 16 terms, error < 2.3" or 0.0025 Gm. */


  /** VSOP87 latitude terms for Mars, 1st order in time. */

  protected static final double itsB1[] = {
        26., 2.48,     2281.230,
        33., 3.46,     5621.843,
        79., 3.72,    16703.062,
       102., 0.776,    3337.0893,
       426., 3.408,   13362.4497,
      1472., 3.2021,  10021.83728,
      9671., 5.4788,   6681.22485,
     14116., 3.14159,     0.,
    350069., 5.368478, 3340.6124267,
         0., 0.,          0.
  };  /* 9 terms, error < 0.32" t or 0.0004 Gm t. */


  /** VSOP87 latitude terms for Mars, 2nd order in time. */

  protected static final double itsB2[] = {
        8., 2.25,   16703.06,
       12., 2.24,    3337.09,
       21., 0.92,   10021.84,
       26., 1.90,   13362.45,
      302., 5.559,   6681.225,
     4987., 3.1416,     0.,
    16727., 0.60221, 3340.61243,
        0., 0.,         0.
  };  /* 7 terms, error < 0.087" t^2 or 0.0001 Gm t^2. */


  /** VSOP87 latitude terms for Mars, 3rd order in time. */

  protected static final double itsB3[] = {
      3., 3.45, 10021.84,
     14., 1.80,  6681.23,
     43., 0.,       0.,
    607., 1.981, 3340.612,
      0., 0.,       0.
  };  /* 4 terms, error < 0.025" t^3 or 0.00003 Gm t^3. */


  /** VSOP87 latitude terms for Mars, 4th order in time. */

  protected static final double itsB4[] = {
     1., 0.50, 6681.22,
    11., 3.46, 3340.61,
    13., 0.,      0.,
     0., 0.,      0.
  };  /* 3 terms, error < 0.007" t^4 or 0.000008 Gm t^4. */


  /** VSOP87 latitude terms for Mars, 5th order in time. */

  protected static final double itsB5[] = {
    0., 0., 0.
  };  /* No terms. */


  /** VSOP87 distance terms for Mars, 0th order in time. */

  protected static final double itsR0[] = {
          164., 3.799,      4136.910,
          176., 5.953,      3870.303,
          179., 4.184,      3333.499,
          183., 5.081,      6684.748,
          186., 5.699,      6677.702,
          208., 4.846,      3340.680,
          208., 5.255,      3340.545,
          219., 5.583,       191.448,
          223., 4.199,      3149.164,
          228., 3.255,      6872.673,
          234., 5.105,      5486.778,
          239., 2.037,      1194.447,
          270., 3.764,      5884.927,
          275., 2.908,      1748.016,
          276., 1.218,      6254.627,
          280., 5.257,      6283.076,
          284., 2.907,      3532.061,
          348., 4.832,     16703.062,
          473., 5.199,      3127.313,
          526., 5.383,      3738.761,
          574., 0.829,      2914.014,
          630., 1.287,      1751.540,
          633., 0.894,      3340.595,
          633., 2.924,      3340.630,
          692., 2.134,      8962.455,
          726., 1.245,      8432.764,
          741., 1.499,      2146.165,
          798., 3.448,       796.298,
          807., 2.102,      1059.382,
          899., 4.408,       529.691,
          992., 5.839,      6151.534,
         1103., 5.0091,      398.1490,
         1167., 2.1126,     5092.1520,
         1960., 4.7425,     3344.1355,
         1999., 5.3606,     3337.0893,
         2307., 0.0908,     2544.3144,
         2484., 4.9255,     2942.4634,
         3825., 4.4941,    13362.4497,
         5523., 1.3644,     2281.2305,
         7485., 1.7724,     5621.8429,
         8110., 5.5596,     2810.9215,
        46179., 4.15595,   10021.83728,
       660776., 3.817834,   6681.224853,
     14184953., 3.47971284, 3340.61242670,
    153033488., 0.,            0.,
            0., 0.,            0.
  };  /* 45 terms, error < 0.0033 Gm. */


  /** VSOP87 distance terms for Mars, 1st order in time. */

  protected static final double itsR1[] = {
         39., 2.32,      4136.91,
         48., 2.29,      2914.01,
         48., 2.58,      3149.16,
         49., 1.48,      3340.63,
         49., 5.73,      3340.60,
         51., 3.73,      6684.75,
         54., 0.68,      8962.46,
         58., 0.54,      1194.45,
         66., 4.41,      1748.02,
         67., 2.55,      1751.54,
         72., 2.76,       529.69,
         76., 4.45,      6151.53,
         83., 3.86,      3738.76,
         88., 3.42,       398.15,
        118., 2.998,     2146.165,
        127., 1.954,      796.298,
        128., 0.630,     1059.382,
        128., 6.043,     3337.089,
        136., 3.385,    16703.062,
        183., 1.584,     2544.314,
        396., 3.423,     3344.136,
        439., 2.888,     2281.230,
       1195., 3.0470,   13362.4497,
      10816., 2.70888,  10021.83728,
      12877., 0.,           0.,
     103176., 2.370718,  6681.224853,
    1107433., 2.0325052, 3340.6124267,
          0., 0.,           0.
  };  /* 43 terms, error < 0.0006 Gm t. */


  /** VSOP87 distance terms for Mars, 2nd order in time. */

  protected static final double itsR2[] = {
       10., 0.42,     796.30,
       10., 5.39,    1059.382,
       12., 4.53,    3185.192,
       18., 4.43,    2281.230,
       27., 1.92,   16703.062,
       41., 1.97,    3344.136,
       52., 3.14,       0.,
      187., 1.573,  13362.4497,
     1275., 1.2259, 10021.83728,
     8138., 0.8700,  6681.22485,
    44242., 0.47931, 3340.612427,
        0., 0.,         0.
  };  /* 11 terms, error < 0.0001 Gm t^2. */


  /** VSOP87 distance terms for Mars, 3rd order in time. */

  protected static final double itsR3[] = {
       3., 0.43,  16703.06,
       5., 3.14,      0.,
      20., 0.08,  13362.45,
     100., 5.997, 10021.837,
     424., 5.613,  6681.225,
    1113., 5.1499, 3340.6124,
       0., 0.,        0.
  };  /* 6 terms, error < 0.00002 Gm t^3. */


  /** VSOP87 distance terms for Mars, 4th order in time. */

  protected static final double itsR4[] = {
     2., 4.84, 13362.45,
     6., 4.46, 10021.84,
    16., 4.05,  6681.22,
    20., 3.58,  3340.61,
     0., 0.,       0.
  };  /* 4 terms, error < 0.00001 Gm t^4. */


  /** VSOP87 distance terms for Mars, 5th order in time. */

  protected static final double itsR5[] = {
    0., 0., 0.
  };  /* No terms. */


  /**
   * Initialise.
   *
   * <p>This initialises the Mars object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal
   * equinox).</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for Mars";
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

    Mars      theObject;
    theObject = new Mars();
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
   * Set Mars for the given time.
   *
   * <p>This calculates for the given time Mars's J2000 coordinates.</p>
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

    itsName = "Mars";

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
   * <p>&rho; = asin(3397.4 km/r)</p>
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
   * <p>&alpha;<SUB>1</SUB> = 317.681&deg; - 0.108&deg; T
   * <br />&delta;<SUB>1</SUB> = 52.886&deg; - 0.061&deg; T
   * <br />W = 176.901&deg; + 350.8919830&deg;/d (t - r/c)</p>
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
   * <p>V = -1.52 + 5 lg(r/au) + 5 lg(R/au) + 1.6 A</p>
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
   *   the central meridian (labelled System I,
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

    theRho = Math.asin(0.0033974 / ra);

    theRA1  = (317.681 -   0.108     * theT) / Hmelib.DEGPERRAD;
    theDec1 = ( 52.886 -   0.061     * theT) / Hmelib.DEGPERRAD;
    theW    = (176.901 + 350.8919830 * (theD - ra / LIGHTGMD))
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

    theMag = -1.52 + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag += 1.6  * theA;

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
