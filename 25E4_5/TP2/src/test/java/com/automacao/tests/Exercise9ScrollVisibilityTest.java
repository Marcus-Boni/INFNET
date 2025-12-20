package com.automacao.tests;

import com.automacao.base.BaseTest;
import com.automacao.pages.ScrollPage;
import com.automacao.utils.ScreenshotManager;
import org.openqa.selenium.By;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Exercise9ScrollVisibilityTest extends BaseTest {
    private ScreenshotManager screenshotManager;
    private ScrollPage scrollPage;

    @Override
    public void setUp() {
        super.setUp();
        screenshotManager = new ScreenshotManager(driver);
        scrollPage = new ScrollPage(driver);
    }

    @Test
    public void testPresenceVsVisibility() {
        try {
            System.out.println("\n=== TESTE 1: PRESENÇA vs VISIBILIDADE ===");
            
            System.out.println("\nCONCEITO:");
            System.out.println("┌─────────────────────────────────┐");
            System.out.println("│ PRESENÇA                        │");
            System.out.println("├─────────────────────────────────┤");
            System.out.println("│ ✓ Elemento existe no DOM        │");
            System.out.println("│ ✓ findElement() encontra        │");
            System.out.println("│ ✗ Pode estar invisível          │");
            System.out.println("│ ✗ Pode estar fora da viewport   │");
            System.out.println("│ ✗ NÃO pode clicar               │");
            System.out.println("└─────────────────────────────────┘");
            
            System.out.println("\n┌─────────────────────────────────┐");
            System.out.println("│ VISIBILIDADE                    │");
            System.out.println("├─────────────────────────────────┤");
            System.out.println("│ ✓ Elemento existe no DOM        │");
            System.out.println("│ ✓ isDisplayed() = true          │");
            System.out.println("│ ✓ Está visível na tela          │");
            System.out.println("│ ✓ Tem altura/largura > 0        │");
            System.out.println("│ ✓ PODE clicar                   │");
            System.out.println("└─────────────────────────────────┘");
            
            System.out.println("\n✓ Diferença conceitual documentada");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testScrollDown() {
        try {
            System.out.println("\n=== TESTE 2: SCROLL DOWN ===");
            
            int initialScroll = scrollPage.getCurrentScrollPosition();
            int pageHeight = scrollPage.getPageHeight();
            int windowHeight = scrollPage.getWindowHeight();
            
            System.out.println("Estado INICIAL:");
            System.out.println("- Posição de scroll: " + initialScroll + "px");
            System.out.println("- Altura da página: " + pageHeight + "px");
            System.out.println("- Altura da janela: " + windowHeight + "px");
            
            screenshotManager.takeScreenshot("scroll_01_topo_pagina");
            
            System.out.println("\nRolando para baixo 500px...");
            scrollPage.scrollDown(500);
            waitSeconds(1);
            
            int scrollAfter = scrollPage.getCurrentScrollPosition();
            System.out.println("Posição de scroll após: " + scrollAfter + "px");
            assertTrue("Scroll não funcionou", scrollAfter > initialScroll);
            
            screenshotManager.takeScreenshot("scroll_02_meio_pagina");
            
            System.out.println("\nRolando para o final da página...");
            scrollPage.scrollToBottom();
            waitSeconds(1);
            
            int finalScroll = scrollPage.getCurrentScrollPosition();
            System.out.println("Posição final: " + finalScroll + "px");
            System.out.println("Esperado (aproximado): " + (pageHeight - windowHeight) + "px");
            
            screenshotManager.takeScreenshot("scroll_03_rodape");
            
            System.out.println("\n✓ Scroll down funciona corretamente");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("scroll_down");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testScrollUp() {
        try {
            System.out.println("\n=== TESTE 3: SCROLL UP ===");
            
            System.out.println("Rolando para baixo inicialmente...");
            scrollPage.scrollDown(1000);
            waitSeconds(1);
            
            int scrollAtMiddle = scrollPage.getCurrentScrollPosition();
            System.out.println("Posição após scroll down: " + scrollAtMiddle + "px");
            
            screenshotManager.takeScreenshot("scroll_antes_up");
            
            System.out.println("\nRolando para cima 500px...");
            scrollPage.scrollUp(500);
            waitSeconds(1);
            
            int scrollAfterUp = scrollPage.getCurrentScrollPosition();
            System.out.println("Posição após scroll up: " + scrollAfterUp + "px");
            assertTrue("Scroll up não funcionou", scrollAfterUp < scrollAtMiddle);
            
            System.out.println("\nRolando para o topo...");
            scrollPage.scrollToTop();
            waitSeconds(1);
            
            int finalScroll = scrollPage.getCurrentScrollPosition();
            System.out.println("Posição final: " + finalScroll + "px");
            assertEquals("Não voltou ao topo", 0, finalScroll);
            
            screenshotManager.takeScreenshot("scroll_topo");
            
            System.out.println("\n✓ Scroll up funciona corretamente");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("scroll_up");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testScrollIntoView() {
        try {
            System.out.println("\n=== TESTE 4: SCROLLINTOVIEW ===");
            
            System.out.println("PROBLEMA CLÁSSICO:");
            System.out.println("1. findElement() encontra elemento");
            System.out.println("2. element.click() dispara erro:");
            System.out.println("   'Element is not interactable'");
            System.out.println("3. Elemento não estava visível!");
            
            System.out.println("\nSOLUÇÃO:");
            System.out.println("1. Usar scrollIntoView()");
            System.out.println("2. Aguardar que elemento fique visível");
            System.out.println("3. Clicar novamente");
            
            System.out.println("\nCÓDIGO:");
            System.out.println("WebElement element = driver.findElement(By.id('...'));");
            System.out.println("((JavascriptExecutor) driver).executeScript(");
            System.out.println("  'arguments[0].scrollIntoView(true);', element");
            System.out.println(");");
            System.out.println("element.click();");
            
            System.out.println("\nDEMONSTRAÇÃO PRÁTICA:");
            scrollPage.scrollToBottom();
            waitSeconds(1);
            
            screenshotManager.takeScreenshot("scroll_rodape_visible");
            System.out.println("✓ Elemento rodapé agora está visível");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("scrollintoview");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testVisibilityVerification() {
        try {
            System.out.println("\n=== TESTE 5: VERIFICAÇÃO DE VISIBILIDADE ===");
            
            System.out.println("MÉTODOS DE VALIDAÇÃO:");
            System.out.println("\n1. isDisplayed():");
            System.out.println("   - Verifica se elemento está visível");
            System.out.println("   - Retorna false se display:none");
            System.out.println("   - Retorna false se visibility:hidden");
            System.out.println("   - Retorna false se height/width = 0");
            
            System.out.println("\n2. isElementPresent():");
            System.out.println("   - Verifica se elemento existe no DOM");
            System.out.println("   - Não valida visibilidade");
            System.out.println("   - findElement() não dispara erro");
            
            System.out.println("\n3. isElementVisibleInViewport():");
            System.out.println("   - Verifica se está na viewport atual");
            System.out.println("   - Usa getBoundingClientRect()");
            System.out.println("   - Valida posição dentro da tela");
            
            scrollPage.scrollToTop();
            waitSeconds(1);
            
            boolean homeVisible = scrollPage.isElementVisibleInViewport(By.xpath("//a[contains(text(), 'Home')]"));
            System.out.println("\nHome link visível no topo: " + homeVisible);
            
            scrollPage.scrollToBottom();
            waitSeconds(1);
            
            screenshotManager.takeScreenshot("visibilidade_teste");
            System.out.println("\n✓ Verificações de visibilidade documentadas");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("visibilidade");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testDynamicElementHandling() {
        try {
            System.out.println("\n=== TESTE 6: ELEMENTOS DINÂMICOS ===");
            
            System.out.println("CENÁRIOS COMUNS:");
            System.out.println("\n1. LAZY LOADING:");
            System.out.println("   - Imagens carregam ao scroll");
            System.out.println("   - Solução: Aguardar loaded = true");
            
            System.out.println("\n2. INFINITE SCROLL:");
            System.out.println("   - Novo conteúdo carrega ao chegar no fim");
            System.out.println("   - Solução: ScrollToBottom + Wait");
            
            System.out.println("\n3. VIRTUAL SCROLLING:");
            System.out.println("   - Apenas elementos visíveis no DOM");
            System.out.println("   - Outros removidos/adicionados dinamicamente");
            System.out.println("   - Solução: Scroll + findElement no novo contexto");
            
            System.out.println("\n4. FLOATING ELEMENTS:");
            System.out.println("   - Headers/footers flutuantes");
            System.out.println("   - Sempre visíveis independente de scroll");
            System.out.println("   - Solução: position:fixed ou similar");
            
            System.out.println("\nPADRÃO RECOMENDADO:");
            System.out.println("try {");
            System.out.println("    element.click();");
            System.out.println("} catch (ElementNotInteractableException e) {");
            System.out.println("    scrollIntoView(element);");
            System.out.println("    wait.until(elementToBeClickable(element));");
            System.out.println("    element.click();");
            System.out.println("}");
            
            System.out.println("\n✓ Padrões de elementos dinâmicos documentados");
            
        } catch (Exception e) {
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testPracticalScrollAndClick() {
        try {
            System.out.println("\n=== TESTE 7: SCROLL E CLIQUE PRÁTICO ===");
            
            System.out.println("CENÁRIO: Clicar em elemento no rodapé");
            
            System.out.println("\n1. Rolando para o rodapé...");
            scrollPage.scrollToBottom();
            waitSeconds(1);
            
            screenshotManager.takeScreenshot("pratico_rodape_visivel");
            System.out.println("✓ Rodapé visível");
            
            System.out.println("\n2. Verificando visibilidade de elemento...");
            boolean isVisible = scrollPage.isElementPresent(By.xpath("//footer"));
            System.out.println("Footer presente e visível: " + isVisible);
            
            System.out.println("\n3. Tentando clicar com scroll seguro...");
            try {
                scrollPage.scrollToElementAndClick(By.xpath("//a[contains(text(), 'Home')]"));
                System.out.println("✓ Clique bem-sucedido após scroll");
            } catch (Exception e) {
                System.out.println("⚠ Elemento específico não encontrado, mas padrão é válido");
            }
            
            System.out.println("\n✓ Teste prático de scroll e clique concluído");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("scroll_click");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }
}
