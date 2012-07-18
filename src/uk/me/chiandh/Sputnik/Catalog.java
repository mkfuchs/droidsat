
package uk.me.chiandh.Sputnik;

import uk.me.chiandh.Lib.Hmelib;

/**
<p>The <code>Catalog</code> class is for storage of an array of positions in
space, such as those of satellites, planets or stars.  The position stored
is rectangular, and the unit is Gm.  Typical heights above sea level are
then 10<sup>-9</sup>, while c/H, the speed of light divided by the Hubble
constant, is 10<sup>26</sup>.  The radius of a proton would be on the
order of 10<sup>-24</sup>.  So this is a sensible unit to describe
anything in the Universe.  All these numbers are within the range of a
double precision number, most probably even their fourth powers are.</p>

<p>The epoch (state of proper motion for stars, time for which the ephemeris
of a comet etc is caltulated, etc.) is unspecified.  The positions refer
to the mean equinox of J2000.0.  Hence the state of precession is known
and the state of nutation ignored.  Positions beyond the Solar System are
heliocentric, i.e. parallax and annual aberration are not applied.
Positions within the Solar System are mean geocentric astrometric,
i.e. the light travel time from the planet to the Earth has been corrected
for, but annual aberration is not applied.</p>

<p>The positions can be stored and retrieved in a variety of coordinate
systems other than J2000, which is used internally.  But in all cases we
deal with mean rather than true and astrometric positions rather than
true, apparent or observed positions.  The coordinate systems we deal with
form a logical sequence of four with three branches.  Arguably you could
re-arange this into a sequence of six with one branch:</p>

<pre>
    B1950 ------ J2000 ------ mean ------ topo.
      |                        |           |
      |                        |           |
     gal.                     ecl.        hori.
</pre>

<dl>
<dt>J2000</dt>
<dd>Heliocentric mean or geocentric astrometric position for equinox
  J2000.0.  This is the internal representation.  The spherical coordinates
  in this system are right ascension (RA) and declination (Dec).</dd>
<dt>B1950</dt>
<dd>Heliocentric mean or geocentric astrometric position for equinox
  B1950 (J1949.99979).  This is the standard equinox in common use for
  much of the 20th century.  The coordinates differ from J2000 due
  to precession, but also due to a change in the reference system (FK4
  instead of FK5).  The spherical coordinates
  in this system are right ascension (RA) and declination (Dec).</dd>
<dt>Mean equinox of date</dt>
<dd>Heliocentric mean or geocentric astrometric position for equinox
  of date.  This differs from J2000 due to precession.  This is often the
  natural system to use, in particular in the Solar System and in conversion
  to topocentric or horizontal coordinates.  The spherical coordinates
  in this system are right ascension (RA) and declination (Dec).</dd>
<dt>Topocentric</dt>
<dd>Topocentric astrometric position for equinox of date.  Since we ignore
  annual parallaxes, positions outside the Solar System are here not
  strictly topocentric, just like in J2000 above they are not strictly
  geocentric.  Topocentric coordinates differ from "mean equinox of date"
  coordinates due to diurnal parallax, i.e. the offset of the observatory
  from the centre of the Earth.  Another difference is that local sidereal
  time is taken into account.  The spherical coordinates
  in this system are hour angle (HA) and declination (Dec).</dd>
<dt>Galactic l<sup>II</sup>,b<sup>II</sup></dt>
<dd>Galactic coordinates as defined after radio observations in the middle
  of the 20th century indicated the location of the galactic centre.  The
  transform from and to B1950 defines this system.  The spherical
  coordinates in this system are longitude along the galactic plane counting
  from the galactic centre (l) and latitude above the galactic plane (b).</dd>
<dt>Ecliptic</dt>
<dd>Mean ecliptic coordinates.  These depend on the mean obliquity at the
  time and are longitude along the ecliptic from the mean equinox of date
  and latitude above the ecliptic.</dd>
<dt>Horizontal</dt>
<dd>Mean topocentric position in azimuth along the horizon counting from
  North through East and elevation above the mathematical horizon.
  Refraction is not taken into account.</dd>
</dl>

<p>An overview of terms used in stellar and planetary coordinates seems
useful here.  For stars:</p>

<ul>
  <li>The <em>absolute position</em> is from a fundamental catalogue.  Such
  catalogues contain only thousands of stars for which many careful
  measurements have been carried out.</li>
  <li>A <em>relative position</em> is from larger and less precise
  catalogues.</li>
  <li>An <em>approximate position</em> is from even larger and even less
  precise catalogues.</li>
  <li>The first set of corrections to make to get from catalogue positions to
  what one observes at a particular station at a particular time, is for
  proper motion and secular aberration, due to the stars in the Galaxy
  moving with different spatial velocities.
  <ul>
    <li>Correction for <em>proper motion</em> itself just takes account of the
    fact that at different times the position of the star is different.
    Catalogue positions are valid for a certain <em>epoch</em>.  Reduction to
    different epochs is by linear extrapolation of right ascension and
    declination.</li>
  </ul>
  </li>
  <li>The next effect to take account of is <em>precession</em>.  Precession
  is due (in increasing order of importance) to the long-term changes in (i)
  the orientation of the ecliptic in the galactic environment, (ii) the
  inclination of the equator against the ecliptic, (iii) the position of the
  equatorial pole on the sky.  Catalogues list positions for a certain
  <em>equinox</em> or <em>equinox and ecliptic</em>.  This is in fact a
  time, meaning they use the mean orientation of ecliptic and equator as
  they were or will be at that time.</li>
  <li>The <em>mean position</em> is after reduction for precession to the mean
  equinox and ecliptic of your choice.  More to the point this is before
  reduction for nutation.</li>
  <li><em>Nutation</em> is due to a shorter-term variation of the orientation
  of ecliptic and equator.  The effect is not exactly small, but unlike
  precession it does not accumulate over hundreds or thousands of years.</li>
  <li>The <em>true position</em> is after reduction for nutation to the
  true equinox and ecliptic of your choice.</li>
  <li>Next is a triplet of reductions from heliocentric to geocentric.
  <ul>
    <li>The largest effect here is <em>annual aberration</em>.  Due to the
    Earth's heliocentric velocity star's positions are shifted slightly
    towards the forward direction of this motion.  This effect is strongest
    at about 20 arc seconds (v/c rad) at the ecliptical longitude of the Sun
    and at its opposite longitude.</li>
    <li>The <em>apparent position</em> is after correction for annual
    aberration.</li>
    <li><em>Light deflection</em> due to the Sun's gravitation amounts to 2 arc
    seconds near the limb of the Sun.</li>
    <li>The <em>annual parallax</em> is the small shift in position that a star
    suffers due to the Earth being on different sides of the Sun at different
    times of the year.</li>
  </ul>
  </li>
  <li>The <em>observed position</em> is after correction for all these
  effects.  It is a geocentric position.</li>
  <li>Reduction to the topocentre involves reduction for <em>diurnal
  parallax</em> and <em>diurnal aberration</em>, the offset of the
  observatory from the centre of the Earth and the velocity of the
  observatory due to the Earth's rotation.</li>
  <li><em>Polar motion</em> is the change of the Earth's rotation axis with
  repect to the Earth's surface.  There are two main periodic effects here.
  One is the seasonal change of the mass distribution near the surface -
  leaves growing in spring and falling to the ground in autumn.  The other
  is due to the elasticity of the Earth.  The effect is small at about 0.3
  arc second.</li>
  <li><em>Refraction</em> is the effect whereby the Earth's atmosphere acts
  like a lens and lifts positions to higher elevation.  Near the horizon the
  effect is not small at about 33 arc minutes for visible light.</li>
</ul>

<p>For planets the terminology is a little different.  Planetary ephemeris
are often expressed in the <em>mean equinox and ecliptic of date</em>,
meaning they use the orientation of ecliptic and equator as it is at the
time for which the ephemeris is valid.</p>

<ul>
<li>For a planet's <em>geometric position</em> no light time effects have
  been corrected.  The geocentric position is simply the difference between
  the heliocentric positions of the planet and the Earth.</li>
<li>For an <em>astrometric position</em> light time effects of the
  heliocentric velocity of the planet have been corrected.  This can be
  compared to a stellar position that has been reduced for parallax but not
  for aberration.  Since the stellar parallax is small compared to the
  aberration, this is comparable to the mean position of a star.</li>
<li>For an <em>apparent position</em> light time effects of the geocentric
  velocity of the planet have been corrected.  This can be compared to the
  observed position of a star (i.e. one where annual aberration has been
  applied).</li>
</ul>

<p>Sputnik uses mean positions for stars and astrometric positions for
planets.  In converting to topocentre, Sputnik ignores nutation, all three
annual effects, diurnal aberration and polar motion.  Refraction is taken
into account only for calculating rise and set times.</p>

<p>Copyright: &copy; 2002-2003 Horst Meyerdierks.</p>

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
<dt><strong>2002-06-15:</strong> hme</dt>
<dd>Translated from C++ (Sputnik 1.9).</dd>
<dt><strong>2002-06-16:</strong> hme</dt>
<dd>Coordinate transforms Gal-B1950-J2000-Mean should work now.  The
  B1950/J2000 transform uses FK4 precession, and takes care of
the equinox correction necessary for FK4/FK5 transition.</dd>
<dt><strong>2002-06-16:</strong> hme</dt>
<dd>Obliquity() and conversions between RA/Dec and ecliptic.</dd>
<dt><strong>2002-06-20:</strong> hme</dt>
<dd>Set*() methods for all systems within the domain of this class.</dd>
<dt><strong>2002-06-21:</strong> hme</dt>
<dd>Coordinate transforms to HA/Dec and A/h.</dd>
<dt><strong>2002-06-22:</strong> hme</dt>
<dd>Add SetEcl(), SetTopo(), SetHori().</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Consolidating documentation.</dd>
<dt><strong>2002-07-16:</strong> hme</dt>
<dd>Make the Set*() methods protected.  Most subclasses should not permit
  the use of these by their user.  E.g. the Sun (subsubclass of this will
  never be at the position the user might set with these.  The NamedObject
  class could have a subclass FixedObject that could make these methods
  public, but for now there is no need, because user commands to set
  coordinates are dealt with by the NamedObject class which can use these
  protected methods without problem.</dd>
<dt><strong>2003-03-09:</strong> hme</dt>
<dd>Review documentation, in particular mathematics.</dd>
<dt><strong>2003-06-14:</strong> hme</dt>
<dd>Add GetHori() public method.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2003-09-17:</strong> hme</dt>
<dd>Add GetTopo() method.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Times
@see Station
@see uk.me.chiandh.Lib.Hmelib
 */


public class Catalog
{

  /** Conversion matrix B1950 to galactic longitude and latitude. */

  private static final double B50toLB[] = {
      Math.cos(282.25/Hmelib.DEGPERRAD)
    * Math.cos(327.  /Hmelib.DEGPERRAD)
    - Math.sin(282.25/Hmelib.DEGPERRAD)
    * Math.sin(327.  /Hmelib.DEGPERRAD)
    * Math.cos( 62.6 /Hmelib.DEGPERRAD),

      Math.sin(282.25/Hmelib.DEGPERRAD)
    * Math.cos(327.  /Hmelib.DEGPERRAD)
    + Math.cos(282.25/Hmelib.DEGPERRAD)
    * Math.sin(327.  /Hmelib.DEGPERRAD)
    * Math.cos( 62.6 /Hmelib.DEGPERRAD),

      Math.sin(327.  /Hmelib.DEGPERRAD)
    * Math.sin( 62.6 /Hmelib.DEGPERRAD),

     -Math.cos(282.25/Hmelib.DEGPERRAD)
    * Math.sin(327.  /Hmelib.DEGPERRAD)
    - Math.sin(282.25/Hmelib.DEGPERRAD)
    * Math.cos(327.  /Hmelib.DEGPERRAD)
    * Math.cos( 62.6 /Hmelib.DEGPERRAD),

     -Math.sin(282.25/Hmelib.DEGPERRAD)
    * Math.sin(327.  /Hmelib.DEGPERRAD)
    + Math.cos(282.25/Hmelib.DEGPERRAD)
    * Math.cos(327.  /Hmelib.DEGPERRAD)
    * Math.cos( 62.6 /Hmelib.DEGPERRAD),

      Math.cos(327.  /Hmelib.DEGPERRAD)
    * Math.sin( 62.6 /Hmelib.DEGPERRAD),

      Math.sin(282.25/Hmelib.DEGPERRAD)
    * Math.sin( 62.6 /Hmelib.DEGPERRAD),

     -Math.cos(282.25/Hmelib.DEGPERRAD)
    * Math.sin( 62.6 /Hmelib.DEGPERRAD),

      Math.cos( 62.6 /Hmelib.DEGPERRAD)
  };


  /** Parameter for matrix to convert between B1950 and J2000. */

  private static final double zeta  = 1152.552 / 3600. / Hmelib.DEGPERRAD;


  /** Parameter for matrix to convert between B1950 and J2000. */


  private static final double z     = 1152.750 / 3600. / Hmelib.DEGPERRAD;


  /** Parameter for matrix to convert between B1950 and J2000. */

  private static final double theta = 1002.016 / 3600. / Hmelib.DEGPERRAD;


  /** Conversion matrix B1950 to J2000. */

  private static final double B50toJ00[] = {
    +Math.cos(zeta) * Math.cos(theta) * Math.cos(z)
   - Math.sin(zeta)                   * Math.sin(z),
    -Math.sin(zeta) * Math.cos(theta) * Math.cos(z)
   - Math.cos(zeta)                   * Math.sin(z),
                     -Math.sin(theta) * Math.cos(z),
    +Math.cos(zeta) * Math.cos(theta) * Math.sin(z)
   + Math.sin(zeta)                   * Math.cos(z),
    -Math.sin(zeta) * Math.cos(theta) * Math.sin(z)
   + Math.cos(zeta)                   * Math.cos(z),
                     -Math.sin(theta) * Math.sin(z),
    +Math.cos(zeta) * Math.sin(theta),
    -Math.sin(zeta) * Math.sin(theta),
                     +Math.cos(theta)
  };


  /** The number of positions in the catalogue. */

  protected int itsNpos;


  /**
   * The positions.
   *
   * <p>These are itsNpos triplets of numbers, each triplet being x,y,z in Gm.
   * The coordinate system is mean equinox of J2000.0. */

  protected double itsR[];


  /**
   * Initialise the object.
   *
   * <p>This creates the array itsR[], but does not initialise the array
   * values.
   *
   * @param aNpos
   *   Obtain space for so many xyz triplets of coordinates. */

  public void Init(int aNpos)
  {
    itsNpos = aNpos;
    itsR = new double[3 * itsNpos];
  }


  /**
   * Convert B1950 to galactic coordinates.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>The transform uses Euler angles of 282.25&deg;, 327&deg; and
   * 62.6&deg;.  The last of these is the inclination of the galactic plane,
   * as the galactic pole is defined to be at declination 27.4&deg;.  The
   * second angle comes from the distance of 33&deg; between the Galactic
   * Centre and the node of the galactic plane with the equator.  The first
   * angle is 90&deg; larger than the right ascension of the galactic pole.
   * See e.g.
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.89f.
   *
   * <p>The matrix to transform from B1950 to galactic is
   *
   * <p>M<sub>11</sub> = cos(282.25&deg;) cos(327&deg;)
   *   - sin(282.25&deg;) sin(327&deg;) cos(62.6&deg;)
   * <br>M<sub>12</sub> = sin(282.25&deg;) cos(327&deg;)
   *   + cos(282.25&deg;) sin(327&deg;) cos(62.6&deg;)
   * <br>M<sub>13</sub> = sin(327&deg;) sin(62.6&deg;)
   * <br>M<sub>21</sub> = -cos(282.25&deg;) sin(327&deg;)
   *   - sin(282.25&deg;) cos(327&deg;) cos(62.6&deg;)
   * <br>M<sub>22</sub> = -sin(282.25&deg;) sin(327&deg;)
   *   + cos(282.25&deg;) cos(327&deg;) cos(62.6&deg;)
   * <br>M<sub>23</sub> = cos(327&deg;) sin(62.6&deg;)
   * <br>M<sub>31</sub> = sin(282.25&deg;) sin(62.6&deg;)
   * <br>M<sub>32</sub> = -cos(282.25&deg;) sin(62.6&deg;)
   * <br>M<sub>33</sub> = cos(62.6&deg;)
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */

  protected final void B19502Gal(int aNpos,
    double inTriplets[], double outTriplets[])
  {
    int i;
    for (i = 0; i < aNpos; i++) {
      outTriplets[3*i+0] = B50toLB[0] * inTriplets[3*i+0]
                         + B50toLB[1] * inTriplets[3*i+1]
                         + B50toLB[2] * inTriplets[3*i+2];
      outTriplets[3*i+1] = B50toLB[3] * inTriplets[3*i+0]
                         + B50toLB[4] * inTriplets[3*i+1]
                         + B50toLB[5] * inTriplets[3*i+2];
      outTriplets[3*i+2] = B50toLB[6] * inTriplets[3*i+0]
                         + B50toLB[7] * inTriplets[3*i+1]
                         + B50toLB[8] * inTriplets[3*i+2];
    }
  }


  /**
   * Convert B1950 to J2000 coordinates.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.130.
   * illustrates the conversion from FK4 to FK5 and in particular from B1950
   * FK4 to a Julian epoch and FK5.  In that case his start epoch is
   * signified by T&nbsp;=&nbsp;0.5.  We specialise further and fix the end
   * to J2000, hence the epoch difference is t&nbsp;=&nbsp;0.5, also.
   *
   * <p>T = (B1950.0 - B1900.0) / 100 = 0.5
   * <br>t = (J2000.0 - B1950.0) / 100 = 0.5
   * <p>&zeta; = (2304.250 + 1.396 T) t
   *     +     0.302 t<sup>2</sup>
   *     +     0.018 t<sup>3</sup>
   * <br>z/" = &zeta;/"
   *     +     0.791 t<sup>2</sup>
   *     +     0.001 t<sup>3</sup>
   * <br>&theta; = (2004.682 - 0.853 T) t
   *     -     0.426 t<sup>2</sup>
   *     -     0.042 t<sup>3</sup>
   * <p>&zeta; = 1152.552"
   * <br>z = 1152.750"
   * <br>&theta; = 1002.016"
   *
   * <p>Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.126,
   * does not give the transform matrix.  From
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.B18,
   * we presume that for transfrom from T to T+t it is:
   *
   * <p>M<sub>11</sub>  =  cos(&zeta;)  * cos(&theta;) * cos(z)
   *   - sin(&zeta;) * sin(z)
   * <br>M<sub>12</sub> = -sin(&zeta;)  * cos(&theta;) * cos(z)
   *   - cos(&zeta;) * sin(z)
   * <br>M<sub>13</sub> = -sin(&theta;) * cos(z)
   * <br>M<sub>21</sub> =  cos(&zeta;)  * cos(&theta;) * sin(z)
   *   + sin(&zeta;) * cos(z)
   * <br>M<sub>22</sub> = -sin(&zeta;)  * cos(&theta;) * sin(z)
   *   + cos(&zeta;) * cos(z)
   * <br>M<sub>23</sub> = -sin(&theta;) * sin(z)
   * <br>M<sub>31</sub> =  cos(&zeta;)  * sin(&theta;)
   * <br>M<sub>32</sub> = -sin(&zeta;)  * sin(&theta;)
   * <br>M<sub>33</sub> =  cos(&theta;)
   *
   * <p>Returning to
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.130,
   * dealing with the precession between 1950 and 2000 like this is not
   * sufficient.  We have to add to the right ascension the equinox
   * correction
   *
   * <p>&Delta;&alpha; = (0.0775 + 0.0850 T) s = 1.8"
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */

  protected final void B19502J2000(int aNpos,
    double inTriplets[], double outTriplets[])
  {
    final double dRA = 1.8 / 3600. / Hmelib.DEGPERRAD;
    double x, y;
    int    i;

    for (i = 0; i < aNpos; i++) {

      /* Precession. */

      x                  = B50toJ00[0] * inTriplets[3*i+0]
                         + B50toJ00[1] * inTriplets[3*i+1]
                         + B50toJ00[2] * inTriplets[3*i+2];
      y                  = B50toJ00[3] * inTriplets[3*i+0]
                         + B50toJ00[4] * inTriplets[3*i+1]
                         + B50toJ00[5] * inTriplets[3*i+2];
      outTriplets[3*i+2] = B50toJ00[6] * inTriplets[3*i+0]
                         + B50toJ00[7] * inTriplets[3*i+1]
                         + B50toJ00[8] * inTriplets[3*i+2];

      /* Equinox correction FK4-FK5. */

      outTriplets[3*i+0] = x * Math.cos(dRA) - y * Math.sin(dRA);
      outTriplets[3*i+1] = x * Math.sin(dRA) + y * Math.cos(dRA);

    }
  }


  /**
   * Convert equinox of date ecliptic coordinates to RA/Dec.
   *
   * <p>See also {@link #Mean2Ecl Mean2Ecl}. */

  static double matEcl2Mean[] = {0.,0.,0.,0.,0.,0.,0.,0.,0.};
  protected final void Ecl2Mean(int aNpos, Times aEquinox,
    double inTriplets[], double outTriplets[])
  {
    double eps;
    int    i;

    eps = Obliquity(aEquinox);

//    double mat[] = {
//      1.,     0.,            0.,
//      0.,  Math.cos(eps), Math.sin(eps),
//      0., -Math.sin(eps), Math.cos(eps)
//    };
    
    matEcl2Mean[0] = 1.;
    matEcl2Mean[1] = 0.;
    matEcl2Mean[2] = 0.;
    matEcl2Mean[3] = 0.;
    matEcl2Mean[4] = Math.cos(eps);
    matEcl2Mean[5] = Math.sin(eps);
    matEcl2Mean[6] = 0.;
    matEcl2Mean[7] = -Math.sin(eps);
    matEcl2Mean[8] = Math.cos(eps);
   

    for (i = 0; i < aNpos; i++) {
      outTriplets[3*i+0] = matEcl2Mean[0] * inTriplets[3*i+0]
                         + matEcl2Mean[3] * inTriplets[3*i+1]
                         + matEcl2Mean[6] * inTriplets[3*i+2];
      outTriplets[3*i+1] = matEcl2Mean[1] * inTriplets[3*i+0]
                         + matEcl2Mean[4] * inTriplets[3*i+1]
                         + matEcl2Mean[7] * inTriplets[3*i+2];
      outTriplets[3*i+2] = matEcl2Mean[2] * inTriplets[3*i+0]
                         + matEcl2Mean[5] * inTriplets[3*i+1]
                         + matEcl2Mean[8] * inTriplets[3*i+2];
    }
  }


  /**
   * Convert galactic to B1950 coordinates.
   *
   * See also {@link #B19502Gal B19502Gal}. */

  protected final void Gal2B1950(int aNpos,
    double inTriplets[], double outTriplets[])
  {
    int i;
    for (i = 0; i < aNpos; i++) {
      outTriplets[3*i+0] = B50toLB[0] * inTriplets[3*i+0]
                         + B50toLB[3] * inTriplets[3*i+1]
                         + B50toLB[6] * inTriplets[3*i+2];
      outTriplets[3*i+1] = B50toLB[1] * inTriplets[3*i+0]
                         + B50toLB[4] * inTriplets[3*i+1]
                         + B50toLB[7] * inTriplets[3*i+2];
      outTriplets[3*i+2] = B50toLB[2] * inTriplets[3*i+0]
                         + B50toLB[5] * inTriplets[3*i+1]
                         + B50toLB[8] * inTriplets[3*i+2];
    }
  }


  /**
   * Get the horizontal spherical coordinates.
   *
   * <p>This retrieves the position stored with conversion to horizontal
   * spherical coordinates (azimuth and elevation).
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be retrieved.
   * @param aStation
   *   The time and location of the observatory.  The station clock is also
   *   the date of the equinox.
   * @param aTriplet
   *   Three floating point numbers containing the azimuth in radian (North is
   *   zero, East 90&deg;, etc.), the elevation in radian and the
   *   topocentric distance in Gm. */

  static double t1GetHori[] = {0.,0.,0.};
  static double t2GetHori[] = {0.,0.,0.};

  protected void GetHori(int aIndex, Station aStation, double aTriplet[])
  {
    
    int    i;

    for (i = 0; i < 3; i++) t1GetHori[i] = itsR[3*aIndex+i];
    J20002Mean(1, aStation, t1GetHori, t2GetHori);
    Mean2Topo(1,  aStation, t2GetHori, t1GetHori);
    Topo2Hori(1,  aStation, t1GetHori, t2GetHori);
    Hmelib.Spher(t2GetHori, aTriplet);
    aTriplet[0] = Hmelib.NormAngle180(aTriplet[0]);
  }


  /**
   * Get the topocentric spherical coordinates.
   *
   * <p>This retrieves the position stored with conversion to topocentric
   * spherical coordiaates (hour angle and declination).
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be retrieved.
   * @param aStation
   *   The time and location of the observatory.  The station clock is also
   *   the date of the equinox.
   * @param aTriplet
   *   Three floating point numbers containing the hour angle in radian
   *   (South is zero, West 6&nbsp;h, etc.), the declination in radian and the
   *   topocentric distance in Gm. */

  static double t1GetTopo[] =  {0.,0.,0.};
  static double t2GetTopo[] = {0.,0.,0.};
  protected void GetTopo(int aIndex, Station aStation, double aTriplet[])
  {
    
    
    int    i;

    for (i = 0; i < 3; i++) t1GetTopo[i] = itsR[3*aIndex+i];
    J20002Mean(1, aStation, t1GetTopo, t2GetTopo);
    Mean2Topo(1,  aStation, t2GetTopo, t1GetTopo);
    Hmelib.Spher(t1GetTopo, aTriplet);
    aTriplet[0] = Hmelib.NormAngle180(aTriplet[0]);
  }


  /**
   * Convert azimuth and elevation to HA/Dec.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>The transform is
   *
   * <p><table>
   * <tr align="center">
   *   <td>(<br>(<br>(</td>
   *   <td>x'<br>y'<br>z'</td>
   *   <td>)<br>)<br>)</td>
   *   <td>=</td>
   *   <td>(<br>(<br>(</td>
   *   <td>-sin(&phi;)<br>0<br>cos(&phi;)</td>
   *   <td>0<br>-1<br>0</td>
   *   <td>cos(&phi;)<br>0<br>sin(&phi;)</td>
   *   <td>)<br>)<br>)</td>
   *   <td>(<br>(<br>(</td>
   *   <td>x"<br>y"<br>z"</td>
   *   <td>)<br>)<br>)</td>
   * </tr>
   * </table>
   *
   * <p>The matrix here includes an inversion of the x and y axes, which
   * amounts to a rotation by 180 degrees about the z axis.  This takes account
   * of the fact that azimuth counts from North while hour angle counts from
   * South.  Both count retrograde (clockwise).
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param aStation
   *   The location of the observatory.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */

  protected final void Hori2Topo(int aNpos, Station aStation,
    double inTriplets[], double outTriplets[])
  {
    double mat[] = {0.,0.,0.,0.};
    double theLat;
    int i;

    theLat = aStation.GetLat();

    mat[0] = -Math.sin(theLat); mat[1] =  Math.cos(theLat);
    mat[2] =  Math.cos(theLat); mat[3] =  Math.sin(theLat);

    for(i = 0; i < aNpos; i++) {
      outTriplets[3*i]   =  inTriplets[3*i]   * mat[0]
                         +  inTriplets[3*i+2] * mat[1];
      outTriplets[3*i+1] = -inTriplets[3*i+1];
      outTriplets[3*i+2] =  inTriplets[3*i]   * mat[2]
                         +  inTriplets[3*i+2] * mat[3];
    }
  }


  /**
   * Convert J2000 to B1950 coordinates.
   *
   * See also {@link #B19502J2000 B19502J2000}. */

  protected final void J20002B1950(int aNpos,
    double inTriplets[], double outTriplets[])
  {
    final double dRA = 1.8 / 3600. / Hmelib.DEGPERRAD;
    double x, y;
    int    i;

    for (i = 0; i < aNpos; i++) {

      /* Equinox correction FK4-FK5. */

      x = inTriplets[3*i+0] * Math.cos(dRA)
        + inTriplets[3*i+1] * Math.sin(dRA);
      y = inTriplets[3*i+1] * Math.cos(dRA)
        - inTriplets[3*i+0] * Math.sin(dRA);

      /* Precession. */

      outTriplets[3*i+0] = B50toJ00[0] * x
                         + B50toJ00[3] * y
                         + B50toJ00[6] * inTriplets[3*i+2];
      outTriplets[3*i+1] = B50toJ00[1] * x
                         + B50toJ00[4] * y
                         + B50toJ00[7] * inTriplets[3*i+2];
      outTriplets[3*i+2] = B50toJ00[2] * x
                         + B50toJ00[5] * y
                         + B50toJ00[8] * inTriplets[3*i+2];
    }
  }


  /**
   * Convert J2000 to equinox of date coordinates.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>The transform uses a matrix calculated from
   * USNO/RGO, 1990, <em>The Astronomical Almanach for the Year 1992</em>, U.S. Government Printing Office, Washington DC, Her Majesty's Stationery Office, London, p.B18.
   *
   * <p>t = (Ep - J2000.0) / 100
   * <p>&zeta;/&deg;
   *     = 0.6406161 t + 8.39 10<sup>-5</sup> t<sup>2</sup>
   *     + 5 10<sup>-6</sup> t<sup>3</sup>
   * <br>z/&deg; = 0.6406161 t + 3.041 10<sup>-4</sup> t<sup>2</sup>
   *     + 5.1 10<sup>-6</sup> t<sup>3</sup>
   * <br>&theta;/&deg;
   *     = 0.556753 t - 1.185 10<sup>-4</sup> t<sup>2</sup>
   *     - 1.16 10<sup>-5</sup> t<sup>3</sup>
   * <p>M<sub>11</sub> = cos(&zeta;) * cos(&theta;) * cos(z)
   *   - sin(&zeta;) * sin(z)
   * <br>M<sub>12</sub> = -sin(&zeta;) * cos(&theta;) * cos(z)
   *   - cos(&zeta;) * sin(z)
   * <br>M<sub>13</sub> = -sin(&theta;) * cos(z)
   * <br>M<sub>21</sub> = cos(&zeta;) * cos(&theta;) * sin(z)
   *   + sin(&zeta;) * cos(z)
   * <br>M<sub>22</sub> = -sin(&zeta;) * cos(&theta;) * sin(z)
   *   + cos(&zeta;) * cos(z)
   * <br>M<sub>23</sub> = -sin(&theta;) * sin(z)
   * <br>M<sub>31</sub> = cos(&zeta;) * sin(&theta;)
   * <br>M<sub>32</sub> = -sin(&zeta;) * sin(&theta;)
   * <br>M<sub>33</sub> = cos(&theta;)
   *
   * <p>This matrix transforms from J2000 to Ep.  The inverse transform is
   * achieved with the transposed matrix.
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param aEquinox
   *   The equinox to which the returned coordinates should refer.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */

  static double matJ2000[] = {0.,0.,0.,0.,0.,0.,0.,0,0.};
  protected final void J20002Mean(int aNpos, Times aEquinox,
    double inTriplets[], double outTriplets[])
  {
    double t, zeta, z, theta;
    int    i;

    t     = (aEquinox.GetJulEpoch() - 2000.) / 100.;
    zeta  = .6406161 * t +  8.39e-5 * t * t
                         +  5e-6    * t * t * t;
    z     = .6406161 * t + 3.041e-4 * t * t
                         +  5.1e-6  * t * t * t;
    theta = .556753  * t - 1.185e-4 * t * t
                         - 1.16e-5  * t * t * t;
    zeta  /= Hmelib.DEGPERRAD;
    z     /= Hmelib.DEGPERRAD;
    theta /= Hmelib.DEGPERRAD;

//    double matJ2000[] = {
//      +Math.cos(zeta) * Math.cos(theta) * Math.cos(z)
//     - Math.sin(zeta)                   * Math.sin(z),
//      -Math.sin(zeta) * Math.cos(theta) * Math.cos(z)
//     - Math.cos(zeta)                   * Math.sin(z),
//                       -Math.sin(theta) * Math.cos(z),
//      +Math.cos(zeta) * Math.cos(theta) * Math.sin(z)
//     + Math.sin(zeta)                   * Math.cos(z),
//      -Math.sin(zeta) * Math.cos(theta) * Math.sin(z)
//     + Math.cos(zeta)                   * Math.cos(z),
//                       -Math.sin(theta) * Math.sin(z),
//      +Math.cos(zeta) * Math.sin(theta),
//      -Math.sin(zeta) * Math.sin(theta),
//                       +Math.cos(theta)
//    };
    matJ2000[0] = +Math.cos(zeta) * Math.cos(theta) * Math.cos(z)
				- Math.sin(zeta) * Math.sin(z);
    
    matJ2000[1] = -Math.sin(zeta) * Math.cos(theta) * Math.cos(z)
				- Math.cos(zeta) * Math.sin(z);
    matJ2000[2] = -Math.sin(theta) * Math.cos(z);
    
    matJ2000[3] = +Math.cos(zeta) * Math.cos(theta) * Math.sin(z)
    + Math.sin(zeta)                   * Math.cos(z);
    matJ2000[4] = -Math.sin(zeta) * Math.cos(theta) * Math.sin(z)
    + Math.cos(zeta)                   * Math.cos(z);

    matJ2000[5] =  -Math.sin(theta) * Math.sin(z);
    matJ2000[6] = +Math.cos(zeta) * Math.sin(theta);

    matJ2000[7] =  -Math.sin(zeta) * Math.sin(theta);
    matJ2000[8] =  +Math.cos(theta);

    for (i = 0; i < aNpos; i++) {
      outTriplets[3*i+0] = matJ2000[0] * inTriplets[3*i+0]
                         + matJ2000[1] * inTriplets[3*i+1]
                         + matJ2000[2] * inTriplets[3*i+2];
      outTriplets[3*i+1] = matJ2000[3] * inTriplets[3*i+0]
                         + matJ2000[4] * inTriplets[3*i+1]
                         + matJ2000[5] * inTriplets[3*i+2];
      outTriplets[3*i+2] = matJ2000[6] * inTriplets[3*i+0]
                         + matJ2000[7] * inTriplets[3*i+1]
                         + matJ2000[8] * inTriplets[3*i+2];
    }
  }


  /**
   * Convert equinox of date RA/Dec to ecliptic coordinates.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>The matrix to transform from RA/Dec to ecliptic is
   *
   * <p><table>
   * <tr align="center">
   *   <td>M =</td>
   *   <td>(<br>(<br>(</td>
   *   <td>1<br>0<br>0</td>
   *   <td>0<br>cos(&epsilon;)<br>
   *           -sin(&epsilon;)</td>
   *   <td>0<br>sin(&epsilon;)<br>
   *            cos(&epsilon;)</td>
   *   <td>)<br>)<br>)</td>
   * </tr>
   * </table>
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param aEquinox
   *   The equinox to which the given coordinates refer, also the time for
   *   which the obliquity of the ecliptic needs to be calculated.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */

  static double matMean2Ecl[] = {0.,0.,0.,0.,0.,0.,0.,0};
  protected final void Mean2Ecl(int aNpos, Times aEquinox,
    double inTriplets[], double outTriplets[])
  {
    double eps;
    int    i;

    eps = Obliquity(aEquinox);

    double matMean2Ecl[] = {
      1.,     0.,            0.,
      0.,  Math.cos(eps), Math.sin(eps),
      0., -Math.sin(eps), Math.cos(eps)
    };

    for (i = 0; i < aNpos; i++) {
      outTriplets[3*i+0] = matMean2Ecl[0] * inTriplets[3*i+0]
                         + matMean2Ecl[1] * inTriplets[3*i+1]
                         + matMean2Ecl[2] * inTriplets[3*i+2];
      outTriplets[3*i+1] = matMean2Ecl[3] * inTriplets[3*i+0]
                         + matMean2Ecl[4] * inTriplets[3*i+1]
                         + matMean2Ecl[5] * inTriplets[3*i+2];
      outTriplets[3*i+2] = matMean2Ecl[6] * inTriplets[3*i+0]
                         + matMean2Ecl[7] * inTriplets[3*i+1]
                         + matMean2Ecl[8] * inTriplets[3*i+2];
    }
  }


  /**
   * Convert equinox of date to J2000 coordinates.
   *
   * <p>See also {@link #J20002Mean J20002Mean}. */
  static double matMean2J2000[] = {0.,0.,0.,0.,0.,0.,0.,0.,0.};
  protected final void Mean2J2000(int aNpos, Times aEquinox,
    double inTriplets[], double outTriplets[])
  {
    double t, zeta, z, theta;
    int    i;

    t     = (aEquinox.GetJulEpoch() - 2000.) / 100.;
    zeta  = .6406161 * t +  8.39e-5 * t * t
                         +  5e-6    * t * t * t;
    z     = .6406161 * t + 3.041e-4 * t * t
                         +  5.1e-6  * t * t * t;
    theta = .556753  * t - 1.185e-4 * t * t
                         - 1.16e-5  * t * t * t;
    zeta  /= Hmelib.DEGPERRAD;
    z     /= Hmelib.DEGPERRAD;
    theta /= Hmelib.DEGPERRAD;

//    double matMean2J2000[] = {
//      +Math.cos(zeta) * Math.cos(theta) * Math.cos(z)
//     - Math.sin(zeta)                   * Math.sin(z),
//      -Math.sin(zeta) * Math.cos(theta) * Math.cos(z)
//     - Math.cos(zeta)                   * Math.sin(z),
//                       -Math.sin(theta) * Math.cos(z),
//      +Math.cos(zeta) * Math.cos(theta) * Math.sin(z)
//     + Math.sin(zeta)                   * Math.cos(z),
//      -Math.sin(zeta) * Math.cos(theta) * Math.sin(z)
//     + Math.cos(zeta)                   * Math.cos(z),
//                       -Math.sin(theta) * Math.sin(z),
//      +Math.cos(zeta) * Math.sin(theta),
//      -Math.sin(zeta) * Math.sin(theta),
//                       +Math.cos(theta)
//    };

    matMean2J2000[0] = +Math.cos(zeta) * Math.cos(theta) * Math.cos(z)
				- Math.sin(zeta) * Math.sin(z);

    matMean2J2000[1] = -Math.sin(zeta) * Math.cos(theta) * Math.cos(z)
				- Math.cos(zeta) * Math.sin(z);
    matMean2J2000[2] = -Math.sin(theta) * Math.cos(z);

    matMean2J2000[3] = +Math.cos(zeta) * Math.cos(theta) * Math.sin(z)
				+ Math.sin(zeta) * Math.cos(z);
    matMean2J2000[4] = -Math.sin(zeta) * Math.cos(theta) * Math.sin(z)
				+ Math.cos(zeta) * Math.cos(z);

    matMean2J2000[5] = -Math.sin(theta) * Math.sin(z);
    matMean2J2000[6] = +Math.cos(zeta) * Math.sin(theta);

    matMean2J2000[7] = -Math.sin(zeta) * Math.sin(theta);
    matMean2J2000[8] = +Math.cos(theta);
		
    for (i = 0; i < aNpos; i++) {
      outTriplets[3*i+0] = matMean2J2000[0] * inTriplets[3*i+0]
                         + matMean2J2000[3] * inTriplets[3*i+1]
                         + matMean2J2000[6] * inTriplets[3*i+2];
      outTriplets[3*i+1] = matMean2J2000[1] * inTriplets[3*i+0]
                         + matMean2J2000[4] * inTriplets[3*i+1]
                         + matMean2J2000[7] * inTriplets[3*i+2];
      outTriplets[3*i+2] = matMean2J2000[2] * inTriplets[3*i+0]
                         + matMean2J2000[5] * inTriplets[3*i+1]
                         + matMean2J2000[8] * inTriplets[3*i+2];
    }
  }


  /**
   * Convert geocentric RA/Dec to topocentric HA/Dec.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>The transform is
   *
   * <p><table>
   * <tr align="center">
   *   <td>(<br>(<br>(</td>
   *   <td>x'<br>y'<br>z'</td>
   *   <td>)<br>)<br>)</td>
   *   <td>=</td>
   *   <td>-</td>
   *   <td>(<br>(<br>(</td>
   *   <td>itsX<br>0<br>itsZ</td>
   *   <td>)<br>)<br>)</td>
   *   <td>+</td>
   *   <td>(<br>(<br>(</td>
   *   <td>cos(LST)<br>sin(LST)<br>0</td>
   *   <td>sin(LST)<br>-cos(LST)<br>0</td>
   *   <td>0<br>0<br>1</td>
   *   <td>)<br>)<br>)</td>
   *   <td>(<br>(<br>(</td>
   *   <td>x<br>y<br>z</td>
   *   <td>)<br>)<br>)</td>
   * </tr>
   * </table>
   *
   * <p>The matrix here includes an inversion of the topocentric y' axis.
   * While the geocentric y axis points towards Orion, the topocentric y' axis
   * points to the West point on the horizon.  Therefore
   * RA&nbsp;=&nbsp;atan(y/x) counts direct (counter-clockwise), but
   * HA&nbsp;=&nbsp;atan(y'/x') counts retrograde (clockwise).
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param aStation
   *   The time and location of the observatory.  There is an assumption that
   *   the given geocentric coordinates are mean RA/Dec for the equinox of the
   *   same date as the station clock shows.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */
static     double vec[] = {0.,0.,0.};

  protected final void Mean2Topo(int aNpos, Station aStation,
    double inTriplets[], double outTriplets[])
  {
    double theLST;
    //double mat[] = {0.,0.,0.,0.};
    int i;

    theLST = aStation.GetLST() * Math.PI / 12.;
    mat[0] =  Math.cos(theLST); mat[1] =  Math.sin(theLST);
    mat[2] =  Math.sin(theLST); mat[3] = -Math.cos(theLST);

    aStation.GetX0Z(vec);

    for(i = 0; i < aNpos; i++) {
      outTriplets[3*i]    = inTriplets[3*i]   * mat[0]
                          + inTriplets[3*i+1] * mat[1] - vec[0];
      outTriplets[3*i+1]  = inTriplets[3*i]   * mat[2]
                          + inTriplets[3*i+1] * mat[3];
      outTriplets[3*i+2]  = inTriplets[3*i+2]          - vec[2];
    }
  }


  /**
   * Obliquity of the ecliptic.
   *
   * <p>We use the expression from
   * Jean Meeus, 1991, <em>Astronomical Algorithms</em>, Willmann-Bell, Richmond VA, p.135:
   *
   * <p>U = (Ep - 2000) / 10000
   * <p>&epsilon;
   *     = 23&deg; + 26'
   *     + (21.448
   *     - 4680.93 U
   *     -    1.55 U<sup>2</sup>
   *     + 1999.25 U<sup>3</sup>
   *     -   51.38 U<sup>4</sup>
   * <br>-  249.67 U<sup>5</sup>
   *     -   39.05 U<sup>6</sup>
   *     +    7.12 U<sup>7</sup>
   *     +   27.87 U<sup>8</sup>
   *     +    5.79 U<sup>9</sup>
   *     +    2.45 U<sup>10</sup>)"
   *
   * @param aTime
   *   The time for which the obliquity (w.r.t. the equator and equinox of
   *   date) is required. */

  protected final double Obliquity(Times aTime)
  {
    double theTime;
    double theEps;

    /* Obtain the time in units of 10000 Julian years from J2000.0. */

    theTime = (aTime.GetJulEpoch() - 2000.) / 10000.;

    /* Use equation 21.3 of Meeus, 1991. */

    theEps  = theTime *      2.45;
    theEps  = theTime * (    5.79 + theEps);
    theEps  = theTime * (   27.87 + theEps);
    theEps  = theTime * (    7.12 + theEps);
    theEps  = theTime * (  -39.05 + theEps);
    theEps  = theTime * ( -249.67 + theEps);
    theEps  = theTime * (  -51.38 + theEps);
    theEps  = theTime * ( 1999.25 + theEps);
    theEps  = theTime * (   -1.55 + theEps);
    theEps  = theTime * (-4680.93 + theEps);
    theEps += 21.448;
    theEps  = 26. + theEps / 60.;
    theEps  = 23. + theEps / 60.;
    theEps /= Hmelib.DEGPERRAD;

    return theEps;
  }


  /**
   * Set the B1950 rectangular coordinates.
   *
   * <p>This sets the position stored from the given B1950/FK4 coordinates.
   * Conversion to J2000 for internal storage is performed.
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be set.
   * @param aTriplet
   *   Three floating point numbers containing the x, y and z coordinates.
   *   These should normally be in Gm. */

  protected void SetB1950(int aIndex, double aTriplet[])
  {
    double t1[] = {0.,0.,0.};
    int    i;

    B19502J2000(1, aTriplet, t1);
    for (i = 0; i < 3; i++) itsR[3*aIndex+i] = t1[i];
  }


  /**
   * Set the mean equinox-of-date ecliptic rectangular coordinates.
   *
   * <p>This sets the position stored from the given mean ecliptic coordinates.
   * Conversion to J2000 for internal storage is performed.
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be set.
   * @param aEquinox
   *   The equinox to which the given coordinates refer, also the time for
   *   which the obliquity of the ecliptic needs to be calculated.
   * @param aTriplet
   *   Three floating point numbers containing the x, y and z coordinates.
   *   These should normally be in Gm. */

  protected void SetEcl(int aIndex, Times aEquinox, double aTriplet[])
  {
    double t1[] = {0.,0.,0.};
    double t2[] = {0.,0.,0.};
    int    i;

    Ecl2Mean(1, aEquinox, aTriplet, t1);
    Mean2J2000(1, aEquinox, t1, t2);
    for (i = 0; i < 3; i++) itsR[3*aIndex+i] = t2[i];
  }


  /**
   * Set the galactic rectangular coordinates.
   *
   * <p>This sets one stored position from the given galactic coordinates.
   * Conversion to J2000 for internal storage is performed.
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be set.
   * @param aTriplet
   *   Three floating point numbers containing the x, y and z coordinates.
   *   These should normally be in Gm. */

  protected void SetGal(int aIndex, double aTriplet[])
  {
    double t1[] = {0.,0.,0.};
    double t2[] = {0.,0.,0.};
    int    i;

    Gal2B1950(1, aTriplet, t1);
    B19502J2000(1, t1, t2);
    for (i = 0; i < 3; i++) itsR[3*aIndex+i] = t2[i];
  }


  /**
   * Set the horizontal rectangular coordinates.
   *
   * <p>This sets the position stored from the given horizontal coordinates.
   * Conversion to J2000 for internal storage is performed.
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be set.
   * @param aStation
   *   The time and location of the observatory.  The station clock is also
   *   the date of the equinox.
   * @param aTriplet
   *   Three floating point numbers containing the x, y and z coordinates.
   *   These should normally be in Gm.  Note that the coordinates are
   *   left-handed and that azimuth counts from North, i.e. the x axis points
   *   to the North point and the y axis points to the East point on the
   *   horizon. */

  protected void SetHori(int aIndex, Station aStation, double aTriplet[])
  {
    double t1[] = {0.,0.,0.};
    double t2[] = {0.,0.,0.};
    int    i;

    Hori2Topo(1, aStation, aTriplet, t2);
    Topo2Mean(1, aStation, t2, t1);
    Mean2J2000(1, aStation, t1, t2);
    for (i = 0; i < 3; i++) itsR[3*aIndex+i] = t2[i];
  }


  /**
   * Set the J2000 rectangular coordinates.
   *
   * <p>This sets the position stored from the given J2000/FK5 coordinates.
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be set.
   * @param aTriplet
   *   Three floating point numbers containing the x, y and z coordinates.
   *   These should normally be in Gm. */

	protected void SetJ2000(int aIndex, double aTriplet[]) {
		int i;
		for (i = 0; i < 3; i++)
			itsR[3 * aIndex + i] = aTriplet[i];
	}


  /**
   * Set the mean equinox-of-date rectangular coordinates.
   *
   * <p>This sets the position stored from the given mean coordinates for the
   * equinox of date.  The date of the equinox also needs to be given.
   * Conversion to J2000 for internal storage is performed.
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be set.
   * @param aEquinox
   *   The equinox to which the given coordinates refer.
   * @param aTriplet
   *   Three floating point numbers containing the x, y and z coordinates.
   *   These should normally be in Gm. */

  static double t1SetMean[] = {0.,0.,0.};
  protected void SetMean(int aIndex, Times aEquinox, double aTriplet[])
  {
    
    int    i;

    Mean2J2000(1, aEquinox, aTriplet, t1SetMean);
    for (i = 0; i < 3; i++) itsR[3*aIndex+i] = t1SetMean[i];
  }


  /**
   * Set the topocentric HA/Dec rectangular coordinates.
   *
   * <p>This sets the position stored from the given topocentric coordinates.
   * Conversion to J2000 for internal storage is performed.
   *
   * @param aIndex
   *   Determines which of the itsNpos triplets is to be set.
   * @param aStation
   *   The time and location of the observatory.  The station clock is also
   *   the date of the equinox.
   * @param aTriplet
   *   Three floating point numbers containing the x, y and z coordinates.
   *   These should normally be in Gm.  Note that the coordinates are
   *   left-handed, i.e. the y axis points to the West point on the horizon. */

  
  static double t1SetTopo[] = {0.,0.,0.};
  static double t2SetTopo[] = {0.,0.,0.};
  protected void SetTopo(int aIndex, Station aStation, double aTriplet[])
  {
    
    
    int    i;

    Topo2Mean(1, aStation, aTriplet, t1SetTopo);
    Mean2J2000(1, aStation, t1SetTopo, t2SetTopo);
    for (i = 0; i < 3; i++) itsR[3*aIndex+i] = t2SetTopo[i];
  }


  /**
   * Convert HA/Dec to azimuth and elevation.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>The transform is
   *
   * <p><table>
   * <tr align="center">
   *   <td>(<br>(<br>(</td>
   *   <td>x"<br>y"<br>z"</td>
   *   <td>)<br>)<br>)</td>
   *   <td>=</td>
   *   <td>(<br>(<br>(</td>
   *   <td>-sin(&phi;)<br>0<br>cos(&phi;)</td>
   *   <td>0<br>-1<br>0</td>
   *   <td>cos(&phi;)<br>0<br>sin(&phi;)</td>
   *   <td>)<br>)<br>)</td>
   *   <td>(<br>(<br>(</td>
   *   <td>x'<br>y'<br>z'</td>
   *   <td>)<br>)<br>)</td>
   * </tr>
   * </table>
   *
   * <p>The matrix here includes an inversion of the x and y axes, which
   * amounts to a rotation by 180 degrees about the z axis.  This takes account
   * of the fact that azimuth counts from North while hour angle counts from
   * South.  Both count retrograde (clockwise).
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param aStation
   *   The location of the observatory.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */
  static double mat[] = {0.,0.,0.,0.};
  protected final void Topo2Hori(int aNpos, Station aStation,
    double inTriplets[], double outTriplets[])
  {
    
    double theLat;
    int i;

    theLat = aStation.GetLat();

    mat[0] = -Math.sin(theLat); mat[1] =  Math.cos(theLat);
    mat[2] =  Math.cos(theLat); mat[3] =  Math.sin(theLat);

    for(i = 0; i < aNpos; i++) {
      outTriplets[3*i]   =  inTriplets[3*i]   * mat[0]
                         +  inTriplets[3*i+2] * mat[1];
      outTriplets[3*i+1] = -inTriplets[3*i+1];
      outTriplets[3*i+2] =  inTriplets[3*i]   * mat[2]
                         +  inTriplets[3*i+2] * mat[3];
    }
  }


  /**
   * Convert topocentric HA/Dec to geocentric RA/Dec.
   *
   * <p>This method does not change the state, it merely operates on aNpos
   * given triplets to generate aNpos returned triplets.
   *
   * <p>The transform is
   *
   * <p><table>
   * <tr align="center">
   *   <td>(<br>(<br>(</td>
   *   <td>x<br>y<br>z</td>
   *   <td>)<br>)<br>)</td>
   *   <td>=</td>
   *   <td>(<br>(<br>(</td>
   *   <td>cos(LST)<br>sin(LST)<br>0</td>
   *   <td>sin(LST)<br>-cos(LST)<br>0</td>
   *   <td>0<br>0<br>1</td>
   *   <td>)<br>)<br>)</td>
   *   <td>(<br>(<br>(</td>
   *   <td>x' + itsX<br>y'<br>z' + itsZ</td>
   *   <td>)<br>)<br>)</td>
   * </tr>
   * </table>
   *
   * <p>The matrix here includes an inversion of the topocentric y' axis.
   * While the geocentric y axis points towards Orion, the topocentric y' axis
   * points to the West point on the horizon.  Therefore
   * RA&nbsp;=&nbsp;atan(y/x) counts direct (counter-clockwise), but
   * HA&nbsp;=&nbsp;atan(y'/x') counts retrograde (clockwise).
   *
   * @param aNpos
   *   So many triplets of xyz are given and to be returned.
   * @param aStation
   *   The time and location of the observatory.  There is an assumption that
   *   the given geocentric coordinates are mean RA/Dec for the equinox of the
   *   same date as the station clock shows.
   * @param inTriplets
   *   Array of 3*aNpos given numbers, each group of three forming an xyz
   *   position in Gm.
   * @param outTriplets
   *   Array of 3*aNpos returned numbers, each group of three forming an xyz
   *   position in Gm. */

  protected final void Topo2Mean(int aNpos, Station aStation,
    double inTriplets[], double outTriplets[])
  {
    double theLST;
    double mat[] = {0.,0.,0.,0.};
    double vec[] = {0.,0.,0.};
    int i;

    /* Vector from geocentre to station,
     * i.e. the geocentric position of the station.
     * Since we already rotated the x axis into the meridian,
     * this affects only x and z. */

    aStation.GetX0Z(vec);

    /* Right ascension is negated hour angle plus local sidereal time,
     * this rotation affects only x and y.
     * (The negated hour angle counts direct, hour angle counts retrograde.) */

    theLST = aStation.GetLST() * Math.PI / 12.;
    mat[0] =  Math.cos(theLST); mat[1] =  Math.sin(theLST);
    mat[2] =  Math.sin(theLST); mat[3] = -Math.cos(theLST);

    /* Process. */

    for(i = 0; i < aNpos; i++) {
      outTriplets[3*i]    = (vec[0] + inTriplets[3*i])   * mat[0]
                          +           inTriplets[3*i+1]  * mat[1];
      outTriplets[3*i+1]  = (vec[0] + inTriplets[3*i])   * mat[2]
                          +           inTriplets[3*i+1]  * mat[3];
      outTriplets[3*i+2]  =  vec[2] + inTriplets[3*i+2];
    }
  }

}
