
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Pluto</code> class extends the {@link NamedObject
NamedObject} class such that it stores not only one position and a name,
but also holds information needed for Pluto.</p>

<p>When it was discovered in the early 20th century, it was classified as a
planet, for it had been found due to it perturbing the orbits of the planets
Uranus and Neptune.  Neptune had had a similar story of discovery.  But its
high orbital inclination and eccentricity have always made it different from
the rest of the planets.  Today we know that it is in fact a double body of
Pluto and its close, large "moon" Charon.  And we find more and more objects
in the Kuiper Belt that are not that much smaller than Pluto.</p>

<p>In terms of how its position is calculated in this programme, it is also
different from the other planets and the Sun, which are all based on the
{@link VSOP87 VSOP87} model.  But it is also different from the
way we caluclate Asteroids, and so it is treated in the user interface like
a planet.</p>

<p>The position of Pluto is calculated as descibed by
Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, chapter 36.
The algorithm is valid only from 1855 to 2099, it initially yields
heliocentric J2000 coordinates.  The discrepancies compared to the numerical
integration of the orbit are less than 0.6" in longitude, 0.2" in latitude
and 0.003&nbsp;Gm in distance.</p>

<p>The position is calculated as a linear polynomial in time plus a sum of
43 terms that are periodic in time.</p>

<p>L = L<sub>0</sub> + L<sub>1</sub> T
     + &sum;<sub>i</sub> [A<sub>Li</sub> sin(&alpha;<sub>i</sub>)
                        + B<sub>Li</sub> cos(&alpha;<sub>i</sub>)]</p>

<p>B = B<sub>0</sub>
     + &sum;<sub>i</sub> [A<sub>Bi</sub> sin(&alpha;<sub>i</sub>)
                        + B<sub>Bi</sub> cos(&alpha;<sub>i</sub>)]</p>

<p>R = R<sub>0</sub>
     + &sum;<sub>i</sub> [A<sub>Ri</sub> sin(&alpha;<sub>i</sub>)
                        + B<sub>Ri</sub> cos(&alpha;<sub>i</sub>)]</p>

<p>The argument &alpha;<sub>i</sub> in each periodic term is a simple
combination of three angles J, S and P.  Simple meaning that the
coefficients in</p>

<p>&alpha;<sub>i</sub> = C<sub>Ji</sub> J
                       + C<sub>Si</sub> S
                       + C<sub>Pi</sub> P</p>

<p>are integers between -6 and +6.  J, S and P in turn are linear in
time.</p>

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
<dt><strong>2003-06-08:</strong> hme</dt>
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
<dt><strong>2006-11-13:</strong> hme</dt>
<dd>Port to Sputnik3.
  Change ShowToFile() to become Show() and
  to return a string rather than write to a stream.</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Revert to using geocentric J2000 in GetPhysics.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */


public class Pluto extends NamedObject
{

  /** C<sub>Xi</sub> coefficients.
   *
   * <p>Each triplet of coefficent applies to one periodic term enumerated by
   * the index i.  In each such triplet the first applies to J, the second to
   * S and the third to P.  The final zero triplet terminates the array.</p>
   *
   * <p>The order is reversed from the traditional representation so that the
   * smallest terms are added in first.</p> */

  protected static final double itsC[] = {
    3.,  0.,  0.,
    3.,  0., -1.,
    3.,  0., -2.,
    2.,  0.,  3.,
    2.,  0.,  2.,
    2.,  0.,  1.,
    2.,  0.,  0.,
    2.,  0., -1.,
    2.,  0., -2.,
    2.,  0., -3.,
    2.,  0., -4.,
    2.,  0., -5.,
    2.,  0., -6.,
    1.,  1.,  3.,
    1.,  1.,  1.,
    1.,  1.,  0.,
    1.,  1., -1.,
    1.,  1., -2.,
    1.,  1., -3.,
    1.,  0.,  4.,
    1.,  0.,  3.,
    1.,  0.,  2.,
    1.,  0.,  1.,
    1.,  0.,  0.,
    1.,  0., -1.,
    1.,  0., -2.,
    1.,  0., -3.,
    1., -1.,  1.,
    1., -1.,  0.,
    0.,  2.,  0.,
    0.,  2., -1.,
    0.,  2., -2.,
    0.,  1.,  3.,
    0.,  1.,  2.,
    0.,  1.,  1.,
    0.,  1.,  0.,
    0.,  1., -1.,
    0.,  0.,  6.,
    0.,  0.,  5.,
    0.,  0.,  4.,
    0.,  0.,  3.,
    0.,  0.,  2.,
    0.,  0.,  1.,
    0.,  0.,  0.
  };


  /** A<sub>Xi</sub> and B<sub>Xi</sub> coefficients.
   *
   * <p>Each set of six coefficents applies to one periodic term enumerated by
   * the index i.  Each such set consists of three pairs, one for longitude,
   * one for latitude and one for radius.  Each pair consists of the A and B
   * coefficients.  The final zero sextuplet terminates the array.</p>
   *
   * <p>The order is reversed from the traditional representation so that the
   * smallest terms are added in first.</p> */

  protected static final double itsAB[] = {
           -1.,       -2.,        0.,         1.,        13.,        3.,
            6.,       -3.,        0.,         0.,        18.,       35.,
           -1.,       -1.,        0.,         1.,         4.,      -14.,
            3.,        4.,        0.,         1.,       -11.,        4.,
           -5.,        0.,        0.,         0.,         7.,        0.,
           -4.,        8.,       -2.,        -3.,       -26.,       -2.,
           12.,      -18.,       13.,        16.,       254.,      155.,
          157.,      -46.,        8.,         5.,       270.,     1068.,
          -57.,      -32.,        0.,        21.,       126.,     -233.,
           10.,       22.,       10.,        -7.,       -65.,       12.,
            6.,      -13.,       -8.,         2.,        14.,       10.,
           -3.,        6.,        1.,         2.,         2.,       -1.,
            8.,        3.,       -2.,        -3.,         9.,        5.,
            1.,        5.,        1.,        -1.,         1.,        0.,
            4.,        7.,        1.,         0.,         1.,       -3.,
           -1.,       15.,        0.,        -2.,        12.,        5.,
          -15.,       21.,        1.,        -1.,         2.,        9.,
          -43.,        1.,        3.,         0.,        -8.,       16.,
          -34.,      -26.,        4.,         2.,       -22.,        7.,
          -78.,      -30.,        2.,         2.,       -16.,        1.,
          -52.,     -154.,        7.,        15.,      -133.,       65.,
          111.,     -268.,       15.,         8.,       208.,     -122.,
          393.,      -63.,     -124.,       -29.,      -896.,     -801.,
         1179.,     -358.,      304.,       825.,      8623.,     8444.,
         7049.,      747.,      157.,       201.,       105.,    45637.,
        -2303.,    -1038.,     -298.,       692.,      8019.,    -7869.,
         -964.,     1059.,      582.,      -285.,     -3218.,      370.,
          839.,    -1414.,       17.,       234.,      -191.,     -396.,
         2484.,     -485.,     -177.,       259.,       260.,     -395.,
          595.,    -1229.,       -8.,      -160.,      -281.,      616.,
         1086.,     -911.,      -94.,       210.,       837.,     -494.,
         1237.,      463.,      -64.,        39.,       -13.,     -221.,
         -601.,     3468.,     -329.,      -563.,       518.,      518.,
        -3812.,     3011.,       -2.,      -674.,        -5.,      792.,
        -5885.,    -3238.,     2036.,      -947.,     -1518.,      641.,
        -4045.,    -4904.,      310.,      -132.,      4434.,     4443.,
        20349.,    -9886.,     4965.,     11263.,     -6140.,    22254.,
       -38215.,    31061.,   -30594.,    -25838.,     30841.,    -5765.,
       129027.,   -34863.,    18763.,    100448.,    -66634.,   -85576.,
      -341639.,  -189719.,   178691.,   -291925.,    -18948.,   482443.,
       610820.,  1210521., -1050939.,    327763.,   1593657., -1439953.,
       897499., -4955707.,  3527363.,   1672673., -11826086.,  -333765.,
    -19798886., 19848454., -5453098., -14974876.,  66867334., 68955876.,
            0.,        0.,        0.,         0.,         0.,        0.
  };


  /**
   * Initialise the Pluto object.
   *
   * <p>This sets the object to be a point 5000&nbsp;Gm from Earth in the
   * direction of the vernal equinox.</p> */

  public void Init()
  {
    final double t[] = {5000., 0., 0.};
    super.Init();
    itsName = "Placeholder object for Pluto";
    SetJ2000(0, t);
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

    Pluto     theObject;
    theObject = new Pluto();
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
Display Pluto.

<p>This writes information about the currently stored object to the given
open file.  The format is</p>

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

Object: Pluto

  coord. system      deg      deg     h  m  s     deg ' "      Gm
  --------------  --------  -------  ----------  ---------  --------
  gal.   lII,bII     8.669   14.731
  B1950  RA,Dec    257.346  -13.695  17:09:23.1  -13:41:41
  J2000  RA,Dec    258.053  -13.754  17:12:12.8  -13:45:14
  ecl.   lam,bet   258.289    9.199
  mean   RA,Dec    258.096  -13.757  17:12:23.0  -13:45:26  4711.338
  topo   HA,Dec   -161.041  -13.757  13:15:50.1  -13:45:27  4711.343
  hori   A,h        26.692  -45.370

     q     48.119 deg   parallactic angle
  vrot      0.082 km/s  geocentric radial velocity of topocentre
  vhel     11.236 km/s  heliocentric radial velocity of topocentre
  vLSR     25.256 km/s  LSR radial velocity of topocentre
  vGSR     57.326 km/s  GSR radial velocity of topocentre

   mag     13.9         V magnitude
   rho      0.1"        apparent radius
    El    -23.6 deg     elongation from the Sun
   phi     -0.7 deg     phase angle
     L      1.000       illuminated fraction of the disc
     i    -30.9 deg     inclination of rotation axis
    PA     70.4 deg     position angle of rotation axis
    CM    320.0 deg     central meridian
</pre>

<p>This method calls the superclass method to make the output for
NamedObject and only adds the physical ephemeris at the end.  These
will previously have been calculated by this class.</p>

@param aTelescope
  Some of the coordinate transforms require the time or the location of
  the observatory to be known.
  The spatial velocity of the Sun is needed in order to reduce
  the radial velocity of the observatory from geocentric to
  heliocentric.
   */

  public final String Show(Telescope aTelescope)
  {
    String theOutput  = "";
    double theOctet[] = new double[8];

    theOutput = super.Show(aTelescope);
    GetPhysics(theOctet, aTelescope);

    theOutput = theOutput + "   mag   "
      + Hmelib.Wfndm(6, 1, theOctet[0])
      + "         V magnitude"

      + "\n   rho   "
      + Hmelib.Wfndm(6, 1, 3600. * Hmelib.DEGPERRAD * theOctet[1])
      + "\"        apparent radius"

      + "\n    El   "
      + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[2])
      + " deg     elongation from the Sun"

      + "\n   phi   "
      + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[3])
      + " deg     phase angle"

      + "\n     L   "
      + Hmelib.Wfndm(8, 3, theOctet[4])
      + "       illuminated fraction of the disc"

      + "\n     i   "
      + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[5])
      + " deg     inclination of rotation axis"

      + "\n    PA   "
      + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[6])
      + " deg     position angle of rotation axis"

      + "\n    CM   "
      + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[7])
      + " deg     central meridian\n\n";

    return theOutput;
  }


  /**
   * Set Pluto for the given time.
   *
   * <p>This calculates for the given time Pluto's J2000 coordinates.</p>
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

    itsName = "Pluto";
    theTime = new Times(); theTime.Init(); theTime.Copy(aTelescope);
    aTelescope.itsSun.GetPos(theSunPos);
    GetHelio(aTelescope, itsR);
    for (i = 0; i < 3; i++) {itsR[i] += theSunPos[i];}

    /* Calculate the light time, go back in time that far, re-calculate
     * the planet but not the Sun. */

    theLightTime  = -Math.sqrt(itsR[0] * itsR[0] + itsR[1] * itsR[1]
                             + itsR[2] * itsR[2]) / LIGHTGMD;
    theTime.Add(theLightTime);
    GetHelio(theTime, itsR);
    for (i = 0; i < 3; i++) {itsR[i] += theSunPos[i];}

    return;
  }


  /**
   * Return the mean J2000 heliocentric position of Pluto for the given time.
   *
   * <p>This is the algorithm of
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, chapter 36.</p>
   *
   * <p>T = (JDE - 2451545 d) / 36525 d</p>
   *
   * <p>J/&deg; =  34.35 + 3034.9057 T</p>
   * <p>S/&deg; =  50.08 + 1222.1138 T</p>
   * <p>P/&deg; = 238.96 +  144.9600 T</p>
   *
   * <p>&alpha;<sub>i</sub> = C<sub>Ji</sub> J
   *                        + C<sub>Si</sub> S
   *                        + C<sub>Pi</sub> P</p>
   *
   * <p>L/&deg; = 238.956785 + 144.96 T
   *    + &sum;<sub>i</sub> [A<sub>Li</sub> sin(&alpha;<sub>i</sub>)
   *                       + B<sub>Li</sub> cos(&alpha;<sub>i</sub>)]</p>
   * 
   * <p>B/&deg; = -3.908202
   *    + &sum;<sub>i</sub> [A<sub>Bi</sub> sin(&alpha;<sub>i</sub>)
   *                       + B<sub>Bi</sub> cos(&alpha;<sub>i</sub>)]</p>
   * 
   * <p>R/au = 40.7247248
   *    + &sum;<sub>i</sub> [A<sub>Ri</sub> sin(&alpha;<sub>i</sub>)
   *                       + B<sub>Ri</sub> cos(&alpha;<sub>i</sub>)]</p>
   *
   * @param aTime
   *   The time for which to calculate the ephemeris.
   * @param aTriplet
   *   The returned J2000 heliocentric position in Gm. */

  protected final void GetHelio(Times aTime, double aTriplet[])
  {
    final double sineps = 0.397777156;
    final double coseps = 0.917482062;
    double theT;
    double theJ, theS, theP, theAlpha;
    double theL, theB, theR, theL1;
    int    i;

    /* Time argument. */

    theT = (aTime.GetJDE() - 1545.) / 36525.;

    /* JSP angles. */

    theJ = ( 34.35 + 3034.9057 * theT) / Hmelib.DEGPERRAD;
    theS = ( 50.08 + 1222.1138 * theT) / Hmelib.DEGPERRAD;
    theP = (238.96 +  144.9600 * theT) / Hmelib.DEGPERRAD;
    theJ = Hmelib.NormAngle180(theJ);
    theS = Hmelib.NormAngle180(theS);
    theP = Hmelib.NormAngle180(theP);

    /* Sum up the periodics.
     * This is in units of 0.000001 deg and 0.0000001 au. */

    theL = 0.; theB = 0.; theR = 0.;
    for (i = 0; 0 != itsC[3*i] || 0 != itsC[1+3*i] || 0 != itsC[2+3*i] ; i++) {
      theAlpha = itsC[  3*i] * theJ
               + itsC[1+3*i] * theS
               + itsC[2+3*i] * theP;
      theL += itsAB[  6*i] * Math.sin(theAlpha)
           +  itsAB[1+6*i] * Math.cos(theAlpha);
      theB += itsAB[2+6*i] * Math.sin(theAlpha)
           +  itsAB[3+6*i] * Math.cos(theAlpha);
      theR += itsAB[4+6*i] * Math.sin(theAlpha)
           +  itsAB[5+6*i] * Math.cos(theAlpha);
    }

    /* Convert to rad, deg, au. */

    theL /= 1e6; theB /= 1e6; theR /= 1e7;
    theL /= Hmelib.DEGPERRAD;

    /* Calculate the linear term of L in rad. */

    theL1 = (238.956785 + 144.96 * theT) / Hmelib.DEGPERRAD;
    theL1 = Hmelib.NormAngle180(theL1);

    /* Add the linear terms to the periodic terms. */

    theL += theL1;
    theL  = Hmelib.NormAngle180(theL);
    theB -=  3.908202;
    theR += 40.7247248;

    /* Convert to rad and Gm. */

    theB /= Hmelib.DEGPERRAD;
    theR *= AU;

    /* These are ecliptical, but J2000.  Oh bother!
     * We don't want to mess around with creating a Times instance for J2000,
     * as this method might be called a lot of times.  Even to call Obliquity,
     * we would need that.  So we use the sin(eps) and cos(eps) that Meeus
     * lists along with the above algorithm. */

    aTriplet[0] = theR *  Math.cos(theL) * Math.cos(theB);
    aTriplet[1] = theR * (Math.sin(theL) * Math.cos(theB) * coseps
                                         - Math.sin(theB) * sineps);
    aTriplet[2] = theR * (Math.sin(theL) * Math.cos(theB) * sineps
                                         + Math.sin(theB) * coseps);
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
   * position of Pluto.  The elongation is therefore correct, but the
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
   * <p>&rho; = asin(1500 km/r)</p>
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
   * <p>&alpha;<sub>1</sub> = 313.02&deg;</p>
   *
   * <p>&delta;<sub>1</sub> = 9.09&deg;</p>
   *
   * <p>W = 236.77&deg; - 56.3623195&deg;/d (t - r/c)</p>
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
   * <p>V = -1.0 + 5 lg(r/au) + 5 lg(R/au) - 2.5 lg(L)</p>
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
   *   the central meridian.
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

    /* The distances of the Moon from Earth and Sun are needed below for
     * the magnitude, and various other calculations. */

    ra = Math.sqrt(theR[0] * theR[0] + theR[1] * theR[1] + theR[2] * theR[2]);
    rb = Math.sqrt(aTriplet[0] * aTriplet[0] + aTriplet[1] * aTriplet[1]
                 + aTriplet[2] * aTriplet[2]);

    Hmelib.Spher(theR, theSpher);

    theRho = Math.asin(0.0015 / ra);

    theRA1  =  313.02                        / Hmelib.DEGPERRAD;
    theDec1 =    9.09                        / Hmelib.DEGPERRAD;
    theW    = (236.77  -  56.3623195 * (theD - ra / LIGHTGMD))
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

    theMag = -1.0  + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag -= 2.5 * Math.log(theIllum) / Math.log(10.);

    aOctet[0] = theMag;
    aOctet[1] = theRho;
    aOctet[2] = theElong;
    aOctet[3] = thePhase;
    aOctet[4] = theIllum;
    aOctet[5] = theBeta;
    aOctet[6] = thePA;
    aOctet[7] = theCM;

    return;
  }

}
