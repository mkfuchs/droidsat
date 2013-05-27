
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>VSOP87</code> class extends the {@link NamedObject
NamedObject} class such that it stores not only one position and a name,
but also holds information needed for the Sun or a major planet (excluding
Pluto).</p>

<p>The VSOP87 model of planetary motion
(Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, chapter 31 and appendix II;
P. Bretagnon, G. Francou, 1988, Planetary theories in rectangular and spherical variables, VSOP87 solutions, <em>Astronomy and Astrophysics</em>, <strong>202</strong>, p.309ff)
gives us polynomials in time for each planet Mercury to Uranus that are
independent of the other planets.  There are three polynomials, one each
for geometric heliocentric longitude, latitude and distance.  Presumably
they are heliocentric and not barycentric and the vectors point to the
planets' centres and not the centres of their planet-moon systems.</p>

<p>The VSOP87 model does not use osculating Kepler orbits, so we do not need
to update the orbital data as we have to for comets and asteroids (cf.
{@link Comet Comet}).</p>

<p>For each planet Mercury to Neptune there are three polynomials in time,
one for longitude (L), one for latitude (B) and one for distance (R).  The
polyomials are of different orders (j), up to 6th order.  Each coefficient
in each of the three polynomials is the sum of up to 91 terms (i).  Each
term is periodic in time and expressed by three numbers, a magnitude A, a
phase B and a frequency C.  for example, the full polynomial in distance
would be</p>

<p>r = &sum;<sub>j</sub>[t<sup>j</sup>
    &sum;<sub>i</sub>{A<sub>Rji</sub>
      cos(B<sub>Rji</sub> + C<sub>Rji</sub> t)}]</p>

<p>The precision of the result can be estimated from the smallest
A<sub>kji</sub> that is kept in the calculation.  The error is less than
2&nbsp;i<sup>0.5</sup>&nbsp;A<sub>kji</sub>.  These errors propagate
through the sums over j to give the full error.</p>

<p>The results of the sums refer to the mean ecliptic and equinox of date,
but not exactly in the FK5 reference frame.  Conversion to J2000
equatorial rectangular coordinates can be performed.  But since the effect
is below 0.1" and the accuracy here is worse than that, this correction is
not calculated.</p>

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
<dt><strong>2002-06-23:</strong> hme</dt>
<dd>New class.</dd>
<dt><strong>2002-07-02:</strong> hme</dt>
<dd>Fix bug: normalise final angle itself, not the L1 coefficient.</dd>
<dt><strong>2002-07-07:</strong> hme</dt>
<dd>Add physical ephemeris to class state.  Add speed of light as
  constant.  Override the superclass's ShowToFile() method so that physical
  ephemeris will be written.</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Consolidate documentation.</dd>
<dt><strong>2002-12-30:</strong> hme</dt>
<dd>The previous storage of the A VSOP87 coefficients did not work when
  different sub-classes began to co-exist.  Now the coefficients are class
  variables in the sub-classes (Sun and Mercury), and passed as arguments
  to the GetHelio() method of the VSOP87 class.</dd>
<dt><strong>2003-06-14:</strong> hme</dt>
<dd>Add GetPhysics() method.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2003/09-18:</strong> hme</dt>
<dd>Make the constructor protected so that only a subclass can instantiate
  it.</dd>
<dt><strong>2005-12-27:</strong> hme</dt>
<dd>The GetPhysics funtion here is now abstract and instantiated by
  each subclass.  The ShowToFile function will remain here, but now uses
  the GetPhysics function instead of class fields for physical
  ephemeris.</dd>
<dt><strong>2005-12-28:</strong> hme</dt>
<dd>Remove the physical ephemeris class fields.</dd>
<dt><strong>2005-12-28:</strong> hme</dt>
<dd>Version 2.1.3.</dd>
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>Port to Sputnik3.
  Change ShowToFile() to Show() and to return a string rather than write to
  stdout.</dd>
<dt><strong>2009-01-26:</strong> hme</dt>
<dd>Extend the octet array to be a decatet, so that rotation systems I,
  II and III can be accommodated.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
@see uk.me.chiandh.Lib.Hmelib
 */


public abstract class VSOP87 extends NamedObject
{

  /**
   * Initialise the VSOP87 object.
   *
   * <p>This sets the object to be a point 100&nbsp;Gm from Earth in the
   * direction of the vernal equinox. */

  public void Init()
  {
    final double t[] = {100., 0., 0.};
    super.Init();
    itsName = "Unspecified VSOP87 object";
    SetJ2000(0, t);
  }


  /**
Display the VSOP87.

<p>This returns information about the currently stored object.
The format is</p>

<pre>
Observatory: Royal Observatory Edinburgh
   East long.   -3.182500 deg
   Latitude     55.925000 deg
   Altitude           146 m

  UT: 2003-06-14T15:26:11.4 (JD  2452805.143188)
  TT: 2003-06-14T15:27:17.3 (JDE 2452805.143950)
  Ep: 2003.450086106
             GST 08:56:15.6 = 134.065033 deg
             LST 08:43:31.8 = 130.882533 deg

Object: Sun

  coord. system      deg      deg     h  m  s     deg ' "      Gm
  --------------  --------  -------  ----------  ---------  --------
  gal.   lII,bII   182.946   -5.976
  B1950  RA,Dec     81.761   23.223  05:27:02.7   23:13:21
  J2000  RA,Dec     82.521   23.261  05:30:04.9   23:15:38
  ecl.   lam,bet    83.180   -0.000
  mean   RA,Dec     82.573   23.263  05:30:17.5   23:15:47   151.941
  topo   HA,Dec     48.311   23.262  03:13:14.6   23:15:42   151.937
  hori   A,h       247.452   42.026

     q    -47.662 deg   parallactic angle
  vrot     -0.179 km/s  geocentric radial velocity of topocentre
  vhel     -0.348 km/s  heliocentric radial velocity of topocentre
  vLSR    -12.138 km/s  LSR radial velocity of topocentre
  vGSR    -23.382 km/s  GSR radial velocity of topocentre

   mag    -26.7         V magnitude
   rho    944.8"        apparent radius
    El      0.0 deg     elongation from the Sun
   phi    180.0 deg     phase angle
     L      1.000       illuminated fraction of the disc
     i      0.9 deg     inclination of rotation axis
    PA    -10.2 deg     position angle of rotation axis
    CM    290.0 deg     central meridian
</pre>

<p>For some planets the CM line differs or there may be two or three
  lines or output for different rotation systems.</p>

<p>This method calls the superclass method to make the output for
NamedObject and only adds the physical ephemeris at the end.  These
will previously have been calculated by the subclass, e.g. the Sun
class.</p>

@param aTelescope
  Some of the coordinate transforms require the time or the location of
  the observatory to be known.
  The spatial velocity of the Sun is needed in order to reduce
  the radial velocity of the observatory from geocentric to
  heliocentric.
 */

  public final String Show(Telescope aTelescope)
  {
    String theOutput = "";
    double theOctet[] = new double[10];

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
      + " deg     position angle of rotation axis";

    if (-998. < theOctet[7] && -998. > theOctet[8] && -998. > theOctet[9]) {
      theOutput = theOutput
        + "\n    CM   "
        + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[7])
        + " deg     central meridian";
    }
    else if (-998. < theOctet[7]) {
      theOutput = theOutput
        + "\n    CM   "
        + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[7])
        + " deg     central meridian (System I)";
    }

    if (-998. < theOctet[8]) {
      theOutput = theOutput
        + "\n    CM   "
        + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[8])
        + " deg     central meridian (System II)";
    }

    if (-998. < theOctet[9]) {
      theOutput = theOutput
        + "\n    CM   "
        + Hmelib.Wfndm(6, 1, Hmelib.DEGPERRAD * theOctet[9])
        + " deg     central meridian (System III)";
    }

    theOutput = theOutput + "\n\n";

    return theOutput;
  }


  /**
Placeholder function to return physical ephemeris.

@param aOctet
  The ten returned numbers.
@param aTelescope
  The time and topocentre for which to calculate the ephemeris.
 */

  public abstract void GetPhysics(double aOctet[], Telescope aTelescope);


  /**
Calculate the heliocentric distance in Gm from the VSPO87 polynomial.

<p>r = &sum;<sub>j</sub>[t<sup>j</sup>
    &sum;<sub>i</sub>{A<sub>Rji</sub>
     cos(B<sub>Rji</sub> + C<sub>Rji</sub> t)}]</p>

@param aTime
  The time for which to calculate the radius.
@param aR0
  The R0 coefficients.
@param aR1
  The R1 coefficients.
@param aR2
  The R2 coefficients.
@param aR3
  The R3 coefficients.
@param aR4
  The R4 coefficients.
@param aR5
  The R5 coefficients.
 */

  protected final double GetDist(Times aTime,
    double [] aR0, double [] aR1, double [] aR2,
    double [] aR3, double [] aR4, double [] aR5)
  {
    double theTau, theR0, theR1, theR2, theR3, theR4, theR5, theR;
    int    i;

    theTau = (aTime.GetJDE() - 1545.) / 365250.;

    theR0 = 0.;
    for (i = 0; 3*i+2 < aR0.length; i++) {
      theR0 += aR0[3*i] * Math.cos(aR0[3*i+1] + aR0[3*i+2] * theTau);
    }
    theR1 = 0.;
    for (i = 0; 3*i+2 < aR1.length; i++) {
      theR1 += aR1[3*i] * Math.cos(aR1[3*i+1] + aR1[3*i+2] * theTau);
    }
    theR2 = 0.;
    for (i = 0; 3*i+2 < aR2.length; i++) {
      theR2 += aR2[3*i] * Math.cos(aR2[3*i+1] + aR2[3*i+2] * theTau);
    }
    theR3 = 0.;
    for (i = 0; 3*i+2 < aR3.length; i++) {
      theR3 += aR3[3*i] * Math.cos(aR3[3*i+1] + aR3[3*i+2] * theTau);
    }
    theR4 = 0.;
    for (i = 0; 3*i+2 < aR4.length; i++) {
      theR4 += aR4[3*i] * Math.cos(aR4[3*i+1] + aR4[3*i+2] * theTau);
    }
    theR5 = 0.;
    for (i = 0; 3*i+2 < aR5.length; i++) {
      theR5 += aR5[3*i] * Math.cos(aR5[3*i+1] + aR5[3*i+2] * theTau);
    }

    theR  = theR5; theR *= theTau;
    theR += theR4; theR *= theTau;
    theR += theR3; theR *= theTau;
    theR += theR2; theR *= theTau;
    theR += theR1; theR *= theTau;
    theR += theR0; theR /= 1E8; theR *= AU;

    return theR;
  }


  /**
Return the mean J2000 heliocentric position for the given time.

<p>This uses the VSOP87 polynomials to calculate the mean heliocentric
coordinates for the given time, then transforms these to J2000.
The position is rectangular in Gm.  The position is returned to the
caller and not stored in the object fields.  This is because it is a
heliocentric position and not a geometric - let alone an astrometric -
one.</p>

@param aTime
  The time for which to calculate the position.
@param aL0
  The L0 coefficients.
@param aL1
  The L1 coefficients.
@param aL2
  The L2 coefficients.
@param aL3
  The L3 coefficients.
@param aL4
  The L4 coefficients.
@param aL5
  The L5 coefficients.
@param aB0
  The B0 coefficients.
@param aB1
  The B1 coefficients.
@param aB2
  The B2 coefficients.
@param aB3
  The B3 coefficients.
@param aB4
  The B4 coefficients.
@param aB5
  The B5 coefficients.
@param aR0
  The R0 coefficients.
@param aR1
  The R1 coefficients.
@param aR2
  The R2 coefficients.
@param aR3
  The R3 coefficients.
@param aR4
  The R4 coefficients.
@param aR5
  The R5 coefficients.
@param aTriplet
  The returned J2000 heliocentric position in Gm.
 */

  protected final void GetHelio(Times aTime, double aTriplet[],
    double [] aL0, double [] aL1, double [] aL2,
    double [] aL3, double [] aL4, double [] aL5,
    double [] aB0, double [] aB1, double [] aB2,
    double [] aB3, double [] aB4, double [] aB5,
    double [] aR0, double [] aR1, double [] aR2,
    double [] aR3, double [] aR4, double [] aR5)
  {
    double t1[] = new double[3];
    double t2[] = new double[3];
    double theL, theB, theR;

    theL   = GetLong(aTime, aL0, aL1, aL2, aL3, aL4, aL5);
    theB   = GetLat( aTime, aB0, aB1, aB2, aB3, aB4, aB5);
    theR   = GetDist(aTime, aR0, aR1, aR2, aR3, aR4, aR5);

    t1[0] = theR * Math.cos(theL) * Math.cos(theB);
    t1[1] = theR * Math.sin(theL) * Math.cos(theB);
    t1[2] = theR * Math.sin(theB);

    Ecl2Mean(  1, aTime, t1, t2);
    Mean2J2000(1, aTime, t2, aTriplet);
  }


  /**
Calculate the heliocentric latitude in rad from the VSPO87 polynomial.

<p>The mean latitude for the ecliptic and equinox of date is</p>

<p>&beta; = &sum;<sub>j</sub>[t<sup>j</sup>
    &sum;<sub>i</sub>{A<sub>Bji</sub>
      cos(B<sub>Bji</sub> + C<sub>Bji</sub> t)}]</p>

@param aTime
  The time for which to calculate the latitude.
@param aB0
  The B0 coefficients.
@param aB1
  The B1 coefficients.
@param aB2
  The B2 coefficients.
@param aB3
  The B3 coefficients.
@param aB4
  The B4 coefficients.
@param aB5
  The B5 coefficients.
 */

  protected final double GetLat(Times aTime,
    double [] aB0, double [] aB1, double [] aB2,
    double [] aB3, double [] aB4, double [] aB5)
  {
    double theTau, theB0, theB1, theB2, theB3, theB4, theB5, theB;
    int    i;

    theTau = (aTime.GetJDE() - 1545.) / 365250.;

    theB0 = 0.;
    for (i = 0; 3*i+2 < aB0.length; i++) {
      theB0 += aB0[3*i] * Math.cos(aB0[3*i+1] + aB0[3*i+2] * theTau);
    }
    theB1 = 0.;
    for (i = 0; 3*i+2 < aB1.length; i++) {
      theB1 += aB1[3*i] * Math.cos(aB1[3*i+1] + aB1[3*i+2] * theTau);
    }
    theB2 = 0.;
    for (i = 0; 3*i+2 < aB2.length; i++) {
      theB2 += aB2[3*i] * Math.cos(aB2[3*i+1] + aB2[3*i+2] * theTau);
    }
    theB3 = 0.;
    for (i = 0; 3*i+2 < aB3.length; i++) {
      theB3 += aB3[3*i] * Math.cos(aB3[3*i+1] + aB3[3*i+2] * theTau);
    }
    theB4 = 0.;
    for (i = 0; 3*i+2 < aB4.length; i++) {
      theB4 += aB4[3*i] * Math.cos(aB4[3*i+1] + aB4[3*i+2] * theTau);
    }
    theB5 = 0.;
    for (i = 0; 3*i+2 < aB5.length; i++) {
      theB5 += aB5[3*i] * Math.cos(aB5[3*i+1] + aB5[3*i+2] * theTau);
    }

    theB  = theB5; theB *= theTau;
    theB += theB4; theB *= theTau;
    theB += theB3; theB *= theTau;
    theB += theB2; theB *= theTau;
    theB += theB1; theB *= theTau;
    theB += theB0; theB /= 1E8;

    return theB;
  }


  /**
Calculate the heliocentric longitude in rad from the VSPO87 polynomial.

<p>The mean longitude for the ecliptic and equinox of date is</p>

<p>&lambda; = &sum;<sub>j</sub>[t<sup>j</sup>
    &sum;<sub>i</sub>{A<sub>Lji</sub>
      cos(B<sub>Lji</sub> + C<sub>Lji</sub> t)}]</p>

@param aTime
  The time for which to calculate the longitude.
@param aL0
  The L0 coefficients.
@param aL1
  The L1 coefficients.
@param aL2
  The L2 coefficients.
@param aL3
  The L3 coefficients.
@param aL4
  The L4 coefficients.
@param aL5
  The L5 coefficients.
 */

  protected final double GetLong(Times aTime,
    double [] aL0, double [] aL1, double [] aL2,
    double [] aL3, double [] aL4, double [] aL5)
  {
    double theTau, theL0, theL1, theL2, theL3, theL4, theL5, theL;
    int    i;

    theTau = (aTime.GetJDE() - 1545.) / 365250.;

    theL0 = 0.;
    for (i = 0; 3*i+2 < aL0.length; i++) {
      theL0 += aL0[3*i] * Math.cos(aL0[3*i+1] + aL0[3*i+2] * theTau);
    }
    theL1 = 0.;
    for (i = 0; 3*i+2 < aL1.length; i++) {
      theL1 += aL1[3*i] * Math.cos(aL1[3*i+1] + aL1[3*i+2] * theTau);
    }
    theL2 = 0.;
    for (i = 0; 3*i+2 < aL2.length; i++) {
      theL2 += aL2[3*i] * Math.cos(aL2[3*i+1] + aL2[3*i+2] * theTau);
    }
    theL3 = 0.;
    for (i = 0; 3*i+2 < aL3.length; i++) {
      theL3 += aL3[3*i] * Math.cos(aL3[3*i+1] + aL3[3*i+2] * theTau);
    }
    theL4 = 0.;
    for (i = 0; 3*i+2 < aL4.length; i++) {
      theL4 += aL4[3*i] * Math.cos(aL4[3*i+1] + aL4[3*i+2] * theTau);
    }
    theL5 = 0.;
    for (i = 0; 3*i+2 < aL5.length; i++) {
      theL5 += aL5[3*i] * Math.cos(aL5[3*i+1] + aL5[3*i+2] * theTau);
    }

    theL  = theL5; theL *= theTau;
    theL += theL4; theL *= theTau;
    theL += theL3; theL *= theTau;
    theL += theL2; theL *= theTau;
    theL += theL1; theL *= theTau;
    theL += theL0; theL /= 1E8;
    theL  = Hmelib.NormAngle180(theL);

    return theL;
  }


  /**
This class can only be instantiated by a subclass.
 */

  protected VSOP87() {}

}
