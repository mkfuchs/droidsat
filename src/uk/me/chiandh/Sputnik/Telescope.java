
package uk.me.chiandh.Sputnik;

import java.io.*;
import uk.me.chiandh.Lib.Hmelib;
import uk.me.chiandh.Lib.HmelibException;
import uk.me.chiandh.Lib.SDP4Exception;

/**
<p>The <code>Telescope</code> class extends the {@link Station
Station} class which is an extension of the {@link Times
Times} class.  To the clock and the location on the Earth's surface we
here add a {@link NamedObject NamedObject}, i.e. the point on
the sky that the telescope is pointed at.  We also add a {@link
Sun Sun} to keep up-to-date information about the Sun.</p>

<p>Copyright: &copy; 2002-2006 Horst Meyerdierks.</p>

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
<dd>New class.</dd>
<dt><strong>2002-06-22:</strong> hme</dt>
<dd>Move CommandShow() from Station to this class so it serves station/show
  and object/show.  Add CommandSetObject().</dd>
<dt><strong>2002-06-23:</strong> hme</dt>
<dd>Add itsSun field.  Move CommandTime() from Times to this class so it
  serves station/{ut|tt|jd|sys} commands and updates itsSun after changing
  the Station clock.</dd>
<dt><strong>2002-07-02:</strong> hme</dt>
<dd>Pass itsSun to the NamedObject method that prints the object to a file
  stream.  It uses this to calculate radial velocities.</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Support comet/show command.</dd>
<dt><strong>2002-07-13:</strong> hme</dt>
<dd>Consolidate documentation.</dd>
<dt><strong>2003-03-02:</strong> hme</dt>
<dd>Support planet/show/mercury and /jupiter commands.</dd>
<dt><strong>2003-03-09:</strong> hme</dt>
<dd>Support satellite/show command.</dd>
<dt><strong>2003-04-21:</strong> hme</dt>
<dd>Support planet/show/mars command.</dd>
<dt><strong>2003-06-01:</strong> hme</dt>
<dd>Support moon/show command.</dd>
<dt><strong>2003-06-01:</strong> hme</dt>
<dd>Support planet/show/saturn command.</dd>
<dt><strong>2003-06-07:</strong> hme</dt>
<dd>Support planet/show/venus, uranus and neptune commands.</dd>
<dt><strong>2003-06-08:</strong> hme</dt>
<dd>Support planet/show/pluto and asteroid/show commands.</dd>
<dt><strong>2003-06-14:</strong> hme</dt>
<dd>Support h0001 command.</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Use Telescope instead of Station plus Sun in many interfaces.</dd>
<dt><strong>2003-09-15:</strong> hme</dt>
<dd>Support satellite/all command.</dd>
<dt><strong>2003-09-16:</strong> hme</dt>
<dd>Package review.</dd>
<dt><strong>2003-09-17:</strong> hme</dt>
<dd>Support object/rise command.</dd>
<dt><strong>2003-09-18:</strong> hme</dt>
<dd>Support moon/rise command.  Delegate the output for any rise/set
  command to common protected method.  Report circumpolarity separately for
  rise and set.</dd>
<dt><strong>2003-09-19:</strong> hme</dt>
<dd>Support station/twilight command.</dd>
<dt><strong>2003-11-10:</strong> hme</dt>
<dd>Fix bug in iridium/flare which did not copy the Station state from this
  to theScope.</dd>
<dt><strong>2004-02-01:</strong> hme</dt>
<dd>Support satellite/pass command.
  Also change iridium/flare to separate flares by blank lines.</dd>
<dt><strong>2004-02-02:</strong> hme</dt>
<dd>Support h0002 command.</dd>
<dt><strong>2004-02-03:</strong> hme</dt>
<dd>Support h0003 and h0004 commands.</dd>
<dt><strong>2004-03-14:</strong> hme</dt>
<dd>Change iridium/flare and h0004 to cope with simultaneous flares.</dd>
<dt><strong>2004-06-11:</strong> hme</dt>
<dd>Add VenusTransit method to serve planet/au/venus command.</dd>
<dt><strong>2004-07-28:</strong> hme</dt>
<dd>Finalise VenusTransit method to serve planet/au/venus command.</dd>
<dt><strong>2004-08-09:</strong> hme</dt>
<dd>Version 2.1.1 and re-instate command string as parameter to Venus
  transit method, it ignores the parameter.</dd>
<dt><strong>2004-10-13:</strong> hme</dt>
<dd>Support planet/coord/sun command.</dd>
<dt><strong>2005-12-27:</strong> hme</dt>
<dd>Support moon/coord command.</dd>
<dt><strong>2005-12-27:</strong> hme</dt>
<dd>Fix use of GetPhysics methods in VSOP87 subclasses, which now need
  aTelescope.</dd>
<dt><strong>2005-12-28:</strong> hme</dt>
<dd>Version 2.1.3.</dd>
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>Port to Sputnik3.
  Change Heliograph(), Selenograph(), CommandDawn(), CommandRise(),
  CommandShow(), ShowRiseSet(), VenusTransit()
  to return a string rather than write to stdout.
  Change CommandSatPass(), CommandIridium()
  to return a string instead of writing to stdout.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see NamedObject
@see Sun
@see uk.me.chiandh.Lib.Hmelib
 */

public final class Telescope extends Station
{

  /**
   * The object under observation. */

  protected NamedObject itsObject;


  /**
   * The Sun.
   *
   * <p>This instance of the {@link Sun Sun} class keeps the
   * information about the Sun at the time of the station clock.</p> */

  protected Sun itsSun;


  /**
   * Add to the time.
   *
   * @param aTimeStep
   *   The time increment to apply. */

  public void Add(double aTimeStep) {
    super.Add(aTimeStep);
    Update();
    return;
  }


  /**
Serve the <code>planet/coord/sun</code> command.

<p>The six numbers on the command line are the centre of the Sun,
the radius of the Sun, the position angle of the detector vertical
in degrees, and the position to be converted to heliographic.
The output format is:</p>

<pre>
Observatory: Edinburgh
   East long.   -3.217000 deg
   Latitude     55.950000 deg
   Altitude            50 m

  UT: 2004-10-13T09:22:00.0 (JD  2453291.890278)
  TT: 2004-10-13T09:23:06.7 (JDE 2453291.891050)
  Ep: 2004.782727036
             GST 10:51:06.8 = 162.778421 deg
             LST 10:38:29.9 = 159.624421 deg

Pixel and heliographic coordinates:
    1316.000   324.000   271.5  -13.2
</pre>

<p>The pixel coordinates are first translated to put the Sun's centre in
the origin, then normalised to solar radius 1.  Then the coordinates are
rotated for the position angles of the detector vertical and of the
solar axis of rotation.  Then they are rotated for the inclination of
the solar axis.  Then the latitude and longitude offset from the central
meridian can be calculated and the central meridian added.</p>
<p>
x<sub>1</sub> = (x - x<sub>0</sub>) / r<br />
y<sub>1</sub> = (y - y<sub>0</sub>) / r
</p><p>
x<sub>2</sub> = x<sub>1</sub> cos(q - PA) - y<sub>1</sub> sin(q - PA)<br />
y<sub>2</sub> = x<sub>1</sub> sin(q - PA) + y<sub>1</sub> cos(q - PA)<br />
z<sub>2</sub> = sqrt(1. - x<sub>2</sub><sup>2</sup> - y<sub>2</sub><sup>2</sup>)
</p><p>
x<sub>3</sub> =  x<sub>2</sub><br />
y<sub>3</sub> =  y<sub>2</sub> cos(i) + z<sub>2</sub> sin(i)
</p><p>
b = asin(y<sub>3</sub>)<br />
l = asin(x<sub>3</sub>/cos(b)) + CM
</p>

@param aCommand
  The command line, including the parameters to be read.
 */

  public String Heliograph(String aCommand)
    throws HmelibException
  {
    String theString;
    double theOctet[] = new double[8];
    double x0, y0, r, q, x, y, l, b;
    double x1, y1, x2, y2, z2;

    /* Read the parameters for the command. */

    theString = Hmelib.Sstring(aCommand).trim();
    x0 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    y0 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    r  = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    q  = Hmelib.Rfndm(theString);
    q /= Hmelib.DEGPERRAD;
    theString = Hmelib.Sstring(theString);
    x  = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    y  = Hmelib.Rfndm(theString);

    /* Get solar ephemeris. */

    itsSun.GetPhysics(theOctet, this);

    /* Subtract centre of Sun and normalise to radius. */

    x1 = (x - x0) / r;
    y1 = (y - y0) / r;

    /* Rotate for PA of vertical and PA of axis. */

    q -= theOctet[6];
    x2 = x1 * Math.cos(q) - y1 * Math.sin(q);
    y2 = x1 * Math.sin(q) + y1 * Math.cos(q);
    z2 = Math.sqrt(1. - x2 * x2 - y2 * y2);

    /* Rotate for i. */

    x1 =  x2;
    y1 =  y2 * Math.cos(theOctet[5]) + z2 * Math.sin(theOctet[5]);

    /* Work out l,b. */

    b = Math.asin(y1);
    l = Math.asin(x1/Math.cos(b)) + theOctet[7];
    b *= Hmelib.DEGPERRAD;
    l *= Hmelib.DEGPERRAD;
    if (0.   > l) {l += 360.;}
    if (360. < l) {l -= 360.;}

    /* Write result. */

    return (Show()
      + "Pixel and heliographic coordinates:\n"
      + Hmelib.Wfndm(12, 3, x)
      + Hmelib.Wfndm(10, 3, y)
      + Hmelib.Wfndm( 8, 1, l)
      + Hmelib.Wfndm( 6, 1, b) + "\n\n");
  }


  /**
Serve the <code>moon/coord</code> command.

<p>The six numbers on the command line are the centre of the Moon,
the radius of the Moon, the position angle of the detector vertical
in degrees, and the position to be converted to selenographic.
The output format is:</p>

<pre>
Observatory: Edinburgh
   East long.   -3.217000 deg
   Latitude     55.950000 deg
   Altitude            50 m

  UT: 2004-10-13T09:22:00.0 (JD  2453291.890278)
  TT: 2004-10-13T09:23:06.7 (JDE 2453291.891050)
  Ep: 2004.782727036
             GST 10:51:06.8 = 162.778421 deg
             LST 10:38:29.9 = 159.624421 deg

Pixel and selenographic coordinates:
    1316.000   324.000   -88.5  -13.2
</pre>

<p>The pixel coordinates are first translated to put the Moon's centre in
the origin, then normalised to lunar radius 1.  Then the coordinates are
rotated for the position angles of the detector vertical and of the
lunar axis of rotation.  Then they are rotated for the inclination of
the lunar axis.  Then the latitude and longitude offset from the central
meridian can be calculated and the central meridian added.</p>
<p>
x<sub>1</sub> = (x - x<sub>0</sub>) / r<br />
y<sub>1</sub> = (y - y<sub>0</sub>) / r
</p><p>
x<sub>2</sub> = x<sub>1</sub> cos(q - PA) - y<sub>1</sub> sin(q - PA)<br />
y<sub>2</sub> = x<sub>1</sub> sin(q - PA) + y<sub>1</sub> cos(q - PA)<br />
z<sub>2</sub> = sqrt(1. - x<sub>2</sub><sup>2</sup> - y<sub>2</sub><sup>2</sup>)
</p><p>
x<sub>3</sub> =  x<sub>2</sub><br />
y<sub>3</sub> =  y<sub>2</sub> cos(i) + z<sub>2</sub> sin(i)
</p><p>
b = asin(y<sub>3</sub>)<br />
l = asin(x<sub>3</sub>/cos(b)) + CM
</p>

@param aCommand
  The command line, including the parameters to be read.
 */

  public String Selenograph(String aCommand)
    throws HmelibException
  {
    Moon   theMoon;
    String theString;
    double theOctet[] = new double[8];
    double x0, y0, r, q, x, y, l, b;
    double x1, y1, x2, y2, z2;

    /* Read the parameters for the command. */

    theString = Hmelib.Sstring(aCommand).trim();
    x0 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    y0 = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    r  = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    q  = Hmelib.Rfndm(theString);
    q /= Hmelib.DEGPERRAD;
    theString = Hmelib.Sstring(theString);
    x  = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString);
    y  = Hmelib.Rfndm(theString);

    /* Get lunar ephemeris. */

    theMoon = new Moon();
    theMoon.Init();
    theMoon.Update(this);
    theMoon.GetPhysics(theOctet, this);

    /* Subtract centre of Moon and normalise to radius. */

    x1 = (x - x0) / r;
    y1 = (y - y0) / r;

    /* Rotate for PA of vertical and PA of axis. */

    q -= theOctet[6];
    x2 = x1 * Math.cos(q) - y1 * Math.sin(q);
    y2 = x1 * Math.sin(q) + y1 * Math.cos(q);
    z2 = Math.sqrt(1. - x2 * x2 - y2 * y2);

    /* Rotate for i. */

    x1 =  x2;
    y1 =  y2 * Math.cos(theOctet[5]) + z2 * Math.sin(theOctet[5]);

    /* Work out l,b. */

    b = Math.asin(y1);
    l = Math.asin(x1/Math.cos(b)) + theOctet[7];
    b *= Hmelib.DEGPERRAD;
    l *= Hmelib.DEGPERRAD;
    if (-180. > l) {l += 360.;}
    if ( 180. < l) {l -= 360.;}

    /* Write result. */

    return (Show()
      + "Pixel and selenographic coordinates:\n"
      + Hmelib.Wfndm(12, 3, x)
      + Hmelib.Wfndm(10, 3, y)
      + Hmelib.Wfndm( 8, 1, l)
      + Hmelib.Wfndm( 6, 1, b) + "\n\n");
  }


  /**
Serve the <code>satellite/pass</code> command.

<p>The search step is given in seconds, 60&nbsp;s is probably short
enough in most cases.  The output format is:</p>

<pre>
Observatory: Edinburgh
   East long.   -3.217000 deg
   Latitude     55.950000 deg
   Altitude            50 m

  UT: 2003-09-15T17:00:00.0 (JD  2452898.208333)
  TT: 2003-09-15T17:01:06.0 (JDE 2452898.209098)
  Ep: 2003.704884593
             GST 16:36:59.2 = 249.246849 deg
             LST 16:24:07.2 = 246.029849 deg

Twilight passes of satellite ISS (ZARYA)

         UT               A      h       r
                         deg    deg      km
---------------------  ------  -----  --------
2004-01-26-18:16:00.0   228.7    2.7    1924.2 
2004-01-26-18:17:00.0   222.6    7.2    1544.4 
2004-01-26-18:18:00.0   212.2   12.9    1197.5 
2004-01-26-18:19:00.0   193.7   19.8     923.6 
2004-01-26-18:20:00.0   163.0   24.2     803.1 

2004-01-26-19:51:00.0   255.9    2.4    1956.5 
2004-01-26-19:52:00.0   252.0    7.1    1554.0 
</pre>

@param aCommand
  The command line, including the parameters to be read.
 */

  public String CommandSatPass(String aCommand)
    throws TelescopeInvIntervException, TimesAmbigDateException,
           HmelibException, SDP4Exception, IOException
  {
    String    theOutput = "";
    int       theDate[] = new int[3];
    double    theTime[] = new double[3];
    Telescope theScope  = new Telescope();
    Satellite theSatellite;
    String    theString, theFile, theName;
    double    theStart, theStep, theEnd, theJD;
    int       theNstep, inPass, wasInPass;
    int       i;

    /* Initialise the Telescope for the time loop.  Then copy its state
     * - in particular the station - from this. */

    theScope.Init();
    theScope.Copy(this);

    /* Read the parameters for the command: TLE file, satellite name,
     * interval, end time */

    theString = aCommand.substring(15);
    theFile   = Hmelib.Rstring(theString);
    theString = Hmelib.Sstring(theString);
    theName   = Hmelib.Rstring(theString);
    theString = Hmelib.Sstring(theString);
    theStep   = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString).trim();
    Hmelib.RTime3(theString, theDate, theTime);
    theScope.SetUT(theDate[0], theDate[1], theDate[2],
	           theTime[0], theTime[1], theTime[2]);

    /* Work out the time stepping in JD. */

    theStart = GetJD();
    theEnd   = theScope.GetJD();
    theStep /= 86400.;
    theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

    if (theEnd <= theStart)
      throw new TelescopeInvIntervException("end before start");

    /* Read the satellite from file. */

    theSatellite = new Satellite();
    theSatellite.Init();
    theSatellite.ReadByName(theFile, theName);

    /* Write table header */

    theOutput = Show()
      + "Twilight passes of satellite " + theName + "\n\n"
      + "         UT               A      h       r\n"
      + "                         deg    deg      km\n"
      + "---------------------  ------  -----  --------\n";

    /* Loop through time */

    wasInPass = 0; inPass = 0;
    for (i = 0; i < theNstep ; i++) {

      /* Calculate time for next step. */

      theJD = theStart + (double) i * theStep;
      theScope.SetJD(theJD);

      /* Let the satellite update itself to the new time, decide whether
       * it is in pass, and write a line. */

      theString = theSatellite.ShowPass(theScope);
      if (null == theString) {
	inPass = 0;
      }
      else {
	inPass = 1;
	theOutput = theOutput + theString;
      }

      /* If we have changed from in pass to not in pass, write a blank
       * line. */

      if (0 != wasInPass && 0 == inPass) {theOutput = theOutput + "\n";}

      wasInPass = inPass;
    }

    /* If at the end we are in pass, write an extra blank line. */

    if (0 != inPass) {theOutput = theOutput + "\n";}

    return theOutput;
  }


  /**
Serve the <code>iridium/flare</code> command.

<p>The search step is given in seconds, because when set to a single
minute flares will be missed.  An interval of 5&nbsp;s is probably short
enough.  The output format is:</p>

<pre>
Observatory: Edinburgh
   East long.   -3.217000 deg
   Latitude     55.950000 deg
   Altitude            50 m

  UT: 2003-09-15T17:00:00.0 (JD  2452898.208333)
  TT: 2003-09-15T17:01:06.0 (JDE 2452898.209098)
  Ep: 2003.704884593
             GST 16:36:59.2 = 249.246849 deg
             LST 16:24:07.2 = 246.029849 deg

Iridium flares from file: /home/hme/lib/sats/iridium.txt

         UT                A        h        r     I   Name
                          deg      deg      km    deg
---------------------  --------  -------  ------  ---  ----------------------
2003-09-15T20:33:35.0   358.331   21.681    1672  1.8  IRIDIUM 83
2003-09-15T20:33:40.0   358.585   20.989    1702  1.4  IRIDIUM 83
2003-09-15T20:33:45.0   358.829   20.316    1733  1.0  IRIDIUM 83
2003-09-15T20:33:50.0   359.063   19.660    1763  0.9  IRIDIUM 83
2003-09-15T20:33:55.0   359.288   19.022    1794  0.9  IRIDIUM 83
2003-09-15T20:34:00.0   359.504   18.400    1825  1.2  IRIDIUM 83
2003-09-15T20:34:05.0   359.713   17.793    1856  1.5  IRIDIUM 83
2003-09-15T20:34:10.0   359.914   17.201    1887  1.8  IRIDIUM 83

2003-09-15T20:43:30.0   357.704   15.800    1964  2.0  IRIDIUM 16
2003-09-15T20:43:35.0   357.971   15.264    1995  1.9  IRIDIUM 16
2003-09-15T20:43:40.0   358.228   14.739    2026  1.9  IRIDIUM 16
2003-09-15T20:43:45.0   358.478   14.225    2058  2.0  IRIDIUM 16

2003-09-15T21:30:05.0    32.998   16.608    1919  1.8  IRIDIUM 70
2003-09-15T21:30:10.0    34.003   16.901    1903  1.7  IRIDIUM 70
2003-09-15T21:30:15.0    35.028   17.189    1888  1.9  IRIDIUM 70
</pre>

@param aCommand
  The command line, including the parameters to be read.
 */

  public String CommandIridium(String aCommand)
    throws TelescopeInvIntervException, TelescopeEmptyFileException,
	   TimesAmbigDateException,
	   HmelibException, SDP4Exception, IOException
  {
    String         theOutput = "";
    int            theDate[] = new int[3];
    double         theTime[] = new double[3];
    Telescope      theScope  = new Telescope();
    Satellite[]    theSatellite;
    BufferedReader theFile;
    String         theString, theFileName;
    double         theStart, theStep, theEnd, theJD;
    int            theNstep, theNsat;
    int            inFlare[], wasInFlare[];
    int            i, j;

    /* Initialise the Telescope for the time loop.  Then copy its state
     * - in particular the station - from this. */

    theScope.Init();
    theScope.Copy(this);

    /* Read the parameters for the command: TLE file, interval, end time */

    theString   = aCommand.substring(14);
    theFileName = Hmelib.Rstring(theString);
    theString   = Hmelib.Sstring(theString);
    theStep   = Hmelib.Rfndm(theString);
    theString = Hmelib.Sstring(theString).trim();
    Hmelib.RTime3(theString, theDate, theTime);
    theScope.SetUT(theDate[0], theDate[1], theDate[2],
	           theTime[0], theTime[1], theTime[2]);

    /* Work out the time stepping in JD. */

    theStart = GetJD();
    theEnd   = theScope.GetJD();
    theStep /= 86400.;
    theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

    if (theEnd <= theStart)
      throw new TelescopeInvIntervException("end before start");

    /* Open the TLE file, count the lines, close and reopen. */

    theFile = new BufferedReader(new FileReader(theFileName));
    for (theNsat = 0; ; theNsat++) {
      if ((theFile.readLine()) == null) break;
    }
    theNsat = (int) Math.floor((double) theNsat / 3.);
    theFile.close();
    if (1 > theNsat)
      throw new TelescopeEmptyFileException("file has no satellites");

    /* Obtain an array of Satellite instances, initialise each.
     * Read each Satellite TLE from file using NoradNext. */

    theSatellite = new Satellite[theNsat];
    wasInFlare   = new int[theNsat];
    inFlare      = new int[theNsat];
    theFile = new BufferedReader(new FileReader(theFileName));
    for (i = 0; i < theNsat ; i++) {
      theSatellite[i] = new Satellite();
      theSatellite[i].Init();
      theSatellite[i].ReadNext(theFile);
      wasInFlare[i] = 0; inFlare[i] = 0;
    }
    theFile.close();

    /* Write table header */

    theOutput = Show()
      + "Iridium flares from file: " + theFileName + "\n\n"
      + "         UT                A        h        r     I   Name\n"
      + "                          deg      deg      km    deg\n"
      + "---------------------  --------  -------  ------  ---"
      + "  ----------------------\n";

    /* Loop through time */

    for (j = 0; j < theNstep ; j++) {

      /* Calculate time for next step. */

      theJD = theStart + (double) j * theStep;
      theScope.SetJD(theJD);

      /* Let each satellite update itself to the new time, decide whether
       * it is in flare, and write a line. */

      for (i = 0; i < theNsat ; i++) {

        theString = theSatellite[i].ShowFlare(theScope);
	if (null == theString) {
	  inFlare[i] = 0;
	}
	else {
	  inFlare[i] = 1;
	  theOutput = theOutput + theString;
	}

	/* If we have changed from in flare to not in flare, write a blank
	 * line. */

	if (0 != wasInFlare[i] && 0 == inFlare[i]) {
          theOutput = theOutput + "\n";
	}

	wasInFlare[i] = inFlare[i];
      }
    }

    /* At the end write an extra blank line. */

    theOutput = theOutput + "\n";

    return theOutput;
  }


  /**
Serve the <code>station/twilight</code> command.

<p>Serve the command <code>station/twilight</code>.  This writes to the
terminal the 8 times relevant to twilight.  The format is</p>

<pre>
Twilight:

  Start of astronomical dawn: 2003-05-04T00:39
  Start of nautical dawn:     2003-05-04T02:35
  Start of civil dawn:        2003-05-04T03:38
  Rise  of the Sun:           2003-05-04T04:24
  Set   of the Sun:           2003-05-04T19:57
  End   of civil dawn:        2003-05-04T20:43
  End   of nautical dawn:     2003-05-04T21:47
  End   of astronomical dawn: none (circumpolar)
</pre>

<p>The times are written in chronological order.  All times are in
the future (as referred to the time in this instance of Telescope), but
at most somewhat more than a day in the future.</p>
 */

  public final String CommandDawn()
  {
    String  theOutput  = "";
    Times[] sixTimes   = new Times[8];
    double  theDate[]  = new double[3];
    double  theTime[]  = new double[3];
    boolean haveDawn[] = new boolean[8];
    int     first, i, j;

    theOutput = "\nTwilight:\n";

    /* Obtain the eight times.
     * If it were midnight now these would be in order.
     * If we are somewhere during dawn, day or dusk some of the former times
     * have been pushed to the next day.  We should print those last. */

    haveDawn[0] = true;
    try {
      sixTimes[0] = itsSun.NextRiseSet(this, NamedObject.RISEASTRO, true);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[0] = false;
    }
    haveDawn[1] = true;
    try {
      sixTimes[1] = itsSun.NextRiseSet(this, NamedObject.RISENAUTI, true);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[1] = false;
    }
    haveDawn[2] = true;
    try {
      sixTimes[2] = itsSun.NextRiseSet(this, NamedObject.RISECIVIL, true);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[2] = false;
    }
    haveDawn[3] = true;
    try {
      sixTimes[3] = itsSun.NextRiseSet(this, NamedObject.RISESUN, true);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[3] = false;
    }
    haveDawn[4] = true;
    try {
      sixTimes[4] = itsSun.NextRiseSet(this, NamedObject.RISESUN, false);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[4] = false;
    }
    haveDawn[5] = true;
    try {
      sixTimes[5] = itsSun.NextRiseSet(this, NamedObject.RISECIVIL, false);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[5] = false;
    }
    haveDawn[6] = true;
    try {
      sixTimes[6] = itsSun.NextRiseSet(this, NamedObject.RISENAUTI, false);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[6] = false;
    }
    haveDawn[7] = true;
    try {
      sixTimes[7] = itsSun.NextRiseSet(this, NamedObject.RISEASTRO, false);
    }
    catch (NamedObjectCircPolException e) {
      haveDawn[7] = false;
    }

    /* Figure out which of the 6 times is the first, i.e. is the smallest
     * of the existing ones. */

    /* Find the first time that exists. */

    for (first = 0; first < 7; first++) {if (haveDawn[first]) break;}

    /* If none of the times exist. */

    if (7 < first) {

      /* Write the eight entries, all as non-existent. */

      theOutput = theOutput
        + "\n  Start of astronomical dawn: none (circumpolar)"
        + "\n  Start of nautical dawn:     none (circumpolar)"
        + "\n  Start of civil dawn:        none (circumpolar)"
        + "\n  Rise  of the Sun:           none (circumpolar)"
        + "\n  Set   of the Sun:           none (circumpolar)"
        + "\n  End   of civil dawn:        none (circumpolar)"
        + "\n  End   of nautical dawn:     none (circumpolar)"
	+ "\n  End   of astronomical dawn: none (circumpolar)";

    }

    /* Else (one or more times exist). */

    else {

      /* Find the smallest of the existing times. */

      for (i = first + 1; i < 8; i++) {
	if (haveDawn[i])
	  if (0. > sixTimes[i].Sub(sixTimes[first])) first = i;
      }

      /* Write the six times, starting with the earliest. */

      j = first;
      for (i = 0; i < 8 ; i++) {

	switch (j) {
	case 0:
	  theOutput = theOutput + "\n  Start of astronomical dawn: ";
	  break;
	case 1:
	  theOutput = theOutput + "\n  Start of nautical dawn:     ";
	  break;
	case 2:
	  theOutput = theOutput + "\n  Start of civil dawn:        ";
	  break;
	case 3:
          theOutput = theOutput + "\n  Rise  of the Sun:           ";
	  break;
	case 4:
          theOutput = theOutput + "\n  Set   of the Sun:           ";
	  break;
	case 5:
	  theOutput = theOutput + "\n  End   of civil dawn:        ";
	  break;
	case 6:
	  theOutput = theOutput + "\n  End   of nautical dawn:     ";
	  break;
	case 7:
	  theOutput = theOutput + "\n  End   of astronomical dawn: ";
	  break;
	default:
	  break;
	}

	if (haveDawn[j]) {
	  sixTimes[j].GetDate(theDate);
	  sixTimes[j].GetUThms(theTime);
	  theOutput = theOutput
            + Hmelib.WTime4(theDate[0], theDate[1], theDate[2],
			    theTime[0], theTime[1], theTime[2]);
	}
	else {
	  theOutput = theOutput + "none (circumpolar)";
	}

	j++; if (7 < j) j -= 8;
      }
    }

    theOutput = theOutput + "\n\n";

    return theOutput;
  }


  /**
Serve the <code>whatever/rise</code> commands.

<p>Serve the commands
<code>object/rise</code>,
<code>planet/rise/sun</code>,
<code>planet/rise/&lt;planet&gt;</code>,
<code>moon/rise</code>,
<code>comet/rise</code>, <code>asteroid/rise</code>.</p>

<p>See {@link #ShowRiseSet ShowRiseSet} for more information.</p>

@param aCommand
  The command string including any parameters to know what to show.
 */

  public final String CommandRise(String aCommand)
    throws CometException, TimesException, HmelibException, IOException
  {
    String    theString, theFile, theName;
    Comet     theComet;
    Moon      theMoon;
    Mercury   theMercury;
    Venus     theVenus;
    Mars      theMars;
    Jupiter   theJupiter;
    Saturn    theSaturn;
    Uranus    theUranus;
    Neptune   theNeptune;
    Pluto     thePluto;

    if (aCommand.startsWith("object/rise")) {
      return ShowRiseSet(itsObject, NamedObject.RISE);
    }

    else if (aCommand.startsWith("planet/rise/sun")) {
      return ShowRiseSet(itsSun, NamedObject.RISESUN);
    }

    else if (aCommand.startsWith("planet/rise/mercury")) {
      theMercury = new Mercury();
      theMercury.Init();
      theMercury.Update(this);
      return ShowRiseSet(theMercury, NamedObject.RISE);
    }
    else if (aCommand.startsWith("planet/rise/venus")) {
      theVenus = new Venus();
      theVenus.Init();
      theVenus.Update(this);
      return ShowRiseSet(theVenus, NamedObject.RISE);
    }
    else if (aCommand.startsWith("planet/rise/mars")) {
      theMars = new Mars();
      theMars.Init();
      theMars.Update(this);
      return ShowRiseSet(theMars, NamedObject.RISE);
    }
    else if (aCommand.startsWith("planet/rise/jupiter")) {
      theJupiter = new Jupiter();
      theJupiter.Init();
      theJupiter.Update(this);
      return ShowRiseSet(theJupiter, NamedObject.RISE);
    }
    else if (aCommand.startsWith("planet/rise/saturn")) {
      theSaturn = new Saturn();
      theSaturn.Init();
      theSaturn.Update(this);
      return ShowRiseSet(theSaturn, NamedObject.RISE);
    }
    else if (aCommand.startsWith("planet/rise/uranus")) {
      theUranus = new Uranus();
      theUranus.Init();
      theUranus.Update(this);
      return ShowRiseSet(theUranus, NamedObject.RISE);
    }
    else if (aCommand.startsWith("planet/rise/neptune")) {
      theNeptune = new Neptune();
      theNeptune.Init();
      theNeptune.Update(this);
      return ShowRiseSet(theNeptune, NamedObject.RISE);
    }
    else if (aCommand.startsWith("planet/rise/pluto")) {
      thePluto = new Pluto();
      thePluto.Init();
      thePluto.Update(this);
      return ShowRiseSet(thePluto, NamedObject.RISE);
    }

    else if (aCommand.startsWith("moon/rise")) {
      theMoon = new Moon();
      theMoon.Init();
      theMoon.Update(this);
      return ShowRiseSet(theMoon, NamedObject.RISESUN);
    }

    else if (aCommand.startsWith("comet/rise") ||
             aCommand.startsWith("asteroid/rise")) {
      theString = aCommand.substring(11);
      theFile   = Hmelib.Rstring(theString);
      theString = Hmelib.Sstring(theString);
      theName   = Hmelib.Rstring(theString);
      theComet = new Comet();
      theComet.Init();
      theComet.ReadByName(theFile, theName);
      theComet.Update(this);
      return ShowRiseSet(theComet, NamedObject.RISE);
    }

    return "";
  }


  /**
   * Serve the <code>object/*</code> commands that set the position.
   *
   * @param aCommand
   *   The command line, including the parameters to be read. */

  public final void CommandSetObject(String aCommand)
    throws HmelibException
  {
    itsObject.CommandSet(aCommand, this);
    return;
  }


  /**
Serve various <code>whatever/show</code> commands.

<p>Serve the commands
<code>station/show</code>,
<code>object/show</code>,
<code>planet/show/sun</code>,
<code>planet/show/&lt;planet&gt;</code>,
<code>moon/show</code>,
<code>comet/show</code>, <code>asteroid/show</code>,
<code>satellite/show</code>,
<code>satellite/all</code>.</p>

@param aCommand
  The command string including any parameters to know what to show.
 */

  public final String CommandShow(String aCommand)
    throws CometException, TimesException, HmelibException,
	   SDP4Exception, IOException
  {
    String    theString, theFile, theName;
    Comet     theComet;
    Satellite theSatellite;
    Moon      theMoon;
    Mercury   theMercury;
    Venus     theVenus;
    Mars      theMars;
    Jupiter   theJupiter;
    Saturn    theSaturn;
    Uranus    theUranus;
    Neptune   theNeptune;
    Pluto     thePluto;

    if      (aCommand.startsWith("station/show")) {
      return Show();
    }

    else if (aCommand.startsWith("object/show")) {
      return itsObject.Show(this);
    }

    else if (aCommand.startsWith("planet/show/sun")) {
      return itsSun.Show(this);
    }

    else if (aCommand.startsWith("planet/show/mercury")) {
      theMercury = new Mercury();
      theMercury.Init();
      theMercury.Update(this);
      return theMercury.Show(this);
    }
    else if (aCommand.startsWith("planet/show/venus")) {
      theVenus = new Venus();
      theVenus.Init();
      theVenus.Update(this);
      return theVenus.Show(this);
    }
    else if (aCommand.startsWith("planet/show/mars")) {
      theMars = new Mars();
      theMars.Init();
      theMars.Update(this);
      return theMars.Show(this);
    }
    else if (aCommand.startsWith("planet/show/jupiter")) {
      theJupiter = new Jupiter();
      theJupiter.Init();
      theJupiter.Update(this);
      return theJupiter.Show(this);
    }
    else if (aCommand.startsWith("planet/show/saturn")) {
      theSaturn = new Saturn();
      theSaturn.Init();
      theSaturn.Update(this);
      return theSaturn.Show(this);
    }
    else if (aCommand.startsWith("planet/show/uranus")) {
      theUranus = new Uranus();
      theUranus.Init();
      theUranus.Update(this);
      return theUranus.Show(this);
    }
    else if (aCommand.startsWith("planet/show/neptune")) {
      theNeptune = new Neptune();
      theNeptune.Init();
      theNeptune.Update(this);
      return theNeptune.Show(this);
    }
    else if (aCommand.startsWith("planet/show/pluto")) {
      thePluto = new Pluto();
      thePluto.Init();
      thePluto.Update(this);
      return thePluto.Show(this);
    }

    else if (aCommand.startsWith("moon/show")) {
      theMoon = new Moon();
      theMoon.Init();
      theMoon.Update(this);
      return theMoon.Show(this);
    }

    else if (aCommand.startsWith("comet/show") ||
             aCommand.startsWith("asteroid/show")) {
      theString = aCommand.substring(11);
      theFile   = Hmelib.Rstring(theString);
      theString = Hmelib.Sstring(theString);
      theName   = Hmelib.Rstring(theString);
      theComet = new Comet();
      theComet.Init();
      theComet.ReadByName(theFile, theName);
      theComet.Update(this);
      return theComet.Show(this);
    }

    else if (aCommand.startsWith("satellite/show")) {
      theString = aCommand.substring(15);
      theFile   = Hmelib.Rstring(theString);
      theString = Hmelib.Sstring(theString);
      theName   = Hmelib.Rstring(theString);
      theSatellite = new Satellite();
      theSatellite.Init();
      theSatellite.ReadByName(theFile, theName);
      theSatellite.Update(this);
      return theSatellite.Show(this);
    }
    else if (aCommand.startsWith("satellite/all")) {
      theString = aCommand.substring(14);
      theFile   = Hmelib.Rstring(theString);
      theSatellite = new Satellite();
      theSatellite.Init();
      return theSatellite.ShowAll(theFile, this);
    }

    return "";
  }


  /**
   * Serve the <code>station/{ut|tt|jd|sys}</code> commands.
   *
   * <p>This also updates the Telescope's instance of the Sun class to show the
   * position of the Sun for the new time.</p>
   *
   * @param aCommand
   *   The command line, including the parameters to be read. */

  public void CommandTime(String aCommand)
    throws TelescopeInvCommException, TimesAmbigDateException, HmelibException
  {
    int    theDate[] = new int[3];
    double theTime[] = new double[3];

    if      (aCommand.startsWith("station/ut ")) {
      Hmelib.RTime3(aCommand.substring(11), theDate, theTime);
      SetUT(theDate[0], theDate[1], theDate[2],
	    theTime[0], theTime[1], theTime[2]);
    }
    else if (aCommand.startsWith("station/tt ")) {
      Hmelib.RTime3(aCommand.substring(11), theDate, theTime);
      SetTT(theDate[0], theDate[1], theDate[2],
	    theTime[0], theTime[1], theTime[2]);
    }
    else if (aCommand.startsWith("station/jd ")) {
      SetJD(Hmelib.Rfndm(aCommand.substring(11)) - 2450000.);
    }
    else if (aCommand.startsWith("station/sys")) {
      SetUTSystem();
    }
    else {
      throw new TelescopeInvCommException("unrecognised command");
    }

  }


  /**
   * Copy the state of a Telescope instance from another.
   *
   * <p>Invoke this method for the new instance of Telescope and pass as
   * argument an existing Telescope instance from which to copy the state.
   * The new instance must have been initialised with Init() before making
   * this call.</p>
   *
   * @param aTelescope
   *   The time, location, object and Sun to be copied into this instance. */

  public void Copy(Telescope aTelescope) {
    super.Copy(aTelescope);
    itsObject.Copy(aTelescope.itsObject);
    Update();
    return;
  }


  /**
   * Serve <code>h0004</code> command.
   *
   * <p>This writes to a file a fragment of XHTML stating when Iridium flares
   * occur during the next 24 hour period (from midday).  It uses the system
   * clock to determine the nearest midday, and it uses the file
   * <code>./data/stations.dat</code> to read the observatory
   * "Royal Observatory Edinburgh" from and the file
   * <code>./data/iridium.txt</code> to read the satellites from.
   * It writes the result to the file <code>./iriday.html</code></p>
   *
   * <p>This also writes an equivalent WML file (for WAP phones) to
   * <code>./iriday.wml</code>.</p>
   *
   * @param aCommand
   *   The command string including any parameters to know what to show.*/

  public final void h0004(String aCommand)
    throws TelescopeEmptyFileException,
           StationException, SDP4Exception, HmelibException, IOException
  {
    BufferedReader theFile;
    PrintStream theXhtml, theWml;
    Telescope   theScope;
    Satellite[] theSatellite;
    double    theStart, theStep, theEnd, theJD;
    double    theDate[]    = new double[3];
    double    startPos[][], startTime[][];
    double    peakPos[][],  peakTime[][];
    double    endPos[][],   endTime[][];
    double    peakAngle[];
    double    theAngle;
    int       inPass[], wasInPass[];
    int       theNstep, theNsat;
    int       i, j;

    /* Read the station from file. */

    ReadByName("data/stations.dat", "Royal Observatory Edinburgh");

    /* Set the time to the nearest midday UT, starting from system time. */

    SetUTSystem();
    SetJD(Math.floor(0.5 + GetJD()));
    GetDate(theDate);

    /* Initialise the Telescope for the time loop.  Then copy its state
     * - in particular the station - from this. */

    theScope = new Telescope();
    theScope.Init();
    theScope.Copy(this);

    /* Work out the time stepping in JD. */

    theStart = GetJD();
    theEnd   = theStart + 1.;
    theStep  = 1. / 86400.;
    theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

    /* Open the TLE file, count the lines, close and reopen. */

    theFile = new BufferedReader(new FileReader("data/iridium.txt"));
    for (theNsat = 0; ; theNsat++) {
      if ((theFile.readLine()) == null) break;
    }
    theNsat = (int) Math.floor((double) theNsat / 3.);
    theFile.close();
    if (1 > theNsat)
      throw new TelescopeEmptyFileException("file has no satellites");

    /* Obtain an array of Satellite instances, initialise each.
     * Read each Satellite TLE from file using NoradNext. */

    theSatellite = new Satellite[theNsat];
    peakAngle    = new double[theNsat];
    inPass       = new int[theNsat];
    wasInPass    = new int[theNsat];
    startTime = new double[theNsat][3];
    peakTime  = new double[theNsat][3];
    endTime   = new double[theNsat][3];
    startPos  = new double[theNsat][3];
    peakPos   = new double[theNsat][3];
    endPos    = new double[theNsat][3];
    theFile = new BufferedReader(new FileReader("data/iridium.txt"));
    for (i = 0; i < theNsat ; i++) {
      theSatellite[i] = new Satellite();
      theSatellite[i].Init();
      theSatellite[i].ReadNext(theFile);
    }
    theFile.close();

    /* Open the output files. */

    theXhtml = new PrintStream(new FileOutputStream("iriday.html"));
    theWml   = new PrintStream(new FileOutputStream("iriday.wml"));

    /* Some fixed XHTML, observatory and date, table header. */

    theXhtml.print("<h2>Iridium tonight</h2>\n\n"
      + "<p>\nThese are the flare times of Iridium satellites\n"
      + "over the location\n"
      + "\"" + itsName + "\"\n"
      + "during the 24 hour period following midday Universal Time of\n");
    theXhtml.print(Hmelib.WTime5(theDate[0], theDate[1], theDate[2]));
    theXhtml.print(", irrespective of daylight.\n</p>\n");

    theWml.print("<?xml version=\"1.0\"?>\n"
      + "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\"\n"
      + "  \"http://www.wapforum.org/DTD/wml_1.1.xml\">\n"
      + "<wml>\n"
      + "<template>\n"
      + "  <do type='prev' label='Back'>\n"
      + "    <prev/>\n"
      + "  </do>\n"
      + "</template>\n"
      + "<card>\n"
      + "<p>\n"
      + "Iridium flares for " + itsName + ", night of ");
    theWml.print(Hmelib.WTime5(theDate[0], theDate[1], theDate[2]));
    theWml.print(".\n</p>\n");

    theXhtml.print("<p></p>\n"
      + "<table border=\"1\" summary=\"Iridium flares\">\n"
      + "<col /><col /><col /><col /><col /><col />"
      + "<col /><col /><col /><col /><col />\n"
      + "<tr><th scope=\"col\">name</th>"
      + "<th scope=\"col\" colspan=\"3\">start</th>"
      + "<th scope=\"col\" colspan=\"4\">peak</th>"
      + "<th scope=\"col\" colspan=\"3\">end</th></tr>\n");
    theWml.print("<p>\nUT A h angle\n</p>\n");

    /* Loop through time. */

    for (j = 0; j < theNstep; j++) {

      /* Calculate JD. */

      theJD = theStart + (double) j * theStep;
      theScope.SetJD(theJD);

      /* Loop through the satellites. */

      for (i = 0; i < theNsat; i++) {

        /* Update the satellite, check it is up, sunlit and flaring. */

	theAngle = theSatellite[i].TestFlare(theScope);
	if (2. >= theAngle) {inPass[i] = 1;} else {inPass[i] = 0;}

	/* If a flare is starting. */

	if      (0 == wasInPass[i] && 0 != inPass[i]) {

	  /* Set the start time and position. */

	  theSatellite[i].GetHori(0, theScope, startPos[i]);
	  theScope.GetUThms(startTime[i]);

	  /* Set candiate end time and position */

	  theScope.GetUThms(endTime[i]);
	  endPos[i][0] = startPos[i][0];
	  endPos[i][1] = startPos[i][1];
	  endPos[i][2] = startPos[i][2];

	  /* Set candiate peak time, position and angle */

	  theScope.GetUThms(peakTime[i]);
	  peakPos[i][0] = startPos[i][0];
	  peakPos[i][1] = startPos[i][1];
	  peakPos[i][2] = startPos[i][2];
	  peakAngle[i]  = theAngle;

	}

	/* Else if a flare has ended. */

	else if (0 != wasInPass[i] && 0 == inPass[i]) {

	  /* Write XHTML table row. */

	  theXhtml.print("<tr><td>"
	    + theSatellite[i].GetName() + "</td><td>");
	  theXhtml.print(Hmelib.WTime6(startTime[i][0],
				       startTime[i][1], startTime[i][2]));
	  theXhtml.print("</td><td align=\"right\">");
	  theXhtml.print(Hmelib.Wfndm(5, 1, startPos[i][0] * Hmelib.DEGPERRAD));
	  theXhtml.print("&deg;</td><td align=\"right\">");
	  theXhtml.print(Hmelib.Wfndm(4, 1, startPos[i][1] * Hmelib.DEGPERRAD));
	  theXhtml.print("&deg;</td>");

	  theXhtml.print("<td>");
	  theXhtml.print(Hmelib.WTime6(peakTime[i][0],
				       peakTime[i][1], peakTime[i][2]));
	  theXhtml.print("</td><td align=\"right\">");
	  theXhtml.print(Hmelib.Wfndm(5, 1, peakPos[i][0] * Hmelib.DEGPERRAD));
	  theXhtml.print("&deg;</td><td align=\"right\">");
	  theXhtml.print(Hmelib.Wfndm(4, 1, peakPos[i][1] * Hmelib.DEGPERRAD));
	  theXhtml.print("&deg;</td><td align=\"right\">");
	  theXhtml.print(Hmelib.Wfndm(3, 1, peakAngle[i]));
	  theXhtml.print("&deg;</td>");

	  theXhtml.print("<td>");
	  theXhtml.print(Hmelib.WTime6(endTime[i][0],
				       endTime[i][1], endTime[i][2]));
	  theXhtml.print("</td><td align=\"right\">");
	  theXhtml.print(Hmelib.Wfndm(5, 1, endPos[i][0] * Hmelib.DEGPERRAD));
	  theXhtml.print("&deg;</td><td align=\"right\">");
	  theXhtml.print(Hmelib.Wfndm(4, 1, endPos[i][1] * Hmelib.DEGPERRAD));
	  theXhtml.print("&deg;</td></tr>\n");

	  /* Write WML table row. */

	  theWml.print("<p>\n");
	  theWml.print(Hmelib.WTime6(peakTime[i][0],
				     peakTime[i][1], peakTime[i][2]));
	  theWml.print(" ");
	  theWml.print(Hmelib.Wfndm(3, 0, peakPos[i][0] * Hmelib.DEGPERRAD));
	  theWml.print(" ");
	  theWml.print(Hmelib.Wfndm(2, 0, peakPos[i][1] * Hmelib.DEGPERRAD));
	  theWml.print(" ");
	  theWml.print(Hmelib.Wfndm(3, 1, peakAngle[i]));
	  theWml.print("\n</p>\n");

	}

	/* Else if in flare. */

	else if (0 != inPass[i]) {

	  /* Update candiate end time and position. */

	  theSatellite[i].GetHori(0, theScope, endPos[i]);
	  theScope.GetUThms(endTime[i]);

	  /* If brighter than peak so far, update candidate peak. */

	  if (peakAngle[i] > theAngle) {
	    theScope.GetUThms(peakTime[i]);
	    peakPos[i][0] = endPos[i][0];
	    peakPos[i][1] = endPos[i][1];
	    peakPos[i][2] = endPos[i][2];
	    peakAngle[i]  = theAngle;
	  }
	}

	wasInPass[i] = inPass[i];
      }
    }

    /* Table end and blurb. */

    theWml.print("</card>\n</wml>\n");
    theXhtml.print("</table>\n"
      + "<p>Listed are for each flare the name of the satellite, the start,\n"
      + "peak and end of the flare.  Start is here when the flare angle\n"
      + "goes below 2&deg;, end when it exceeds that value again.  The peak\n"
      + "is when the angle is at its smallest (to the nearest second).\n"
      + "Apart from the times are listed the azimuth\n"
      + "(0&deg; N, 90&deg; E, 180&deg; S, 270&deg; W) and elevation\n"
      + "(0&deg; horizon, 90&deg; zenith).  For the peak the flare angle\n"
      + "is also listed.</p>\n");

    /* Close the output files. */

    theWml.close();
    theXhtml.close();

    return;
  }


  /**
   * Serve <code>h0003</code> command.
   *
   * <p>This writes to a file a fragment of XHTML stating when ISS/Zarya
   * passes during the next 24 hour period (from midday).  It uses the system
   * clock to determine the nearest midday, and it uses the file
   * <code>./data/stations.dat</code> to read the observatory
   * "Royal Observatory Edinburgh" from and the file
   * <code>./data/stations.txt</code> to read the satellite "ISS (ZARYA)" from.
   * It writes the result to the file <code>./zarday.html</code></p>
   *
   * <p>This also writes an equivalent WML file (for WAP phones) to
   * <code>./zarday.wml</code>.</p>
   *
   * @param aCommand
   *   The command string including any parameters to know what to show.*/

  public final void h0003(String aCommand)
    throws StationException, SDP4Exception, HmelibException, IOException
  {
    PrintStream theXhtml, theWml;
    Telescope theScope;
    Satellite theSatellite;
    double    theStart, theStep, theEnd, theJD;
    double    theSun[]     = new double[3];
    double    theDate[]    = new double[3];
    double    theSatPos[]  = new double[3];
    double    peakTime[]   = new double[3];
    double    peakPos[]    = new double[3];
    double    lastTime[]   = new double[3];
    double    lastPos[]    = new double[3];
    int       theNstep, inPass, wasInPass;
    int       i;

    /* Read the station from file. */

    ReadByName("data/stations.dat", "Royal Observatory Edinburgh");

    /* Set the time to the nearest midday UT, starting from system time. */

    SetUTSystem();
    SetJD(Math.floor(0.5 + GetJD()));
    GetDate(theDate);

    /* Initialise the Telescope for the time loop.  Then copy its state
     * - in particular the station - from this. */

    theScope = new Telescope();
    theScope.Init();
    theScope.Copy(this);

    /* Work out the time stepping in JD. */

    theStart = GetJD();
    theEnd   = theStart + 1.;
    theStep  = 1. / 1440.;
    theNstep = (int) Math.ceil((theEnd - theStart) / theStep);

    /* Read the satellite from file. */

    theSatellite = new Satellite();
    theSatellite.Init();
    theSatellite.ReadByName("data/stations.txt", "ISS (ZARYA)");

    /* Open the output files. */

    theXhtml = new PrintStream(new FileOutputStream("zarday.html"));
    theWml   = new PrintStream(new FileOutputStream("zarday.wml"));

    /* Some fixed XHTML, observatory and date, table header. */

    theXhtml.print("<h2>ISS/Zarya tonight</h2>\n\n"
      + "<p>\nThese are the passes of the International Space Station\n"
      + "(Zarya) over the location\n"
      + "\"" + itsName + "\"\n"
      + "during the 24 hour period following midday Universal Time of\n");
    theXhtml.print(Hmelib.WTime5(theDate[0], theDate[1], theDate[2]));
    theXhtml.print(", subject to civil twilight.\n</p>\n");

    theWml.print("<?xml version=\"1.0\"?>\n"
      + "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\"\n"
      + "  \"http://www.wapforum.org/DTD/wml_1.1.xml\">\n"
      + "<wml>\n"
      + "<template>\n"
      + "  <do type='prev' label='Back'>\n"
      + "    <prev/>\n"
      + "  </do>\n"
      + "</template>\n"
      + "<card>\n"
      + "<p>\n"
      + "ISS/Zarya for " + itsName + ", night of ");
    theWml.print(Hmelib.WTime5(theDate[0], theDate[1], theDate[2]));
    theWml.print(".\n</p>\n");

    theXhtml.print("<p></p>\n"
      + "<table border=\"1\" summary=\"ISS/Zarya passes\">\n"
      + "<col /><col /><col /><col /><col /><col /><col /><col /><col />\n"
      + "<tr><th scope=\"col\" colspan=\"3\">start</th>"
      + "<th scope=\"col\" colspan=\"3\">peak</th>"
      + "<th scope=\"col\" colspan=\"3\">end</th></tr>\n");
    theWml.print("<p>\nUT A h\n</p>\n");

    /* Loop through time. */

    wasInPass = 0; inPass = 0;
    for (i = 0; i < theNstep ; i++) {

      /* Calculate time for next step. */

      theJD = theStart + (double) i * theStep;
      theScope.SetJD(theJD);

      /* Update the satellite, check it is up, sunlit and it is dark. */

      theSatellite.Update(theScope);
      inPass = 0;
      if (0 != theSatellite.itsIsSunlit) {
	theSatellite.GetHori(0, theScope, theSatPos);
	if (0. < theSatPos[1]) {
	  theScope.itsSun.GetHori(0, theScope, theSun);
	  if (NamedObject.RISECIVIL > theSun[1]) {
	    inPass = 1;
	  }
	}
      }

      /* If we changed from no to yes. */

      if (0 == wasInPass && 0 != inPass) {

	/* Reset the peak tracker. */

	theScope.GetUThms(peakTime);
	peakPos[0] = theSatPos[0];
	peakPos[1] = theSatPos[1];
	peakPos[2] = theSatPos[2];

        /* Write time, A, h as start of a pass. */

	theXhtml.print("<tr><td>");
        theXhtml.print(Hmelib.WTime1(peakTime[0], peakTime[1], peakTime[2]));
	theXhtml.print("</td><td align=\"right\">");
	theXhtml.print(Hmelib.Wfndm(5, 1, peakPos[0] * Hmelib.DEGPERRAD));
	theXhtml.print("&deg;</td><td align=\"right\">");
	theXhtml.print(Hmelib.Wfndm(4, 1, peakPos[1] * Hmelib.DEGPERRAD));
	theXhtml.print("&deg;</td>");
      }

      /* Else if we changed from yes to no. */

      else if (0 != wasInPass && 0 == inPass) {

	/* Write the peak, then the previous entry as end of pass. */

	theXhtml.print("<td>");
        theXhtml.print(Hmelib.WTime1(peakTime[0], peakTime[1], peakTime[2]));
	theXhtml.print("</td><td align=\"right\">");
	theXhtml.print(Hmelib.Wfndm(5, 1, peakPos[0] * Hmelib.DEGPERRAD));
	theXhtml.print("&deg;</td><td align=\"right\">");
	theXhtml.print(Hmelib.Wfndm(4, 1, peakPos[1] * Hmelib.DEGPERRAD));
	theXhtml.print("&deg;</td>");

	theWml.print("<p>\n");
        theWml.print(Hmelib.WTime1(peakTime[0], peakTime[1], peakTime[2]));
	theWml.print(" ");
	theWml.print(Hmelib.Wfndm(3, 0, peakPos[0] * Hmelib.DEGPERRAD));
	theWml.print(" ");
	theWml.print(Hmelib.Wfndm(2, 0, peakPos[1] * Hmelib.DEGPERRAD));
	theWml.print("</p>\n");

	theXhtml.print("<td>");
        theXhtml.print(Hmelib.WTime1(lastTime[0], lastTime[1], lastTime[2]));
	theXhtml.print("</td><td align=\"right\">");
	theXhtml.print(Hmelib.Wfndm(5, 1, lastPos[0] * Hmelib.DEGPERRAD));
	theXhtml.print("&deg;</td><td align=\"right\">");
	theXhtml.print(Hmelib.Wfndm(4, 1, lastPos[1] * Hmelib.DEGPERRAD));
	theXhtml.print("&deg;</td></tr>\n");
      }

      /* Else if h higher than peak so far. */

      else if (peakPos[1] < theSatPos[1]) {

	/* Update peak tracker. */

	theScope.GetUThms(peakTime);
	peakPos[0] = theSatPos[0];
	peakPos[1] = theSatPos[1];
	peakPos[2] = theSatPos[2];
      }

      theScope.GetUThms(lastTime);
      lastPos[0] = theSatPos[0];
      lastPos[1] = theSatPos[1];
      lastPos[2] = theSatPos[2];
      wasInPass = inPass;
    }

    /* Table end and blurb. */

    theWml.print("</card>\n</wml>\n");
    theXhtml.print("</table>\n"
      + "<p>Listed are for each pass the start, highest elevation and end\n"
      + "of visibility.  The times are full minutes within the period of\n"
      + "visibility.  For these instances are listed the time, azimuth\n"
      + "(0&deg; N, 90&deg; E, 180&deg; S, 270&deg; W) and elevation\n"
      + "(0&deg; horizon, 90&deg; zenith).  Evening passes start with the\n"
      + "rise and end with ingress into the Earth's shadow.  Morning passes\n"
      + "start with egress and end with the set.  The peak elevation\n"
      + "(during visibility) may be the same as the ingress or egress.</p>\n");

    /* Close the output files. */

    theWml.close();
    theXhtml.close();

    return;
  }


  /**
   * Serve <code>h0002</code> command.
   *
   * <p>This writes to a file a fragment of XHTML stating when planets rise
   * and set during the next 24 hour period (from midday).  It uses the system
   * clock to determine the nearest midday, and it uses the file
   * <code>./data/stations.dat</code> to read the observatory
   * "Royal Observatory Edinburgh" from.  It writes the result to the file
   * <code>./planday.html</code></p>
   *
   * <p>This also writes an equivalent WML file (for WAP phones) to
   * <code>./planday.wml</code>.</p>
   *
   * @param aCommand
   *   The command string including any parameters to know what to show.*/

  public final void h0002(String aCommand)
    throws StationException, HmelibException, IOException
  {
    PrintStream theXhtml, theWml;
    Moon        theMoon;
    Mercury     theMercury;
    Venus       theVenus;
    Mars        theMars;
    Jupiter     theJupiter;
    Saturn      theSaturn;
    Uranus      theUranus;
    Neptune     theNeptune;
    Pluto       thePluto;
    Times       theTime;
    double      theDate[]    = new double[3];
    double      theTriplet[] = new double[3];

    /* Set up theTime in case we fail to get a rise/set time. */

    theTime = new Times();
    theTime.Init();

    /* Read the station from file. */

    ReadByName("data/stations.dat", "Royal Observatory Edinburgh");

    /* Set the time to the nearest midday UT, starting from system time. */

    SetUTSystem();
    SetJD(Math.floor(0.5 + GetJD()));
    GetDate(theDate);

    /* Open the output files. */

    theXhtml = new PrintStream(new FileOutputStream("planday.html"));
    theWml   = new PrintStream(new FileOutputStream("planday.wml"));

    /* Some fixed XHTML, observatory and date. */

    theXhtml.print("<h2>The planets tonight</h2>\n\n"
      + "<p>\nThese are the times of twilight for the location\n"
      + "\"" + itsName + "\"\n"
      + "during the 24 hour period following midday Universal Time of\n");
    theXhtml.print(Hmelib.WTime5(theDate[0], theDate[1], theDate[2]));
    theXhtml.print(":\n</p>\n");

    theWml.print("<?xml version=\"1.0\"?>\n"
      + "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\"\n"
      + "  \"http://www.wapforum.org/DTD/wml_1.1.xml\">\n"
      + "<wml>\n"
      + "<template>\n"
      + "  <do type='prev' label='Back'>\n"
      + "    <prev/>\n"
      + "  </do>\n"
      + "</template>\n"
      + "<card>\n"
      + "<p>\n"
      + "Planets' rise &amp; set (UT) for " + itsName + ",\n");
    theWml.print(Hmelib.WTime5(theDate[0], theDate[1], theDate[2]));
    theWml.print(":\n</p>\n");

    /* Civil dusk and dawn, dto. nautical and astronomical.
     * (false for set, true for rise.) */

    theXhtml.print("<p></p>\n"
      + "<table border=\"1\" summary=\"twilight times\">\n"
      + "<col /><col /><col />\n<tr><td>&nbsp;</td>"
      + "<th scope=\"col\">dusk</th><th scope=\"col\">dawn</th></tr>\n");
    theWml.print("<p>\ndusk dawn\n</p>\n");

    theXhtml.print("<tr><th scope=\"row\">civil</td>");
    theWml.print("<p>\n");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISECIVIL, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISECIVIL, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" civil\n</p>\n");
    theXhtml.print("</tr>\n");

    theXhtml.print("<tr><th scope=\"row\">nautical</td>");
    theWml.print("<p>\n");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISENAUTI, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISENAUTI, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" nautical\n</p>\n");
    theXhtml.print("</tr>\n");

    theXhtml.print("<tr><th scope=\"row\">astronomical</td>");
    theWml.print("<p>\n");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISEASTRO, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISEASTRO, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" astronomical\n</p>\n");
    theXhtml.print("</tr>\n");

    theXhtml.print("</table>\n");

    /* Some fixed XHTML, observatory and date. */

    theXhtml.print(
        "<p>\nThese are the times of rise and set of the Sun, Moon\n"
      + "and planets for the location\n"
      + "\"" + itsName + "\"\n"
      + "during the 24 hour period following midday Universal Time of\n");
    theXhtml.print(Hmelib.WTime5(theDate[0], theDate[1], theDate[2]));
    theXhtml.print(":\n</p>\n");

    /* Rise and set of Sun, Moon and each planet. */

    theXhtml.print("<p></p>\n"
      + "<table border=\"1\" summary=\"twilight times\">\n"
      + "<col /><col /><col />\n<tr><td>&nbsp;</td>"
      + "<th scope=\"col\">rise</th><th scope=\"col\">set</th></tr>\n");
    theWml.print("<p>\nrise set\n</p>\n");

    theXhtml.print("<tr><th scope=\"row\">Sun</td>");
    theWml.print("<p>\n");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISESUN, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = itsSun.NextRiseSet(this, NamedObject.RISESUN, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Sun\n</p>\n");
    theXhtml.print("</tr>\n");

    theMoon = new Moon();
    theMoon.Init();
    theMoon.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Moon</td>");
    theWml.print("<p>\n");
    try {
      theTime = theMoon.NextRiseSet(this, NamedObject.RISESUN, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theMoon.NextRiseSet(this, NamedObject.RISESUN, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Moon\n</p>\n");
    theXhtml.print("</tr>\n");

    theMercury = new Mercury();
    theMercury.Init();
    theMercury.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Mercury</td>");
    theWml.print("<p>\n");
    try {
      theTime = theMercury.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theMercury.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Mercury\n</p>\n");
    theXhtml.print("</tr>\n");

    theVenus = new Venus();
    theVenus.Init();
    theVenus.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Venus</td>");
    theWml.print("<p>\n");
    try {
      theTime = theVenus.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theVenus.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Venus\n</p>\n");
    theXhtml.print("</tr>\n");

    theMars = new Mars();
    theMars.Init();
    theMars.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Mars</td>");
    theWml.print("<p>\n");
    try {
      theTime = theMars.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theMars.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Mars\n</p>\n");
    theXhtml.print("</tr>\n");

    theJupiter = new Jupiter();
    theJupiter.Init();
    theJupiter.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Jupiter</td>");
    theWml.print("<p>\n");
    try {
      theTime = theJupiter.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theJupiter.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Jupiter\n</p>\n");
    theXhtml.print("</tr>\n");

    theSaturn = new Saturn();
    theSaturn.Init();
    theSaturn.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Saturn</td>");
    theWml.print("<p>\n");
    try {
      theTime = theSaturn.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theSaturn.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Saturn\n</p>\n");
    theXhtml.print("</tr>\n");

    theUranus = new Uranus();
    theUranus.Init();
    theUranus.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Uranus</td>");
    theWml.print("<p>\n");
    try {
      theTime = theUranus.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theUranus.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Uranus\n</p>\n");
    theXhtml.print("</tr>\n");

    theNeptune = new Neptune();
    theNeptune.Init();
    theNeptune.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Neptune</td>");
    theWml.print("<p>\n");
    try {
      theTime = theNeptune.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = theNeptune.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Neptune\n</p>\n");
    theXhtml.print("</tr>\n");

    thePluto = new Pluto();
    thePluto.Init();
    thePluto.Update(this);
    theXhtml.print("<tr><th scope=\"row\">Pluto</td>");
    theWml.print("<p>\n");
    try {
      theTime = thePluto.NextRiseSet(this, NamedObject.RISE, true);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" ");
    try {
      theTime = thePluto.NextRiseSet(this, NamedObject.RISE, false);
    }
    catch (NamedObjectCircPolException e) {
      theTime.Copy(this);
      theTime.Add(2.);
    }
    if (1. > theTime.Sub(this)) {
      theTime.GetUThms(theTriplet);
      theXhtml.print("<td>");
      theXhtml.print(Hmelib.WTime1(theTriplet[0],
				   theTriplet[1], theTriplet[2]));
      theXhtml.print("</td>");
      theWml.print(Hmelib.WTime1(theTriplet[0], theTriplet[1], theTriplet[2]));
    }
    else {
      theXhtml.print("<td>&nbsp;</td>");
      theWml.print("--:--");
    }
    theWml.print(" Pluto\n</p>\n");
    theXhtml.print("</tr>\n");

    theWml.print("</card>\n</wml>\n");
    theXhtml.print("</table>\n<p></p>\n");

    theWml.close();
    theXhtml.close();

    return;
  }


  /**
   * Serve <code>h0001</code> command.
   *
   * <p>This returns the current information about Mars in a prosaic form
   * and in HTML language.</p>
   *
   * @param aCommand
   *   The command string including any parameters to know what to show.*/

  public final void h0001(String aCommand)
  {
    double theOctet[] = new double[8];
    Mars theMars;

    /* Create Mars and update it to the time currently set. */

    theMars = new Mars();
    theMars.Init();
    theMars.Update(this);

    /* The text we want looks like this:
     *
     * Today is 2003/06/13, the time is 06:02:25 Univeral Time.
     * Mars is currently 100.3 million km from Earth.
     * To an observer in Edinburgh it is 17 degrees above the South horizon.
     * The apparent diameter is 14 arcseconds, the brightness -1.0 mag.
     * 88 per cent of the planet appears illuminated.
     * The Martian South Pole is inclined 21 degrees toward the observer and
     * the rotation axis appears rotated 9 degrees clockwise
     * from celestial North.
     * The centre of the planetary disc as seen from Earth has
     * Martian longitude 37.3 degrees. */

    GetDate(theOctet);
    System.out.print("<p>Today is " + (int)theOctet[0] + "/");
    if (10 > (int)theOctet[1]) System.out.print("0");
    System.out.print((int)theOctet[1] + "/");
    if (10 > (int)theOctet[2]) System.out.print("0");
    System.out.print((int)theOctet[2] + ", the time is ");
    GetUThms(theOctet);
    if (10 > (int)theOctet[0]) System.out.print("0");
    System.out.print((int)theOctet[0] + ":");
    if (10 > (int)theOctet[1]) System.out.print("0");
    System.out.print((int)theOctet[1] + ":");
    if (10 > (int)(theOctet[2] + 0.5)) System.out.print("0");
    System.out.print((int)(theOctet[2] + 0.5) + " Universal Time.\n");

    theMars.GetXYZ(theOctet);
    System.out.print("<p>" + theMars.GetName() + " is currently ");
    System.out.print(Hmelib.Wfndm(1, 1, Math.sqrt(theOctet[0] * theOctet[0]
      + theOctet[1] * theOctet[1] + theOctet[2] * theOctet[2])));
    System.out.print(" million km from Earth.\n");

    theMars.GetHori(0, this, theOctet);
    theOctet[0] *= Hmelib.DEGPERRAD;
    theOctet[1] *= Hmelib.DEGPERRAD;
    System.out.print("<p>As seen from the location \""
      + itsName + "\"\nit is ");
    if (0. > theOctet[1]) {
      System.out.print(Hmelib.Wfndm(1, 0, -theOctet[1]));
      System.out.print(" degrees below the ");
    }
    else {
      System.out.print(Hmelib.Wfndm(1, 0, theOctet[1]));
      System.out.print(" degrees above the ");
    }
    if (22.5 > theOctet[0] || 360. - 22.5 < theOctet[0]) {
      System.out.print("North horizon.\n");
    }
    else if (67.5 > theOctet[0]) {
      System.out.print("Northeast horizon.\n");
    }
    else if (112.5 > theOctet[0]) {
      System.out.print("East horizon.\n");
    }
    else if (157.5 > theOctet[0]) {
      System.out.print("Southeast horizon.\n");
    }
    else if (202.5 > theOctet[0]) {
      System.out.print("South horizon.\n");
    }
    else if (247.5 > theOctet[0]) {
      System.out.print("Southwest horizon.\n");
    }
    else if (292.5 > theOctet[0]) {
      System.out.print("West horizon.\n");
    }
    else {
      System.out.print("Northwest horizon.\n");
    }

    theMars.GetPhysics(theOctet, this);
    System.out.print("<p>The apparent diameter is ");
    System.out.print(Hmelib.Wfndm(1, 0, 2. * theOctet[1] * 3600.
      * Hmelib.DEGPERRAD));
    System.out.print(" arcseconds, the brightness ");
    System.out.print(Hmelib.Wfndm(1, 1, theOctet[0]));
    System.out.print(" mag.\n");
    System.out.print(Hmelib.Wfndm(1, 0, 100. * theOctet[4]));
    System.out.print(" per cent of the planet appears illuminated.\n");

    if (0. > theOctet[5]) {
      System.out.print("<p>The Martian South pole is inclined ");
      System.out.print(Hmelib.Wfndm(1, 0, -theOctet[5] * Hmelib.DEGPERRAD));
    }
    else {
      System.out.print("<p>The Martian North pole is inclined ");
      System.out.print(Hmelib.Wfndm(1, 0, theOctet[5] * Hmelib.DEGPERRAD));
    }
    System.out.print(" degrees toward the observer and the \n"
      + "rotation axis appears rotated ");
    if (0. > theOctet[6]) {
      System.out.print(Hmelib.Wfndm(1, 0, -theOctet[6] * Hmelib.DEGPERRAD));
      System.out.print(" degrees clockwise from celestial North.\n");
    }
    else {
      System.out.print(Hmelib.Wfndm(1, 0, theOctet[6] * Hmelib.DEGPERRAD));
      System.out.print(" degrees counter-clockwise from celestial North.\n");
    }

    System.out.print("The centre of the planetary disc "
      + "as seen from Earth\nhas Martian longitude ");
    System.out.print(Hmelib.Wfndm(3, 1, theOctet[7] * Hmelib.DEGPERRAD));
    System.out.print(" degrees.\n");
  }


  /**
   * Initialise the object.
   *
   * <p>This initialises the Station part, then obtains itsObject and itsSun
   * and initialises them.  itsSun is also updated to the current time.</p> */

  public final void Init() {
    super.Init();
    itsObject = new NamedObject();
    itsObject.Init();
    itsSun = new Sun();
    itsSun.Init();
    Update();
  }


  /**
   * Set the time to the given Julian Day (UT).
   *
   * @param aJD
   *   The Julian Day minus 2.45 million days that is to be stored. */

  public void SetJD(double aJD) {
    super.SetJD(aJD);
    Update();
    return;
  }


  /**
   * Set the time to the given date and time (TT).
   *
   * @param aYear
   *   The calendar year.
   * @param aMonth
   *   The calendar month.
   * @param aDay
   *   The calendar day.
   * @param aHour
   *   The TT hour.
   * @param aMinute
   *   The TT minute.
   * @param aSecond
   *   The TT second. */

  public void SetTT(int aYear, int aMonth, int aDay,
    double aHour, double aMinute, double aSecond)
    throws TimesAmbigDateException
  {
    super.SetTT(aYear, aMonth, aDay, aHour, aMinute, aSecond);
    Update();
    return;
  }


  /**
   * Set the time to the given date and time (UT).
   *
   * @param aYear
   *   The calendar year.
   * @param aMonth
   *   The calendar month.
   * @param aDay
   *   The calendar day.
   * @param aHour
   *   The UT hour.
   * @param aMinute
   *   The UT minute.
   * @param aSecond
   *   The UT second. */

  public void SetUT(int aYear, int aMonth, int aDay,
    double aHour, double aMinute, double aSecond)
    throws TimesAmbigDateException
  {
    super.SetUT(aYear, aMonth, aDay, aHour, aMinute, aSecond);
    Update();
    return;
  }


  /**
   * Set the time from the system clock. */

  public void SetUTSystem()
  {
    super.SetUTSystem();
    Update();
    return;
  }


  /**
Calculate and print rise and set.

<p>This asks the given object for its rise and set times (in fact the
times it crosses the given elevation upwards and downwards).  It then
writes out the result to the given open output file.  The format is</p>

<pre>
Object: Galactic Centre

  Set:  2003-09-17T20:42
  Rise: 2003-09-18T15:42
</pre>

<p>Rise and set are written in chronological order.  Both times are in
the future (as referred to the time in this instance of Telescope), but
at most somewhat more than a day in the future.</p>

@param aObject
  The object whose rise and set is requested.
@param aElev
  The elevation defining the event.  See
  {@link NamedObject#NextRiseSet NamedObject.NextRiseSet} for details.
   */

  protected final String ShowRiseSet(NamedObject aObject, double aElev)
    throws CometNoConvException
  {
    String theOutput = "";
    Times[] twoTimes = new Times[2];
    double theDate[] = new double[3];
    double theTime[] = new double[3];
    boolean haveRise, haveSet;

    /* Print out the object name. */

    theOutput = "\nObject: " + aObject.GetName() + "\n";

    /* Calculate the rise, then the set.
     * If either inidicates circumpolarity, catch, report this for both and
     * bail out. */

    haveRise = true;
    try {
      twoTimes[0] = aObject.NextRiseSet(this, aElev, true);
    }
    catch (NamedObjectCircPolException e) {
      haveRise = false;
    }
    haveSet = true;
    try {
      twoTimes[1] = aObject.NextRiseSet(this, aElev, false);
    }
    catch (NamedObjectCircPolException e) {
      haveSet = false;
    }

    if (!haveRise && !haveSet) {
      theOutput = theOutput
        + "\n  Rise: none (circumpolar)"
        + "\n  Set:  none (circumpolar)";
    }
    else if (!haveRise) {
      twoTimes[1].GetDate(theDate);
      twoTimes[1].GetUThms(theTime);
      theOutput = theOutput
        + "\n  Set:  "
        + Hmelib.WTime4(theDate[0], theDate[1], theDate[2],
			theTime[0], theTime[1], theTime[2])
        + "\n  Rise: none (circumpolar)";
    }
    else if (!haveSet) {
      twoTimes[0].GetDate(theDate);
      twoTimes[0].GetUThms(theTime);
      theOutput = theOutput
        + "\n  Rise: "
        + Hmelib.WTime4(theDate[0], theDate[1], theDate[2],
			theTime[0], theTime[1], theTime[2])
        + "\n  Set:  none (circumpolar)";
    }
    else if (0. < twoTimes[1].Sub(twoTimes[0])) {
      twoTimes[0].GetDate(theDate);
      twoTimes[0].GetUThms(theTime);
      theOutput = theOutput
        + "\n  Rise: "
        + Hmelib.WTime4(theDate[0], theDate[1], theDate[2],
			theTime[0], theTime[1], theTime[2]);
      twoTimes[1].GetDate(theDate);
      twoTimes[1].GetUThms(theTime);
      theOutput = theOutput
        + "\n  Set:  "
        + Hmelib.WTime4(theDate[0], theDate[1], theDate[2],
			theTime[0], theTime[1], theTime[2]);
    }
    else {
      twoTimes[1].GetDate(theDate);
      twoTimes[1].GetUThms(theTime);
      theOutput = theOutput
        + "\n  Set:  "
        + Hmelib.WTime4(theDate[0], theDate[1], theDate[2],
			theTime[0], theTime[1], theTime[2]);
      twoTimes[0].GetDate(theDate);
      twoTimes[0].GetUThms(theTime);
      theOutput = theOutput
        + "\n  Rise: "
        + Hmelib.WTime4(theDate[0], theDate[1], theDate[2],
			theTime[0], theTime[1], theTime[2]);
    }

    theOutput = theOutput + "\n\n";

    return theOutput;
  }


  /**
   * Update after time change.
   *
   * <p>This updates the whole state according to the time currently
   * set.</p> */

  protected final void Update() {itsSun.Update(this);}


  /**
Serve <code>planet/au/venus</code> command.

<p>This calculates assumes 11 different values of the astronomical unit
and prints out the consequent topocentric apparent separation between
Venus and the Sun.  While this can be done for any time, it makes most
sense during a Venus transit like 2004-06-08 or 2012-06-06.  The format
of the output is</p>

<pre>
Observatory: Edinburgh
   East long.   -3.217000 deg
   Latitude     55.950000 deg
   Altitude            50 m

  UT: 2004-06-08T08:00:00.0 (JD  2453164.833333)
  TT: 2004-06-08T08:01:06.5 (JDE 2453164.834103)
  Ep: 2004.434864074
             GST 01:08:10.8 =  17.045078 deg
             LST 00:55:18.7 =  13.828078 deg

Venus-Sun separation for various AU values

    AU      d       d       d       d       d
    Gm      "       "       "       "       "
  -----  --+0--  --+2--  --+4--  --+6--  --+8--
  100.0   657.4   656.9   656.4   655.9   655.4
  110.0   655.0   654.6   654.2   653.8   653.4
  120.0   653.0   652.7   652.3   652.0   651.7
  130.0   651.4   651.1   650.8   650.5   650.2
  140.0   649.9   649.7   649.4   649.2   648.9
  150.0   648.7   648.5   648.2   648.0   647.8
  160.0   647.6   647.4   647.2   647.0   646.8
  170.0   646.6   646.5   646.3   646.1   646.0
  180.0   645.8   645.6   645.5   645.3   645.2
  190.0   645.0   644.9   644.8   644.6   644.5
  200.0   644.4   644.2   644.1   644.0   643.9
</pre>

<p>The AU values are 100.0, 102.0, ... 110.0, ... 208.0 Gm.  The left
column shows an AU value, the second column (+0) the Venus-Sun separation
for that value.  The further columns show the separation for increasing
AU values (+2, +4, +6, +8 Gm).</p>

@param aCommand
  The command string, ignored.
 */

  public final String VenusTransit(String aCommand)
    throws TimesAmbigDateException, HmelibException
  {
    String      theOutput = "";
    double      thea[]    = new double[3];     /* topocentre */
    double      thex[]    = new double[3];     /* Sun */
    double      theX[]    = new double[3];     /* Venus */
    double      thext[]   = new double[3];
    double      theXt[]   = new double[3];
    NamedObject theTopo   = new NamedObject();
    Telescope   theScope  = new Telescope();
    Venus       theVenus  = new Venus();
    double      theAU, theDist;
    int         i, j, k;

    /* Copy this instance of Telescope to a new instance. */

    theScope.Init();
    theScope.Copy(this);

    /* Get an instance of Venus and update it for this time. */

    theVenus.Init();
    theVenus.Update(theScope);

    /* Get an instance of NamedObject for the topocentre.
     * Set it to (0,0,0) topocentric.  The method converts to geocentric. */

    theTopo.Init();
    thea[0] = 0.; thea[1] = 0.; thea[2] = 0.;
    theTopo.SetTopo(0, theScope, thea);

    /* Extract the geocentric position of the topocentre (a) with GetXYZ.
     * Extract the geocentric position of the Sun (x) with GetPos or GetXYZ.
     * Extract the geocentric position of Venus (X) with GetXYZ.
     * Re-convert Sun and Venus from Gm to AU. */

    theTopo.GetXYZ(thea);
    theScope.itsSun.GetXYZ(thex);
    theVenus.GetXYZ(theX);
    for (i = 0; i < 3; i++) {
      thex[i] /= NamedObject.AU;
      theX[i] /= NamedObject.AU;
    }

    /* Write header. */

    theOutput = Show()
      + "Venus-Sun separation for various AU values\n\n"
      + "    AU      d       d       d       d       d\n"
      + "    Gm      \"       \"       \"       \"       \"\n"
      + "  -----  --+0--  --+2--  --+4--  --+6--  --+8--\n";

    /* Loop through hypothetical AU values. */

    for (j = 0; j < 11; j++) {
      for (k = 0; k < 5; k++) {

        /* Set the AU value. */

        theAU = 100. + 10. * j + 2. * k;

	/* Topocentric positions. */

	for (i = 0; i < 3; i++) {
	  thext[i] = thex[i] - thea[i] / theAU;
	  theXt[i] = theX[i] - thea[i] / theAU;
	}

	/* Topocentric apparent distance. */

	theDist  = Hmelib.SpherDist(thext, theXt);
	theDist *= Hmelib.DEGPERRAD;
	theDist *= 3600.;

	/* Print the lead AU value. */

	if (0 == k) {
	  theOutput = theOutput + "  " + Hmelib.Wfndm(5, 1, theAU);
	}

	/* Print the result. */

	if (10000. > theDist) {
	  theOutput = theOutput + "  " + Hmelib.Wfndm(6, 1, theDist);
	}
	else {
	  theOutput = theOutput + "  ******";
	}

	/* Print the line feed. */

	if (4 == k) {
	  theOutput = theOutput + "\n";
	}
      }
    }

    /* Trailing blank line. */

    theOutput = theOutput + "\n";

    return theOutput;
  }

}
