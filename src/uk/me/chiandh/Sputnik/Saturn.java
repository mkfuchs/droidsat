
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Saturn</code> class extends the {@link VSOP87
VSOP87} and holds information needed for the planet Saturn.  Most notably
these are the coefficients of the polynomials in time for the planet's
heliocentric longitude, latitude and distance.  The coefficients are
A<SUB>kji</SUB>, B<SUB>kji</SUB> and C<SUB>kji</SUB>, where k is L, B or
R, j is between 0 and 5 and i between 1 and up to 64.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in
the series, the precision of calculated longitudes, latitudes and
distances should be on the order of 4" (0.028&nbsp;Gm), 2.7"
(0.019&nbsp;Gm) and 0.040&nbsp;Gm, resp.  It is therefore not necessary
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
<dt><strong>2003-06-01:</strong> hme</dt>
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
<dd>Calculate the central meridian not only for System III, but also for
  System I.</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Revert to using geocentric J2000 in GetPhysics.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */

public class Saturn extends VSOP87
{

  /** VSOP87 longitude terms for Saturn, 0th order in time. */

  protected static final double itsL0[] = {
         101., 4.965,      269.921,
         103., 1.197,     1685.052,
         104., 2.192,       88.866,
         107., 4.012,      956.289,
         109., 3.438,      536.805,
         110., 0.166,        1.484,
         112., 1.105,      191.208,
         114., 5.594,     1059.382,
         117., 2.679,     1155.361,
         118., 5.341,      554.070,
         122., 1.976,        4.666,
         125., 6.277,     1898.351,
         131., 4.068,       10.295,
         140., 4.295,       21.341,
         146., 6.231,      195.140,
         148., 1.535,        5.629,
         149., 5.736,       52.690,
         165., 0.440,        5.417,
         174., 1.863,        0.751,
         182., 5.491,        2.921,
         184., 0.973,        4.193,
         185., 3.503,      149.563,
         204., 6.011,      265.989,
         208., 0.483,     1162.475,
         208., 1.283,       39.357,
         209., 1.345,      625.670,
         220., 4.204,      200.769,
         227., 4.910,       12.530,
         249., 1.470,     1368.660,
         278., 0.400,      211.815,
         287., 2.370,      351.817,
         309., 3.495,      216.480,
         322., 2.572,      647.011,
         322., 0.961,      203.738,
         330., 0.247,     1581.959,
         343., 0.246,        0.521,
         347., 1.539,      340.771,
         355., 3.013,      838.969,
         372., 2.278,      217.231,
         449., 1.290,      127.472,
         452., 1.044,      490.334,
         474., 5.475,      742.990,
         478., 2.965,      137.033,
         530., 4.449,      117.320,
         543., 1.518,        9.561,
         546., 2.127,      350.332,
         580., 3.093,       74.782,
         625., 0.970,      210.118,
         634., 2.299,      412.371,
         654., 1.599,        0.048,
         687., 1.747,     1052.268,
         744., 5.253,      224.345,
         749., 2.144,      853.196,
         789., 5.007,        0.963,
         849., 3.191,      209.367,
         853., 3.421,      175.166,
         957., 0.507,     1265.567,
        1017., 3.7170,     227.5262,
        1087., 4.1834,       2.4477,
        1124., 2.8373,     415.5525,
        1391., 4.0233,     323.5054,
        1581., 4.3727,     309.2783,
        1640., 5.5050,     846.0828,
        1758., 3.2658,     522.5774,
        2461., 2.0316,     735.8765,
        2954., 0.9828,      95.9792,
        3269., 0.7749,     949.1756,
        3874., 3.2228,     138.5175,
        4006., 2.2448,      63.7359,
        4593., 0.6198,     199.0720,
        5020., 3.1779,     433.7117,
        5228., 4.2078,       3.1814,
        5863., 0.2366,     529.6910,
        6126., 1.7633,     277.0350,
       10725., 3.12940,    202.25340,
       13005., 5.98119,     11.04570,
       13160., 4.44891,     14.22709,
       14610., 1.56519,      3.93215,
       14907., 5.76903,    316.39187,
       15054., 2.71670,    639.89729,
       15820., 0.93809,    632.78374,
       16574., 0.43719,    419.48464,
       23990., 4.66977,    110.20632,
       79271., 3.84007,    220.41264,
      206816., 0.246584,   103.092774,
      350769., 3.303299,   426.598191,
      398380., 0.521120,   206.185548,
     1414151., 4.5858152,    7.1135470,
    11107660., 3.96205090, 213.29909544,
    87401354., 0., 0.,
           0., 0.,          0.
  };  /* 90 terms, error < 4.0" or 0.028 Gm. */


  /** VSOP87 longitude terms for Saturn, 1st order in time. */

  protected static final double itsL1[] = {
             26., 4.51,      340.77,
             28., 2.74,      265.99,
             29., 2.03,      330.62,
             30., 3.39,     1059.38,
             30., 6.19,      284.15,
             30., 2.84,      203.00,
             31., 2.43,       52.69,
             32., 4.39,     1155.36,
             33., 0.30,      351.82,
             33., 5.43,     1066.50,
             33., 4.64,      277.03,
             34., 3.21,     1368.66,
             35., 6.08,        5.63,
             37., 3.78,        2.92,
             38., 2.53,       12.53,
             38., 0.65,      422.67,
             40., 0.41,      269.92,
             40., 3.89,      728.76,
             44., 2.71,        5.42,
             46., 2.23,      956.29,
             47., 5.15,      515.46,
             47., 1.18,      149.56,
             51., 1.46,      536.80,
             54., 5.13,      490.33,
             55., 0.28,       74.78,
             57., 5.02,      137.03,
             58., 2.48,      191.96,
             62., 1.83,      195.14,
             62., 4.29,      127.47,
             66., 5.65,        9.56,
             67., 0.29,        4.67,
             78., 6.24,      302.16,
             83., 3.11,      625.67,
             87., 1.22,      440.83,
             92., 3.95,       88.87,
             94., 3.48,     1052.27,
             98., 4.73,      838.97,
            109., 6.161,     415.552,
            128., 4.095,     217.231,
            131., 3.441,     742.990,
            136., 2.286,      10.295,
            167., 2.598,      21.341,
            173., 4.077,     846.083,
            192., 2.965,     224.345,
            230., 1.644,     216.480,
            266., 0.543,     647.011,
            281., 5.744,       2.448,
            289., 2.733,     117.320,
            332., 2.861,     210.118,
            336., 3.772,     735.877,
            340., 3.634,     316.392,
            344., 3.959,     412.371,
            352., 2.317,     632.784,
            408., 1.299,     209.367,
            417., 2.117,     323.505,
            468., 4.617,      63.736,
            479., 4.988,     522.577,
            487., 6.040,     853.196,
            628., 6.111,     309.278,
            650., 6.174,     202.253,
            706., 4.417,     529.691,
            922., 1.961,     227.526,
           1250., 2.6280,     95.9792,
           1953., 3.5639,     11.0457,
           3071., 2.3274,    199.0720,
           3302., 1.2626,    433.7117,
           3385., 2.4169,      3.1814,
           3769., 3.6497,      3.9322,
           4056., 2.9217,    110.2063,
           4803., 2.4419,    419.4846,
           6939., 0.4049,    639.8973,
          10512., 2.74880,    14.22709,
          19942., 1.27955,   103.09277,
          40255., 2.04128,   220.41264,
          98323., 1.08070,   426.59819,
         107679., 2.277699,  206.185548,
         564348., 2.885001,    7.113547,
        1296855., 1.8282054, 213.2990954,
    21354295596., 0.,          0.,
              0., 0.,          0.
  };  /* 79 terms, error < 0.95" t or 0.0066 Gm t. */


  /** VSOP87 longitude terms for Saturn, 2nd order in time. */

  protected static final double itsL2[] = {
         6., 5.93,    405.26,
         6., 4.46,    284.15,
         7., 5.40,   1052.27,
         8., 4.03,      9.56,
         8., 5.25,    429.78,
         8., 2.14,    269.92,
         9., 0.46,    956.29,
        10., 4.15,    860.31,
        10., 0.26,    330.62,
        10., 4.99,    422.67,
        11., 3.20,   1066.50,
        11., 5.60,    728.76,
        11., 5.92,    536.80,
        12., 3.12,    846.08,
        12., 0.13,    234.64,
        12., 4.72,    203.00,
        14., 3.76,    195.14,
        14., 0.21,    838.97,
        16., 0.58,    515.46,
        17., 1.63,    742.99,
        18., 4.90,    625.67,
        20., 5.94,    217.23,
        25., 5.66,    735.88,
        27., 0.83,    224.34,
        31., 4.16,    191.96,
        32., 1.67,    302.16,
        32., 0.07,     63.74,
        42., 5.71,     88.87,
        45., 1.67,    202.25,
        46., 5.69,    440.83,
        53., 2.75,    529.69,
        61., 4.88,    632.78,
        64., 0.35,    323.51,
        66., 0.48,     10.29,
        67., 0.46,    522.58,
        75., 4.76,    210.12,
        82., 1.02,    117.32,
        83., 6.05,    216.48,
        85., 5.73,    209.37,
        95., 5.63,    412.37,
        96., 2.91,    316.39,
       101., 0.893,    21.341,
       105., 4.900,   647.011,
       117., 3.881,   853.196,
       129., 1.566,   309.278,
       162., 1.381,    11.046,
       274., 4.288,    95.979,
       425., 0.209,   227.526,
       457., 1.268,   110.206,
       549., 5.573,     3.932,
       634., 4.388,   419.484,
      1020., 0.6337,    3.1814,
      1045., 4.0421,  199.0720,
      1082., 5.6913,  433.7117,
      1165., 4.6094,  639.8973,
      1216., 2.9186,  103.0928,
      4265., 1.0460,   14.2271,
     10605., 5.40964, 426.59819,
     10631., 0.25778, 220.41264,
     15277., 4.06492, 206.18555,
     90592., 0.,        0.,
     91921., 0.07425, 213.29910,
    116441., 1.179879,  7.113547,
         0., 0.,        0.
  };  /* 63 terms, error < 0.20" t^2 or 0.0014 Gm t^2. */


  /** VSOP87 longitude terms for Saturn, 3rd order in time. */

  protected static final double itsL3[] = {
        2., 3.07,   654.12,
        2., 4.16,   223.59,
        2., 1.19,  1066.50,
        2., 1.35,   405.26,
        2., 3.20,   202.25,
        2., 3.35,   429.78,
        2., 4.77,   330.62,
        3., 0.42,   625.67,
        3., 4.93,   224.34,
        3., 0.59,   529.69,
        3., 2.20,   860.31,
        4., 2.31,   515.46,
        4., 3.14,     0.,
        5., 4.64,   234.64,
        6., 1.06,   210.12,
        6., 2.25,   522.58,
        7., 0.38,   632.78,
        8., 4.88,   323.51,
        9., 3.39,   302.16,
       10., 3.95,   209.37,
       11., 5.93,   191.96,
       11., 5.58,    11.05,
       13., 1.18,    88.87,
       16., 5.62,   117.32,
       16., 3.90,   440.83,
       18., 3.32,   309.28,
       18., 4.20,   216.48,
       18., 1.03,   412.37,
       18., 4.97,    10.29,
       19., 1.92,   853.20,
       25., 0.99,     3.93,
       28., 3.01,   647.01,
       39., 5.83,   110.21,
       40., 5.96,    95.98,
       40., 5.47,    21.34,
       62., 4.74,   103.09,
       63., 0.23,   419.485,
      131., 4.743,  227.526,
      151., 2.736,  639.897,
      166., 5.116,    3.181,
      237., 5.768,  199.072,
      239., 3.861,  433.712,
     1067., 3.6082, 426.5982,
     1162., 5.6197,  14.2271,
     1466., 5.9133, 206.1855,
     1907., 4.7608, 220.4126,
     4250., 4.5854, 213.2991,
    16039., 5.73945,  7.11355,
        0., 0.,       0.
  };  /* 48 terms, error < 0.06" t^3 or 0.0004 Gm t^3. */


  /** VSOP87 longitude terms for Saturn, 4th order in time. */

  protected static final double itsL4[] = {
       1., 1.55,  191.96,
       2., 2.24,  216.48,
       2., 5.19,  302.16,
       2., 2.83,  234.64,
       2., 5.08,  309.28,
       2., 3.78,  117.32,
       3., 0.00,  853.20,
       3., 0.39,  103.09,
       3., 3.01,   88.87,
       3., 2.77,  412.37,
       3., 4.09,  110.21,
       4., 1.45,   95.98,
       4., 2.12,  440.83,
       6., 1.16,  647.01,
       6., 2.42,  419.48,
       9., 3.71,   21.34,
      15., 0.83,  639.90,
      31., 3.01,  227.53,
      38., 1.24,  199.07,
      40., 2.05,  433.71,
      68., 1.72,  426.60,
     110., 1.515, 206.186,
     114., 3.142,   0.,
     149., 2.741, 213.299,
     236., 3.902,  14.227,
     257., 2.984, 220.413,
    1662., 3.9983,  7.1135,
       0., 0.,      0.
  };  /* 27 terms, error < 0.021" t^4 or 0.00015 Gm t^4. */


  /** VSOP87 longitude terms for Saturn, 5th order in time. */

  protected static final double itsL5[] = {
      1., 0.24, 440.83,
      1., 3.14,   0.,
      1., 5.28, 639.90,
      2., 6.25, 213.30,
      3., 4.29, 206.19,
      3., 2.97, 199.07,
      4., 6.23, 426.60,
      5., 0.24, 433.71,
      6., 1.22, 227.53,
     28., 1.20, 220.41,
     34., 2.16,  14.23,
    124., 2.259,  7.114,
      0., 0.,     0.
  };  /* 12 terms, error < 0.014" t^5 or 0.000097 Gm t^5. */


  /** VSOP87 latitude terms for Saturn, 0th order in time. */

  protected static final double itsB0[] = {
        114., 0.963,     210.118,
        116., 3.109,     216.480,
        122., 3.115,     522.577,
        135., 5.245,     742.990,
        139., 1.998,     735.877,
        139., 4.595,      14.227,
        141., 0.644,     490.334,
        179., 2.954,      63.736,
        207., 0.730,     199.072,
        209., 2.120,     415.552,
        215., 5.950,     846.083,
        236., 2.139,      11.046,
        284., 4.886,     224.345,
        314., 0.465,     217.231,
        316., 1.997,     647.011,
        319., 3.626,     209.367,
        400., 3.359,     227.526,
        552., 5.131,     202.253,
        708., 3.803,     323.505,
        942., 1.396,     853.196,
        969., 5.204,     632.784,
       1060., 5.6310,    529.6910,
       1506., 6.0130,    103.0928,
       3432., 2.7326,    433.7117,
       4788., 4.9651,    110.2063,
       4808., 5.4331,    316.3919,
       6994., 4.7360,      7.11355,
       9917., 5.7900,    419.4846,
      14734., 2.11847,   639.89729,
      30863., 3.48442,   220.41264,
      34116., 0.57297,   206.18555,
      84746., 0.,          0.,
     240348., 2.852385,  426.598191,
    4330678., 3.6028443, 213.2990954,
           0., 0.,         0.
  };  /* 34 terms, error < 2.7" or 0.019 Gm. */


  /** VSOP87 latitude terms for Saturn, 1st order in time. */

  protected static final double itsB1[] = {
        27., 4.44,      11.05,
        27., 4.65,    1066.50,
        32., 1.19,     846.08,
        33., 1.31,     412.37,
        34., 2.84,     117.32,
        36., 1.82,     224.34,
        46., 0.82,     440.83,
        59., 1.82,     323.51,
        61., 1.25,     209.37,
        65., 1.26,     216.48,
        69., 1.66,     202.25,
        81., 2.86,      14.23,
        82., 2.76,     210.12,
       110., 2.467,    217.231,
       128., 1.207,    529.691,
       158., 5.209,    110.206,
       166., 2.444,    199.072,
       172., 0.052,    647.011,
       275., 3.889,    103.093,
       284., 1.619,    227.526,
       292., 5.316,    853.196,
       298., 0.919,    632.784,
       853., 0.436,    316.392,
      1291., 2.9177,     7.1135,
      1455., 0.8516,   433.7117,
      2717., 5.9117,   639.8973,
      3757., 1.2543,   419.4846,
      9644., 1.6967,   220.4126,
     14801., 2.30586,  206.18555,
     18572., 6.09919,  426.59819,
     49479., 3.14159,    0.,
    397555., 5.332900, 213.299095,
         0., 0.,         0.
  };  /* 32 terms, error < 0.63" t or 0.0044 Gm t. */


  /** VSOP87 latitude terms for Saturn, 2nd order in time. */

  protected static final double itsB2[] = {
        6., 3.61,    860.31,
        6., 1.16,    117.32,
        7., 0.29,    323.51,
        7., 5.56,    209.37,
        8., 3.32,    202.25,
       12., 2.53,    529.69,
       14., 3.00,    412.37,
       16., 4.26,    103.09,
       17., 5.68,    216.48,
       18., 0.85,    110.21,
       20., 5.31,    440.83,
       21., 4.35,    217.23,
       24., 1.12,     14.23,
       29., 4.53,    210.12,
       41., 3.16,    853.20,
       49., 4.43,    647.01,
       52., 2.88,    632.78,
       71., 4.15,    199.07,
       93., 1.98,    316.39,
      104., 6.157,   227.526,
      139., 1.043,     7.114,
      219., 3.828,   639.897,
      330., 5.279,   433.712,
      365., 5.099,   426.598,
      706., 3.039,   419.485,
     1346., 0.,        0.,
     1627., 6.1819,  220.4126,
     3720., 3.9983,  206.1855,
    20630., 0.50482, 213.29910,
        0., 0.,        0.
  };  /* 29 terms, error < 0.133" t^2 or 0.00092 Gm t^2. */


  /** VSOP87 latitude terms for Saturn, 3rd order in time. */

  protected static final double itsB3[] = {
      2., 3.72,  216.48,
      3., 0.63,  103.09,
      4., 4.71,  412.37,
      5., 1.22,  853.20,
      5., 5.64,   14.23,
      6., 3.52,  440.83,
      6., 0.02,  210.12,
      6., 4.80,  632.78,
      7., 3.46,  316.39,
     10., 2.55,  647.01,
     11., 5.37,    7.11,
     18., 1.99,  639.90,
     21., 5.85,  199.07,
     26., 4.40,  227.53,
     42., 2.38,  426.60,
     52., 3.42,  433.71,
     92., 4.84,  419.48,
    188., 4.338, 220.413,
    398., 0.,      0.,
    632., 5.698, 206.186,
    666., 1.990, 213.299,
      0., 0.,      0.
  };  /* 21 terms, error < 0.038" t^3 or 0.00026 Gm t^3. */


  /** VSOP87 latitude terms for Saturn, 4th order in time. */

  protected static final double itsB4[] = {
     1., 1.72, 440.83,
     1., 6.18, 639.90,
     1., 0.67, 647.01,
     1., 1.43, 426.60,
     5., 1.28, 199.07,
     5., 2.63, 227.53,
     6., 1.56, 433.71,
     9., 0.38, 419.48,
    12., 3.14,   0.,
    17., 2.48, 220.41,
    32., 3.12, 213.30,
    80., 1.12, 206.19,
     0., 0.,     0.
  };  /* 12 terms, error < 0.014" t^4 or 0.000097 Gm t^4. */


  /** VSOP87 latitude terms for Saturn, 5th order in time. */

  protected static final double itsB5[] = {
    1., 0.51, 220.41,
    8., 2.82, 206.19,
    0., 0.,     0.
  };  /* 2 term, error < 0.006" t^5 or 0.000042 Gm t^5. */


  /** VSOP87 distance terms for Saturn, 0th order in time. */

  protected static final double itsR0[] = {
         2024., 5.0541,      11.0457,
         2174., 0.0151,     340.7709,
         2406., 2.9656,     117.3199,
         2448., 6.1841,    1368.6603,
         2508., 3.5385,     742.9901,
         2881., 0.1796,     853.1964,
         2885., 1.3876,     838.9693,
         2976., 5.6847,     210.1177,
         3376., 3.6953,     224.3448,
         3401., 0.5539,     350.3321,
         3420., 4.9455,    1581.9593,
         3461., 1.8509,     175.1661,
         3688., 0.7802,     412.3711,
         4044., 1.6401,     209.3669,
         4696., 2.1492,     227.5262,
         5307., 0.5974,      63.7359,
         5850., 1.4552,     415.5525,
         6466., 0.1773,    1052.2684,
         6771., 3.0043,      14.2271,
         7753., 5.8519,      95.9792,
         9796., 5.2048,    1265.5675,
        11380., 1.73106,    522.57742,
        11993., 5.98051,    846.08283,
        12884., 1.64892,    138.51750,
        14296., 2.60434,    323.50542,
        15298., 3.05944,    529.69097,
        20747., 5.33256,    199.07200,
        20839., 1.52103,    433.71174,
        20937., 0.46349,    735.87651,
        32402., 5.47085,    949.17561,
        34144., 0.19519,    277.03499,
        48913., 1.55733,    202.25340,
        61053., 0.94038,    639.89729,
        69007., 5.94100,    419.48464,
       108975., 3.293136,   110.206321,
       140618., 5.704067,   632.783739,
       361778., 3.139043,     7.113547,
       371684., 2.271148,   220.412642,
       547507., 5.015326,   103.092774,
       821891., 5.935200,   316.391870,
      1464664., 1.6476305,  426.5981909,
      1873680., 5.2354961,  206.1855484,
     52921382., 2.39226220, 213.29909544,
    955758136., 0.,           0.,
            0., 0.,           0.
  };  /* 44 terms, error < 0.040 Gm. */


  /** VSOP87 distance terms for Saturn, 1st order in time. */

  protected static final double itsR1[] = {
        503., 2.130,       3.932,
        599., 2.549,     217.231,
        613., 3.033,      63.736,
        650., 1.725,     742.990,
        658., 4.144,     309.278,
        740., 1.382,     625.670,
        785., 3.064,     838.969,
        874., 1.402,     224.345,
        882., 1.885,    1052.268,
        898., 0.983,     529.691,
        954., 5.152,     647.011,
        966., 0.480,     632.784,
       1091., 0.0753,    216.4805,
       1203., 1.8665,    316.3919,
       1316., 1.2530,    117.3199,
       1340., 4.3080,    853.1964,
       1581., 1.2919,    210.1177,
       1941., 6.0239,    209.3669,
       1988., 2.4505,    412.3711,
       2856., 2.1673,    735.8765,
       2909., 4.6068,    202.2534,
       3081., 3.4366,    522.5774,
       3252., 1.2585,     95.9792,
       4247., 0.3930,    227.5262,
       4869., 0.8679,    323.5054,
       5397., 1.2885,     14.2271,
      12893., 5.94330,   433.71174,
      13877., 0.75886,   199.07200,
      18840., 1.60820,   110.20632,
      19953., 1.17560,   419.48464,
      20928., 5.09246,   639.89729,
      49621., 6.01744,   103.09277,
     143891., 1.407449,    7.113547,
     186262., 3.141593,    0.,
     188491., 0.472157,  220.412642,
     341394., 5.796358,  426.598191,
     506578., 0.711147,  206.185548,
    6182981., 0.2584352, 213.2990954,
          0., 0.,          0.
  };  /* 38 terms, error < 0.009 Gm t. */


  /** VSOP87 distance terms for Saturn, 2nd order in time. */

  protected static final double itsR2[] = {
       132., 5.933,    309.278,
       133., 2.594,    191.958,
       148., 0.136,    302.165,
       154., 3.135,    625.670,
       178., 4.097,    440.825,
       180., 3.597,    632.784,
       204., 0.088,    202.253,
       207., 4.022,    735.877,
       323., 2.269,    853.196,
       356., 3.192,    210.118,
       361., 3.277,    647.011,
       374., 5.834,    117.320,
       391., 4.481,    216.480,
       405., 4.173,    209.367,
       431., 5.178,    522.577,
       546., 4.129,    412.371,
       706., 2.971,     95.979,
       924., 5.464,    323.505,
      1957., 4.9245,   227.5262,
      2188., 5.8555,    14.2271,
      2208., 6.2759,   110.2063,
      2327., 0.,         0.,
      2556., 2.8507,   419.4846,
      2964., 1.3721,   103.0928,
      3789., 3.0977,   639.8973,
      4142., 4.1067,   433.7117,
      4721., 2.4753,   199.0720,
     29646., 5.96310,    7.11355,
     43221., 3.86940,  426.59819,
     49767., 4.97168,  220.41264,
     71923., 2.50070,  206.18555,
    436902., 4.786717, 213.299095,
         0., 0.,         0.
  };  /* 32 terms, error < 0.0022 Gm t^2. */


  /** VSOP87 distance terms for Saturn, 3rd order in time. */

  protected static final double itsR3[] = {
       32., 4.01,     21.34,
       38., 5.94,     88.87,
       40., 1.84,    302.16,
       41., 0.69,    522.58,
       45., 4.37,    191.96,
       50., 2.39,    209.37,
       55., 0.31,    853.20,
       62., 2.31,    440.83,
       73., 4.15,    117.32,
       84., 2.63,    216.48,
       93., 1.44,    647.01,
      101., 5.819,   412.371,
      102., 4.710,    95.979,
      121., 3.768,   323.505,
      150., 3.202,   103.093,
      188., 4.590,   110.206,
      229., 4.698,   419.485,
      393., 0.,        0.,
      483., 1.173,   639.897,
      597., 4.135,    14.227,
      606., 3.175,   227.526,
      907., 2.283,   433.712,
     1071., 4.2036,  199.0720,
     3879., 2.0106,  426.5982,
     4087., 4.2241,    7.1135,
     6909., 4.3518,  206.1855,
     8924., 3.1914,  220.4126,
    20315., 3.02187, 213.29910,
        0., 0.,        0.
  };  /* 28 terms, error < 0.00051 Gm t^3. */


  /** VSOP87 distance terms for Saturn, 4th order in time. */

  protected static final double itsR4[] = {
       8., 1.27,   234.64,
       9., 0.68,   216.48,
       9., 1.56,    88.87,
       9., 2.28,    21.34,
      10., 3.14,     0.,
      11., 2.46,   117.32,
      11., 0.22,    95.98,
      13., 2.09,   323.51,
      14., 1.30,   412.37,
      15., 0.30,   419.48,
      16., 2.90,   110.21,
      17., 0.53,   440.83,
      19., 5.86,   647.01,
      47., 5.57,   639.90,
     121., 2.405,   14.227,
     145., 1.442,  227.526,
     150., 0.480,  433.712,
     170., 5.959,  199.072,
     268., 0.187,  426.598,
     427., 2.469,    7.114,
     516., 6.240,  206.186,
     708., 1.162,  213.299,
    1202., 1.4150, 220.4126,
       0., 0.,       0.
  };  /* 23 terms, error < 0.00011 Gm t^4. */


  /** VSOP87 distance terms for Saturn, 5th order in time. */

  protected static final double itsR5[] = {
      2., 0.56,  117.32,
      2., 3.32,   95.98,
      2., 3.70,   88.87,
      3., 0.49,  323.51,
      3., 3.18,  419.48,
      3., 4.66,  191.96,
      3., 4.07,  647.01,
      4., 4.90,  440.83,
      5., 3.61,  639.90,
      7., 4.63,  213.30,
     13., 4.59,  426.60,
     14., 1.46,  199.07,
     14., 2.67,  206.19,
     20., 0.67,   14.23,
     20., 4.95,  433.71,
     27., 5.91,  227.53,
     32., 0.69,    7.11,
    129., 5.913, 220.413,
      0., 0.,      0.
  };  /* 18 terms, error < 0.000025 Gm t^5. */


  /**
   * Initialise.
   *
   * <p>This initialises the Saturn object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal
   * equinox).</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for Saturn";
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

    Saturn    theObject;
    theObject = new Saturn();
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
   * Set Saturn for the given time.
   *
   * <p>This calculates for the given time Saturn's J2000 coordinates.</p>
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

    itsName = "Saturn";

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
   * <p>&rho; = asin(60268.4 km/r)</p>
   *
   * <p>To calcluate the orientation of the planet and its state of rotation we
   * use the expressions of
   * M.E. Davies, V.K. Abalakin, M. Bursa, J.H. Lieske, B. Morando, D. Morrison, P.K. Seidelmann, A.T. Sinclair, B. Yallop, Y.S. Tjuflin, 1996, Report of the IAU/IAG/COSPAR working group on cartographic coordinates and rotational elements of the planets and satellites: 1994, <em>Celestial Mechanics and Dynamical Astronomy</em>, <strong>63</strong>, p.127ff,
   * for the celestial coordinates of the planet's pole of rotation and the
   * prime meridian W of system III:</p>
   * 
   * <p>t = JDE - 2451545 d</p>
   * 
   * <p>T = t / 36525 d</p>
   *
   * <p>&alpha;<sub>1</sub> = 40.589&deg; - 0.036&deg; T
   * <br />&delta;<sub>1</sub> = 83.537&deg; - 0.004&deg; T
   * <br />W<sub>III</sub> = 38.90&deg; + 810.7939024&deg;/d (t - r/c)</p>
   *
   * <p>System III refers to the magnetic field.
M.E. Davies, V.K. Abalakin, C.A. Cross, R.L. Duncombe, H. Masursky, B. Morando, T.C. Owen, P.K. Seidelmann, A.T. Sinclair, G.A. Wilkins, Y.S. Tjuflin, 1980, Report of the IAU working group on cartographic coordinates and rotational elements of the planets and satellites, <em>Celestial Mechanics</em>, <strong>22</strong>, p.205ff
   * also list a - now inofficial - rotation system for the atmosphere at the
   * equator (System I). After transforming from their standard epoch
   * JDE&nbsp;2433282.5 (1950-01-01.0) to JDE&nbsp;2451545 (J2000)
   * the prime meridian is</p>
   *
   * <p>W<sub>I</sub> = 228.75&deg; + 844.3&deg;/d (t - r/c)</p>
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
   * <br />CM = W - K</p>
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
   * <p>V = -8.88 + 5 lg(r/au) + 5 lg(R/au)
   *      + 4.4 A - 2.60 |sin(i)| + 1.25  sin<sup>2</sup>(i)</p>
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
   *   the central meridian in System I (atmosphere, equatorial),
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
    double theCMI, theCMIII;
    double ra, rb, theRA1, theDec1, theD, theT, theA;
    double theWI, theWIII;

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

    theRho = Math.asin(0.0602684 / ra);

    theRA1  = ( 40.589 -   0.036     * theT) / Hmelib.DEGPERRAD;
    theDec1 = ( 83.537 -   0.004     * theT) / Hmelib.DEGPERRAD;

    theWI   = (228.75  + 844.3 * (theD - ra / LIGHTGMD))
	    / Hmelib.DEGPERRAD;
    theWI   = Hmelib.NormAngle180(theWI);

    theWIII = ( 38.90  + 810.7939024 * (theD - ra / LIGHTGMD))
	    / Hmelib.DEGPERRAD;
    theWIII = Hmelib.NormAngle180(theWIII);

    theBeta = Math.asin(
            - Math.sin(theDec1) * Math.sin(theSpher[1])
            - Math.cos(theDec1) * Math.cos(theSpher[1])
            * Math.cos(theRA1 - theSpher[0]));
    thePA = Math.atan2(
            Math.sin(theRA1 - theSpher[0]) * Math.cos(theDec1),
            Math.sin(theDec1) * Math.cos(theSpher[1])
          - Math.cos(theDec1) * Math.sin(theSpher[1])
          * Math.cos(theRA1 - theSpher[0]));

    theCMI   = theWI - Math.atan2(
             - Math.cos(theDec1) * Math.sin(theSpher[1])
             + Math.sin(theDec1) * Math.cos(theSpher[1])
             * Math.cos(theRA1 - theSpher[0]),
               Math.sin(theRA1 - theSpher[0]) * Math.cos(theSpher[1]));
    theCMI   = Hmelib.NormAngle180(theCMI);

    theCMIII = theWIII - Math.atan2(
             - Math.cos(theDec1) * Math.sin(theSpher[1])
             + Math.sin(theDec1) * Math.cos(theSpher[1])
             * Math.cos(theRA1 - theSpher[0]),
               Math.sin(theRA1 - theSpher[0]) * Math.cos(theSpher[1]));
    theCMIII = Hmelib.NormAngle180(theCMIII);

    theMag = -8.88 + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag += 4.4  * theA - 2.60 * Math.abs(Math.sin(theBeta))
                          + 1.25 * Math.sin(theBeta) * Math.sin(theBeta);

    aOctet[0] = theMag;
    aOctet[1] = theRho;
    aOctet[2] = theElong;
    aOctet[3] = thePhase;
    aOctet[4] = theIllum;
    aOctet[5] = theBeta;
    aOctet[6] = thePA;
    aOctet[7] = theCMI;
    aOctet[8] = -999.;
    aOctet[9] = theCMIII;

    return;
  }

}
