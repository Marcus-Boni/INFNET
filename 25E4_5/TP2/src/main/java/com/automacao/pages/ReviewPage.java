package com.automacao.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ReviewPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By productLink = By.xpath("//a[@href='/product/1']");
    private By reviewTab = By.xpath("//a[contains(text(), 'Write Your Review')]");
    private By nameField = By.id("name");
    private By emailField = By.id("email");
    private By reviewTextField = By.id("review");
    private By reviewRating = By.id("rating");
    private By submitReviewButton = By.id("button-review");
    private By successMessage = By.xpath("//span[contains(text(), 'Thank you')]");
    private By reviewsList = By.xpath("//div[@class='review-item']");
    private By starRatings = By.xpath("//input[@name='rating']");

    public ReviewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickProductLink() {
        wait.until(ExpectedConditions.elementToBeClickable(productLink)).click();
    }

    public void clickReviewTab() {
        wait.until(ExpectedConditions.elementToBeClickable(reviewTab)).click();
    }

    public void enterReviewerName(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
    }

    public void enterReviewerEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterReviewText(String reviewText) {
        driver.findElement(reviewTextField).sendKeys(reviewText);
    }

    public void selectRating(String rating) {
        /*
         * Estratégia para selecionar rating:
         * Localiza o radio button correspondente ao rating desejado
         * Simula clique do usuário
         */
        By ratingButton = By.xpath("//input[@name='rating'][@value='" + rating + "']");
        driver.findElement(ratingButton).click();
    }

    public void submitReview() {
        driver.findElement(submitReviewButton).click();
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }

    public int getTotalReviews() {
        return driver.findElements(reviewsList).size();
    }

    public boolean isReviewSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
