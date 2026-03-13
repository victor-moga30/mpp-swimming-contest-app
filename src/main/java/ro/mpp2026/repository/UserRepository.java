package ro.mpp2026.repository;

import ro.mpp2026.model.User;

public interface UserRepository {
    User findByUsername(String username);
}