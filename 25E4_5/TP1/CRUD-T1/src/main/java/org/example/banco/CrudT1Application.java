package org.example.banco;

import org.example.banco.cli.ContaBancariaCLI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação de gerenciamento de contas bancárias.
 * 
 * Esta aplicação fornece um sistema CRUD completo com:
 * - Interface de linha de comando interativa
 * - Validações robustas de dados
 * - Tratamento de exceções consistente
 * - Testes automatizados com alta cobertura
 * - Código limpo seguindo princípios de Clean Code
 */
@SpringBootApplication
public class CrudT1Application implements CommandLineRunner {

    private final ContaBancariaCLI cli;

    public CrudT1Application(final ContaBancariaCLI cli) {
        this.cli = cli;
    }

    public static void main(String[] args) {
        // Configurar perfil do banco de dados
        // Altere para "h2" se preferir usar banco em memória para testes
        System.setProperty("spring.profiles.active", "mysql");
        
        SpringApplication.run(CrudT1Application.class, args);
    }

    @Override
    public void run(String... args) {
        // Iniciar interface de linha de comando
        cli.iniciar();
    }
}
