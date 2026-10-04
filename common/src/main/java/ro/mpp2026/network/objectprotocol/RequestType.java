package ro.mpp2026.network.objectprotocol;

public enum RequestType {
    LOGIN,
    LOGOUT,
    GET_EVENTS,
    GET_CHILDREN_FOR_EVENT,
    SEARCH_CHILDREN,
    REGISTER_CHILD,
    GET_EVENT_IDS_FOR_CHILD,
    UPDATE_CHILD_REGISTRATIONS,
    GET_CHILD_BY_CNP
}