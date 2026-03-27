package ro.mpp2026.repository;

import ro.mpp2026.model.Event;

import java.util.List;

public interface EventRepository extends Repository<Long, Event> {
    List<Event> findAll();
}