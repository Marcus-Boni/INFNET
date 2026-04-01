package org.example.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("post-deploy")
@DisplayName("Pós-deploy - Smoke tests Selenium")
class PostDeploySmokeSeleniumTest {

    private HtmlUnitDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));

        String configuredBaseUrl = System.getProperty("tp5.base.url");
        if (configuredBaseUrl == null || configuredBaseUrl.isBlank()) {
            configuredBaseUrl = System.getenv().getOrDefault("TP5_BASE_URL", "http://127.0.0.1:8080");
        }
        baseUrl = configuredBaseUrl;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Listagem de produtos responde após deploy")
    void listagemProdutosDisponivel() {
        driver.get(baseUrl + "/produtos");
        assertThat(driver.getTitle()).contains("Produtos");
        assertThat(driver.getPageSource()).contains("Produtos Cadastrados");
    }

    @Test
    @DisplayName("Cadastro web funciona após deploy")
    void cadastroProdutoPosDeployFunciona() {
        String nomeProduto = "Smoke-" + System.currentTimeMillis();

        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys(nomeProduto);
        driver.findElement(By.id("descricao")).sendKeys("Cadastro via teste pós-deploy");
        driver.findElement(By.id("preco")).sendKeys("99.90");
        driver.findElement(By.id("estoque")).sendKeys("7");

        WebElement botaoSalvar = driver.findElement(By.id("btnSalvar"));
        botaoSalvar.click();

        assertThat(driver.getCurrentUrl()).contains("/produtos");
        assertThat(driver.getPageSource()).contains(nomeProduto);
    }
}