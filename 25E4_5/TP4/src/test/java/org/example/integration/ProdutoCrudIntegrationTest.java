package org.example.integration;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração: exercitam o stack completo (Controller → Service → Repository → H2).
 * Cobrem happy paths, erros esperados e entradas inválidas.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CRUD Integração — MockMvc")
class ProdutoCrudIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProdutoRepository repository;

    // ── Listagem ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos retorna status 200 e página de listagem")
    void listar_retorna200() throws Exception {
        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/lista"));
    }

    @Test
    @DisplayName("GET /produtos exibe produto cadastrado na tabela")
    void listar_exibeProdutoCadastrado() throws Exception {
        repository.save(new Produto("Notebook", "Alta performance", new BigDecimal("3500.00"), 5));

        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Notebook")));
    }

    // ── Cadastro ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos/novo retorna formulário vazio")
    void novoForm_retornaFormulario() throws Exception {
        mvc.perform(get("/produtos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"));
    }

    @Test
    @DisplayName("POST /produtos/novo com dados válidos redireciona para listagem")
    void salvar_valido_redireciona() throws Exception {
        mvc.perform(post("/produtos/novo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "Mouse Gamer")
                        .param("descricao", "Mouse de alta precisao")
                        .param("preco", "199.99")
                        .param("estoque", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        assertThat(repository.findByNomeContainingIgnoreCase("Mouse Gamer")).hasSize(1);
    }

    @ParameterizedTest(name = "POST /produtos/novo campo inválido: {0}={1}")
    @CsvSource({
            "nome, ''",
            "preco, -1",
            "preco, 0",
            "estoque, -5"
    })
    @DisplayName("POST /produtos/novo com campos inválidos fica no formulário")
    void salvar_camposInvalidos(String campo, String valor) throws Exception {
        var request = post("/produtos/novo")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nome", campo.equals("nome") ? valor : "Produto Válido")
                .param("preco", campo.equals("preco") ? valor : "10.00")
                .param("estoque", campo.equals("estoque") ? valor : "5");

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"));
    }

    // ── Edição ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos/{id}/editar com ID existente retorna formulário preenchido")
    void editarForm_valido() throws Exception {
        Produto salvo = repository.save(new Produto("Cadeira", "Ergonômica", new BigDecimal("800.00"), 3));

        mvc.perform(get("/produtos/" + salvo.getId() + "/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cadeira")));
    }

    @Test
    @DisplayName("GET /produtos/{id}/editar com ID inexistente redireciona com erro")
    void editarForm_naoEncontrado() throws Exception {
        mvc.perform(get("/produtos/9999/editar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));
    }

    @Test
    @DisplayName("POST /produtos/{id}/editar com dados válidos atualiza e redireciona")
    void atualizar_valido() throws Exception {
        Produto salvo = repository.save(new Produto("Nome Antigo", "Desc", new BigDecimal("10.00"), 1));

        mvc.perform(post("/produtos/" + salvo.getId() + "/editar")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "Nome Novo")
                        .param("descricao", "Desc Atualizada")
                        .param("preco", "25.00")
                        .param("estoque", "8"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        Produto atualizado = repository.findById(salvo.getId()).orElseThrow();
        assertThat(atualizado.getNome()).isEqualTo("Nome Novo");
    }

    // ── Exclusão ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /produtos/{id}/deletar remove produto e redireciona")
    void deletar_valido() throws Exception {
        Produto salvo = repository.save(new Produto("Para Deletar", "Desc", new BigDecimal("5.00"), 0));
        Long id = salvo.getId();

        mvc.perform(post("/produtos/" + id + "/deletar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("POST /produtos/{id}/deletar com ID inexistente redireciona com erro")
    void deletar_naoEncontrado() throws Exception {
        mvc.perform(post("/produtos/99999/deletar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));
    }

    // ── Detalhe ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos/{id} retorna detalhe do produto")
    void detalhe_valido() throws Exception {
        Produto salvo = repository.save(new Produto("Detalhe Teste", "Desc", new BigDecimal("15.00"), 2));

        mvc.perform(get("/produtos/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/detalhe"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Detalhe Teste")));
    }

    @Test
    @DisplayName("GET /produtos/{id} com ID inexistente redireciona")
    void detalhe_naoEncontrado() throws Exception {
        mvc.perform(get("/produtos/88888"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));
    }

    // ── Busca ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos?busca=X retorna apenas produtos correspondentes")
    void busca_filtra() throws Exception {
        repository.save(new Produto("Impressora", "Laser", new BigDecimal("500.00"), 2));
        repository.save(new Produto("Scanner", "Mesa", new BigDecimal("300.00"), 1));

        MvcResult result = mvc.perform(get("/produtos").param("busca", "Impressora"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        assertThat(html).contains("Impressora");
        assertThat(html).doesNotContain("Scanner");
    }
}

