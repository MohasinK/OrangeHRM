package com.OrangeHRM.Action;

import org.openqa.selenium.By;

public interface ActionInterface {
	
	void click(By locator);

	void sendKeys(By locator, String value);

	String getText(By locator);

	boolean isDisplayed(By locator);

	void jsClick(By locator);

	void scrollToElement(By locator);

	void hover(By locator);

	void doubleClick(By locator);

	void rightClick(By locator);

	void pressEnter(By locator);

	void waitForVisibility(By locator);
	
	void waitForClickability(By locator);
	
	void HighlightElement(By locator);
	
	void selectByVisibleText(By locator, String text);
	
	void selectByValue(By locator, String value);
	
	void selectByIndex(By locator, int index);
	
	void clear(By locator);
	
	void SwitchToFrame(By locator);

	void SwitchToFrame(int index);
	
	void SwitchToFrame(String nameOrId);
	
	void SwitchToParentFrame();
	
	void SwitchToDefaultContent();
	
	void acceptAlert();
	
	void dismissAlert();
	
	void sendKeysToAlert(String value);
	
	void waitForAlert();
	
	void SwitchToWindow(String windowHandle);
	
	void SwitchToWindowByTitle(String title);
	
	void SwitchToWindowByURL(String url);
	
	void SwitchToWindowByIndex(int index);
	
	void closeCurrentWindow();
	
	void closeAllWindowsExceptCurrent();
	
	void takeScreenshot(String filePath);
	
	void waitForPageLoad();
	
	void waitForElementToBeInvisible(By locator);
	
	void findElements(By locator);
	
	void findElement(By locator);
	
	void dragAndDrop(By sourceLocator, By targetLocator);
	
	void getAttribute(By locator, String attribute);
	
	void getCssValue(By locator, String property);
	
	void getTagName(By locator);
	
	void getListOfElements(By locator);
	
	void webdriverWaitForElement(By locator, int timeoutInSeconds);
	
	void fluentWaitForElement(By locator, int timeoutInSeconds, int pollingInMillis);
	
	void implicitWait(int timeoutInSeconds);
	
	String getTitle(String title);
	
	void getPropertyValue(String key);

	String getCurrentURL(String url);
	
	String getText(By locator, String text);
	
	
	
	
}
