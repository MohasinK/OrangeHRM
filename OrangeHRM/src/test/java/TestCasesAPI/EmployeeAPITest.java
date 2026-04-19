package TestCasesAPI;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.OrangeHRM.Base.BaseClass;
import com.OrangeHRM.ExceL.ExcelLibrary;
import com.OrangeHRM.Pages.LoginPage;
import com.OrangeHRM.Utility.TestLogger;
import com.OrangeHRM.api.endpoints.EmployeeAPI;
import com.OrangeHRM.api.utils.CookieUtil;
import com.OrangeHRM.api.utils.SessionManager;

import TestCasesUI.LoginPageTest;
import io.restassured.response.Response;

public class EmployeeAPITest extends BaseClass {
	private LoginPage login;
	private ExcelLibrary excel;
	private String SheetName = "LoginPage";
	TestLogger logger = new TestLogger(LoginPageTest.class);
	
	@BeforeClass
	@Parameters({"browser", "headless"})
	public void setUp(
	        @Optional("chrome") String browser,
	        @Optional("false") String headless) throws IOException {
	    logger.start("Initializing LoginPageTest...");
	    BaseClass.setup(browser, headless);
	    login = new LoginPage();
	    excel = new ExcelLibrary();
	    logger.end("LoginPageTest setup completed.");
	}
	
	
	@Test
	public void testEmployeeFlow() {

	    // UI login
		login = new LoginPage();
		login.enterUsername("Admin");
		login.enterPassword("admin123");
		login.clickLogin();
	    
		// session capture
	    CookieUtil.captureSession(driver);
	    
	    System.out.println("Session: " + SessionManager.getSessionId());

	    // API call
	    Response res = EmployeeAPI.searchEmployee("ESSUser");

	    // API validation
	    Assert.assertEquals(res.getStatusCode(), 200);

	    // UI validation
//	    employeePage.searchEmployee("Chandu");
//	    Assert.assertTrue(employeePage.isEmployeeDisplayed("Chandu"));
	}

}
