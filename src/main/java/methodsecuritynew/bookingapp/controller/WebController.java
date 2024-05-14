package methodsecuritynew.bookingapp.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import methodsecuritynew.bookingapp.entity.*;
import methodsecuritynew.bookingapp.model.response.VerifyAccountResponse;
import methodsecuritynew.bookingapp.model.statics.Gender;
import methodsecuritynew.bookingapp.model.statics.PaymentMethod;
import methodsecuritynew.bookingapp.model.statics.SupportType;
import methodsecuritynew.bookingapp.repository.BookingRepository;
import methodsecuritynew.bookingapp.service.*;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final CityService cityService;
    private final RoomService roomService;
    private final SupportService supportService;
    private final AuthService authService;
    private final BookingService bookingService;

    private final HotelService hotelService;
    private final HttpSession session;
    private  final BookingRepository bookingRepository;

    @GetMapping("/")
    public String getHome(Model model) {
        List<City> cityList = cityService.findCityFavourite();
        model.addAttribute("cityFavourite", cityList);
        return "web/home";
    }



    @GetMapping("/danh-sach-khach-san")
    public String getHotelList(@RequestParam String nameCity,
                               @RequestParam(required = false) String checkIn,
                               @RequestParam(required = false) String checkOut,
                               @RequestParam(required = false, defaultValue = "1") Integer numberGuest,
                               @RequestParam(required = false, defaultValue = "1") Integer numberRoom,
                               Model model) {

        List<Hotel> hotelList = hotelService.getHotelBySearch(nameCity, checkIn, checkOut, numberGuest, numberRoom);
        if (session.getAttribute("MY_SESSION")!= null) {
            System.out.println(session.getAttribute("MY_SESSION"));
            List<Hotel> hotelFavourite = hotelService.getAllHotelFavourite((String) session.getAttribute("MY_SESSION"));
            System.out.println("dữ liệu"+hotelFavourite);
            model.addAttribute("hotelFavourite", hotelFavourite);
        }

        model.addAttribute("nameCity", nameCity);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("numberGuest", numberGuest);
        model.addAttribute("numberRoom", numberRoom);
        model.addAttribute("hotelList", hotelList);

        return "web/hotel-list";
    }


    @GetMapping("/chi-tiet-khach-san/{id}")
    public String getHotelDetail(@PathVariable Integer id, Model model,
                                 @RequestParam String nameCity,
                                 @RequestParam(required = false) String checkIn,
                                 @RequestParam(required = false) String checkOut,
                                 @RequestParam(required = false, defaultValue = "1") Integer numberGuest,
                                 @RequestParam(required = false, defaultValue = "1") Integer numberRoom) {
        Hotel hotel = hotelService.getHotelById(id);
        List<Room> roomList = roomService.getRoomByIdHotel(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomList", roomList);
        model.addAttribute("nameCity", nameCity);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("numberGuest", numberGuest);
        model.addAttribute("numberRoom", numberRoom);
        return "web/hotel-detail";
    }


    @GetMapping("/support")
    public String getSupport(Model model) {
        List<SupportType> supportTypes = List.of(SupportType.values());

        List<Support> supportList = supportService.getAllSupport();
        model.addAttribute("supportTypes", supportTypes);
        model.addAttribute("supportList", supportList);
        return "web/support";
    }

    @Secured({"ROLE_USER"})
    @GetMapping("/thong-tin-khach-hang")
    public String getProfile(Model model) {
        List<Gender> list = Arrays.stream(Gender.values()).toList();
        model.addAttribute("listGender", list );
        User user = authService.getUserCurrent();
        model.addAttribute("user" , user);
        return "web/profile-user";
    }

    @Secured({"ROLE_USER","ROLE_HOTEL","ROLE_ADMIN"})
    @GetMapping("/yeu-thich/{id}")
    public String getFavouriteHotel(Model model, @PathVariable Integer id ) {
        Map<String,Integer> mapHotelFavouriteByCity = hotelService.getNameCityHotelFavourite(id);
        model.addAttribute("mapHotelFavouriteByCity" , mapHotelFavouriteByCity);
        Boolean authenticated = SecurityContextHolder.getContext().getAuthentication() != null;
        model.addAttribute("authentication" , authenticated);
        return "web/favourite";
    }

    @Secured({"ROLE_USER","ROLE_HOTEL","ROLE_ADMIN"})
    @GetMapping("/danh-sach-dat-phong/{id}")
    public String getHistoryBooking(Model model, @PathVariable Integer id) {
        List<Booking> bookingList = bookingService.getAllBookingByIdUer(id);
        model.addAttribute("bookingList" , bookingList);
        return "web/booking";
    }
    @Secured({"ROLE_USER","ROLE_HOTEL","ROLE_ADMIN"})
    @GetMapping("/cancel-booking/{id}")
    public String getCancelBooking(Model model, @PathVariable Integer id) {
        Booking booking = bookingRepository.findById(id).get();
        model.addAttribute("booking",booking);

        return "web/cancel-booking";
    }
    @Secured({"ROLE_USER","ROLE_HOTEL","ROLE_ADMIN"})
    @GetMapping("/chi-tiet-booking/{id}")
    public String getBookingDetail(Model model, @PathVariable Integer id) {
        Booking booking = bookingService.getBooking(id);
        Locale locale = new Locale("vi","VN");
        String dayOfWeekStar = booking.getCheckIn().getDayOfWeek().getDisplayName(TextStyle.SHORT,locale);
        String dayOfWeekEnd = booking.getCheckOut().getDayOfWeek().getDisplayName(TextStyle.SHORT,locale);

        model.addAttribute("booking" , booking);
        model.addAttribute("dayOfWeekStar" , dayOfWeekStar);
        model.addAttribute("dayOfWeekEnd" , dayOfWeekEnd);

        return "web/booking-detail";
    }

    @Secured({"ROLE_USER","ROLE_HOTEL","ROLE_ADMIN"})
    @GetMapping("/danh-sach-yeu-thich/{id}/{city}")
    public String getFavouriteList(Model model, @PathVariable Integer id ,@PathVariable String city) {
        List<Hotel> hotelList = hotelService.findHotelFavouriteByCity(id,city);
        model.addAttribute("hotelsFavouriteBuCity",hotelList);
        return "web/favourite-list";
    }


    @Secured({"ROLE_USER","ROLE_HOTEL","ROLE_ADMIN"})
    @GetMapping("/thanh-toan/{idHotel}/{idRoom}")
    public String getPayment(@PathVariable Integer idHotel,
                             @PathVariable Integer idRoom,
                             @RequestParam String checkIn,
                             @RequestParam String checkOut,
                             @RequestParam String numberGuest,
                             @RequestParam String numberRoom,
                             Model model) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(checkIn,dateTimeFormatter);
        LocalDate end = LocalDate.parse(checkOut,dateTimeFormatter);
        Hotel hotel = hotelService.getHotelById(idHotel);
        Room room = roomService.getRoomById(idRoom);

        Locale locale = new Locale("vi","VN");


        String startDayOfWeek = start.getDayOfWeek().getDisplayName(TextStyle.SHORT, locale);
        String endDayOfWeek = end.getDayOfWeek().getDisplayName(TextStyle.SHORT, locale);
        Period period = Period.between(start,end);


        model.addAttribute("startDayOfWeek",startDayOfWeek);
        model.addAttribute("listPaymentMethod", PaymentMethod.values());
        model.addAttribute("endDayOfWeek",endDayOfWeek);
        model.addAttribute("start",start);
        model.addAttribute("end",end);
        model.addAttribute("hotel",hotel);
        model.addAttribute("room",room);
        model.addAttribute("period",period);
        model.addAttribute("numberGuest",numberGuest);
        model.addAttribute("numberRoom",numberRoom);
        return "web/thong-tin-thanh-toan";
    }


    @GetMapping("/account/login")
    public String getLogin(Model model) {
        return "web/auth/login";
    }
    @GetMapping("/account/dang-ky")
    public String getRegister(Model model) {
        return "web/auth/register";
    }
    @GetMapping("/account/quen-mat-khau")
    public String getForgotPassword(Model model,@RequestParam(required = false) String token) {
       model.addAttribute("token" ,authService.verifyForgotPassword(token)) ;
        return "web/auth/forgot-password";
    }

    @GetMapping("/account/xac-minh-tai-khoan")
    public String getVerifyAccount(@RequestParam(required = false) String token, Model model) {
        VerifyAccountResponse data = authService.verifyAccount(token);
        model.addAttribute("data" , data);
        return "web/auth/verify-account";
    }



}
