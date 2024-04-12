package methodsecuritynew.bookingapp.model.statics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER"),
    ROLE_HOTEL("HOTEL");
    private final String value;
}
