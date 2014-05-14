package com.people.client;

public class Constants {

	// 当前系统的版本号
	public static final String VERSION 					          	= "1";

	public static final String AESKEY 					          	= "dynamicode";
	
	public static final String kUSERNAME				          	= "kUSERNAME";
	public static final String kPASSWORD				          	= "kPASSWORD";
	
	public static final int OVERTIME    				          	= 3;
	
	public static boolean HASSETBLUETOOTH				          	= false;
	public static boolean HASSIGN									= false;
	
	public static final String kLOCKKEY					          	="LockKey";
	public static final String kFIRSTLOGIN      		         	="FirstLogin";
	public static final String kGESTRUECLOSE      		          	="GestureClose";
	
	public static final String ACTION_QPOS_CANCEL					= "ACTION_QPOS_CANCEL";
	public static final String ACTION_QPOS_STARTSWIPE				= "ACTION_QPOS_STARTSWIPE";
	public static final String ACTION_QPOS_SWIPEDONE				= "ACTION_QPOS_SWIPEDONE";
	
	public static final String APPFILEPATH 				    		= "/data/data/" + ApplicationEnvironment.getInstance().getApplication().getPackageName();
	public static final String SIGNIMAGESPATH						= APPFILEPATH + "/signImages/";
	public static final String kTRACEAUDITNUM 			         	 = "kTRACEAUDITNUM";
	

}
