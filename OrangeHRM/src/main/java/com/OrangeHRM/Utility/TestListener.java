package com.OrangeHRM.Utility;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.OrangeHRM.Base.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class TestListener extends BaseClass implements ITestListener {

    private static final Map<String, ExtentReports> extentMap =
            new ConcurrentHashMap<>();

    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    @Override
    public void onStart(ITestContext context) {
        clearScreenshotFolder();
        System.out.println("Old screenshots deleted");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass()
                .getRealClass()
                .getSimpleName();

        ExtentReports extent = extentMap.computeIfAbsent(
                className,
                ExtentManager::getInstance
        );

        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest extentTest = extent.createTest(testName);

        if (description != null) {
            extentTest.info("📝 Description: " + description);
        }

        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");

        String screenshotPath = captureScreenshot(result.getMethod().getMethodName());

        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            try {
                test.get().pass("Screenshot on pass",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.get().warning("Unable to attach pass screenshot: " + e.getMessage());
            }
        } else {
            test.get().warning("Pass screenshot not captured because driver was null.");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());

        String screenshotPath = captureScreenshot(result.getMethod().getMethodName());

        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            try {
                test.get().fail("Screenshot on failure",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.get().warning("Unable to attach failure screenshot: " + e.getMessage());
            }
        } else {
            test.get().warning("Failure screenshot not captured because driver was null.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("⚠ Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        for (ExtentReports extent : extentMap.values()) {
            extent.flush();
        }
    }
    
    private void clearScreenshotFolder() {
        File folder = new File(System.getProperty("user.dir")
                + "/ScreenShots");

        if (!folder.exists()) {
            folder.mkdirs();
            return;
        }

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
    
}