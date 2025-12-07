package br.edu.infnet.exercicio5.service;

import br.edu.infnet.domain.Project;
import br.edu.infnet.domain.Sprint;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Project criarProjeto(String nome, String descricao);
    Optional<Project> buscarPorId(String id);
    List<Project> listarTodos();
    Project adicionarSprint(String projectId, Sprint sprint);
    Project removerSprint(String projectId, String sprintId);
    void deletarProjeto(String id);
    List<Project> buscarPorNome(String nome);
}
