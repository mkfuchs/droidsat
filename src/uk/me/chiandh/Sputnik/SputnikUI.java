
package uk.me.chiandh.Sputnik;


import java.io.*;
import java.util.*;

/**
<h2>Sputnik: Astronomical ephemeris</h2>

<ul>
<li><a href="#install">Installation</a></li>
<li><a href="#invoke">Invocation</a></li>
<li><a href="#commands">User commands</a></li>
<li><a href="#classes">An overview of the class hierarchy</a></li>
<li><a href="#todo">To do</a></li>
<li><a href="#maint">Source maintenance and build process</a></li>
</ul>

<h2><a name="install" id="install">Installation</a></h2>

<p>It is necessary to install the JLine package before trying to run
Sputnik. The JLine distribution contains a jar that has to be placed in the
tree of the Java Runtime Environment (JRE). For example, I currently have
the JRE as part of a Java Developer Kit (JDK) in /usr/local/jdk1.6.0_03/jre.
The JLine jar file should then be /usr/local/jdk1.6.0_03/jre/lib/ext/jline.jar.
This will enable the <code>java</code> command (the JRE) to find the
JLine classes, which is necessary for the Sputnik application to run.</p>

<p>All you need to run the application Sputnik is the Sputnik.jar file, which
you can place anywhere in your file space. There is a separate tar ball
archive with documentation (Sputnik-doc.tgz), and another tar ball with
the Java source code (Sputnik-source.tgz).</p>

<h2><a name="invoke" id="invoke">Invocation</a></h2>

<p>The user interface is strictly by command line, but there are several
variations. The uusal way to run the Sputnik application is to invoke its
JLine interface. Get yourself a command line window, say an xterm running
a bash or tcsh shell on Linux or MacOS, or a DOS shell on Windows and
invoke the Java Runtime Environment (JRE) with the Sputnik jar as argument
and with a further argument "--jline":</p>

<pre>
java -jar /path/to/Sputnik.jar --jline
</pre>

<p>Sputnik will display a licence/warranty notice and show the command
prompt and is then ready to take commands. With the JLine interface you
can use the Tab key to complete the names of commands or files as you
might be familiar with from your Linux, MacOS or DOS shell. You can also
recall previous commands and edit them, using the cursor keys. If you leave
out the "--jline" above you get the same interface with the same prompt,
but you have to type everything correctly by yourself.</p>

<p>Occasionally you may want to run just one command for Sputnik to
execute and that's all. This can be done with a command line like</p>

<pre>
java -jar /path/to/Sputnik.jar --command "comet/show Soft03Cmt.edb 17P/Holmes"
</pre>

<h2><a name="commands" id="commands">User commands</a></h2>

<p>Once Sputnik has started and displays its command prompt you can use the
following commands to change settings like the clock or viewing direction
and then to obtain answers about the objects in the universe.</p>

<p><strong>Regular commands:</strong></p>

<dl>
<dt>help</dt>
<dd><code>help [&lt;command&gt;]</code><br />
  <em>Display help or usage text.</em><br />
  If given without parameter this will list all commands without syntax but
  with the short explanation.  If given with parameter this will list
  command syntaxes for all commands whose start matches the given
  parameter.</dd>
<dt>quit</dt>
<dd><code>quit</code><br />
<em>Quit the application.</em><br />
  This command will end command processing and quit the application.  You
  can also enter the end-of-file character Ctrl-D to achieve this.</dd>
<dt>file/macro</dt>
<dd><code>file/macro &lt;file&gt;</code><br />
<em>Execute a script of commands.</em><br />
  If you have a file with one command per line, use this to read and execute
  those commands.</dd>
<dt>station/show</dt>
<dd><code>station/show</code><br />
<em>Show observatory position and clock.</em><br />
  This will write to the terminal the observatory's position and clock
  reading in various representations: UT, TT, civil format, JD, Julian
  Epoch, also GST and LST.</dd>
<dt>station/twilight</dt>
<dd><code>station/twilight</code><br />
<em>Show dusk and dawn.</em><br />
  This will write to the terminal the next times when dawn starts and dusk
  ends.  All three definitions of twilight (civil, nautical, astronomical)
  are shown.</dd>
<dt>station/sys</dt>
<dd><code>station/sys</code><br />
<em>Set observatory clock from the system clock.</em><br />
  This will set the observatory clock using the computer's system
  clock.</dd>
<dt>station/ut</dt>
<dd><code>station/ut &lt;YMDhms&gt;</code><br />
<em>Set observatory clock with UT.</em><br />
  This will set the observatory clock to the time given as UT date and time.
  Enter the time in the format Y-M-D-h:m:s.  Year and month must be
  integers, the others can be floating point numbers.  However, all six
  numbers must be given, e.g. 1977-4-26.4-0:0:0.  Each number can
  individually have a minus sign, which applies only to that number.</dd>
<dt>station/tt</dt>
<dd><code>station/tt &lt;YMDhms&gt;</code><br />
<em>Set observatory clock with TT.</em><br />
  This will set the observatory clock to the time given as TT date and time.
  Enter the time in the format Y-M-D-h:m:s.  Year and month must be
  integers, the others can be floating point numbers.  However, all six
  numbers must be given, e.g. 1977-4-26.4-0:0:0.  Each number can
  individually have a minus sign, which applies only to that number.</dd>
<dt>station/jd</dt>
<dd><code>station/jd &lt;JD&gt;</code><br />
<em>Set observatory clock with JD.</em><br />
  This will set the observatory clock to the time given as Julian Day (UT).
  The units are days, of course.  Include the 2450000 days that are
  internally subtracted.</dd>
<dt>station/read</dt>
<dd><code>station/read &lt;file&gt; &lt;station&gt;</code><br />
<em>Read observatory position from file.</em><br />
  This will read the file and search for the parameters of a station of
  the given name.  The parameters are used to set the position of the
  station, the time (UT) is unaffected.</dd>
<dt>object/show</dt>
<dd><code>object/show</code><br />
<em>Show coordinates of the object.</em><br />
  This will write to the terminal the sky position that the telescope is
  pointing at.  This is in various coordinate systems, from galactic to
  horizontal.  It also displays the radial velocity of the station along
  the line of sight in various standards of rest.</dd>
<dt>object/rise</dt>
<dd><code>object/rise</code><br />
<em>Show next rise and set of the object.</em><br />
  This will write to the terminal the times of the next rise and set of
  the object.  This takes account of an average refraction of 34'.</dd>
<dt>object/gal</dt>
<dd><code>object/gal
  &lt;lII&gt; &lt;bII&gt; &lt;dist&gt; &lt;name&gt;</code><br />
<em>Set galactic coordinates of the object.</em><br />
  This will set the sky position that the telescope it pointing at, using
  the given galactic longitude and latitude in degrees and the given
  distance in Gm.</dd>
<dt>object/B1950</dt>
<dd><code>object/B1950
  &lt;RA&gt; &lt;Dec&gt; &lt;dist&gt; &lt;name&gt;</code><br />
<em>Set B1950 coordinates of the object.</em><br />
  This will set the sky position that the telescope it pointing at, using
  the given B1950 right ascension and declination (both in degrees) and the
  given distance in Gm.</dd>
<dt>object/J2000</dt>
<dd><code>object/J2000
  &lt;RA&gt; &lt;Dec&gt; &lt;dist&gt; &lt;name&gt;</code><br />
<em>Set J2000 coordinates of the object.</em><br />
  This will set the sky position that the telescope it pointing at, using
  the given J2000 right ascension and declination (both in degrees) and the
  given distance in Gm.</dd>
<dt>object/mean</dt>
<dd><code>object/mean
  &lt;RA&gt; &lt;Dec&gt; &lt;dist&gt; &lt;name&gt;</code><br />
<em>Set equinox of date coordinates of the object.</em><br />
  This will set the sky position that the telescope it pointing at, using
  the given right ascension and declination (both in degrees, for the
  equinox of the date set in the station clock) and the given geocentric
  distance in Gm.</dd>
<dt>object/ecl</dt>
<dd><code>object/ecl
  &lt;lam&gt; &lt;bet&gt; &lt;dist&gt; &lt;name&gt;</code><br />
<em>Set ecliptic coordinates of the object.</em><br />
  This will set the sky position that the telescope it pointing at, using
  the given ecliptic longitude and latitude (in degrees, for the equinox and
  ecliptic of the date set in the station clock) and the given distance in
  Gm.</dd>
<dt>object/topo</dt>
<dd><code>object/topo
  &lt;HA&gt; &lt;Dec&gt; &lt;dist&gt; &lt;name&gt;</code><br />
<em>Set topocentric HA/Dec coordinates of the object.</em><br />
  This will set the sky position that the telescope it pointing at, using
  the given hour angle and declination (both in degrees) and the given
  topocentric distance in Gm.  Note that hour hangle counts retrograde
  (clockwise) from South towards West.</dd>
<dt>object/hori</dt>
<dd><code>object/hori
  &lt;A&gt; &lt;h&gt; &lt;dist&gt; &lt;name&gt;</code><br />
<em>Set horizontal coordinates of the object.</em><br />
  This will set the sky position that the telescope it pointing at, using
  the given azimuth and elevation (in degrees) and the given topocentric
  distance in Gm.  Note that azimuth counts retrograde (clockwise) from
  North towards East.</dd>
<dt>planet/show/sun</dt>
<dd><code>planet/show/sun</code><br />
<em>Show ephemeris of the Sun.</em><br />
  This will write to the terminal the ephemeris of the Sun at the time of
  the station clock.  This is similar to object/show.</dd>
<dt>planet/rise/sun</dt>
<dd><code>planet/rise/sun</code><br />
<em>Show next rise and set of the Sun.</em><br />
  This will write to the terminal the times of the next rise and set of
  the Sun.  This takes account of average refraction and apparent diameter
  to give the times the upper limb is on the real horizon.</dd>
<dt>planet/limb/sun</dt>
<dd><code>planet/limb/sun &lt;x1&gt; &lt;y1&gt;
  &lt;x2&gt; &lt;y2&gt; &lt;x3&gt; &lt;y3&gt;</code><br />
<em>Find centre and radius from three limb points.</em><br />
  This will use the three limb positions to work out the position of the
  centre and the radius.</dd>
<dt>planet/coord/sun</dt>
<dd><code>planet/coord/sun &lt;x0&gt; &lt;y0&gt;
  &lt;r&gt; &lt;q&gt; &lt;x&gt; &lt;y&gt;</code><br />
<em>Calculate heliographic coordinates.</em><br />
  This will use the given position of the centre and radius of the Sun as
  well as the given position angle of the detector vertical, it will use
  these to calculate the heliographic longitude and latitude of the
  position given in the last two numbers.  The calculation also involves
  the Sun's physical ephemeris, so the station clock must have been set
  beforehand.</dd>
<dt>planet/show/&lt;planet&gt;</dt>
<dd><code>planet/show/&lt;planet&gt;</code><br />
<em>Show ephemeris of a planet.</em><br />
  This will write to the terminal the ephemeris of a planet at the time of
  the station clock.  This is similar to object/show.  Valid planets are
  Mercury, Venus, Mars, Jupiter, Saturn, Uranus, Neptune and Pluto.  Use
  lower case and don't capitalise the planet name in the command.</dd>
<dt>planet/rise/&lt;planet&gt;</dt>
<dd><code>planet/rise/&lt;planet&gt;</code><br />
<em>Show next rise and set of a planet.</em><br />
  This will write to the terminal the times of the next rise and set of
  a planet.  This takes account of an average refraction of 34'.</dd>
<dt>planet/au/venus</dt>
<dd><code>planet/au/venus</code><br />
<em>Help to calculate AU from Venus transit.</em><br />
  This displays for the current time and topocentre the separation between
  Venus and the Sun in arc seconds.  It does this for 11 different values
  of the AU from 100 to 200 Gm in steps of 10 Gm.</dd>
<dt>moon/show</dt>
<dd><code>moon/show</code><br />
<em>Show ephemeris of the Moon.</em><br />
  This will write to the terminal the ephemeris of the Moon at the time of
  the station clock.  This is similar to object/show.</dd>
<dt>moon/rise</dt>
<dd><code>moon/rise</code><br />
<em>Show next rise and set of the Moon.</em><br />
  This will write to the terminal the times of the next rise and set of
  the Moon.  This takes account of average refraction and apparent diameter
  to give the times the upper limb is on the real horizon.</dd>
<dt>moon/limb</dt>
<dd><code>moon/limb &lt;x1&gt; &lt;y1&gt;
  &lt;x2&gt; &lt;y2&gt; &lt;x3&gt; &lt;y3&gt;</code><br />
<em>Find centre and radius from three limb points.</em><br />
  This will use the three limb positions to work out the position of the
  centre and the radius.</dd>
<dt>moon/coord</dt>
<dd><code>moon/coord &lt;x0&gt; &lt;y0&gt;
  &lt;r&gt; &lt;q&gt; &lt;x&gt; &lt;y&gt;</code><br />
<em>Calculate selenographic coordinates.</em><br />
  This will use the given position of the centre and radius of the Moon as
  well as the given position angle of the detector vertical, it will use
  these to calculate the selenographic longitude and latitude of the
  position given in the last two numbers.  The calculation also involves
  the Moon's physical ephemeris, so the station clock must have been set
  beforehand.</dd>
<dt>comet/show</dt>
<dd><code>comet/show &lt;file&gt; &lt;name&gt;</code><br />
<em>Show ephemeris of a comet or asteroid.</em><br />
  The file must be in xephem format, preferably obtained from
  <a href="http://cfa-www.harvard.edu/iau/">http://cfa-www.harvard.edu/iau/</a>.
  The name of the comet or asteroid is searched for in the file, orbital
  elements are read, ephemeris calculated and displayed to the terminal.
  The above Web site has separate files for the
  <a href="http://cfa-www.harvard.edu/iau/Ephemerides/Comets/Soft03Cmt.txt">current comets</a>
  and the bright astroids with opposition in a particular year, e.g.
  <a href="http://cfa-www.harvard.edu/iau/Ephemerides/Bright/2002/Soft03Bright.txt">for 2002.</a></dd>
<dt>comet/rise</dt>
<dd><code>comet/rise &lt;file&gt; &lt;name&gt;</code><br />
<em>Show next rise and set of a comet or asteroid.</em><br />
  This will write to the terminal the times of the next rise and set of
  a planet.  This takes account of an average refraction of 34'.
  The file must be in xephem format, preferably obtained from
  <a href="http://cfa-www.harvard.edu/iau/">http://cfa-www.harvard.edu/iau/</a>.
  The name of the comet or asteroid is searched for in the file, orbital
  elements are read, ephemeris calculated and displayed to the terminal.
  The above Web site has separate files for the
  <a href="http://cfa-www.harvard.edu/iau/Ephemerides/Comets/Soft03Cmt.txt">current comets</a>
  and the bright astroids with opposition in a particular year, e.g.
  <a href="http://cfa-www.harvard.edu/iau/Ephemerides/Bright/2002/Soft03Bright.txt">for 2002.</a></dd>
<dt>asteroid/show</dt>
<dd><code>asteroid/show &lt;file&gt; &lt;name&gt;</code><br />
<em>Show ephemeris of a comet or asteroid.</em><br />
  This is the same as <code>comet/show</code>, in case users hesitate to
  use a comet command on an asteroid.</dd>
<dt>asteroid/rise</dt>
<dd><code>asteroid/rise &lt;file&gt; &lt;name&gt;</code><br />
<em>Show next rise and set of a comet or asteroid.</em><br />
  This is the same as <code>comet/rise</code>, in case users hesitate to
  use a comet command on an asteroid.</dd>
<dt>satellite/show</dt>
<dd><code>satellite/show &lt;file&gt; &lt;name&gt;</code><br />
<em>Show ephemeris of a satellite.</em><br />
  The file must be in NORAD TLE format, usually obtained from
  <a href="http://www.celestrak.com">http://www.celestrak.com</a>.
  The name of the satellite is searched for in the file, orbital
  elements are read, ephemeris calculated and displayed to the
  terminal.</dd>
<dt>satellite/all</dt>
<dd><code>satellite/all &lt;file&gt;</code><br />
<em>Show ephemeris of many satellites.</em><br />
  The file must be in NORAD TLE format, usually obtained from
  <a href="http://www.celestrak.com">http://www.celestrak.com</a>.
  Each satellite listed in the file is processed.  The horizontal
  coordinates and sunlit status are listed to the terminal.  Only satellites
  above the horizon are listed.</dd>
<dt>satellite/pass</dt>
<dd><code>satellite/pass &lt;file&gt; &lt;name&gt; &lt;interval&gt; &lt;endYMDhms&gt;</code><br />
<em>Show twilight passes of a satellite.</em><br />
  The file must be in NORAD TLE format, usually obtained from
  <a href="http://www.celestrak.com">http://www.celestrak.com</a>.
  The name of the satellite is searched for in the file, orbital
  elements are read, ephemeris calculated at the given intervals
  (in seconds) from the present clock setting to the given end time.
  Any periods of the satellite being sunlit and above the horizon while the
  Sun is at least 6&deg; below the horizon, such periods are reported.</dd>
<dt>iridium/flare</dt>
<dd><code>iridium/flare &lt;file&gt; &lt;interval&gt; &lt;endYMDhms&gt;</code><br />
<em>Scan for Iridium flares.</em><br />
  The file must be in NORAD TLE format and should contain exactly all
  Iridium satellites that are oriented properly so that their flares are
  predictable.  The file can usually be obtained from
  <a href="http://www.celestrak.com/NORAD/elements/iridium.txt">http://www.celestrak.com/NORAD/elements/iridium.txt</a>.
  The interval is the time step in seconds, the search starts at the
  currently set time and ends as given in Y-M-D-h:m:s format.  The output
  is simply a chronological list of instances when one of the statellites
  has one of its flare angles smaller than 2&deg;.</dd>
</dl>

<p><strong>Batch commands:</strong></p>

<dl>
<dt>h0001</dt>
<dd><code>h0001</code><br />
  This writes a few sentences of HTML about the current ephemeris of Mars
  to standard output.</dd>
<dt>h0002</dt>
<dd><code>h0002</code><br />
  This writes XHTML and WML regarding the planets' rise and set for the
  next night.  Observatory is Royal Observatory Edinburgh.</dd>
<dt>h0003</dt>
<dd><code>h0003</code><br />
  This writes XHTML and WML regarding the twilight passes of ISS/Zarya for
  the next night.  Observatory is Royal Observatory Edinburgh.</dd>
<dt>h0004</dt>
<dd><code>h0004</code><br />
  This writes XHTML and WML regarding the Iridium flares for
  the next night.  Observatory is Royal Observatory Edinburgh.</dd>
</dl>

<h2><a name="classes" id="classes">An overview of the class hierarchy</a></h2>

<h3>Objects in the universe</h3>

<p>A large part of the class hierarchy deals with various type of objects in
the universe.  The base class there is {@link Catalog Catalog}.
It can store a number of positions and it has methods to convert between
different coordinate systems.  For some of these transforms it will need
instances of {@link Times Times} or {@link Station Station}.</p>

<p>The {@link NamedObject NamedObject} class extends (or put
another way, specialises and restricts) the <code>Catalog</code> class.
There can be only one position in an instance of this class, but on the
other hand there is also an object name.  <code>NamedObject</code> does not
have many methods of its own.  One of its major roles is to construct more
powerful and specialised subclasses from it.  To these it does, however,
provide a number of constants.</p>

<p>The {@link Comet Comet} class extends
<code>NamedObject</code> to perform the orbit integration and magnitude
calculation for a comet, but also for an asteroid.  Its public interface is
also a prototype for other objects of the Solar System.  An instance of
<code>Comet</code> is configured by reading orbital elements from a file
that lists such elements for many comets or asteroids.  In this respect the
<code>Comet</code> class is prototypical for other objects, such as
artificial satellites in Earth orbit.</p>

<p>The {@link VSOP87 VSOP87} class extends
<code>NamedObject</code> to calculate ephemeris using the VSOP87 model.
This covers the heliocentric positions of the planets (Mercury to Neptune).
Since this includes the Earth, it also covers the geocentric positions of
those planets and the Sun.  <code>VSOP87</code> is indeed an abstract class
and can only be instantiated as an instance of a subclass.  But this class
does provide methods common to all these subclasses.</p>

<p>The {@link Sun Sun},
{@link Mercury Mercury},
{@link Venus Venus},
{@link Mars Mars},
{@link Jupiter Jupiter},
{@link Saturn Saturn},
{@link Uranus Uranus} and
{@link Neptune Neptune},
classes extend <code>VSOP87</code> to perform calcluations for the Sun and
planets.</p>

<p>The
{@link Pluto Pluto} and
{@link Moon Moon} classes operate similar to <code>Comet</code>
and are extensions directly of <code>NamedObject</code>.  The
{@link Satellite Satellite} class is also an extension of
<code>NamedObject</code>.  This forms the glue between the application in
general and its interface on one side and the
{@link uk.me.chiandh.Lib.SDP4 SDP4}
class that is a fairly literal port of the satellite orbit models SDP4 and
SGP4.</p>

<h3>Objects for the observatory</h3>

<p>A second part of the class hierarchy deals with the observatory from
which the objects in the universe are observed.  The base class here is
{@link Times Times}, which provides for the storage of time and
the conversion between time scales.  This includes the difference between
Universal Time and Terrestrial Time (Ephemeris Time) and also includes
Greenwich Sidereal Time.</p>

<p>{@link Station Station} extends the <code>Times</code> class
to account for the observatory's location on the surface of the Earth.  This
includes longitude, latitude, local sidereal time and the shape of the
Earth.</p>

<p>{@link Telescope Telescope} extends the <code>Station</code>
class to include the viewing direction toward the sky.  It achieves this by
being a subclass of <code>Station</code> and by owning an instance of {@link
NamedObject NamedObject} and an instance of {@link
Sun Sun}.  The <code>NamedObject</code> represents the direction
we are looking at.  The <code>Sun</code> is of general use: In the same way
that we need to reduce from the observatory (topocentre) to the Earth
(geocentre), we need to reduce from the Earth to the Sun (heliocentre).</p>

<h3>Other classes</h3>

<p>There are a number of sub-classes of <code>Exception</code> to handle
failures.</p>

<p>{@link uk.me.chiandh.Lib.Hmelib Hmelib} is a class to provide all sorts
of little methods and a few constants that are so useful in so many
circumstances.  It helps with parsing and formatting, but also with
mathematics and spherical trigonometry.</p>

<p>{@link SputnikUI SputnikUI}, binds everything together and provides
the application main routine. <code>SputnikUI</code> has an
instance of {@link Telescope Telescope}, which it uses for all
calculations that pertain to the "current" time and observatory: The user
sets the time and place and asks questions about objects in the
universe.</p>

<h3>Packages</h3>

<p>There are two packages.  The <code>uk.me.chiandh.Sputnik</code> package is
for all classes specific to this application.  This is the bulk of classes.</p>

<p>The <code>uk.me.chiandh.Lib</code> package collects classes that could be
useful in other applications.  <code>Hmelib</code> is there.  But also
<code>SDP4</code>.  <code>SDP4</code> and <code>Satellite</code> are
separate to distinguish the port of the SDP4/SGP4 model proper from how
this application makes use of it.  It would be possible to give the
<code>SDP4</code> to other Java programmers; it depends only on
<code>java.lang</code>, <code>java.io</code> and <code>java.text</code>.</p>

<h2><a name="todo" id="todo">To do</a></h2>

<ol>
  <li>The lunar phases are defined incorrectly in Sputnik 1.9.
    Half Moon is when the eclipitical longitude is 90 degrees from the Sun.
    This is neither the elongation nor the phase angle.  Sputnik 2.1 does not
    calculate lunar phases.</li>
</ol>

<h2><a name="maint" id="maint">Source maintenance and build process</a></h2>

<p>This is the file tree:</p>

<pre>
Sputnik3/                       Top level directory.

  ReadMe.html                   Where the user starts to read.
  chiandh80.gif                 Logo for ReadMe.html
  COPYRIGHT                     Copyright note for my code and for code used.
  LICENCE                       GNU GPL text.
  VERSION                       Software version number.
  Manifest                      This identifies the main class.
  Makefile                      Interface to building the software.

  Sputnik.jar                   The Java executable.
  Sputnik-source.tgz            The source code.
  Sputnik-doc.tgz               The documentation.

  jdoc/                         Target tree for javadoc command.
  jadd/                         Snippets to copy into jdoc after running
                                javadoc (e.g. figures for img tags).
  doc/                          Non-javadoc documentation.

  uk/me/chiandh/                Tree containing the Java packages.

    Sputnik/                    Package with application specific classes.
                                This excludes the SDP4 class.
      *.java                    Java source code

    Lib/                        Package with generally useful classes.
                                This includes the SDP4 class.
      *.java                    Java source code
</pre>

<p>The Makefile can be used to build the distributable jars and tar balls.
The Java source code can be compiled with</p>

<pre>
make classes
</pre>

<p>This will re-compile all Java source code, irrespective of how little
has changed in the source. The jar that is the binary distribution of the
application can be created with (this includes re-compiling as above):</p>

<pre>
make jar
</pre>

<p>The documentation that is embedded in the Java source code can be generated
and the tar ball built from it with</p>

<pre>
make docs
</pre>

<p>And finally, for a source distribution the tar ball is made with</p>

<pre>
make source
</pre>

<hr />

<p><strong>Copyright:</strong> &copy; 2006-2009 Horst Meyerdierks.</p>

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
<dt><strong>2006-11-12:</strong> hme</dt>
<dd>New class. This class merges the functions of the previous Applic and
  Sputnik classes. In this first version it can run the CLI or the GUI with
  a few basic commands.</dd>
<dt><strong>2008-08-07:</strong> hme</dt>
<dd>Minor fixes of output formatting. Complete the port of Sputnik 2.1.3.
  Add the JLine and --command interfaces.<br />
  Version 3.0.2.</dd>
<dt><strong>2008-08-08:</strong> hme</dt>
<dd>Port the Sputnik*Exception classes and report command syntax errors.<br />
  Version 3.0.3.</dd>
<dt><strong>2008-12-17:</strong> hme</dt>
<dd>Calculate HJD correction for distant objects.<br />
  Version 3.0.4.</dd>
<dt><strong>2009-01-26:</strong> hme</dt>
<dd>Version 3.0.7.</dd>
<dt><strong>2009-01-27:</strong> hme</dt>
<dd>Calculate two or three rotation systems for Jupiter and Saturn.<br />
  Remove the GUI.<br />
  Revert physical ephemeris from EOD to J2000. Sun, planets and Pluto
  now again use geocentric J2000 for this, the Moon uses topocentric
  J2000. Also fix a long-standing typo in Neptune's calculation of
  rotation ephemeris.<br />
  Improve the linear fit to DeltaT for the present.<br />
  Version 3.1.</dd>
</dl>

@author
  Horst Meyerdierks, http://www.chiandh.me.uk

@see Telescope
 */

public class SputnikUI {


  /**
Version string.

<p>This string is used to store the current version of the software.</p>
   */

  static final String VERSION = "3.1";



  /**
The observatory, clock and viewing direction.

<p>This instance of the {@link Telescope Telescope} class is
the holder for information about the principal observing station.  This is
a location on Earth plus a clock plus a position on the sky we are looking
at.  It also includes a Sun for coordinate and velocity reduction.</p>
   */

  private Telescope itsTelescope;


  /**
Run the application.

<p>This runs the application. This will
instantiate the SputnikUI class (this class itself) in order to call its
runStdin method.</p>

@param args
  Command line arguments.
   */

  public static void main(String args[])
  {
    SputnikUI theUI;
    String theCommand = "";
    boolean useJLine = false;
    boolean singleCommand = false;
    int     i;
    
    System.out.println("hey joe");

    /* See which command flags are set. */

    for (i = 0; i < args.length; i++) {
      if (args[i].equals("--jline")) useJLine = true;
      if (args[i].equals("--command")) {
	singleCommand = true; i++; theCommand = args[i];
      }
    }

    theUI = new SputnikUI();

    /* If we run a single command and quit, do that. */

    if (singleCommand) {theUI.runOne(theCommand);}

    /* Else if we run with JLine, run that. */


    /* Else we run a simple user interface on stdin/stdout. */

    else {theUI.runStdin();}

  }


  /**
Instantiate the SputnikUI class.

<p>The appliation needs to be instantiated so that its non-static methods
can be used.</p>
   */

  public SputnikUI()
  {

    /* We need a telescope to do astronomy. */

    itsTelescope = new Telescope();
    itsTelescope.Init();

  }


  /**
Run the command line interface on stdin/stdout.

<p>This is the endless loop of reading a command from stdin, calling the
CommandAction method, and copying its output to stdout.</p>
   */

  private void runStdin()
  {
    String theCommand;
    String theOutput;
    BufferedReader stdin = null;
    boolean goon;

    try {
      stdin = new BufferedReader(new InputStreamReader(System.in));
    }
    catch(Exception e) {
      System.err.println("Sputnik: " + e);
      return;
    }

    System.out.print("\nSputnik " + VERSION + "\n\n"
        + "(C) 2002-2009 Horst Meyerdierks.\n\n"
	+ "This programme is free software; you can redistribute it and/or\n"
        + "modify it under the terms of the GNU General Public Licence as\n"
        + "published by the Free Software Foundation; either version 2 of\n"
        + "the Licence, or (at your option) any later version.\n\n"
        + "This programme is distributed in the hope that it will be useful,\n"
        + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
        + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\n"
        + "GNU General Public Licence for more details.\n\n"
        + "You should have received a copy of the GNU General Public Licence\n"
        + "along with this programme; if not, write to the Free Software\n"
        + "Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.\n\n");

    for (goon = true; goon; ) {

      /* Prompt for and read a command. */

      System.out.print("Sputnik> ");
      try {
	if ((theCommand = stdin.readLine()) == null) {
	  goon = false; continue;
	}
      }
      catch(Exception e) {
	System.err.println("Sputnik: " + e);
	continue;
      }
      theCommand = theCommand.trim();

      /* If we have a comment, ignore it. */

      if (theCommand.startsWith("#")) {continue;}

      /* Act upon the command. */

      theOutput = CommandAction(theCommand);
      if (! theOutput.equals("")) System.out.print(theOutput);
    }
  }


  /**
Run the command line interface on stdin/stdout with JLine.

<p>This is the endless loop of reading a command from stdin, calling the
CommandAction method, and copying its output to stdout. The difference to
the runStdin method is that JLine is used to permit command line recall
and editing.</p>
   */

 

  /**
Act upon a command.

<p>Given a command, this method will decide which
method of which class needs to be called to act upon the command.  The method
returns a string - that may be empty - with any response calculated by the
command action.</p>

@param theCommand
  The command to execute.
   */

  private String CommandAction(String theCommand)
  {
    String theOutput = "";

    try {

      /* General commands. */

      if (theCommand.startsWith("quit")) {
        System.exit(0);
      }
      else if (theCommand.startsWith("help")) {
        theOutput = Help(theCommand);
      }
      else if (theCommand.startsWith("file/macro ")) {
	theOutput = runFile((theCommand.substring(11)).trim());
      }

      /* Regular commands.
       * For commands that require parameters, include the space character
       * here.  That will avoid a string index bound error one level down and
       * allow any problem to be caught by the attempt to read the parameter.
       * For parameter-less commands, we must not search for a space bar behind
       * the command, of course. */

      else if (theCommand.startsWith("station/show")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("object/show")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/sun")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/mercury")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/venus")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/mars")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/jupiter")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/saturn")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/uranus")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/neptune")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("planet/show/pluto")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }

      else if (theCommand.startsWith("planet/limb/sun ")) {
        theOutput = Geometry.LimbToCentre(theCommand);
      }
      else if (theCommand.startsWith("planet/coord/sun ")) {
        theOutput = itsTelescope.Heliograph(theCommand);
      }
      else if (theCommand.startsWith("moon/limb ")) {
        theOutput = Geometry.LimbToCentre(theCommand);
      }
      else if (theCommand.startsWith("moon/coord ")) {
        theOutput = itsTelescope.Selenograph(theCommand);
      }

      else if (theCommand.startsWith("moon/show")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("comet/show ")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("asteroid/show ")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("satellite/show ")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("satellite/all ")) {
        theOutput = itsTelescope.CommandShow(theCommand);
      }
      else if (theCommand.startsWith("satellite/pass ")) {
        theOutput = itsTelescope.CommandSatPass(theCommand);
      }
      else if (theCommand.startsWith("iridium/flare ")) {
        theOutput = itsTelescope.CommandIridium(theCommand);
      }

      else if (theCommand.startsWith("station/twilight")) {
        theOutput = itsTelescope.CommandDawn();
      }
      else if (theCommand.startsWith("object/rise")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/sun")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/mercury")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/venus")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/mars")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/jupiter")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/saturn")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/uranus")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/neptune")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/rise/pluto")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("planet/au/venus")) {
        theOutput = itsTelescope.VenusTransit(theCommand);
      }
      else if (theCommand.startsWith("moon/rise")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("comet/rise ")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }
      else if (theCommand.startsWith("asteroid/rise ")) {
        theOutput = itsTelescope.CommandRise(theCommand);
      }

      else if (theCommand.startsWith("station/ut ")) {
        itsTelescope.CommandTime(theCommand);
      }
      else if (theCommand.startsWith("station/tt ")) {
        itsTelescope.CommandTime(theCommand);
      }
      else if (theCommand.startsWith("station/jd ")) {
        itsTelescope.CommandTime(theCommand);
      }
      else if (theCommand.startsWith("station/sys")) {
        itsTelescope.CommandTime(theCommand);
      }
      else if (theCommand.startsWith("station/read ")) {
        itsTelescope.CommandRead(theCommand);
      }
      else if (theCommand.startsWith("object/gal ")) {
        itsTelescope.CommandSetObject(theCommand);
      }
      else if (theCommand.startsWith("object/B1950 ")) {
        itsTelescope.CommandSetObject(theCommand);
      }
      else if (theCommand.startsWith("object/J2000 ")) {
        itsTelescope.CommandSetObject(theCommand);
      }
      else if (theCommand.startsWith("object/mean ")) {
        itsTelescope.CommandSetObject(theCommand);
      }
      else if (theCommand.startsWith("object/ecl ")) {
        itsTelescope.CommandSetObject(theCommand);
      }
      else if (theCommand.startsWith("object/topo ")) {
        itsTelescope.CommandSetObject(theCommand);
      }
      else if (theCommand.startsWith("object/hori ")) {
        itsTelescope.CommandSetObject(theCommand);
      }

      /* Batch commands. */

      else if (theCommand.startsWith("h0001")) {
        itsTelescope.h0001(theCommand);
      }
      else if (theCommand.startsWith("h0002")) {
        itsTelescope.h0002(theCommand);
      }
      else if (theCommand.startsWith("h0003")) {
        itsTelescope.h0003(theCommand);
      }
      else if (theCommand.startsWith("h0004")) {
        itsTelescope.h0004(theCommand);
      }

      /* Unrecognised command. */

      else {
        throw new SputnikInvCommException("unrecognised command");
      }
    }
    catch(Exception e) {
      theOutput = "Sputnik: " + e + "\n";
    }

    return theOutput;
  }


  /**
Serve the <code>help</code> command.

<p>This lists valid commands.</p>

@param theCommand
  The command to execute.
   */

  private String Help(String theCommand)
  {
    final String commands[] = {
      "help                 Display help or usage text",
      "help [<command>]",
      "quit                 Quit the application",
      "quit",
      "file/macro           Execute a script of commands",
      "file/macro <file>",
      "station/show         Show observatory position and clock",
      "station/show",
      "station/twilight     Show dusk and dawn",
      "station/twilight",
      "station/ut           Set observatory clock with UT",
      "station/ut <YMDhms>",
      "station/tt           Set observatory clock with TT",
      "station/tt <YMDhms>",
      "station/jd           Set observatory clock with JD",
      "station/jd <JD>",
      "station/sys          Set observatory clock from the system clock",
      "station/sys",
      "station/read         Read observatory position from file",
      "station/read <file> <station>",
      "object/show          Show coordinates of the object",
      "object/show",
      "object/rise          Show next rise and set of the object",
      "object/rise",
      "object/gal           Set galactic coordinates of the object",
      "object/gal <lII> <bII> <dist> <name>",
      "object/B1950         Set B1950 coordinates of the object",
      "object/B1950 <RA> <Dec> <dist> <name>",
      "object/J2000         Set J2000 coordinates of the object",
      "object/J2000 <RA> <Dec> <dist> <name>",
      "object/mean          Set equinox of date coordinates of the object",
      "object/mean <RA> <Dec> <dist> <name>",
      "object/ecl           Set ecliptic coordinates of the object",
      "object/ecl <lam> <bet> <dist> <name>",
      "object/topo          Set topocentric HA/Dec coordinates of the object",
      "object/topo <HA> <Dec> <dist> <name>",
      "object/hori          Set horizontal coordinates of the object",
      "object/hori <A> <h> <dist> <name>",
      "planet/show/sun      Show ephemeris of the Sun",
      "planet/show/sun",
      "planet/show/<planet> Show ephemeris of a planet",
      "planet/show/<planet>",
      "planet/rise/sun      Show next rise and set of the Sun",
      "planet/rise/sun",
      "planet/limb/sun      Find centre and radius from three limb points",
      "planet/limb/sun <x1> <y1> <x2> <y2> <x3> <y3>",
      "planet/coord/sun     Calculate heliographic coordinates",
      "planet/coord/sun <x0> <y0> <r> <q> <x> <y>",
      "planet/rise/<planet> Show next rise and set of a planet",
      "planet/rise/<planet>",
      "planet/au/venus      Help to calculate AU from Venus transit",
      "planet/au/venus",
      "moon/show            Show ephemeris of the Moon",
      "moon/show",
      "moon/rise            Show next rise and set of the Moon",
      "moon/rise",
      "moon/limb            Find centre and radius from three limb points",
      "moon/limb <x1> <y1> <x2> <y2> <x3> <y3>",
      "moon/coord           Calculate selenographic coordinates",
      "moon/coord <x0> <y0> <r> <q> <x> <y>",
      "comet/show           Show ephemeris of a comet or asteroid",
      "comet/show <file> <name>",
      "comet/rise           Show next rise and set of a comet or asteroid",
      "comet/rise <file> <name>",
      "asteroid/show        Show ephemeris of a comet or asteroid",
      "asteroid/show <file> <name>",
      "asteroid/rise        Show next rise and set of a comet or asteroid",
      "asteroid/rise <file> <name>",
      "satellite/show       Show ephemeris of a satellite",
      "satellite/show <file> <name>",
      "satellite/all        Show ephemeris of many satellites",
      "satellite/all <file>",
      "satellite/pass       Show passes of a satellite",
      "satellite/pass <file> <name> <interval> <end>",
      "iridium/flare        Scan for Iridium flares",
      "iridium/flare <file> <interval> <end>"
    };
    String theOutput = "";
    int i;

    /* If no parameter given, print full list. */

    if (theCommand.equals("help")) {
      for (i = 0; i < commands.length; i += 2)
        theOutput = theOutput + commands[i] + "\n";
    }

    /* Else (partial or full command name given),
     * print usage for matching commands. */

    else {
      for (i = 1; i < commands.length; i += 2) {
	if ((commands[i]).startsWith((theCommand.substring(4)).trim()))
	  theOutput = theOutput + commands[i] + "\n";
      }
    }

    return "\n" + theOutput + "\n";
  }


  /**
Serve the <code>file/macro</code> command.

<p>This opens, reads and closes the named file.  After reading each line
it executes it as a command with parameters.</p>

@param aFile
  The name of the file to use.  */

  private String runFile(String aFile)
  {
    String theCommand;
    String theOutput = "";
    BufferedReader theFile;
    boolean goon;

    try {
      theFile = new BufferedReader(new FileReader(aFile));
    }
    catch(Exception e) {
      return "Sputnik: " + e;
    }

    /* Endless loop to read and act upon commands. */

    for (goon = true; goon; ) {

      /* Read a command line.  Any error is reported and ignored.  An EOF is
       * signalled by returning null.  In that case we break the loop. */

      try {
	if ((theCommand = theFile.readLine()) == null) {
	  goon = false; continue;
	}
      }
      catch(Exception e) {
	theOutput = theOutput + "Sputnik: " + e;
	continue;
      }

      /* Trim white space off the start and end.
       * If we have a comment, ignore it. */

      theCommand = theCommand.trim();
      if (theCommand.startsWith("#")) {continue;}

      /* We handle the quit command ourselves, as CommandAction would quit
       * the application with loss of all results. Intended is only to
       * quit the macro and return to the user interface. */

      if (theCommand.startsWith("quit")) {
	goon = false;
      }

      /* Act upon the command and sweep up its output. */

      else {
	theOutput = theOutput + CommandAction(theCommand);
      }

    }

    /* Pass the collected output back to the caller so that it will be
     * put out where the user interface requires. */

    return theOutput;

  }


  /**
Run one command.

<p>This is the equivalent of runStdin() for running one command only.</p> */

  public void runOne(String aCommand)
  {
    String theCommand;
    String theOutput;

    theCommand = aCommand.trim();
    try {
      theOutput = CommandAction(theCommand);
      if (! theOutput.equals("")) System.out.print(theOutput);
    }
    catch(Exception e) {
      System.err.println("Sputnik: " + e);
    }
    return;
  }

}
