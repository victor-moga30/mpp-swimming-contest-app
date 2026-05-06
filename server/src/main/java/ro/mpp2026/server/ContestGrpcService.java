package ro.mpp2026.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import ro.mpp2026.grpc.*;
import ro.mpp2026.model.User;
import ro.mpp2026.service.IContestObserver;
import ro.mpp2026.service.IContestServices;
import ro.mpp2026.service.ServiceException;
import ro.mpp2026.service.dto.ChildDTO;
import ro.mpp2026.service.dto.ChildRegistrationDTO;
import ro.mpp2026.service.dto.EventParticipantsDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ContestGrpcService extends ContestRpcGrpc.ContestRpcImplBase {
    private final IContestServices service;
    private final ConcurrentHashMap<String, GrpcObserver> grpcObservers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, StreamObserver<UpdateNotification>> subscribers =
            new ConcurrentHashMap<String, StreamObserver<UpdateNotification>>();

    public ContestGrpcService(IContestServices service) {
        this.service = service;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            GrpcObserver observer = new GrpcObserver(request.getUsername());
            User user = service.login(request.getUsername(), request.getPassword(), observer);
            grpcObservers.put(request.getUsername(), observer);

            UserDto userDto = UserDto.newBuilder()
                    .setId(user.getId())
                    .setUsername(user.getUsername())
                    .setPasswordHash(user.getPasswordHash())
                    .setOffice(user.getOffice())
                    .build();

            responseObserver.onNext(UserResponse.newBuilder().setUser(userDto).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void logout(UserRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            GrpcObserver observer = grpcObservers.remove(request.getUsername());
            if (observer == null) {
                observer = new GrpcObserver(request.getUsername());
            }

            service.logout(user, observer);
            subscribers.remove(request.getUsername());

            responseObserver.onNext(EmptyResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getEventSummaries(EmptyRequest request, StreamObserver<EventSummariesResponse> responseObserver) {
        try {
            EventSummariesResponse.Builder builder = EventSummariesResponse.newBuilder();

            for (EventParticipantsDTO dto : service.getAllEventsWithParticipantsCount()) {
                builder.addEventSummaries(EventSummaryDto.newBuilder()
                        .setEventId(dto.getEventId())
                        .setName(dto.getEventName())
                        .setDistance(dto.getDistance())
                        .setAgeGroup(dto.getMinAge() + "-" + dto.getMaxAge())
                        .setParticipantsCount(dto.getParticipantsCount())
                        .build());
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void searchChildren(SearchChildrenRequest request, StreamObserver<ChildSearchResultsResponse> responseObserver) {
        try {
            ChildSearchResultsResponse.Builder builder = ChildSearchResultsResponse.newBuilder();

            List<ChildRegistrationDTO> children = service.searchChildren(
                    request.getEventId(),
                    request.getMinAge(),
                    request.getMaxAge()
            );

            for (ChildRegistrationDTO dto : children) {
                builder.addSearchResults(ChildSearchResultDto.newBuilder()
                        .setChildId(dto.getChildId())
                        .setName(dto.getChildName())
                        .setAge(dto.getAge())
                        .setNumberOfEvents(countEvents(dto.getEvents()))
                        .build());
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void registerChild(RegisterChildRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            List<Long> ids = new ArrayList<Long>();
            ids.addAll(request.getEventIdsList());

            service.registerChild(request.getName(), request.getCnp(), request.getAge(), ids);

            notifySubscribers();

            responseObserver.onNext(EmptyResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void findChildByCnp(CnpRequest request, StreamObserver<ChildResponse> responseObserver) {
        try {
            ChildDTO child = service.getChildByCnp(request.getCnp());

            ChildDto dto = ChildDto.newBuilder()
                    .setId(child.getId())
                    .setName(child.getName())
                    .setCnp(child.getCnp())
                    .setAge(child.getAge())
                    .build();

            responseObserver.onNext(ChildResponse.newBuilder().setChild(dto).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getChildEventIds(ChildIdRequest request, StreamObserver<EventIdsResponse> responseObserver) {
        try {
            List<Long> ids = service.getChildEventIds(request.getChildId());

            EventIdsResponse.Builder builder = EventIdsResponse.newBuilder();
            builder.addAllEventIds(ids);

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void updateChildRegistrations(UpdateChildRegistrationsRequest request, StreamObserver<EmptyResponse> responseObserver) {
        try {
            List<Long> ids = new ArrayList<Long>();
            ids.addAll(request.getEventIdsList());

            service.updateChildRegistrations(request.getCnp(), ids);

            notifySubscribers();

            responseObserver.onNext(EmptyResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void subscribeUpdates(UserRequest request, StreamObserver<UpdateNotification> responseObserver) {
        subscribers.put(request.getUsername(), responseObserver);
    }

    private void notifySubscribers() {
        for (StreamObserver<UpdateNotification> observer : subscribers.values()) {
            try {
                observer.onNext(UpdateNotification.newBuilder()
                        .setMessage("refresh")
                        .build());
            } catch (Exception ignored) {
            }
        }
    }

    private int countEvents(String events) {
        if (events == null || events.trim().isEmpty()) {
            return 0;
        }
        return events.split(",").length;
    }

    private class GrpcObserver implements IContestObserver {
        private final String username;

        public GrpcObserver(String username) {
            this.username = username;
        }

        @Override
        public void contestDataUpdated() throws ServiceException {
            StreamObserver<UpdateNotification> observer = subscribers.get(username);
            if (observer != null) {
                observer.onNext(UpdateNotification.newBuilder().setMessage("refresh").build());
            }
        }
    }
}