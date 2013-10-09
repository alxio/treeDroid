package com.example.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class SceneRenderer implements Renderer {

	private PointF surfaceSize;
	private PointF offset;
	private Cone cone;
	private Context mContext;

	public SceneRenderer(Context ctx) {
		mContext = ctx;
		surfaceSize = new PointF();
		offset = new PointF();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		cone = new Cone(mContext, gl);
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		surfaceSize.set(width, height);
		gl.glViewport(0, 0, (int) surfaceSize.x, (int) surfaceSize.y);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-surfaceSize.x / 2, surfaceSize.x / 2,
				-surfaceSize.y / 2, surfaceSize.y / 2, 1, 3);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -2);
	}

	public void onDrawFrame(GL10 gl) {
		gl.glPushMatrix();

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glTranslatef(offset.x, offset.y, 0);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glScalef(10f, 10f, 1f);
		cone.draw(gl);

		gl.glPopMatrix();
	}

	public void move(float xDelta, float yDelta) {
		offset.x += xDelta;
		offset.y += yDelta;
		Log.d("TriangleRenderer", "offset=[" + offset.x + " ," + offset.y + "]");
	}
}