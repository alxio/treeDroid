package com.example.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public abstract class GraphicObject {
	protected abstract float[] getVertices();
	protected abstract float[] getTexCoords();
	protected int texResId;
	protected int vertexCount;
	
	private int[] textures = new int[1];
	private FloatBuffer verticesBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer buffer;

	protected void init(Context ctx, GL10 gl) {
		Log.d("VERT",getVertices().toString());
		buffer = ByteBuffer.allocateDirect(getVertices().length * 4);
		buffer.order(ByteOrder.nativeOrder());
		verticesBuffer = buffer.asFloatBuffer();
		verticesBuffer.put(getVertices());
		verticesBuffer.position(0);

		Log.d("VERT",getTexCoords().toString());
		buffer = ByteBuffer.allocateDirect(getTexCoords().length * 4);
		buffer.order(ByteOrder.nativeOrder());
		textureBuffer = buffer.asFloatBuffer();
		textureBuffer.put(getTexCoords());
		textureBuffer.position(0);
		loadTexture(gl, ctx);
	}

	public void loadTexture(GL10 gl, Context context) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				texResId);

		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}

	public void draw(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glFrontFace(GL10.GL_CW);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertexCount);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
}
