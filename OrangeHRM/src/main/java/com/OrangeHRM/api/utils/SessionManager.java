package com.OrangeHRM.api.utils;

public class SessionManager {
	
	 private static String sessionId;

	    public static void setSessionId(String id) {
	        sessionId = id;
	    }

	    public static String getSessionId() {
	        return sessionId;
	    }

}
