package com.automacao.tests;

import com.automacao.base.BaseTest;
import com.automacao.pages.LoginPage;
import com.automacao.utils.ScreenshotManager;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Exercise4LoginTest extends BaseTest {
    private ScreenshotManager screenshotManager;
    private static final String VALID_EMAIL = "usuario@test.com";
    private static final String VALID_PASSWORD = "Senha123!";
    private static final String INVALID_PASSWORD = "SenhaErrada123";

    @Override
    public void setUp() {
        super.setUp();
        screenshotManager = new ScreenshotManager(driver);
    }

    @Test
    public void testLoginWithCorrectCredentials() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            
            loginPage.clickLoginLink();
            waitSeconds(2);
            
            screenshotManager.takeScreenshot("pagina_login");
            
            loginPage.enterEmail(VALID_EMAIL);
            loginPage.enterPassword(VALID_PASSWORD);
            
            screenshotManager.takeScreenshot("formulario_login_preenchido");
            
            loginPage.clickLoginButton();
            waitSeconds(3);
            
            assertTrue("Login não foi bem-sucedido", loginPage.isLoginSuccessful());
            
            String currentURL = loginPage.getCurrentURL();
            System.out.println("URL após login: " + currentURL);
            assertTrue("URL não corresponde ao esperado", currentURL.contains("automationexercise.com"));
            
            screenshotManager.takeSuccessScreenshot("login_correto");
            System.out.println("✓ Teste de login com credenciais corretas passou");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("login_correto");
            System.err.println("✗ Erro no teste de login correto: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testLoginWithIncorrectCredentials() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            
            loginPage.clickLoginLink();
            waitSeconds(2);
            
            loginPage.enterEmail(VALID_EMAIL);
            loginPage.enterPassword(INVALID_PASSWORD);
            
            screenshotManager.takeScreenshot("login_credenciais_incorretas");
            
            loginPage.clickLoginButton();
            waitSeconds(2);
            
            assertTrue("Mensagem de erro não foi exibida", loginPage.isErrorMessageDisplayed());
            
            String errorMessage = loginPage.getErrorMessage();
            System.out.println("Mensagem de erro: " + errorMessage);
            assertFalse("Login não deveria ser bem-sucedido com credenciais incorretas", loginPage.isLoginSuccessful());
            
            screenshotManager.takeErrorScreenshot("login_incorreto");
            System.out.println("✓ Teste de login com credenciais incorretas passou");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("login_incorreto");
            System.err.println("✗ Erro no teste de login incorreto: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testLogoutUser() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            
            loginPage.clickLoginLink();
            waitSeconds(2);
            loginPage.enterEmail(VALID_EMAIL);
            loginPage.enterPassword(VALID_PASSWORD);
            loginPage.clickLoginButton();
            waitSeconds(3);
            
            assertTrue(loginPage.isLoginSuccessful());
            screenshotManager.takeScreenshot("usuario_logado");
            
            loginPage.clickLogoutLink();
            waitSeconds(3);
            
            assertTrue("Usuário não foi deslogado corretamente", loginPage.isLoggedOut());
            
            String currentURL = loginPage.getCurrentURL();
            System.out.println("URL após logout: " + currentURL);
            
            screenshotManager.takeSuccessScreenshot("logout");
            System.out.println("✓ Teste de logout passou");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("logout");
            System.err.println("✗ Erro no teste de logout: " + e.getMessage());
            throw e;
        }
    }
}
