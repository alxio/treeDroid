package com.example.opengl;


//TODO to nie fiszku
//if (c=='p' || c=='P') 
//    pause(); 
//if (c==' ') 
//    growing=0; 
//if (c=='n' || c=='N') 
//    initTree(); 
//if (c=='i' || c=='I') 
//    leafDance=!leafDance; 
//if ((c=='f' || c=='F')&&!growing) 
//    if(animationSpeed==1) 
//        animationSpeed=10; 
//    else
//        animationSpeed=1; 
//if ((c=='+')&&!growing) 
//    segmentSize+=0.1; 
//if ((c=='-')&&!growing) 
//    segmentSize-=0.1; 
//if (c=='l' || c=='L') 
//    lampOn=!lampOn; 
//if (c=='t' || c=='T') 
//    thunder = 1; 

public class Camera {
	public static final int FRONT = 16;
	public static final int BACK = 32;
	public static final int CLOCK = 64;
	public static final int CCLOCK = 128;
	public static final int SLEFT = 256;
	public static final int SRIGHT = 512;
	public static final int SUP = 1024;
	public static final int SDOWN = 2048;

	public void handleKeyDown(int key){
		camera|=key;
	}
	public void handleKeyUp(int key){
		camera&=~key;
	}
	public void handleMove(int x, int y){
		camX = x;
		camY = y;
	}
	
	// Mysz
	int mouseOn = 0;
	int lastX;
	int lastY;

	// Kamera
	int camX = 0;
	int camY = 0;
	int camera = 0;
	float camRot = 0.5f;
	float camMov = 0.2f;
	Vector3 camPos = new Vector3(-20.0f,0.0f,6.0f);
	Vector3 camLook = new Vector3(1.0f,0.0f,0.1f);
	Vector3 camNose = new Vector3(0.0f,0.0f,1.0f);
	
	
	//Obs³uga ruchu kamery 
	private void moveCamera(){ 
	    if(camX!=0){ 
//TODO	        camLook = glm::rotate(camLook,camRot*camX,camNose); 
	        camX=0; 
	    } 
	    if(camY!=0){ 
//TODO	        glm::vec3 axis = glm::cross(camLook,camNose); 
//TODO	        camLook = glm::rotate(camLook,camRot*camY,axis); 
//TODO	        camNose = glm::rotate(camNose,camRot*camY,axis); 
	        camY=0; 
	    }
	    if((camera&FRONT) != 0){ 
	        camPos.add(camLook.mult(camMov));
	    } 
	    if((camera&BACK) != 0){ 
	        camPos.sub(camLook.mult(camMov)); 
	    } 
	    if((camera&CLOCK) != 0){
	    	//TODO: camNose = glm::rotate(camNose,-camRot,camLook); 
	    } 
	    if((camera&CCLOCK) != 0){ 
	    	//TODO: camNose = glm::rotate(camNose,camRot,camLook); 
	    } 
	    if((camera&SLEFT) != 0){ 
	    	//TODO: glm::vec3 axis = glm::cross(camLook,camNose); 
	    	//TODO: camPos-=camMov*axis; 
	    } 
	    if((camera&SRIGHT) != 0){ 
	    	//TODO: glm::vec3 axis = glm::cross(camLook,camNose); 
	    	//TODO: camPos+=camMov*axis; 
	    }    
	    if((camera&SUP) != 0){ 
	    	//TODO: camPos-=camMov*camNose; 
	    } 
	    if((camera&SDOWN) != 0){ 
	    	//TODO: camPos+=camMov*camNose; 
	    }    
	}
}
