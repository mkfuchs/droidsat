
package uk.me.chiandh.Sputnik;

/**
 * <p>The <code>TimesAmbigDateException</code> is thrown when a given date
 * is ambiguous.</p>
 *
 * <p>Copyright: &copy; 2003 Horst Meyerdierks.</p>
 *
 * <p>This programme is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public Licence as
 * published by the Free Software Foundation; either version 2 of
 * the Licence, or (at your option) any later version.</p>
 *
 * <p>This programme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public Licence for more details.</p>
 *
 * <p>You should have received a copy of the GNU General Public Licence
 * along with this programme; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.</p>

<dl>
<dt><strong>2003-09-15:</strong> hme
<dd>Initial revision.
<dt><strong>2003-09-15:</strong> hme
<dd>Package review.
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk
 */

public final class TimesAmbigDateException extends TimesException {
  public TimesAmbigDateException()         {super();}
  public TimesAmbigDateException(String s) {super(s);}
}
