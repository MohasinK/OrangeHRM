package TestCases;

import java.io.IOException;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.OrangeHRM.Base.BaseClass;
import com.OrangeHRM.ExceL.ExcelLibrary;
import com.OrangeHRM.Pages.LoginPage;
import com.OrangeHRM.Utility.TestLogger;
import java.lang.reflect.Method;
import org.testng.annotations.BeforeMethod;

public class LoginPageTest extends BaseClass{
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
	
	@BeforeMethod
	public void printTestCaseInfo(Method method) {
	    Test testAnnotation = method.getAnnotation(Test.class);

	    String testName = method.getName();
	    String description = testAnnotation.description();

	    System.out.println("==================================================");
	    System.out.println("STARTING TEST CASE : " + testName);
	    System.out.println("DESCRIPTION        : " + description);
	    System.out.println("==================================================");
	}
	
	@Test(priority = 1,description = "To verify navigate to login page")
	public void TC_01()
	{
		logger.start("TC_01: Verify navigation to login page");
		boolean flag = login.verifyLoginPage();
		Assert.assertTrue(flag, "Login page verification failed");
		logger.end("TC_01: Verify navigation to login page");
	}
	
	@Test(priority = 2,description = "To verify logo is displayed on login page")
	public void TC_02()
	{
		logger.start("TC_02: Verify logo is displayed on login page");
		boolean flag = login.isOrangeHRMLogoDisplayed();
		Assert.assertTrue(flag, "Logo is not displayed on login page");
		logger.end("TC_02: Verify logo is displayed on login page");
	}
	
	@Test(priority = 3,description = "To verify forgot password link is displayed on login page")
	public void TC_03()
	{
		logger.start("TC_03: Verify forgot password link is displayed on login page");
		boolean flag = login.isForgotPasswordDisplayed();
		Assert.assertTrue(flag, "Forgot password link is not displayed on login page");
		logger.end("TC_03: Verify forgot password link is displayed on login page");
	}
	
	@Test(priority = 4,description = "To verify login header is displayed on login page")
	public void TC_04()
	{
		logger.start("TC_04: Verify login header is displayed on login page");
		boolean flag = login.isLoginHeaderDisplayed();
		Assert.assertTrue(flag, "Login header is not displayed on login page");
		logger.end("TC_04: Verify login header is displayed on login page");
	}
	
	@Test(priority = 5,description = "To verify company branding is displayed on login page")
	public void TC_05()
	{
		logger.start("TC_05: Verify company branding is displayed on login page");
		boolean flag = login.isCompanyBrandingDisplayed();
		Assert.assertTrue(flag, "Company branding is not displayed on login page");
		logger.end("TC_05: Verify company branding is displayed on login page");
	}
	
	@Test(priority = 6,description = "To verify user is able to login with Invalid credentials")
	public void TC_06()
	{
		logger.start("TC_06: Verify login with invalid credentials");
		login.enterUsername(excel.getCellData(SheetName, 1, 0));
		login.enterPassword(excel.getCellData(SheetName, 1, 1));
		login.clickLogin();
		boolean flag = login.isInvalidCredentialsMessageDisplay();
		Assert.assertTrue(flag, "Invalid credentials message is not displayed");
		logger.end("TC_06: Verify login with invalid credentials");
	}
	
	@Test(priority = 7,description = "To verify user is able to login with valid credentials")
	public void TC_07()
	{
		logger.start("TC_07: Verify login with valid credentials");
		login.enterUsername(excel.getCellData(SheetName, 2, 0));
		login.enterPassword(excel.getCellData(SheetName, 2, 1));
		boolean flag = login.clickLogin();
		Assert.assertTrue(flag, "Login failed with valid credentials");
		logger.end("TC_07: Verify login with valid credentials");
	}
	
	@AfterClass
	public void tearDown() {
		driver.quit();
	}
	
	
	
	


	
	

	
	
	
	
	

}