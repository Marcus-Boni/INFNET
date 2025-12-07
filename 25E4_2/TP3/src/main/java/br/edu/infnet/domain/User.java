package br.edu.infnet.domain;

import java.util.Objects;

public class User {
    private final String id;
    private final String nome;
    private final String email;
    private final String cargo;

    public User(String id, String nome, String email, String cargo) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo");
        this.cargo = Objects.requireNonNull(cargo, "Cargo não pode ser nulo");
    }

    public User atualizarEmail(String novoEmail) {
        return new User(this.id, this.nome, novoEmail, this.cargo);
    }

    public User definirCargo(String novoCargo) {
        return new User(this.id, this.nome, this.email, novoCargo);
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCargo() {
        return cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
