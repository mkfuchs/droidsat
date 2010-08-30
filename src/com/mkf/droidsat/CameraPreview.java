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


import android.content.Context;
import android.hardware.Camera;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.lang.reflect.Method;


class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;
    volatile float verticalViewAngle = 43;
    volatile boolean inPreview = false;
    
    public CameraPreview(Context context) {
        super(context);
        
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.verticalViewAngle = this.getVerticalViewAngle();
    }
	public CameraPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.verticalViewAngle = this.getVerticalViewAngle();
	}
	public CameraPreview(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.verticalViewAngle = this.getVerticalViewAngle();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		mCamera = Camera.open();
		try {

			if (VERSION.SDK_INT < 8) { // Android 2.2 
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.set("orientation", "portrait");
				parameters.set("rotation", 90);
				mCamera.setParameters(parameters);
			} else {
				Method setDisplayOrientation = mCamera.getClass().getMethod("setDisplayOrientation", Integer.TYPE);
				setDisplayOrientation.invoke(mCamera, 90);
			}
			mCamera.setPreviewDisplay(holder);
		} catch (Exception exception) {
			mCamera.release();
			this.inPreview=false;
			mCamera = null;
		}

	}

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        this.turnOff();
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setParameters(parameters);
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
	public float getVerticalViewAngle() {
		float viewAngle = 43; //default value
		if (VERSION.SDK_INT >= 8){
			
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				Method getVerticalViewAngleMethod = parameters.getClass().getMethod("getHorizontalViewAngle");
				viewAngle = (Float)getVerticalViewAngleMethod.invoke(parameters, null);
				
			} catch (Exception e) {
				Log.d(this.getClass().getName(),"Exception getting view angle");;
			}
		}
		return viewAngle;
	}

}
