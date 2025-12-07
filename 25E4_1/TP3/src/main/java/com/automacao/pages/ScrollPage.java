package com.automacao.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ScrollPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private By arrowUpButton = By.id("scrollUp");
    private By arrowDownButton = By.id("scrollDown");
    private By testCasesLink = By.xpath("//a[contains(text(), 'Test Cases')]");
    private By productsList = By.xpath("//div[@class='product-overlay']");
    private By bottomText = By.xpath("//h2[contains(text(), 'Subscription')]");

    public ScrollPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
    }

    public void scrollDown(int pixels) {
        js.executeScript("window.scrollBy(0, " + pixels + ");");
    }

    public void scrollUp(int pixels) {
        js.executeScript("window.scrollBy(0, -" + pixels + ");");
    }

    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void clickArrowDownButton() {
        wait.until(ExpectedConditions.elementToBeClickable(arrowDownButton)).click();
    }

    public void clickArrowUpButton() {
        wait.until(ExpectedConditions.elementToBeClickable(arrowUpButton)).click();
    }

    public void clickTestCasesLink() {
        wait.until(ExpectedConditions.elementToBeClickable(testCasesLink)).click();
    }

    public boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isElementVisibleInViewport(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return (Boolean) js.executeScript(
                "var elem = arguments[0]; " +
                "var rect = elem.getBoundingClientRect(); " +
                "return rect.top >= 0 && rect.left >= 0 && rect.bottom <= window.innerHeight && rect.right <= window.innerWidth;",
                element
            );
        } catch (Exception e) {
            return false;
        }
    }

    public int getPageHeight() {
        return ((Number) js.executeScript("return document.body.scrollHeight;")).intValue();
    }

    public int getCurrentScrollPosition() {
        return ((Number) js.executeScript("return window.pageYOffset;")).intValue();
    }

    public int getWindowHeight() {
        return ((Number) js.executeScript("return window.innerHeight;")).intValue();
    }

    public void scrollToElementAndClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        scrollToElement(element);
        try {
            Thread.sleep(500);  
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public String getBottomText() {
        scrollToBottom();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(bottomText)).getText();
    }
}
