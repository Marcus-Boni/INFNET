package br.edu.infnet.exercicio5.repository;

import br.edu.infnet.domain.Task;
import br.edu.infnet.domain.enums.TaskStatus;
import java.util.List;

public interface TaskRepository extends Repository<Task, String> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByResponsavelId(String responsavelId);
}
