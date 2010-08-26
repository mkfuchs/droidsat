
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Moon</code> class extends the {@link NamedObject
NamedObject} class such that it stores not only one position and a name,
but also holds information needed for the Moon.</p>

<p>This class calculates the coordinates of the Moon including
perturbations.  The precision is about 10" in longitude, 3" in latitude
and 0.2" in parallax.  This routine is based on
Jean Meeus, 1982, <em>Astronomical formulae for calculators, 2nd edition, enlarged and revised</em>, Willmann-Bell, Richmond VA.</p>

<p>Copyright: &copy; 1996-2009 Horst Meyerdierks.</p>

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
<dt><strong>2005-12-27:</strong> hme</dt>
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
<dd>Revert to using J2000 in GetPhysics, but keep using topocentric.
  topocentric J2000 is not something Sputnik does, normally.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */


public class Moon extends NamedObject
{

  /** Orbital elements.
   *
   * <p>The array elem is conceptually a Fortran array elem(1:4,1:6).
   * It contains third-order polynomial coefficients (coeff = 1...4) for
   * the orbital elements (elm = 1...6) of the Moon.
   * coeff = 1...5 for a0, a1, a2, a3 where a = a0 + a1 x + a2 x x + a3 x x x.
   * elm = 1...6 for L', M, M', D, F, OMEGA, angles in degrees.
   * Since elem is technically a one-dimensional C array with elements 0...23,
   * we have to keep in mind that elem(i,j) = elem[4*(j-1)+i-1].</p> */

  protected static final double elem[] = {
    270434164e-6 , 481267883.1e-3 , -1133e-6 ,  1.9e-6 ,
    358475833e-6 ,  35999049.8e-3 ,  -150e-6 , -3.3e-6 ,
    296104608e-6 , 477198849.1e-3 ,  9192e-6 , 14.4e-6 ,
    350737486e-6 , 445267114.2e-3 , -1436e-6 ,  1.9e-6 ,
     11250889e-6 , 483202025.1e-3 , -3211e-6 ,  -.3e-6 ,
    259183275e-6 ,    -1934142e-3 ,  2078e-6 ,  2.2e-6 };


  /**
   * Initialise the Moon object.
   *
   * <p>This sets the object to be a point 400&nbsp;km from Earth in the
   * direction of the vernal equinox.</p> */

  public void Init()
  {
    final double t[] = {.0004, 0., 0.};
    super.Init();
    itsName = "Placeholder object for Moon";
    SetJ2000(0, t);
  }


  /**
   * Return the times of the next rise and set.
   *
   * <p>This takes the given station and its time to scan forward for the next
   * rise and set times.  The set may occur before the rise, of course.  The
   * given Telescope instance is left alone.</p>
   *
   * <p>See {@link NamedObject#NaiveRiseSet NamedObject.NaiveRiseSet}
   * for details, but note that this method makes sure that the time is in the
   * future (greater than the time in the given Telescope).</p>
   *
   * <p>This method overrides that of the {@link NamedObject NamedObject}
   * base class.  See {@link NamedObject#NextRiseSet NamedObject.NextRiseSet}.
   * The override is necessary because this object moves
   * amongst the stars and needs a more sophisticated algorithm.</p>
   *
   * <p>In most cases all that is needed is to use <code>NaiveRiseSet</code>
   * iteratively to correct for the movement of the object.  This is what is
   * attempted first.  But it can happen that the object is circumpolar part
   * or all of the time (the interval from the given time to somewhat more
   * than a day ahead).  So if the naive iteration fails time is then scanned
   * with a granularity of one minute to detect any crossing of the target
   * elevation in the requested direction.  This is rather slow, and
   * unfortunately wastes a lot of time when usually the object is circumpolar
   * all of the time and no event can be found.</p>
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

    Moon      theObject;
    theObject = new Moon();
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
Display the Moon.

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

Object: Moon

  coord. system      deg      deg     h  m  s     deg ' "      km
  --------------  --------  -------  ----------  ---------  --------
  gal.   lII,bII   358.703   11.791
  B1950  RA,Dec    253.959  -23.308  16:55:50.3  -23:18:30
  J2000  RA,Dec    254.716  -23.384  16:58:51.8  -23:23:01
  ecl.   lam,bet   256.039   -0.685
  mean   RA,Dec    254.761  -23.388  16:59:02.7  -23:23:17    369766
  topo   HA,Dec   -157.934  -23.925  13:28:15.9  -23:55:28    374915
  hori   A,h        35.896  -54.148

     q     70.356 deg   parallactic angle
  vrot      0.090 km/s  geocentric radial velocity of topocentre
  vhel     12.483 km/s  heliocentric radial velocity of topocentre
  vLSR     23.776 km/s  LSR radial velocity of topocentre
  vGSR     18.900 km/s  GSR radial velocity of topocentre

   mag     -6.6         V magnitude
   rho    969.5"        apparent radius
    El    -24.1 deg     elongation from the Sun
   phi   -155.8 deg     phase angle
     L      0.044       illuminated fraction of the disc
     i      0.9 deg     inclination of rotation axis
    PA      4.5 deg     position angle of rotation axis
    CM      2.9 deg     central meridian
</pre>

<p>This method calls the superclass method to make the output for
NamedObject and only adds the physical ephemeris at the end.</p>

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
   * Set the Moon for the given time.
   *
   * <p>This calculates for the given time the lunar J2000 coordinates and
   * physical ephemeris.  The light time effect is limited to 0.7", negligible
   * compared to the error in longitude.</p>
   *
   * @param aTelescope
   *   Primarily the time for which to calculate the ephemeris.
   *   Also the position of the Sun. */

  public final void Update(Telescope aTelescope)
  {
    itsName = "Moon";
    MeeusMoon(aTelescope);
  }


  /** Cosine of angle given in degrees. */

  protected final double cosd(double a) {
    return Math.cos(a/Hmelib.DEGPERRAD); }


  /**
   * Set position of the Moon.
   *
   * <p>This is the algorithm of
   * Jean Meeus, 1982, <em>Astronomical formulae for calculators, 2nd edition, enlarged and revised</em>, Willmann-Bell, Richmond VA.</p>
   *
   * @param aTime
   *   The time for which to calculate the ephemeris. */

  protected final void MeeusMoon(Times aTime)
  {
    double t1[] = new double[3];
    double t2[] = new double[3];

    double lprime; /* Mean longitude in degrees */
    double m;      /* Sun's mean anomaly in degrees */
    double mprime; /* Moon's mean anomaly in degrees */
    double d;      /* Moon's mean elongation in degrees */
    double f;      /* Mean distance of Moon from its asc. node in degrees */
    double ome;    /* Longitude of Moon's ascending node in degreees */

    double lam, bet, para, r;  /* Geocentric ecliptical in degrees. */
    double temp1, temp2, temp3, e, b;
    double t;

    /* Find Ephemeris Time and convert to Julian centuries since 1900.0,
     * which is (JD_TDT - 2415020.) / 36525. */

    t = (aTime.GetJDE() + 4e4 - 5020.) / 36525.;

    /* Unperturbed elements. */

    lprime = elem[0]  + t * (elem[1]  + t * (elem[2]  + t * elem[3] ));
    m      = elem[4]  + t * (elem[5]  + t * (elem[6]  + t * elem[7] ));
    mprime = elem[8]  + t * (elem[9]  + t * (elem[10] + t * elem[11]));
    d      = elem[12] + t * (elem[13] + t * (elem[14] + t * elem[15]));
    f      = elem[16] + t * (elem[17] + t * (elem[18] + t * elem[19]));
    ome    = elem[20] + t * (elem[21] + t * (elem[22] + t * elem[23]));
    while (lprime <   0.) lprime += 360.;
    while (lprime > 360.) lprime -= 360.;
    while (m      <   0.) m += 360.;
    while (m      > 360.) m -= 360.;
    while (mprime <   0.) mprime += 360.;
    while (mprime > 360.) mprime -= 360.;
    while (d      <   0.) d += 360.;
    while (d      > 360.) d -= 360.;
    while (f      <   0.) f += 360.;
    while (f      > 360.) f -= 360.;
    while (ome    <   0.) ome += 360.;
    while (ome    > 360.) ome -= 360.;

    /* Perturbation in elements. */

    temp1 = 346.56 + 132.87 * t - 9173.1e-6 * t * t;
    temp1 = 3964e-6 * sind(temp1);
    temp2 = 51.2 + 20.2 * t;
    temp2 = sind(temp2);
    temp3 = sind(ome);
    lprime = lprime  +  233e-6 * temp2 + temp1 +  1964e-6 * temp3;
    m      = m       - 1778e-6 * temp2;
    mprime = mprime  +  817e-6 * temp2 + temp1 +  2541e-6 * temp3;
    d      = d       + 2011e-6 * temp2 + temp1 +  1964e-6 * temp3;
    f      = f                         + temp1 - 24691e-6 * temp3;
    f     -= 4328e-6 * sind( ome + 275.05 - 2.3 * t );

    /* e parameter. */

    e = 1. - 2495e-6 * t - 7520e-9 * t * t;

    /* Longitude. */

    lam = lprime;
    lam = lam
       + 6288750e-6 * sind( mprime )
       + 1274018e-6 * sind( 2. * d - mprime )
        + 658309e-6 * sind( 2. * d )
        + 213616e-6 * sind( 2. * mprime )
        - 185596e-6 * sind( m )                        * e
        - 114336e-6 * sind( 2. * f );
    lam = lam
         + 58793e-6 * sind( 2. * d - 2. * mprime )
         + 57212e-6 * sind( 2. * d - m - mprime )      * e
         + 53320e-6 * sind( 2. * d + mprime )
         + 45874e-6 * sind( 2. * d - m )               * e
         + 41024e-6 * sind( mprime - m )               * e
         - 34718e-6 * sind( d )
         - 30465e-6 * sind( m + mprime )               * e;
    lam = lam
         + 15326e-6 * sind( 2. * d - 2. * f )
         - 12528e-6 * sind( 2. * f + mprime )
         - 10980e-6 * sind( 2. * f - mprime )
         + 10674e-6 * sind( 4. * d - mprime )
         + 10034e-6 * sind( 3. * mprime );
    lam = lam
          + 8548e-6 * sind( 4. * d - 2. * mprime )
          - 7910e-6 * sind( m - mprime + 2. * d )      * e
          - 6783e-6 * sind( 2. * d + m )               * e
          + 5162e-6 * sind( mprime - d )
          + 5000e-6 * sind( m + d )                    * e
          + 4049e-6 * sind( mprime - m + 2. * d)       * e;
    lam = lam
          + 3996e-6 * sind( 2. * mprime + 2. * d )
          + 3862e-6 * sind( 4. * d )
          + 3665e-6 * sind( 2. * d - 3. * mprime )
          + 2695e-6 * sind( 2. * mprime - m )          * e
          + 2602e-6 * sind( mprime - 2. * f - 2. * d )
          + 2396e-6 * sind( 2. * d - m - 2. * mprime ) * e
          - 2349e-6 * sind( mprime + d );
    lam = lam
          + 2249e-6 * sind( 2. * d - 2. * m )          * e * e
          - 2125e-6 * sind( 2. * mprime + m )          * e
          - 2079e-6 * sind( 2. * m )                   * e * e
          + 2059e-6 * sind( 2. * d - mprime - 2. * m ) * e * e
          - 1773e-6 * sind( mprime + 2. * d - 2. * f )
          - 1595e-6 * sind( 2. * f + 2. * d )
          + 1220e-6 * sind( 4. * d - m - mprime )      * e
          - 1110e-6 * sind( 2. * mprime + 2. * f );
    lam = lam
           + 892e-6 * sind( mprime - 3. * d )
           - 811e-6 * sind( m + mprime + 2. * d )      * e
           + 761e-6 * sind( 4. * d - m - 2. * mprime ) * e
           + 717e-6 * sind( mprime - 2. * m )          * e * e
           + 704e-6 * sind( mprime - 2. * m - 2. * d ) * e * e;
    lam = lam
           + 693e-6 * sind( m - 2. * mprime + 2. * d ) * e
           + 598e-6 * sind( 2. * d - m - 2. * f )      * e
           + 550e-6 * sind( mprime + 4. * d )
           + 538e-6 * sind( 4. * mprime )
           + 521e-6 * sind( 4. * d - m )               * e
           + 486e-6 * sind( 2. * mprime - d );

    /* Latitude. */

    b =
      + 5128189e-6 * sind( f )
       + 280606e-6 * sind( mprime + f )
       + 277693e-6 * sind( mprime - f )
       + 173238e-6 * sind( 2. * d - f )
        + 55413e-6 * sind( 2. * d + f - mprime )
        + 46272e-6 * sind( 2. * d - f - mprime )
        + 32573e-6 * sind( 2. * d + f )
        + 17198e-6 * sind( 2. * mprime+ f );
    b = b
         + 9267e-6 * sind( 2. * d + mprime - f )
         + 8823e-6 * sind( 2. * mprime- f )
         + 8247e-6 * sind( 2. * d - m - f )           * e
         + 4323e-6 * sind( 2. * d - f -2. * mprime )
         + 4200e-6 * sind( 2. * d + f + mprime )
         + 3372e-6 * sind( f - m - 2. * d )           * e
         + 2472e-6 * sind( 2. * d + f - m - mprime )  * e
         + 2222e-6 * sind( 2. * d + f - m )           * e
         + 2072e-6 * sind( 2. * d - f - m - mprime )  * e
         + 1877e-6 * sind( f - m - mprime )           * e
         + 1828e-6 * sind( 4. * d - f - mprime );
    b = b
         - 1803e-6 * sind( f + m )                    * e
         - 1750e-6 * sind( 3. * f )
         + 1570e-6 * sind( mprime - m - f )           * e
         - 1487e-6 * sind( f + d )
         - 1481e-6 * sind( f + m + mprime )           * e
         + 1417e-6 * sind( f - m - mprime )           * e
         + 1350e-6 * sind( f - m )                    * e
         + 1330e-6 * sind( f - d )
         + 1106e-6 * sind( f + 3. * mprime )
         + 1020e-6 * sind( 4. * d - f );
    b = b
          + 833e-6 * sind( f + 4. * d - mprime )
          + 781e-6 * sind( mprime - 3. * f )
          + 670e-6 * sind( f + 4. * d -2. * mprime )
          + 606e-6 * sind( 2. * d - 3. * f )
          + 597e-6 * sind( 2. * d + 2. * mprime - f )
          + 492e-6 * sind( 2. * d + mprime - m - f )  * e
          + 450e-6 * sind( 2. * mprime - f - 2. * d )
          + 439e-6 * sind( 3. * mprime - f );
    b = b
          + 423e-6 * sind( f + 2. * d + 2. * mprime )
          + 422e-6 * sind( 2. * d - f - 3. * mprime )
          - 367e-6 * sind( m + f + 2. * d - mprime )  * e
          - 353e-6 * sind( m + f + 2. * d )           * e
          + 331e-6 * sind( f + 4. * d )
          + 317e-6 * sind( 2. * d + f - m + mprime )  * e
          + 306e-6 * sind( 2. * d - 2. * m - f )      * e * e
          - 283e-6 * sind( mprime + 3. * f );
    bet = b * ( 1. - 4664e-7 * cosd( ome )
                   -  754e-7 * cosd( ome + 275.05 - 2.3 * t ) );

    /* Parallax. */

    para =
       950724e-6
      + 51818e-6 * cosd( mprime )
       + 9531e-6 * cosd( 2. * d - mprime )
       + 7843e-6 * cosd( 2. * d )
       + 2824e-6 * cosd( 2. * mprime )
        + 857e-6 * cosd( 2. * d + mprime )
        + 533e-6 * cosd( 2. * d - m )               * e
        + 401e-6 * cosd( 2. * d - m - mprime )      * e
        + 320e-6 * cosd( mprime - m )               * e;
    para = para
        - 271e-6 * cosd( d )
        - 264e-6 * cosd( m + mprime )               * e
        - 198e-6 * cosd( 2. * f - mprime )
        + 173e-6 * cosd( 3. * mprime )
        + 167e-6 * cosd( 4. * d - mprime )
        - 111e-6 * cosd( m )                        * e
        + 103e-6 * cosd( 4. * d - 2. * mprime )
         - 84e-6 * cosd( 2. * mprime - 2. * d )
         - 83e-6 * cosd( 2. * d + m )               * e
         + 79e-6 * cosd( 2. * d + 2. * mprime )
         + 72e-6 * cosd( 4. * d );
    para = para
         + 64e-6 * cosd( 2. * d - m + mprime )      * e
         - 63e-6 * cosd( 2. * d + m - mprime )      * e
         + 41e-6 * cosd( m + d )                    * e
         + 35e-6 * cosd( 2. * mprime - m )          * e
         - 33e-6 * cosd( 3. * mprime - 2. * d )
         - 30e-6 * cosd( mprime + d )
         - 29e-6 * cosd( 2. * f - 2. * d )
         - 29e-6 * cosd( 2. * mprime + m )          * e
         + 26e-6 * cosd( 2. * d - 2. * m )          * e * e
         - 23e-6 * cosd( 2. * f - 2. * d + mprime )
         + 19e-6 * cosd( 4. * d - m  - mprime )     * e;
    r = Station.A / sind(para);

    lam /= Hmelib.DEGPERRAD;
    bet /= Hmelib.DEGPERRAD;
    t1[0] = r * Math.cos(lam) * Math.cos(bet);
    t1[1] = r * Math.sin(lam) * Math.cos(bet);
    t1[2] = r * Math.sin(bet);

    Ecl2Mean(  1, aTime, t1, t2);
    Mean2J2000(1, aTime, t2, itsR);
  }


  /**
   * Return topocentric physical ephemeris.
   *
   * <p>These comprise the elongation from the Sun, the phase angle, the
   * illuminated fraction of the disc, the magnitude, the apparent radius, the
   * inclination and position angle of the rotation axis, and the central
   * meridian.</p>
   *
   * <p>The elongation and phase angle are calulcated from the triangle formed
   * by the topocentre, the topocentric position of the Sun and the topocentric
   * position of the Moon.  The elongation is therefore correct, but the
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
   * and the topocentric distance:</p>
   *
   * <p>&rho; = asin(1738 km/r)</p>
   *
   * <p>To calcluate the orientation of the planet and its state of rotation we
   * use the expressions of
   * M.E. Davies, V.K. Abalakin, M. Bursa, J.H. Lieske, B.Morando, D. Morrison, P.K. Seidelmann, A.T. Sinclair, B. Yallop, Y.S. Tjuflin, 1996, Report of the IAU/IAG/COSPAR working group on cartographic coordinates and rotational elements of the planets and satellites: 1994, <em>Celestial Mechanics and Dynamical Astronomy</em>, <strong>63</strong>, p.127ff,
   * for the celestial coordinates of the lunar pole of rotation and the
   * prime meridian W:</p>
   * 
   * <p>t = JDE - 2451545 d</p>
   * 
   * <p>T = t / 36525 d</p>
   *
   * <p>E<sub>1</sub>   = 125.045&deg; -  0.0529921&deg;/d  t
   * <br />E<sub>2</sub>   = 250.089&deg; -  0.1059842&deg;/d  t
   * <br />E<sub>3</sub>   = 260.008&deg; - 13.0120009&deg;/d  t
   * <br />E<sub>4</sub>   = 176.625&deg; + 13.3407154&deg;/d  t
   * <br />E<sub>5</sub>   = 357.529&deg; +  0.9856003&deg;/d  t
   * <br />E<sub>6</sub>   = 311.589&deg; + 26.4057084&deg;/d  t
   * <br />E<sub>7</sub>   = 134.963&deg; + 13.0649930&deg;/d  t
   * <br />E<sub>8</sub>   = 276.617&deg; +  0.3287146&deg;/d  t
   * <br />E<sub>9</sub>   =  34.226&deg; +  1.7484877&deg;/d  t
   * <br />E<sub>10</sub>  =  15.134&deg; -  0.1589763&deg;/d  t
   * <br />E<sub>11</sub>  = 119.743&deg; +  0.0036096&deg;/d  t
   * <br />E<sub>12</sub>  = 239.961&deg; +  0.1643573&deg;/d  t
   * <br />E<sub>13</sub>  =  25.053&deg; + 12.9590088&deg;/d  t</p>
   *
   * <p>&alpha;<SUB>1</SUB> = 269.9949&deg; + 0.0031&deg; T
   *   - 3.8787&deg; sin(E<sub>1</sub>)
   *   - 0.1204&deg; sin(E<sub>2</sub>)
   *   + 0.0700&deg; sin(E<sub>3</sub>)
   *   - 0.0172&deg; sin(E<sub>4</sub>)
   *   + 0.0072&deg; sin(E<sub>6</sub>)
   *   - 0.0052&deg; sin(E<sub>10</sub>)
   *   + 0.0043&deg; sin(E<sub>13</sub>)</p>
   *
   * <p>&delta;<sub>1</sub> = 66.5392&deg; + 0.0130&deg; T
   *   + 1.5419&deg; cos(theE<sub>1</sub>)
   *   + 0.0239&deg; cos(theE<sub>2</sub>)
   *   - 0.0278&deg; cos(theE<sub>3</sub>)
   *   + 0.0068&deg; cos(theE<sub>4</sub>)
   *   - 0.0029&deg; cos(theE<sub>6</sub>)
   *   + 0.0009&deg; cos(theE<sub>7</sub>)
   *   + 0.0008&deg; cos(theE<sub>10</sub>)
   *   - 0.0009&deg; cos(theE<sub>13</sub>)</p>
   *
   * <p>W = 38.3213&deg; + 13.17635815&deg;/d (t - r/c)
   *      - 1.4 10<sup>-12</sup> &deg;/d<sup>2</sup> (t - r/c)<sup>2</sup>
   *      + 3.5610&deg; sin(E<sub>1</sub>)
   *      + 0.1208&deg; sin(E<sub>2</sub>)
   *      - 0.0642&deg; sin(E<sub>3</sub>)
   *      + 0.0158&deg; sin(E<sub>4</sub>)
   *      + 0.0252&deg; sin(E<sub>5</sub>)
   *      - 0.0066&deg; sin(E<sub>6</sub>)
   *      - 0.0047&deg; sin(E<sub>7</sub>)
   *      - 0.0046&deg; sin(E<sub>8</sub>)
   *      + 0.0028&deg; sin(E<sub>9</sub>)
   *      + 0.0052&deg; sin(E<sub>10</sub>)
   *      + 0.0040&deg; sin(E<sub>11</sub>)
   *      + 0.0019&deg; sin(E<sub>12</sub>)
   *      - 0.0044&deg; sin(E<sub>13</sub>)</p>
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
   * <p>This routine uses topocentric J2000 &alpha; and &delta; here.</p>
   *
   * <p>The V magnitude is calculated from the V(0,1) values listed in
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.E88.
   * These are first corrected for heliocentric and topocentric distance, then
   * the phase angle Ph according to
   * Daniel L. Harris, 1961, Photometry and colorometry of planets and satellites, in: <em>Gerard P. Kuiper &amp; Barbara M. Middlehurst (eds.), Planets and satellites,</em> University of Chicago Press, Chicago, p.272ff:</p>
   *
   * <p>A = Ph/100&deg;</p>
   * <p>V = 0.21 + 5 lg(r/au) + 5 lg(R/au)
   *      + 3.05 A - 1.02 A<sup>2</sup> + 1.05 A<sup>3</sup></p>
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
   *   The time and topocentre for which to calculate the ephemeris.
   *   Also the position of the Sun. */

  public final void GetPhysics(double aOctet[], Telescope aTelescope)
  {
    double theSunPos[] = new double[3];
    double theSpher[]  = new double[3];
    double aTriplet[]  = new double[3];
    double theR[]      = new double[3];
    double theE[]      = new double[14];
    double theElong;
    double thePhase;
    double theIllum;
    double theMag;
    double theRho;
    double theBeta;
    double thePA;
    double theCM;
    double ra, rb, theRA1, theDec1, theW, theD, theT, theA;
    int    j;

    /* Get the topocentric J2000 positions of the Sun and Moon.
     * Initially we get geocentric J2000. We transform this correctly to
     * topocentric with J20002Mean and Mean2Topo. That leaves us with
     * HA and Dec in EOD. We then convert HA to RA, and then apply
     * precession from EOD to J2000.
     * The rotation elements are expressed in J2000 coordinates,
     * but we need to use topocentric coordinates to get the libration
     * as seen by the observer and not as seen from the geocentre. */

    aTelescope.itsSun.GetPos(theSunPos);
    J20002Mean(1, aTelescope, theSunPos, aTriplet);
    Mean2Topo( 1, aTelescope, aTriplet,  theSunPos);
    Hmelib.Spher(theSunPos, theSpher);
    theSpher[0] = aTelescope.GetLST() * 15. / Hmelib.DEGPERRAD - theSpher[0];
    Hmelib.Rect(theSpher, aTriplet);
    Mean2J2000(1, aTelescope, aTriplet, theSunPos);

    J20002Mean(1, aTelescope, itsR,      aTriplet);
    Mean2Topo( 1, aTelescope, aTriplet,  theR);
    Hmelib.Spher(theR, theSpher);
    theSpher[0] = aTelescope.GetLST() * 15. / Hmelib.DEGPERRAD - theSpher[0];
    Hmelib.Rect(theSpher, aTriplet);
    Mean2J2000(1, aTelescope, aTriplet, theR);

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

    /* The distances of the Moon from Earth and Sun are needed below for
     * the magnitude, and various other calculations. */

    ra = Math.sqrt(theR[0] * theR[0] + theR[1] * theR[1] + theR[2] * theR[2]);
    rb = Math.sqrt(aTriplet[0] * aTriplet[0] + aTriplet[1] * aTriplet[1]
                 + aTriplet[2] * aTriplet[2]);

    Hmelib.Spher(theR, theSpher);

    theRho = Math.asin(0.001738 / ra);

    theE[1]   = (125.045 -  0.0529921 * theD);
    theE[2]   = (250.089 -  0.1059842 * theD);
    theE[3]   = (260.008 - 13.0120009 * theD);
    theE[4]   = (176.625 + 13.3407154 * theD);
    theE[5]   = (357.529 +  0.9856003 * theD);
    theE[6]   = (311.589 + 26.4057084 * theD);
    theE[7]   = (134.963 + 13.0649930 * theD);
    theE[8]   = (276.617 +  0.3287146 * theD);
    theE[9]   = ( 34.226 +  1.7484877 * theD);
    theE[10]  = ( 15.134 -  0.1589763 * theD);
    theE[11]  = (119.743 +  0.0036096 * theD);
    theE[12]  = (239.961 +  0.1643573 * theD);
    theE[13]  = ( 25.053 + 12.9590088 * theD);
    for (j = 1; j < 14; j++) {
      while (0.   >  theE[j]) theE[j] += 360.;
      while (360. <= theE[j]) theE[j] -= 360.;
      theE[j] /= Hmelib.DEGPERRAD;
    }
    theRA1  = (269.9949 + 0.0031 * theT
            - 3.8787 * Math.sin(theE[1])  - 0.1204 * Math.sin(theE[2])
            + 0.0700 * Math.sin(theE[3])  - 0.0172 * Math.sin(theE[4])
            + 0.0072 * Math.sin(theE[6])  - 0.0052 * Math.sin(theE[10])
            + 0.0043 * Math.sin(theE[13]))
            / Hmelib.DEGPERRAD;
    theDec1 = ( 66.5392 + 0.0130 * theT
            + 1.5419 * Math.cos(theE[1])  + 0.0239 * Math.cos(theE[2])
            - 0.0278 * Math.cos(theE[3])  + 0.0068 * Math.cos(theE[4])
            - 0.0029 * Math.cos(theE[6])  + 0.0009 * Math.cos(theE[7])
            + 0.0008 * Math.cos(theE[10]) - 0.0009 * Math.cos(theE[13]))
            / Hmelib.DEGPERRAD;
    theW    =   38.3213 + 13.17635815 * (theD - ra / LIGHTGMD);
    while (0.   >  theW) theW += 360.;
    while (360. <= theW) theW -= 360.;
    theW   -=   1.4e-12 * (theD - ra / LIGHTGMD) * (theD - ra / LIGHTGMD);
    theW    = ( theW
            + 3.5610 * Math.sin(theE[1])  + 0.1208 * Math.sin(theE[2])
            - 0.0642 * Math.sin(theE[3])  + 0.0158 * Math.sin(theE[4])
            + 0.0252 * Math.sin(theE[5])  - 0.0066 * Math.sin(theE[6])
            - 0.0047 * Math.sin(theE[7])  - 0.0046 * Math.sin(theE[8])
            + 0.0028 * Math.sin(theE[9])  + 0.0052 * Math.sin(theE[10])
            + 0.0040 * Math.sin(theE[11]) + 0.0019 * Math.sin(theE[12])
            - 0.0044 * Math.sin(theE[13]))
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
    theCM = Hmelib.NormAngle0(-1. * theCM);

    theMag = +0.21 + 5. * Math.log(ra / AU) / Math.log(10.)
                   + 5. * Math.log(rb / AU) / Math.log(10.);
    theMag += 3.05 * theA - 1.02 * theA * theA
                          + 1.05 * theA * theA * theA;

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


  /** Sine of angle given in degrees. */

  protected final double sind(double a) {
    return Math.sin(a/Hmelib.DEGPERRAD); }

}
