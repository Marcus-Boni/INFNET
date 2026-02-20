package org.example.unit;

import org.example.exception.NegocioException;
import org.example.exception.ProdutoNotFoundException;
import org.example.model.Produto;
import org.example.service.ProdutoService;
import org.example.controller.ProdutoController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do ProdutoController cobrindo todos os branches.
 */
@DisplayName("ProdutoController — Testes Unitários")
class ProdutoControllerTest {

    @Mock
    private ProdutoService service;

    @InjectMocks
    private ProdutoController controller;

    private Model model;
    private RedirectAttributesModelMap redirect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = new ConcurrentModel();
        redirect = new RedirectAttributesModelMap();
    }

    // ── listar ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos sem busca retorna view lista com todos os produtos")
    void listar_semBusca() {
        when(service.listarTodos()).thenReturn(List.of(produto()));
        String view = controller.listar(null, model);

        assertThat(view).isEqualTo("produtos/lista");
        assertThat(model.asMap()).containsKey("produtos");
    }

    @Test
    @DisplayName("GET /produtos com busca chama buscarPorNome")
    void listar_comBusca() {
        when(service.buscarPorNome("teclado")).thenReturn(List.of());
        String view = controller.listar("teclado", model);

        assertThat(view).isEqualTo("produtos/lista");
        verify(service).buscarPorNome("teclado");
    }

    @Test
    @DisplayName("GET /produtos busca com NegocioException adiciona mensagem de erro")
    void listar_negocioException() {
        when(service.buscarPorNome(anyString()))
                .thenThrow(new NegocioException("Termo muito longo."));

        String view = controller.listar("x".repeat(300), model);
        assertThat(view).isEqualTo("produtos/lista");
        assertThat(model.asMap()).containsKey("erroMensagem");
    }

    // ── novoForm ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos/novo retorna formulário com produto vazio")
    void novoForm_retornaFormulario() {
        String view = controller.novoForm(model);
        assertThat(view).isEqualTo("produtos/formulario");
        assertThat(model.asMap().get("produto")).isInstanceOf(Produto.class);
    }

    // ── salvar ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /produtos/novo com erros de validação fica no formulário")
    void salvar_comErrosValidacao() {
        Produto p = new Produto();
        BindingResult erros = bindingResultComErro(p);

        String view = controller.salvar(p, erros, model, redirect);
        assertThat(view).isEqualTo("produtos/formulario");
        verify(service, never()).salvar(any());
    }

    @Test
    @DisplayName("POST /produtos/novo válido redireciona com mensagem de sucesso")
    void salvar_valido() {
        Produto p = produto();
        BindingResult semErros = new BeanPropertyBindingResult(p, "produto");
        when(service.salvar(any())).thenReturn(p);

        String view = controller.salvar(p, semErros, model, redirect);
        assertThat(view).isEqualTo("redirect:/produtos");
        assertThat(redirect.getFlashAttributes()).containsKey("sucesso");
    }

    @Test
    @DisplayName("POST /produtos/novo com NegocioException fica no formulário com erro")
    void salvar_negocioException() {
        Produto p = produto();
        BindingResult semErros = new BeanPropertyBindingResult(p, "produto");
        when(service.salvar(any())).thenThrow(new NegocioException("Preço inválido."));

        String view = controller.salvar(p, semErros, model, redirect);
        assertThat(view).isEqualTo("produtos/formulario");
        assertThat(model.asMap()).containsKey("erroMensagem");
    }

    // ── editarForm ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos/{id}/editar com ID válido retorna formulário")
    void editarForm_valido() {
        Produto p = produto();
        when(service.buscarPorId(1L)).thenReturn(p);

        String view = controller.editarForm(1L, model, redirect);
        assertThat(view).isEqualTo("produtos/formulario");
    }

    @Test
    @DisplayName("GET /produtos/{id}/editar com ID inexistente redireciona com erro")
    void editarForm_naoEncontrado() {
        when(service.buscarPorId(99L)).thenThrow(new ProdutoNotFoundException(99L));

        String view = controller.editarForm(99L, model, redirect);
        assertThat(view).isEqualTo("redirect:/produtos");
        assertThat(redirect.getFlashAttributes()).containsKey("erroMensagem");
    }

    // ── atualizar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /produtos/{id}/editar válido redireciona com sucesso")
    void atualizar_valido() {
        Produto p = produto();
        BindingResult semErros = new BeanPropertyBindingResult(p, "produto");
        when(service.atualizar(eq(1L), any())).thenReturn(p);

        String view = controller.atualizar(1L, p, semErros, model, redirect);
        assertThat(view).isEqualTo("redirect:/produtos");
        assertThat(redirect.getFlashAttributes()).containsKey("sucesso");
    }

    @Test
    @DisplayName("POST /produtos/{id}/editar com erros de validação fica no formulário")
    void atualizar_comErros() {
        Produto p = new Produto();
        BindingResult erros = bindingResultComErro(p);

        String view = controller.atualizar(1L, p, erros, model, redirect);
        assertThat(view).isEqualTo("produtos/formulario");
    }

    // ── deletar ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /produtos/{id}/deletar com sucesso redireciona com mensagem")
    void deletar_sucesso() {
        doNothing().when(service).deletar(1L);
        String view = controller.deletar(1L, redirect);

        assertThat(view).isEqualTo("redirect:/produtos");
        assertThat(redirect.getFlashAttributes()).containsKey("sucesso");
    }

    @Test
    @DisplayName("POST /produtos/{id}/deletar com ID inexistente redireciona com erro")
    void deletar_naoEncontrado() {
        doThrow(new ProdutoNotFoundException(5L)).when(service).deletar(5L);
        String view = controller.deletar(5L, redirect);

        assertThat(view).isEqualTo("redirect:/produtos");
        assertThat(redirect.getFlashAttributes()).containsKey("erroMensagem");
    }

    // ── detalhe ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /produtos/{id} com ID válido retorna view de detalhe")
    void detalhe_valido() {
        when(service.buscarPorId(1L)).thenReturn(produto());
        String view = controller.detalhe(1L, model, redirect);
        assertThat(view).isEqualTo("produtos/detalhe");
    }

    @ParameterizedTest(name = "detalhe com ID inválido={0}")
    @ValueSource(longs = {0L, -1L})
    @DisplayName("GET /produtos/{id} com ID inválido redireciona com erro")
    void detalhe_idInvalido(long id) {
        when(service.buscarPorId(id)).thenThrow(new NegocioException("ID inválido: " + id));
        String view = controller.detalhe(id, model, redirect);
        assertThat(view).isEqualTo("redirect:/produtos");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Produto produto() {
        Produto p = new Produto("Produto Teste", "Desc", new BigDecimal("9.99"), 10);
        p.setId(1L);
        return p;
    }

    private static BindingResult bindingResultComErro(Produto p) {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(p, "produto");
        br.rejectValue("nome", "NotBlank", "Nome obrigatório");
        return br;
    }
}

