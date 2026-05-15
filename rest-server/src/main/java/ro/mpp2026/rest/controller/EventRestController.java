package ro.mpp2026.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2026.model.Event;
import ro.mpp2026.repository.EventRepository;
import ro.mpp2026.repository.hibernate.EventHibernateRepository;
import ro.mpp2026.rest.dto.EventCreateRequest;
import ro.mpp2026.rest.dto.EventUpdateRequest;
import ro.mpp2026.rest.dto.ErrorResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRestController {
    private final EventRepository eventRepository;

    public EventRestController() {
        this.eventRepository = new EventHibernateRepository();
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(
            @RequestParam(value = "distance", required = false) Integer distance) {
        List<Event> events;

        if (distance == null) {
            events = eventRepository.findAll();
        } else {
            events = eventRepository.findByDistance(distance);
        }

        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Event event = eventRepository.findById(id);

        if (event == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Proba cu id-ul " + id + " nu exista."));
        }

        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequest request) {
        validateEventData(request.getName(), request.getDistance(), request.getMinAge(), request.getMaxAge());

        Event event = new Event(
                0,
                request.getName().trim(),
                request.getDistance(),
                request.getMinAge(),
                request.getMaxAge()
        );

        eventRepository.save(event);

        return ResponseEntity
                .created(URI.create("/api/events/" + event.getId()))
                .body(event.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody EventUpdateRequest request) {
        validateEventData(request.getName(), request.getDistance(), request.getMinAge(), request.getMaxAge());

        Event existing = eventRepository.findById(id);

        if (existing == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Proba cu id-ul " + id + " nu exista."));
        }

        Event updated = new Event(
                id,
                request.getName().trim(),
                request.getDistance(),
                request.getMinAge(),
                request.getMaxAge()
        );

        Event result = eventRepository.update(updated);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        Event existing = eventRepository.findById(id);

        if (existing == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Proba cu id-ul " + id + " nu exista."));
        }

        try {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("Proba nu poate fi stearsa deoarece poate avea inscrieri asociate."));
        }
    }

    private void validateEventData(String name, int distance, int minAge, int maxAge) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele probei nu poate fi gol.");
        }

        if (distance <= 0) {
            throw new IllegalArgumentException("Distanta trebuie sa fie pozitiva.");
        }

        if (minAge < 0 || maxAge < 0) {
            throw new IllegalArgumentException("Varstele nu pot fi negative.");
        }

        if (minAge > maxAge) {
            throw new IllegalArgumentException("Varsta minima nu poate fi mai mare decat varsta maxima.");
        }

        if (minAge < 6 || maxAge > 15) {
            throw new IllegalArgumentException("Pentru concursul acesta varsta trebuie sa fie intre 6 si 15 ani.");
        }
    }
}