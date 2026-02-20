package org.example.controller;

import jakarta.validation.Valid;
import org.example.exception.NegocioException;
import org.example.exception.ProdutoNotFoundException;
import org.example.model.Produto;
import org.example.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller MVC para operações CRUD de Produto via interface web Thymeleaf.
 * Aplica fail-gracefully: captura exceções e apresenta mensagens amigáveis.
 */
@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    // ── LISTAGEM ──────────────────────────────────────────────────────────────

    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        try {
            List<Produto> produtos = (busca != null && !busca.isBlank())
                    ? service.buscarPorNome(busca)
                    : service.listarTodos();
            model.addAttribute("produtos", produtos);
            model.addAttribute("busca", busca);
        } catch (NegocioException e) {
            model.addAttribute("erroMensagem", e.getMessage());
            model.addAttribute("produtos", List.of());
        }
        return "produtos/lista";
    }

    // ── CADASTRO ──────────────────────────────────────────────────────────────

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("titulo", "Novo Produto");
        return "produtos/formulario";
    }

    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute Produto produto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Novo Produto");
            return "produtos/formulario";
        }
        try {
            service.salvar(produto);
            redirect.addFlashAttribute("sucesso", "Produto cadastrado com sucesso!");
            return "redirect:/produtos";
        } catch (NegocioException e) {
            model.addAttribute("erroMensagem", e.getMessage());
            model.addAttribute("titulo", "Novo Produto");
            return "produtos/formulario";
        }
    }

    // ── EDIÇÃO ────────────────────────────────────────────────────────────────

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("produto", service.buscarPorId(id));
            model.addAttribute("titulo", "Editar Produto");
            return "produtos/formulario";
        } catch (ProdutoNotFoundException | NegocioException e) {
            redirect.addFlashAttribute("erroMensagem", e.getMessage());
            return "redirect:/produtos";
        }
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute Produto produto,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Produto");
            return "produtos/formulario";
        }
        try {
            service.atualizar(id, produto);
            redirect.addFlashAttribute("sucesso", "Produto atualizado com sucesso!");
            return "redirect:/produtos";
        } catch (ProdutoNotFoundException | NegocioException e) {
            model.addAttribute("erroMensagem", e.getMessage());
            model.addAttribute("titulo", "Editar Produto");
            return "produtos/formulario";
        }
    }

    // ── EXCLUSÃO ──────────────────────────────────────────────────────────────

    @PostMapping("/{id}/deletar")
    public String deletar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            service.deletar(id);
            redirect.addFlashAttribute("sucesso", "Produto excluído com sucesso!");
        } catch (ProdutoNotFoundException | NegocioException e) {
            redirect.addFlashAttribute("erroMensagem", e.getMessage());
        }
        return "redirect:/produtos";
    }

    // ── DETALHES ──────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("produto", service.buscarPorId(id));
            return "produtos/detalhe";
        } catch (ProdutoNotFoundException | NegocioException e) {
            redirect.addFlashAttribute("erroMensagem", e.getMessage());
            return "redirect:/produtos";
        }
    }
}

