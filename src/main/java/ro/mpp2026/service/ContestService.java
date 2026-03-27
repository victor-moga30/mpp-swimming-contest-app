package ro.mpp2026.service;

import ro.mpp2026.model.Child;
import ro.mpp2026.model.Event;
import ro.mpp2026.model.Registration;
import ro.mpp2026.model.User;
import ro.mpp2026.repository.ChildRepository;
import ro.mpp2026.repository.EventRepository;
import ro.mpp2026.repository.RegistrationRepository;
import ro.mpp2026.repository.UserRepository;
import ro.mpp2026.service.dto.ChildRegistrationDTO;
import ro.mpp2026.service.dto.EventParticipantsDTO;
import ro.mpp2026.utils.PasswordUtils;

import java.util.ArrayList;
import java.util.List;

public class ContestService {
    private final UserRepository userRepository;
    private final ChildRepository childRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public ContestService(UserRepository userRepository,
                          ChildRepository childRepository,
                          EventRepository eventRepository,
                          RegistrationRepository registrationRepository) {
        this.userRepository = userRepository;
        this.childRepository = childRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ServiceException("Username inexistent.");
        }

        String hashedPassword = PasswordUtils.hashPassword(password);

        if (!user.getPasswordHash().equals(hashedPassword)) {
            throw new ServiceException("Parola incorecta.");
        }

        return user;
    }

    public List<EventParticipantsDTO> getAllEventsWithParticipantsCount() {
        List<Event> events = eventRepository.findAll();
        List<EventParticipantsDTO> result = new ArrayList<EventParticipantsDTO>();

        int i;
        for (i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            int count = registrationRepository.findByEventId(event.getId()).size();

            result.add(new EventParticipantsDTO(
                    event.getId(),
                    event.getName(),
                    event.getDistance(),
                    event.getMinAge(),
                    event.getMaxAge(),
                    count
            ));
        }

        return result;
    }

    public List<ChildRegistrationDTO> getChildrenForEvent(long eventId) {
        List<Registration> registrations = registrationRepository.findByEventId(eventId);
        List<ChildRegistrationDTO> result = new ArrayList<ChildRegistrationDTO>();

        int i;
        for (i = 0; i < registrations.size(); i++) {
            Registration registration = registrations.get(i);
            Child child = childRepository.findById(registration.getChildId());

            if (child != null) {
                result.add(buildChildRegistrationDTO(child));
            }
        }

        return result;
    }

    public List<ChildRegistrationDTO> searchChildren(long eventId, int minAge, int maxAge) {
        List<Registration> registrations = registrationRepository.findByEventId(eventId);
        List<ChildRegistrationDTO> result = new ArrayList<ChildRegistrationDTO>();

        int i;
        for (i = 0; i < registrations.size(); i++) {
            Registration registration = registrations.get(i);
            Child child = childRepository.findById(registration.getChildId());

            if (child != null && child.getAge() >= minAge && child.getAge() <= maxAge) {
                result.add(buildChildRegistrationDTO(child));
            }
        }

        return result;
    }

    public void registerChild(String name, String cnp, int age, List<Long> eventIds) {
        validateChildData(name, cnp, age);
        validateSelectedEvents(eventIds);

        Child child = childRepository.findByCnp(cnp);

        if (child == null) {
            child = new Child(0, name, cnp, age);
            childRepository.save(child);
            child = childRepository.findByCnp(cnp);
        } else {
            if (child.getAge() != age) {
                throw new ServiceException("Exista deja un copil cu acest CNP, dar cu alta varsta.");
            }
            if (!child.getName().equals(name)) {
                throw new ServiceException("Exista deja un copil cu acest CNP, dar cu alt nume.");
            }
        }

        List<Registration> existingRegistrations = registrationRepository.findByChildId(child.getId());

        if (existingRegistrations.size() + eventIds.size() > 2) {
            throw new ServiceException("Un copil poate fi inscris la maximum 2 probe.");
        }

        int i;
        for (i = 0; i < eventIds.size(); i++) {
            long eventId = eventIds.get(i);
            Event event = eventRepository.findById(eventId);

            if (event == null) {
                throw new ServiceException("Proba inexistenta.");
            }

            if (!event.isAllowedForAge(age)) {
                throw new ServiceException("Proba " + event.getName() + " nu este permisa pentru varsta " + age + ".");
            }

            if (isAlreadyRegistered(existingRegistrations, eventId)) {
                throw new ServiceException("Copilul este deja inscris la aceasta proba.");
            }
        }

        for (i = 0; i < eventIds.size(); i++) {
            registrationRepository.save(new Registration(0, child.getId(), eventIds.get(i)));
        }
    }

    public List<Long> getEventIdsForChild(String cnp) {
        if (cnp == null || cnp.trim().isEmpty()) {
            throw new ServiceException("CNP-ul nu poate fi gol.");
        }

        Child child = childRepository.findByCnp(cnp);
        if (child == null) {
            throw new ServiceException("Nu exista copil cu acest CNP.");
        }

        List<Registration> registrations = registrationRepository.findByChildId(child.getId());
        List<Long> result = new ArrayList<Long>();

        int i;
        for (i = 0; i < registrations.size(); i++) {
            result.add(registrations.get(i).getEventId());
        }

        return result;
    }

    public void updateChildRegistrations(String cnp, List<Long> newEventIds) {
        if (cnp == null || cnp.trim().isEmpty()) {
            throw new ServiceException("CNP-ul nu poate fi gol.");
        }

        validateSelectedEvents(newEventIds);

        Child child = childRepository.findByCnp(cnp);
        if (child == null) {
            throw new ServiceException("Nu exista niciun copil cu acest CNP.");
        }

        int i;
        for (i = 0; i < newEventIds.size(); i++) {
            Event event = eventRepository.findById(newEventIds.get(i));
            if (event == null) {
                throw new ServiceException("Proba inexistenta.");
            }

            if (!event.isAllowedForAge(child.getAge())) {
                throw new ServiceException("Proba " + event.getName() + " nu este permisa pentru varsta copilului.");
            }
        }

        List<Registration> oldRegistrations = registrationRepository.findByChildId(child.getId());

        for (i = 0; i < oldRegistrations.size(); i++) {
            registrationRepository.delete(oldRegistrations.get(i));
        }

        for (i = 0; i < newEventIds.size(); i++) {
            registrationRepository.save(new Registration(0, child.getId(), newEventIds.get(i)));
        }
    }

    private ChildRegistrationDTO buildChildRegistrationDTO(Child child) {
        List<Registration> registrations = registrationRepository.findByChildId(child.getId());
        String eventsText = buildEventsTextForChild(child.getId());

        return new ChildRegistrationDTO(
                child.getId(),
                child.getName(),
                child.getCnp(),
                child.getAge(),
                eventsText
        );
    }

    private void validateChildData(String name, String cnp, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new ServiceException("Numele nu poate fi gol.");
        }

        if (cnp == null || cnp.trim().isEmpty()) {
            throw new ServiceException("CNP-ul nu poate fi gol.");
        }

        if (cnp.length() != 13) {
            throw new ServiceException("CNP-ul trebuie sa aiba exact 13 cifre.");
        }

        int i;
        for (i = 0; i < cnp.length(); i++) {
            if (!Character.isDigit(cnp.charAt(i))) {
                throw new ServiceException("CNP-ul trebuie sa contina doar cifre.");
            }
        }

        if (age < 6 || age > 15) {
            throw new ServiceException("Varsta trebuie sa fie intre 6 si 15.");
        }
    }

    private void validateSelectedEvents(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            throw new ServiceException("Trebuie selectata cel putin o proba.");
        }

        if (eventIds.size() > 2) {
            throw new ServiceException("Se pot selecta maximum 2 probe.");
        }

        if (eventIds.size() == 2 && eventIds.get(0).longValue() == eventIds.get(1).longValue()) {
            throw new ServiceException("Nu poti selecta aceeasi proba de doua ori.");
        }
    }

    private boolean isAlreadyRegistered(List<Registration> registrations, long eventId) {
        int i;
        for (i = 0; i < registrations.size(); i++) {
            if (registrations.get(i).getEventId() == eventId) {
                return true;
            }
        }
        return false;
    }

    private String buildEventsTextForChild(long childId) {
        List<Registration> registrations = registrationRepository.findByChildId(childId);
        StringBuilder builder = new StringBuilder();

        int i;
        for (i = 0; i < registrations.size(); i++) {
            Event event = eventRepository.findById(registrations.get(i).getEventId());
            if (event != null) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(event.getName());
            }
        }

        return builder.toString();
    }
}