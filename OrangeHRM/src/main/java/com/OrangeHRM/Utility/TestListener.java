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
        String path = captureScreenshot(result.getMethod().getMethodName());

        try {
            test.get().pass(
                    "✅ Test Passed",
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String path = captureScreenshot(result.getMethod().getMethodName());

        try {
            test.get().fail(
                    "❌ Test Failed: " + result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build()
            );
        } catch (Exception e) {
            e.printStackTrace();
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