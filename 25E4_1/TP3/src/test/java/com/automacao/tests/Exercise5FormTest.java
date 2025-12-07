package com.automacao.tests;

import com.automacao.base.BaseTest;
import com.automacao.pages.ContactPage;
import com.automacao.utils.ScreenshotManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Exercise5FormTest extends BaseTest {
    private ScreenshotManager screenshotManager;

    @Override
    public void setUp() {
        super.setUp();
        screenshotManager = new ScreenshotManager(driver);
    }

    @Test
    public void testCompleteFormFilling() {
        try {
            ContactPage contactPage = new ContactPage(driver);
            
            contactPage.clickContactLink();
            waitSeconds(2);
            
            screenshotManager.takeScreenshot("formulario_vazio");
            
            System.out.println("\n--- Preenchendo campos de texto ---");
            
            String name = "Carlos Alberto";
            String email = "carlos@email.com";
            String subject = "Dúvida sobre Frete";
            String message = "Gostaria de saber como funciona o sistema de frete. " +
                           "Vocês entregam em todo o Brasil?";
            
            contactPage.enterName(name);
            contactPage.enterEmail(email);
            contactPage.enterSubject(subject);
            contactPage.enterMessage(message);
            
            System.out.println("✓ Nome: " + name);
            System.out.println("✓ Email: " + email);
            System.out.println("✓ Assunto: " + subject);
            System.out.println("✓ Mensagem: " + message);
            
            screenshotManager.takeScreenshot("formulario_preenchido_texto");
            waitSeconds(2);
            
            System.out.println("\n--- Validando campos preenchidos ---");
            
            String inputName = driver.findElement(By.xpath("//input[@data-qa='name']"))
                .getAttribute("value");
            assertEquals("Nome não foi preenchido corretamente", name, inputName);
            System.out.println("✓ Validação de nome: OK");
            
            String inputEmail = driver.findElement(By.xpath("//input[@data-qa='email']"))
                .getAttribute("value");
            assertEquals("Email não foi preenchido corretamente", email, inputEmail);
            System.out.println("✓ Validação de email: OK");
            
            System.out.println("\n--- Enviando formulário ---");
            
            screenshotManager.takeScreenshot("antes_enviar_formulario");
            
            contactPage.clickSubmitButton();
            waitSeconds(3);
            
            System.out.println("✓ Formulário enviado");
            
            System.out.println("\n--- Validando resposta do servidor ---");
            
            assertTrue("Mensagem de sucesso não foi exibida", contactPage.isSuccessMessageDisplayed());
            System.out.println("✓ Mensagem de sucesso exibida");
            
            screenshotManager.takeSuccessScreenshot("formulario_enviado_sucesso");
            
            System.out.println("✓ Teste de formulário completo passou");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("formulario");
            System.err.println("✗ Erro no teste de formulário: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testRadioButtonsAndCheckboxes() {
        try {
            System.out.println("\n--- TESTE DE RADIO BUTTONS E CHECKBOXES ---");
            
            System.out.println("Estrutura de trabalho com Radio Buttons:");
            System.out.println("1. By.id('id_gender1') - Seleciona 'Mr'");
            System.out.println("2. .isSelected() - Verifica se está selecionado");
            System.out.println("3. .click() - Clica para selecionar");
            
            System.out.println("\nEstrutura de trabalho com Checkboxes:");
            System.out.println("1. By.id('newsletter') - Seleciona newsletter");
            System.out.println("2. .isSelected() - Verifica se está marcado");
            System.out.println("3. .click() - Clica para marcar/desmarcar");
            
            System.out.println("\n✓ Padrões documentados");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testDropdownManipulation() {
        try {
            System.out.println("\n--- TESTE DE DROPDOWNS ---");
            
            System.out.println("\nMétodos para trabalhar com Dropdowns:");
            System.out.println("1. selectByVisibleText(String text) - Seleciona por texto visível");
            System.out.println("2. selectByValue(String value) - Seleciona por atributo 'value'");
            System.out.println("3. selectByIndex(int index) - Seleciona por índice");
            System.out.println("4. getFirstSelectedOption() - Obtém opção selecionada");
            System.out.println("5. getAllSelectedOptions() - Obtém todas as opções em multi-select");
            
            System.out.println("\nExemplo de uso:");
            System.out.println("Select countrySelect = new Select(driver.findElement(By.id('country')));");
            System.out.println("countrySelect.selectByVisibleText('United States');");
            System.out.println("String selected = countrySelect.getFirstSelectedOption().getText();");
            
            System.out.println("\n✓ Padrões de dropdown documentados");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAlertHandling() {
        try {
            System.out.println("\n--- TESTE DE ALERTAS ---");
            
            System.out.println("\nTratamento de alertas JavaScript:");
            System.out.println("1. ACEITAR ALERTA:");
            System.out.println("   Alert alert = driver.switchTo().alert();");
            System.out.println("   alert.accept();");
            
            System.out.println("\n2. REJEITAR ALERTA:");
            System.out.println("   Alert alert = driver.switchTo().alert();");
            System.out.println("   alert.dismiss();");
            
            System.out.println("\n3. LER TEXTO DO ALERTA:");
            System.out.println("   String alertText = alert.getText();");
            
            System.out.println("\n4. DIGITAR EM PROMPT:");
            System.out.println("   alert.sendKeys('Seu nome');");
            System.out.println("   alert.accept();");
            
            System.out.println("\nPadrão de tratamento:");
            System.out.println("try {");
            System.out.println("    Alert alert = driver.switchTo().alert();");
            System.out.println("    System.out.println(alert.getText());");
            System.out.println("    alert.accept();");
            System.out.println("} catch (NoAlertPresentException e) {");
            System.out.println("    System.out.println('Alerta não presente');");
            System.out.println("}");
            
            System.out.println("\n✓ Padrões de alerta documentados");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }
}
