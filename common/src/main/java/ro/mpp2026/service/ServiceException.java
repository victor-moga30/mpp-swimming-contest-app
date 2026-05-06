package ro.mpp2026.service;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}