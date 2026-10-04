package ro.mpp2026.repository;

import ro.mpp2026.model.Child;

public interface ChildRepository extends Repository<Long, Child> {
    Child findByCnp(String cnp);
}