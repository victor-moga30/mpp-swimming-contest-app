package ro.mpp2026.repository;

import ro.mpp2026.model.Registration;

import java.util.List;

public interface RegistrationRepository extends Repository<Long, Registration> {
    List<Registration> findByChildId(long childId);

    List<Registration> findByEventId(long eventId);

    void delete(Registration registration);

    List<Long> findEventIdsByChildId(Long childId);
}