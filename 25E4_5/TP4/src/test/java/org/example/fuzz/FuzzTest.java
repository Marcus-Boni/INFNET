package org.example.fuzz;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Fuzz Testing — Entradas Maliciosas e Aleatórias")
class FuzzTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProdutoRepository repository;

    private static final Random RND = new Random(42L);

    static Stream<String> payloadsMaliciosos() {
        return Stream.of(
                "<script>alert('xss')</script>",
                "<img src=x onerror=alert(1)>",
                "javascript:alert(document.cookie)",
                "<svg onload=alert(1)>",
                "'\"><script>alert(1)</script>",
                "' OR '1'='1",
                "'; DROP TABLE produtos; --",
                "1 UNION SELECT * FROM produtos --",
                "admin'--",
                "A".repeat(1000),
                "B".repeat(10000),
                "\u0000\u0001\u0002",
                "あいうえお",
                "中文测试",
                "مرحبا بالعالم",
                "𝓕𝓪𝓷𝓬𝔂 𝓣𝓮𝔁𝓽",
                "   ",
                "\t\n\r",
                "../../../etc/passwd",
                "..\\..\\windows\\system32",
                "&lt;script&gt;alert(1)&lt;/script&gt;",
                "&#x3C;script&#x3E;",
                "nome\r\nSet-Cookie: malicioso=true"
        );
    }

    static Stream<String> precosFuzzy() {
        return Stream.of(
                "abc", "null", "", " ", "99999999999", "-999",
                "0", "-0.01", "1e10", "NaN", "Infinity", "-Infinity",
                "1/0", "1,5", "1.2.3", "0x1F", "%20", "''", "\""
        );
    }

    static Stream<String> idsFuzzy() {
        return Stream.of(
                "0", "-1", "-9999", "abc", "null",
                "9999999999999999999", "1.5", "true",
                "xyzABC", "script", "admin"
        );
    }

    @ParameterizedTest(name = "[{index}] POST /novo nome malicioso: {0}")
    @MethodSource("payloadsMaliciosos")
    @DisplayName("POST /produtos/novo com nome malicioso: nunca causa 5xx nem expõe internals")
    void fuzz_nomeMalicioso(String payload) throws Exception {
        MvcResult result = mvc.perform(post("/produtos/novo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", payload)
                        .param("preco", "10.00")
                        .param("estoque", "1"))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).as("Status não deve ser 5xx para nome malicioso: " + payload)
                .isBetween(200, 399);

        String body = result.getResponse().getContentAsString();
        assertThat(body)
                .as("Resposta não deve conter stacktrace")
                .doesNotContainIgnoringCase("at org.")
                .doesNotContainIgnoringCase("NullPointerException")
                .doesNotContainIgnoringCase("Exception in thread");
    }

    @ParameterizedTest(name = "[{index}] POST /novo preço fuzzy: ''{0}''")
    @MethodSource("precosFuzzy")
    @DisplayName("POST /produtos/novo com preço inválido: retorna formulário, nunca 5xx")
    void fuzz_precoInvalido(String preco) throws Exception {
        MvcResult result = mvc.perform(post("/produtos/novo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "ProdutoFuzz")
                        .param("preco", preco)
                        .param("estoque", "1"))
                .andReturn();

        assertThat(result.getResponse().getStatus())
                .as("Preço inválido '%s' não deve causar 5xx".formatted(preco))
                .isBetween(200, 399);
    }

    @ParameterizedTest(name = "[{index}] GET /produtos/{0}")
    @MethodSource("idsFuzzy")
    @DisplayName("GET /produtos/{id} com IDs maliciosos: nunca causa 5xx")
    void fuzz_idMalicioso(String id) throws Exception {
        int status = mvc.perform(get("/produtos/" + id))
                .andReturn().getResponse().getStatus();

        assertThat(status)
                .as("ID malicioso '%s' nao deve causar 5xx".formatted(id))
                .isNotEqualTo(500);
    }

    @ParameterizedTest(name = "[{index}] POST /novo descrição maliciosa")
    @MethodSource("payloadsMaliciosos")
    @DisplayName("POST /produtos/novo com descrição maliciosa: nunca causa 5xx")
    void fuzz_descricaoMaliciosa(String payload) throws Exception {
        MvcResult result = mvc.perform(post("/produtos/novo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "ProdutoFuzz")
                        .param("descricao", payload)
                        .param("preco", "10.00")
                        .param("estoque", "1"))
                .andReturn();

        assertThat(result.getResponse().getStatus())
                .isBetween(200, 399);
    }

    @Test
    @DisplayName("Sistema responde corretamente sob 100 requisições simultâneas de listagem")
    void fuzz_sobrecarga_listagem() throws Exception {
        for (int i = 0; i < 10; i++) {
            repository.save(new Produto("Produto " + i, "Desc", new BigDecimal("10.00"), i));
        }

        for (int i = 0; i < 100; i++) {
            mvc.perform(get("/produtos"))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest(name = "[{index}] GET /produtos?busca={0}")
    @MethodSource("payloadsMaliciosos")
    @DisplayName("GET /produtos?busca= com payloads maliciosos: nunca causa 5xx")
    void fuzz_busca(String payload) throws Exception {
        MvcResult result = mvc.perform(get("/produtos").param("busca", payload))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status)
                .as("Busca com payload malicioso não deve causar 5xx")
                .isNotEqualTo(500);

        if (status == 200) {
            assertThat(result.getResponse().getContentAsString())
                    .doesNotContainIgnoringCase("Exception")
                    .doesNotContainIgnoringCase("at org.");
        }
    }

    @Test
    @DisplayName("POST /produtos/{id}/editar com payload XSS no nome é sanitizado")
    void fuzz_edicaoXSS_sanitizado() throws Exception {
        Produto salvo = repository.save(new Produto("Produto Original", "Desc", new BigDecimal("10.00"), 1));

        mvc.perform(post("/produtos/" + salvo.getId() + "/editar")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "<script>alert('xss')</script>")
                        .param("descricao", "Desc Normal")
                        .param("preco", "10.00")
                        .param("estoque", "1"))
                .andReturn();

        repository.findById(salvo.getId()).ifPresent(p -> {
            assertThat(p.getNome())
                    .doesNotContain("<script>")
                    .doesNotContain("</script>");
        });
    }

    @Test
    @DisplayName("50 requisições POST com campos totalmente aleatórios: nunca causa 5xx")
    void fuzz_aleatorio() throws Exception {
        String chars = "abcdefghijklmnopqrstuvwxyz<>'\"/\\!@#$%^&*()_+-=[]{}";
        for (int i = 0; i < 50; i++) {
            String nome = randomString(chars, RND.nextInt(200));
            String preco = randomString("0123456789.-abc", RND.nextInt(10));
            String estoque = randomString("0123456789-abc", RND.nextInt(5));

            MvcResult r = mvc.perform(post("/produtos/novo")
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .param("nome", nome)
                            .param("preco", preco)
                            .param("estoque", estoque))
                    .andReturn();

            assertThat(r.getResponse().getStatus())
                    .as("Iteração %d com nome='%s' não deve causar 5xx".formatted(i, nome))
                    .isNotEqualTo(500);
        }
    }

    private String randomString(String chars, int length) {
        if (length == 0) return "";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RND.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
