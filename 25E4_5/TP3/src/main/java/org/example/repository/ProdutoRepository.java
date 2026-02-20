package org.example.repository;

import org.example.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);

    List<Produto> findByEstoqueGreaterThan(int quantidade);

    @Query("SELECT p FROM Produto p WHERE p.estoque = 0")
    List<Produto> findSemEstoque();

    boolean existsByNomeIgnoreCase(String nome);

    @Query("SELECT COUNT(p) FROM Produto p WHERE p.preco > :valor")
    long countByPrecoAcimaDe(@Param("valor") BigDecimal valor);
}

