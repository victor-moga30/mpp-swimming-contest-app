package ro.mpp2026.network.objectprotocol;

import ro.mpp2026.model.User;
import ro.mpp2026.network.dto.RegisterChildDTO;
import ro.mpp2026.network.dto.SearchDTO;
import ro.mpp2026.network.dto.UpdateChildDTO;
import ro.mpp2026.network.dto.UserDTO;
import ro.mpp2026.network.utils.DtoUtils;
import ro.mpp2026.service.IContestObserver;
import ro.mpp2026.service.IContestServices;
import ro.mpp2026.service.ServiceException;
import ro.mpp2026.service.dto.ChildDTO;
import ro.mpp2026.service.dto.ChildRegistrationDTO;
import ro.mpp2026.service.dto.EventParticipantsDTO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ContestServicesObjectProxy implements IContestServices {
    private final String host;
    private final int port;
    private IContestObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private volatile boolean finished;
    private final BlockingQueue<Response> qresponses = new LinkedBlockingQueue<Response>();

    public ContestServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public User login(String username, String password, IContestObserver observer) {
        initializeConnection();
        this.client = observer;
        sendRequest(new Request(RequestType.LOGIN, new UserDTO(username, password)));
        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            return DtoUtils.fromDto((UserDTO) response.getData());
        }
        closeConnection();
        throw new ServiceException(String.valueOf(response.getData()));
    }

    @Override
    public void logout(User user, IContestObserver observer) {
        sendRequest(new Request(RequestType.LOGOUT, null));
        Response response = readResponse();
        closeConnection();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EventParticipantsDTO> getAllEventsWithParticipantsCount() {
        sendRequest(new Request(RequestType.GET_EVENTS, null));
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
        return (List<EventParticipantsDTO>) response.getData();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ChildRegistrationDTO> getChildrenForEvent(long eventId) {
        sendRequest(new Request(RequestType.GET_CHILDREN_FOR_EVENT, Long.valueOf(eventId)));
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
        return (List<ChildRegistrationDTO>) response.getData();
    }

    @Override
    public ChildDTO getChildByCnp(String cnp) {
        sendRequest(new Request(RequestType.GET_CHILD_BY_CNP, cnp));
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
        return (ChildDTO) response.getData();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ChildRegistrationDTO> searchChildren(long eventId, int minAge, int maxAge) {
        sendRequest(new Request(RequestType.SEARCH_CHILDREN, new SearchDTO(eventId, minAge, maxAge)));
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
        return (List<ChildRegistrationDTO>) response.getData();
    }

    @Override
    public void registerChild(String name, String cnp, int age, List<Long> eventIds) {
        sendRequest(new Request(RequestType.REGISTER_CHILD, new RegisterChildDTO(name, cnp, age, eventIds)));
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> getEventIdsForChild(String cnp) {
        sendRequest(new Request(RequestType.GET_EVENT_IDS_FOR_CHILD, cnp));
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
        return (List<Long>) response.getData();
    }

    @Override
    public void updateChildRegistrations(String cnp, List<Long> newEventIds) {
        sendRequest(new Request(RequestType.UPDATE_CHILD_REGISTRATIONS, new UpdateChildDTO(cnp, newEventIds)));
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServiceException(String.valueOf(response.getData()));
        }
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            throw new ServiceException("Nu se poate realiza conexiunea la server: " + e.getMessage());
        }
    }

    private void startReader() {
        Thread thread = new Thread(new ReaderThread());
        thread.setDaemon(true);
        thread.start();
    }

    private void sendRequest(Request request) {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Eroare la trimiterea cererii: " + e.getMessage());
        }
    }

    private Response readResponse() {
        try {
            return qresponses.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceException("Firul de executie a fost intrerupt.");
        }
    }

    private void handleUpdate(Response response) {
        if (client == null) {
            return;
        }

        try {
            client.contestDataUpdated();
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ignored) {
        }
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException ignored) {
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (IOException ignored) {
        }
        client = null;
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.UPDATE;
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    Object responseObject = input.readObject();
                    Response response = (Response) responseObject;
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        qresponses.put(response);
                    }
                } catch (Exception e) {
                    if (!finished) {
                        qresponses.offer(new Response(ResponseType.ERROR, "Conexiunea cu serverul a fost inchisa."));
                    }
                    finished = true;
                }
            }
        }
    }
}