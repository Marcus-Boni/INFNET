package br.edu.infnet.exercicio5.service.impl;

import br.edu.infnet.domain.Task;
import br.edu.infnet.domain.User;
import br.edu.infnet.domain.enums.TaskStatus;
import br.edu.infnet.exercicio5.repository.TaskRepository;
import br.edu.infnet.exercicio5.service.TaskService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = Objects.requireNonNull(taskRepository, 
            "TaskRepository não pode ser nulo");
    }

    @Override
    public Task criarTask(String titulo, String descricao, TaskStatus status) {
        Task task = new Task(UUID.randomUUID().toString(), titulo, descricao, status, null);
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> buscarPorId(String id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> listarTodas() {
        return taskRepository.findAll();
    }

    @Override
    public Task atribuirResponsavel(String taskId, User responsavel) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task não encontrada"));
        
        Task taskAtualizada = task.atribuirResponsavel(responsavel);
        return taskRepository.save(taskAtualizada);
    }

    @Override
    public Task alterarStatus(String taskId, TaskStatus novoStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task não encontrada"));
        
        Task taskAtualizada = task.alterarStatus(novoStatus);
        return taskRepository.save(taskAtualizada);
    }

    @Override
    public void deletarTask(String id) {
        taskRepository.delete(id);
    }

    @Override
    public List<Task> buscarPorStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> buscarPorResponsavel(String responsavelId) {
        return taskRepository.findByResponsavelId(responsavelId);
    }
}
