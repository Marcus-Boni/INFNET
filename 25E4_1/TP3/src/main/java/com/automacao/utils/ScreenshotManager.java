package com.automacao.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotManager {
    private WebDriver driver;
    private String screenshotPath;

    public ScreenshotManager(WebDriver driver) {
        this.driver = driver;
        this.screenshotPath = System.getProperty("user.dir") + File.separator + "screenshots";
        createDirectoryIfNotExists();
    }

    public ScreenshotManager(WebDriver driver, String customPath) {
        this.driver = driver;
        this.screenshotPath = customPath;
        createDirectoryIfNotExists();
    }

    private void createDirectoryIfNotExists() {
        File directory = new File(screenshotPath);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Diretório criado: " + screenshotPath);
        }
    }

    public String takeScreenshot(String testName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String fileName = generateFileName(testName);
            File destinationPath = new File(screenshotPath + File.separator + fileName);
            FileUtils.copyFile(screenshot, destinationPath);
            System.out.println("Screenshot salvo: " + destinationPath.getAbsolutePath());
            return destinationPath.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Erro ao capturar screenshot: " + e.getMessage());
            return null;
        }
    }

    public String takeElementScreenshot(WebElement element, String elementName) {
        try {
            File screenshot = element.getScreenshotAs(OutputType.FILE);
            String fileName = generateFileName(elementName);
            File destinationPath = new File(screenshotPath + File.separator + fileName);
            FileUtils.copyFile(screenshot, destinationPath);
            System.out.println("Screenshot do elemento salvo: " + destinationPath.getAbsolutePath());
            return destinationPath.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Erro ao capturar screenshot do elemento: " + e.getMessage());
            return null;
        }
    }

    private String generateFileName(String testName) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
        return testName + "_" + timestamp + ".png";
    }

    public String takeErrorScreenshot(String testName) {
        return takeScreenshot(testName + "_ERROR");
    }
  
    public String takeSuccessScreenshot(String testName) {
        return takeScreenshot(testName + "_SUCCESS");
    }
  
    public void listAllScreenshots() {
        File directory = new File(screenshotPath);
        File[] files = directory.listFiles();
        
        if (files != null && files.length > 0) {
            System.out.println("\n=== SCREENSHOTS CAPTURADOS ===");
            for (File file : files) {
                System.out.println(file.getName());
            }
            System.out.println("Total: " + files.length + "\n");
        } else {
            System.out.println("Nenhum screenshot encontrado.");
        }
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }
}
