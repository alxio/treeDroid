package com.example.opengl;

import android.app.Activity;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
 
public class MainActivity extends Activity {
 
  private PointF mTouchStart;
  private GLSurfaceView mGlView;  
  private SceneRenderer mRenderer;
 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    mTouchStart = new PointF();
    mRenderer = new SceneRenderer(this);
    
	mGlView = (GLSurfaceView)findViewById(R.id.glSurfView);
	mGlView.setRenderer(mRenderer);
	mGlView.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
		    switch (event.getAction()) {
		      case MotionEvent.ACTION_MOVE:
		        mRenderer.move(event.getX() - mTouchStart.x, mTouchStart.y - event.getY());
		        mTouchStart.set(event.getX(), event.getY());
		        mGlView.requestRender();
		        return true;
		      case MotionEvent.ACTION_DOWN:
		        mTouchStart.set(event.getX(), event.getY());
		        return true;
		      default:
		        return false;
		    }
		}
	});
  }
 
  @Override
  protected void onResume() {
    super.onResume();
    mGlView.onResume();
  }
 
  @Override
  protected void onPause() {
    super.onPause();
    mGlView.onPause();
  }
}