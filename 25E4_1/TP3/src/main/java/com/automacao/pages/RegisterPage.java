package com.automacao.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;

public class RegisterPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By signupLink = By.xpath("//a[@href='/login']");
    private By nameField = By.xpath("//input[@data-qa='signup-name']");
    private By emailField = By.xpath("//input[@data-qa='signup-email']");
    private By signupButton = By.xpath("//button[@data-qa='signup-button']");
    
    private By titleMr = By.id("id_gender1");
    private By passwordField = By.id("password");
    private By firstNameField = By.id("first_name");
    private By lastNameField = By.id("last_name");
    private By companyField = By.id("company");
    private By addressField = By.id("address1");
    private By countrySelect = By.id("country");
    private By stateField = By.id("state");
    private By cityField = By.id("city");
    private By zipField = By.id("zipcode");
    private By phoneField = By.id("mobile_number");
    private By createAccountButton = By.xpath("//button[@data-qa='create-account']");
    private By confirmMessage = By.xpath("//h2[@data-qa='account-created']");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickSignupLink() {
        wait.until(ExpectedConditions.elementToBeClickable(signupLink)).click();
    }

    public void enterSignupName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
    }

    public void enterSignupEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void clickSignupButton() {
        driver.findElement(signupButton).click();
    }

    public void selectTitle() {
        driver.findElement(titleMr).click();
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void enterFirstName(String firstName) {
        driver.findElement(firstNameField).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        driver.findElement(lastNameField).sendKeys(lastName);
    }

    public void enterCompany(String company) {
        driver.findElement(companyField).sendKeys(company);
    }

    public void enterAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    public void selectCountry(String country) {
        Select countryDropdown = new Select(driver.findElement(countrySelect));
        countryDropdown.selectByVisibleText(country);
    }

    public void enterState(String state) {
        driver.findElement(stateField).sendKeys(state);
    }

    public void enterCity(String city) {
        driver.findElement(cityField).sendKeys(city);
    }

    public void enterZip(String zip) {
        driver.findElement(zipField).sendKeys(zip);
    }

    public void enterPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    public void clickCreateAccountButton() {
        driver.findElement(createAccountButton).click();
    }

    public String getConfirmMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmMessage)).getText();
    }
}
