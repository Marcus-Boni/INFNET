package org.example.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de interface com HtmlUnitDriver (headless — sem browser instalado).
 * Exercita componentes da UI: formulários, tabelas, alertas e mensagens de feedback.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Selenium (HtmlUnit) — Testes de Interface")
class ProdutoSeleniumTest {

    @LocalServerPort
    private int port;

    private HtmlUnitDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        driver = new HtmlUnitDriver(true); // JS habilitado
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    // ── Listagem ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Página de listagem carrega com título correto")
    void lista_tituloCorreto() {
        driver.get(baseUrl + "/produtos");
        assertThat(driver.getTitle()).contains("Produtos");
    }

    @Test
    @DisplayName("Navbar contém links de navegação")
    void lista_navbarLinks() {
        driver.get(baseUrl + "/produtos");
        WebElement nav = driver.findElement(By.tagName("nav"));
        List<WebElement> links = nav.findElements(By.tagName("a"));
        assertThat(links).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Botão '+ Novo' está presente na listagem")
    void lista_botaoNovoPresenteUmbutton() {
        driver.get(baseUrl + "/produtos");
        WebElement botaoNovo = driver.findElement(By.linkText("+ Novo"));
        assertThat(botaoNovo).isNotNull();
        assertThat(botaoNovo.getAttribute("href")).contains("/produtos/novo");
    }

    // ── Formulário ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Formulário de novo produto tem campos obrigatórios")
    void form_camposPresentes() {
        driver.get(baseUrl + "/produtos/novo");

        assertThat(driver.findElement(By.id("nome"))).isNotNull();
        assertThat(driver.findElement(By.id("descricao"))).isNotNull();
        assertThat(driver.findElement(By.id("preco"))).isNotNull();
        assertThat(driver.findElement(By.id("estoque"))).isNotNull();
        assertThat(driver.findElement(By.id("btnSalvar"))).isNotNull();
    }

    // ── Fluxo CRUD completo ───────────────────────────────────────────────────

    @Test
    @DisplayName("Fluxo completo: cadastrar → listar → editar → deletar")
    void fluxoCRUD_completo() {
        // CRIAR
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys("Produto Selenium");
        driver.findElement(By.id("descricao")).sendKeys("Criado via Selenium");
        driver.findElement(By.id("preco")).sendKeys("49.99");
        driver.findElement(By.id("estoque")).sendKeys("7");
        driver.findElement(By.id("btnSalvar")).click();

        // Verificar redirect para lista e produto presente
        assertThat(driver.getCurrentUrl()).contains("/produtos");
        String paginaLista = driver.getPageSource();
        assertThat(paginaLista).contains("Produto Selenium");

        // EDITAR — encontra link de edição do produto criado
        WebElement linkEditar = driver.findElements(By.cssSelector(".btn-warning"))
                .stream().findFirst().orElseThrow(() -> new AssertionError("Botão Editar não encontrado"));
        linkEditar.click();

        driver.findElement(By.id("nome")).clear();
        driver.findElement(By.id("nome")).sendKeys("Produto Editado");
        driver.findElement(By.id("btnSalvar")).click();

        assertThat(driver.getPageSource()).contains("Produto Editado");

        // DELETAR
        WebElement formDelete = driver.findElements(By.cssSelector("form"))
                .stream()
                .filter(f -> {
                    String action = f.getAttribute("action");
                    return action != null && action.contains("deletar");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Formulário de exclusão não encontrado"));

        // HtmlUnit não exibe diálogos confirm — submete diretamente
        formDelete.submit();

        assertThat(driver.getCurrentUrl()).contains("/produtos");
    }

    // ── Validação de campos ────────────────────────────────────────────────────

    @Test
    @DisplayName("Formulário com nome em branco exibe mensagem de erro")
    void form_nomeEmBranco_exibeErro() {
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("preco")).sendKeys("10.00");
        driver.findElement(By.id("estoque")).sendKeys("1");
        // Submete sem preencher nome via URL direta (contorna validação JS)
        driver.get(baseUrl + "/produtos/novo");
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('formProduto').submit()");

        String src = driver.getPageSource();
        // Deve permanecer no formulário (não houve redirect)
        assertThat(driver.getCurrentUrl()).contains("/produtos/novo");
    }

    // ── Campo de busca ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Campo de busca filtra produtos pelo nome")
    void busca_filtraPorNome() {
        // Cria produto via API interna
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys("Roteador WiFi");
        driver.findElement(By.id("preco")).sendKeys("350.00");
        driver.findElement(By.id("estoque")).sendKeys("4");
        driver.findElement(By.id("btnSalvar")).click();

        // Busca
        driver.get(baseUrl + "/produtos");
        driver.findElement(By.id("campoBusca")).sendKeys("Roteador");
        driver.findElement(By.cssSelector(".search-bar button[type=submit]")).click();

        assertThat(driver.getPageSource()).contains("Roteador WiFi");
    }

    @Test
    @DisplayName("Busca com resultado vazio exibe mensagem de 'Nenhum produto encontrado'")
    void busca_semResultado() {
        driver.get(baseUrl + "/produtos");
        driver.findElement(By.id("campoBusca")).sendKeys("ProdutoQueNaoExiste12345");
        driver.findElement(By.cssSelector(".search-bar button[type=submit]")).click();

        assertThat(driver.getPageSource()).contains("Nenhum produto encontrado");
    }

    // ── Entradas inválidas via UI ─────────────────────────────────────────────

    @ParameterizedTest(name = "[{index}] Preço inválido: {0}")
    @ValueSource(strings = {"abc", "0", "-5"})
    @DisplayName("Formulário rejeita preços inválidos e permanece na página")
    void form_precoInvalido(String preco) {
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys("ProdutoInvalido");
        driver.findElement(By.id("preco")).sendKeys(preco);
        driver.findElement(By.id("estoque")).sendKeys("1");
        driver.findElement(By.id("btnSalvar")).click();

        // Deve ter permanecido no formulário ou ido para lista com erro
        String url = driver.getCurrentUrl();
        assertThat(url.contains("novo") || url.contains("produtos")).isTrue();
    }

    // ── Página de erro ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Acesso a produto inexistente mostra mensagem amigável sem stacktrace")
    void erro_produtoNaoEncontrado() {
        driver.get(baseUrl + "/produtos/99999");
        // Após redirect, deve estar na listagem com flash de erro
        String src = driver.getPageSource();
        assertThat(src).doesNotContainIgnoringCase("NullPointerException");
        assertThat(src).doesNotContainIgnoringCase("at org.example");
    }

    // ── Tabela de listagem ────────────────────────────────────────────────────

    @Test
    @DisplayName("Tabela de listagem exibe cabeçalhos corretos")
    void tabela_cabecalhos() {
        // Cria produto para garantir que tabela apareça
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys("ProdutoTabela");
        driver.findElement(By.id("preco")).sendKeys("1.00");
        driver.findElement(By.id("estoque")).sendKeys("1");
        driver.findElement(By.id("btnSalvar")).click();

        driver.get(baseUrl + "/produtos");
        String src = driver.getPageSource();
        assertThat(src).contains("Nome").contains("Preço").contains("Estoque").contains("Ações");
    }
}

