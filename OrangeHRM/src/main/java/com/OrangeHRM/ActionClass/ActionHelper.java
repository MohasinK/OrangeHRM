package com.OrangeHRM.ActionClass;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.OrangeHRM.Action.ActionInterface;

public class ActionHelper implements ActionInterface{
	
	private static final Logger LOGGER = Logger.getLogger(ActionHelper.class.getName());
	private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
	private String text;

    public ActionHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    // --- Helper utilities to centralize waiting and error handling ---
    private WebElement waitVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private <T> T safeCall(Supplier<T> supplier, String actionDescription) {
        try {
            return supplier.get();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, actionDescription + " - Error: ", e);
            return null;
        } finally {
            LOGGER.log(Level.FINE, actionDescription + " attempted");
        }
    }

    private void safeRun(Runnable r, String actionDescription) {
        try {
            r.run();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, actionDescription + " - Error: ", e);
        } finally {
            LOGGER.log(Level.FINE, actionDescription + " attempted");
        }
    }

    // --- Action methods (concise implementations using helpers) ---
    @Override
    public void click(By locator) {
        safeRun(() -> waitClickable(locator).click(), "click(" + locator + ")");
    }

    @Override
    public void sendKeys(By locator, String value) {
        safeRun(() -> {
            WebElement element = waitVisibility(locator);
            element.clear();
            element.sendKeys(value);
        }, "sendKeys(" + locator + ", value)");
    }

    @Override
    public String getText(By locator) {
        String text = safeCall(() -> waitVisibility(locator).getText(), "getText(" + locator + ")");
        return text == null ? "" : text;
    }

    @Override
    public boolean isDisplayed(By locator) {
        Boolean displayed = safeCall(() -> waitVisibility(locator).isDisplayed(), "isDisplayed(" + locator + ")");
        return displayed == null ? false : displayed;
    }

    @Override
    public void jsClick(By locator) {
        safeRun(() -> {
            WebElement element = waitVisibility(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }, "jsClick(" + locator + ")");
    }

    @Override
    public void scrollToElement(By locator) {
        safeRun(() -> {
            WebElement element = waitVisibility(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }, "scrollToElement(" + locator + ")");
    }

    @Override
    public void hover(By locator) {
        safeRun(() -> actions.moveToElement(waitVisibility(locator)).perform(), "hover(" + locator + ")");
    }

    @Override
    public void doubleClick(By locator) {
        safeRun(() -> actions.doubleClick(waitClickable(locator)).perform(), "doubleClick(" + locator + ")");
    }

    @Override
    public void rightClick(By locator) {
        safeRun(() -> actions.contextClick(waitClickable(locator)).perform(), "rightClick(" + locator + ")");
    }

    @Override
    public void pressEnter(By locator) {
        safeRun(() -> waitVisibility(locator).sendKeys(Keys.ENTER), "pressEnter(" + locator + ")");
    }

    @Override
    public void waitForVisibility(By locator) {
        safeRun(() -> waitVisibility(locator), "waitForVisibility(" + locator + ")");
    }

	@Override
	public void waitForClickability(By locator) {
		safeRun(() -> waitClickable(locator), "waitForClickability(" + locator + ")");
	}

	@Override
	public void HighlightElement(By locator) {
		safeRun(() -> ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", waitVisibility(locator)), "HighlightElement(" + locator + ")");
	}

	@Override
	public void selectByVisibleText(By locator, String text) {
		safeRun(() -> {
			org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(waitClickable(locator));
			select.selectByVisibleText(text);
		}, "selectByVisibleText(" + locator + ", " + text + ")");
	}

	@Override
	public void selectByValue(By locator, String value) {
		safeRun(() -> {
			org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(waitClickable(locator));
			select.selectByValue(value);
		}, "selectByValue(" + locator + ", " + value + ")");
	}

	@Override
	public void selectByIndex(By locator, int index) {
		safeRun(() -> {
			org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(waitClickable(locator));
			select.selectByIndex(index);
		}, "selectByIndex(" + locator + ", " + index + ")");
	}

	@Override
	public void clear(By locator) {
		safeRun(() -> waitVisibility(locator).clear(), "clear(" + locator + ")");
	}

	@Override
	public void SwitchToFrame(By locator) {
		safeRun(() -> driver.switchTo().frame(waitVisibility(locator)), "SwitchToFrame(By)" );
	}

	@Override
	public void SwitchToFrame(int index) {
		safeRun(() -> driver.switchTo().frame(index), "SwitchToFrame(index)");
	}

	@Override
	public void SwitchToFrame(String nameOrId) {
		safeRun(() -> driver.switchTo().frame(nameOrId), "SwitchToFrame(nameOrId)");
	}

	@Override
	public void SwitchToParentFrame() {
		safeRun(() -> driver.switchTo().parentFrame(), "SwitchToParentFrame");
	}

	@Override
	public void SwitchToDefaultContent() {
		safeRun(() -> driver.switchTo().defaultContent(), "SwitchToDefaultContent");
	}

	@Override
	public void acceptAlert() {
		safeRun(() -> wait.until(ExpectedConditions.alertIsPresent()).accept(), "acceptAlert");
	}

	@Override
	public void dismissAlert() {
		safeRun(() -> wait.until(ExpectedConditions.alertIsPresent()).dismiss(), "dismissAlert");
	}

	@Override
	public void sendKeysToAlert(String value) {
		safeRun(() -> wait.until(ExpectedConditions.alertIsPresent()).sendKeys(value), "sendKeysToAlert");
	}

	@Override
	public void waitForAlert() {
		safeRun(() -> wait.until(ExpectedConditions.alertIsPresent()), "waitForAlert");
	}

	@Override
	public void SwitchToWindow(String windowHandle) {
		safeRun(() -> driver.switchTo().window(windowHandle), "SwitchToWindow(handle)");
	}

	@Override
	public void SwitchToWindowByTitle(String title) {
		safeRun(() -> {
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
				if (title.equals(driver.getTitle())) {
					return;
				}
			}
		}, "SwitchToWindowByTitle(" + title + ")");
	}

	@Override
	public void SwitchToWindowByURL(String url) {
		safeRun(() -> {
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
				if (url.equals(driver.getCurrentUrl())) {
					return;
				}
			}
		}, "SwitchToWindowByURL(" + url + ")");
	}

	@Override
	public void SwitchToWindowByIndex(int index) {
		safeRun(() -> {
			int i = 0;
			for (String handle : driver.getWindowHandles()) {
				if (i == index) {
					driver.switchTo().window(handle);
					break;
				}
				i++;
			}
		}, "SwitchToWindowByIndex(" + index + ")");
	}

	@Override
	public void closeCurrentWindow() {
		safeRun(() -> driver.close(), "closeCurrentWindow");
	}

	@Override
	public void closeAllWindowsExceptCurrent() {
		safeRun(() -> {
			String current = driver.getWindowHandle();
			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(current)) {
					driver.switchTo().window(handle);
					driver.close();
				}
			}
			driver.switchTo().window(current);
		}, "closeAllWindowsExceptCurrent");
	}

	@Override
	public void takeScreenshot(String filePath) {
		safeRun(() -> {
			if (driver instanceof org.openqa.selenium.TakesScreenshot) {
				org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
				java.io.File src = ts.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
				try {
					java.nio.file.Files.copy(src.toPath(), java.nio.file.Paths.get(filePath));
				} catch (java.io.IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		}, "takeScreenshot(" + filePath + ")");
	}

	@Override
	public void waitForPageLoad() {
		safeRun(() -> new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(30))
				.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")), "waitForPageLoad");
	}

	@Override
	public void waitForElementToBeInvisible(By locator) {
		safeRun(() -> wait.until(ExpectedConditions.invisibilityOfElementLocated(locator)), "waitForElementToBeInvisible(" + locator + ")");
	}

	@Override
	public void findElements(By locator) {
		safeRun(() -> {
			java.util.List<WebElement> elements = driver.findElements(locator);
			LOGGER.log(Level.FINE, "Found elements count: " + elements.size());
		}, "findElements(" + locator + ")");
	}

	@Override
	public void findElement(By locator) {
		safeRun(() -> {
			WebElement element = driver.findElement(locator);
			LOGGER.log(Level.FINE, "findElement located: " + element);
		}, "findElement(" + locator + ")");
	}

	@Override
	public void dragAndDrop(By sourceLocator, By targetLocator) {
		safeRun(() -> actions.dragAndDrop(waitVisibility(sourceLocator), waitVisibility(targetLocator)).perform(), "dragAndDrop(" + sourceLocator + ", " + targetLocator + ")");
	}

	@Override
	public void getAttribute(By locator, String attribute) {
		String value = safeCall(() -> waitVisibility(locator).getAttribute(attribute), "getAttribute(" + locator + ", " + attribute + ")");
		if (value != null) LOGGER.log(Level.FINE, "Attribute value: " + value);
	}

	@Override
	public void getCssValue(By locator, String property) {
		String value = safeCall(() -> waitVisibility(locator).getCssValue(property), "getCssValue(" + locator + ", " + property + ")");
		if (value != null) LOGGER.log(Level.FINE, "CSS value: " + value);
	}

	@Override
	public void getTagName(By locator) {
		String tag = safeCall(() -> waitVisibility(locator).getTagName(), "getTagName(" + locator + ")");
		if (tag != null) LOGGER.log(Level.FINE, "Tag name: " + tag);
	}

	@Override
	public void getListOfElements(By locator) {
		safeRun(() -> {
			java.util.List<WebElement> list = driver.findElements(locator);
			LOGGER.log(Level.FINE, "List size: " + list.size());
		}, "getListOfElements(" + locator + ")");
	}

	@Override
	public void webdriverWaitForElement(By locator, int timeoutInSeconds) {
		safeRun(() -> new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds)).until(ExpectedConditions.visibilityOfElementLocated(locator)), "webdriverWaitForElement(" + locator + ", " + timeoutInSeconds + ")");
	}

	@Override
	public void fluentWaitForElement(By locator, int timeoutInSeconds, int pollingInMillis) {
		safeRun(() -> {
			org.openqa.selenium.support.ui.FluentWait<WebDriver> fluent = new org.openqa.selenium.support.ui.FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(timeoutInSeconds))
				.pollingEvery(Duration.ofMillis(pollingInMillis))
				.ignoring(Exception.class);
			fluent.until(webDriver -> webDriver.findElement(locator));
		}, "fluentWaitForElement(" + locator + ", " + timeoutInSeconds + ", " + pollingInMillis + ")");
	}

	@Override
	public void implicitWait(int timeoutInSeconds) {
		safeRun(() -> driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeoutInSeconds)), "implicitWait(" + timeoutInSeconds + ")");
	}

	@Override
	public String getCurrentURL(String text) {
		safeRun(() -> {
			this.text = driver.getCurrentUrl();
			LOGGER.log(Level.FINE, "Current URL: " + text);
		}, "getCurrentURL");
		return text;
	}

	@Override
	public String getTitle(String text) {
		safeRun(() -> {
			this.text = driver.getTitle();
			LOGGER.log(Level.FINE, "Page Title: " + text);
		}, "getTitle");		
		return text;
	}

	@Override
	public void getPropertyValue(String key) {
		safeRun(() -> {
			String value = System.getProperty(key);
			LOGGER.log(Level.FINE, "Property value: " + value);
		}, "getPropertyValue(" + key + ")");		
	}

	@Override
	public String getText(By locator, String text) {
		safeRun(() -> {
			this.text = waitVisibility(locator).getText();
			LOGGER.log(Level.FINE, "Text from element: " + text);
		}, "getText(" + locator + ", text)");
		return text;
	}

}
