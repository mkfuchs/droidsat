
package uk.me.chiandh.Sputnik;

import java.io.*;
import uk.me.chiandh.Lib.Hmelib;
import uk.me.chiandh.Lib.HmelibException;

/**
<p>The <code>Geometry</code> class comprises some methods that are purely
geometric and require no ephemeris.</p>

<p>Copyright: &copy; 2004 Horst Meyerdierks.</p>

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
<dt><strong>2004-10-13:</strong> hme</dt>
<dd>New class.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see uk.me.chiandh.Lib.Hmelib
 */

public final class Geometry
{

  /**
Serve the <code>planet/limb/sun</code> and
<code>moon/limb</code> commands.

<p>The command line must contain six numbers, i.e. three x,y pairs.
The output format is:</p>

<pre>
Centre point and radius:
    1516.021   750.512  491.078
</pre>

<p>We use the three points assuming they cover the observed limb well
and assuming that the second point is roughly halfway between the other
two.  Points 1+2 and 2+3 each define a segment vector of the circular
limb.  The verticals on their midpoints go through the centre and are
an unknown distance t12 and t23 from the segment midpoints.</p>

<p>The two verticals intersect in the centre.  Setting the two x0 equal
and also setting the two y0 equal, we get two equations in the two
unknown t12 and t23.  Both can be solved for t23 and the two can then
be equated.  This can be solved for t12:</p>

<p>
t12 = [(x1 - x3) / (y3 - y2) + (y3 - y1) / (x2 - x3)]
    / [(x1 - x2) / (x2 - x3) + (y1 - y2) / (y3 - y2)]
    / 2;
</p>

<p>With this the centre is</p>

<p>
x0 = (x1 + x2) / 2 + t12 (y2 - y1)
<br />
y0 = (y1 + y2) / 2 + t12 (x1 - x2)
</p>

@param aCommand
  The command line, including the parameters to be read.
   */

  public static final String LimbToCentre(String aCommand)
    throws HmelibException
  {
    String theString;
    double x1, x2, x3, y1, y2, y3, t, x0, y0, r1, r2, r3;

    /* Read the parameters for the command. */

    theString = Hmelib.Sstring(aCommand).trim();
    x1 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    y1 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    x2 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    y2 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    x3 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    y3 = Hmelib.Rfndm(theString);

    /* Calculate distance of centre from segment midpoint vertical,
     * then the centre. */

    t = ((x1 - x3) / (y3 - y2) + (y3 - y1) / (x2 - x3))
      / ((x1 - x2) / (x2 - x3) + (y1 - y2) / (y3 - y2))
      / 2.;

    x0 = (x1 + x2) / 2 + t * (y2 - y1);
    y0 = (y1 + y2) / 2 + t * (x1 - x2);

    /* Calculate the three points' distances from the centre. */

    r1 = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
    r2 = Math.sqrt((x2 - x0) * (x2 - x0) + (y2 - y0) * (y2 - y0));
    r3 = Math.sqrt((x3 - x0) * (x3 - x0) + (y3 - y0) * (y3 - y0));

    /* Write the results. */

    return ("\nCentre point and radius:\n"
      + Hmelib.Wfndm(12, 3, x0)
      + Hmelib.Wfndm(10, 3, y0)
      + Hmelib.Wfndm( 9, 3, (r1 + r2 + r3) / 3.)
      + "\n\n");
  }
}
