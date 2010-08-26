
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Jupiter</code> class extends the {@link VSOP87
VSOP87} and holds information needed for the planet Jupiter.  Most notably
these are the coefficients of the polynomials in time for the planet's
heliocentric longitude, latitude and distance.  The coefficients are
A<SUB>kji</SUB>, B<SUB>kji</SUB> and C<SUB>kji</SUB>, where k is L, B or
R, j is between 0 and 5 and i between 1 and up to 64.</p>

<p>The coefficients are taken from
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.386ff.
Given the cutoff in
the series, the precision of calculated longitudes, latitudes and
distances should be on the order of 3.5" (0.013&nbsp;Gm), 2.1"
(0.008&nbsp;Gm) and 0.006&nbsp;Gm, resp.  It is therefore not necessary
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
<dt><strong>2003-03-02:</strong> hme</dt>
<dd>New class.</dd>
<dt><strong>2003-04-21:</strong> hme</dt>
<dd>Fix bug whereby magnitude depended on sign of phase angle.</dd>
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
<dd>Calculate the central meridian not only for System II, but also for
  Systems I and III.</dd>
<dt><strong>2009-01-26:</strong> hme</dt>
<dd>Use 1994 report for W coefficients for Systems I and III
  (as well as II).</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Revert to using geocentric J2000 in GetPhysics.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */

public class Jupiter extends VSOP87
{

  /** VSOP87 longitude terms for Jupiter, 0th order in time. */

  protected static final double itsL0[] = {
          106., 4.554,     526.510,
          117., 3.389,       0.521,
          117., 2.500,    1596.186,
          131., 4.169,    1045.155,
          138., 1.318,    1169.588,
          141., 3.136,     491.558,
          149., 4.377,    1685.052,
          151., 3.906,      74.782,
          158., 4.365,    1795.258,
          175., 5.910,     956.289,
          175., 3.226,    1898.351,
          175., 3.730,     942.062,
          197., 5.293,    1155.361,
          207., 1.855,     525.759,
          202., 1.807,    1375.774,
          220., 1.651,     543.918,
          235., 1.227,     909.819,
          244., 5.220,     728.763,
          257., 3.724,     199.072,
          261., 0.820,     380.128,
          262., 1.877,       0.963,
          330., 4.740,       0.048,
          341., 5.715,     533.623,
          390., 4.897,    1692.166,
          376., 4.703,    1368.660,
          417., 1.036,       2.448,
          441., 2.958,     454.909,
          495., 3.756,     323.505,
          582., 4.540,     309.278,
          614., 4.109,    1478.867,
          692., 6.134,    2118.764,
          709., 1.293,     742.990,
          731., 3.806,    1581.959,
          733., 6.085,     838.969,
          884., 2.437,     412.371,
          973., 4.098,      95.979,
         1432., 4.2968,    625.6702,
         1633., 3.5820,    515.4639,
         1921., 0.9717,    639.8973,
         1723., 3.8804,   1265.5675,
         1765., 2.1415,   1066.4955,
         2028., 1.0638,      3.1814,
         2610., 1.5667,    846.0828,
         3045., 4.3168,    426.5982,
         4647., 4.6996,      3.9322,
         4905., 1.3208,    110.2063,
         5305., 1.3067,     14.2271,
         5305., 4.1863,   1052.2684,
         6114., 4.5132,   1162.4747,
         6263., 0.0250,    213.2991,
         7368., 5.0810,    735.8765,
         8246., 3.5823,    206.1855,
         8769., 3.6300,    949.1756,
        13590., 5.77481,  1589.07290,
        27965., 1.78455,   536.80451,
        38858., 1.27232,   316.39187,
        39806., 2.29377,   419.48464,
        64264., 3.41145,   103.09277,
        72903., 3.64043,   522.57742,
        97178., 4.14265,   632.78374,
       306389., 5.417347, 1059.381930,
       573610., 1.444062,    7.113547,
      9695899., 5.0619179, 529.6909651,
     59954691., 0.,          0.,
            0., 0.,          0.
  };  /* 64 terms, error < 3.5" or 0.013 Gm. */


  /** VSOP87 longitude terms for Jupiter, 1st order in time. */

  protected static final double itsL1[] = {
              25., 1.61,     831.86,
              29., 0.76,      88.87,
              29., 3.36,       4.67,
              29., 5.42,    1272.68,
              32., 5.37,     508.35,
              33., 5.04,     220.41,
              34., 0.10,     302.16,
              40., 4.16,    1692.17,
              47., 0.51,    1265.57,
              47., 3.63,    1478.87,
              50., 6.08,     525.76,
              52., 0.23,    1368.66,
              52., 5.73,     117.32,
              55., 5.43,      10.29,
              57., 1.41,     533.62,
              57., 5.97,    1169.59,
              58., 0.99,    1596.19,
              59., 0.59,    1155.36,
              65., 6.09,    1581.96,
              66., 0.13,     526.51,
              67., 5.73,      21.34,
              70., 5.97,     532.87,
              72., 5.34,     942.06,
              80., 5.82,    1045.15,
             108., 4.493,    956.289,
             115., 5.286,   2118.764,
             115., 0.680,    846.083,
             131., 0.626,    728.763,
             171., 5.417,    199.072,
             184., 6.280,    543.918,
             187., 6.086,    742.990,
             199., 1.505,    838.969,
             195., 2.219,    323.505,
             234., 6.243,    309.278,
             234., 4.035,    949.176,
             336., 3.732,   1162.475,
             345., 4.242,    632.784,
             413., 5.737,     95.979,
             474., 4.132,    412.371,
             568., 5.989,    625.670,
             725., 5.518,    639.897,
             816., 0.586,   1066.495,
             827., 4.803,    213.299,
             848., 5.758,    110.206,
            1004., 3.1504,   426.5982,
            1007., 0.4648,   735.8765,
            1099., 5.3070,   515.4639,
            1163., 0.5145,     3.9322,
            1173., 5.8565,  1052.2684,
            1296., 5.5513,     3.1814,
            1746., 4.9267,  1589.0729,
            2212., 5.2677,   206.1855,
            4238., 5.8901,    14.2271,
            5434., 3.9848,   419.4846,
            6068., 4.4242,   103.0928,
           12106., 0.16986,  536.80451,
           20721., 5.45939,  522.57742,
           27655., 4.57266, 1059.38193,
          228919., 6.026475,   7.113547,
          489741., 4.220667, 529.690965,
     52993480757., 0.,         0.,
               0., 0.,         0.
  };  /* 61 terms, error < 0.81" t or 0.0030 Gm t. */


  /** VSOP87 longitude terms for Jupiter, 2nd order in time. */

  protected static final double itsL2[] = {
         6., 0.50,    949.18,
         7., 2.18,   1265.57,
         8., 2.71,    533.62,
         8., 5.76,    846.08,
         9., 3.32,    831.86,
         9., 3.29,    220.41,
         9., 2.18,   1155.36,
        10., 1.72,   1581.96,
        11., 4.44,    525.76,
        13., 4.37,   1169.59,
        13., 2.52,     88.87,
        14., 1.80,    302.16,
        14., 5.95,    316.39,
        15., 4.00,    117.32,
        15., 0.68,    942.06,
        15., 5.81,   1596.19,
        17., 1.83,    526.51,
        17., 4.20,   2118.76,
        18., 0.81,    508.35,
        19., 4.29,    532.87,
        24., 3.01,    956.29,
        25., 1.22,   1045.15,
        26., 2.51,   1162.47,
        26., 4.50,    742.99,
        28., 3.24,    838.97,
        29., 3.61,     10.29,
        36., 2.33,    728.76,
        40., 0.62,    323.51,
        40., 4.02,     21.34,
        49., 1.67,    309.28,
        57., 3.12,    213.30,
        58., 0.83,    199.07,
        72., 2.22,    735.88,
        79., 4.64,    543.92,
        87., 2.52,    632.78,
        91., 1.11,     95.98,
        97., 4.03,    110.21,
       117., 1.414,   625.670,
       130., 5.837,   412.371,
       142., 1.634,   426.598,
       146., 3.814,   639.897,
       156., 1.406,  1052.268,
       197., 2.484,     3.932,
       199., 5.340,  1066.495,
       218., 3.814,  1589.073,
       308., 0.694,   206.186,
       337., 3.786,     3.181,
       367., 6.055,   103.093,
       378., 0.760,   515.464,
       383., 5.768,   419.485,
      1721., 4.1873,   14.2271,
      2723., 3.4141, 1059.3819,
      2729., 4.8455,  536.8045,
      3189., 1.0550,  522.5774,
     30629., 2.93021, 529.69097,
     38966., 0.,        0.,
     47234., 4.32148,   7.11355,
         0., 0.,        0.
  };  /* 57 terms, error < 0.19" t^2 or 0.0007 Gm t^2. */


  /** VSOP87 longitude terms for Jupiter, 3rd order in time. */

  protected static final double itsL3[] = {
        2., 2.36,   942.06,
        2., 2.90,   742.99,
        3., 2.24,   117.32,
        3., 5.02,   838.97,
        3., 1.25,   213.30,
        3., 4.36,  1596.19,
        3., 1.43,   956.29,
        4., 4.09,   735.88,
        4., 3.52,   302.16,
        4., 4.30,    88.87,
        5., 2.91,  1045.15,
        5., 5.25,   323.51,
        6., 2.52,   508.35,
        7., 4.04,   728.76,
        7., 3.43,   309.28,
        9., 1.76,    10.29,
        9., 2.27,   110.21,
       13., 6.27,   426.60,
       13., 2.54,   199.07,
       13., 2.76,    95.98,
       16., 3.36,  1052.27,
       16., 3.15,   625.67,
       17., 2.60,  1589.07,
       17., 2.30,    21.34,
       19., 1.59,   103.09,
       20., 1.40,   419.48,
       20., 2.10,   639.90,
       23., 2.98,   543.92,
       24., 1.28,   412.37,
       28., 2.45,   206.19,
       34., 3.83,  1066.50,
       44., 0.,       0.,
       87., 2.51,   515.46,
      155., 2.076, 1059.382,
      353., 2.974,  522.577,
      417., 3.245,  536.805,
      471., 2.475,   14.227,
     1357., 1.3464, 529.6910,
     6502., 2.5986,   7.1135,
        0., 0.,       0.
  };  /* 39 terms, error < 0.05" t^3 or 0.0002 Gm t^3. */


  /** VSOP87 longitude terms for Jupiter, 4th order in time. */

  protected static final double itsL4[] = {
       1., 1.29, 1589.07,
       1., 4.72,   95.98,
       1., 5.26, 1052.27,
       2., 4.26,  206.19,
       2., 4.26,  199.07,
       2., 4.91,  625.67,
       2., 0.40,  639.90,
       3., 3.00,  412.37,
       4., 0.48,   21.34,
       4., 2.32, 1066.50,
       5., 1.30,  543.92,
       9., 0.71, 1059.38,
      15., 4.29,  515.46,
      32., 4.86,  522.58,
      44., 5.82,  529.69,
      50., 1.65,  536.80,
     100., 0.743,  14.227,
     114., 3.142,   0.,
     669., 0.853,   7.114,
       0., 0.,      0.
  };  /* 19 terms, error < 0.018" t^4 or 0.000067 Gm t^4. */


  /** VSOP87 longitude terms for Jupiter, 5th order in time. */

  protected static final double itsL5[] = {
      1., 3.14,   0.,
      2., 1.10, 522.58,
      4., 0.01, 536.80,
     16., 5.25,  14.23,
     50., 5.26,   7.11,
      0., 0.,     0.
  };  /* 5 terms, error < 0.009" t^5 or 0.000035 Gm t^5. */


  /** VSOP87 latitude terms for Jupiter, 0th order in time. */

  protected static final double itsB0[] = {
         102., 3.153,    1581.959,
         103., 2.319,    1478.867,
         104., 3.701,     515.464,
         115., 5.049,     316.392,
         116., 1.387,     323.505,
         123., 3.350,    1692.166,
         132., 4.778,     742.990,
         351., 4.611,    2118.764,
         431., 2.608,     419.485,
         464., 1.173,     949.176,
         532., 2.703,     110.206,
         559., 0.014,     846.083,
         629., 0.643,    1066.495,
         684., 3.678,     213.299,
         767., 2.155,     632.784,
         836., 5.179,     103.093,
         894., 1.754,       7.114,
         942., 2.936,    1052.268,
         944., 1.675,     426.598,
        1107., 2.9853,   1162.4747,
        6044., 4.2588,   1589.0729,
        6438., 0.3063,    536.8045,
        8101., 3.6051,    522.5774,
      109972., 3.908094, 1059.381930,
      110090., 0.,          0.,
     2268616., 3.5585261, 529.6909651,
           0., 0.,          0.
  };  /* 26 terms, error < 2.1" or 0.008 Gm. */


  /** VSOP87 latitude terms for Jupiter, 1st order in time. */

  protected static final double itsB1[] = {
         32., 4.92,    1581.96,
         36., 6.11,     316.39,
         37., 4.70,     543.92,
         45., 1.90,     846.08,
         46., 0.54,     110.21,
         50., 3.95,     735.88,
         61., 5.45,     213.30,
         74., 5.50,     515.46,
         77., 0.61,     419.48,
         77., 2.51,     103.09,
         82., 5.08,    1162.47,
         97., 2.91,     949.18,
        114., 3.439,    632.784,
        150., 3.927,   1589.073,
        196., 6.186,      7.114,
        234., 5.189,   1066.495,
        346., 4.746,   1052.268,
       1694., 3.1416,     0.,
       2212., 4.7348,   536.8045,
       3081., 5.4746,   522.5774,
       3230., 5.7794,  1059.3819,
     177352., 5.701665, 529.690965,
          0., 0.,         0.
  };  /* 22 terms, error < 0.62" t or 0.0023 Gm t. */


  /** VSOP87 latitude terms for Jupiter, 2nd order in time. */

  protected static final double itsB2[] = {
        6., 6.21,  1045.15,
       11., 4.88,   949.18,
       12., 5.22,   632.78,
       14., 2.92,   543.92,
       23., 4.27,     7.11,
       29., 0.99,   515.46,
       30., 1.93,  1589.07,
       46., 3.48,  1066.50,
       74., 0.41,  1052.27,
      342., 1.447, 1059.382,
      399., 2.899,  536.805,
      742., 0.957,  522.577,
      813., 3.1416,   0.,
     8094., 1.4632, 529.6910,
        0., 0.,       0.
  };  /* 14 terms, error < 0.093" t^2 or 0.00035 Gm t^2. */


  /** VSOP87 latitude terms for Jupiter, 3rd order in time. */

  protected static final double itsB3[] = {
       3., 3.14,    0.,
       4., 1.13,  543.92,
       6., 1.78, 1066.50,
       7., 4.25, 1059.38,
       8., 2.77,  515.46,
      11., 2.31, 1052.27,
      49., 1.04,  536.80,
     122., 2.733, 522.577,
     252., 3.381, 529.691,
      0., 0.,        0.
  };  /* 9 terms, error < 0.037" t^3 or 0.00014 Gm t^3. */


  /** VSOP87 latitude terms for Jupiter, 4th order in time. */

  protected static final double itsB4[] = {
      1., 4.20, 1052.27,
      2., 4.52,  515.46,
      3., 0.,      0.,
      4., 5.44,  536.80,
      5., 4.47,  529.69,
     15., 4.53,  522.58,
      0., 0.,      0.
  };  /* 6 terms, error < 0.010" t^4 or 0.000038 Gm t^4. */


  /** VSOP87 latitude terms for Jupiter, 5th order in time. */

  protected static final double itsB5[] = {
     1., 0.09, 522.58,
     0., 0.,     0.
  };  /* 1 term, error < 0.004" t^5 or 0.000016 Gm t^5. */


  /** VSOP87 distance terms for Jupiter, 0th order in time. */

  protected static final double itsR0[] = {
           542., 0.284,      525.759,
           562., 0.081,      543.918,
           615., 2.276,      942.062,
           621., 4.823,      956.289,
           654., 3.382,     1692.166,
           655., 2.791,     1685.052,
           727., 3.988,     1155.361,
           777., 3.677,      728.763,
           812., 5.941,      909.819,
           821., 1.593,     1898.351,
           886., 4.148,      533.623,
           961., 4.549,     2118.764,
           999., 2.872,      309.278,
          1015., 1.3867,     454.9094,
          1217., 1.8017,     110.2063,
          1231., 1.8904,     323.5054,
          1479., 2.6803,    1478.8666,
          1611., 3.0887,    1368.6603,
          1912., 0.8562,     412.3711,
          2128., 6.1275,     742.9901,
          2500., 4.5518,     838.9693,
          2617., 2.0099,    1581.9593,
          4137., 2.7222,     625.6702,
          4170., 2.0161,     515.4639,
          3503., 0.5653,    1066.4955,
          5477., 5.6573,     639.8973,
          6138., 6.2642,     846.0828,
          7058., 2.1818,    1265.5675,
          7895., 2.4791,     426.5982,
          9161., 4.4135,     213.2991,
          9703., 1.9067,     206.1855,
         12749., 2.71550,   1052.26838,
         13033., 2.96043,   1162.47470,
         22284., 4.19363,   1589.07290,
         23453., 3.54023,    735.87651,
         23947., 0.27458,      7.11355,
         29135., 1.67759,    103.09277,
         30135., 2.16132,    949.17561,
         65517., 5.97996,    316.39187,
         72063., 0.21466,    536.80451,
         86793., 0.71001,    419.48464,
        187647., 2.075904,   522.577418,
        282029., 2.574199,   632.783739,
        610600., 3.841154,  1059.381930,
      25209327., 3.49108640, 529.69096509,
     520887429., 0.,           0.,
             0., 0.,           0.
  };  /* 46 terms, error < 0.0057 Gm. */


  /** VSOP87 distance terms for Jupiter, 1st order in time. */

  protected static final double itsR1[] = {
         132., 4.512,     525.759,
         133., 1.322,     110.206,
         146., 6.130,     533.623,
         170., 4.846,     526.510,
         180., 4.402,     532.872,
         184., 4.265,      95.979,
         196., 3.759,     199.072,
         197., 3.706,    2118.764,
         200., 4.439,    1045.155,
         203., 5.600,    1155.361,
         220., 4.842,    1368.660,
         247., 3.923,     942.062,
         261., 5.343,     846.083,
         338., 3.168,     956.289,
         347., 4.681,      14.227,
         402., 4.605,     309.278,
         416., 5.368,     728.763,
         445., 0.403,     323.505,
         469., 4.710,     543.918,
         485., 2.469,     949.176,
         567., 4.577,     742.990,
         677., 6.250,     838.969,
         741., 2.171,    1162.475,
         806., 2.678,     632.784,
        1025., 2.5543,    412.3711,
        1050., 3.1611,    213.2991,
        1641., 4.4163,    625.6702,
        1646., 5.3095,   1066.4955,
        2101., 3.9276,    639.8973,
        2412., 1.4695,    426.5982,
        2600., 3.6344,    206.1855,
        2677., 4.3305,   1052.2684,
        2806., 3.7422,    515.4639,
        3176., 2.7930,    103.0928,
        3203., 5.2108,    735.8765,
        3404., 3.3469,   1589.0729,
        9166., 4.7598,      7.1135,
       11847., 2.41330,   419.48464,
       31185., 4.88277,   536.80451,
       41390., 0.,          0.,
       53444., 3.89718,   522.57742,
       61662., 3.00076,  1059.38193,
     1271802., 2.6493751, 529.6909651,
         0., 0.,           0.
  };  /* 43 terms, error < 0.013 Gm t. */


  /** VSOP87 distance terms for Jupiter, 2nd order in time. */

  protected static final double itsR2[] = {
        40., 5.95,     95.98,
        44., 0.27,    526.51,
        45., 5.52,    508.35,
        50., 2.72,    532.87,
        52., 5.58,    942.06,
        56., 0.96,   1162.47,
        62., 6.10,   1045.15,
        67., 5.47,    199.07,
        70., 1.51,    213.30,
        75., 1.60,    956.29,
        80., 2.98,    742.99,
        83., 0.06,    309.28,
        86., 5.14,    323.51,
        95., 1.70,    838.97,
       114., 0.787,   728.763,
       139., 2.932,    14.227,
       200., 4.429,   103.093,
       201., 3.069,   543.918,
       230., 0.705,   735.877,
       257., 0.963,   632.784,
       280., 4.262,   412.371,
       333., 0.003,   426.598,
       339., 6.127,   625.670,
       342., 6.099,  1052.268,
       363., 5.368,   206.186,
       377., 2.242,  1589.073,
       406., 3.783,  1066.495,
       427., 2.228,   639.897,
       498., 3.142,     0.,
       836., 4.199,   419.485,
       964., 5.480,   515.464,
      1861., 2.9768,    7.1135,
      5314., 1.8384, 1059.3819,
      7030., 3.2748,  536.8045,
      8252., 5.7777,  522.5774,
     79645., 1.35866, 529.69097,
         0., 0.,        0.
  };  /* 36 terms, error < 0.0037 Gm t^2. */


  /** VSOP87 distance terms for Jupiter, 3rd order in time. */

  protected static final double itsR3[] = {
        9., 3.45,   838.97,
       10., 6.26,   103.09,
       11., 6.28,   956.29,
       11., 1.79,   309.28,
       12., 3.56,   323.51,
       12., 2.61,   735.88,
       13., 1.50,  1045.15,
       14., 0.96,   508.35,
       15., 0.89,   199.07,
       21., 2.50,   728.76,
       30., 4.63,   426.60,
       31., 1.04,  1589.07,
       34., 0.85,   206.19,
       34., 1.67,  1052.27,
       37., 1.18,    14.23,
       43., 6.12,   419.48,
       47., 1.58,   625.67,
       51., 5.98,   412.37,
       58., 0.53,   639.90,
       58., 1.41,   543.92,
       69., 2.23,  1066.50,
       90., 3.14,     0.,
      222., 0.952,  515.464,
      255., 1.196,    7.114,
      342., 0.523, 1059.382,
      916., 1.413,  522.577,
     1073., 1.6732, 536.8045,
     3519., 6.0580, 529.6910,
        0., 0.,       0.
  };  /* 28 terms, error < 0.00074 Gm t^3. */


  /** VSOP87 distance terms for Jupiter, 4th order in time. */

  protected static final double itsR4[] = {
       3., 2.90,  426.60,
       3., 4.16,  728.76,
       3., 3.40, 1052.27,
       5., 3.34,  625.67,
       6., 5.12,  639.90,
       7., 1.43,  412.37,
       8., 5.68,   14.23,
       9., 0.77, 1066.50,
      13., 6.02,  543.92,
      18., 5.40, 1059.38,
      27., 5.69,    7.11,
      38., 2.73,  515.46,
      83., 3.30,  522.58,
     114., 4.249, 529.691,
     129., 0.084, 536.805,
       0., 0.,      0.
  };  /* 15 terms, error < 0.00018 Gm t^4. */


  /** VSOP87 distance terms for Jupiter, 5th order in time. */

  protected static final double itsR5[] = {
      2., 5.49, 1066.50,
      2., 4.13, 1059.38,
      2., 3.69,    7.11,
      2., 4.30,  543.92,
      2., 5.57,  515.46,
      4., 5.92,  522.58,
     11., 4.75,  536.80,
      0., 0.,      0.
  };  /* 7 terms, error < 0.00008 Gm t^5. */


  /**
   * Initialise.
   *
   * <p>This initialises the Jupiter object.  The position results from
   * initialising the superclass (100&nbsp;Gm toward the vernal
   * equinox).</p> */

  public void Init()
  {
    super.Init();
    itsName = "Placeholder object for Jupiter";
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

    Jupiter   theObject;
    theObject = new Jupiter();
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
   * Set Jupiter for the given time.
   *
   * <p>This calculates for the given time Jupiter's J2000 coordinates.</p>
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

    itsName = "Jupiter";

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
   * position of the planet. The elongation is therefore correct, but the
   * phase angle is not properly corrected for light time effects. Both are
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
   * <p>&rho; = asin(71492.4 km/r)</p>
   *
   * <p>To calcluate the orientation of the planet and its state of rotation we
   * use the expressions of
   * M.E. Davies, V.K. Abalakin, M. Bursa, J.H. Lieske, B.Morando, D. Morrison, P.K. Seidelmann, A.T. Sinclair, B. Yallop, Y.S. Tjuflin, 1996, Report of the IAU/IAG/COSPAR working group on cartographic coordinates and rotational elements of the planets and satellites: 1994, <em>Celestial Mechanics and Dynamical Astronomy</em>, <strong>63</strong>, p.127ff,
   * for the celestial coordinates of the planet's pole of rotation and the
   * prime meridian W of three rotation systems:</p>
   * 
   * <p>t = JDE - 2451545 d</p>
   * 
   * <p>T = t / 36525 d</p>
   *
   * <p>&alpha;<SUB>1</SUB> = 268.05&deg; - 0.009&deg; T
   * <br />&delta;<SUB>1</SUB> = 64.49&deg; + 0.003&deg; T
   * <br />W<sub>I</sub>   =  67.1&deg;  + 877.900&deg;/d (t - r/c)
   * <br />W<sub>II</sub>  =  43.30&deg; + 870.270&deg;/d (t - r/c)
   * <br />W<sub>III</sub> = 284.95&deg; + 870.5360000&deg;/d (t - r/c)</p>
   *
   * <p>System I refers to the atmosphere at the equator, System II refers to
   * the atmosphere at latitudes north of the south component of the NEB and
   * south of the north component of the SEB, and System III refers to the
   * magnetic field.</p>
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
   * <p>V = -9.40 + 5 lg(r/au) + 5 lg(R/au) + 0.5 A</p>
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
   *   the central meridian in System II (atmosphere beyond EB),
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
    double theCMI, theCMII, theCMIII;
    double ra, rb, theRA1, theDec1, theD, theT, theA;
    double theWI, theWII, theWIII;

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

    theRho = Math.asin(0.0714924 / ra);

    theRA1  = (268.05 - 0.009   * theT) / Hmelib.DEGPERRAD;
    theDec1 = ( 64.49 + 0.003   * theT) / Hmelib.DEGPERRAD;

    theWI   = ( 67.1  + 877.900 * (theD - ra / LIGHTGMD))
            / Hmelib.DEGPERRAD;
    theWI   = Hmelib.NormAngle180(theWI);

    theWII  = ( 43.30 + 870.270 * (theD - ra / LIGHTGMD))
            / Hmelib.DEGPERRAD;
    theWII  = Hmelib.NormAngle180(theWII);

    theWIII = (284.95 + 870.5360000 * (theD - ra / LIGHTGMD))
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

    theCMII  = theWII - Math.atan2(
             - Math.cos(theDec1) * Math.sin(theSpher[1])
             + Math.sin(theDec1) * Math.cos(theSpher[1])
             * Math.cos(theRA1 - theSpher[0]),
               Math.sin(theRA1 - theSpher[0]) * Math.cos(theSpher[1]));
    theCMII  = Hmelib.NormAngle180(theCMII);

    theCMIII = theWIII - Math.atan2(
             - Math.cos(theDec1) * Math.sin(theSpher[1])
             + Math.sin(theDec1) * Math.cos(theSpher[1])
             * Math.cos(theRA1 - theSpher[0]),
               Math.sin(theRA1 - theSpher[0]) * Math.cos(theSpher[1]));
    theCMIII = Hmelib.NormAngle180(theCMIII);

    theMag = -9.40 + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag += 0.5  * theA;

    aOctet[0] = theMag;
    aOctet[1] = theRho;
    aOctet[2] = theElong;
    aOctet[3] = thePhase;
    aOctet[4] = theIllum;
    aOctet[5] = theBeta;
    aOctet[6] = thePA;
    aOctet[7] = theCMI;
    aOctet[8] = theCMII;
    aOctet[9] = theCMIII;

    return;
  }

}
