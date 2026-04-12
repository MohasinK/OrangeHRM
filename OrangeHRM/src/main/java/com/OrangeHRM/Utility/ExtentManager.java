package com.OrangeHRM.Utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    public static ExtentReports getInstance(String className) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        String reportPath = "reports/" + className + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

        spark.config().setDocumentTitle("OrangeHRM Automation Report");
        spark.config().setReportName(className + " Execution Report");
        spark.config().setTheme(Theme.STANDARD);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Project", "OrangeHRM");
        extent.setSystemInfo("Tester", "Mohasin");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("Browser", "Chrome");

        return extent;
    }
}