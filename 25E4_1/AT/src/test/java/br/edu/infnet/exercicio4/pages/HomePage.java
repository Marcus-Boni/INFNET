package br.edu.infnet.exercicio4.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    private By signupLoginLink = By.linkText("Signup / Login");
    private By loggedInUser = By.xpath("//li//a[contains(text(), 'Logged in as')]");
    private By deleteAccountLink = By.linkText("Delete Account");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateTo() {
        driver.get("https://automationexercise.com");
    }

    public void clickSignupLogin() {
        click(signupLoginLink);
    }

    public boolean isUserLoggedIn(String username) {
        try {
            String loggedText = getText(loggedInUser);
            return loggedText.contains(username);
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDeleteAccount() {
        click(deleteAccountLink);
    }

    public boolean isDeleteAccountVisible() {
        return isDisplayed(deleteAccountLink);
    }
}
