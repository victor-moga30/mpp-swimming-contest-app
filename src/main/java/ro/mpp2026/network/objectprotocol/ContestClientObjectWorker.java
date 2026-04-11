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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ContestClientObjectWorker implements Runnable, IContestObserver {
    private final IContestServices server;
    private final Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private User loggedUser;

    public ContestClientObjectWorker(IContestServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        this.connected = true;

        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                Object requestObject = input.readObject();
                if (!(requestObject instanceof Request)) {
                    continue;
                }

                Response response = handleRequest((Request) requestObject);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (EOFException e) {
                connected = false;
            } catch (Exception e) {
                try {
                    sendResponse(new Response(ResponseType.ERROR, e.getMessage()));
                } catch (Exception ignored) {
                }
                connected = false;
            }
        }

        closeConnection();
    }

    private Response handleRequest(Request request) {
        try {
            switch (request.getType()) {
                case LOGIN:
                    UserDTO loginDto = (UserDTO) request.getData();
                    User user = server.login(loginDto.getUsername(), loginDto.getPassword(), this);
                    loggedUser = user;
                    return new Response(ResponseType.OK, DtoUtils.toDto(user));

                case LOGOUT:
                    if (loggedUser != null) {
                        server.logout(loggedUser, this);
                    }
                    connected = false;
                    return new Response(ResponseType.OK, null);

                case GET_EVENTS:
                    return new Response(ResponseType.EVENTS, server.getAllEventsWithParticipantsCount());

                case GET_CHILDREN_FOR_EVENT:
                    Long eventId = (Long) request.getData();
                    return new Response(ResponseType.CHILDREN, server.getChildrenForEvent(eventId.longValue()));

                case SEARCH_CHILDREN:
                    SearchDTO searchDTO = (SearchDTO) request.getData();
                    return new Response(
                            ResponseType.CHILDREN,
                            server.searchChildren(searchDTO.getEventId(), searchDTO.getMinAge(), searchDTO.getMaxAge())
                    );

                case REGISTER_CHILD:
                    RegisterChildDTO registerChildDTO = (RegisterChildDTO) request.getData();
                    server.registerChild(
                            registerChildDTO.getName(),
                            registerChildDTO.getCnp(),
                            registerChildDTO.getAge(),
                            registerChildDTO.getEventIds()
                    );
                    return new Response(ResponseType.OK, null);

                case GET_EVENT_IDS_FOR_CHILD:
                    String cnp = (String) request.getData();
                    return new Response(ResponseType.EVENT_IDS, server.getEventIdsForChild(cnp));

                case UPDATE_CHILD_REGISTRATIONS:
                    UpdateChildDTO updateChildDTO = (UpdateChildDTO) request.getData();
                    server.updateChildRegistrations(updateChildDTO.getCnp(), updateChildDTO.getEventIds());
                    return new Response(ResponseType.OK, null);

                case GET_CHILD_BY_CNP:
                    String childCnp = (String) request.getData();
                    ChildDTO childDTO = server.getChildByCnp(childCnp);
                    return new Response(ResponseType.OK, childDTO);

                default:
                    return new Response(ResponseType.ERROR, "Cerere necunoscuta.");
            }
        } catch (ServiceException e) {
            return new Response(ResponseType.ERROR, e.getMessage());
        }
    }

    private synchronized void sendResponse(Response response) throws IOException {
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void contestDataUpdated() throws ServiceException {
        try {
            sendResponse(new Response(ResponseType.UPDATE, "refresh"));
        } catch (IOException e) {
            throw new ServiceException("Eroare la trimiterea notificarii catre client.");
        }
    }

    private void closeConnection() {
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
    }
}