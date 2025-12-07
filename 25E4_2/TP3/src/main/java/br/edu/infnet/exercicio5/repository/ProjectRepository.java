package br.edu.infnet.exercicio5.repository;

import br.edu.infnet.domain.Project;
import java.util.List;

public interface ProjectRepository extends Repository<Project, String> {
    List<Project> findByNome(String nome);
    List<Project> findProjectsWithSprints();
}
