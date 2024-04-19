package methodsecuritynew.bookingapp.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import methodsecuritynew.bookingapp.model.statics.AmenityHotelType;
import methodsecuritynew.bookingapp.model.statics.AmenityRoomType;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class AmenityRoom extends Amenity {

    @Enumerated(EnumType.STRING)
    AmenityRoomType amenityRoomType;


}
