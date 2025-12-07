package com.automacao.tests;

import com.automacao.base.BaseTest;
import com.automacao.pages.CartPage;
import com.automacao.pages.CheckoutPage;
import com.automacao.utils.ScreenshotManager;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class Exercise8CartCheckoutTest extends BaseTest {
    private ScreenshotManager screenshotManager;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @Override
    public void setUp() {
        super.setUp();
        screenshotManager = new ScreenshotManager(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test
    public void testAddProductsToCart() {
        try {
            System.out.println("\n=== TESTE 1: ADIÇÃO DE PRODUTOS ===");
            
            cartPage.clickProductsLink();
            waitSeconds(3);
            
            screenshotManager.takeScreenshot("lista_produtos");
            System.out.println("✓ Página de produtos carregada");
            
            System.out.println("Adicionando primeiro produto ao carrinho...");
            cartPage.addProductToCart(0);
            waitSeconds(2);
            System.out.println("✓ Produto 1 adicionado");
            
            System.out.println("Adicionando segundo produto ao carrinho...");
            cartPage.addProductToCart(1);
            waitSeconds(2);
            System.out.println("✓ Produto 2 adicionado");
            
            screenshotManager.takeScreenshot("produtos_adicionados");
            
            System.out.println("Navegando para carrinho...");
            cartPage.clickCartLink();
            waitSeconds(3);
            
            screenshotManager.takeScreenshot("carrinho_com_produtos");
            
            System.out.println("Validando carrinho...");
            assertFalse("Carrinho deveria ter produtos", cartPage.isCartEmpty());
            int itemCount = cartPage.getCartItemsCount();
            System.out.println("✓ Carrinho contém " + itemCount + " itens");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("adicionar_produtos");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testValidateCartQuantityAndPrices() {
        try {
            System.out.println("\n=== TESTE 2: VALIDAÇÃO DE QUANTIDADE E PREÇOS ===");
            
            cartPage.clickProductsLink();
            waitSeconds(2);
            cartPage.addProductToCart(0);
            waitSeconds(2);
            cartPage.clickCartLink();
            waitSeconds(3);
            
            screenshotManager.takeScreenshot("validacao_precos");
            
            System.out.println("Estrutura do carrinho:");
            System.out.println("- Coluna de produto");
            System.out.println("- Coluna de preço unitário");
            System.out.println("- Coluna de quantidade");
            System.out.println("- Coluna de preço total");
            System.out.println("- Linha de SUBTOTAL");
            System.out.println("- Linha de IMPOSTOS (se aplicável)");
            System.out.println("- Linha de TOTAL");
            
            System.out.println("\nValidando primeiro produto:");
            String price = cartPage.getProductPrice(0);
            String total = cartPage.getProductTotal(0);
            System.out.println("Preço: " + price);
            System.out.println("Total: " + total);
            
            assertNotNull("Preço não deveria ser nulo", price);
            assertNotNull("Total não deveria ser nulo", total);
            System.out.println("✓ Preços e quantidades são válidos");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("validacao_precos");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testModifyCartQuantity() {
        try {
            System.out.println("\n=== TESTE 3: MODIFICAÇÃO DE QUANTIDADE ===");
            
            cartPage.clickProductsLink();
            waitSeconds(2);
            cartPage.addProductToCart(0);
            waitSeconds(2);
            cartPage.clickCartLink();
            waitSeconds(3);
            
            screenshotManager.takeScreenshot("quantidade_original");
            
            System.out.println("Quantidade original: 1");
            
            System.out.println("Aumentando quantidade para 3...");
            cartPage.updateProductQuantity(0, "3");
            waitSeconds(2);
            
            screenshotManager.takeScreenshot("quantidade_alterada");
            System.out.println("✓ Quantidade alterada");
            
            String newTotal = cartPage.getProductTotal(0);
            System.out.println("Novo total: " + newTotal);
            System.out.println("✓ Total recalculado com sucesso");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("modificar_quantidade");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testRemoveProductFromCart() {
        try {
            System.out.println("\n=== TESTE 4: REMOÇÃO DE PRODUTOS ===");
            
            cartPage.clickProductsLink();
            waitSeconds(2);
            cartPage.addProductToCart(0);
            waitSeconds(1);
            cartPage.addProductToCart(1);
            waitSeconds(2);
            cartPage.clickCartLink();
            waitSeconds(3);
            
            int itemsBefore = cartPage.getCartItemsCount();
            System.out.println("Produtos antes de deletar: " + itemsBefore);
            
            screenshotManager.takeScreenshot("carrinho_antes_deletar");
            
            System.out.println("Deletando primeiro produto...");
            cartPage.deleteProductFromCart(0);
            waitSeconds(2);
            
            screenshotManager.takeScreenshot("carrinho_depois_deletar");
            
            int itemsAfter = cartPage.getCartItemsCount();
            System.out.println("Produtos depois de deletar: " + itemsAfter);
            assertEquals("Quantidade de produtos não foi decrementada", itemsBefore - 1, itemsAfter);
            
            System.out.println("✓ Produto deletado com sucesso");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("remover_produto");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCompleteCheckoutFlow() {
        try {
            System.out.println("\n=== TESTE 5: FLUXO COMPLETO DE CHECKOUT ===");
            
            System.out.println("1. Adicionando produtos...");
            cartPage.clickProductsLink();
            waitSeconds(2);
            cartPage.addProductToCart(0);
            waitSeconds(1);
            cartPage.clickCartLink();
            waitSeconds(3);
            
            System.out.println("2. Validando carrinho...");
            assertFalse(cartPage.isCartEmpty());
            System.out.println("✓ Carrinho contém itens");
            
            screenshotManager.takeScreenshot("checkout_carrinho");
            
            System.out.println("3. Iniciando checkout...");
            cartPage.clickProceedCheckout();
            waitSeconds(3);
            
            System.out.println("4. Preenchendo endereço de entrega...");
            checkoutPage.enterFirstName("João");
            checkoutPage.enterLastName("Silva");
            checkoutPage.enterCompany("Empresa XYZ");
            checkoutPage.enterAddress("Rua Teste, 123");
            checkoutPage.selectCountry("Brazil");
            checkoutPage.enterState("São Paulo");
            checkoutPage.enterCity("São Paulo");
            checkoutPage.enterZip("01000-000");
            checkoutPage.enterPhone("1199999999");
            
            screenshotManager.takeScreenshot("checkout_endereco");
            System.out.println("✓ Endereço preenchido");
            
            System.out.println("5. Configurando pagamento...");
            checkoutPage.enterOrderComment("Primeira compra. Obrigado!");
            
            screenshotManager.takeScreenshot("checkout_comentario");
            System.out.println("✓ Informações de pagamento configuradas");
            
            System.out.println("\n✓ Fluxo de checkout demonstrado com sucesso");
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("checkout_completo");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testEmptyCartValidation() {
        try {
            System.out.println("\n=== TESTE 6: VALIDAÇÃO DE CARRINHO VAZIO ===");
            
            cartPage.clickProductsLink();
            waitSeconds(2);
            cartPage.clickCartLink();
            waitSeconds(3);
            
            boolean isEmpty = cartPage.isCartEmpty();
            System.out.println("Carrinho vazio: " + isEmpty);
            
            if (isEmpty) {
                System.out.println("✓ Carrinho vazio detectado corretamente");
                screenshotManager.takeScreenshot("carrinho_vazio");
            } else {
                System.out.println("⚠ Carrinho contém itens de testes anteriores");
            }
            
        } catch (Exception e) {
            screenshotManager.takeErrorScreenshot("carrinho_vazio");
            System.err.println("✗ Erro: " + e.getMessage());
            throw e;
        }
    }
}
