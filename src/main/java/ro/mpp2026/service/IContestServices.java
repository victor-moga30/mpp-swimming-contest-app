package ro.mpp2026.service;

import ro.mpp2026.model.User;
import ro.mpp2026.service.dto.ChildDTO;
import ro.mpp2026.service.dto.ChildRegistrationDTO;
import ro.mpp2026.service.dto.EventParticipantsDTO;

import java.util.List;

public interface IContestServices {
    User login(String username, String password, IContestObserver observer) throws ServiceException;
    void logout(User user, IContestObserver observer) throws ServiceException;
    List<EventParticipantsDTO> getAllEventsWithParticipantsCount() throws ServiceException;
    List<ChildRegistrationDTO> getChildrenForEvent(long eventId) throws ServiceException;
    List<ChildRegistrationDTO> searchChildren(long eventId, int minAge, int maxAge) throws ServiceException;
    void registerChild(String name, String cnp, int age, List<Long> eventIds) throws ServiceException;
    List<Long> getEventIdsForChild(String cnp) throws ServiceException;
    void updateChildRegistrations(String cnp, List<Long> newEventIds) throws ServiceException;
    ChildDTO getChildByCnp(String cnp) throws ServiceException;
}