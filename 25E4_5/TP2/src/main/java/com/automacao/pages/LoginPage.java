package com.automacao.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By loginLink = By.xpath("//a[@href='/login']");
    private By emailField = By.xpath("//input[@data-qa='login-email']");
    private By passwordField = By.xpath("//input[@data-qa='login-password']");
    private By loginButton = By.xpath("//button[@data-qa='login-button']");
    private By logoutLink = By.xpath("//a[contains(@href, '/logout')]");
    private By accountLink = By.xpath("//a[contains(@href, '/account')]");
    private By deleteAccountButton = By.xpath("//a[contains(@href, '/delete')]");
    private By errorMessage = By.xpath("//p[contains(text(), 'Email or password is incorrect')]");
    private By successLoginMessage = By.xpath("//a[contains(text(), 'Logged in as')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickLoginLink() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }

    public boolean isLoginSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successLoginMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public void clickLogoutLink() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public boolean isLoggedOut() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginLink));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    public void clickAccountLink() {
        wait.until(ExpectedConditions.elementToBeClickable(accountLink)).click();
    }

    public void deleteAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteAccountButton)).click();
    }
}
