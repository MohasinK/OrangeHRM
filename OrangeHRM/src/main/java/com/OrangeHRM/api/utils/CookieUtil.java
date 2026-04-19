package com.OrangeHRM.api.utils;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class CookieUtil {
	
	 public static void captureSession(WebDriver driver) {

	        Set<Cookie> cookies = driver.manage().getCookies();

	        for (Cookie cookie : cookies) {

	            if (cookie.getName().contains("orangehrm")) {

	                System.out.println("Session Captured: " + cookie.getValue());

	                SessionManager.setSessionId(cookie.getValue());
	                return;
	            }
	        }

	        throw new RuntimeException("Session cookie not found!");
	    }

}
