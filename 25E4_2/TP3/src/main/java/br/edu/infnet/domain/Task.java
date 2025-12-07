package br.edu.infnet.domain;

import br.edu.infnet.domain.enums.TaskStatus;

import java.util.Objects;
import java.util.Optional;

public class Task {
    private final String id;
    private final String titulo;
    private final String descricao;
    private final TaskStatus status;
    private final User responsavel;

    public Task(String id, String titulo, String descricao, TaskStatus status, User responsavel) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.titulo = Objects.requireNonNull(titulo, "Título não pode ser nulo");
        this.descricao = Objects.requireNonNull(descricao, "Descrição não pode ser nula");
        this.status = Objects.requireNonNull(status, "Status não pode ser nulo");
        this.responsavel = responsavel;
    }

    public Task atribuirResponsavel(User novoResponsavel) {
        return new Task(this.id, this.titulo, this.descricao, this.status, novoResponsavel);
    }

    public Task alterarStatus(TaskStatus novoStatus) {
        return new Task(this.id, this.titulo, this.descricao, novoStatus, this.responsavel);
    }

    public void exibirDetalhes() {
        System.out.println("=== Detalhes da Tarefa ===");
        System.out.println("ID: " + id);
        System.out.println("Título: " + titulo);
        System.out.println("Descrição: " + descricao);
        System.out.println("Status: " + status);
        System.out.println("Responsável: " + (responsavel != null ? responsavel.getNome() : "Não atribuído"));
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Optional<User> getResponsavel() {
        return Optional.ofNullable(responsavel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", status=" + status +
                ", responsavel=" + (responsavel != null ? responsavel.getNome() : "Não atribuído") +
                '}';
    }
}
