package br.edu.infnet.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Project {
    private final String id;
    private final String nome;
    private final String descricao;
    private final List<Sprint> sprints;

    public Project(String id, String nome, String descricao) {
        this(id, nome, descricao, new ArrayList<>());
    }

    private Project(String id, String nome, String descricao, List<Sprint> sprints) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.descricao = Objects.requireNonNull(descricao, "Descrição não pode ser nula");
        this.sprints = new ArrayList<>(sprints);
    }

    public Project adicionarSprint(Sprint sprint) {
        List<Sprint> novasSprints = new ArrayList<>(this.sprints);
        novasSprints.add(Objects.requireNonNull(sprint, "Sprint não pode ser nula"));
        return new Project(this.id, this.nome, this.descricao, novasSprints);
    }

    public Project removerSprint(String sprintId) {
        List<Sprint> novasSprints = new ArrayList<>(this.sprints);
        novasSprints.removeIf(s -> s.getId().equals(sprintId));
        return new Project(this.id, this.nome, this.descricao, novasSprints);
    }

    public void listarSprints() {
        System.out.println("=== Sprints do Projeto: " + nome + " ===");
        if (sprints.isEmpty()) {
            System.out.println("Nenhuma sprint cadastrada.");
        } else {
            sprints.forEach(System.out::println);
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public List<Sprint> getSprints() {
        return Collections.unmodifiableList(sprints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", totalSprints=" + sprints.size() +
                '}';
    }
}
