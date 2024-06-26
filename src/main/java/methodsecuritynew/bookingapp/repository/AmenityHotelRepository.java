package methodsecuritynew.bookingapp.repository;

import methodsecuritynew.bookingapp.entity.AmenityHotel;
import methodsecuritynew.bookingapp.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmenityHotelRepository extends JpaRepository<AmenityHotel,Integer> {
    AmenityHotel findByName(String name);
}
