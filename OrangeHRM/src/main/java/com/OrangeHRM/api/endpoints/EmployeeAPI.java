package com.OrangeHRM.api.endpoints;

import com.OrangeHRM.api.base.APIBase;
import com.OrangeHRM.api.utils.SessionManager;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class EmployeeAPI {
	
	  public static Response searchEmployee(String empName) {

	        APIBase.setup();

	        return given()
	                .cookie("orangehrm", SessionManager.getSessionId())   
	                .queryParam("nameOrId", empName)
	                .log().all()

	        .when()
	                .get("/web/index.php/api/v2/pim/employees")

	        .then()
	                .log().all()
	                .extract().response();
	    }

}
