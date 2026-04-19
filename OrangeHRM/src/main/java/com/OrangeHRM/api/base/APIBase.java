package com.OrangeHRM.api.base;

import io.restassured.RestAssured;

public class APIBase {

	 public static void setup() {
	        RestAssured.baseURI = "https://opensource-demo.orangehrmlive.com";
	    }
	
}
