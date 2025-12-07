package com.automacao.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By productsLink = By.xpath("//a[contains(@href, '/products')]");
    private By productItems = By.xpath("//div[@class='productinfo']");
    private By addToCartButtons = By.xpath("//a[@data-product-id]");
    private By cartLink = By.xpath("//a[contains(@href, '/view_cart')]");
    private By cartTable = By.xpath("//table[@id='cart_info_table']");
    private By cartItems = By.xpath("//tr[@id]");
    private By quantityInputs = By.xpath("//input[@class='quantity']");
    private By priceColumn = By.xpath("//td[@class='price']");
    private By totalColumn = By.xpath("//td[@class='total']");
    private By proceedCheckout = By.xpath("//a[@class='btn-default'][contains(text(), 'Proceed')]");
    private By couponCode = By.id("coupon_code");
    private By applyCoupon = By.id("apply_coupon");
    private By deleteButton = By.xpath("//a[@class='cart_quantity_delete']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickProductsLink() {
        wait.until(ExpectedConditions.elementToBeClickable(productsLink)).click();
    }

    public void addProductToCart(int productIndex) {
        List<WebElement> buttons = driver.findElements(addToCartButtons);
        if (productIndex < buttons.size()) {
            buttons.get(productIndex).click();
            wait.until(ExpectedConditions.stalenessOf(buttons.get(productIndex)));
        }
    }

    public void clickCartLink() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
    }

    public int getCartItemsCount() {
        return driver.findElements(cartItems).size();
    }

    public String getProductPrice(int productIndex) {
        List<WebElement> prices = driver.findElements(priceColumn);
        if (productIndex < prices.size()) {
            return prices.get(productIndex).getText();
        }
        return "";
    }

    public String getProductTotal(int productIndex) {
        List<WebElement> totals = driver.findElements(totalColumn);
        if (productIndex < totals.size()) {
            return totals.get(productIndex).getText();
        }
        return "";
    }

    public void updateProductQuantity(int productIndex, String quantity) {
        List<WebElement> inputs = driver.findElements(quantityInputs);
        if (productIndex < inputs.size()) {
            WebElement input = inputs.get(productIndex);
            input.clear();
            input.sendKeys(quantity);
        }
    }

    public void deleteProductFromCart(int productIndex) {
        List<WebElement> deleteButtons = driver.findElements(deleteButton);
        if (productIndex < deleteButtons.size()) {
            deleteButtons.get(productIndex).click();
        }
    }

    public void applyCouponCode(String code) {
        driver.findElement(couponCode).sendKeys(code);
        driver.findElement(applyCoupon).click();
    }

    public void clickProceedCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(proceedCheckout)).click();
    }

    public boolean isCartEmpty() {
        return driver.findElements(cartItems).isEmpty();
    }
}
