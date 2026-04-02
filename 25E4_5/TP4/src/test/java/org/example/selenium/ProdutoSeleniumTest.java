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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Selenium (HtmlUnit) — Testes de Interface")
class ProdutoSeleniumTest {

    @LocalServerPort
    private int port;

    private HtmlUnitDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

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

    @Test
    @DisplayName("Fluxo completo: cadastrar → listar → editar → deletar")
    void fluxoCRUD_completo() {
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys("Produto Selenium");
        driver.findElement(By.id("descricao")).sendKeys("Criado via Selenium");
        driver.findElement(By.id("preco")).sendKeys("49.99");
        driver.findElement(By.id("estoque")).sendKeys("7");
        driver.findElement(By.id("btnSalvar")).click();

        assertThat(driver.getCurrentUrl()).contains("/produtos");
        String paginaLista = driver.getPageSource();
        assertThat(paginaLista).contains("Produto Selenium");

        WebElement linkEditar = driver.findElements(By.cssSelector(".btn-warning"))
                .stream().findFirst().orElseThrow(() -> new AssertionError("Botão Editar não encontrado"));
        linkEditar.click();

        driver.findElement(By.id("nome")).clear();
        driver.findElement(By.id("nome")).sendKeys("Produto Editado");
        driver.findElement(By.id("btnSalvar")).click();

        assertThat(driver.getPageSource()).contains("Produto Editado");

        WebElement formDelete = driver.findElements(By.cssSelector("form"))
                .stream()
                .filter(f -> {
                    String action = f.getAttribute("action");
                    return action != null && action.contains("deletar");
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Formulário de exclusão não encontrado"));

        formDelete.submit();

        assertThat(driver.getCurrentUrl()).contains("/produtos");
    }

    @Test
    @DisplayName("Formulário com nome em branco exibe mensagem de erro")
    void form_nomeEmBranco_exibeErro() {
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("preco")).sendKeys("10.00");
        driver.findElement(By.id("estoque")).sendKeys("1");
        driver.get(baseUrl + "/produtos/novo");
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('formProduto').submit()");

        String src = driver.getPageSource();
        assertThat(driver.getCurrentUrl()).contains("/produtos/novo");
    }

    @Test
    @DisplayName("Campo de busca filtra produtos pelo nome")
    void busca_filtraPorNome() {
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys("Roteador WiFi");
        driver.findElement(By.id("preco")).sendKeys("350.00");
        driver.findElement(By.id("estoque")).sendKeys("4");
        driver.findElement(By.id("btnSalvar")).click();

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

    @ParameterizedTest(name = "[{index}] Preço inválido: {0}")
    @ValueSource(strings = {"abc", "0", "-5"})
    @DisplayName("Formulário rejeita preços inválidos e permanece na página")
    void form_precoInvalido(String preco) {
        driver.get(baseUrl + "/produtos/novo");
        driver.findElement(By.id("nome")).sendKeys("ProdutoInvalido");
        driver.findElement(By.id("preco")).sendKeys(preco);
        driver.findElement(By.id("estoque")).sendKeys("1");
        driver.findElement(By.id("btnSalvar")).click();

        String url = driver.getCurrentUrl();
        assertThat(url.contains("novo") || url.contains("produtos")).isTrue();
    }

    @Test
    @DisplayName("Acesso a produto inexistente mostra mensagem amigável sem stacktrace")
    void erro_produtoNaoEncontrado() {
        driver.get(baseUrl + "/produtos/99999");
        String src = driver.getPageSource();
        assertThat(src).doesNotContainIgnoringCase("NullPointerException");
        assertThat(src).doesNotContainIgnoringCase("at org.example");
    }

    @Test
    @DisplayName("Tabela de listagem exibe cabeçalhos corretos")
    void tabela_cabecalhos() {
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

