package br.edu.infnet.exercicio5.service;

import br.edu.infnet.domain.Task;
import br.edu.infnet.domain.User;
import br.edu.infnet.domain.enums.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task criarTask(String titulo, String descricao, TaskStatus status);
    Optional<Task> buscarPorId(String id);
    List<Task> listarTodas();
    Task atribuirResponsavel(String taskId, User responsavel);
    Task alterarStatus(String taskId, TaskStatus novoStatus);
    void deletarTask(String id);
    List<Task> buscarPorStatus(TaskStatus status);
    List<Task> buscarPorResponsavel(String responsavelId);
}
