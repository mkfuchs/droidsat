
package uk.me.chiandh.Lib;

import java.text.*;

/**
<p><code>Hmelib</code> is a loose collection of methods for all sorts of
general little tasks, such as mathematics, string stuff, line i/o etc.  It
also collects some constants we need in a variety of classes, similar to
Java's <code>Math.E</code> and <code>Math.PI</code>, although this would
normally be the task of the common base class of the classes that need a
constant.</p>

<p>Copyright: &copy; 2002-2009 Horst Meyerdierks.

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
<dt><strong>2002-06-09:</strong> hme</dt>
<dd>Translated from C++ after Times class was finalised in the prospective
  version 2 of Sputnik.</dd>
<dt><strong>2002-06-15:</strong> hme</dt>
<dd>Add support for precision 0 to Wfndm.  Add Rstring() and Sstring().
  Add Spher().</dd>
<dt><strong>2002-06-17:</strong> hme</dt>
<dd>Add Wdms() and Wfexp().</dd>
<dt><strong>2002-06-22:</strong> hme</dt>
<dd>Add Rect().</dd>
<dt><strong>2002-06-22:</strong> hme</dt>
<dd>Add SpherAng().</dd>
<dt><strong>2002-06-23:</strong> hme</dt>
<dd>Add NormAngle0() and NormAngle180().</dd>
<dt><strong>2002-07-07:</strong> hme</dt>
<dd>Add support for precision 1 to Wfndm.</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Fix format for Rfndm so that commas are not taken as part of a number
  but as separators.  Add SpherDist().</dd>
<dt><strong>2002-07-14:</strong> hme</dt>
<dd>Consolidating documentation.  Also enhance Rrndm() to read
  mantissa/exponent numbers.</dd>
<dt><strong>2002-07-16:</strong> hme</dt>
<dd>RTime3 now throws an exception if the year or month are not integer.
  Before it would return their integer value instead.
  Also improved handling of mantissa/exponent numbers in Rfndm, in
  particular less chance of a crash due to invalid input.</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Change date format from Y/M/D- to Y-M-D-.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.
  Renamed from SputnikLib to Hmelib.</dd>
<dt><strong>2003-09-18:</strong> hme</dt>
<dd>Fix bug in WTime4 whereby it would always write the minute with on
  decimal (which was always .0 as it should be).</dd>
<dt><strong>2004-02-02:</strong> hme</dt>
<dd>Added WTime5.</dd>
<dt><strong>2004-02-03:</strong> hme</dt>
<dd>Added WTime6.</dd>
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>Ported to Sputnik3.  Changed package name to uk.me.chiandh.Lib.
  Changed the W methods to return a string instead of writing to a given
  stream.</dd>
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>Changed time-and-date format from date-time to dateTtime.  This affects
  RTime3, WTime3, WTime4.</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Change RTime3 to accept old date formats as well as the new one.<br />
  Version 3.0.8.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk
 */

public final class Hmelib
{

  /** Degrees per radian. */

  public static final double DEGPERRAD = 180. / Math.PI;


  /** Blank string used in right-justifying numeric fields. */

  final static private String blank = "                                ";


  /** A %02.0f format for formatting times and sexagesimal angles. */

  final static private DecimalFormat zi2 = new DecimalFormat("00");


  /** A %04.0f format for formatting the year in dates. */

  final static private DecimalFormat zi4 = new DecimalFormat("0000");


  /** A %04.1f format for formatting the last sexagesimal component. */

  final static private DecimalFormat zf4d1 = new DecimalFormat("00.0");


  /** A %n.0f format for formatting integer output. */

  final static private DecimalFormat f1d0 = new DecimalFormat("0");


  /** A %n.1f format for formatting with one decimal digit. */

  final static private DecimalFormat f1d1 = new DecimalFormat("0.0");


  /** A %n.3f format for formatting with three decimal digits. */

  final static private DecimalFormat f1d3 = new DecimalFormat("0.000");


  /** A %n.6f format for formatting with six decimal digits. */

  final static private DecimalFormat f1d6 = new DecimalFormat("0.000000");


  /** A %n.9f format for formatting with nine decimal digits. */

  final static private DecimalFormat f1d9 = new DecimalFormat("0.000000000");


  /** A %n.3g format for formatting with three decimal digits and exponent. */

  final static private DecimalFormat fexp = new DecimalFormat("0.000E00");


  /**
   * A %f format for parsing a floating point number.
   *
   * <p>Specifying a hash means that a comma (,) is not recognised as
   * throusand separator but taken to be the end of the numeric field.
   * Note that an "E" is also taken to be the end of the numeric field, so
   * 123E4 cannot be read properly.</p> */

  final static private DecimalFormat form  = new DecimalFormat("#");


  /**
   * Convert a number into a sexagesimal triplet.
   *
   * <p>The given number (e.g. an angle in degrees or a time in hours) is split
   * into a triplet of numbers (degrees, arcmin, arcsec or hours, minutes,
   * seconds).</p>
   *
   * @param aTime
   *   Time or angle in hours or degrees.
   * @param aTriplet
   *   The triplet of hours, minutes, seconds or &deg;, ', ". */

  static public void deg2dms(double aTime, double aTriplet[])
  {
    double theT;

    theT = aTime;
    if (0. > aTime) {
      aTriplet[0] = Math.ceil(theT); theT -= aTriplet[0]; theT *= 60.;
      aTriplet[1] = Math.ceil(theT); theT -= aTriplet[1]; theT *= 60.;
      aTriplet[2] = theT;
    }
    else{
      aTriplet[0] = Math.floor(theT); theT -= aTriplet[0]; theT *= 60.;
      aTriplet[1] = Math.floor(theT); theT -= aTriplet[1]; theT *= 60.;
      aTriplet[2] = theT;
    }
  }


  /**
   * Normalise an angle around zero.
   *
   * <p>This normalises an angle in case it is outside the interval
   * [-&pi;,+&pi;].  The returned value represents the same angle but is
   * within this interval.</p>
   *
   * @param aAngle
   *   The angle before normalisation. */

  static public double NormAngle0(double aAngle)
  {
    double theAngle;
    theAngle = aAngle;
    while (theAngle <= -Math.PI) theAngle += 2. * Math.PI;
    while (theAngle  >  Math.PI) theAngle -= 2. * Math.PI;
    return theAngle;
  }


  /**
   * Normalise an angle around 180&#176;.
   *
   * <p>This normalises an angle in case it is outside the interval
   * [0,2&pi;].  The returned value represents the same angle but is within
   * this interval.</p>
   *
   * @param aAngle
   *   The angle before normalisation. */

  static public double NormAngle180(double aAngle)
  {
    double theAngle;
    theAngle = NormAngle0(aAngle);
    if (0. > theAngle) theAngle += 2. * Math.PI;
    return theAngle;
  }


  /**
   * Convert spherical to orthogonal coordinates.
   *
   * <p>Take the given a,b,r coordinates and convert them to x,y,z where a is
   * the azimuth angle in the x-y plane, b the elevation over the x-y plane,
   * and r the distance from the origin.  a and b are in radian, r in the same
   * units as x, y and z.</p>
   *
   * @param aSpher
   *   Triplet of spherical coordinates a, b, r.
   * @param aRect
   *   Triplet of rectangular coordinates x, y, z. */

  static public void Rect(double aSpher[], double aRect[])
  {
    aRect[0] = aSpher[2] * Math.cos(aSpher[0]) * Math.cos(aSpher[1]);
    aRect[1] = aSpher[2] * Math.sin(aSpher[0]) * Math.cos(aSpher[1]);
    aRect[2] = aSpher[2] * Math.sin(aSpher[1]);
  }


  /**
   * Read a floating point number.
   *
   * <p>Given a string this trims off any leading and trailing white space and
   * reads a floating point number from the start of the string.  If the
   * parsing fails an exception is thrown.  The given
   * string can be a plain number like <code>1234.5678</code> or one that is
   * split into mantissa and exponent like <code>1.2345678E3</code>.  Mantissa
   * or exponent can have a minus sign or indeed a plus sign in front of them.
   * (This would be a trivial statement if this were written in C, but in Java
   * this takes some coding to achieve.)</p>
   *
   * @param aString
   *   The string to read from. */

  static public double Rfndm(String aString)
    throws HmelibInvalidNumException
  {
    ParsePosition where = new ParsePosition(0);
    String theString;
    Number theNumber;
    double m = 0., e = 0.;
    int    i;

    /* Trim off white space and a leading plus sign. */

    theString = aString.trim();
    if (theString.startsWith("+")) theString = theString.substring(1);

    /* Parse the number up to the next invalid character or end of string.
     * Valid characters would be -.0123456789 or something like that. */

    theNumber = form.parse(theString, where);
    if (null == theNumber)
      throw new HmelibInvalidNumException("invalid number");
    m = theNumber.doubleValue();

    /* Check if the next character is an e or an E, in which case we have a
     * decimal magnitude behind it.  Read it and make use of it. */

    i = where.getIndex();
    if (i >= theString.length()) {
      return m;
    }
    else if ('e' == theString.charAt(i) ||
             'E' == theString.charAt(i)) {
      i++;
      if (i >= theString.length()) {
        return m;
      }
      else {
        if ('+' == theString.charAt(i)) i++;
        where.setIndex(i);
	theNumber = form.parse(theString, where);
	if (null == theNumber) {
	    return m;
	}
	else {
	    e = theNumber.doubleValue();
	    return m * Math.pow(10., e);
	}
      }
    }
    else {
      return m;
    }
  }


  /**
   * Read a string.
   *
   * <p>Given a string this trims off any leading and trailing white space and
   * reads a string from the start.  The string read is usually a single word.
   * But if the string begins with a double quote (") then the string read is
   * all words between this and the matching closing quote.  The quotes are
   * sripped off.  This routine will not recognise escaped quotes as part of
   * the string, but as end of the string.</p>
   *
   * @param aString
   *   The string to read from. */

  static public String Rstring(String aString)
  {
    String theString;
    int    theIndex;

    /* Trim white space. */

    theString = aString.trim();

    /* If quoted string. */

    if (theString.startsWith("\"")) {

      /* Strip opening quote, find closing one. */

      theString = theString.substring(1);
      theIndex  = theString.indexOf('\"');

      /* If empty string. */

      if (0 == theIndex) {
	theString = "";
      }

      /* Else if genuine string. */

      else if (0 < theIndex) {
	theString = theString.substring(0, theIndex);
      }

      /* Else (no closing quote), do nothing, the String is fine. */

    }

    /* Else (unquoted string). */

    else {

      /* Find space character. */

      theIndex  = theString.indexOf(' ');

      /* If there is one. */

      if (0 < theIndex) {
	theString = theString.substring(0, theIndex);
      }

      /* Else (no space character), do nothing, the String is fine. */
    }
    return theString;
  }


  /**
Read date as year, month and day, time as hours, minutes and seconds.

<p>Given a string of the form <code>2002-12-13T12:34:56.7</code>, this
routine returns the six numbers.  The third number in the string can also
be floating point.  In that case its fractional part is added to the hours
in order that all three date numbers can be returned as integers.  All
numbers can be negative (inlcuding the month and day,
which are given behind a -
character).  Each negative sign affects only the one number.  The given
string will be trimmed for leading and trailing blanks before reading.
But the separators between the numbers must be precisely as shown,
i.e. single characters - or /, - or /, T or t or -, :, :.
If this is not the case an exception is thrown.</p>

<p>Older formats are accepted. Sputnik 1.9 used
<code>2002/12/13-12:34:56.7</code> and Sputnik 2.1 used
<code>2002-12-13-12:34:56.7</code>. Lower-case is also accepted, i.e.
<code>2002-12-13t12:34:56.7</code>.</p>

@param aString
  The string to read from.
@param date
  The triplet of integers that are year, month and day.
@param time
  The triplet of floating point numbers that are hours, minutes and
  seconds.
   */

  final static public void RTime3(String aString, int date[], double time[])
    throws HmelibInvalidDateException
  {
    ParsePosition where = new ParsePosition(0);
    double theDayd, theDayi;
    String theString;
    Number theNumber;

    theString = aString.trim();

    theNumber = form.parse(theString, where);
    if (null == theNumber)
      throw new HmelibInvalidDateException("invalid date");
    date[0] = theNumber.intValue();
    if (date[0] != theNumber.doubleValue())
      throw new HmelibInvalidDateException("invalid date");
    if  (!(theString.substring(where.getIndex())).startsWith("-")
      && !(theString.substring(where.getIndex())).startsWith("/"))
      throw new HmelibInvalidDateException("invalid date");

    where.setIndex(1 + where.getIndex());
    theNumber = form.parse(theString, where);
    if (null == theNumber)
      throw new HmelibInvalidDateException("invalid date");
    date[1] = theNumber.intValue();
    if (date[1] != theNumber.doubleValue())
      throw new HmelibInvalidDateException("invalid date");
    if  (!(theString.substring(where.getIndex())).startsWith("-")
      && !(theString.substring(where.getIndex())).startsWith("/"))
      throw new HmelibInvalidDateException("invalid date");

    where.setIndex(1 + where.getIndex());
    theNumber = form.parse(theString, where);
    if (null == theNumber)
      throw new HmelibInvalidDateException("invalid date");
    theDayd = theNumber.doubleValue();
    if  (!(theString.substring(where.getIndex())).startsWith("T")
      && !(theString.substring(where.getIndex())).startsWith("t")
      && !(theString.substring(where.getIndex())).startsWith("-"))
      throw new HmelibInvalidDateException("invalid date");

    where.setIndex(1 + where.getIndex());
    theNumber = form.parse(theString, where);
    if (null == theNumber)
      throw new HmelibInvalidDateException("invalid date");
    time[0] = theNumber.doubleValue();
    if (!(theString.substring(where.getIndex())).startsWith(":"))
      throw new HmelibInvalidDateException("invalid date");

    where.setIndex(1 + where.getIndex());
    theNumber = form.parse(theString, where);
    if (null == theNumber)
      throw new HmelibInvalidDateException("invalid date");
    time[1] = theNumber.doubleValue();
    if (!(theString.substring(where.getIndex())).startsWith(":"))
      throw new HmelibInvalidDateException("invalid date");

    where.setIndex(1 + where.getIndex());
    theNumber = form.parse(theString, where);
    if (null == theNumber)
      throw new HmelibInvalidDateException("invalid date");
    time[2] = theNumber.doubleValue();

    theDayi  = Math.floor(theDayd);
    theDayd -= theDayi; date[2] = (int)theDayi;
    time[0] += 24. * theDayd;
  }


  /**
   * Convert orthogonal to spherical coordinates.
   *
   * <p>Take the given x,y,z coordinates and convert them to a,b,r where a is
   * the azimuth angle in the x-y plane, b the elevation over the x-y plane,
   * and r the distance from the origin.  a and b are in radian, r in the same
   * units as x, y and z.</p>
   *
   * @param aRect
   *   Triplet of rectangular coordinates x, y, z.
   * @param aSpher
   *   Triplet of spherical coordinates a, b, r. */

  static public void Spher(double aRect[], double aSpher[])
  {
    double xx, yy, zz;

    if (aRect[0] == 0. && aRect[1] == 0. && aRect[2] == 0.) {
      aSpher[0] = 0; aSpher[1] = 0; aSpher[2] = 0; return;
    }

    aSpher[2] = Math.sqrt(aRect[0] * aRect[0] + aRect[1] * aRect[1]
		        + aRect[2] * aRect[2]);

    xx = aRect[0] / aSpher[2];
    yy = aRect[1] / aSpher[2];
    zz = aRect[2] / aSpher[2];
    aSpher[1] = Math.asin(zz);
    aSpher[0] = Math.atan2(yy, xx);
    if (aSpher[0] < 0.) aSpher[0] += 2. * Math.PI;

    return;
  }


  /**
   * Calculate a spherical angle.
   *
   * <p>The three given xyz triplets define a spherical triangle on the unit
   * sphere.  The returned value is the angle at the second corner.  To
   * calculate a postition angle, give the North Pole {0,0,1} as the first
   * position, the bright component or reference positions as second and the
   * faint component as third.  To calculate the parallactic angle give the
   * zenith {0,0,1} as first, the position of interest Rect({A,h,1}) as
   * second and the North Celestial Pole
   * Rect({0,&phi;,1}) as third.</p>
   *
   * @param Triplets
   *   Nine numbers x1, y1, z1, x2, y2, z2, x3, y3, z3.  Each group of three
   *   is a vector that defines one corner of the spherical triangle. */

  static public double SpherAng(double Triplets[])
  {
    double mat[] = new double[9];
    double x1, x2, x3, y1, y2, y3, z1, z2, z3, t1;
    int    i;

    /* Get unit vectors. */

    t1 = Math.sqrt(Triplets[0] * Triplets[0]
                 + Triplets[1] * Triplets[1] + Triplets[2] * Triplets[2]);
    x1 = Triplets[0] / t1; y1 = Triplets[1] / t1; z1 = Triplets[2] / t1;
    t1 = Math.sqrt(Triplets[3] * Triplets[3]
                 + Triplets[4] * Triplets[4] + Triplets[5] * Triplets[5]);
    x2 = Triplets[3] / t1; y2 = Triplets[4] / t1; z2 = Triplets[5] / t1;
    t1 = Math.sqrt(Triplets[6] * Triplets[6]
                 + Triplets[7] * Triplets[7] + Triplets[8] * Triplets[8]);
    x3 = Triplets[6] / t1; y3 = Triplets[7] / t1; z3 = Triplets[8] / t1;

    /* New z axis to point behind our head. */

    mat[6] = -x2;
    mat[7] = -y2;
    mat[8] = -z2;

    /* New x axis is projection of north pole vertical to line of sight. */

    t1 = x1 * x2 + y1 * y2 + z1 * z2;
    mat[0] = x1 - t1 * x2;
    mat[1] = y1 - t1 * y2;
    mat[2] = z1 - t1 * z2;
    t1 = Math.sqrt(mat[0] * mat[0] + mat[1] * mat[1] + mat[2] * mat[2]);
    for (i = 0; i < 3; i++) mat[i] /= t1;

    /* New y axis is z cross x. */

    mat[3] = mat[7] * mat[2] - mat[1] * mat[8];
    mat[4] = mat[0] * mat[8] - mat[6] * mat[2];
    mat[5] = mat[6] * mat[1] - mat[0] * mat[7];

    /* Transform the third position with the matrix defined by the first two.
     * Then the position angle is the longitude in the transformed system. */

    x1 = mat[0] * x3 + mat[1] * y3 + mat[2] * z3;
    y1 = mat[3] * x3 + mat[4] * y3 + mat[5] * z3;
    z1 = mat[6] * x3 + mat[7] * y3 + mat[8] * z3;

    x3 = Math.atan2(y1, x1);
    if (x3 < 0.) x3 += 2. * Math.PI;

    return x3;
  }


  /**
   * Calculate a spherical distance.
   *
   * <p>The given xyz triplets define two directions.  The returned value is
   * the angle between the two vectors.  In the first instance the distance is
   * calculated as the arccos of the dot product of the two unit vectors.  If
   * this results in a distance less than ~1&#176; or more than ~179&#176;,
   * then the distance is recalculated as the arcsin of the cross product.</p>
   *
   * @param aTriplet1
   *   The first vector.
   * @param aTriplet2
   *   The second vector. */

  static public double SpherDist(double aTriplet1[], double aTriplet2[])
  {
    double ax, ay, az, bx, by, bz, ra, rb;
    double dot, cross, cx, cy, cz;

    /* Unit vectors. */

    ax = aTriplet1[0]; ay = aTriplet1[1]; az = aTriplet1[2];
    bx = aTriplet2[0]; by = aTriplet2[1]; bz = aTriplet2[2];
    ra = Math.sqrt(ax * ax + ay * ay + az * az);
    rb = Math.sqrt(bx * bx + by * by + bz * bz);
    ax /= ra; ay /= ra; az /= ra;
    bx /= rb; by /= rb; bz /= rb;

    /* Dot product. */

    dot = ax * bx + ay * by + az * bz;
    if (0.9998 > Math.abs(dot)) {
      return Math.acos(dot);
    }
    else if (0. < dot) {
      cx = ay * bz - az * by;
      cy = az * bx - ax * bz;
      cz = ax * by - ay * bx;
      cross = Math.sqrt(cx * cx + cy * cy + cz * cz);
      return Math.asin(cross);
    }
    else {
      cx = ay * bz - az * by;
      cy = az * bx - ax * bz;
      cz = ax * by - ay * bx;
      cross = Math.sqrt(cx * cx + cy * cy + cz * cz);
      return Math.PI - Math.asin(cross);
    }
  }


  /**
Skip a string.

<p>Given a string this behaves like {@link #Rstring Rstring},
but returns the remainder of the given string instead of the string
read.</p>

@param aString
  The string to read from.
   */

  static public String Sstring(String aString)
  {
    String theString;
    int    theIndex;

    /* Trim white space. */

    theString = aString.trim();

    /* If quoted string. */

    if (theString.startsWith("\"")) {

      /* Strip opening quote, find closing one. */

      theString = theString.substring(1);
      theIndex  = theString.indexOf('\"');

      /* If no closing quote, the remainder string is empty. */

      if (-1 == theIndex) {
	theString = "";
      }

      /* Else, the remainder starts after it. */

      else {
	theString = theString.substring(theIndex + 1);
      }
    }

    /* Else (unquoted string). */

    else {

      /* Find space character. */

      theIndex  = theString.indexOf(' ');

      /* If there is none, the remainder is empty. */

      if (-1 == theIndex) {
	theString = "";
      }

      /* Else, the remainder starts with it. */

      else {
	theString = theString.substring(theIndex);
      }
    }
    return theString;
  }


  /**
Return angle as degrees, arc minutes and arc seconds.

<p>Format the given triplet of in the form <code>"&nbsp;12:34:56"</code>
or <code>"-12:34:56"</code>.  Note the leading blank for positive angles.</p>

@param degree
  The angle in degrees.
   */

  final static public String Wdms(double degree)
  {
    String theString = "";
    double theDMS[] = new double[3];

    deg2dms(Math.abs(degree), theDMS);
    if (0. > degree) {theString = "-";}
    else             {theString = " ";}

    theDMS[2] = Math.floor(0.5 + theDMS[2]);
    if (60. == theDMS[2]) {
      theDMS[2] -= 60.; theDMS[1] += 1.;
      if (60. == theDMS[1]) {theDMS[1] -= 60.; theDMS[0] += 1.;}
    }
    theString = theString + zi2.format(theDMS[0]) + ":"
              + zi2.format(theDMS[1]) + ":"
              + zi2.format(theDMS[2]);

    return theString;
  }


  /**
Return a number in <code>1.234E12</code> format.

<p>Format the given floating point number into a string of lenth 8 with
4 significant digits, 3 of them behind the decimal dot.</p>

@param aNum
  The number to format and write.
   */

  final static public String Wfexp(double aNum)
  {
    return fexp.format(aNum);
  }


  /**
Return a number in a fixed-width field.

<p>Format the given floating point number into a string of fixed lenth
with the number on its right (blank-padded on the left).  The given field
width can have any positive integer value.  If it is too small to fit the
number a longer string is generated.  The precision can currently be given
as 0, 1, 3, 6 or 9 digits behind the decimal point.  If another precision
is given, 3 is used anyway.  As an example the number
<code>-1234.567</code> with a width of 14 and a precision of 6 should come
out as <code>"&nbsp;&nbsp;-1234.567000"</code>.</p>

@param aWidth
  The number of characters to write.
@param aPrec
  The number of digits behind the decimal point.
@param aNum
  The number to format and write.
   */

  final static public String Wfndm(int aWidth, int aPrec, double aNum)
  {
    String theOut;
    int theLen;

    /* Format the number into a string, which necessarily is left justified. */

    switch(aPrec) {
    case 9:  theOut = f1d9.format(aNum); break;
    case 6:  theOut = f1d6.format(aNum); break;
    case 3:  theOut = f1d3.format(aNum); break;
    case 1:  theOut = f1d1.format(aNum); break;
    case 0:  theOut = f1d0.format(aNum); break;
    default: theOut = f1d3.format(aNum); break;
    }

    /* If the string is narrower than the requested field length, print a few
     * space characters and then the number.  Else print the number. */

    theLen = theOut.length();
    if (theLen < aWidth) theOut = blank.substring(theLen,aWidth) + theOut;

    return theOut;
  }


  /**
Return time as hours and minutes.

<p>Round and format the given triplet of hour, minute and second into
hours and minutes of the form <code>12:35</code> where the second number
takes into account the third.  The rounding is also modified to prevent
the second number to reach the value 60 on output.</p>

@param hour
  First sexagesimal unit, hour or &#176;.
@param min
  Second sexagesimal unit, minute or '.
@param sec
  Third sexagesimal unit, second or ".
   */

  final static public String WTime1(double hour, double min, double sec)
  {
    double h, m;

    h = hour; m = Math.floor(0.5 + min + sec / 60.);
    if (60. == m) {m -= 60.; h += 1.;}
    return (zi2.format(h) + ":" + zi2.format(m));
  }


  /**
Return time as hours, minutes and seconds.

<p>Format the given triplet of hour, minute and second in the form
<code>12:34:56.7</code>.</p>

@param hour
  First sexagesimal unit, hour or &#176;.
@param min
  Second sexagesimal unit, minute or '.
@param sec
  Third sexagesimal unit, second or ".
   */

  final static public String WTime2(double hour, double min, double sec)
  {
    double h, m, s;

    h = hour; m = min; s = 0.1 * Math.floor(0.5 + 10. * sec);
    if (60. == s) {
      s -= 60.; m += 1.;
      if (60. == m) {m -= 60.; h += 1.;}
    }
    return (zi2.format(h) + ":" + zi2.format(m) + ":" + zf4d1.format(s));
  }


  /**
Return date as year, month and day, time as hours, minutes and seconds.

<p>Format the given sextuplet of year, month, day, hour, minute and
second in the form <code>2002-12-13T12:34:56.7</code>.</p>

@param year
  Year.
@param month
  Month.
@param day
  Day.
@param hour
  Hour.
@param min
  Minute.
@param sec
  Second.
   */

  final static public String WTime3(double year, double month, double day,
    double hour, double min, double sec)
  {
    double h, m, s;

    h = hour; m = min; s = 0.1 * Math.floor(0.5 + 10. * sec);
    if (60. == s) {
      s -= 60.; m += 1.;
      if (60. == m) {m -= 60.; h += 1.;}
    }
    return (zi4.format(year) + "-" + zi2.format(month) + "-" +
	    zi2.format(day)  + "T" + zi2.format(h)     + ":" +
	    zi2.format(m)    + ":" + zf4d1.format(s));
  }


  /**
Return date as year, month and day, time as hours and minutes.

<p>Format the given sextuplet of year, month, day, hour, minute and
second in the form <code>2002-12-13T12:35</code> where the fifth number
takes into account the sixth.  The rounding is also modified to prevent
the fifth number to reach the value 60 on output.</p>

@param year
  Year.
@param month
  Month.
@param day
  Day.
@param hour
  Hour.
@param min
  Minute.
@param sec
  Second.
   */

  final static public String WTime4(double year, double month, double day,
    double hour, double min, double sec)
  {
    double h, m;

    h = hour; m = Math.floor(0.5 + min + sec / 60.);
    if (60. == m) {m -= 60.; h += 1.;}
    return(zi4.format(year) + "-" + zi2.format(month) + "-" +
	   zi2.format(day)  + "T" + zi2.format(h)     + ":" +
	   zi2.format(m));
  }


  /**
Return date as year, month and day.

<p>Format the given triplet of year, month and day
in the form <code>2002-12-13</code>.</p>

@param year
  Year.
@param month
  Month.
@param day
  Day.
   */

  final static public String WTime5(double year, double month, double day)
  {
    return (zi4.format(year)
	    + "-" + zi2.format(month)
	    + "-" + zi2.format(day));
  }


  /**
Return time as hours, minutes and seconds.

<p>Format the given triplet of hour, minute and second in the form
<code>12:34:56</code>.</p>

@param hour
  First sexagesimal unit, hour or &#176;.
@param min
  Second sexagesimal unit, minute or '.
@param sec
  Third sexagesimal unit, second or ".
   */

  final static public String WTime6(double hour, double min, double sec)
  {
    double h, m, s;

    h = hour; m = min; s = 0.1 * Math.floor(0.5 + 10. * sec);
    if (60. == s) {
      s -= 60.; m += 1.;
      if (60. == m) {m -= 60.; h += 1.;}
    }
    return (zi2.format(h) + ":" + zi2.format(m) + ":" + zi2.format(s));
  }


  /**
   * This class cannot be instantiated. */

  private Hmelib() {}

}
