package ro.mpp2026.network.utils;

import ro.mpp2026.model.User;
import ro.mpp2026.network.dto.UserDTO;

public final class DtoUtils {
    private DtoUtils() {
    }

    public static UserDTO toDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getOffice());
    }

    public static User fromDto(UserDTO dto) {
        return new User(dto.getId(), dto.getUsername(), null, dto.getOffice());
    }
}