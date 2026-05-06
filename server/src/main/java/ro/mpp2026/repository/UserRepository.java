package ro.mpp2026.repository;

import ro.mpp2026.model.User;

public interface UserRepository extends Repository<Long, User> {
    User findByUsername(String username);
}