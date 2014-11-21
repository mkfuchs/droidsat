
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Uranus</code> class extends the {@link VSOP87
VSOP87} and holds information needed for the planet Uranus.  Most notably
these are the coefficients of the polynomials in time for the planet's
heliocentric longitude, latitude and distance.  The coefficients are
A<SUB>kji</SUB>, B<SUB>kji</SUB> and C<SUB>kji</SUB>, where k is L, B or
R, j is between 0 and 5 and i between 1 and up to 38.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in
the series, the precision of calculated longitudes, latitudes and
distances should be on the order of 4.1" (0.0056&nbsp;Gm), 2.2"
(0.0031&nbsp;Gm) and 0.05&nbsp;Gm, resp.  It is therefore not necessary
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
<dd>Revert to using geocentric J2000 in GetPhysics.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */

public class Uranus extends VSOP87
{

  /** VSOP87 longitude terms for Uranus, 0th order in time. */

  protected static final double itsL0[] = {
          103., 0.681,     14.978,
          104., 1.458,     24.379,
          104., 5.028,      0.751,
          109., 5.706,     77.963,
          110., 2.027,    554.070,
          124., 1.374,      7.114,
          139., 4.260,    909.819,
          139., 5.386,     32.195,
          143., 1.300,     35.425,
          147., 1.263,     59.804,
          158., 0.738,     54.175,
          163., 3.050,    112.915,
          165., 1.424,    106.977,
          169., 5.879,     18.159,
          170., 3.677,      5.417,
          172., 5.680,    219.891,
          173., 1.539,    160.609,
          182., 3.536,     79.235,
          187., 1.319,      0.160,
          193., 0.916,    453.425,
          194., 1.888,    456.394,
          199., 0.956,    152.532,
          202., 1.297,      0.048,
          208., 5.580,     68.844,
          216., 4.778,    340.771,
          217., 6.142,      5.938,
          220., 1.922,     67.668,
          223., 2.843,      0.261,
          224., 0.516,     84.343,
          239., 2.350,    137.033,
          249., 4.746,    225.829,
          252., 1.637,    221.376,
          294., 5.839,     39.618,
          300., 5.644,     22.091,
          310., 5.833,    145.631,
          379., 2.350,     56.622,
          396., 5.870,    351.817,
          399., 0.338,    415.552,
          405., 5.987,      8.077,
          434., 5.521,    183.243,
          467., 0.415,    145.110,
          471., 1.407,    184.727,
          483., 2.106,      0.963,
          524., 2.013,    299.126,
          559., 3.358,      0.521,
          607., 5.432,    529.691,
          628., 0.182,    984.600,
          653., 0.966,     78.714,
          708., 5.183,    213.299,
          946., 1.192,    127.472,
         1072., 0.2356,    62.2514,
         1090., 1.7750,    12.5302,
         1150., 0.9334,     3.1814,
         1151., 4.1790,    33.6796,
         1221., 0.1990,   108.4612,
         1244., 0.9161,     2.4477,
         1282., 0.5427,   222.8603,
         1284., 3.1135,   202.2534,
         1372., 4.1964,   111.4302,
         1376., 2.0428,    65.2204,
         1533., 2.5859,    52.6902,
         1667., 3.6274,   380.1278,
         1992., 4.9244,   277.0350,
         2051., 1.5177,     0.1119,
         2149., 0.6075,    38.1330,
         2273., 4.3660,    70.3282,
         2922., 5.3524,    85.8273,
         2927., 4.6290,     9.5612,
         3144., 4.7520,    77.7505,
         3355., 1.0655,     4.4534,
         3490., 5.4831,   146.5943,
         4052., 2.2775,   151.0477,
         4220., 3.2333,    70.8494,
         7546., 5.2363,   109.9457,
         9527., 2.9552,    35.1641,
        10998., 0.48865,  138.51750,
        11163., 5.82682,  224.34480,
        14613., 4.73732,    3.93215,
        17819., 1.74437,   36.64856,
        21079., 4.36059,  148.07872,
        25711., 6.11380,  454.90937,
        26469., 3.14152,   71.81265,
        61951., 2.85099,   11.04570,
        61999., 2.26952,    2.96895,
        68893., 6.09292,   76.26607,
        70328., 5.39254,   63.73590,
       272328., 3.358237, 149.563197,
       365982., 1.899622,  73.297126,
      1504248., 3.6271926,  1.4844727,
      9260408., 0.8910642, 74.7815986,
    548129294., 0.,         0.,
            0., 0.,         0.
  };  /* 91 terms, error < 4.1" or 0.0056 Gm. */


  /** VSOP87 longitude terms for Uranus, 1st order in time. */

  protected static final double itsL1[] = {
            25., 5.74,    380.13,
            26., 4.99,    137.03,
            27., 5.54,    131.40,
            27., 6.15,    299.13,
            29., 4.52,     84.34,
            29., 1.15,    462.02,
            30., 1.66,    447.80,
            31., 5.46,    160.61,
            31., 5.50,     59.80,
            31., 5.62,    984.60,
            35., 5.08,     38.13,
            36., 3.33,     71.60,
            36., 3.29,      8.08,
            36., 5.90,     33.68,
            39., 4.92,    222.86,
            43., 5.72,      5.42,
            44., 5.91,      7.11,
            47., 3.54,    351.82,
            59., 3.70,     67.67,
            69., 4.05,     77.96,
            72., 6.05,     70.33,
            81., 2.64,     22.09,
            88., 6.16,    202.25,
            88., 3.99,     18.16,
           102., 6.034,     0.112,
           102., 4.188,   145.631,
           116., 3.732,    65.220,
           121., 4.148,   127.472,
           143., 2.590,    62.251,
           152., 2.942,    77.751,
           154., 4.652,    35.164,
           155., 5.591,     4.453,
           158., 2.909,     0.963,
           171., 3.001,    78.714,
           180., 5.684,    12.530,
           184., 0.284,   151.048,
           189., 4.202,    56.622,
           206., 2.363,     2.448,
           317., 5.579,    52.690,
           348., 2.454,     9.561,
           354., 2.583,   148.079,
           427., 4.731,    71.813,
           446., 3.723,   224.345,
           450., 4.138,   138.517,
           482., 2.984,    85.827,
           767., 1.996,    73.297,
           791., 5.436,     3.181,
          1233., 1.5863,   70.8494,
          1927., 0.5301,    2.9690,
          2284., 4.1737,   76.2661,
          3899., 0.4648,    3.9322,
          7842., 1.3198,  149.5632,
          8266., 1.5022,   63.7359,
          9258., 0.4284,   11.0457,
         24456., 1.71256,   1.48447,
        154458., 5.242017, 74.781599,
    7502543122., 0.,        0.,
             0., 0.,        0.
  };


  /** VSOP87 longitude terms for Uranus, 2nd order in time. */

  protected static final double itsL2[] = {
        6., 5.73,  462.02,
        6., 4.52,  151.05,
        6., 5.45,   65.22,
        6., 3.36,  447.80,
        7., 1.25,    5.42,
        8., 5.50,   67.67,
        9., 4.26,    7.11,
       10., 4.46,   62.25,
       10., 5.16,   71.60,
       11., 0.08,  127.47,
       12., 0.02,   22.09,
       17., 3.47,   12.53,
       17., 2.54,  145.63,
       21., 2.17,  224.34,
       21., 2.40,   77.96,
       22., 4.82,   78.71,
       22., 5.99,  138.52,
       24., 2.11,   18.16,
       29., 5.10,   73.30,
       33., 0.86,    9.56,
       37., 4.46,    2.97,
       38., 1.78,   52.69,
       45., 0.81,   85.83,
       45., 3.91,    2.45,
       49., 6.03,   56.62,
       54., 1.44,   76.27,
      182., 6.218,  70.849,
      239., 5.858, 149.563,
      258., 3.691,   3.181,
      529., 4.923,   1.484,
      542., 2.276,   3.932,
      552., 3.258,  63.736,
      769., 4.526,  11.046,
     2358., 2.2601, 74.7816,
    53033., 0.,      0.,
        0., 0.,      0.
  };


  /** VSOP87 longitude terms for Uranus, 3rd order in time. */

  protected static final double itsL3[] = {
      2., 5.66,   9.56,
      2., 0.86, 145.63,
      3., 0.37,  78.71,
      3., 4.13,  52.69,
      3., 4.98,  85.83,
      4., 0.95,  77.96,
      4., 5.39,  76.27,
      4., 0.23,  18.16,
      9., 1.58,  56.62,
     20., 2.31, 149.56,
     21., 4.55,  70.85,
     25., 4.89,  63.74,
     44., 2.96,   1.48,
     45., 2.04,   3.18,
     46., 0.,     0.,
     53., 2.39,  11.05,
     68., 4.12,   3.93,
    121., 0.024, 74.782,
      0., 0.,     0.
  };


  /** VSOP87 longitude terms for Uranus, 4th order in time. */

  protected static final double itsL4[] = {
      1., 3.42,  56.62,
      3., 0.35,  11.05,
      6., 4.58,  74.78,
    114., 3.142,  0.,
      0., 0.,     0.
  };


  /** VSOP87 longitude terms for Uranus, 5th order in time. */

  protected static final double itsL5[] = {0., 0., 0.};


  /** VSOP87 latitude terms for Uranus, 0th order in time. */

  protected static final double itsB0[] = {
        102., 2.619,     78.714,
        106., 0.941,     70.328,
        116., 5.739,     70.849,
        144., 5.962,     35.164,
        160., 5.336,    111.430,
        174., 1.937,    380.128,
        175., 1.236,    146.594,
        180., 3.725,    299.126,
        216., 1.591,     38.133,
        233., 2.257,    222.860,
        245., 0.788,      2.969,
        420., 5.213,     11.046,
        431., 3.554,    213.299,
        435., 0.341,     77.751,
        437., 3.381,    529.691,
        463., 0.743,     85.827,
        522., 3.321,    138.517,
        761., 6.140,     71.813,
        924., 4.038,    151.048,
       1522., 0.2796,    63.7359,
       2010., 6.0555,   148.0787,
       2972., 2.2437,     1.4845,
       3259., 1.2612,   224.3448,
       9926., 0.5763,    73.2971,
       9964., 1.6160,    76.2661,
      61601., 3.14159,    0.,
      62341., 5.08111,  149.56320,
    1346278., 2.6187781, 74.7815986,
          0., 0.,         0.
  };  /* 28 terms, error < 2.2" or 0.0031 Gm. */


  /** VSOP87 latitude terms for Uranus, 1st order in time. */

  protected static final double itsB1[] = {
        26., 0.42,    380.13,
        27., 5.34,    213.30,
        30., 2.56,      2.97,
        32., 3.77,    222.86,
        41., 4.45,     78.71,
        42., 1.21,     11.05,
        54., 1.70,     77.75,
        56., 3.40,     85.83,
        83., 3.59,     71.81,
       111., 5.329,   138.517,
       112., 5.573,   151.048,
       154., 3.786,    63.736,
       307., 1.255,   148.079,
       400., 2.848,   224.345,
       451., 3.777,     1.484,
      1369., 3.0686,   76.2661,
      1374., 0.,        0.,
      1726., 2.1219,   73.2971,
      8563., 0.3382,  149.5632,
    206366., 4.123943, 74.781599,
         0., 0.,        0.
  };


  /** VSOP87 latitude terms for Uranus, 2nd order in time. */

  protected static final double itsB2[] = {
       8., 6.27,   78.71,
      10., 5.00,  224.34,
      14., 5.07,   63.74,
      14., 2.85,  148.08,
      15., 0.88,  138.52,
      20., 5.46,    1.48,
      45., 4.88,   76.27,
      95., 3.84,   73.30,
     286., 2.177, 149.563,
     557., 0.,      0.,
    9212., 5.8004, 74.7816,
       0., 0.,      0.
  };


  /** VSOP87 latitude terms for Uranus, 3rd order in time. */

  protected static final double itsB3[] = {
      3., 5.78,  73.30,
      6., 4.01, 149.56,
     11., 3.14,   0.,
    268., 1.251, 74.782,
      0., 0.,     0.
  };


  /** VSOP87 latitude terms for Uranus, 4th order in time. */

  protected static final double itsB4[] = {
    6., 2.85, 74.78,
    0., 0.,    0.
  };


  /** VSOP87 latitude terms for Uranus, 5th order in time. */

  protected static final double itsB5[] = {0., 0., 0.};


  /** VSOP87 distance terms for Uranus, 0th order in time. */

  protected static final double itsR0[] = {
          2183., 2.9404,    305.3462,
          2364., 0.4425,    554.0700,
          2538., 4.8546,    131.4039,
          2865., 0.3100,     12.5302,
          2938., 3.6766,    140.0020,
          2940., 2.1464,    137.0330,
          2942., 0.4239,    299.1264,
          2963., 0.8298,     56.6224,
          3102., 4.1403,    219.8914,
          3687., 2.4872,    453.4249,
          3781., 3.4584,    456.3938,
          3802., 6.1099,    184.7273,
          3919., 4.2502,     39.6175,
          4079., 3.2206,    340.7709,
          5238., 2.6296,     33.6796,
          5445., 5.1058,    145.1098,
          5524., 3.1150,      9.5612,
          6046., 5.6796,     78.7138,
          7329., 3.9728,    183.2428,
          7449., 0.7949,    351.8166,
          8402., 5.0388,    415.5525,
          8421., 5.2535,    222.8603,
          9111., 4.9964,     62.2514,
         10793., 1.42105,   213.29910,
         11495., 0.43774,    65.22037,
         11696., 3.29826,     3.93215,
         11853., 0.99343,    52.69020,
         11959., 1.75044,   984.60033,
         12328., 5.96039,   127.47180,
         12897., 2.62154,   111.43016,
         14702., 4.90434,   108.46122,
         15503., 5.35405,    38.13304,
         17901., 0.55455,     2.96895,
         20472., 1.55589,   202.25340,
         20473., 2.79640,    70.32818,
         22637., 0.72519,   529.69097,
         25620., 5.25656,   380.12777,
         25786., 3.78538,    85.82730,
         29156., 3.18056,    77.75054,
         30349., 0.70100,   151.04767,
         36755., 3.88649,   146.59425,
         39010., 1.66971,    70.84945,
         39026., 3.36235,   277.03499,
         46677., 1.39977,    35.16409,
         71424., 4.24509,   224.34480,
         89806., 3.66105,   109.94569,
         93192., 0.17437,    36.64856,
        143706., 1.383686,   11.045700,
        161858., 2.791379,  148.078724,
        190522., 1.998094,    1.484473,
        243508., 1.570866,   71.812653,
        338526., 1.580027,  138.517497,
        496404., 1.401399,  454.909367,
        602248., 3.860038,   63.735898,
        649322., 4.522473,   76.266071,
       2055653., 1.7829517, 149.5631971,
       3440836., 0.3283610,  73.2971259,
      88784984., 5.60377527, 74.78159857,
    1921264848., 0.,          0.,
             0., 0.,          0.
  };  /* 59 terms, error < 0.005 Gm. */


  /** VSOP87 distance terms for Uranus, 1st order in time. */

  protected static final double itsR1[] = {
        528., 5.151,      2.969,
        530., 5.917,    213.299,
        562., 2.718,    462.023,
        575., 3.231,    447.796,
        604., 0.907,    984.600,
        624., 0.863,      9.561,
        647., 4.473,     70.328,
        687., 2.499,     77.963,
        744., 3.076,     35.164,
        862., 5.055,    351.817,
        992., 2.172,     65.220,
       1033., 0.2646,   131.4039,
       1228., 1.0470,    62.2514,
       1403., 1.3699,    77.7505,
       1413., 4.5746,   202.2534,
       1490., 2.6756,    56.6224,
       1508., 5.0600,   151.0477,
       1584., 1.4305,    78.7138,
       1645., 2.6535,   127.4718,
       2429., 3.9944,    52.6902,
       2564., 0.9808,   148.0787,
       3060., 0.1532,     1.4845,
       3229., 5.2550,     3.9322,
       3506., 2.5835,   138.5175,
       3578., 2.3116,   224.3448,
       3927., 3.1551,    71.8127,
       4244., 1.4169,    85.8273,
       7497., 0.4236,    73.2971,
      11405., 0.01848,   70.84945,
      20857., 5.24625,   11.04570,
      21468., 2.60177,   76.26607,
      24060., 3.14159,    0.,
      68627., 6.13411,  149.56320,
      71212., 6.22601,   63.73590,
    1479896., 3.6720571, 74.7815986,
          0., 0.,         0.
  };


  /** VSOP87 distance terms for Uranus, 2nd order in time. */

  protected static final double itsR2[] = {
      129., 2.081,    3.181,
      149., 4.898,  127.472,
      205., 3.248,   78.714,
      216., 0.848,   77.963,
      220., 1.964,  131.404,
      273., 3.847,  138.517,
      287., 3.534,   73.297,
      292., 0.204,   52.690,
      390., 5.527,   85.827,
      390., 4.496,   56.622,
      461., 0.767,    3.932,
      500., 6.172,   76.266,
      770., 0.,       0.,
     1434., 3.5212, 149.5632,
     1650., 3.0966,  11.0457,
     1682., 4.6483,  70.8494,
     4727., 1.6990,  63.7359,
    22440., 0.69953, 74.78160,
        0., 0.,       0.
  };


  /** VSOP87 distance terms for Uranus, 3rd order in time. */

  protected static final double itsR3[] = {
      32., 3.60,  131.40,
      34., 3.82,   76.27,
      36., 5.65,   77.96,
      55., 2.59,    3.93,
      72., 0.03,   56.62,
      73., 1.00,  149.56,
     105., 0.958,  11.046,
     196., 2.980,  70.849,
     212., 3.343,  63.736,
    1164., 4.7345, 74.7816,
       0., 0.,      0.
  };


  /** VSOP87 distance terms for Uranus, 4th order in time. */

  protected static final double itsR4[] = {
    10., 1.91, 56.62,
    53., 3.01, 74.78,
     0., 0., 0.
  };


  /** VSOP87 distance terms for Uranus, 5th order in time. */

  protected static final double itsR5[] = {0., 0., 0.};


  /**
   * Initialise.
   *
   * <p>This initialises the Uranus object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal
   * equinox).</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for Uranus";
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

    Uranus    theObject;
    theObject = new Uranus();
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
   * Set Uranus for the given time.
   *
   * <p>This calculates for the given time Uranus's J2000 coordinates.</p>
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

    itsName = "Uranus";

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
   * <p>&rho; = asin(25559.4 km/r)</p>
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
   * <p>&alpha;<SUB>1</SUB> = 257.311&deg;
   * <br />&delta;<SUB>1</SUB> = -15.175&deg;
   * <br />W = 203.81&deg; - 501.1600928&deg;/d (t - r/c)</p>
   *
   * <p>W here refers to System III, which applies to the magnetic field of
   * Uranus.</p>
   *
   * <p>According to
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E87
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
   * <p>V = -7.19 + 5 lg(r/au) + 5 lg(R/au) - 2.5 lg(L)</p>
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
    double ra, rb, theRA1, theDec1, theW, theD;

    /* Get the position of the Sun and of this planet, geocentric J2000. */

    aTelescope.itsSun.GetPos(theSunPos);
    GetXYZ(theR);

    /* The time parameters as used below. */

    theD = aTelescope.GetJDE() - 1545.;
    

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

    theRho = Math.asin(0.0255594 / ra);

    theRA1  =  257.311                       / Hmelib.DEGPERRAD;
    theDec1 =  -15.175                       / Hmelib.DEGPERRAD;
    theW    = (203.81  - 501.1600928 * (theD - ra / LIGHTGMD))
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

    theMag = -7.19 + 5. * Math.log(ra / AU) / Math.log(10.)
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
