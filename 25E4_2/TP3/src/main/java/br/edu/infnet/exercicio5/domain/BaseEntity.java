package br.edu.infnet.exercicio5.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class BaseEntity {
    private final String id;
    private final List<String> tags;

    protected BaseEntity(String id) {
        this(id, new ArrayList<>());
    }

    protected BaseEntity(String id, List<String> tags) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.tags = new ArrayList<>(tags);
    }

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
