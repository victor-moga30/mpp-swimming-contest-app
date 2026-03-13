package ro.mpp2026.repository;

import ro.mpp2026.model.Child;

public interface ChildRepository {
    Child findByCnp(String cnp);

    Child findById(long id);

    void save(Child child);
}