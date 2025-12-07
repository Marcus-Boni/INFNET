package com.automacao.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By proceedCheckout = By.xpath("//a[@class='btn-default'][contains(text(), 'Proceed')]");
    private By registerCheckout = By.xpath("//a[@data-toggle='tab'][contains(text(), 'Register')]");
    private By guestCheckout = By.xpath("//a[@data-toggle='tab'][contains(text(), 'Guest')]");
    
    private By firstNameField = By.id("first_name");
    private By lastNameField = By.id("last_name");
    private By companyField = By.id("company");
    private By addressField = By.id("address1");
    private By countrySelect = By.id("country");
    private By stateField = By.id("state");
    private By cityField = By.id("city");
    private By zipField = By.id("zipcode");
    private By phoneField = By.id("mobile_number");
    
    private By orderCommentField = By.id("comments");
    
    private By placeOrderButton = By.xpath("//a[@href='#payment'][contains(text(), 'Place Order')]");
    
    private By nameOnCardField = By.id("name_on_card");
    private By cardNumberField = By.id("card_number");
    private By cvvField = By.id("cvc");
    private By expiryMonthField = By.id("expiry_month");
    private By expiryYearField = By.id("expiry_year");
    private By payButton = By.id("submit");
    
    private By orderPlacedMessage = By.xpath("//h2[@data-qa='order-placed']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /*
     * DESAFIO: Lidar com múltiplas abas e navegação
     * Solução: Usar JavaScript para scroll e garantir elementos visíveis
     * Estratégia: Clicar em elementos após scroll ou usando JavaScript
     */

    public void clickProceedCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(proceedCheckout)).click();
    }

    public void clickRegisterCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(registerCheckout)).click();
    }

    public void clickGuestCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(guestCheckout)).click();
    }

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
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
        driver.findElement(countrySelect).sendKeys(country);
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

    public void enterOrderComment(String comment) {
        driver.findElement(orderCommentField).sendKeys(comment);
    }

    public void clickPlaceOrder() {
        /*
         * Solução para elemento que pode estar fora da viewport:
         * Usar JavaScript para scroll até o elemento
         * Depois clicar normalmente
         */
        WebElement placeOrderBtn = driver.findElement(placeOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", placeOrderBtn);
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
    }

    public void enterNameOnCard(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameOnCardField)).sendKeys(name);
    }

    public void enterCardNumber(String cardNumber) {
        driver.findElement(cardNumberField).sendKeys(cardNumber);
    }

    public void enterCVV(String cvv) {
        driver.findElement(cvvField).sendKeys(cvv);
    }

    public void enterExpiryMonth(String month) {
        driver.findElement(expiryMonthField).sendKeys(month);
    }

    public void enterExpiryYear(String year) {
        driver.findElement(expiryYearField).sendKeys(year);
    }

    public void clickPayButton() {
        wait.until(ExpectedConditions.elementToBeClickable(payButton)).click();
    }

    public String getOrderPlacedMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(orderPlacedMessage)).getText();
    }

    public boolean isOrderPlacedSuccessfully() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(orderPlacedMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
