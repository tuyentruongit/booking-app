package methodsecuritynew.bookingapp.rest;

import lombok.RequiredArgsConstructor;
import methodsecuritynew.bookingapp.entity.Hotel;
import methodsecuritynew.bookingapp.entity.Support;
import methodsecuritynew.bookingapp.model.request.UpsertSupportRequest;
import methodsecuritynew.bookingapp.model.statics.SupportType;
import methodsecuritynew.bookingapp.service.HotelService;
import methodsecuritynew.bookingapp.service.SupportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportApi {
    private final SupportService supportService;

    //    @GetMapping("/")
//    public ResponseEntity<?> getAll (){
//        List<Support> supportList =  supportService.getAllSupport();
//        return ResponseEntity.ok(supportList);
//    }
    @GetMapping("/{supportType}")
    public ResponseEntity<?> getSupportByType(@PathVariable String supportType) {
        List<Support> supportList = supportService.getSupportByType(supportType);
        return ResponseEntity.ok(supportList);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateSupport(@PathVariable Integer id, @RequestBody UpsertSupportRequest upsertSupportRequest) {
        return ResponseEntity.ok(supportService.updateSupport(id, upsertSupportRequest));
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> updateSupport(@RequestBody UpsertSupportRequest upsertSupportRequest) {
        Support support = supportService.createSupport(upsertSupportRequest);
        return new ResponseEntity<>(support, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> updateSupport(@PathVariable Integer id) {
        supportService.deleteSupport(id);
        return ResponseEntity.noContent().build();
    }

}
