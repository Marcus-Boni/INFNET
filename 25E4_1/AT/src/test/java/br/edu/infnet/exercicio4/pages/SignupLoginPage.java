package br.edu.infnet.exercicio4.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupLoginPage extends BasePage {
    private By signupName = By.name("name");
    private By signupEmail = By.xpath("//input[@data-qa='signup-email']");
    private By signupButton = By.xpath("//button[@data-qa='signup-button']");
    
    private By loginEmail = By.xpath("//input[@data-qa='login-email']");
    private By loginPassword = By.xpath("//input[@data-qa='login-password']");
    private By loginButton = By.xpath("//button[@data-qa='login-button']");
    
    private By loginErrorMessage = By.xpath("//p[contains(text(), 'Your email or password is incorrect!')]");
    private By signupErrorMessage = By.xpath("//p[contains(text(), 'Email Address already exist!')]");

    public SignupLoginPage(WebDriver driver) {
        super(driver);
    }

    public void fillSignupForm(String name, String email) {
        type(signupName, name);
        type(signupEmail, email);
    }

    public void clickSignupButton() {
        click(signupButton);
    }

    public void fillLoginForm(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public boolean isLoginErrorDisplayed() {
        return isDisplayed(loginErrorMessage);
    }

    public String getLoginErrorMessage() {
        return getText(loginErrorMessage);
    }

    public boolean isSignupErrorDisplayed() {
        return isDisplayed(signupErrorMessage);
    }
}
