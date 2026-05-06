package ro.mpp2026.service;

public interface IContestObserver {
    void contestDataUpdated() throws ServiceException;
}