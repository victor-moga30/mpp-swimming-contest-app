package ro.mpp2026.repository;

import ro.mpp2026.model.Registration;

import java.util.List;

public interface RegistrationRepository {
    List<Registration> findByChildId(long childId);

    List<Registration> findByEventId(long eventId);

    void save(Registration registration);

    void delete(Registration registration);
}