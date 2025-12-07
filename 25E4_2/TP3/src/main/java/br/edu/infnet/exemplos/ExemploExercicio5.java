package br.edu.infnet.exemplos;

import br.edu.infnet.domain.Project;
import br.edu.infnet.domain.Sprint;
import br.edu.infnet.domain.Task;
import br.edu.infnet.domain.enums.TaskStatus;
import br.edu.infnet.exercicio5.repository.ProjectRepository;
import br.edu.infnet.exercicio5.repository.TaskRepository;
import br.edu.infnet.exercicio5.repository.impl.InMemoryProjectRepository;
import br.edu.infnet.exercicio5.repository.impl.InMemoryTaskRepository;
import br.edu.infnet.exercicio5.service.ProjectService;
import br.edu.infnet.exercicio5.service.TaskService;
import br.edu.infnet.exercicio5.service.impl.ProjectServiceImpl;
import br.edu.infnet.exercicio5.service.impl.TaskServiceImpl;

import java.time.LocalDate;

public class ExemploExercicio5 {
    public static void main(String[] args) {
        System.out.println("=== Exercício 5: Arquitetura e Injeção de Dependência ===\n");

        ProjectRepository projectRepository = new InMemoryProjectRepository();
        ProjectService projectService = new ProjectServiceImpl(projectRepository);

        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskService taskService = new TaskServiceImpl(taskRepository);

        System.out.println("Camadas da arquitetura:");
        System.out.println("1. Repository - Persistência de dados");
        System.out.println("2. Service - Lógica de negócio e orquestração");
        System.out.println("3. Domain - Entidades e regras de domínio\n");

        Project projeto = projectService.criarProjeto(
            "Sistema de E-commerce",
            "Desenvolvimento de plataforma de vendas online"
        );
        System.out.println("Projeto criado via Service:");
        System.out.println(projeto);
        System.out.println();

        Task task1 = taskService.criarTask(
            "Desenvolver catálogo de produtos",
            "Implementar listagem e busca de produtos",
            TaskStatus.TODO
        );
        System.out.println("Task criada via Service:");
        System.out.println(task1);
        System.out.println();

        Task taskAtualizada = taskService.alterarStatus(task1.getId(), TaskStatus.IN_PROGRESS);
        System.out.println("Task após atualizar status:");
        System.out.println(taskAtualizada);
        System.out.println();

        Sprint sprint = new Sprint(
            "sprint-1",
            "Sprint 1 - Setup",
            LocalDate.now(),
            LocalDate.now().plusWeeks(2)
        );
        sprint = sprint.adicionarTarefa(taskAtualizada);

        Project projetoAtualizado = projectService.adicionarSprint(projeto.getId(), sprint);
        System.out.println("Projeto após adicionar sprint:");
        System.out.println(projetoAtualizado);
        System.out.println();

        System.out.println("Princípios aplicados:");
        System.out.println("✓ Injeção de Dependência via Construtor");
        System.out.println("✓ Programar para Interfaces, não Implementações");
        System.out.println("✓ Separação de Responsabilidades (SRP)");
        System.out.println("✓ Baixo Acoplamento");
        System.out.println("✓ Alta Coesão");
        System.out.println();

        System.out.println("Vantagens da arquitetura:");
        System.out.println("• Fácil substituir implementação do repositório");
        System.out.println("• Testável (pode mockar dependências)");
        System.out.println("• Manutenível (mudanças isoladas)");
        System.out.println("• Escalável (adicionar funcionalidades sem quebrar)");
    }
}
