package org.example.controller.api;

import jakarta.validation.Valid;
import org.example.model.Produto;
import org.example.service.catalogo.ProdutoCatalogo;
import org.example.service.support.ProdutoCollection;
import org.example.service.support.TermoBusca;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoApiController {

    private final ProdutoCatalogo produtoCatalogo;
    private final ProdutoDtoMapper produtoDtoMapper;

    public ProdutoApiController(ProdutoCatalogo produtoCatalogo, ProdutoDtoMapper produtoDtoMapper) {
        this.produtoCatalogo = produtoCatalogo;
        this.produtoDtoMapper = produtoDtoMapper;
    }

    @GetMapping
    public List<ProdutoResponse> listar(@RequestParam(required = false) String busca) {
        ProdutoCollection produtos = (busca == null)
                ? produtoCatalogo.listarTodos()
                : produtoCatalogo.buscarPorNome(TermoBusca.of(busca));
        return produtos.asList().stream().map(produtoDtoMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        return produtoDtoMapper.toResponse(produtoCatalogo.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@Valid @RequestBody ProdutoRequest request) {
        Produto salvo = produtoCatalogo.salvar(produtoDtoMapper.toEntity(request));
        return produtoDtoMapper.toResponse(salvo);
    }
}
