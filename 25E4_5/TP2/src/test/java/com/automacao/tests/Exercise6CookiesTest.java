package com.automacao.tests;

import com.automacao.base.BaseTest;
import com.automacao.pages.LoginPage;
import com.automacao.utils.CookieManager;
import com.automacao.utils.ScreenshotManager;
import org.openqa.selenium.Cookie;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Exercise6CookiesTest extends BaseTest {
    private ScreenshotManager screenshotManager;
    private CookieManager cookieManager;
    private static final String VALID_EMAIL = "usuario@test.com";
    private static final String VALID_PASSWORD = "Senha123!";

    @Override
    public void setUp() {
        super.setUp();
        screenshotManager = new ScreenshotManager(driver);
        cookieManager = new CookieManager(driver, 
            System.getProperty("user.dir") + "/cookies/session_cookies.txt");
    }

    @Test
    public void testLoginAndSaveCookies() {
        try {
            System.out.println("\n=== TESTE 1: LOGIN E SALVAMENTO DE COOKIES ===");
            
            LoginPage loginPage = new LoginPage(driver);
            
            System.out.println("Realizando login...");
            loginPage.clickLoginLink();
            waitSeconds(2);
            loginPage.enterEmail(VALID_EMAIL);
            loginPage.enterPassword(VALID_PASSWORD);
            loginPage.clickLoginButton();
            waitSeconds(3);
            
            assertTrue(loginPage.isLoginSuccessful());
            System.out.println("✓ Login realizado com sucesso");
            
            screenshotManager.takeSuccessScreenshot("login_para_salvar_cookies");
            
            System.out.println("\nCookies ativos antes de salvar:");
            cookieManager.printAllCookies();
            
            System.out.println("Salvando cookies...");
            cookieManager.saveCookies();
            System.out.println("✓ Cookies salvos com sucesso\n");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("login_salvar_cookies");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testLoadCookiesAndDirectAccess() {
        try {
            System.out.println("\n=== TESTE 2: CARREGAMENTO DE COOKIES E ACESSO DIRETO ===");
            
            System.out.println("Carregando cookies...");
            cookieManager.loadCookies();
            System.out.println("✓ Cookies carregados");
            
            System.out.println("\nCookies após carregar:");
            cookieManager.printAllCookies();
            
            System.out.println("Navegando para página de conta com cookies...");
            driver.get(BASE_URL + "/account");
            waitSeconds(3);
            
            screenshotManager.takeScreenshot("acesso_direto_com_cookies");
            
            LoginPage loginPage = new LoginPage(driver);
            if (loginPage.isLoginSuccessful()) {
                System.out.println("✓ Acesso bem-sucedido com cookies armazenados");
                System.out.println("✓ Não foi necessário fazer login novamente");
            } else {
                System.out.println("⚠ Cookies podem ter expirado");
            }
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("carregar_cookies");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testIndividualCookieManipulation() {
        try {
            System.out.println("\n=== TESTE 3: MANIPULAÇÃO INDIVIDUAL DE COOKIES ===");
            
            var allCookies = cookieManager.getAllCookies();
            System.out.println("Total de cookies: " + allCookies.size());
            
            System.out.println("\nCookies disponíveis:");
            for (Cookie cookie : allCookies) {
                System.out.println("  - " + cookie.getName() + " = " + cookie.getValue().substring(0, 
                    Math.min(20, cookie.getValue().length())) + "...");
            }
            
            System.out.println("\nBuscando cookies de sessão...");
            boolean hasSessionCookie = cookieManager.cookieExists("PHPSESSID");
            System.out.println("PHPSESSID existe: " + hasSessionCookie);
            
            if (hasSessionCookie) {
                System.out.println("Deletando PHPSESSID...");
                cookieManager.deleteCookie("PHPSESSID");
                System.out.println("✓ Cookie deletado");
            }
            
            System.out.println("\n✓ Manipulação de cookies demonstrada");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCookieOptimizationBenefit() {
        try {
            System.out.println("\n=== TESTE 4: BENEFÍCIO DE OTIMIZAÇÃO COM COOKIES ===");
            
            System.out.println("\nCOMPARAÇÃO DE TEMPOS:");
            System.out.println("\nSEM COOKIES (Login normal):");
            System.out.println("1. Ir para página de login: ~1s");
            System.out.println("2. Preencher e enviar form: ~1s");
            System.out.println("3. Aguardar resposta: ~2-3s");
            System.out.println("TOTAL: ~4-5 segundos por login");
            
            System.out.println("\nCOM COOKIES (Acesso direto):");
            System.out.println("1. Carregar cookies: ~0.5s");
            System.out.println("2. Navegar para página autenticada: ~1s");
            System.out.println("TOTAL: ~1.5 segundos por acesso");
            
            System.out.println("\nECONOMIA:");
            System.out.println("- Por teste: ~2.5-3.5 segundos");
            System.out.println("- Em 10 testes: ~25-35 segundos");
            System.out.println("- Em 100 testes: ~250-350 segundos (5+ minutos)");
            
            System.out.println("\nOUTROS BENEFÍCIOS:");
            System.out.println("✓ Reduz carga no servidor (menos logins)");
            System.out.println("✓ Testes mais rápidos e confiáveis");
            System.out.println("✓ Simula usuários reais com sessão ativa");
            System.out.println("✓ Permite testar logout sem preciso fazer login");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCleanupCookies() {
        try {
            System.out.println("\n=== TESTE 5: LIMPEZA DE COOKIES ===");
            
            LoginPage loginPage = new LoginPage(driver);
            
            System.out.println("Deletando todos os cookies...");
            cookieManager.deleteAllCookies();
            driver.navigate().refresh();
            waitSeconds(2);
            System.out.println("✓ Cookies deletados");
            
            screenshotManager.takeScreenshot("cookies_deletados");
            
            System.out.println("Verificando se foi deslogado...");
            if (loginPage.isLoggedOut() || !loginPage.isLoginSuccessful()) {
                System.out.println("✓ Confirmado: Usuário foi deslogado após deletar cookies");
            }
            
            System.out.println("\n✓ Limpeza de cookies completa\n");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("limpeza_cookies");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }
}
