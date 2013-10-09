package com.example.opengl;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Leaf extends GraphicObject{
	public Leaf(Context ctx, GL10 gl) {
		vertexCount=12;
		texResId = R.drawable.leaf;
		init(ctx, gl);
	}
	@Override
	protected float[] getVertices() {
		return vertices;
	}
	@Override
	protected float[] getTexCoords() {
		return texCoords;
	}
	float vertices[] = {
		-1,3,0,
		-1,0,0,
		1,0,0,

		-1,3,0,
		1,3,0,
		1,0,0,

		-1,3,0,
		-1,0,0,
		1,0,0,

		-1,3,0,
		1,3,0,
		1,0,0
	};
	float texCoords[] = {
		1,0,
		1,1,
		0,1,
		1,0,
		0,0,
		0,1,

		1,0,
		1,1,
		0,1,
		1,0,
		0,0,
		0,1,
	};
}