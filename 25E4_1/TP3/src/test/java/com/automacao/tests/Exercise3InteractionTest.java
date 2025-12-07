package com.automacao.tests;

import com.automacao.base.BaseTest;
import com.automacao.pages.RegisterPage;
import com.automacao.pages.ContactPage;
import com.automacao.pages.ReviewPage;
import com.automacao.utils.ScreenshotManager;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Exercise3InteractionTest extends BaseTest {
    private ScreenshotManager screenshotManager;

    @Override
    public void setUp() {
        super.setUp();
        screenshotManager = new ScreenshotManager(driver);
    }

    @Test
    public void testRegisterUser() {
        try {
            RegisterPage registerPage = new RegisterPage(driver);
            
            registerPage.clickSignupLink();
            waitSeconds(2);
            
            String testEmail = "usuario" + System.currentTimeMillis() + "@test.com";
            registerPage.enterSignupName("João Silva");
            registerPage.enterSignupEmail(testEmail);
            registerPage.clickSignupButton();
            waitSeconds(2);
            
            screenshotManager.takeScreenshot("antes_preencher_detalhes");
            
            registerPage.selectTitle();
            registerPage.enterPassword("Senha123!");
            registerPage.enterFirstName("João");
            registerPage.enterLastName("Silva");
            registerPage.enterCompany("Empresa XYZ");
            registerPage.enterAddress("Rua Teste, 123");
            registerPage.selectCountry("United States");
            registerPage.enterState("California");
            registerPage.enterCity("Los Angeles");
            registerPage.enterZip("90001");
            registerPage.enterPhone("1234567890");
            
            screenshotManager.takeScreenshot("antes_criar_conta");
            
            registerPage.clickCreateAccountButton();
            waitSeconds(3);
            
            String confirmMessage = registerPage.getConfirmMessage();
            assertNotNull(confirmMessage);
            assertTrue(confirmMessage.contains("Account Created"));
            
            screenshotManager.takeSuccessScreenshot("registro_usuario");
            System.out.println("✓ Teste de registro de usuário passou");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("registro_usuario");
            System.err.println("✗ Erro no teste de registro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testContactUsForm() {
        try {
            ContactPage contactPage = new ContactPage(driver);
            
            contactPage.clickContactLink();
            waitSeconds(2);
            
            contactPage.enterName("Maria Santos");
            contactPage.enterEmail("maria@test.com");
            contactPage.enterSubject("Teste de Contato");
            contactPage.enterMessage("Esta é uma mensagem de teste para validar o formulário de contato.");
            
            screenshotManager.takeScreenshot("formulario_contato_preenchido");
            
            contactPage.clickSubmitButton();
            waitSeconds(3);
            
            assertTrue(contactPage.isSuccessMessageDisplayed());
            
            screenshotManager.takeSuccessScreenshot("contato_enviado");
            System.out.println("✓ Teste de formulário de contato passou");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("contato");
            System.err.println("✗ Erro no teste de contato: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAddProductReview() {
        try {
            ReviewPage reviewPage = new ReviewPage(driver);
            
            reviewPage.clickProductLink();
            waitSeconds(2);
            
            reviewPage.clickReviewTab();
            waitSeconds(2);
            
            screenshotManager.takeScreenshot("formulario_review");
            
            reviewPage.enterReviewerName("Pedro Oliveira");
            reviewPage.enterReviewerEmail("pedro@test.com");
            reviewPage.selectRating("5");
            reviewPage.enterReviewText("Produto excelente! Muito satisfeito com a qualidade e entrega rápida.");
            
            screenshotManager.takeScreenshot("review_preenchido");
            
            reviewPage.submitReview();
            waitSeconds(2);
            
            assertTrue(reviewPage.isReviewSuccessful());
            
            screenshotManager.takeSuccessScreenshot("review_adicionado");
            System.out.println("✓ Teste de adição de review passou");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("review");
            System.err.println("✗ Erro no teste de review: " + e.getMessage());
            throw e;
        }
    }
}
