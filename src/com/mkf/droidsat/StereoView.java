package com.mkf.droidsat;
/*<p>This programme is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public Licence for more details.</p>

<p>You should have received a copy of the GNU General Public Licence
along with this programme; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.</p>
*/

import com.mkf.droidsat.R;

import uk.me.chiandh.Sputnik.SatellitePosition;
import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.view.*;
import android.util.AttributeSet;
import android.util.Log;
import android.content.res.Resources;

public class StereoView extends View {

	private float heading;
	private float pitch;
	private float roll;
	private float headingRadians;
	private float pitchRadians;
	public static Paint textPaint;
	public static Paint satPaint;
	private Paint targetPaint;
	public static Paint sunlitPaint;
	public static Paint notSunlitPaint;
	public static Paint latLonPaint;
	public static int textHeight;
	private String target;
	public static int semiWidthDegrees = 90;
	public static int semiHeightDegrees = 45;
	private int reticleRadius = 15;
	private int targetRadius = 1;
	static  volatile double projectionRadius = 480;
	static  volatile double prevProjectionRadius = 0;
	public volatile boolean updatingDisplay=false;
	public static volatile int gridSizeDegrees=5;
	public volatile static int lonDisplayDegrees = 30;
	public volatile static int latDisplayDegrees = 30;
	public volatile static int segmentsPerLine=3;
	public volatile static int lonIncrement = lonDisplayDegrees/gridSizeDegrees;
	public volatile static int latIncrement = latDisplayDegrees/gridSizeDegrees;
	private static volatile double cosTheta1;
	private static volatile double sinTheta1;
	
	
	private static double[] radianLons = new double [360/gridSizeDegrees+1];
	private static double[] radianLats = new double [180/gridSizeDegrees+1];
	private static String[] lonLabels = new String [360/gridSizeDegrees+1];
	private static String[] latLabels = new String [180/gridSizeDegrees+1];
	
	public static volatile boolean displayAltAzGrid = true;
	public static volatile boolean displayDarkSats = true;
	public static volatile boolean displayLowSats = true;

	public static volatile int width;
	public static volatile int height;
	

	private Coord stereoCoord=new Coord();
	private Coord prevCoord=new Coord();
	
	private float trackballX = -100;
	private float trackballY = -100;
	
	public static volatile int trackballSpeed = 2;
	public static volatile float textSize = 16;
	
	

	
	private class Coord {
		float x;
		float y;
	}
	
	
	

	
	public void setHeading(float _heading) {
		if (!ShowSatellites.orientationLocked){
			heading = _heading;
			headingRadians =(float)Math.toRadians(heading);
			
		}
	}

	public float getHeading() {
		return heading;
	}

	public void setPitch(float _pitch) {
		if (!ShowSatellites.orientationLocked){
			pitch = _pitch * -1 - 90;
			if (pitch > 90 || pitch < -90){
				pitch = pitch - (pitch - 90);
			}
			pitchRadians = (float) Math.toRadians(pitch);
			cosTheta1 = Math.cos(pitchRadians);
			sinTheta1 = Math.sin(pitchRadians);
		}
	}

	public float getPitch() {
		return pitch;
	}

	public void setRoll(float _roll) {
		if (!ShowSatellites.orientationLocked)
			if (Math.abs(_roll)< 20)
				roll = _roll;
	}

	public float getRoll() {
		return roll;
	}

	public StereoView(Context context) {
		super(context);
		initStereoView();
	}

	public StereoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initStereoView();
	}

	public StereoView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		initStereoView();
	}

	protected void initStereoView() {
		setFocusable(true);
		


		Resources r = this.getResources();
		

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(r.getColor(R.color.text_color));
		textPaint.setFakeBoldText(true);
		textPaint.setSubpixelText(true);
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setTextSize(16);

		textHeight = (int) textPaint.measureText("yY");

		satPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		satPaint.setColor(r.getColor(R.color.text_color));
		satPaint.setStyle(Paint.Style.STROKE);
		satPaint.setFakeBoldText(true);
		satPaint.setSubpixelText(true);
		satPaint.setTextAlign(Align.LEFT);
 		
		latLonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		latLonPaint.setColor(r.getColor(R.color.text_color));
		latLonPaint.setStyle(Paint.Style.STROKE);
		latLonPaint.setFakeBoldText(true);
		latLonPaint.setSubpixelText(true);
		latLonPaint.setTextAlign(Align.LEFT);

		sunlitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		sunlitPaint.setColor(r.getColor(R.color.text_color));
		sunlitPaint.setStyle(Paint.Style.FILL);
		sunlitPaint.setFakeBoldText(true);
		sunlitPaint.setSubpixelText(true);
		sunlitPaint.setTextAlign(Align.LEFT);

		notSunlitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		notSunlitPaint.setColor(r.getColor(R.color.text_color));
		notSunlitPaint.setStyle(Paint.Style.STROKE);
		notSunlitPaint.setFakeBoldText(true);
		notSunlitPaint.setSubpixelText(true);
		notSunlitPaint.setTextAlign(Align.LEFT);
		
		int j = 0;
		
		for (int i = 0; i <= 360; i +=gridSizeDegrees){
			radianLons[j] = Math.toRadians(i);
			if (i==0){
				lonLabels[j] = "N";
			}
			else if (i==90){
				lonLabels[j] = "E";
			}
			else if (i==180){
				lonLabels[j] = "S";
			}
			else if (i==270){
				lonLabels[j] = "W";
			}
			else if (i==360){
				lonLabels[j] = "";
			}
			else{
				lonLabels[j] = String.valueOf(i);
			}
			j++;
		}

		j = 0;
		for (int i = -90; i <= 90; i +=gridSizeDegrees){
			radianLats[j] = Math.toRadians(i);
			latLabels[j] = String.valueOf(i);
			j++;
		}
		

	}
	
	@Override
	//StereoProjection of satellites.
	protected void onDraw(Canvas canvas) {

		// center points
		int displayHeight = getHeight();
		int displayWidth = getWidth();
		int px = displayWidth / 2;
		int py = displayHeight / 2;
		int i;

		if (textPaint.getTextSize() != textSize) {
			textPaint.setTextSize(textSize);
		}
		
		if (ShowSatellites.fullSky){
			StereoView.projectionRadius = displayWidth/4 - StereoView.textSize;
		}

		updatingDisplay = true;

		if (!ShowSatellites.loadingTle && 
				ShowSatellites.satellitePositions != null
				&& !ShowSatellites.satellitePositions.isEmpty())
		{

			i = -1;
			// need to check here
			
			try {
				for (SatellitePosition satPos : ShowSatellites.satellitePositions) {

					i++;

					if (!(satPos.elevation < 0 && (ShowSatellites.fullSky || !displayLowSats))
							&& !(satPos.sat.itsIsSunlit != 1 && !displayDarkSats)) {

						stereoProjRad(headingRadians, pitchRadians,
								satPos.azRadians, satPos.elRadians, stereoCoord);
						if (stereoCoord.x >= 0 && stereoCoord.x <= displayWidth
								&& stereoCoord.y >= 0
								&& stereoCoord.y <= displayHeight) {
							if ((Math.abs(trackballX - stereoCoord.x) <= reticleRadius)
									&& (Math.abs(trackballY - stereoCoord.y) <= reticleRadius)) {

								target = satPos.name;
								targetPaint = satPaint;
							}

							if (satPos.sat.itsIsSunlit == 1) {
								targetPaint = sunlitPaint;
							} else {
								targetPaint = notSunlitPaint;
							}
							if (satPos == ShowSatellites.selectedSatPosn) {
								targetRadius = 4;
							} else
								targetRadius = 2;
							if (satPos.elevation < 0) {
								targetRadius = 1;
							}

							canvas.drawCircle(stereoCoord.x, stereoCoord.y,
									targetRadius, targetPaint);

						}
					}

				}
			} catch (Exception e) {
				//Transitioning from large tle file to small file may cause a concurrent
				//modification exception. Disregard, it is infrequent, and transitory
				//with no side-effects
				Log.d(this.getClass().getName(), "Exception plotting satellite");
			}
		}
		if (ShowSatellites.loadingTle)
			canvas.drawText("Loading tle", px - 20, py - 20, textPaint);
		else {
			if (trackballX < -99 && trackballY < -99) {
				trackballX = px;
				trackballY = py;
			}
			canvas.drawCircle(trackballX, trackballY, reticleRadius,
					latLonPaint);
		}

		// canvas.rotate(-roll);
		canvas.drawText("Az " + (int) heading + " ("
				+ (int) ShowSatellites.magDeclination + ")", 0, 0 + textHeight,
				textPaint);
		canvas.drawText("El " + (int) pitch, 0, 0 + 2 * textHeight, textPaint);
		canvas.drawText("Sat " + target, 0, 0 + 3 * textHeight, textPaint);
		canvas.drawText("Loc " + " " + ShowSatellites.latitude + " "
				+ ShowSatellites.longitude, 0, 0 + 4 * textHeight, textPaint);
		canvas.drawText("File: " + ShowSatellites.selectedTle + " "
				+ ShowSatellites.selectedSpeed + "X", 0, 5 * textHeight,
				textPaint);
		if (ShowSatellites.gettingTles){
			canvas.drawText("Downloading celestrak.net, amsat.org", 0,
					0 + 6 * textHeight, textPaint);
			canvas.drawText("and m mccants tle files", 0, 0 + 7 * textHeight,
					textPaint);
		}

		drawHorizonLabels(canvas, displayHeight, displayWidth);
		drawMeridanLabels(canvas, displayHeight, displayWidth);

		if (displayAltAzGrid) {
			drawLats(canvas, displayHeight, displayWidth);
			drawLons(canvas, displayHeight, displayWidth);
		}

		updatingDisplay = false;

	}

	private void drawHorizonLabels(Canvas canvas, int displayHeight, int displayWidth) {

		//only drawing along the horizon
		for (int lon = 0; lon < radianLons.length; lon+= lonIncrement) {
			stereoProjRad(headingRadians,pitchRadians, radianLons[lon],0.0,stereoCoord);
			canvas.drawText(lonLabels[lon],stereoCoord.x, stereoCoord.y - textHeight, textPaint);
		}
	}
	
	private void drawMeridanLabels(Canvas canvas, int displayHeight, int displayWidth) {
		
		//only draw along the cardinal points - lonIncrement will be 90/gridSizeDegrees
		//skip drawing on horizon (0 elevation)
		int startLat = 0;
		int lonIncrement90 = 90/gridSizeDegrees;
		int horizonLat = (180/gridSizeDegrees)/2;
		if (ShowSatellites.fullSky) {
			startLat = horizonLat;
		}
		
		for (int lat = startLat; lat < radianLats.length; lat+= latIncrement) {
			for (int lon = 0; lon < radianLons.length; lon+= lonIncrement90) {
				if (lat != horizonLat) {
					stereoProjRad(headingRadians, pitchRadians,
							radianLons[lon], radianLats[lat], stereoCoord);
					canvas.drawText(latLabels[lat], stereoCoord.x,
							stereoCoord.y - textHeight, textPaint);
				}
			}
		}

	}

	private void drawLats(Canvas canvas, int displayHeight, int displayWidth) {

		int startLat = 0;
		if (ShowSatellites.fullSky) {
			startLat = (180/gridSizeDegrees)/2;
		}

		for (int lat = startLat; lat < radianLats.length; lat+= latIncrement) {
			for (int lon = 0; lon < radianLons.length; lon+= lonIncrement/segmentsPerLine) {
				if (lon != 0) {
					prevCoord.x = stereoCoord.x;
					prevCoord.y = stereoCoord.y;
				}
				stereoProjRad(headingRadians, pitchRadians, radianLons[lon],
						radianLats[lat], stereoCoord);
				if (lon != 0) {
					if (((stereoCoord.x >= 0 && stereoCoord.x <=displayWidth) ||( prevCoord.x >= 0 && prevCoord.x <= displayWidth))
							&& ((stereoCoord.y >= 0 && stereoCoord.y <=displayHeight) || (prevCoord.y >= 0 && prevCoord.y <= displayHeight))) {
						canvas.drawLine(prevCoord.x, prevCoord.y,
								stereoCoord.x, stereoCoord.y, latLonPaint);
					}

				}

			}
		}
	}

	private void drawLons(Canvas canvas, int displayHeight, int displayWidth) {

		int startLat = 0;
		if (ShowSatellites.fullSky) {
			startLat = (180/gridSizeDegrees)/2;
		}

		for (int lon = 0; lon < radianLons.length; lon+= lonIncrement) {
			for (int lat = startLat; lat < radianLats.length; lat+= latIncrement/segmentsPerLine) {
				if (lat != startLat) {
					prevCoord.x = stereoCoord.x;
					prevCoord.y = stereoCoord.y;
				}
				stereoProjRad(headingRadians, pitchRadians, radianLons[lon],
						radianLats[lat], stereoCoord);
				if (lat != startLat) {
					if (((stereoCoord.x >= 0 && stereoCoord.x <=displayWidth) ||( prevCoord.x >= 0 && prevCoord.x <= displayWidth))
							&& ((stereoCoord.y >= 0 && stereoCoord.y <=displayHeight) || (prevCoord.y >= 0 && prevCoord.y <= displayHeight))) {
						canvas.drawLine(prevCoord.x, prevCoord.y,
								stereoCoord.x, stereoCoord.y, latLonPaint);
					}

				}

			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);


		setMeasuredDimension(measuredWidth, measuredHeight);

	}

	private int measure(int measureSpec) {

		int result = 0;

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = 200;
		} else {
			result = specSize;
		}

		return result;
	}
	
	/**
	 * @param lambda0 heading or longitude of centre of projection
	 * @param theta1  pitch or latitude of centre of projection
	 * @param lambda  azimuth or longitude of point to be projected
	 * @param theta   elevation or latitude of point to be projected
	 * @param coord   re-usable coord object to hold results.
	 */
	private void stereoProjRad(float lambda0, float theta1, double lambda,
			double theta, Coord coord) {

		
		if (heading == 0){
			heading = 0.001f;
		}
		if (pitch == 0){
			pitch  = 0.001f;
		}

		double k = (2 * projectionRadius)
				/ (1 + sinTheta1 * Math.sin(theta) + cosTheta1
						* Math.cos(theta) * Math.cos(lambda - lambda0));

		coord.x = (float) (k * Math.cos(theta) * Math.sin(lambda - lambda0))
				+ getWidth() / 2;
		coord.y = (float) (k * (cosTheta1 * Math.sin(theta) - sinTheta1
				* Math.cos(theta) * Math.cos(lambda - lambda0)))
				* -1 + getHeight() / 2;

//		coord.y = (float) (k * (Math.cos(theta1) * Math.sin(theta) - Math
//				.sin(theta1)
//				* Math.cos(theta) * Math.cos(lambda - lambda0)))
//				+ getWidth() / 2;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event){
		
		float yDiff = event.getY() * event.getYPrecision() * trackballSpeed;
		float xDiff = event.getX() * event.getXPrecision() * trackballSpeed;
		
		switch (event.getAction()){
		case MotionEvent.ACTION_MOVE: 
			if (ShowSatellites.sensorOrientationOn) {
				trackballX += xDiff;
				trackballY += yDiff;
			}
			else{
				setHeading(getHeading()+ xDiff);
				setPitch(getPitch()*-1 + 90 +yDiff);
				
			}
			break;
		
		case MotionEvent.ACTION_UP: 
			if (ShowSatellites.sensorOrientationOn) {
				trackballX = getWidth() / 2;
				trackballY = getHeight() / 2;
			}
			else{
				
			}
			break;
		}
		
		
		return true;
		
	}
	

}
