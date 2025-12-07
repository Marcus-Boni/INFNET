package br.edu.infnet.exemplos;

import br.edu.infnet.domain.Project;
import br.edu.infnet.domain.Sprint;
import br.edu.infnet.domain.Task;
import br.edu.infnet.domain.User;
import br.edu.infnet.domain.enums.TaskStatus;

import java.time.LocalDate;

public class ExemploSistemaGestao {
    public static void main(String[] args) {
        System.out.println("=== Sistema de Gestão de Projetos Ágil ===\n");

        User usuario1 = new User("user-1", "João Silva", "joao@email.com", "Desenvolvedor");
        User usuario2 = new User("user-2", "Maria Santos", "maria@email.com", "Tech Lead");

        System.out.println("Usuários criados:");
        System.out.println(usuario1);
        System.out.println(usuario2);
        System.out.println();

        Task task1 = new Task("task-1", "Implementar login", 
            "Desenvolver tela de login com autenticação", TaskStatus.TODO, null);
        Task task2 = new Task("task-2", "Criar API REST", 
            "Desenvolver endpoints da API", TaskStatus.TODO, null);

        task1 = task1.atribuirResponsavel(usuario1);
        task2 = task2.atribuirResponsavel(usuario2);

        System.out.println("Tarefas criadas e atribuídas:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();

        Sprint sprint1 = new Sprint("sprint-1", "Sprint 1", 
            LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 15));
        
        sprint1 = sprint1.adicionarTarefa(task1);
        sprint1 = sprint1.adicionarTarefa(task2);

        System.out.println("Sprint criada:");
        System.out.println(sprint1);
        System.out.println();

        Project projeto = new Project("proj-1", "Sistema Web", 
            "Desenvolvimento de sistema web corporativo");
        
        projeto = projeto.adicionarSprint(sprint1);

        System.out.println("Projeto criado:");
        System.out.println(projeto);
        System.out.println();

        projeto.listarSprints();
        System.out.println();

        sprint1.listarTarefas();
        System.out.println();

        task1 = task1.alterarStatus(TaskStatus.IN_PROGRESS);
        System.out.println("Tarefa atualizada para IN_PROGRESS:");
        task1.exibirDetalhes();
        System.out.println();

        task1 = task1.alterarStatus(TaskStatus.DONE);
        System.out.println("Tarefa concluída:");
        task1.exibirDetalhes();
    }
}
