package br.edu.infnet.exercicio5.service.impl;

import br.edu.infnet.domain.Project;
import br.edu.infnet.domain.Sprint;
import br.edu.infnet.exercicio5.repository.ProjectRepository;
import br.edu.infnet.exercicio5.service.ProjectService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = Objects.requireNonNull(projectRepository, 
            "ProjectRepository não pode ser nulo");
    }

    @Override
    public Project criarProjeto(String nome, String descricao) {
        Project project = new Project(UUID.randomUUID().toString(), nome, descricao);
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> buscarPorId(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> listarTodos() {
        return projectRepository.findAll();
    }

    @Override
    public Project adicionarSprint(String projectId, Sprint sprint) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
        
        Project projetoAtualizado = project.adicionarSprint(sprint);
        return projectRepository.save(projetoAtualizado);
    }

    @Override
    public Project removerSprint(String projectId, String sprintId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
        
        Project projetoAtualizado = project.removerSprint(sprintId);
        return projectRepository.save(projetoAtualizado);
    }

    @Override
    public void deletarProjeto(String id) {
        projectRepository.delete(id);
    }

    @Override
    public List<Project> buscarPorNome(String nome) {
        return projectRepository.findByNome(nome);
    }
}
