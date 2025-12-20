package com.automacao.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ContactPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By contactLink = By.xpath("//a[contains(@href, '/contact')]");
    private By nameField = By.xpath("//input[@data-qa='name']");
    private By emailField = By.xpath("//input[@data-qa='email']");
    private By subjectField = By.xpath("//input[@data-qa='subject']");
    private By messageField = By.xpath("//textarea[@data-qa='message']");
    private By uploadFile = By.xpath("//input[@name='upload_file']");
    private By submitButton = By.xpath("//input[@data-qa='submit-button']");
    private By successMessage = By.xpath("//div[@class='alert-success']");

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickContactLink() {
        wait.until(ExpectedConditions.elementToBeClickable(contactLink)).click();
    }

    public void enterName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterSubject(String subject) {
        driver.findElement(subjectField).sendKeys(subject);
    }

    public void enterMessage(String message) {
        driver.findElement(messageField).sendKeys(message);
    }

    public void uploadFile(String filePath) {
        driver.findElement(uploadFile).sendKeys(filePath);
    }

    public void clickSubmitButton() {
        driver.findElement(submitButton).click();
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
