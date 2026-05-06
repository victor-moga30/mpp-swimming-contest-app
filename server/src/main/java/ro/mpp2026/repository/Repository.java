package ro.mpp2026.repository;

public interface Repository<ID, E> {
    E findById(ID id);

    void save(E entity);
}