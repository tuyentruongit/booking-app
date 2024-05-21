package methodsecuritynew.bookingapp.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;
    String confirmPassword;
}
