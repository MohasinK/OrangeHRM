package com.OrangeHRM.Pages;

import org.openqa.selenium.By;

import com.OrangeHRM.ActionClass.ActionHelper;
import com.OrangeHRM.Base.BaseClass;

public class LoginPage extends BaseClass {
	ActionHelper action;
	
	By username = By.xpath("//input[@name='username']");

	By password = By.xpath("//input[@name='password']");
	
	By loginBtn = By.xpath("//button[normalize-space()='Login']");
	
	By forgotPassword = By.xpath("//p[contains(@class,\"oxd-text oxd-text--p orangehrm-login-forgot-header\")]");
	
	By loginHeader = By.xpath("//h5[text()='Login']");
	
	By orangeHRMLogo = By.xpath("(//img[contains(@src,'/web/images/ohrm_logo.png')])[2]");
	
	By companyBranding = By.xpath("//img[@alt='company-branding']");
	
	By invalidCredentials = By.xpath("//*[@class=\"oxd-text oxd-text--p oxd-alert-content-text\"]");
	
	
	public LoginPage() {
		action = new ActionHelper(driver);
	}
	
	public boolean verifyLoginPage() {
		String expectedTitle = "OrangeHRM";
		String actualTitle = action.getTitle("");
		if (actualTitle.equals(expectedTitle)) {
			System.out.println("Successfully navigated to Login Page. Title: " + actualTitle);
		} else {
			System.err.println("Failed to navigate to Login Page. Expected Title: " + expectedTitle + ", Actual Title: " + actualTitle);
		}
		return true;
	}
	
	public boolean enterUsername(String user) {
		action.sendKeys(username, user);
		return true;
	}
	
	public boolean enterPassword(String pass) {
		action.sendKeys(password, pass);
		return true;
	}
	
	public boolean clickLogin() {
		action.click(loginBtn);
		return true;
	}
	
	public boolean isForgotPasswordDisplayed() {
		return action.isDisplayed(forgotPassword);
	}
	
	public boolean isLoginHeaderDisplayed() {
		return action.isDisplayed(loginHeader);
	}
	
	public boolean isOrangeHRMLogoDisplayed() {
		return action.isDisplayed(orangeHRMLogo);
	}
	
	public boolean isCompanyBrandingDisplayed() {
		return action.isDisplayed(companyBranding);
	}
	
	public boolean isInvalidCredentialsMessageDisplay() {
		String text = action.getText(invalidCredentials,"Invalid credentials message: ");
		System.out.println("Invalid credentials message: " + text);
		return true;
	}
	
	public boolean getTitle(String pageTitle) {
		pageTitle = action.getTitle(pageTitle);
		if (pageTitle != null) {
			System.out.println("Current page title: " + pageTitle);
		} else {
			System.err.println("Failed to retrieve page title.");
		}
		return true;
	}

	
	
	
	

}
