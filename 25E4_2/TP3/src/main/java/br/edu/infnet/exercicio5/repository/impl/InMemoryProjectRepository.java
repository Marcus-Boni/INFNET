package br.edu.infnet.exercicio5.repository.impl;

import br.edu.infnet.domain.Project;
import br.edu.infnet.exercicio5.repository.ProjectRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryProjectRepository implements ProjectRepository {
    private final Map<String, Project> storage;

    public InMemoryProjectRepository() {
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public Project save(Project entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Project> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Project> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    @Override
    public List<Project> findByNome(String nome) {
        return storage.values().stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findProjectsWithSprints() {
        return storage.values().stream()
                .filter(p -> !p.getSprints().isEmpty())
                .collect(Collectors.toList());
    }
}
