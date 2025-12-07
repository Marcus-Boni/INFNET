package br.edu.infnet.exercicio1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Pedido {
    private final String id;
    private final LocalDateTime dataCriacao;
    private final StatusPedido status;
    private final String clienteId;
    private final List<ItemPedido> itens;
    private final LocalDateTime dataAtualizacao;

    public Pedido(String clienteId, List<ItemPedido> itens) {
        this(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            StatusPedido.CRIADO,
            clienteId,
            itens,
            LocalDateTime.now()
        );
    }

    private Pedido(String id, LocalDateTime dataCriacao, StatusPedido status, 
                   String clienteId, List<ItemPedido> itens, LocalDateTime dataAtualizacao) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.dataCriacao = Objects.requireNonNull(dataCriacao, "Data de criação não pode ser nula");
        this.status = Objects.requireNonNull(status, "Status não pode ser nulo");
        this.clienteId = Objects.requireNonNull(clienteId, "Cliente ID não pode ser nulo");
        this.itens = new ArrayList<>(Objects.requireNonNull(itens, "Itens não podem ser nulos"));
        this.dataAtualizacao = Objects.requireNonNull(dataAtualizacao, "Data de atualização não pode ser nula");
        
        if (this.itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter ao menos um item");
        }
    }

    public Pedido atualizarStatus(StatusPedido novoStatus) {
        Objects.requireNonNull(novoStatus, "Novo status não pode ser nulo");
        return new Pedido(
            this.id,
            this.dataCriacao,
            novoStatus,
            this.clienteId,
            this.itens,
            LocalDateTime.now()
        );
    }

    public Pedido adicionarItem(ItemPedido item) {
        Objects.requireNonNull(item, "Item não pode ser nulo");
        List<ItemPedido> novosItens = new ArrayList<>(this.itens);
        novosItens.add(item);
        return new Pedido(
            this.id,
            this.dataCriacao,
            this.status,
            this.clienteId,
            novosItens,
            LocalDateTime.now()
        );
    }

    public Pedido removerItem(String itemId) {
        List<ItemPedido> novosItens = new ArrayList<>(this.itens);
        novosItens.removeIf(item -> item.getId().equals(itemId));
        
        if (novosItens.isEmpty()) {
            throw new IllegalStateException("Não é possível remover todos os itens do pedido");
        }
        
        return new Pedido(
            this.id,
            this.dataCriacao,
            this.status,
            this.clienteId,
            novosItens,
            LocalDateTime.now()
        );
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public String getClienteId() {
        return clienteId;
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", status=" + status +
                ", clienteId='" + clienteId + '\'' +
                ", totalItens=" + itens.size() +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
