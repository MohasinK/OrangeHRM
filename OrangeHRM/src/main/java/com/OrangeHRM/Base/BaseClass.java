package com.OrangeHRM.Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	// Use ThreadLocal to support parallel test execution safely
	private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

	// Keep protected field for backward compatibility with existing tests
	protected static WebDriver driver;

	// ExtentReports instances
	//private static ExtentReports extent;
	//private static final ThreadLocal<ExtentTest> tlTest = new ThreadLocal<>();

	// Configuration properties
	private final static Properties config = new Properties();

	/**
	 * Return the thread-local WebDriver instance.
	 */
	protected WebDriver getDriver() {
		return tlDriver.get();
	}

	// --- new helper to construct ChromeOptions for Jenkins/CI and local runs ---
	private static ChromeOptions buildChromeOptions(String headless) {
		ChromeOptions options = new ChromeOptions();
		// Detect CI/Jenkins environment by common environment variables
		String jenkinsHome = System.getenv("JENKINS_HOME");
		String ciEnv = System.getenv("CI");
		String chromeBin = System.getenv("CHROME_BIN");
		boolean isCI = (jenkinsHome != null && !jenkinsHome.isEmpty()) || (ciEnv != null && ciEnv.equalsIgnoreCase("true")) || "true".equalsIgnoreCase(System.getProperty("ci"));

		// Allow overriding headless via property
		boolean isHeadless =
		        "true".equalsIgnoreCase(headless)
		        || isCI;

		if (isHeadless){
			// Recommended flags for running Chrome in CI (Jenkins) environments
			// Use new headless option where available; fall back to --headless
//			if (headless) options.addArguments("--headless=new");
			options.addArguments("--headless=new");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
			options.addArguments("--disable-gpu");
			options.addArguments("--window-size=1920,1080");
			options.addArguments("--disable-extensions");
			options.addArguments("--disable-infobars");
			options.addArguments("--remote-allow-origins=*");
			options.setAcceptInsecureCerts(true);
			// If CI provides a CHROME_BIN location (common in some containers), use it
			if (chromeBin != null && !chromeBin.isEmpty()) {
				options.setBinary(chromeBin);
			}
		} else {
			// Local-friendly defaults
			options.addArguments("--start-maximized");
		}

		// Add any extra args from properties (comma-separated)
		String extra = getConfig("chrome.extraArgs", "");
		if (!extra.isEmpty()) {
			for (String arg : extra.split(",")) {
				arg = arg.trim();
				if (!arg.isEmpty()) options.addArguments(arg);
			}
		}

		return options;
	}

	/**
	 * Load configuration properties from classpath or fallback locations.
	 */
	private void loadProperties() {
		// Try classpath first
		try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (is != null) {
				config.load(is);
				System.out.println("Loaded config.properties from classpath");
				return;
			}
		} catch (IOException e) {
			System.err.println("Failed to load config.properties from classpath: " + e.getMessage());
		}

		// Fallback: try common file system paths
		String[] candidates = {
			System.getProperty("user.dir") + "/src/main/resources/config.properties",
			System.getProperty("user.dir") + "/src/test/resources/config.properties"
		};
		for (String path : candidates) {
			try (InputStream is = new FileInputStream(path)) {
				config.load(is);
				System.out.println("Loaded config.properties from " + path);
				return;
			} catch (IOException e) {
				// continue to next candidate
			}
		}

		System.out.println("No config.properties found; using defaults and system properties");
	}

	/**
	 * Helper to get a config property with default.
	 */
	private static String getConfig(String key, String defaultValue) {
		// System properties override file properties
		String sys = System.getProperty(key);
		if (sys != null && !sys.isEmpty()) return sys;
		String val = config.getProperty(key);
		return (val == null || val.isEmpty()) ? defaultValue : val;
	}

	/**
	 * Helper to get int property with default.
	 */
	private static int getConfigInt(String key, int defaultValue) {
		String v = getConfig(key, "");
		if (v == null || v.isEmpty()) return defaultValue;
		try {
			return Integer.parseInt(v);
		} catch (NumberFormatException e) {
			System.err.println("Invalid integer for property " + key + ": " + v + " — using default " + defaultValue);
			return defaultValue;
		}
	}

	/**
	 * Initialize ExtentReports before the test suite runs.
	 */
	@BeforeSuite(alwaysRun = true)
	public void initReport() {
		// Load configuration first
		loadProperties();
		//extent = ExtentManager.getInstance(getClass().getSimpleName());
	}

	/**
	 * Create an ExtentTest for each test method.
	 */
//	@BeforeMethod(alwaysRun = true)
//	public void beforeMethod(Method method) {
//		if (extent == null) {
//			extent = ExtentManager.getInstance(getClass().getSimpleName());
//		}
//		ExtentTest test = extent.createTest(method.getDeclaringClass().getSimpleName() + "." + method.getName());
//		tlTest.set(test);
//	}

    /**
     * Setup driver. Accepts a TestNG parameter named "browser" (chrome|firefox|edge).
     * If not provided or equal to "default", uses property from config.properties.
     */
	
	public static void setup(@Optional("default") String browser,
            @Optional("false") String headless){
        String defaultBrowser = getConfig("browser.default", "chrome");
        String b = (browser == null || "default".equalsIgnoreCase(browser)) ? defaultBrowser : browser.toLowerCase().trim();

        WebDriver drv;
        switch (b) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fo = new FirefoxOptions();
                // Add any desired default options for Firefox here
                drv = new FirefoxDriver(fo);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions eo = new EdgeOptions();
                // Add any desired default options for Edge here
                drv = new EdgeDriver(eo);
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                // Use helper to build ChromeOptions appropriate for CI (Jenkins) or local runs
                ChromeOptions co = buildChromeOptions(headless);
                drv = new ChromeDriver(co);
                break;
        }

        // Store in ThreadLocal and keep backward-compatible field in sync
        tlDriver.set(drv);
        driver = tlDriver.get();

        // Common configuration - use properties if present
        int implicit = getConfigInt("implicitly.wait.seconds", 10);
        int pageLoad = getConfigInt("pageLoad.timeout.seconds", 30);

        if (!"true".equalsIgnoreCase(headless)) {
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
        driver.get(getConfig("URL",""));
    }

    /**
     * Record test result to ExtentReports and attach a screenshot on failure.
     */
//    @AfterMethod(alwaysRun = true)
//    public void recordResult(ITestResult result) {
//		ExtentTest test = tlTest.get();
//		if (test == null) return;
//
//		if (result.getStatus() == ITestResult.FAILURE) {
//			test.log(Status.FAIL, result.getThrowable());
//			String screenshot = captureScreenshot(result.getMethod().getMethodName());
//			if (screenshot != null) {
//				try {
//					test.addScreenCaptureFromPath(screenshot);
//				} catch (Exception e) {
//					test.info("Failed to attach screenshot: " + e.getMessage());
//				}
//			}
//		} else if (result.getStatus() == ITestResult.SUCCESS) {
//			test.log(Status.PASS, "Test passed");
//		} else if (result.getStatus() == ITestResult.SKIP) {
//			test.log(Status.SKIP, "Test skipped: " + result.getThrowable());
//		}
//
//		// remove thread-local test
//		tlTest.remove();
//    }

    @AfterSuite(alwaysRun = true)
    public void tearDownReport() {
//		if (extent != null) {
//			extent.flush();
//		}
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        WebDriver d = tlDriver.get();

        if (d != null) {
            try {
                d.quit();
            } catch (Exception e) {
                System.err.println("Error quitting WebDriver: " + e.getMessage());
            }
            tlDriver.remove();
        }

        BaseClass.driver = null;
    }

	/**
	 * Capture a screenshot using the thread-local driver and return the file path.
	 */
	protected String captureScreenshot(String testName) {
		try {
			WebDriver driver = getDriver();
			
		    if (driver == null) {
	            System.out.println("Driver is null, screenshot skipped");
	            return null;
	        }

			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());

			String fileName = testName + "_" + timeStamp + ".png";

			String absolutePath = System.getProperty("user.dir") + "/ScreenShots/" + fileName;

			File dest = new File(absolutePath);
			dest.getParentFile().mkdirs();

			Files.copy(src.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			// IMPORTANT: relative path for report
			return "../ScreenShots/" + fileName;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	

}
