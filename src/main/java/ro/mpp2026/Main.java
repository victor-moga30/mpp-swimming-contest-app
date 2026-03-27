package ro.mpp2026;

import ro.mpp2026.repository.db.ChildDbRepository;
import ro.mpp2026.repository.db.EventDbRepository;
import ro.mpp2026.repository.db.JdbcUtils;
import ro.mpp2026.repository.db.RegistrationDbRepository;
import ro.mpp2026.repository.db.UserDbRepository;
import ro.mpp2026.service.ContestService;
import ro.mpp2026.service.dto.EventParticipantsDTO;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JdbcUtils jdbcUtils = new JdbcUtils();

        ContestService service = new ContestService(
                new UserDbRepository(jdbcUtils),
                new ChildDbRepository(jdbcUtils),
                new EventDbRepository(jdbcUtils),
                new RegistrationDbRepository(jdbcUtils)
        );

        System.out.println(service.login("oficiu1", "1234"));

        List<EventParticipantsDTO> events = service.getAllEventsWithParticipantsCount();
        int i;
        for (i = 0; i < events.size(); i++) {
            System.out.println(events.get(i).getEventName() + " " +
                    events.get(i).getDistance() + "m -> " +
                    events.get(i).getParticipantsCount());
        }

        List<Long> selectedEvents = new ArrayList<Long>();
        selectedEvents.add(1L);
        selectedEvents.add(2L);

        service.registerChild("Ana Pop", "1234567890123", 7, selectedEvents);

        System.out.println(service.getChildrenForEvent(1L));
    }
}