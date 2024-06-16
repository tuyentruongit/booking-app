package methodsecuritynew.bookingapp.service;

import lombok.RequiredArgsConstructor;
import methodsecuritynew.bookingapp.entity.Hotel;
import methodsecuritynew.bookingapp.entity.PolicyHotel;
import methodsecuritynew.bookingapp.model.request.UpsertPolicyRequest;
import methodsecuritynew.bookingapp.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyRepository policyRepository;

    public void updatePolicyHotel( UpsertPolicyRequest request , Hotel hotel) {
        PolicyHotel policyHotel = hotel.getPolicyHotel();
        policyHotel.setStartCheckIn(request.getStartCheckIn());
        policyHotel.setStartCheckOut(request.getStartCheckOut());
        policyHotel.setEndCheckIn(request.getEndCheckIn());
        policyHotel.setEndCheckOut(request.getEndCheckOut());
        policyHotel.setService(request.getService());
        policyHotel.setNote(request.getOther());
        policyHotel.setCancelPolicy(request.getCancel());
        policyHotel.setAnimal(request.getAnimal());
        policyHotel.setAgeLimit(request.getAge());
        policyHotel.setUpdatedAt(LocalDate.now());
        
        policyRepository.save(policyHotel);
    }


    public PolicyHotel createPolicyHotel(UpsertPolicyRequest upsertPolicyRequest) {
        PolicyHotel policyHotel = PolicyHotel.builder()
                .startCheckIn(upsertPolicyRequest.getStartCheckIn())
                .startCheckOut(upsertPolicyRequest.getStartCheckOut())
                .endCheckIn(upsertPolicyRequest.getEndCheckIn())
                .endCheckOut(upsertPolicyRequest.getEndCheckOut())
                .service(upsertPolicyRequest.getService())
                .cancelPolicy(upsertPolicyRequest.getCancel())
                .note(upsertPolicyRequest.getOther())
                .animal(upsertPolicyRequest.getAnimal())
                .ageLimit(upsertPolicyRequest.getAge())
                .createdAt(LocalDate.now())
                .build();
        return  policyRepository.save(policyHotel);
    }
}
