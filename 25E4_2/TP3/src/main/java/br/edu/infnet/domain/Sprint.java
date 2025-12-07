package br.edu.infnet.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Sprint {
    private final String id;
    private final String nome;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;
    private final List<Task> tarefas;

    public Sprint(String id, String nome, LocalDate dataInicio, LocalDate dataFim) {
        this(id, nome, dataInicio, dataFim, new ArrayList<>());
    }

    private Sprint(String id, String nome, LocalDate dataInicio, LocalDate dataFim, List<Task> tarefas) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.dataInicio = Objects.requireNonNull(dataInicio, "Data de início não pode ser nula");
        this.dataFim = Objects.requireNonNull(dataFim, "Data de fim não pode ser nula");
        
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data de fim não pode ser anterior à data de início");
        }
        
        this.tarefas = new ArrayList<>(tarefas);
    }

    public Sprint adicionarTarefa(Task tarefa) {
        List<Task> novasTarefas = new ArrayList<>(this.tarefas);
        novasTarefas.add(Objects.requireNonNull(tarefa, "Tarefa não pode ser nula"));
        return new Sprint(this.id, this.nome, this.dataInicio, this.dataFim, novasTarefas);
    }

    public Sprint removerTarefa(String tarefaId) {
        List<Task> novasTarefas = new ArrayList<>(this.tarefas);
        novasTarefas.removeIf(t -> t.getId().equals(tarefaId));
        return new Sprint(this.id, this.nome, this.dataInicio, this.dataFim, novasTarefas);
    }

    public void listarTarefas() {
        System.out.println("=== Tarefas da Sprint: " + nome + " ===");
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            tarefas.forEach(System.out::println);
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public List<Task> getTarefas() {
        return Collections.unmodifiableList(tarefas);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprint sprint = (Sprint) o;
        return Objects.equals(id, sprint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", totalTarefas=" + tarefas.size() +
                '}';
    }
}
