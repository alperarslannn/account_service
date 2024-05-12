package account.api.security.event;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum SecurityEventType {
    CREATE_USER("A user has been successfully registered", "CREATE_USER", "/api/auth/signup", ""),
    CHANGE_PASSWORD("A user has changed the password successfully", "CHANGE_PASSWORD", "/api/auth/changepass", ""),


    //todo
    ACCESS_DENIED("A user is trying to access a resource without access rights", "ACCESS_DENIED", "", ""),
    LOGIN_FAILED("Failed authentication", "LOGIN_FAILED", "", ""),


    GRANT_ROLE("A role is granted to a user", "GRANT_ROLE", "/api/admin/user/role", "GRANT"),
    REMOVE_ROLE("A role has been revoked", "REMOVE_ROLE", "/api/admin/user/role", "REMOVE"),
    LOCK_USER("The Administrator has locked the user", "LOCK_USER", "/api/admin/user/access", "LOCK"),
    UNLOCK_USER("The Administrator has unlocked a user", "UNLOCK_USER", "/api/admin/user/access", "UNLOCK"),


    //todo
    DELETE_USER("The Administrator has deleted a user", "DELETE_USER", "/api/admin/user", ""),
    BRUTE_FORCE("A user has been blocked on suspicion of a brute force attack", "BRUTE_FORCE", "", "");


    private final String description;
    private final String eventName;
    private final String path;
    private final String type;

    SecurityEventType(String description, String eventName, String path, String type) {
        this.description = description;
        this.eventName = eventName;
        this.path = path;
        this.type = type;
    }

    public static List<SecurityEventType> findFilledPath() {
        return Arrays.stream(SecurityEventType.values())
                .filter(securityEventType -> !securityEventType.path.equals("")).toList();
    }

    public static Optional<SecurityEventType> findByPath(String path) {
        return Arrays.stream(SecurityEventType.values())
                .filter(securityEventType -> !securityEventType.path.equals(path)).findFirst();
    }

    public String getDescription() {
        return description;
    }

    public String getEventName() {
        return eventName;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }
}

