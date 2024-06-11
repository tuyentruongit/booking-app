package methodsecuritynew.bookingapp.repository;

import methodsecuritynew.bookingapp.entity.User;
import methodsecuritynew.bookingapp.model.dto.RegisterDto;
import methodsecuritynew.bookingapp.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findAllByUserRole (UserRole userRole);

    @Query("SELECT new methodsecuritynew.bookingapp.model.dto.RegisterDto(MONTH(o.createdAt), YEAR(o.createdAt), COUNT(o.id)) " +
            "FROM User o " +
            "WHERE o.enable = true " +
            "GROUP BY MONTH(o.createdAt), YEAR(o.createdAt) " +
            "ORDER BY YEAR(o.createdAt) ASC, MONTH(o.createdAt) ASC")
    List<RegisterDto> findUserRegistrationsByMonthAndYear();

    List<User> findUserByCreatedAtBetweenAndUserRoleOrderByCreatedAtDesc(LocalDate star , LocalDate end , UserRole userRole);
}
