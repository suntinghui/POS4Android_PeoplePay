package com.people.client;

public class Constants {
	
	// 要相应的更新string.xml中的 versionstr 字段
		// 当前系统的版本号
		public static final String VERSION					= "12";
		
		// 注意此值并不是真正的联网地址，真正的地址在登录成功后返回，存于 kREALHOST 中
		// http://124.205.53.178:9596/lkoa5/WapService/
		public static final String DEFAULTHOST				= "http://211.147.87.22:8092/";
		
		public static final String AESKEY 					= "dynamicode";
		
		public static final String kHOSTNAME 				= "hostName";
		public static final String kREALHOST				= "RealHost"; // 存储真正的连网地址
		
		public static final String kWEBSERVICENAME			= "WebServiceName";
		
		public static final String kUSERNAME				= "userName";
		public static final String kPASSWORD 				= "password"; // 保存用户输入的密码
		public static final String kUSERID 					= "userId";
		public static final String kDEPTID  				= "deptId"; // 用户的部门ID
		
		public static final String kREMEBERPWD  			= "remeberPWD";
		public static final String kAUTOLOGIN  				= "autoLogin";
		public static final String kPASSWORD_MD5 			= "MD5_password"; // 资金管理中取列表的时候需要用到密码的MD5值

		
		public static final String kBGIMAGEID       		= "backgroundImageId"; // 登录界面的背景ID
		public static final int kBGIMAGECOUNT    			= 3;
		public static final String kPAGESIZE        		= "20";
		
		public static final String kDBGWCOUNT       		= "kDBGWCOUNT";
		public static final String kQSBGCOUNT       		= "kQSBGCOUNT";
		public static final String kXMGLCOUNT       		= "kXMGLCOUNT";
		public static final String kZJGLCOUNT       		= "kZJGLCOUNT";
		public static final String kXXZXCOUNT       		= "kXXZXCOUNT";
		public static final String kXTSZCOUNT				= "kXTSZCOUNT";
		
		// BPUSH
		public static final String kBPUSH_APPID				="appid";
		public static final String kBPUSH_USERID			="userid";
		public static final String kBPUSH_CHANNELID			="channelid";
		
		public static final int overtime    				= 1;
		public static final String LockKey					="LockKey";
		public static final String FirstLogin      			="FirstLogin";
		public static final String GestureClose      		="GestureClose";
		
	}
