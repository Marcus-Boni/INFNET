package com.automacao.tests;

import com.automacao.base.BaseTest;
import com.automacao.pages.CartPage;
import com.automacao.pages.ScrollPage;
import com.automacao.utils.ScreenshotManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Exercise7ScreenshotsTest extends BaseTest {
    private ScreenshotManager screenshotManager;
    private ScrollPage scrollPage;

    @Override
    public void setUp() {
        super.setUp();
        screenshotManager = new ScreenshotManager(driver);
        scrollPage = new ScrollPage(driver);
    }

    @Test
    public void testFullPageScreenshots() {
        try {
            System.out.println("\n=== TESTE 1: CAPTURA DE PÁGINA INTEIRA ===");
            
            System.out.println("Capturando página inicial...");
            String initialPagePath = screenshotManager.takeScreenshot("01_homepage_inicial");
            System.out.println("✓ Screenshot salvo: " + initialPagePath);
            
            System.out.println("Navegando para produtos...");
            CartPage cartPage = new CartPage(driver);
            cartPage.clickProductsLink();
            waitSeconds(3);
            
            System.out.println("Capturando página de produtos...");
            String productsPagePath = screenshotManager.takeScreenshot("02_products_page");
            System.out.println("✓ Screenshot salvo: " + productsPagePath);
            
            System.out.println("\n✓ Captura de páginas inteiras concluída");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("fullpage_screenshots");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testElementScreenshots() {
        try {
            System.out.println("\n=== TESTE 2: CAPTURA DE ELEMENTO ESPECÍFICO ===");
            
            System.out.println("Capturando logotipo...");
            try {
                WebElement logo = driver.findElement(By.xpath("//img[@alt='Website for automation practice']"));
                String logoPath = screenshotManager.takeElementScreenshot(logo, "logo_site");
                System.out.println("✓ Logo capturado: " + logoPath);
            } catch (Exception e) {
                System.out.println("⚠ Logo não encontrado, continuando com outros elementos");
            }
            
            System.out.println("Capturando barra de navegação...");
            try {
                WebElement navbar = driver.findElement(By.className("navbar"));
                String navbarPath = screenshotManager.takeElementScreenshot(navbar, "navbar");
                System.out.println("✓ Navbar capturada: " + navbarPath);
            } catch (Exception e) {
                System.out.println("⚠ Navbar não encontrada");
            }
            
            System.out.println("\n✓ Captura de elementos concluída");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("element_screenshots");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testFlowScreenshots() {
        try {
            System.out.println("\n=== TESTE 3: SCREENSHOTS DE FLUXO ===");
            
            System.out.println("1. Capturando estado ANTES de ação...");
            String beforePath = screenshotManager.takeScreenshot("fluxo_01_antes");
            System.out.println("✓ Screenshot ANTES: " + beforePath);
            
            System.out.println("\n2. Realizando ação (navegação)...");
            CartPage cartPage = new CartPage(driver);
            cartPage.clickProductsLink();
            waitSeconds(2);
            
            System.out.println("✓ Ação realizada");
            
            waitSeconds(2);
            
            System.out.println("\n3. Capturando estado DEPOIS da ação...");
            String afterPath = screenshotManager.takeScreenshot("fluxo_02_depois");
            System.out.println("✓ Screenshot DEPOIS: " + afterPath);
            
            System.out.println("\nFluxo documentado com 3 screenshots:");
            System.out.println("  1. Estado inicial");
            System.out.println("  2. Estado final");
            System.out.println("  3. Pronto para relatório");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("fluxo_screenshots");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testErrorScreenshots() {
        try {
            System.out.println("\n=== TESTE 4: SCREENSHOTS DE ERRO ===");
            
            System.out.println("Simulando cenário com erro...");
            
            try {
                driver.findElement(By.id("elemento_inexistente_que_causa_erro"));
            } catch (Exception e) {
                System.out.println("\n⚠ Erro detectado!");
                System.out.println("Tipo: " + e.getClass().getSimpleName());
                System.out.println("Mensagem: " + e.getMessage());
                
                System.out.println("\nCapturando screenshot do erro...");
                String errorPath = screenshotManager.takeErrorScreenshot("erro_elemento_nao_encontrado");
                System.out.println("✓ Screenshot de erro salvo: " + errorPath);
                
                System.out.println("\nInformações úteis para debug:");
                System.out.println("- URL atual: " + driver.getCurrentUrl());
                System.out.println("- Screenshot: " + errorPath);
                System.out.println("- Stack trace: Verifique logs");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Erro inesperado: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testSuccessScreenshots() {
        try {
            System.out.println("\n=== TESTE 5: SCREENSHOTS DE SUCESSO ===");
            
            System.out.println("Executando ação bem-sucedida...");
            
            CartPage cartPage = new CartPage(driver);
            cartPage.clickProductsLink();
            waitSeconds(3);
            
            System.out.println("✓ Ação concluída com sucesso");
            
            System.out.println("Capturando evidência de sucesso...");
            String successPath = screenshotManager.takeSuccessScreenshot("teste_com_sucesso");
            System.out.println("✓ Screenshot de sucesso salvo: " + successPath);
            
            System.out.println("\nEvidência disponível para:");
            System.out.println("- Relatório de teste");
            System.out.println("- Documentação");
            System.out.println("- Validação manual posterior");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("success_screenshots");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testScreenshotOrganization() {
        try {
            System.out.println("\n=== TESTE 6: ORGANIZAÇÃO DE SCREENSHOTS ===");
            
            screenshotManager.listAllScreenshots();
            
            System.out.println("Caminho dos screenshots: " + screenshotManager.getScreenshotPath());
            System.out.println("\nNomeação padrão:");
            System.out.println("- {nomeTeste}_{descricao}_{timestamp}.png");
            System.out.println("- Exemplo: login_correto_2024-01-15_14-30-25-123.png");
            
            System.out.println("\nOrganização recomendada:");
            System.out.println("screenshots/");
            System.out.println("├── 01_homepage/");
            System.out.println("├── 02_login/");
            System.out.println("├── 03_products/");
            System.out.println("├── 04_errors/");
            System.out.println("└── 05_success/");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }
}
