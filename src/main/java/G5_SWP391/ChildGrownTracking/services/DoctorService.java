package G5_SWP391.ChildGrownTracking.services;

import java.util.ArrayList;
import java.util.List;

import G5_SWP391.ChildGrownTracking.repositories.*;
import org.springframework.stereotype.Service;

import G5_SWP391.ChildGrownTracking.dtos.DoctorDTO;
import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.Membership;
import G5_SWP391.ChildGrownTracking.models.MembershipPlan;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.Role;
import G5_SWP391.ChildGrownTracking.responses.DoctorResponse;
import G5_SWP391.ChildGrownTracking.responses.DoctorResponse2;
import G5_SWP391.ChildGrownTracking.responses.SpecResponse;
import G5_SWP391.ChildGrownTracking.responses.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DoctorService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final ChildRepository childRepository;
    private final MembershipRepository membershipRepository;

    public List<DoctorResponse2> getAllDoctor() {
        List<User> doctors = userRepository.findAllByStatusIsTrueAndRole(Role.DOCTOR);
        List<DoctorResponse2> doctorsResponses = new ArrayList<>();

        for (User user : doctors) {
            String planName = null;
            Membership membership = membershipRepository.findByUser(user);
            if (membership != null && membership.isStatus()) {
                MembershipPlan plan = membership.getPlan();
                if (plan != null) {
                    planName = plan.getName();
                }
            } else {
                planName = null;
            }
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    planName,
                    user.getCreatedDate(),
                    user.getUpdateDate(),
                    user.isStatus());

            Doctor doctor = doctorRepository.findByUserId(user.getId());
            Long childCount = childRepository.countByDoctorAndStatusIsTrue(user); // Lấy số lượng trẻ

            // Tạo DoctorResponse2 dùng record
            doctorsResponses.add(new DoctorResponse2(
                    userResponse,
                    doctor.getId(),
                    doctor.getSpecialization(),
                    doctor.getCertificate(),
                    childCount));
        }

        return doctorsResponses;
    }

    public SpecResponse getDoctor(long id) {
        Doctor doctor = doctorRepository.findByUserId(id);
        if (doctor != null)
            return new SpecResponse(doctor.getId(), doctor.getSpecialization(), doctor.getCertificate());
        return null;
    }

    public SpecResponse updateDoctor(Doctor doctor, DoctorDTO doctorDTO) {
        // Update the doctor with values from doctorDTO
        doctor.setSpecialization(doctorDTO.getSpecialization());
        doctor.setCertificate(doctorDTO.getCertificate());
        Doctor updatedDoctor = doctorRepository.save(doctor);

        return new SpecResponse(
                updatedDoctor.getId(),
                updatedDoctor.getSpecialization(),
                updatedDoctor.getCertificate());
    }
}
