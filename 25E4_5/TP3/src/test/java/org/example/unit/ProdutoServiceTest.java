package org.example.unit;

import org.example.exception.NegocioException;
import org.example.exception.ProdutoNotFoundException;
import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.example.service.ProdutoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do ProdutoService.
 * Cobre todos os branches: happy path, falhas esperadas e inesperadas.
 */
@DisplayName("ProdutoService — Testes Unitários")
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ── listarTodos ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("listarTodos retorna lista completa")
    void listarTodos_retornaLista() {
        List<Produto> lista = List.of(
                novoProduto("A", "10.00", 1),
                novoProduto("B", "20.00", 2)
        );
        when(repository.findAll()).thenReturn(lista);

        assertThat(service.listarTodos()).hasSize(2);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("listarTodos retorna lista vazia quando não há produtos")
    void listarTodos_listaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertThat(service.listarTodos()).isEmpty();
    }

    // ── buscarPorId ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId retorna produto existente")
    void buscarPorId_sucesso() {
        Produto p = novoProduto("X", "5.00", 10);
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Produto resultado = service.buscarPorId(1L);
        assertThat(resultado.getNome()).isEqualTo("X");
    }

    @Test
    @DisplayName("buscarPorId lança ProdutoNotFoundException para ID inexistente")
    void buscarPorId_naoEncontrado() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(ProdutoNotFoundException.class)
                .hasMessageContaining("99");
    }

    @ParameterizedTest(name = "buscarPorId com ID inválido={0}")
    @ValueSource(longs = {0L, -1L, -100L})
    @DisplayName("buscarPorId lança NegocioException para IDs inválidos")
    void buscarPorId_idInvalido(long id) {
        assertThatThrownBy(() -> service.buscarPorId(id))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("inválido");
    }

    @Test
    @DisplayName("buscarPorId com null lança NegocioException")
    void buscarPorId_null() {
        assertThatThrownBy(() -> service.buscarPorId(null))
                .isInstanceOf(NegocioException.class);
    }

    // ── buscarPorNome ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorNome com nome válido delega ao repositório")
    void buscarPorNome_valido() {
        when(repository.findByNomeContainingIgnoreCase("teclado"))
                .thenReturn(List.of(novoProduto("Teclado Gamer", "150.00", 5)));
        assertThat(service.buscarPorNome("teclado")).hasSize(1);
    }

    @Test
    @DisplayName("buscarPorNome com nome em branco retorna todos")
    void buscarPorNome_vazio_retornaTodos() {
        when(repository.findAll()).thenReturn(List.of(novoProduto("A", "1.00", 1)));
        assertThat(service.buscarPorNome("")).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorNome com null retorna todos")
    void buscarPorNome_null_retornaTodos() {
        when(repository.findAll()).thenReturn(List.of());
        service.buscarPorNome(null);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("buscarPorNome com string > 200 chars lança NegocioException (fail-early)")
    void buscarPorNome_muitoLongo() {
        String busca = "a".repeat(201);
        assertThatThrownBy(() -> service.buscarPorNome(busca))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("longo");
    }

    // ── salvar ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("salvar produto válido persiste e retorna entidade")
    void salvar_valido() {
        Produto p = novoProduto("Monitor", "999.00", 3);
        when(repository.save(any(Produto.class))).thenReturn(p);

        Produto salvo = service.salvar(p);
        assertThat(salvo.getNome()).isEqualTo("Monitor");
        verify(repository).save(any());
    }

    @Test
    @DisplayName("salvar produto null lança NegocioException")
    void salvar_null() {
        assertThatThrownBy(() -> service.salvar(null))
                .isInstanceOf(NegocioException.class);
    }

    @ParameterizedTest(name = "salvar com nome inválido=''{0}''")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("salvar lança NegocioException para nomes em branco")
    void salvar_nomeEmBranco(String nome) {
        Produto p = novoProduto(nome, "10.00", 0);
        p.setNome(nome);
        assertThatThrownBy(() -> service.salvar(p))
                .isInstanceOf(NegocioException.class);
    }

    @ParameterizedTest(name = "salvar com preço inválido={0}")
    @ValueSource(strings = {"0", "-1", "-0.01"})
    @DisplayName("salvar lança NegocioException para preços não positivos")
    void salvar_precoInvalido(String precoStr) {
        Produto p = novoProduto("Produto", precoStr, 1);
        assertThatThrownBy(() -> service.salvar(p))
                .isInstanceOf(NegocioException.class);
    }

    @Test
    @DisplayName("salvar remove caracteres XSS do nome e descrição (sanitização)")
    void salvar_sanitizaXSS() {
        Produto p = novoProduto("<script>alert(1)</script>", "10.00", 1);
        p.setDescricao("<img src=x onerror='alert(1)'>");
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Produto salvo = service.salvar(p);
        assertThat(salvo.getNome()).doesNotContain("<", ">", "\"", "'");
        assertThat(salvo.getDescricao()).doesNotContain("<", ">", "\"", "'");
    }

    @Test
    @DisplayName("salvar com estoque negativo lança NegocioException")
    void salvar_estoqueNegativo() {
        Produto p = novoProduto("Produto", "10.00", -1);
        assertThatThrownBy(() -> service.salvar(p))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("negativo");
    }

    // ── atualizar ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("atualizar produto existente com dados válidos persiste alterações")
    void atualizar_sucesso() {
        Produto existente = novoProduto("Antigo", "10.00", 5);
        existente.setId(1L);
        Produto novos = novoProduto("Novo", "20.00", 8);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Produto atualizado = service.atualizar(1L, novos);
        assertThat(atualizado.getNome()).isEqualTo("Novo");
        assertThat(atualizado.getPreco()).isEqualByComparingTo("20.00");
    }

    @Test
    @DisplayName("atualizar produto inexistente lança ProdutoNotFoundException")
    void atualizar_naoEncontrado() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.atualizar(99L, novoProduto("X", "1.00", 0)))
                .isInstanceOf(ProdutoNotFoundException.class);
    }

    // ── deletar ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deletar produto existente chama repository.delete")
    void deletar_sucesso() {
        Produto p = novoProduto("Para deletar", "5.00", 2);
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));
        doNothing().when(repository).delete(p);

        service.deletar(1L);
        verify(repository).delete(p);
    }

    @Test
    @DisplayName("deletar produto inexistente lança ProdutoNotFoundException")
    void deletar_naoEncontrado() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deletar(5L))
                .isInstanceOf(ProdutoNotFoundException.class);
    }

    @Test
    @DisplayName("deletar com ID inválido lança NegocioException antes de consultar o banco")
    void deletar_idInvalido() {
        assertThatThrownBy(() -> service.deletar(-3L))
                .isInstanceOf(NegocioException.class);
        verify(repository, never()).findById(anyLong());
    }

    // ── Falhas inesperadas (simulação) ────────────────────────────────────────

    @Test
    @DisplayName("listarTodos propaga RuntimeException do repositório")
    void listarTodos_falhaRepositorio() {
        when(repository.findAll()).thenThrow(new RuntimeException("Timeout de conexão"));
        assertThatThrownBy(() -> service.listarTodos())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Timeout");
    }

    @Test
    @DisplayName("salvar propaga exceção ao falhar a persistência")
    void salvar_falhaRepositorio() {
        when(repository.save(any())).thenThrow(new RuntimeException("DB unavailable"));
        Produto p = novoProduto("X", "1.00", 1);
        assertThatThrownBy(() -> service.salvar(p))
                .isInstanceOf(RuntimeException.class);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static Produto novoProduto(String nome, String preco, int estoque) {
        return new Produto(nome, "Descrição", new BigDecimal(preco), estoque);
    }
}

