package br.edu.infnet.exercicio4.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class SignupPage extends BasePage {
    private By genderMr = By.id("id_gender1");
    private By genderMrs = By.id("id_gender2");
    private By password = By.id("password");
    private By dayOfBirth = By.id("days");
    private By monthOfBirth = By.id("months");
    private By yearOfBirth = By.id("years");
    private By newsletter = By.id("newsletter");
    private By specialOffers = By.id("optin");
    private By firstName = By.id("first_name");
    private By lastName = By.id("last_name");
    private By company = By.id("company");
    private By address1 = By.id("address1");
    private By address2 = By.id("address2");
    private By country = By.id("country");
    private By state = By.id("state");
    private By city = By.id("city");
    private By zipcode = By.id("zipcode");
    private By mobileNumber = By.id("mobile_number");
    private By createAccountButton = By.xpath("//button[@data-qa='create-account']");
    
    private By accountCreatedMessage = By.xpath("//h2[@data-qa='account-created']");
    private By continueButton = By.xpath("//a[@data-qa='continue-button']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public void selectGender(String gender) {
        if (gender.equalsIgnoreCase("Mr")) {
            click(genderMr);
        } else {
            click(genderMrs);
        }
    }

    public void fillAccountInformation(String pwd, String day, String month, String year) {
        type(password, pwd);
        new Select(driver.findElement(dayOfBirth)).selectByValue(day);
        new Select(driver.findElement(monthOfBirth)).selectByValue(month);
        new Select(driver.findElement(yearOfBirth)).selectByValue(year);
    }

    public void fillAddressInformation(String fName, String lName, String comp, String addr1, 
                                      String addr2, String cntry, String st, String cty, 
                                      String zip, String mobile) {
        type(firstName, fName);
        type(lastName, lName);
        type(company, comp);
        type(address1, addr1);
        type(address2, addr2);
        new Select(driver.findElement(country)).selectByValue(cntry);
        type(state, st);
        type(city, cty);
        type(zipcode, zip);
        type(mobileNumber, mobile);
    }

    public void clickCreateAccount() {
        click(createAccountButton);
    }

    public boolean isAccountCreated() {
        return isDisplayed(accountCreatedMessage);
    }

    public void clickContinue() {
        click(continueButton);
    }
}
