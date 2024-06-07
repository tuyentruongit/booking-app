package methodsecuritynew.bookingapp.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUpdateUserRequest {
    String name;
    String phone;
    String role;
}
