package methodsecuritynew.bookingapp.repository;


import methodsecuritynew.bookingapp.entity.ImageHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ImageHotelRepository  extends JpaRepository<ImageHotel,String> {

    List<ImageHotel> findAllByHotel_Id(Integer id);



}

