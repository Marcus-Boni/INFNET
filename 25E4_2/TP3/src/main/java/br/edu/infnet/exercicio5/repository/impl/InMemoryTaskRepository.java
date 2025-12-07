package br.edu.infnet.exercicio5.repository.impl;

import br.edu.infnet.domain.Task;
import br.edu.infnet.domain.enums.TaskStatus;
import br.edu.infnet.exercicio5.repository.TaskRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> storage;

    public InMemoryTaskRepository() {
        this.storage = new ConcurrentHashMap<>();
    }

    @Override
    public Task save(Task entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Task> findAll() {
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
    public List<Task> findByStatus(TaskStatus status) {
        return storage.values().stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByResponsavelId(String responsavelId) {
        return storage.values().stream()
                .filter(t -> t.getResponsavel().isPresent() && 
                            t.getResponsavel().get().getId().equals(responsavelId))
                .collect(Collectors.toList());
    }
}
