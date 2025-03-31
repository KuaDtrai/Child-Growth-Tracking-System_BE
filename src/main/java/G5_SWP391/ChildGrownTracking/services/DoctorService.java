package G5_SWP391.ChildGrownTracking.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import G5_SWP391.ChildGrownTracking.dtos.DoctorDTO;
import G5_SWP391.ChildGrownTracking.models.Doctor;
import G5_SWP391.ChildGrownTracking.models.User;
import G5_SWP391.ChildGrownTracking.models.role;
import G5_SWP391.ChildGrownTracking.repositories.ChildRepository;
import G5_SWP391.ChildGrownTracking.repositories.DoctorRepository;
import G5_SWP391.ChildGrownTracking.repositories.UserRepository;
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

    public List<DoctorResponse2> getAllDoctor() {
        List<User> doctors = userRepository.findAllByStatusIsTrueAndRole(role.DOCTOR);
        List<DoctorResponse2> doctorsResponses = new ArrayList<>();

        for (User user : doctors) {
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getMembership(),
                    user.getCreatedDate(),
                    user.getUpdateDate(),
                    user.isStatus()
            );

            Doctor doctor = doctorRepository.findByUserId(user.getId());
            Long childCount = childRepository.countByDoctorAndStatusIsTrue(user); // Lấy số lượng trẻ

            // Tạo DoctorResponse2 dùng record
            doctorsResponses.add(new DoctorResponse2(
                    userResponse,
                    doctor.getId(),
                    doctor.getSpecialization(),
                    doctor.getCertificate(),
                    childCount
            ));
        }

        return doctorsResponses;
    }

    public SpecResponse getDoctor(long id) {
        Doctor doctor = doctorRepository.findByUserId(id);
        return new SpecResponse(doctor.getId(), doctor.getSpecialization(), doctor.getCertificate());
    }


    public DoctorResponse addDoctor(User user, DoctorDTO doctor) {
        Doctor newDoctor = new Doctor(
                user,
                doctor.getSpecialization(),
                doctor.getCertificate()
        );
        newDoctor = doctorRepository.save(newDoctor);
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getMembership(),
                user.getCreatedDate(),
                user.getUpdateDate(),
                user.isStatus()
        );
        return new DoctorResponse(newDoctor.getId(), userResponse, newDoctor.getSpecialization(), newDoctor.getCertificate());
    }

    public DoctorResponse updateDoctor(Doctor doctor, DoctorDTO doctorDTO) {
        Doctor newDoctor = new Doctor(
                doctor.getUser(),
                doctor.getSpecialization(),
                doctor.getCertificate()
        );
        User user = doctor.getUser();
        newDoctor = doctorRepository.save(newDoctor);
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getMembership(),
                user.getCreatedDate(),
                user.getUpdateDate(),
                user.isStatus()
        );

        return new DoctorResponse(newDoctor.getId(), userResponse, newDoctor.getSpecialization(), newDoctor.getCertificate());
    }
}
