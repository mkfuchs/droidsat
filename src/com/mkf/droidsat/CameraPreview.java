/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mkf.droidsat;


import java.lang.reflect.Method;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;


class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;
    volatile float viewAngle = 37.5f;
    volatile boolean inPreview = false;
    
    public CameraPreview(Context context) {
        super(context);
        
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.viewAngle = this.getViewAngle();
    }
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.viewAngle = this.getViewAngle();
	}
	public CameraPreview(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.viewAngle = this.getViewAngle();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		
		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
			
		} catch (Exception exception) {
			if (null != mCamera) {
				mCamera.release();
				this.inPreview = false;
				mCamera = null;
			}
		}

	}

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        this.turnOff();
        if (null != mCamera) {
			mCamera.release();
			mCamera = null;
		}
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        //Camera.Parameters parameters = mCamera.getParameters();
        if (ShowSatellites.video) {
			this.turnOff();
			
			if (null != mCamera){
				Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				int rotation = display.getOrientation();
				String orientation = getScreenOrientation();
				try {

					
					if (VERSION.SDK_INT < 8) { // Android 2.2 
						Camera.Parameters parameters = mCamera.getParameters();
						if (rotation == Surface.ROTATION_0 && orientation.equals("portrait")) {
							parameters.set("orientation", orientation);
							parameters.set("rotation", 90);
						}
						else if (rotation == Surface.ROTATION_270 && orientation.equals("landscape")) {
							parameters.set("orientation", orientation);
							parameters.set("rotation", 180);
						}
						mCamera.setParameters(parameters);
					} else {
						Method setDisplayOrientation = mCamera.getClass().getMethod("setDisplayOrientation", Integer.TYPE);
						if (rotation == Surface.ROTATION_0) {
							if (orientation.equals("portrait")){
								setDisplayOrientation.invoke(mCamera, 90);
							}
							else {
								setDisplayOrientation.invoke(mCamera, 0);
							}
							
						}
						else if (rotation == Surface.ROTATION_90) {
							if (orientation.equals("landscape")){
								setDisplayOrientation.invoke(mCamera, 0);
							}
							else{
								setDisplayOrientation.invoke(mCamera, 270);
							}
						}
						else if (rotation == Surface.ROTATION_270) {
							if (orientation.equals("landscape")){
								setDisplayOrientation.invoke(mCamera, 180);
							}
							else{
								setDisplayOrientation.invoke(mCamera, 90);
							}
						}
					}

				} catch (Exception exception) {
					mCamera.release();
					this.inPreview=false;
					mCamera = null;
				}
			}
			
			this.turnOn();
		}
		
    }
    
    public void turnOff(){
    	if (mCamera!=null && this.inPreview) {
			mCamera.stopPreview();
			this.inPreview=false;
		}
    }
    
    public void turnOn(){
    	if (mCamera!=null && !this.inPreview) {
			mCamera.startPreview();
			this.inPreview=true;
		}
    }
	public float getViewAngle() {
		float myViewAngle = 37.5f; //default value
		String angleMethod = "getHorizontalViewAngle";
		if (VERSION.SDK_INT >= 8){			
			try {
				
				Camera.Parameters parameters = mCamera.getParameters();
				Method getViewAngleMethod = parameters.getClass().getMethod(angleMethod);
				myViewAngle = (Float)getViewAngleMethod.invoke(parameters, null);
				
				
			} catch (Exception e) {
				Log.d(this.getClass().getName(),"Exception getting view angle");;
			}
		}
		if (myViewAngle > 10 && myViewAngle < 90)
			return myViewAngle;
		else
			return 37.5f;
	}
	
	private String getScreenOrientation(){
		Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		String orientation;
		boolean isPortrait = (display.getHeight() > display.getWidth());
		if (isPortrait){
			orientation = "portrait";
		}
		else{
			orientation = "landscape";
		}
		
		return orientation;
	}

}
