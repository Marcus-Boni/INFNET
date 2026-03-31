package br.infnet.at.q4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {
    private final String name;
    private final String email;
    private final List<Address> addresses;

    public User(String name, String email) {
        this.name = Objects.requireNonNull(name, "name nao pode ser nulo");
        this.email = Objects.requireNonNull(email, "email nao pode ser nulo");
        this.addresses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addAddress(Address address) {
        addresses.add(Objects.requireNonNull(address, "address nao pode ser nulo"));
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }
}
