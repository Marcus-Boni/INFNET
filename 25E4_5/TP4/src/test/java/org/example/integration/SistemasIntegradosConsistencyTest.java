package org.example.integration;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Integração TP3 + TP4 — consistência entre MVC e API")
class SistemasIntegradosConsistencyTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProdutoRepository repository;

    @Test
    @DisplayName("Criado via API aparece na listagem MVC")
    void criadoViaApiApareceNaListagemMvc() throws Exception {
        mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Produto API",
                                  "descricao": "Criado no endpoint integrado",
                                  "preco": 100.00,
                                  "estoque": 4
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Produto API")));

        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Produto API")));
    }

    @Test
    @DisplayName("Criado via MVC aparece na API")
    void criadoViaMvcApareceNaApi() throws Exception {
        mvc.perform(post("/produtos/novo")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "Produto MVC")
                        .param("descricao", "Criado no fluxo web")
                        .param("preco", "55.90")
                        .param("estoque", "8"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        mvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Produto MVC")));
    }

    @Test
    @DisplayName("Atualização em um fluxo mantém integridade no repositório compartilhado")
    void atualizacaoMantemIntegridade() throws Exception {
        Produto salvo = repository.save(new Produto("Base", "Desc", new BigDecimal("10.00"), 3));

        mvc.perform(post("/produtos/" + salvo.getId() + "/editar")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nome", "Base Atualizada")
                        .param("descricao", "Desc Atualizada")
                        .param("preco", "20.00")
                        .param("estoque", "9"))
                .andExpect(status().is3xxRedirection());

        Produto atualizado = repository.findById(salvo.getId()).orElseThrow();
        assertThat(atualizado.getNome()).isEqualTo("Base Atualizada");
        assertThat(atualizado.getPreco()).isEqualByComparingTo("20.00");
    }
}
