package com.example.opengl;

import java.util.Random;

import android.opengl.Matrix;

public abstract class AnimationControler {
	// Macierze
	float[] P = new float[16];// rzutowania
	float[] V = new float[16];// widoku
	float[] M = new float[16];// modelu

	// Ustawienia okna i rzutowania
	int windowPositionX = 50;
	int windowPositionY = 50;
	int windowWidth = 600;
	int windowHeight = 400;
	float cameraAngle = 45.0f;

	// logika drzewa
	int randIt = 0;
	int randTab[] = new int[1000];

	// <-max , max)
	float los(int max) {
		int f = randTab[randIt++];
		if (randIt == 1000)
			randIt = 0;
		return f % (2 * max) - max;
	}

	// <min,max)
	float los(float min, float max) {
		int f = randTab[randIt++];
		if (randIt == 1000)
			randIt = 0;
		return min + (max - min) * ((float) (f % 10000)) / 10000;
	}

	class treeSegment {
		int parent;
		float[] myM = new float[16];
		float[] realM = new float[16];
		float height;
		float width;
		float size;
		int level;
	}

	// Zmienne do animacji
	float animationSpeed = 1;
	float wspeed = 0.00003f;
	float hspeed = 0.001f;
	float lspeed = 0.0001f;
	float segmentSize = 1.5f;
	long currentTime = 0;
	long lastTime = 0;
	long startTime = 0;
	long pausedTime;
	boolean paused = false;
	treeSegment segments[] = new treeSegment[2000];
	treeSegment leafs[] = new treeSegment[2000];
	int segmentsCount = 0;
	int leafsCount = 0;
	int maxSize = 500;
	int maxLeaf = 1000;
	int growing = 1;
	int maxLevel = 4;
	float leafSize = 0.3f;
	float dayTime = 0;
	int dayLength = 40000;
	boolean leafDance = false;
	float lastDayTime = 0;
	boolean lampOn = false;
	boolean thunder = false;

	Vector3 sun;
	Vector3 lamp;
	Vector3 sky = new Vector3(0, 0, 0);
	float[] sunc = new float[4];
	float[] lampc = new float[4];

	// void recalcutateLights(){
	// if(dayTime<0.25)
	// sky.b = 0.5+2*dayTime;
	// else if(dayTime<0.75)
	// sky.b = 1.5-2*dayTime;
	// else
	// sky.b = 2*dayTime-1.5;
	//
	// sky.b = sky.b*sky.b;
	// sky.g = sky.b/2;
	// sky.r = 0;
	// if((dayTime>0.7 && lastDayTime<=0.7)||(dayTime>0.75 &&
	// lastDayTime<=0.75)||(dayTime>0.9 && lastDayTime<=0.9)||thunder){
	// thunder=0;
	// sky.r=sky.g=sky.b=1;
	// }
	//
	// sunc.r=sky.b;
	// sunc.g=sky.b;
	// sunc.b=sky.b;
	// sunc.a=1;
	//
	// sun.z = 100*sin(dayTime*2*PI);
	// sun.x = 0;
	// sun.y = 100*cos(dayTime*2*PI);
	//
	// lamp.x = 2*sin(dayTime*2*PI);
	// lamp.y = 2*cos(dayTime*2*PI);
	// lamp.z = 1;
	//
	// lampc.a=1;
	// lampc.r=lampc.g=lampc.b=0;
	// if(dayTime>0.5 && lastDayTime<=0.5)
	// lampOn=1;
	// if(dayTime<0.1 && lastDayTime>0.1)
	// lampOn=0;
	// if(lampOn){
	// lampc.r=1;
	// lampc.g=0.5;
	// }
	// lastDayTime=dayTime;
	// }

	abstract void drawObject(int id);

	abstract void drawCone();

	abstract void drawLeaf();

	abstract void drawBg();

	void initTree() {
		lastTime = 0;
		startTime = System.currentTimeMillis();
		Random r = new Random(startTime);
		for (int i = 0; i < 1000; i++)
			randTab[i] = r.nextInt();
		growing = 1;
		segmentsCount = 1;
		leafsCount = 0;
		treeSegment s = segments[0];
		s.parent = 0;
		s.myM = s.realM = Vector3.mat4(1.0f);
		s.height = 0.0f;
		s.width = 0.0f;
		s.size = 1.0f;
		s.level = 1;
	}

	void drawTree() {
		for (int i = 0; i < segmentsCount; i++) {
			treeSegment s = segments[i];
			Matrix.scaleM(M, 0, s.realM, 0, s.size * s.width, s.size * s.width,
					s.height * segmentSize * s.size);
			drawCone();
		}
		M = Vector3.mat4(1);
		Matrix.translateM(M, 0, 0, 0, 9);
		Matrix.scaleM(M, 0, 50, 50, 20);
		drawBg();
		for (int i = 0; i < leafsCount; i++) {
			treeSegment s = leafs[i];
			Matrix.scaleM(M, 0, s.realM, 0, s.size * s.height, s.size
					* s.height, 1);
			float angle = (float) (5 - (((currentTime / 100) % 10 > 5) ? (10 - (currentTime / 100) % 10)
					: ((currentTime / 100) % 10)));
			if (leafDance)
				Matrix.rotateM(M, 0, angle, 1, 0, 0);
			drawLeaf();
		}

	}

	void createLeaf(int parent, float size, float xAngle, float zAngle,
			float translation, int level) {
		if (leafsCount >= maxLeaf) {
			return;
		}
		treeSegment s = leafs[leafsCount++];
		s.parent = parent;
		s.level = level;
		s.myM = s.realM = Vector3.mat4(1.0f);
		Matrix.translateM(s.myM, 0, 0, 0, translation * segments[parent].size
				* segments[parent].height * segmentSize);
		Matrix.rotateM(s.myM, 0, zAngle, 0, 0, 1);
		Matrix.translateM(s.myM, 0, 0, segments[parent].size
				* segments[parent].width, 0);
		Matrix.rotateM(s.myM, 0, xAngle, 1, 0, 0);
		s.size = (float) (size * Math.sqrt(segments[parent].size));
		s.height = 0.0f;
		s.width = 0.0f;
	}

	void createLeaf(int i) {
		createLeaf(i, leafSize * los(0.9f, 1.1f), los(10f, 80f), los(180),
				los(0, 1), maxLevel + 1);
	}

	void createBranch(int parent, float size, float xAngle, float yAngle,
			int level) {
		if (segmentsCount >= maxSize) {
			growing = 2;
			return;
		}
		treeSegment s = segments[segmentsCount++];
		s.parent = parent;
		s.level = level;
		s.myM = s.realM = Vector3.mat4(1.0f);
		Matrix.translateM(s.myM, 0, 0, 0, 0.95f * segments[parent].size
				* segments[parent].height * segmentSize);
		Matrix.rotateM(s.myM, 0, xAngle, 1, 0, 0);

		Matrix.rotateM(s.myM, 0, yAngle, 0, 1, 0);
		s.size = size * segments[parent].size;
		s.height = 0.0f;
		s.width = 0.9f * segments[parent].width;
	}

	void recalculateTree() {
		if (segmentsCount == 0)
			return;
		if (growing == 0)
			return;
		float actTime = currentTime;
		float interval = (actTime - lastTime);
		lastTime = (long) actTime;
		// tylko liscie
		if (growing == 2) {
			for (int i = segmentsCount - 1; i >= 0; i--) {
				treeSegment s = segments[i];
				if (s.level == maxLevel)
					createLeaf(i);
			}
		} else {
			segments[0].width = wspeed * actTime;
			for (int i = 0; i < segmentsCount; i++) {
				treeSegment s = segments[i];
				if (i != 0)
					s.width = 0.9f * segments[s.parent].width;
				if (s.height < 1.0f) {
					s.height += hspeed * interval;
					if (s.height >= 1.0f) {
						if (s.level < maxLevel) {
							if (los(0, 10) < 9) {
								createBranch(i, 1, s.level * los(5), s.level
										* los(5), s.level);
								createBranch(i, los(0.4f, 0.7f), los(40),
										los(40), s.level + 1);
							} else {
								float a = los(10, 20);
								float b = los(0, 20);
								createBranch(i, 0.9f, a, b, s.level);
								createBranch(i, 0.9f, -a, -b, s.level);
							}
						} else if (s.level == maxLevel) {
							createLeaf(i);
						}
					}
				}
				Matrix.multiplyMM(s.realM, 0, segments[s.parent].realM, 0,
						s.myM, 0);
			}
		}
		int changed = 0;
		for (int i = 0; i < leafsCount; i++) {
			treeSegment s = leafs[i];
			// TODO s.realM = segments[s.parent].realM*s.myM;
			Matrix.multiplyMM(s.realM, 0, segments[s.parent].realM, 0, s.myM, 0);
			if (s.height < 1.0f) {
				s.height += lspeed * interval;
				changed = 1;
			}
		}
		if (changed == 0 && leafsCount == maxLeaf && segmentsCount == maxSize)
			growing = 0;
	}

	void pause() {
		if (paused)
			pausedTime += (System.currentTimeMillis() - startTime - pausedTime)
					* animationSpeed - lastTime;
		paused = !paused;
	}

}
