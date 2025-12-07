package br.edu.infnet.exercicio5.domain;

import java.time.LocalDateTime;
import java.util.List;

public final class ProjetoComplexo extends BaseEntity {
    private final String nome;
    private final String descricao;
    private final LocalDateTime dataCriacao;
    private final LocalDateTime dataAtualizacao;

    public ProjetoComplexo(String id, String nome, String descricao, List<String> tags) {
        super(id, tags);
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    private ProjetoComplexo(String id, String nome, String descricao, List<String> tags,
                           LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        super(id, tags);
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public ProjetoComplexo atualizarDescricao(String novaDescricao) {
        return new ProjetoComplexo(getId(), this.nome, novaDescricao, getTags(),
                this.dataCriacao, LocalDateTime.now());
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "ProjetoComplexo{" +
                "id='" + getId() + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                ", tags=" + getTags() +
                '}';
    }
}
